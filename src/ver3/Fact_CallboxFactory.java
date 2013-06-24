package ver3;
/*
 * This is a class that creates a callbox for a floor.
 * @author Kevin Newhouse
 * @author Bejnamin McFerren
 * @since Version 1.0
 */
public class Fact_CallboxFactory {
    
    /*
     * This is the private constructor, it makes sure that this class is not instantiated as "new".
     * @since Verison 1.0
     */
    private Fact_CallboxFactory() {
        //private constructor
    }
    
    /*
     * This method builds the callbox entity given an integer representing the floor that owns it.
     * @param flr
     * @since Version 1.0
     */
    public static Interface_Callbox build(int flr) {
        // will not be a conditional here bc we are only making one 
        // kind of callbox
        return new Entity_WorkingCallbox(flr);
    }
}
