package ver3;

import java.text.SimpleDateFormat;
import java.util.List;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a class that represents a expert that the controller relies upon.
 * The purpose is to provide a method (pick) that is responsible for selecting
 * the best possible active elevator to fulfill callbox requests
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class IMPL_ElevatorPicker_PROVIDED implements Interface_IMPL_ElevatorPicker {
    
    /**
     * This variable SimpleDateFormat simply sets the format of our date and time
     * for tracking purposes.
     * @since Version 1.0
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    
    /**
     * This is the public constructor. It takes no parameters and simply acts as a stub
     * @since Version 1.0
     */
    public IMPL_ElevatorPicker_PROVIDED() {
        
    }
    
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
                        throws IllegalFloorException, IllegalDirectionException {
        
        if(!(direction == -1 || direction  == 1))
            throw new IllegalDirectionException("Out of bounds direction encountered"
                    + " while delegate is picking: " + direction);
        
        if(floor < 0)
            throw new IllegalFloorException("Negative number of floors encountered: " + floor);
               
        System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller is being asked to send an elevator to "
                + "floor " + floor + " so it can go " 
                + (direction == 1 ? "up" : "down") + " from there");
        
        List<Interface_Elevator> elevatorList 
                = Singl_WorkingBuilding.getInstance().getElevatorList();
        int backup = 0;
        int sameDirectionElevator = 0;
        int alreadyOnFloorElevator = 0;
        
            
        for(int i = 0; i < elevatorList.size(); ++i)
        {
            
            // if there's already an elevator on that floor, simply arrive
            if(floor == elevatorList.get(i).getCurrentFloor() 
                                && elevatorList.get(i).getDoorsAreOpen() == true
                                && (direction == elevatorList.get(i).getDirection() 
                                    || elevatorList.get(i).getDirection() == 0))
            {
                alreadyOnFloorElevator = elevatorList.get(i).getSerialNumber();
                
                try {
                    elevatorList.get(alreadyOnFloorElevator - 1)
                            .setDirection(direction);
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                }
                
                break;
            }
            
            // finding elevator that is going in the same direction
            if((elevatorList.get(i).getDirection() == -1 
                    && floor <= elevatorList.get(i).getCurrentFloor() 
                    && direction == -1)
                || (elevatorList.get(i).getDirection() == 1 
                    && floor >= elevatorList.get(i).getCurrentFloor() 
                    && direction == 1)
              )   
            {
                sameDirectionElevator = elevatorList.get(i)
                                                .getSerialNumber();
            }
            
            // backup plan if after I iterate through loop, 
            // I find no directional choices
            if(elevatorList.get(i).getDirection() == 0)
            {
                backup = elevatorList.get(i).getSerialNumber();
            }
        }
        
        if(alreadyOnFloorElevator != 0)
        {
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller has found that elevator "
                + elevatorList.get(alreadyOnFloorElevator - 1).getLetterName() 
                + elevatorList.get(alreadyOnFloorElevator - 1).getSerialNumber()
                + " is already on floor #" 
                + elevatorList.get(alreadyOnFloorElevator - 1).getCurrentFloor()
                + ". So it can satisfy\n\t\tthe request to go " 
                + (direction == 1 ? "up" : "down") + " from floor #" + floor);
            
            Singl_WorkingController.getInstance().disPatchElevator(
                                                        floor, 
                                                        alreadyOnFloorElevator);
        }
        else if(sameDirectionElevator != 0)
        {
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller has selected elevator "
                + elevatorList.get(sameDirectionElevator - 1).getLetterName() 
                + elevatorList.get(sameDirectionElevator - 1).getSerialNumber()
                + " to satisfy the " + (direction == 1 ? "up" : "down") 
                + " request from floor #" + floor 
                + "\n\t\tbecause it is currently on floor " 
                + elevatorList.get(sameDirectionElevator - 1).getCurrentFloor()
                + " and it is " 
                + (elevatorList.get(sameDirectionElevator - 1)
                        .getDirection()== 1 ? "heading up" : 
                    (elevatorList.get(sameDirectionElevator - 1)
                            .getDirection()== 0 ? "idle" : "heading down")
                   )
            );
            System.out.println(sdf.format(System.currentTimeMillis()) 
                    + "\tSending Elevator " 
                    + elevatorList.get(sameDirectionElevator - 1).getLetterName() 
                    + elevatorList.get(sameDirectionElevator - 1).getSerialNumber() 
                    + " to Floor " + floor + "…");
            
            Singl_WorkingController.getInstance().disPatchElevator(
                                                        floor, 
                                                        sameDirectionElevator);
        }
        else if(backup != 0)
        {
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller has selected elevator "
                + elevatorList.get(backup - 1).getLetterName() 
                + elevatorList.get(backup - 1).getSerialNumber()
                + " to satisfy the " + (direction == 1 ? "up" : "down") 
                + " request from floor #" + floor + "\n\t\tbecause there is no "
                + "elevator currently going in the direction of floor #" + floor
                + "\n\t\tand elevator " + elevatorList.get(backup - 1).getLetterName() 
                + elevatorList.get(backup - 1).getSerialNumber() + " was idle at "
                + "the time the request was received");
            
            System.out.println(sdf.format(System.currentTimeMillis()) 
                    + "\tSending Elevator " 
                    + elevatorList.get(backup - 1).getLetterName() 
                    + elevatorList.get(backup - 1).getSerialNumber() 
                    + " to Floor " + floor + "…");
            
            Singl_WorkingController.getInstance().disPatchElevator(floor, backup);
        }
        else
        {
            System.out.println(sdf.format(System.currentTimeMillis()) 
                    + "The controller could not find an elevator that is heading"
                    + " in the direction of that floor \n\t\twhile also planning"
                    + " to continue in the direction of the request. It tried "
                    + "to\n\t\tfind one standing idle but there wasn't one of "
                    + "those either.\n\t\tSo it is sending the request to a "
                    + "pending list.");
            
            System.out.println(sdf.format(System.currentTimeMillis()) + "\tAdding "
                    + "floor " + floor + " to the list of pending requests, "
                    + "specifically marked to go " + (direction == 1 ? "up" : "down"));
            
            Singl_WorkingController.getInstance()
                                        .addToPendingList(floor, direction);
        }
    }
    
}
