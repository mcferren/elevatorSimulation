package ver3;

/**
 * This is an simple factory class meant to build floor objects so new 
 * Entity_WorkingFloor() is never used anywhere else in the program. It contains 
 * a private constructor so no one can instantiate an object of this class. It
 * also has a public build method so that classes can invoke and build floor objects
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public class Fact_FloorFactory {

    /**
     * This is a private constructor so that noone on the outside can instantiate
     * an object from the class directly
     * 
     * @since Version 1.0
     */
    private Fact_FloorFactory() {
    }
    
    /**
     * This public build method returns an Interface_Floor so that we allow the 
     * invoking class to code to role instead of a class to class relationship.
     * Basically the body of the method creates a new Entity_WorkingFloor
     * 
     * @since Version 1.0
     * @return Interface_Floor so that invoker codes to interface
     */
    public static Interface_Floor build() {
        // will not be a conditional here bc we are only making one 
        // kind of floor
        return new Entity_WorkingFloor();
    }
    
}
