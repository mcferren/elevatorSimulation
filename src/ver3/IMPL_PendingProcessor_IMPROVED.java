package ver3;

import java.text.SimpleDateFormat;
import java.util.*;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a class that represents an expert that the controller relies upon.
 * The purpose is to provide a method (deliverPendingList) that is responsible 
 * for delivering the appropriate floors from the pending list to an elevator
 * that is making a request
 * 
 * Improvement strategy was to not have any one elevator take too many pending
 * requests ate one requests. The thought was while servicing just a few requests
 * then either 
 *          a) itself could be better positioned to ask for more 
 *          b) others could be positioning themselves better while the 
 *             elevator services that small amount of requests
 * 
 * Improvements over the provided delegate include:
 * 1. Checks to see that elevators going the same direction don't show up 
 *    on a floor at the same time
 * 2. Never dispatch more floors to an elevator than the calculated average 
 *    amount of destination list sizes (of other elevators)
 * 3. First identifying and prioritizing the closest elevator instead of farthest
 * 4. The elevators are set up to service first pressed, first served relative to 
 *    callboxes. So the improved delegate checks if there is not already a lit 
 *    callbox button in a direction different than the request we are examining     
 *    so requests don't get lost (and have to be checked up upon shutdown
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class IMPL_PendingProcessor_IMPROVED implements Interface_IMPL_PendingProcessor {
    
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
    public IMPL_PendingProcessor_IMPROVED() {
    }
    
    /**
     * This method is responsible for delivering the appropriate floors from 
     * the pending list to an elevator that is making a request. It first makes
     * sure that there are items in the pending list. If so, it examines each
     * floor on the pending list one at a time. For each iteration, it finds the
     * closest elevaotor. It checks that the elevator doesn't have more destinations
     * than the average amonst all other elevators. If so, it assigns the pending
     * request to the elevator's destination lsit, then it removes the floor from 
     * the controller's pending list.
     * 
     * @param floor, direction
     * @throws IllegalDirectionException, IllegalFloorException, IllegalElevatorException
     * @since Version 1.0
     */
    public void deliverPendingList(int requestingFloor, int requestingElevatorNumber)
                    throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException {
        
        if(requestingElevatorNumber < 0)
            throw new IllegalElevatorException("Negative number of elevators "
                                  + "encountered: " + requestingElevatorNumber);
        
        if(requestingElevatorNumber > Singl_WorkingSimulation.getInstance().getNumberOfElevators())
            throw new IllegalElevatorException("Elevator does not exist (too high): " + requestingElevatorNumber);
                    
        
        if(requestingFloor < 0)
            throw new IllegalFloorException("Negative number of floors "
                                            + "encountered: " + requestingFloor);
        
        if(requestingFloor > Singl_WorkingSimulation.getInstance().getNumberOfFloors()+1)
            throw new IllegalFloorException("Floor does not exist (too high): " + requestingFloor);
        
        
                    
        TreeMap<Integer,Integer> pList = Singl_WorkingController
                                            .getInstance()
                                            .getPendingList();
        
        List<Interface_Elevator> eList = Singl_WorkingBuilding
                                            .getInstance()
                                            .getElevatorList();
        
        TreeMap<Integer,Integer> removalList = new TreeMap<Integer,Integer>();
        
                
        System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "Controller has received the pending request message from "
                + "elevator " + eList.get(requestingElevatorNumber - 1).getLetterName() 
                + requestingElevatorNumber + "\n\t\tand is now preparing "
                + "destinations from the pendinglist to\n\t\tadd to the "
                + "elevator's destination list");
        
        
        // first make sure there are actually pending requests to review
        if(pList.size() > 0)
        {
            
            // Determine closest floor away from current in all pending requests
            int shortestLength = Singl_WorkingSimulation.getInstance().getNumberOfElevators();
            int closestFloor = Singl_WorkingSimulation.getInstance().getNumberOfElevators();
            int requestingElevatorDir = eList
                    .get(requestingElevatorNumber - 1).getDirection();
            int requestingElevatorLetter = eList
                    .get(requestingElevatorNumber - 1).getLetterName();
       
            for (Map.Entry<Integer, Integer> entry : pList.entrySet())
            {     
                if(requestingElevatorDir == 0) // then get any floor for closest floor
                {
                    if(Math.abs(entry.getKey() - requestingFloor) < shortestLength)
                    {
                        shortestLength = Math.abs(entry.getKey() - requestingFloor);
                        closestFloor = entry.getKey();
                    }
                }
                else // then make sure that elevator is already heading 
                     // in the direction of closest floor
                {   
                    if(Math.abs(entry.getKey() - requestingFloor) < shortestLength
                            && ((requestingElevatorDir == 1 // Heading towards
                                        && entry.getKey() >= requestingFloor) 
                                    || (requestingElevatorDir == -1 // Heading towards
                                        && entry.getKey() <= requestingFloor)) 
                      )
                    {
                        try {
                            // check if there is not already a lit callbox button in a
                            // direction different than the request we are examining
                            if(checkCallboxConflict(
                                    eList.get(requestingElevatorNumber - 1), 
                                    entry.getKey()
                                    )
                                        == false) // false means there is no conflict
                            {
                                shortestLength = Math.abs(entry.getKey() - requestingFloor);
                                closestFloor = entry.getKey();
                            }
                        } catch (IllegalFloorException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            
            // Set temporary direction attribute so we know which pending requests 
            // to add to the destination list of the requesting elevator
            int tempDirection = 0;
            if(closestFloor > requestingFloor)
                tempDirection = 1;
            else
                tempDirection = -1;
            
            // determine the average amount of destinations among all elevators
            int totalDestinations = 0;
            int totalCountingElevators = 0;
            for(Interface_Elevator elevator : eList)
            {
                // if current elevator does have destinations, count it
                // Don't count it if it has none
                if(elevator.getSerialNumber() == requestingElevatorNumber
                        && elevator.getDestinationList().isEmpty())
                {
                    totalDestinations += elevator.getDestinationList().size();
                    ++totalCountingElevators;
                }
                else if(elevator.getSerialNumber() != requestingElevatorNumber)
                {
                    totalDestinations += elevator.getDestinationList().size();
                    ++totalCountingElevators;
                }
            }
            double avgDestinationListSize = (totalDestinations / totalCountingElevators);
            
            
            // Add the floor of all pending requests with desired-direction 
            // matching travel-direction to the elevators list of destinations
            for (Map.Entry<Integer, Integer> entry : pList.entrySet())
            {
                
                // Do not dispatch more floors to an elevator than the calculated 
                // average amount of destination list sizes (of other elevators)
                if(avgDestinationListSize > 1 && removalList.size() >= avgDestinationListSize)
                    break;
                
                // if the entry is the closest floor then add it to the list
                if(entry.getKey() == closestFloor)
                {
                    int tempDir;
                    if(entry.getValue() == 1 || entry.getValue() == 2)
                        tempDir = 1;
                    else
                        tempDir = -1;
                    
                    System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                            + "The controller sends elevator " 
                            + eList.get(requestingElevatorNumber - 1).getLetterName()
                            + eList.get(requestingElevatorNumber - 1).getSerialNumber()
                            + " to closestfloor " + entry.getKey() + ". Floor " 
                            + entry.getKey() + " has " 
                            + (tempDir == 1 ? "an up" : "a down")
                            + " request there.");
                    
                    eList.get(requestingElevatorNumber - 1)
                            .addToDestinationList(entry.getKey());
                    
                    removalList.put(entry.getKey(), tempDir);
                    
                    continue; 
                }
                
                // means we are gathering only those pending requests above 
                // current floor == means we're going up
                if(closestFloor >= requestingFloor) 
                {
                                        
                    if((entry.getKey() >= requestingFloor 
                                && entry.getKey() < closestFloor) 
                        && (entry.getValue() == 1 || entry.getValue() == 2))
                    {
                        try {
                            // check if there is not already a lit callbox button in a
                            // direction different than the request we are examining
                            if(checkCallboxConflict(
                                        eList.get(requestingElevatorNumber - 1)
                                        , entry.getKey()
                                    )
                                        == false) // false means there is no conflict
                            {
                                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                                    + "The controller sends elevator " 
                                    + eList.get(requestingElevatorNumber - 1).getLetterName() 
                                    + requestingElevatorNumber + " to floor " + entry.getKey() 
                                    + ". Floor has an up request there.");

                                eList.get(requestingElevatorNumber - 1)
                                        .addToDestinationList(entry.getKey());

                                removalList.put(entry.getKey(), 1);
                            }
                        } catch (IllegalFloorException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                // means we are gathering only those pending requests below current 
                // floor == means we're going down
                else if(closestFloor <= requestingFloor)
                {
                    if((entry.getKey() <= requestingFloor 
                                && entry.getKey() > closestFloor) 
                        && (entry.getValue() == -1 || entry.getValue() == 2))
                    {                        
                        System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                            + "The controller sends elevator " 
                            + eList.get(requestingElevatorNumber - 1).getLetterName() 
                            + requestingElevatorNumber + " to floor " + entry.getKey() 
                            + ". Floor has a down request there.");
                        
                        eList.get(requestingElevatorNumber - 1)
                                .addToDestinationList(entry.getKey());
                        
                        removalList.put(entry.getKey(), -1);
                    }
                }
            }
            
            // clean up & remove requests you've added from pending requests list
            if(!removalList.isEmpty())
            {
                for (Map.Entry<Integer, Integer> pendingRequest : removalList.entrySet())
                {
                    System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "Removing pending request (" + pendingRequest.getKey() + "-" 
                        + (pendingRequest.getValue() == 1 
                                ? "up" 
                                : (pendingRequest.getValue() == -1 
                                        ? "down" : "up&down")) + ")");
                    
                    Singl_WorkingController.getInstance()
                                    .removeFromPendingList(pendingRequest.getKey(),
                                                           pendingRequest.getValue());
                }
            }
            else
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                    + "The controller has no pending destinations to give to "
                    + "elevator " + requestingElevatorLetter
                    + requestingElevatorNumber);
            }
        }
        else
        {
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller has no pending destinations to give to "
                + "elevator " + Singl_WorkingBuilding.getInstance()
                                    .getElevatorList()
                                    .get(requestingElevatorNumber - 1)
                                    .getLetterName()
                + requestingElevatorNumber);
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
     * @return  a boolean that signals that the floor doesn't have a conflict
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

