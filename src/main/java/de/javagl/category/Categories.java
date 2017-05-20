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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Methods related to {@link Category} instances
 */
public class Categories
{
    /**
     * Creates a new instance of a {@link CategoriesBuilder}, 
     * using the given name for the root category.
     * 
     * @param <T> The type of the elements in the {@link Category}
     *  
     * @param name The name of the root {@link Category}
     * @return The {@link CategoriesBuilder} instance
     */
    public static <T> CategoriesBuilder<T> createBuilder(String name)
    {
        return new CategoriesBuilder<T>(name);
    }
    
    /**
     * Create a new {@link MutableCategory} with the given name
     * 
     * @param <T> The type of the elements in the {@link Category}
     * 
     * @param name The name of the {@link Category}
     * @return The new {@link MutableCategory}
     */
    public static <T> MutableCategory<T> create(String name)
    {
        return new DefaultCategory<T>(name);
    }

    
    
    /**
     * Returns a new set containing all elements of the given {@link Category}
     * and its children.
     * 
     * @param <T> The type of the elements in the {@link Category}
     * 
     * @param category The category to collect all elements from
     * @return The set of all elements of the given category and its children
     */
    public static <T> Set<T> getAllElements(Category<T> category)
    {
        Set<T> result = new LinkedHashSet<T>();
        getAllElements(category, result);
        return result;
    }
    
    /**
     * Add all elements of the given category and its children to the
     * given result set, recursively
     * 
     * @param <T> The type of the elements in the {@link Category}
     * @param category The category to collect all elements from
     * @param result The resulting set storing the elements
     */
    private static <T> void getAllElements(Category<T> category, Set<T> result)
    {
        result.addAll(category.getElements());
        for (Category<T> child : category.getChildren())
        {
            getAllElements(child, result);
        }
    }
    
    /**
     * Clean up the given {@link Category}. That is, this method will 
     * recursively remove all categories from the given hierarchy that 
     * have neither {@link Category#getChildren() children} nor 
     * {@link Category#getElements() elements}. 
     *  
     * @param category The {@link Category} to clean up
     */
    public static void removeEmptyCategories(MutableCategory<?> category)
    {
        List<? extends MutableCategory<?>> children = category.getChildren();
        for (MutableCategory<?> child : children)
        {
            removeEmptyCategories(child);
        }
        for (MutableCategory<?> child : children)
        {
            if (child.getChildren().isEmpty() && 
                child.getElements().isEmpty())
            {
                category.removeChild(child.getName());
            }
        }
    }
    
    /**
     * Creates a formatted, multi-line string representation of the given 
     * {@link Category}. <br>
     * <br>
     * <b>The exact format of this string is unspecified!</b><br>
     * <br>
     * This method is mainly intended for debugging.
     * 
     * @param category The {@link Category}
     * @return The string representation
     */
    public static String toFormattedString(Category<?> category)
    {
        return toFormattedString(category, "");
    }
    
    /**
     * Recursive method for the formatted string creation
     * 
     * @param category The {@link Category}
     * @param indent The indentation string
     * @return The string
     */
    private static String toFormattedString(
        Category<?> category, String indent)
    {
        if (category == null)
        {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        String i = indent;
        if (indent.length() >= 2)
        {
            i = indent.substring(0, indent.length() - 2) + "+-";
        }
        sb.append(i + category.getName()).append("\n");
        if (!category.getElements().isEmpty())
        {
            for (Object element : category.getElements())
            {
                sb.append(indent + "|-" + element).append("\n");
            }
        }
        int numChildren = category.getChildren().size();
        if (numChildren > 0)
        {
            int counter = 0;
            for (Category<?> child : category.getChildren())
            {
                if (counter == numChildren - 1)
                {
                    sb.append(toFormattedString(child, indent + "  "));
                }
                else
                {
                    sb.append(toFormattedString(child, indent + "| "));
                }
                counter++;
            }
        }
        return sb.toString();
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Categories()
    {
        // Private constructor to prevent instantiation
    }
}
