package ver3;
/**
 * This class is a factory to create a new WorkingPerson entity.
 * @author Kevin Newhouse
 * @author Ben McFerren
 */
public class Fact_PersonFactory {
        
    /*
     * This is a private constructor for the Impl Pending Processor Factory.
     * This is designed to be private so a new factory cannot be instantiated.
     * @since Version 1.0
     */
    private Fact_PersonFactory() {
        //private constructor
    }
    
    /**
     * This method build a new WorkingPerson Entity.  It takes integers startFloor and intendedDestination
     * and builds the new person accordingly
     * @param startFloor
     * @param intendedDestination
     * @return 
     * @since Version 1.0
     */
    public static Interface_Person build(int startFloor, int intendedDestination) {
        
        return new Entity_WorkingPerson(startFloor, intendedDestination);
    }
    
}
