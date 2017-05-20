/*
 * www.javagl.de - Category
 *
 * Copyright (c) 2012-2017 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package de.javagl.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Default implementation of a {@link MutableCategory}
 * 
 * @param <T> The type of the elements in this {@link Category}
 */
final class DefaultCategory<T> implements MutableCategory<T>
{
    /**
     * The name of this category
     */
    private final String name;
    
    /**
     * The list of children of this category
     */
    private final List<MutableCategory<T>> children;
    
    /**
     * The elements in this category
     */
    private final List<T> elements;
    
    /**
     * The listeners that are attached to this category
     */
    private final List<CategoryListener<T>> categoryListeners;
    
    /**
     * A {@link CategoryListener} that will forward all events from child
     * categories to the listeners that are attached to
     * this category.
     */
    private final CategoryListener<T> forwardingListener = 
        new CategoryListener<T>()
    {
        @Override
        public void elementsAdded(CategoryEvent<T> event)
        {
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.elementsAdded(event);
            }
        }

        @Override
        public void elementsRemoved(CategoryEvent<T> event)
        {
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.elementsRemoved(event);
            }
        }

        @Override
        public void childAdded(CategoryEvent<T> event)
        {
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.childAdded(event);
            }
        }

        @Override
        public void childRemoved(CategoryEvent<T> event)
        {
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.childRemoved(event);
            }
        }
    };
    
    
    /**
     * Creates a new category. 
     * 
     * @param name The name of the category
     */
    DefaultCategory(String name)
    {
        this.name = Objects.requireNonNull(name, "The name may not be null");
        this.children = new ArrayList<MutableCategory<T>>();
        this.categoryListeners = 
            new CopyOnWriteArrayList<CategoryListener<T>>();
        this.elements = new ArrayList<T>();
    }
    
    /**
     * Package-private method to add a child category. This method will not
     * cause an event to be fired.
     * 
     * @param child The child category
     */
    void addChild(MutableCategory<T> child)
    {
        Objects.requireNonNull(child, "The child may not be null");
        this.children.add(child);
        child.addCategoryListener(forwardingListener);
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public MutableCategory<T> addChild(String name)
    {
        Objects.requireNonNull(name, "The name may not be null");
        MutableCategory<T> present = getChild(name);
        if (present != null)
        {
            return present;
        }
        MutableCategory<T> child = new DefaultCategory<T>(name);
        addChild(child);
        fireChildAdded(child);
        return child;
    }
    

    @Override
    public MutableCategory<T> removeChild(String name)
    {
        MutableCategory<T> removedChild = getChild(name);
        if (removedChild != null)
        {
            children.remove(removedChild);
            removedChild.removeCategoryListener(forwardingListener);
            fireChildRemoved(removedChild);
        }
        return removedChild;
    }
    
    @Override
    public void removeAllChildren()
    {
        List<MutableCategory<T>> removedChildren = getChildren();
        for (MutableCategory<T> child : removedChildren)
        {
            removeChild(child.getName());
        }
    }

    @Override
    public MutableCategory<T> getChild(String name)
    {
        for (MutableCategory<T> child : children)
        {
            if (child.getName().equals(name))
            {
                return child;
            }
        }
        return null;
    }
    
    @Override
    public List<MutableCategory<T>> getChildren()
    {
        return Collections.unmodifiableList(
            new ArrayList<MutableCategory<T>>(children));
    }

    @Override
    public boolean addElements(Iterable<? extends T> elements)
    {
        boolean changed = false;
        if (elements != null)
        {
            for (T element : elements)
            {
                changed |= this.elements.add(element);
            }
            if (changed)
            {
                fireElementsAdded(elements);
            }
        }
        return changed;
    }

    @Override
    public boolean removeElements(Iterable<? extends T> elements)
    {
        boolean changed = false; 
        if (elements != null)
        {
            for (T element : elements)
            {
                changed |= this.elements.remove(element);
            }
            if (changed)
            {
                fireElementsRemoved(elements);
            }
        }
        return changed;
    }
    
    @Override
    public void removeAllElements()
    {
        removeElements(getElements());
    }

    @Override
    public List<T> getElements()
    {
        return Collections.unmodifiableList(new ArrayList<T>(elements));
    }
    
    /**
     * Notify each registered {@link CategoryListener} that the 
     * given child was added
     * 
     * @param child The child that was added
     */
    private void fireChildAdded(Category<T> child)
    {
        if (!categoryListeners.isEmpty())
        {
            CategoryEvent<T> categoryEvent = 
                new CategoryEvent<T>(this, null, child);
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.childAdded(categoryEvent);
            }
        }
    }

    /**
     * Notify each registered {@link CategoryListener} that the 
     * given child was removed
     * 
     * @param child The child that was removed
     */
    private void fireChildRemoved(Category<T> child)
    {
        if (!categoryListeners.isEmpty())
        {
            CategoryEvent<T> categoryEvent = 
                new CategoryEvent<T>(this, null, child);
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.childRemoved(categoryEvent);
            }
        }
    }

    /**
     * Notify each registered {@link CategoryListener} that the 
     * given elements have been added
     * 
     * @param elements The element that have been added
     */
    private void fireElementsAdded(Iterable<? extends T> elements)
    {
        if (!categoryListeners.isEmpty())
        {
            CategoryEvent<T> categoryEvent = 
                new CategoryEvent<T>(this, elements, null);
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.elementsAdded(categoryEvent);
            }
        }
    }

    /**
     * Notify each registered {@link CategoryListener} that the 
     * given elements have been removed
     * 
     * @param elements The elements that have been removed
     */
    private void fireElementsRemoved(Iterable<? extends T> elements)
    {
        if (!categoryListeners.isEmpty())
        {
            CategoryEvent<T> categoryEvent = 
                new CategoryEvent<T>(this, elements, null);
            for (CategoryListener<T> listener : categoryListeners)
            {
                listener.elementsRemoved(categoryEvent);
            }
        }
    }
    
    @Override
    public void addCategoryListener(CategoryListener<T> listener)
    {
        categoryListeners.add(listener);
    }

    @Override
    public void removeCategoryListener(CategoryListener<T> listener)
    {
        categoryListeners.remove(listener);
    }


    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, children, elements);
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null)
        {
            return false;
        }
        if (!(object instanceof Category))
        {
            return false;
        }
        Category<?> other = (Category<?>) object;
        
        if (!Objects.equals(name, other.getName()))
        {
            return false;
        }
        if (!Objects.equals(children, other.getChildren()))
        {
            return false;
        }
        if (!Objects.equals(elements, other.getElements()))
        {
            return false;
        }
        return true;
    }
    
}
