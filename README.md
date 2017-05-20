# Category

A simple model for hierarchical categories. Basically, an observable tree 
with a fancy name...

The `CategoriesBuilder` class may be used to create `Category` instances:

    CategoriesBuilder<Integer> b = Categories.createBuilder("Root");
    b.addAll(Arrays.asList(0,1,2));
    b.get("ChildA").add(10);
    b.get("ChildA").add(11);
    b.get("ChildA").get("ChildA0").add(100);
    b.get("ChildA").get("ChildA1").add(101);
    b.get("ChildB").add(20);
    b.get("ChildB").add(21);
    b.get("ChildB").get("ChildB0").add(200);
    b.get("ChildB").get("ChildB1").add(201);
    Category<Integer> category = b.build();
    
This will create the following category hierarchy:

    Root
    |-0
    |-1
    |-2
    +-ChildA
    | |-10
    | |-11
    | +-ChildA0
    | | |-100
    | +-ChildA1
    |   |-101
    +-ChildB
      |-20
      |-21
      +-ChildB0
      | |-200
      +-ChildB1
        |-201    


Alternatively, it is possible to directly create a `MutableCategory` 
and modify it directly:

    MutableCategory<Integer> category = Categories.create("Root");
    category.addElements(Arrays.asList(0,1,2));
    MutableCategory<Integer> childA = category.addChild("ChildA");
    childA.addElements(Arrays.asList(3,4,5));


A `CategoryListener` may be attached to a `Category`, to be informed
about changes in the category, or in any of its children: 

    MutableCategory<Integer> category = Categories.create("Root");
    category.addCategoryListener(categoryListener);
    
    ...
    // This will inform the listener that was attached to the root:
    category.getChild("ChildA").addElements(Arrays.asList(3,4,5));

    
    
    