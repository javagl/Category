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

import java.util.EventListener;

/**
 * Interface for classes that want to be informed about changes in
 * a {@link Category}.<br>
 * <br>
 * Note that when such a listener is added to a {@link Category},
 * it will receive events for changes in the {@link Category} that
 * it was added to, as well as changes in one of the
 * {@link Category#getChildren() children} of this category. 
 * The {@link CategoryEvent} that is passed to this listener will
 * contain the category where the change took place. This is not 
 * necessarily the same as the one that the listener was added to. 
 *
 * @param <T> The type of the elements in the {@link Category}
 */
public interface CategoryListener<T> extends EventListener
{
    /**
     * Will be called when elements have been added to a {@link Category}
     * 
     * @param event The event describing the change
     */
    void elementsAdded(CategoryEvent<T> event);

    /**
     * Will be called when elements have been removed from a {@link Category}
     * 
     * @param event The event describing the change
     */
    void elementsRemoved(CategoryEvent<T> event);

    /**
     * Will be called when a child was added to a {@link Category}
     * 
     * @param event The event describing the change
     */
    void childAdded(CategoryEvent<T> event);

    /**
     * Will be called when a child was removed from a {@link Category}
     * 
     * @param event The event describing the change
     */
    void childRemoved(CategoryEvent<T> event);
}
