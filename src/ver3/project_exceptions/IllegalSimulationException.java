package ver3.project_exceptions;

/**
 * This exception class arises from any problems related to the simulation 
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class IllegalSimulationException extends Exception {
    
    public IllegalSimulationException(String s) {
        super(s);
    }
}
