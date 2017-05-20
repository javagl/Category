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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A builder for {@link Category} hierarchies. This class allows an easy 
 * construction of category hierarchies: The builders are organized 
 * hierarchically. Once the construction is finished, the actual 
 * {@link Category} hierarchy may be built by the root of the builder
 * hierarchy:
 * <pre><code>
 * CategoriesBuilder&lt;Type&gt; b = Categories.createBuilder("Root");
 * b.add(elementForRoot);
 * b.get("FirstChild").add(someElementForFirstChild);
 * b.get("FirstChild").add(someOtherElementForFirstChild);
 * b.get("SecondChild").add(ElementForSecondChild);
 * b.get("SecondChild").get("GrandChild").add(lastElement);
 * Category&lt;Type&gt; root = b.build();
 * </code></pre>
 * 
 * @param <T> The type of elements for the {@link Category}
 */
public final class CategoriesBuilder<T>
{
    /**
     * The name of the root category being built
     */
    private final String name;

    /**
     * The map from names to the builder instances for the
     * sub-categories
     */
    private final Map<String, CategoriesBuilder<T>> subCategories;
    
    /**
     * The elements that are added to this category
     */
    private final Set<T> elements;
    
    /**
     * Creates a new instance of this builder, using the given name
     * for the root category.
     * 
     * @param parent The parent category
     * @param name The name of the root category
     */
    CategoriesBuilder(String name)
    {
        this.name = Objects.requireNonNull(name, "The name may not be null");
        this.subCategories = new LinkedHashMap<String, CategoriesBuilder<T>>();
        this.elements = new LinkedHashSet<T>();
    }
    
    /**
     * Add the given element to the category represented by this builder.
     * 
     * @param element The element to add
     * @return This builder
     */
    public CategoriesBuilder<T> add(T element)
    {
        elements.add(element);
        return this;
    }
    
    /**
     * Add the given elements to the category represented by this builder.
     * 
     * @param elements The elements to add
     * @return This builder
     */
    public CategoriesBuilder<T> addAll(Iterable<? extends T> elements)
    {
        if (elements != null)
        {
            for (T element : elements)
            {
                this.elements.add(element);
            }
        }
        return this;
    }
    
    /**
     * Returns a builder for the sub-category with the given name.
     * If the respective sub-category does not exist, it will be
     * created. 
     * 
     * @param name The name of the sub-category
     * @return The builder for the sub-category
     */
    public CategoriesBuilder<T> get(String name)
    {
        Objects.requireNonNull(name, "The name may not be null");
        CategoriesBuilder<T> result = subCategories.get(name);
        if (result == null)
        {
            result = new CategoriesBuilder<T>(name);
            subCategories.put(name, result);
        }
        return result;
    }
    
    /**
     * Returns an unmodifiable set containing elements that are contained 
     * in the {@link Category} that is about to be constructed with this 
     * builder, or in any of its sub-categories. Changes in this builder
     * will not affect the returned set.
     * 
     * @return The set containing all elements
     */
    private Set<T> getAllElements()
    {
        Set<T> allElements = new LinkedHashSet<T>();
        getAllElements(allElements);
        return Collections.unmodifiableSet(allElements);
    }
    
    /**
     * Recursively adds all elements of this builder
     * and its descendants to the given collection.
     * 
     * @param allElements The collection to add the elements to
     */
    private void getAllElements(Collection<? super T> allElements)
    {
        allElements.addAll(elements);
        for (CategoriesBuilder<T> subBuilder : subCategories.values())
        {
            subBuilder.getAllElements(allElements);
        }
    }
    
    /**
     * Merges the given {@link Category} with the {@link Category} that is 
     * currently being built. This merge will be performed recursively 
     * for all sub-categories. 
     * 
     * @param other The other {@link Category}
     * @return This builder 
     */
    public CategoriesBuilder<T> mergeRecursively(Category<? extends T> other)
    {
        get(other.getName()).addAll(other.getElements());
        for (Category<? extends T> otherChild : other.getChildren())
        {
            get(other.getName()).mergeRecursively(otherChild);
        }
        return this;
    }
    
    /**
     * Add each of the given elements to a category with the given name
     * <i>iff</i> it is <b>not</b> already contained in the current
     * category or any of its children.
     * 
     * @param name The category name
     * @param candidates The candidates to add, if they are not yet
     * contained somewhere in the category hierarchy
     * @return This builder
     */
    public CategoriesBuilder<T> addIfUncategorized(
        String name, Iterable<? extends T> candidates)
    {
        Objects.requireNonNull(name, "The name may not be null");
        Set<T> available = new LinkedHashSet<T>();
        for (T candidate : candidates)
        {
            available.add(candidate);
        }
        available.removeAll(getAllElements());
        for (T element : available)
        {
            get(name).add(element);
        }
        return this;
        
    }
    
    /**
     * Build the {@link Category} according to the current state of 
     * this builder. 
     * 
     * @return The {@link Category}
     */
    public MutableCategory<T> build()
    {
        DefaultCategory<T> result = new DefaultCategory<T>(name);
        result.addElements(elements);
        if (subCategories != null)
        {
            for (CategoriesBuilder<T> subBuilder : subCategories.values())
            {
                MutableCategory<T> child = subBuilder.build();
                result.addChild(child);
            }
        }
        return result;
    }
    
     
}
