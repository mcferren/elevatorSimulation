package ver3;

/**
 * This is a factory that creates a new Elevator Picker implementation
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class Fact_IMPL_ElevatorPicker {
    
    /*
     * This is a private constructor for the Impl Elevator Picker Factory.
     * This is designed to be private so a new factory cannot be instantiated.
     * @since Version 1.0
     */
    private Fact_IMPL_ElevatorPicker() {
        //private constructor
    }
    
    /*
     * This is the build() function to create a new Implementation of the Elevator Picker.
     * It takes the string parameter version to determine if it should use the original version or not.
     * @param version
     * @since Version 1.0
     */
    public static Interface_IMPL_ElevatorPicker build(String version) {
        
        // conditional here so we know which version to create
        if(version.equals("Original"))
        {
            return new IMPL_ElevatorPicker_PROVIDED();
        }
        else 
        {
            return new IMPL_ElevatorPicker_IMPROVED();
        }
    }
    
}