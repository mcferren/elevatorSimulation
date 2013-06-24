package ver3;

import java.text.SimpleDateFormat;
import java.util.List;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a class that represents an expert that the controller relies upon.
 * The purpose is to provide a method (pick) that is responsible for selecting
 * the best possible active elevator to fulfill callbox requests
 * 
 * Improvements over the provided delegate include:
 * 1. When deciding which elevator to selects, this improved delegate also considers 
 *    the average amount of destinations among all elevators. It selects those that
 *    are below the average it order to keep the elevators evenly dispersed.
 * 2. The elevators are set up to service first pressed, first served relative to 
 *    callboxes. So the improved delegate checks if there is not already a lit 
 *    callbox button in a direction different than the request we are examining
 *    so requests dont get lost (and have to be checked up upon shutdown
 * 
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class IMPL_ElevatorPicker_IMPROVED implements Interface_IMPL_ElevatorPicker {
    
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
    public IMPL_ElevatorPicker_IMPROVED() {
        
    }
    
    /**
     * This method selects which elevator is best suited to fullfill a request 
     * that comes in from a callbox on a particular floor. The parameters included 
     * in the method include the floor number that the callbox is on and the 
     * direction related to the where the request intends to go. It first checks
     * if any elevator is already on the requesting floor. If not, it then checks
     * if there is an elevator already heading in the direction of that floor. If
     * there is not then it checks if there is an idle elevator. If not, it adds
     * the request to the controller's pending list
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
        
        if(floor > Singl_WorkingSimulation.getInstance().getNumberOfFloors()+1)
            throw new IllegalFloorException("Floor does not exist (too high): " + floor);
                    
        System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller is being asked to send an elevator to "
                + "floor " + floor + " so it can go " 
                + (direction == 1 ? "up" : "down") + " from there");
        
        List<Interface_Elevator> elevatorList 
                = Singl_WorkingBuilding.getInstance().getElevatorList();
        int backup = 0;
        int lessThanAvgAndSameDir = 0;
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
            
            
            // determine the average amount of destinations among all elevators
            int totalDestinations = 0;
            int totalCountingElevators = 0;
            for(Interface_Elevator elevator : elevatorList)
            {
                // if current elevator does have destinations, count it
                // Don't count it if it has none
                if(elevator.getSerialNumber() == elevatorList.get(i).getSerialNumber()
                        && elevator.getDestinationList().isEmpty())
                {
                    totalDestinations += elevator.getDestinationList().size();
                    ++totalCountingElevators;
                }
                else if(elevator.getSerialNumber() != elevatorList.get(i).getSerialNumber())
                {
                    totalDestinations += elevator.getDestinationList().size();
                    ++totalCountingElevators;
                }
            }
            double avgDestinationListSize = (totalDestinations / totalCountingElevators);
            
            
            
            if(elevatorList.get(i).getDestinationList().size() <= avgDestinationListSize
                && (avgDestinationListSize >= 1)
                && ((elevatorList.get(i).getDirection() == -1 
                    && floor <= elevatorList.get(i).getCurrentFloor() 
                    && direction == -1)
                || (elevatorList.get(i).getDirection() == 1 
                    && floor >= elevatorList.get(i).getCurrentFloor() 
                    && direction == 1))
              )   
            {
                try {
                    // check if there is not already a lit callbox button in a
                    // direction different than the request we are examining
                    if(checkCallboxConflict(elevatorList.get(i), floor) 
                            == false) // false means there is no conflict
                    {
                        lessThanAvgAndSameDir = elevatorList.get(i)
                                                    .getSerialNumber();
                    }
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
            }
            
            
            
            // if it doesn't meet the criteria above, then just find
            // elevator that is going in the same direction
            if((elevatorList.get(i).getDirection() == -1 
                    && floor <= elevatorList.get(i).getCurrentFloor() 
                    && direction == -1)
                || (elevatorList.get(i).getDirection() == 1 
                    && floor >= elevatorList.get(i).getCurrentFloor() 
                    && direction == 1)
              )   
            {
                try {
                    // check if there is not already a lit callbox button in a
                    // direction different than the request we are examining
                    if(checkCallboxConflict(elevatorList.get(i), floor) 
                            == false) // false means there is no conflict
                    {
                        sameDirectionElevator = elevatorList.get(i)
                                                    .getSerialNumber();
                    }
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
            }
            
            // backup plan if after I iterate through loop, 
            // I find no directional choices
            if(elevatorList.get(i).getDirection() == 0)
            {
                try {
                    // check if there is not already a lit callbox button in a
                    // direction different than the request we are examining
                    if(checkCallboxConflict(elevatorList.get(i), floor) 
                            == false) // false means there is no conflict
                    {
                        backup = elevatorList.get(i).getSerialNumber();
                    }
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
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
        else if(lessThanAvgAndSameDir != 0)
        {
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller has selected elevator "
                + elevatorList.get(lessThanAvgAndSameDir - 1).getLetterName() 
                + elevatorList.get(lessThanAvgAndSameDir - 1).getSerialNumber()
                + " to satisfy the " + (direction == 1 ? "up" : "down") 
                + " request from floor #" + floor 
                + "\n\t\tbecause it is currently on floor " 
                + elevatorList.get(lessThanAvgAndSameDir - 1).getCurrentFloor()
                + " and it is " 
                + (elevatorList.get(lessThanAvgAndSameDir - 1)
                        .getDirection()== 1 ? "heading up" : 
                    (elevatorList.get(lessThanAvgAndSameDir - 1)
                            .getDirection()== 0 ? "idle" : "heading down")
                   )
                + "\n\t\tIn addition, this elevator was chosen because it had less "
                + "\n\t\tdestinations that the average amount"
            );
            System.out.println(sdf.format(System.currentTimeMillis()) 
                    + "\tSending Elevator " 
                    + elevatorList.get(lessThanAvgAndSameDir - 1).getLetterName() 
                    + elevatorList.get(lessThanAvgAndSameDir - 1).getSerialNumber() 
                    + " to Floor " + floor + "…");
            
            Singl_WorkingController.getInstance().disPatchElevator(
                                                        floor, 
                                                        sameDirectionElevator);
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
            try {
                Singl_WorkingController.getInstance()
                                            .addToPendingList(floor, direction);
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            } catch (IllegalFloorException ex) {
                ex.printStackTrace();
            }
        }
    }
    
        
    /**
     * The elevators are set up to service first pressed, first served relative to 
     * callboxes. So the improved delegate checks if there is not already a lit 
     * callbox button in a direction different than the request we are examining
     * so requests dont get lost (and have to be checked up upon shutdown
     * 
     * 
     * @param elevator, floor
     * @throws IllegalDirectionException, IllegalFloorException
     * @since Version 1.0
     * @return a boolean that signals that the floor doesn't have a conflict
     */
    private boolean checkCallboxConflict(Interface_Elevator elevator, int floor) 
                                                   throws IllegalFloorException {
        
        if(floor < 0)
            throw new IllegalFloorException("Negative floors number encountered: " + floor);
        
        // check if there is not already a lit callbox button in a
        // direction different than the request we are examining
        if(elevator.getDestinationList().contains(floor))
        {
            if(elevator.getDirection() == 1 && Singl_WorkingBuilding.getInstance()
                                            .getFloorList().get(floor - 1)
                                            .getcBox().getDownButton() == false)
            {
                // means there's no conflicting, lit callbox button on the floor
                return false;
            }
            else if(elevator.getDirection() == -1 && Singl_WorkingBuilding.getInstance()
                                                        .getFloorList().get(floor - 1)
                                                        .getcBox().getUpButton() == false)
            {
                // means there's no conflicting, lit callbox button on the floor
                return false;
            }
            
            // means that there is already a lit callbox button on that floor
            // in the opposite direction of the request
            return true;
        }
        
        // means that floor is not yet on that elevator's destination list
        return false;
    }
    
}

