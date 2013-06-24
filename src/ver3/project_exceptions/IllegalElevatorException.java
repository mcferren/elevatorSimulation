package ver3.project_exceptions;

/**
 * This exception class arises from any problems related to the Elevator
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class IllegalElevatorException extends Exception {
    
    public IllegalElevatorException(String s) {
        super(s);
    }
}
