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

import java.util.List;

/**
 * Interface for a mutable {@link Category}
 *
 * @param <T> The type of elements in this category
 */
public interface MutableCategory<T> extends Category<T>
{
    /**
     * Add a new child {@link Category} with the given name and
     * return it. If such a child already exists, it will be
     * returned.
     * 
     * @param name The name of the child {@link Category}
     * @return The child {@link Category} with the given name
     */
    MutableCategory<T> addChild(String name);
    
    /**
     * Remove the child {@link Category} with the given name. 
     * 
     * @param name The name of the child {@link Category} to remove
     * @return The removed child {@link Category}, or <code>null</code>
     * if there was not child with the given name
     */
    MutableCategory<T> removeChild(String name);

    /**
     * Remove all children from this {@link Category}. This will
     * not cause any elements to be removed.
     */
    void removeAllChildren();
    
    /**
     * Add the given elements to this {@link Category}
     * 
     * @param elements The element to add
     * @return Whether this {@link Category} changed through this
     * action (that is, whether at least one of the elements was 
     * not yet present)
     */
    boolean addElements(Iterable<? extends T> elements);
    
    /**
     * Remove the given elements from this {@link Category}
     * 
     * @param elements The elements to remove
     * @return Whether this {@link Category} changed through this
     * action (that is, whether at least one of the elements was 
     * previously contained in this {@link Category})
     */
    boolean removeElements(Iterable<? extends T> elements);
    
    /**
     * Remove all elements from this {@link Category}. This will
     * not cause any children to be removed.
     */
    void removeAllElements();
    
    /**
     * {@inheritDoc}
     * 
     * This method specifies the return type to be a {@link MutableCategory}
     */
    @Override
    List<? extends MutableCategory<T>> getChildren();
    
    /**
     * {@inheritDoc}
     * 
     * This method specifies the return type to be a {@link MutableCategory}
     */
    @Override
    MutableCategory<T> getChild(String name);
    
}
