package ver3;

import java.text.SimpleDateFormat;
import java.util.*;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a class that represents an expert that the controller relies upon.
 * The purpose is to provide a method (deliverPendingList) that is responsible 
 * for delivering the appropriate floors from the pending list to an elevator
 * that is making a request
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class IMPL_PendingProcessor_PROVIDED implements Interface_IMPL_PendingProcessor {

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
    public IMPL_PendingProcessor_PROVIDED() {
    }
         
    /**
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
                         throws IllegalFloorException, IllegalDirectionException {
        
        if(requestingElevatorNumber < 0)
            throw new IllegalDirectionException("Negative number of elevators "
                                  + "encountered: " + requestingElevatorNumber);
        
        if(requestingFloor < 0)
            throw new IllegalFloorException("Negative number of floors "
                                            + "encountered: " + requestingFloor);
                   
        
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
            
            // Determine furthest floor away from current in all pending requests
            int furthestLength = 0;
            int furthestFloor = 0;
            int requestingElevatorDir = eList
                    .get(requestingElevatorNumber - 1).getDirection();
       
            for (Map.Entry<Integer, Integer> entry : pList.entrySet())
            {     
                if(requestingElevatorDir == 0) // then get any floor for furthest floor
                {
                    if(Math.abs(entry.getKey() - requestingFloor) > furthestLength)
                    {
                        furthestLength = Math.abs(entry.getKey() - requestingFloor);
                        furthestFloor = entry.getKey();
                    }
                }
                else // then make sure that elevator is already heading 
                     // in the direction of furthest floor
                {   
                    if(Math.abs(entry.getKey() - requestingFloor) > furthestLength
                            && ((requestingElevatorDir == 1 // Heading towards
                                        && entry.getKey() >= requestingFloor) 
                                    || (requestingElevatorDir == -1 // Heading towards
                                        && entry.getKey() <= requestingFloor)) 
                      )
                    {
                        furthestLength = Math.abs(entry.getKey() - requestingFloor);
                        furthestFloor = entry.getKey();
                    }
                }
            }
            
            // Set temporary direction attribute so we know which pending requests 
            // to add to the destination list of the requesting elevator
            int tempDirection = 0;
            if(furthestFloor > requestingFloor)
                tempDirection = 1;
            else
                tempDirection = -1;
            
            // Add the floor of all pending requests with desired-direction 
            // matching travel-direction to the elevators list of destinations
            for (Map.Entry<Integer, Integer> entry : pList.entrySet())
            {
                
                // if the entry is the furthest floor then add it to the list
                if(entry.getKey() == furthestFloor)
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
                            + " to furthestfloor " + entry.getKey() + ". Floor " 
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
                if(furthestFloor >= requestingFloor) 
                {
                                        
                    if((entry.getKey() >= requestingFloor 
                                && entry.getKey() < furthestFloor) 
                        && (entry.getValue() == 1 || entry.getValue() == 2))
                    {
                        System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                            + "The controller sends elevator " 
                            + eList.get(requestingElevatorNumber - 1).getLetterName() 
                            + requestingElevatorNumber + " to floor " 
                            + entry.getKey() + ". Floor has an up request there.");

                        eList.get(requestingElevatorNumber - 1)
                                .addToDestinationList(entry.getKey());

                        removalList.put(entry.getKey(), 1);
                    }
                }
                // means we are gathering only those pending requests below current 
                // floor == means we're going down
                else if(furthestFloor <= requestingFloor)
                {
                    if((entry.getKey() <= requestingFloor 
                                && entry.getKey() > furthestFloor) 
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
                    + "elevator " + requestingElevatorNumber);
            }
        }
        else
        {
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                + "The controller has no pending destinations to give to "
                + "elevator " + requestingElevatorNumber);
        }
        
    }
    
}
