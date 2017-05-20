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

import java.util.Collections;
import java.util.EventObject;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A class describing a change in a {@link Category}
 *
 * @param <T> The type of the elements in the {@link Category}
 */
public final class CategoryEvent<T> extends EventObject
{
    /**
     * The {@link Category} from which this event originated
     */
    private final Category<T> category; 
    
    /**
     * The elements that have been added to or removed from the {@link Category}
     */
    private final Set<T> elements;
    
    /**
     * The child that was added to or removed from the {@link Category}
     */
    private final Category<T> child;
    
    /**
     * Creates a new event that describes a change in the given 
     * {@link Category}. It will store a copy of the given sequence of
     * elements (or an empty set, if the given sequence is <code>null</code>)
     * 
     * @param category The {@link Category} from which this event originated
     * @param elements The elements that have been added or removed
     * @param child The child that was added or removed
     */
    CategoryEvent(Category<T> category, Iterable<? extends T> elements, 
        Category<T> child)
    {
        super(category);
        this.category = category;
        if (elements == null)
        {
            this.elements = Collections.emptySet();
        }
        else
        {
            Set<T> e = new LinkedHashSet<T>();
            for (T element : elements)
            {
                e.add(element);
            }
            this.elements = Collections.unmodifiableSet(e);
        }
        this.child = child;
    }
    
    /**
     * Returns the {@link Category} from which this event originated
     * 
     * @return The {@link Category} from which this event originated
     */
    public Category<T> getCategory()
    {
        return category;
    }
    
    /**
     * Returns an unmodifiable set containing the elements that have been
     * added or removed
     * 
     * @return The elements that was added or removed
     */
    public Set<T> getElements()
    {
        return elements;
    }
    
    /**
     * Returns the child that was added or removed
     * 
     * @return The child that was added or removed
     */
    public Category<T> getChild()
    {
        return child;
    }
    
    @Override
    public String toString()
    {
        return "CategoryEvent["+
            "category="+category+","+
            "elements="+elements+","+
            "child="+child+"]";
    }
}
