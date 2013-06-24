package ver3;

/**
 * This class is a factory to create a new implementation of a PendingProcessor.
 * @author Kevin Newhouse
 * @author Ben McFerren
 * @since Version 1.0
 */
public class Fact_IMPL_PendingProcessor {
    
    /*
     * This is a private constructor for the Impl Pending Processor Factory.
     * This is designed to be private so a new factory cannot be instantiated.
     * @since Version 1.0
     */
    private Fact_IMPL_PendingProcessor() {
        //private constructor
    }
    
    /*
     * This is the build() function to create a new Implementation of the PendingProcessor.
     * It takes the string version as a parameter to determine if it should use the original version or not.
     * @param version
     * @since Version 1.0
     */
    public static Interface_IMPL_PendingProcessor build(String version) {
        
        // conditional here so we know which version to create
        if(version.equals("Original"))
        {
            return new IMPL_PendingProcessor_PROVIDED();
        }
        else
        {
            return new IMPL_PendingProcessor_IMPROVED();
        }
    }
    
}