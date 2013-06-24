package ver3;

import java.text.SimpleDateFormat;
import java.util.TreeMap;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a Singleton class that represents a Controller. The controller is used
 * to dispatch elevators to a specified address. It is Singleton because we want
 * all elevators to get requests from the same controller. Future iterations will 
 * involve determining (from a list of elevators) the best to fulfill an incoming
 * request. This version dispatches directly based on provided arguments. The class
 * is a traditional Singleton that has a private constructor, a private local 
 * instance variable and a public static getInstance class. In addition, it has
 * public dispatchElevator() method used to tell an elevator to go to a particular
 * floor
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public class Singl_WorkingController {
    
    /**
     * This is a private static variable used to store the instance of the 
     * Singl_WorkingController object. It is volatile because we are working with 
     * threads and do not want to cache copies. 
     * 
     * @since Version 1.0
     * @see #Singl_WorkingController()
     */
    private volatile static Singl_WorkingController instance; 
    /**
     * This is a private delegate variable we'll use to 
     * select elevators when a callbox signals
     * 
     * @since Version 1.0
     * @see #pick(int floor, int direction)
     */
    private Interface_IMPL_ElevatorPicker delegateEP;
    
    /**
     * This is a private delegate variable we'll use to 
     * select floors when an elevator asks for pending requests
     * 
     * @since Version 1.0
     * @see #deliverPendingList(int requestingFloor, int requestingElevatorNumber)
     */
    private Interface_IMPL_PendingProcessor delegatePP;
    
    /**
     * This is a private collection we'll use to store pending requests which are
     * callbox requests that cannot be serviced right away
     * 
     * Argument order is floor, direction code
     * Direction code: -1 for just down, 1 for just up, 2 for down & up
     * 
     * @since Version 1.0
     * @see #deliverPendingList(int requestingFloor, int requestingElevatorNumber)
     */
    private TreeMap<Integer, Integer> pendingList = new TreeMap<Integer, Integer>(); 

    /**
     * This is a private constructor so that noone on the outside can instantiate
     * an object from the class directly
     * 
     * @since Version 1.0
     * @see #getInstance()
     */
    private Singl_WorkingController() {
                
    }
    
    /**
     * This is a static method that returns an instance of the Controller object.
     * The method contains conditionals to check if the instance has already been
     * instantiated. If so, then it returns the pre-existing object. If not, then
     * inside a synchronized block (to protect against multiple threads potentially
     * create two instantiations of the class)
     * 
     * @since Version 1.0
     * @return the instance variable which is the controller object
     */
    public static Singl_WorkingController getInstance() {
        
        if(instance == null)
            synchronized(Singl_WorkingController.class)
            {
                if(instance == null)
                {
                    instance = new Singl_WorkingController();
                }
            }
        
        return instance;
        
    }
         
    /*
     * This method is responsible for delivering the appropriate floors from 
     * the pending list to an elevator that is making a request. It first makes
     * sure that there are items in the pending list. If so, it examines each
     * floor on the pending list one at a time. For each iteration, it finds the
     * farthest elevaotor heading it that direction. It then finds all pending
     * requests in between that farthest floor and the selected elevator. It then
     * assigns the pending request to the elevator's destination lsit, then it 
     * removes the floor from the controller's pending list. We refer to our
     * delegate in order to invoke this method
     * 
     * @param floor, direction
     * @throws IllegalDirectionException, IllegalFloorException, IllegalElevatorException
     * @since Version 1.0
     */
    public void deliverPendingList(int requestingFloor, int requestingElevatorNumber) 
                       throws IllegalElevatorException, IllegalFloorException {
         
        if(requestingElevatorNumber < 0)
            throw new IllegalElevatorException("Negative number of elevators "
                                  + "encountered: " + requestingElevatorNumber);
        
        if(requestingFloor < 0)
            throw new IllegalFloorException("Negative number of floors "
                                            + "encountered: " + requestingFloor);
        
        String version = Singl_WorkingSimulation.getInstance().getVersion();
        setDelegatePP(Fact_IMPL_PendingProcessor.build(version));
        try {
            getDelegatePP().deliverPendingList(requestingFloor, requestingElevatorNumber);
        } catch (IllegalDirectionException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * This method reaches out to the designated delegate class in order to select
     * which elevator is best suited to fullfill a request that comes in from a 
     * callbox on a particular floor. The parameters included in the method 
     * include the floor number that the callbox is on and the direction related
     * to the where the request intends to go. We refer to our
     * delegate in order to invoke this method
     * 
     * @param floor, direction
     * @throws IllegalDirectionException, IllegalFloorException
     * @since Version 1.0
     */
    public void pick(int floor, int direction) throws IllegalDirectionException,
                                                         IllegalFloorException {
        
        if(!(direction == -1 || direction  == 1))
            throw new IllegalDirectionException("Out of bounds direction encountered"
                    + " while delegate is picking: " + direction);
        
        if(floor < 0)
        {
            throw new IllegalFloorException("Invalid floor argument encountered"
                         + " when invoking picking method: " + floor);
        }
         
        String version = Singl_WorkingSimulation.getInstance().getVersion();
        
        setDelegateEP(Fact_IMPL_ElevatorPicker.build(version));
        
        try {
            getDelegateEP().pick(floor, direction);
        } catch (IllegalDirectionException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * This method is invoked when no appropriate elevator is found to respond
     * to a callbox request. It adds floor requests to the controllers pending
     * list. It is a little tricky bc the pending list is a map that has a code
     * for the scenario where a floor has a lit up button and a lit down button
     * 
     * @param int floor, int direction
     * @throws IllegalDirectionException, IllegalFloorException 
     * @since Version 1.0
     */
    public void addToPendingList(int floor, int direction) 
                       throws IllegalDirectionException, IllegalFloorException {
             
        if(!(direction == -1 || direction  == 1))
            throw new IllegalDirectionException("Out of bounds direction encountered"
                    + " while controller is addingtopendinglist: " + direction);
        
        if(floor < 0)
        {
            throw new IllegalFloorException("Invalid floor argument encountered"
                         + " when invoking addToPendingList method: " + floor);
        }
        
        // if the floor is already in the pending list 
        // but that request is of a different direction
        if(getPendingList().containsKey(floor) && getPendingList().get(floor) != direction)
            getPendingList().put(floor, 2); // then notate that both floor directions 
        else if (!getPendingList().containsKey(floor)) // floor isn't in pendinglist,
            getPendingList().put(floor, direction);    // then add
        
        // if the request contains a floor and a directional code that is already in the pending list
        // then just ignore it and let it fall through the conditional statement
        
    }
    
    /**
     * This method is invoked when the contoller's IMPL decides to dispatch
     * a particular elevator to a particular floor found from the pending list.
     * It removes floor requests from the pending list after it has given them
     * to the elevator. It is a little tricky bc the pending list is a map that 
     * has a code for the scenario where a floor has a lit up button and a lit 
     * down button
     * 
     * @param int floor, int direction
     * @throws IllegalDirectionException, IllegalFloorException 
     * @since Version 1.0
     */
    public void removeFromPendingList(int floor, int direction) 
                       throws IllegalDirectionException, IllegalFloorException {
        
        if(!(direction == -1 || direction  == 1))
            throw new IllegalDirectionException("Out of bounds direction encountered"
                    + " while controller is removing from pending list: " + direction);
        
        if(floor < 0)
        {
            throw new IllegalFloorException("Invalid floor argument encountered"
                         + " when invoking removePendingList method: " + floor);
        }

        try {
            // if the remove request contains a floor and a directional that is in 
            // the pending list and the directional code is 2, then that floor has 
            // an up and a down request in the pending list
            if(getPendingList().containsKey(floor) && getPendingList().get(floor) == 2)
                // remove the request direction by multiplying it by negative one
                getPendingList().put(floor, (direction * -1)); 
            else if(getPendingList().containsKey(floor) && getPendingList().get(floor) != 2)
                getPendingList().remove(floor);

            // if the remove request contains a floor and a directional code that is 
            // NOT in the pending list then just ignore it and let it fall through 
            // the conditional statement

        } catch(IndexOutOfBoundsException ex) {
            System.out.printf("Error occurred while trying to get floor index %d"
                    + " of  from Pending list inside removeFromPendingList"
                    + " method: %s\n", ex.getMessage(), floor);
        }
    }
    
    /**
     * This public method is used to by the controller to send an elevator to a 
     * particular floor. It takes two ints as arguments in orfer to know which
     * elevator to send and where to send it to. The method checks the input
     * arguments to make sure they are in the correct range related to how many
     * floors and elevators the program's building owns. If the ints are outside
     * of those ranges then it propagates an exception up to the invoking method.
     * This method gets an instance of the building singlton in order to locate
     * the specific elevator from its elevator arraylist. Once it pinpoints the 
     * specific elevator, it then invokes its addDestinationList method. 
     * 
     * @see #addToDestinationList(int floor)
     * @since Version 1.0
     * @param floorRequested used to tell the elevator where to go
     * @param elevatorNumber used to pinpoint which elevator to dispatch
     * @throws IllegalArgumentException if an out of bounds int is used for floors or elevators
     * @throws IndexOutOfBoundsException if it tries to get an index from outside
     *         the boundry of the building's elevator list
     */
    public void disPatchElevator(int floorRequested, int elevatorNumber) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
        //check to make sure the floor argument is in range
        if(floorRequested < 1 
                || floorRequested > Singl_WorkingBuilding.getInstance()
                                                            .getNumberOfFloors()
          )
        {
            System.out.printf(sdf.format(System.currentTimeMillis()) 
                        + "\tInvalid Request: Requested floor %d is outside the "
                        + "range held in the building\n", floorRequested);
            throw new IllegalArgumentException();
        }
        
        //check to make sure the elevator argument is in range
        if(elevatorNumber < 1 
                || elevatorNumber > Singl_WorkingBuilding.getInstance()
                                                         .getNumberOfElevators()
          )
        {
            System.out.printf(sdf.format(System.currentTimeMillis()) 
                        + "\tInvalid Request: Requested elevator %d is outside the "
                        + "range owned by the building\n", elevatorNumber);
            throw new IllegalArgumentException();
        }
           
        try
        {
            Interface_Elevator eActual = Singl_WorkingBuilding.getInstance()
                                          .getElevatorList().get(elevatorNumber - 1); 
            
            eActual.addToDestinationList(floorRequested);
        } catch(IndexOutOfBoundsException ex) {
            System.out.printf("Error occurred while trying to get "
                    + " index %d of destination list (passing msg): %s\n", 
                    ex.getMessage(), elevatorNumber);
        }
        
    }
    
    /**
     * A simple accessor method that returns the IMPL EP Delegate
     * 
     * @see #delegateEP
     * @since Version 1.0
     * @return Interface_IMPL_ElevatorPicker representing the EP IMPL
     */
    public Interface_IMPL_ElevatorPicker getDelegateEP() {
        return delegateEP;
    }
    
    /**
     * A simple accessor method that returns the IMPL PP Delegate
     * 
     * @see #delegatePP
     * @since Version 1.0
     * @return Interface_IMPL_ElevatorPicker representing the PP IMPL
     */
    public Interface_IMPL_PendingProcessor getDelegatePP() {
        return delegatePP;
    }
    
    /**
     * A simple accessor method that returns the pending list
     * 
     * @see #pendingList
     * @since Version 1.0
     * @return TreeMap<Integer, Integer> representing the pending list
     */
    public TreeMap<Integer, Integer> getPendingList() {
        return pendingList;
    }
    
    /**
     * Sets the PP delegate variable
     * 
     * @since Version 1.0
     * @see #delegatePP
     * @param Interface_IMPL_PendingProcessor dPP
     */
    private void setDelegatePP(Interface_IMPL_PendingProcessor dPP) {
        delegatePP = dPP;
    }
    
    /**
     * Sets the EP delegate variable
     * 
     * @since Version 1.0
     * @see #delegateEP
     * @param Interface_IMPL_PendingProcessor dEP
     */
    private void setDelegateEP(Interface_IMPL_ElevatorPicker dEP) {
        delegateEP = dEP;
    }

}
