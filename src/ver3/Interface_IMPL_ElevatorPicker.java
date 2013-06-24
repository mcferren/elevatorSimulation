package ver3;

import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalFloorException;


/**
 * This is an interface mostly used as a delegate expert that the controller
 * refers to in order to respond to callbox requests with the best possible
 * elevator
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public interface Interface_IMPL_ElevatorPicker {
    
     /*
     * This method selects which elevator is best suited to fullfill a request 
     * that comes in from a callbox on a particular floor. The parameters included 
     * in the method include the floor number that the callbox is on and the 
     * direction related to the where the request intends to go. It first checks
     * if any elevator is already on the requesting floor. If not, it then checks
     * if there is an elevator already heading in the direction of that floor. If
     * there is not then it checks if there is an idle elevator. If not, it adds
     * the request to the controller's pending list
     * 
     * 
     * @param floor, direction
     * @throws IllegalDirectionException, IllegalFloorException
     * @since Version 1.0
     */
    public void pick(int floor, int direction) 
                        throws IllegalFloorException, IllegalDirectionException;
    
}
