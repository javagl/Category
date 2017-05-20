package de.javagl.category;

import java.util.ArrayList;
import java.util.List;

/**
 * A listener for the unit tests
 */
@SuppressWarnings("javadoc")
class CollectingCategoryListener implements CategoryListener<Object>
{
    List<CategoryEvent<Object>> elementsAddedEvents = 
        new ArrayList<CategoryEvent<Object>>();
    List<CategoryEvent<Object>> elementsRemovedEvents = 
        new ArrayList<CategoryEvent<Object>>();
    List<CategoryEvent<Object>> childAddedEvents = 
        new ArrayList<CategoryEvent<Object>>();
    List<CategoryEvent<Object>> childRemovedEvents = 
        new ArrayList<CategoryEvent<Object>>();
    
    @Override
    public void elementsRemoved(CategoryEvent<Object> event)
    {
        elementsRemovedEvents.add(event);
    }
    
    @Override
    public void elementsAdded(CategoryEvent<Object> event)
    {
        elementsAddedEvents.add(event);
    }
    
    @Override
    public void childRemoved(CategoryEvent<Object> event)
    {
        childRemovedEvents.add(event);
    }
    
    @Override
    public void childAdded(CategoryEvent<Object> event)
    {
        childAddedEvents.add(event);
    }
}