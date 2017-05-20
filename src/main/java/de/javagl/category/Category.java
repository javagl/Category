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
 * Interface for a category that contains elements and is part of a 
 * hierarchy. Category hierarchies can be constructed using a 
 * {@link CategoriesBuilder} or using the methods from the
 * {@link Categories} class. <br>
 * 
 * @param <T> The type of elements in this category
 */
public interface Category<T>
{
    /**
     * Returns the name of this category. This should be a short, 
     * human-readable string summarizing the purpose of all 
     * elements that are contained in this category.
     * 
     * @return The name of this category
     */
    String getName();
    
    /**
     * Returns an unmodifiable (possibly empty) list containing the 
     * children of this category. Changes in this category
     * will not affect the returned list.
     * 
     * @return The children of this category
     */
    List<? extends Category<T>> getChildren();
    
    /**
     * Returns the child category with the given name, or <code>null</code>
     * if there is no child with the given name. 
     * 
     * @param name The name of the child category
     * @return The child category, or <code>null</code>
     */
    Category<T> getChild(String name);
    
    /**
     * Returns an unmodifiable (possibly empty) list containing the
     * elements that belong to this category. Changes in this category
     * will not affect the returned list.
     * 
     * @return The elements of this category
     */
    List<T> getElements();
    
    /**
     * Add the given {@link CategoryListener} to be informed about
     * changes in this category <b>or any of its children.</b>
     * 
     * @param listener The {@link CategoryListener} to add
     */
    void addCategoryListener(CategoryListener<T> listener);

    /**
     * Remove the given {@link CategoryListener} from this category
     * 
     * @param listener The {@link CategoryListener} to remove
     */
    void removeCategoryListener(CategoryListener<T> listener);
}
