package ver3;

import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is an interface mostly used as a delegate expert that the controller
 * refers to in order to respond to an elevator when it asks for pending
 * requests
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public interface Interface_IMPL_PendingProcessor {
         
    /*
     * This method is responsible for delivering the appropriate floors from 
     * the pending list to an elevator that is making a request. It first makes
     * sure that there are items in the pending list. If so, it examines each
     * floor on the pending list one at a time. For each iteration, it finds the
     * farthest elevaotor heading it that direction. It then finds all pending
     * requests in between that farthest floor and the selected elevator. It then
     * assigns the pending request to the elevator's destination lsit, then it 
     * removes the floor from the controller's pending list.
     * 
     * @param floor, direction
     * @throws IllegalDirectionException, IllegalFloorException, IllegalElevatorException
     * @since Version 1.0
     */
    public void deliverPendingList(int requestingFloor, int requestingElevatorNumber)
                        throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException;
    
}
