package de.javagl.category;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@SuppressWarnings("javadoc")
@RunWith(JUnit4.class)
public class TestCategory
{
    @Test(expected=NullPointerException.class)
    public void testNullNameThrows() 
    {
        Categories.create(null);
    }
    @Test(expected=NullPointerException.class)
    public void testNullChildNameThrows() 
    {
        MutableCategory<Object> category = Categories.create("Root");
        category.addChild(null);
    }
    
    @Test
    public void testElements() 
    {
        MutableCategory<Object> category = Categories.create("Root");
        CollectingCategoryListener listener = new CollectingCategoryListener();
        category.addCategoryListener(listener);
        
        category.addElements(Arrays.asList(0,1,2,3));
        assertEquals(1, listener.elementsAddedEvents.size());
        
        category.removeElements(Arrays.asList(1,2));
        assertEquals(1, listener.elementsRemovedEvents.size());
        
        assertEquals(Arrays.asList(0,3), category.getElements());
    }
    
    @Test
    public void testChildren() 
    {
        MutableCategory<Object> category = Categories.create("Root");
        CollectingCategoryListener listener = new CollectingCategoryListener();
        category.addCategoryListener(listener);
        
        category.addChild("ChildA");
        assertEquals(1, listener.childAddedEvents.size());

        MutableCategory<Object> childB = category.addChild("ChildB");
        assertEquals(2, listener.childAddedEvents.size());

        category.removeChild("ChildA");
        assertEquals(1, listener.childRemovedEvents.size());
        
        assertEquals(Arrays.asList(childB), category.getChildren());
    }
    
    @Test
    public void testDeepListening() 
    {
        MutableCategory<Object> category = Categories.create("Root");
        CollectingCategoryListener listener = new CollectingCategoryListener();
        category.addCategoryListener(listener);
        
        MutableCategory<Object> childA = category.addChild("ChildA");
        assertEquals(1, listener.childAddedEvents.size());

        MutableCategory<Object> childA0 = childA.addChild("ChildA0");
        MutableCategory<Object> childA1 = childA.addChild("ChildA1");
        assertEquals(3, listener.childAddedEvents.size());

        childA0.addElements(Arrays.asList(0,1));
        assertEquals(1, listener.elementsAddedEvents.size());
        
        childA1.addElements(Arrays.asList(0,1));
        assertEquals(2, listener.elementsAddedEvents.size());
    }
    
    

    
    
}
