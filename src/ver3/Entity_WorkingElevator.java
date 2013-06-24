package ver3;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;
import ver3.project_exceptions.IllegalSimulationException;

/**
 * This class represents a Working Elevator.  This elevator is automatically assigned
 * a serial number when constructed.  It can be run, started up, and shut down.  It 
 * also has several private class variables, such as the destination list for the
 * elevator instance (destinationList), the length of time the doors stay open 
 * (doorOpenTime), the length of time to travel between floors (floorTravelTime),
 * the floor an elevator will default back to when idle (defaultFloor), the elevator's
 * current floor (currentFloor), the elevator's direction (direction), the elevator's
 * serial number (serialNumber), the status of whether the elevator is running or idle
 * (isRunning), and an internal static count to assign an elevator's serial number 
 * (serialCount).  It also creates a SimpleDateFormat to display time (sdf).
 * 
 *
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class Entity_WorkingElevator implements Interface_Elevator, Runnable {
    
    /**
     * A private ArrayList of Integers used to store the elevator's destined floors.
     * 
     * @since Version 1.0
     * @see #run
     * @see #addToDestinationList(int) 
     * @see #getDestinationList() 
     */
    private List <Integer> destinationList = new ArrayList <Integer>();
    
    /**
     * A private ArrayList of Interface_Person objects used to store the people
     * currently in the elevator
     * 
     * @since Version 1.0
     * @see #run
     * @see #getPassengerList()
     */
    private List <Interface_Person> passengerList = new ArrayList <Interface_Person>();
    
    /**
     * A constant integer used to store the length of time (in milliseconds) 
     * for the doors to be open on a given floor.
     * 
     * @since Version 1.0
     * @see #getDoorOpenTime()
     * @see #setDoorOpenTime(int dOP) 
     */
    private int doorOpenTime;
    
    /**
     * An integer used to store the length of time (in milliseconds)
     * it takes for the elevator to travel one floor.
     * 
     * @since Version 1.0
     * @see #getFloorTravelTime() 
     * @see #setFloorTravelTime(int fTT)
     */
    private int floorTravelTime;
    
    /**
     * An integer used to store the rate we will scale the time
     * 
     * @since Version 1.0
     * @see #getFloorTravelTime() 
     */  
    private int timeScaleFactor;
    
    /**
     * An integer used the maximum amount of people allowed in an elevator at once
     * 
     * @since Version 1.0
     * @see #getMaxPersonsPerElevator()
     * @see #setMaxPersonsPerElevator(int mPPE) 
     */     
    private int maxPersonsPerElevator;
    
    /**
     * An integer used to store the default floor for the elevator to 
     * rest at while idle.
     * 
     * @since Version 1.0
     * @see #getDefaultFloor()
     * @see #setDefaultFloor(int dF)
     */
    private int defaultFloor;
    
    /**
     * An integer used to store the floor the elevator is currently on.
     * 
     * @since Version 1.0
     * @see #getCurrentFloor()
     * @see #setCurrentFloor(int)  
     */
    private int currentFloor;
    
    /**
     * An integer used to describe the direction the elevator is currently heading.
     * 0 means no direction, a positive integer is up, and a negative integer is down.
     * 
     * @since Version 1.0
     * @see #getDirection() 
     */
    private int direction; // 0 is no direction, positive int is up, negative int is down
    
    /**
     * A unique integer used to represent the serial number of the Elevator. 
     * Each serial number should only have one elevator, and vice versa.
     * 
     * @since Version 1.0
     * @see #getSerialNumber()
     * @see #setSerialNumber(int)  
     */
    private int serialNumber;
    
    /**
     * A character used to distinuguish an elevator
     * 
     * @since Version 1.0
     * @see #getLetterName() 
     * @see #setLetterName(char lN)
     */ 
    private char letterName;
    
    /**
     * A boolean used to determine if the elevator doors are open
     * 
     * @since Version 1.0
     * @see #getDoorsAreOpen() 
     * @see #setDoorsAreOpen(boolean dAO)
     */ 
    private boolean doorsAreOpen;
    
    /**
     * A private static char used to create unique letter names
     * 
     * @see #Entity_WorkingElevator()
     * @since Version 1.0
     */     
    private static char serialChar = 'A';
    
    /**
     * A boolean variable describing whether or not the elevator is running.
     * The variable is set to true if it is currently running, or false if idle.
     * 
     * @since Version 1.0
     * @see #getIsRunning() 
     * @see #setIsRunning(boolean) 
     */
    private boolean isRunning;
    
    /**
     * A static integer used to store the number of elevators and assign serial
     * numbers.  This is incremented when a new elevator is created.
     * 
     * @since Version 1.0
     * @see #Entity_WorkingElevator()
     */
    private static int serialCount = 0; // static so we can use number to assign 
                                        // serial number to elevator object
    
    /**
     * This variable SimpleDateFormat simply sets the format of our date and time
     * for tracking purposes.
     * @since Version 1.0
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * Constructs a new Working Elevator.  Sets the status from idle to running,
     * sets the serial number and increments the serial count, and sets the 
     * direction to none.
     * 
     * @since Version 1.0
     * @see #setIsRunning(boolean) 
     * @see #setCurrentFloor(int) 
     * @see #setSerialNumber(int) 
     * @see #serialCount
     * @see #setDirection(int) 
     */
    public Entity_WorkingElevator() { 
        
        try{

            setTimeScaleFactor(Singl_WorkingSimulation.getInstance().getTimeScaleFactor());
            setSerialNumber(++serialCount); // set first then increments
            setLetterName(serialChar++);
            setMaxPersonsPerElevator(Singl_WorkingSimulation.getInstance().getMaxPersonsPerElevator());
            setDirection(0);
            setDoorOpenTime(Singl_WorkingSimulation.getInstance().getDoorOpenTime());
            setFloorTravelTime(Singl_WorkingSimulation.getInstance().getFloorTravelTime());
            setDoorsAreOpen(true);
            setDefaultFloor(Singl_WorkingSimulation.getInstance()
                                    .getElevatorDefaultFloorDetails()
                                    .get(getSerialNumber()));
            setCurrentFloor(getDefaultFloor());
            
        } catch (IllegalElevatorException ex) {
                        ex.printStackTrace();
        } catch (IllegalFloorException ex) {
            ex.printStackTrace();
        } catch (IllegalDirectionException ex) {
            ex.printStackTrace();
        } catch (IllegalSimulationException ex) {
            ex.printStackTrace();
        }
    } 
    
    /**
     * Simulates the running of the elevator.  Includes two synchronized blocks,
     * these are synchronized because we want each thread to fully complete before
     * another thread may enter the block.  
     * 
     * @since Version 1.0
     * @see #destinationList
     * @see #getDirection() 
     * @see #setDirection(int) 
     * @see #checkChangeDirection(int) 
     * @see #addToDestinationList(int) 
     * @see #getFloorTravelTime()
     * @see #currentFloor
     * @see #getSerialNumber() 
     * @see #getCurrentFloor() 
     */
    public void run(){
        
        boolean waitFlag;
        
        while(getIsRunning()){
            
            waitFlag = false;
            
            synchronized(this) {
                
                if(getDestinationList().isEmpty())
                {
                    try{
                        int tempDir = getDirection();
                        setDirection(0);
                        
                        //make sure door is shut
                        setDoorsAreOpen(true);
                        
                        if(!Singl_WorkingController.getInstance().getPendingList().isEmpty())
                        {
                            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                                    + "Elevator " + getLetterName() + getSerialNumber()
                                    + " will now try to ask the controller for "
                                    + "pending requests");
                            
                            // check if there are any pending destinations before it
                            // goes to waits in idle state
                            Singl_WorkingController.getInstance()
                                .deliverPendingList(getCurrentFloor(), getSerialNumber());
                        }
                        
                        wait(10000 / getTimeScaleFactor());
                        waitFlag = true;
                    } catch (InterruptedException exOne){
                        exOne.printStackTrace();
                    } catch (IllegalElevatorException ex) {
                        ex.printStackTrace();
                    } catch (IllegalFloorException ex) {
                        ex.printStackTrace();
                    } catch (IllegalDirectionException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
            synchronized(destinationList) {
                
                        
                // if the elevator just woke up from sleeping
                if(getDestinationList().isEmpty() && waitFlag == true
                        && getCurrentFloor() != getDefaultFloor()){
                    
                    try {
                        checkChangeDirection(getDefaultFloor());
                    } catch (IllegalDirectionException ex) {
                        ex.printStackTrace();
                    }
                    
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                                + "\tElevator %c%d timed out; returning to its "
                                + "default floor: Floor #%d\n", getLetterName(), 
                                getSerialNumber(), getDefaultFloor());

                    addToDestinationList(getDefaultFloor());
                    
                }
                else if(!getDestinationList().isEmpty())
                {
                    try {
                        //make sure door is shut
                        setDoorsAreOpen(false);
                        
                        Thread.sleep(getFloorTravelTime() / getTimeScaleFactor());
                    } catch (IllegalElevatorException ex) {
                        ex.printStackTrace();
                    } catch(InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                    
                    if(getDirection() == -1 && getCurrentFloor() > getDestinationList().get(0))
                    {
                        currentFloor--;
                    }
                    else if(getDirection() == 1 && getCurrentFloor() < getDestinationList().get(0))
                    {
                        currentFloor++;
                    }
                    
                    try
                    {
                        if(getCurrentFloor() == getDestinationList().get(0)) 
                            considerArrival(); // means I have arrived
                        else
                        {                            
                            //sort destination list
                            Collections.sort(getDestinationList());
                            if(getDirection() == -1)
                                Collections.reverse(getDestinationList());
                            
                            System.out.printf(sdf.format(System.currentTimeMillis()) +
                              "\tElevator %c%d passing Floor %d on the way to %d. %s\n", 
                              getLetterName(), getSerialNumber(),
                              getCurrentFloor(),
                              getDestinationList().get(0),
                              printDestinationList()
                              );  
                        }
                        
                    } catch(IndexOutOfBoundsException ex) {
                        System.out.println("Error occurred while trying to get "
                                + "zero index of destination list (passing msg):" 
                                + ex.getMessage());
                    }
                }
            }
        }
    }      
    
    /**
     * Simulates the elevator starting up from creation.  This creates a new thread
     * for the elevator to run in.  Each elevator runs in its own thread.
     * 
     * @since Version 1.0
     * @see Thread
     */
    public void startUp(){
        
        System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tStarting up elevator %c%d on floor #" 
                            + getCurrentFloor()+ "\n", 
                            getLetterName(), getSerialNumber());
        try {
            setIsRunning(true);
        } catch (IllegalElevatorException ex) {
            ex.printStackTrace();
        }
            new Thread(this).start();      
        
    }
    
    /**
     * Simulates the shut down of the elevator at the end of the program.  This 
     * sets the status to idle and then displays the shutdown message.
     * 
     * @since Version 1.0
     * @see #setIsRunning(boolean) 
     * @see #getSerialNumber() 
     */
    public void shutDown(){
        try{
            setIsRunning(false);
        } catch (IllegalElevatorException ex) {
            ex.printStackTrace();
        }

        System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tElevator %c%d has shutdown\n", 
                            getLetterName(), getSerialNumber());
                    
    }
    
    /**
     * Adds a new destination to the list and keeps the list sorted.  This list 
     * is always sorted in the order that the elevator will visit them, so index
     * 0 of the destinationList will always be the next destination for the elevator.
     * 
     * @since Version 1.0
     * @see #getDirection() 
     * @see #getCurrentFloor()
     * @see #getDestinationList() 
     * @see #checkChangeDirection(int) 
     * @param floor 
     */
    public void addToDestinationList(int floor) {
        
        //Basic checks to ensure we have a good floor argument
        if(floor < 1 || floor > Singl_WorkingBuilding.getInstance().getNumberOfFloors())
            return;
                        
        synchronized (this) {           
            
            //check for wrong direction
            if ((getDirection() == 1 && floor < getCurrentFloor()) ||
                    (getDirection() == -1 && floor > getCurrentFloor()))
            {    
                System.out.printf(sdf.format(System.currentTimeMillis()) 
                        + "\tInvalid Request: Elevator %c%d is moving in a different "
                        + "direction\n", getLetterName(), getSerialNumber());
                    
                return;
            }
            
            //check if the floor is already in the destination list
            if(getDestinationList().indexOf(floor) != -1 && getCurrentFloor() != floor)
            {    
                System.out.printf(sdf.format(System.currentTimeMillis()) 
                        + "\tInvalid Request: Requested floor is already in "
                        + "Elevator %c%d's destination list\n", 
                        getLetterName(), getSerialNumber());
                    
                return;
            }
            
            getDestinationList().add(floor);
            
            if(getDestinationList().size() == 1)
            {
                try {
                    checkChangeDirection(floor);
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                }
            }
            
            //sort destination list
            Collections.sort(getDestinationList());
            if(getDirection() == -1)
                Collections.reverse(getDestinationList());
            
            this.notifyAll();
        
        }
        
        System.out.printf(sdf.format(System.currentTimeMillis()) + 
                          "\tElevator %c%d is adding Floor %d to its destination "
                          + "list, %s\n", 
                          getLetterName(), 
                          getSerialNumber(), 
                          floor, 
                          printDestinationList() );
        
    }

    /**
     * Prints the current destination list and keeps the list sorted according to
     * the direction of the elevator.
     * 
     * @since Version 1.0
     * @see #getDestinationList() 
     * @see Collections
     * @return a string that will be used to display the destination list
     */
    public String printDestinationList() {
        
        String destlistToString = "";
        
        if(!getDestinationList().isEmpty())
        {
            
            //sort destination list
            Collections.sort(getDestinationList());
            if(getDirection() == -1)
                Collections.reverse(getDestinationList());

            destlistToString = "Full Destination List: [";

            for(int i = 0; i < getDestinationList().size(); ++i)
            {
                try
                {
                    destlistToString += getDestinationList().get(i);
                } catch(IndexOutOfBoundsException ex) {
                        System.out.printf("Error occurred while trying to get "
                                + "zero index of destination list (within print "
                                + "destlist method) - index%d: " + ex.getMessage() 
                                + "\n", i);
                }

                if(i != getDestinationList().size() - 1)
                    destlistToString += ", ";
            }
            
            destlistToString += "].";
        }
        else
        {
            destlistToString = " No further destinations.";
        }
        
        return destlistToString;
    }
    
    /**
     * Returns true if the elevator is currently running, and false if it is
     * not.
     * 
     * @since Version 1.0
     * @see #isRunning
     * @return a boolean that shows the status of the elevator
     */
    public boolean getIsRunning() {
        //return isRunning;
        
        if(isRunning)
            return true;
        else
            return false;
    }

    /**
     * Returns the current sorted destination list.  This is represented as an
     * ArrayList of Integers.
     * 
     * @since Version 1.0
     * @see #destinationList
     * @return a collection of destinations the elevator is to visit
     */
    public List <Integer> getDestinationList() {
        return destinationList;
    }

    /**
     * Returns the current direction of the elevator represented as a positive
     * integer if up, a negative integer if down, and 0 if no direction.
     * 
     * @since Version 1.0
     * @see #direction
     * @return an int that represents the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Returns the default floor the elevator will return to when idle.
     * 
     * @since Version 1.0
     * @see #defaultFloor
     * @return the default floor int that the elevator should return to after timeout
     */
    public int getDefaultFloor() {
        return defaultFloor;
    }
    
    /**
     * Returns the serial number of the elevator.
     * 
     * @since Version 1.0
     * @see #serialNumber
     * @return an int the serial number associated
     */
    public int getSerialNumber() {
        return serialNumber;
    }
    
    /**
     * Returns the floor the elevator is currently on.
     * 
     * @since Version 1.0
     * @see #currentFloor
     * @return an int where the elevator currently is located
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Sets the direction of the elevator.
     * 
     * @since Version 1.0
     * @see #direction
     * @param dir
     * @throws InvalidDataException 
     */
    public void setDirection(int dir) throws IllegalDirectionException {
        if(!(dir == -1 || dir == 0 || dir == 1))
            throw new IllegalDirectionException("Out of bounds direction encountered: " + dir);
        else
            direction = dir;
    }
    
    /**
     * Determines if two elevators are equal or not, for unit testing purposes.
     * 
     * @since Version 1.0
     * @see #destinationList
     * @param e
     * @return a boolean determining the comparison
     */
    public boolean equals(Entity_WorkingElevator e) {
        
        if (!this.destinationList.equals(e.destinationList))
            return false;
        //if (this.currentFloor != e.currentFloor )
         //   return false;
        //if (this.direction != e.direction)
          //  return false;
        //if (this.serialNumber != e.getSerialNumber())
          //  return false;
        else
            return true;
        
        //for (Integer dest : this.destinationList) 
          //  for (Integer eDest : e.{
            //if(dest  )
    }    
    
    /**
     * Returns the elevator's passenger list
     * 
     * @since Version 1.0
     * @see #currentFloor
     * @return a list of Interface_Person objects
     */
    public List<Interface_Person> getPassengerList() {
        return passengerList;
    }
    
    /**
     * Adds a person to the elevator's passenger list
     * 
     * @since Version 1.0
     * @see #getPassengerList()
     * @param person
     */
    public void addToPassengerList(Interface_Person person){
        getPassengerList().add(person);
    }  
    
    /**
     * Returns elevator's distinguishing letter
     * 
     * @since Version 1.0
     * @see #letterName
     * @return a char that is the elevator's distinguisher
     */
    public char getLetterName() {
        return letterName;
    }  
    
    /**
     * Returns elevator door's status
     * 
     * @since Version 1.0
     * @see #doorsAreOpen
     * @return a bool that outline an elevator door status
     */
    public boolean getDoorsAreOpen() {
        return doorsAreOpen;
    }
    
    /**
     * Returns elevator door's status
     * 
     * @since Version 1.0
     * @see #doorsAreOpen
     * @return a bool that outlines an elevator door status
     */
    public void setDoorsAreOpen(boolean dAO) throws IllegalElevatorException {
        if(!(dAO == true || dAO == false))
            throw new IllegalElevatorException("Incorrect type encountered "
                                        + "when setting opening doors: " + dAO);
        else
            doorsAreOpen = dAO;
    }

   /**
     * Opens the door
     * 
     * @since Version 1.0
     * @see #doorsAreOpen
     * @param dOP
     * @throws IllegalElevatorException 
     */
    private void setDoorOpenTime(int dOP) throws IllegalElevatorException {
        if(dOP < 0)
            throw new IllegalElevatorException("Negative doorOpenTime encountered: " + dOP);
        else
            doorOpenTime = dOP;
    }

   /**
     * Sets the Floor Travel Time
     * 
     * @since Version 1.0
     * @see #doorsAreOpen
     * @param fTT
     * @throws IllegalFloorException 
     */
    private void setFloorTravelTime(int fTT) throws IllegalFloorException {
        if(fTT < 0)
            throw new IllegalFloorException("Negative floorTravelTime encountered: " + fTT);
        else
            floorTravelTime = fTT;
    }

   /**
     * Sets the Floor Default Floor
     * 
     * @since Version 1.0
     * @see #defaultFloor
     * @param dF
     * @throws IllegalFloorException 
     */
    private void setDefaultFloor(int dF) throws IllegalFloorException {
        
        if(dF < 1 || dF > Singl_WorkingSimulation.getInstance().getNumberOfFloors())
            throw new IllegalFloorException("Out of bounds defaultFloor encountered: " + dF);
        else
        {
            defaultFloor = dF;
        }
    }
    
    /**
     * Returns the simulation Time Scale Factor
     * 
     * @since Version 1.0
     * @see #timeScaleFactor
     * @return an int that represents what time calculations should be divided by
     */
    private int getTimeScaleFactor() {
        return timeScaleFactor;
    }

   /**
     * Sets the Time Scale Factor
     * 
     * @since Version 1.0
     * @see #timeScaleFactor
     * @param tSF
     * @throws IllegalSimulationException 
     */
    private void setTimeScaleFactor(int tSF) throws IllegalSimulationException {
        if(tSF < 0)
            throw new IllegalSimulationException("Negative TimeScale encountered: " + tSF);
        else
            timeScaleFactor = tSF;
    }
    
    /**
     * Returns the simulation Maximum Persons per Elevator
     * 
     * @since Version 1.0
     * @see #timeScaleFactor
     * @return an int that represents how many people are allowed in the elevator
     */
    private int getMaxPersonsPerElevator() {
        return maxPersonsPerElevator;
    }

   /**
     * Sets the maximum allowed people in an elevator
     * 
     * @since Version 1.0
     * @see #maxPersonsPerElevator
     * @param mPPE
     * @throws IllegalSimulationException 
     */
    private void setMaxPersonsPerElevator(int mPPE) throws IllegalSimulationException {
        if(mPPE < 0)
            throw new IllegalSimulationException("Negative TimeScale encountered: " + mPPE);
        else
            maxPersonsPerElevator = mPPE;
    }

   /**
     * Sets the elevator's letter name
     * 
     * @since Version 1.0
     * @see #letterName
     * @param lN
     * @throws IllegalSimulationException 
     */
    private void setLetterName(char lN) throws IllegalSimulationException {
        if(!Character.isLetter(lN))
            throw new IllegalSimulationException("Illegal elevator "
                                              + "character encountered: " + lN);
        else
            letterName = lN;
    }

    /**
     * Sets the state of the elevator to running or not running.
     * 
     * @since Version 1.0
     * @see #isRunning
     * @param b
     * @throws InvalidDataException 
     */
    private void setIsRunning(boolean b) throws IllegalElevatorException {
        if(!(b == true || b == false))
            throw new IllegalElevatorException("Incorrect isRunning type encountered: " + b);
        else
            isRunning = b;
    }

    /**
     * Sets the floor the elevator is currently on.
     * 
     * @since Version 1.0
     * @see #currentFloor
     * @param floorNumber
     * @throws InvalidDataException 
     */
    private void setCurrentFloor(int floorNumber) throws IllegalFloorException {
        if((floorNumber < 1))
            throw new IllegalFloorException("Negative floorNumber encountered: " + floorNumber);
        else
            currentFloor = floorNumber;
    }
    
    /**
     * Sets the serial number of the elevator.
     * 
     * @since Version 1.0
     * @see #serialNumber
     * @param sN
     * @throws InvalidDataException 
     */
    private void setSerialNumber(int sN) throws IllegalSimulationException {
        if(sN < 0)
            throw new IllegalSimulationException("Negative serialNumber encountered: " + sN);
        else
            serialNumber = sN;
    }
    
    /**
     * Returns the constant integer for the travel time between floors.
     * 
     * @since Version 1.0
     * @see #floorTravelTime
     * @return 
     */
    private int getFloorTravelTime() {
        return floorTravelTime;
    }
    
    /**
     * Checks to see if the elevator needs to change direction.  This is done when
     * the elevator processes to the end of its queue, and the destinationList is 
     * empty.  If the direction does need to be changed, this method will automatically
     * call setDirection and change it accordingly.
     * 
     * @since Version 1.0
     * @see #getCurrentFloor() 
     * @see #setDirection(int) 
     * @param floor 
     */
    private void checkChangeDirection(int floor) throws IllegalDirectionException {
        
        if(floor < 0 || floor > Singl_WorkingSimulation.getInstance().getNumberOfFloors())
            throw new IllegalDirectionException("Invalid floor encountered "
                                  + "when trying to change direction: " + floor);
        else
        {
            int tempDir = getDirection();
            try{
                if(getCurrentFloor() > floor)
                    setDirection(-1);
                if(getCurrentFloor() < floor) 
                    setDirection(1);  
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * This method simulates the arrival of an elevator.  It announces its arrival,
     * opens the doors, and announces the next destination in the list. First, 
     * identify which floors in the destination list have been selected by people 
     * in the elevaotr. It then checks if the stop is asking to go in the same 
     * direction that the elevator is heading. If not and noone in the elevator 
     * selected the button, then skip the floor. If when you arrive, there are 
     * waiting people on the floor and your destination list happens to be empty, 
     * then change your direction to what's popular on the floor. Open the doors, 
     * remove passengers from the elevator, check if you need to change floors. 
     * If so, then ask the controller to check the pending list. Sleep for a 
     * little but while doors are open. Add people to passengers list. Close the 
     * doors. Remove the floor from the destination list. Reset the callbox 
     * buttons on the floor
     * 
     * @since Version 1.0
     * @see #getDestinationList() 
     * @see Thread
     * @see #getCurrentFloor()
     * @see #getSerialNumber() 
     * @see #getDestinationList() 
     * @see #printDestinationList() 
     */
    private void considerArrival(){
                
        // acknowledge passengers on the floor you've arrived upon 
        Interface_Floor focusFloor = Singl_WorkingBuilding.getInstance()
                                        .getFloorList().get(getCurrentFloor() - 1);
        
        
        //is there anyone in the elevator who originally selected this destination?
        boolean selectedFromWithin = false;
        
        for(Interface_Person guy : getPassengerList())
        {
            if(getCurrentFloor() == guy.getIntendedDestination())
                selectedFromWithin = true;
        }
        
        
        // now check if the stop is asking to go in the same direction that the 
        // elevator is heading if its not, and noone within the elevator asked 
        // for it, then skip and add floor to pending list
        if(getDestinationList().size() > 1 
                && focusFloor.getcBox().getDownButton() == false 
                && focusFloor.getcBox().getUpButton() == true 
                && selectedFromWithin == false
                && getDirection() == -1) // elevator is heading down
        {       // if elevator is going down, the down callbox button must be 
                // pressed in order for elevator to stop on floor
            
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                    + "Even though floor #" + getCurrentFloor() + " has been on elevator " 
                    + getLetterName() + getSerialNumber() + "'s destination list, the "
                    + "\n\t\trequest to stop on this floor denied because the elevator "
                    + getLetterName() + getSerialNumber() + "\n\t\tis heading in a direction (" 
                    + (getDirection() == 1 ? "up" : "down") + ") that is different than the up "
                    + "direction that the\n\t\tcallbox on floor #" + getCurrentFloor() + " is asking for."
                    + " So elevator " + getLetterName() + getSerialNumber() + " is told to skip"
                    + " this floor,\n\t\tremove it from it's destination list and post the request to"
                    + " the controller's\n\t\tpending list instead."
                    );
            try {
                //add floor and direction to pendinglist 
                Singl_WorkingController.getInstance().addToPendingList(getCurrentFloor(), 1);
            } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
            } catch (IllegalFloorException ex) {
                ex.printStackTrace();
            }
            
            // now remove the floor from the destination list
            if(getDestinationList().contains(new Integer (getCurrentFloor())))
                getDestinationList().remove(new Integer (getCurrentFloor())); 
            
            // return and skip the rest of the method
            return;
                    
        }
        else if(getDestinationList().size() > 1 
                && focusFloor.getcBox().getUpButton() == false 
                && focusFloor.getcBox().getDownButton() == true 
                && selectedFromWithin == false
                && getDirection() == 1) // elevator is heading up
        {       // if elevator is going up, the up callbox button must be pressed 
                // in order for elevator to stop on floor
                        
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                    + "Even though floor #" + getCurrentFloor() + " has been on "
                    + "elevator " + getLetterName() + getSerialNumber() + "'s "
                    + "destination list, the \n\t\trequest to stop on this floor "
                    + "denied because the elevator " + getLetterName() 
                    + getSerialNumber() + "\n\t\tis heading in a direction (" 
                    + (getDirection() == 1 ? "up" : "down") + ") that is different "
                    + "than the down direction that the\n\t\tcallbox on floor #" 
                    + getCurrentFloor() + " is asking for. So elevator " 
                    + getLetterName() + getSerialNumber() + " is told to skip"
                    + " this floor,\n\t\tremove it from it's destination list "
                    + "and post the request to the controller's\n\t\tpending list "
                    + "instead."
                    );
            try {
                //add floor and direction to pendinglist 
                Singl_WorkingController.getInstance().addToPendingList(getCurrentFloor(), -1);
            } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
            } catch (IllegalFloorException ex) {
                ex.printStackTrace();
            }
            
            // now remove the floor from the destination list
            if(getDestinationList().contains(new Integer (getCurrentFloor())))
                getDestinationList().remove(new Integer (getCurrentFloor())); 
            
            // return and skip the rest of the method
            return;
        }
                
        
        // recognize list of people on that floor
        ArrayList<Interface_Person> focusFloorList = focusFloor.getPeopleOnFloorList();
        
        
        // first be sure that the elevator has a direction
        if(getDirection() == 0 && focusFloor.getNumberOfWaitingPeopleCurrentlyOnFloor() > 0)
        {
            // if elevator direction is idle, then set the direction to the same
            // as the anticipated first passenger
            int tempDest = focusFloor.findPersonIndexInWaitingList(0).getIntendedDestination();
            int tempDir = getDirection();
            try {
                setDirection(tempDest > getCurrentFloor() ? 1 : -1);
                
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
        }
        
        // if when you arrive, there are waiting people on the floor and your 
        // destination list happens to be empty, then change your direction to 
        // what's popular on the floor
        if(getPassengerList().isEmpty() 
                && focusFloor.getNumberOfWaitingPeopleCurrentlyOnFloor() > 0
                && getDestinationList().isEmpty())
        {
            // change the direction to the same as the anticipated first passenger
            int tempDest = focusFloor.findPersonIndexInWaitingList(0).getIntendedDestination();
            int tempDir = getDirection();
            try {
                setDirection(tempDest > getCurrentFloor() ? 1 : -1);
                
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
        }
        
        
        // open your doors
        if(getDoorsAreOpen() == false)
        {
            try {
                setDoorsAreOpen(true);
            } catch (IllegalElevatorException ex) {
                ex.printStackTrace();
            }

            System.out.printf(sdf.format(System.currentTimeMillis()) 
                                + "\tElevator %c%d arrives at destination "
                                + "floor %d. Doors open...\n", 
                                getLetterName(), getSerialNumber(), 
                                getCurrentFloor()
                          );
        }
        else
        {
            System.out.printf(sdf.format(System.currentTimeMillis()) 
                                + "\tElevator %c%d is already at "
                                + "floor %d and has its doors open...\n", 
                                getLetterName(), getSerialNumber(), 
                                getCurrentFloor()
                          );
        }
        
        
        // allow any passengers to exit if they've reached their destination
        removePassengersFromElevator();
        
        boolean flagToCheckForPendingRequests = false;
        
        // check to see if any other elevators are currently on the same floor
        // and that floor has people on it
        boolean otherElevatorsOnSameFloor = false; // true means there are others
        List<Interface_Elevator> eList = Singl_WorkingBuilding.getInstance().getElevatorList();
        for(Interface_Elevator elevator : eList)
        {
            
            if(elevator.getCurrentFloor() == getCurrentFloor()
                    && elevator.getDoorsAreOpen() == true // his doors are open
                    && getDoorsAreOpen() == true // my doors are open
                    && elevator.getSerialNumber() != getSerialNumber() // not same 
                    && focusFloorList.size() > 1 // there's more than one person 
                    && elevator.getDirection() != 0
                    && getDirection() != 0
                    && !elevator.getDestinationList().isEmpty()
                    && !getDestinationList().isEmpty()) 
            {
                
                otherElevatorsOnSameFloor = true;
                
                break;
            }
        }
                
        // check to see if passenger list is empty; if so be flexible with 
        // elevator direction don't worry, if people accumulate on a floor, it 
        // will maintain first come, first serve
        // This is an adjustment bc we can't remove currentfloor yet
        if(getPassengerList().isEmpty() 
                && focusFloor.getNumberOfWaitingPeopleCurrentlyOnFloor() > 0 
                && getDestinationList().get(0) == getCurrentFloor() 
                && getDestinationList().size() == 1                
                && otherElevatorsOnSameFloor == false
                )
        {
            // change the direction to the same as the anticipated first passenger
            int tempDest = focusFloor.findPersonIndexInWaitingList(0).getIntendedDestination();
            
            try {
                int tempDir = getDirection();
                
                setDirection(tempDest > getCurrentFloor() ? 1 : -1);
                
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                    + "Elevator " + getLetterName() + getSerialNumber() 
                    + " changes direction from " 
                    + (tempDir == 1 ? "up" : (tempDir == 0 ? "idle" : "down")) + " to " 
                    + (getDirection()== 1 ? "up" : (getDirection() == 0 ? "idle" : "down")));
                
                // if there has been a change in direction, then set a flag for 
                // a pending request check in the near future (after current 
                // floor has been removed)
                 if(tempDir != getDirection())
                     flagToCheckForPendingRequests = true;
                
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
        }            
        
        
        
        
        // sleep for a little bit while the elevator doors are open
        try {
            Thread.sleep(getDoorOpenTime() / getTimeScaleFactor());

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch(IndexOutOfBoundsException ex) {
                        System.out.println("Error occurred while trying to get "
                                + "zero index of destination list (invoking print "
                                + "destlist conditional):" + ex.getMessage());
        }
        
        
        
        // allow any passengers on the floor to enter 
        // elevator if they wish (match direction)
        TreeMap<Integer, Interface_Person> newPassengerRequests 
                = addPassengersToElevator();
        try {
            // allow any passengers on the floor to enter 
            // elevator if they wish (match direction)
            //TreeMap<Integer, Interface_Person> newPassengerRequests 
            //        = focusFloor.sendPassengersToElevator(getSerialNumber());
            
          
            // close the elevator doors
            setDoorsAreOpen(false);
        } catch (IllegalElevatorException ex) {
            ex.printStackTrace();
        }
        
        
        // now remove the destination you've arrived at 
        // (if you haven't spontaneously arrived)
        if(getDestinationList().contains(new Integer (getCurrentFloor())))
            getDestinationList().remove(new Integer (getCurrentFloor())); 
                
        
        // now that doors are closed, register all new 
        // passenger floor requests with the controller
        for (Map.Entry<Integer, Interface_Person> request : newPassengerRequests.entrySet())
        {
            if(!getDestinationList().contains(request.getKey()))
            {                
                addToDestinationList(request.getKey());
            }
        }
        
        // if there had been a direction change above and the flag was set, 
        // then check for pending requests
        TreeMap<Integer, Integer> pList = Singl_WorkingController.getInstance()
                                                .getPendingList();
        
        if(flagToCheckForPendingRequests == true)
        {
            // first check controller's pending list to make sure its accounted 
            // for people entering due to the direction change
            if(pList.containsKey(getCurrentFloor())
                    && (pList.get(getCurrentFloor()) == 2 
                            || pList.get(getCurrentFloor()) == getDirection()))
            {
                try {
                    Singl_WorkingController.getInstance()
                            .removeFromPendingList(getCurrentFloor(), getDirection());
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
                
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t" + "Removed " 
                        + getCurrentFloor() + "-" + (getDirection() == 1 ? "up" : "down")
                        + " request from the controller's pending list because it was just "
                        + "satisfied");
            }
            
            System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                    + "The direction of elevator " + getLetterName() + getSerialNumber() 
                    + " has changed. Now that the current floor\n\t\thas been reached"
                    + " elevator " + getLetterName() + getSerialNumber() 
                    + " will ask the controller for pending requests");
            try {
                // check if there are any pending destinations before it
                // goes to waits in idle state
                Singl_WorkingController.getInstance()
                    .deliverPendingList(getCurrentFloor(), getSerialNumber());
            } catch (IllegalElevatorException ex) {
                    ex.printStackTrace();
            } catch (IllegalFloorException ex) {
                ex.printStackTrace();
            }
        }
        
        System.out.printf(sdf.format(System.currentTimeMillis()) +
                "\tElevator %c%d Doors are closed and the destination list has "
                + "been\n\t\tupdated. %s%s\n", 
                getLetterName(), 
                getSerialNumber(), 
                !getDestinationList().isEmpty() ? "Continuing to next destination: " 
                                                  + getDestinationList().get(0) + ". " 
                                                : "",
                printDestinationList());
        
        
        //set the callbox button back to its default state 
        //and make sure pending list has been accounted for
        
        boolean tempUpStatus = focusFloor.getcBox().getUpButton();
        
        boolean tempDownStatus = focusFloor.getcBox().getDownButton();
        
        if(getDirection() == 1)
        {
            try {
                focusFloor.getcBox().setUpButton(false);
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
            
            // check controller's pending list to make sure its not there anymore
            if(pList.containsKey(getCurrentFloor())
                    && (pList.get(getCurrentFloor()) == 2 
                            || pList.get(getCurrentFloor()) == 1))
            {
                try {
                    Singl_WorkingController.getInstance()
                            .removeFromPendingList(getCurrentFloor(), 1);
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
                
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "Request " + getCurrentFloor() + "-" + "up"
                        + " has been removed from the controller's pending list"
                        + " because it has just been satisfied.");
            }
            
            if(tempUpStatus == true)
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "The up button on floor #" + getCurrentFloor() 
                        + " has been reset from " + tempUpStatus + " to " 
                        + focusFloor.getcBox().getUpButton() 
                        + " so it is no longer lit.");
            }
            else
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "The up button on floor #" + getCurrentFloor()
                        + " was already " + tempUpStatus + " and not lit so it "
                        + "does not need to be reset.");
            }
        }
        else
        {
            try {
                focusFloor.getcBox().setDownButton(false);
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
            
            // check controller's pending list to make sure its not there anymore
            if(pList.containsKey(getCurrentFloor())
                    && (pList.get(getCurrentFloor()) == 2 
                            || pList.get(getCurrentFloor()) == -1)
               )
            {
                try {
                    Singl_WorkingController.getInstance()
                            .removeFromPendingList(getCurrentFloor(), -1);
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
                
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "Request " + getCurrentFloor() + "-" + "down"
                        + " has been removed from the controller's pending list"
                        + " because it has just been satisfied.");
            }
            
            if(tempDownStatus == true)
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "The down button on floor #" + getCurrentFloor() 
                        + " has been reset from " + tempDownStatus + " to " 
                        + focusFloor.getcBox().getDownButton() 
                        + " so it is no longer lit.");
            }
            else
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "The down button on floor #" + getCurrentFloor()
                        + " was already " + tempDownStatus + " and not lit so it "
                        + "does not need to be reset.");
            }

        }
        
        // HERE IS WHERE WE PROCESS THE LIST OF PEOPLE FRUSTRATED BC THEY COULDN"T 
        // GET ON A FULL ELEVATOR EACH ONE WILL INVOKE THE PERSON pressCallbox() METHOD
        for(Interface_Person angryLeftOver : focusFloorList)
        {
            if(angryLeftOver.getIntendedDirection() == getDirection() 
                        && getPassengerList().size() == getMaxPersonsPerElevator())
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                    + "Frustrated Person " + angryLeftOver.getSerialNumber() 
                    + " was left behind, standing on floor #" + getCurrentFloor() 
                    + ", because\n\t\televator " + getLetterName() + getSerialNumber() 
                    + " was too full. So he presses his callbox" + " request again.");
                    
                angryLeftOver.pressCallbox();
            }
        }
    }    
    
    /**
     * This method removes passengers from the elevator
     * 
     * @since Version 1.0
     * @see #getPassengerList() 
     * @see #getPersonsCompleted()
     * @see #getPassengerList()
     * @see #markArrivalTime()
     */
    private void removePassengersFromElevator(){
        
        // allow any passengers to exit if they've reached their destination
        if(getPassengerList().size() > 0)
        {
            List <Interface_Person> tempRemovalList = new ArrayList <Interface_Person>();
            
            for(Interface_Person person : getPassengerList())
            {
                if(person.getIntendedDestination() == getCurrentFloor())
                {
                    // use a temparraylist so we're not pulling 
                    // up boards as we walk on bridge
                    tempRemovalList.add(person);
                    
                    // add person to the floor's completed list
                    Singl_WorkingBuilding.getInstance()
                            .getFloorList().get(getCurrentFloor() - 1)
                            .getPersonsCompleted().add(person);
                    
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                                + "\tPerson %d has arrived at his destination. "
                                + "He is now exiting elevator at floor %d.\n", 
                                person.getSerialNumber(), 
                                person.getIntendedDestination()
                          );   
                }
                
            }
            
            // clean up and remove all the people that 
            // were previous marked for removal
            for(Interface_Person person: tempRemovalList)
            {
                int indexOfPersonToRemove = getPassengerList().indexOf(person);
                getPassengerList().remove(indexOfPersonToRemove);
                person.markArrivalTime();
            }
        }
    }
    
    /**
     * This adds passengers to the elevator and returns a list of requests
     * 
     * @since Version 1.0
     * @see #getPassengerList() 
     * @see #getPersonsCompleted()
     * @see #getPassengerList()
     * @see #markArrivalTime()
     * @return TreeMap<Integer, Interface_Person> requests for elevator to iterate through to controller
     */
    private synchronized TreeMap<Integer, Interface_Person> addPassengersToElevator(){
        
        // this list is used to collect all new passenger floor requests
        TreeMap<Integer, Interface_Person> newPassengerRequests 
                = new TreeMap<Integer, Interface_Person>();
        
        // acknowledge passengers on the floor you've arrived upon 
        ArrayList<Interface_Person> examineFloorList = 
                                Singl_WorkingBuilding.getInstance()
                                    .getFloorList().get(getCurrentFloor() - 1)
                                    .getPeopleOnFloorList();
        
        // acknowledge passengers on the floor you've arrived upon 
        Interface_Floor focusFloor = Singl_WorkingBuilding.getInstance()
                                    .getFloorList().get(getCurrentFloor() - 1);
        
        // allow any passengers on the floor to enter elevator 
        // if they wish (match direction)
        if(getPassengerList().size() < getMaxPersonsPerElevator())
        {
            
            // this list is used to remove people from the 
            // floor list after they've entered elevator
            List <Interface_Person> tempRemovalList = new ArrayList <Interface_Person>();
            
            for(Interface_Person person : examineFloorList)
            {                
                // if the person intends to go in the same direction as the elevator
                // then they will enter the elevator
                if(person.getIntendedDirection() == getDirection() 
                        && getPassengerList().size() < getMaxPersonsPerElevator())
                {
                    // we mark the person for removal from the floor's list of people and we
                    // use a temparraylist so we're not pulling up boards as we walk on bridge
                    tempRemovalList.add(person);
                    
                    // person enters elevator
                    getPassengerList().add(person);
                    
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tPerson %d is now entering elevator %c%d floor %d.\n", 
                            person.getSerialNumber(),
                            getLetterName(), 
                            getSerialNumber(),
                            getCurrentFloor()
                    );              
                    
                    
                    // collect requests from new passengers
                    // if the floor has not yet been pressed in the elevator, then add to 
                    // the newPassengerRequests list that will be processed below
                    if(!getDestinationList().contains(person.getIntendedDestination())
                            && !newPassengerRequests.containsKey(person.getIntendedDestination())
                            )
                    {
                        System.out.printf(sdf.format(System.currentTimeMillis())
                        + "\tNow that he is in the elevator %c%d, person %d "
                        + "presses the button for floor %d\n", 
                        getLetterName(), 
                        getSerialNumber(),
                        person.getSerialNumber(), 
                        person.getIntendedDestination());

                        newPassengerRequests.put(person.getIntendedDestination(), person);
                    }
                    else
                    {
                        System.out.printf(sdf.format(System.currentTimeMillis()) +
                            "\tAs person %d enters elevator %c%d, he notices the "
                                + "button for floor %d is already a destination\n", 
                            person.getSerialNumber(),
                            getLetterName(), 
                            getSerialNumber(), 
                            person.getIntendedDestination());
                    }
                }
                else if(person.getIntendedDirection() == getDirection() 
                        && getPassengerList().size() == getMaxPersonsPerElevator())
                {
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tPerson %d on floor %d is frustrated with "
                            + "elevator %c%d\n\t\tbecause it has opened on his "
                            + "floor but it now is too full\n\t\tof people.\n", 
                            person.getSerialNumber(),
                            getCurrentFloor(),
                            getLetterName(), 
                            getSerialNumber()
                    );  
                    
                }
                else if(person.getIntendedDirection() != getDirection())
                {
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tPerson %d on floor %d is frustrated with "
                            + "elevator %c%d \n\t\tbecause it has opened on his "
                            + "floor but it is going\n\t\tin the wrong direction. "
                            + "Person %d wants to go %s to floor %d\n", 
                            person.getSerialNumber(),
                            getCurrentFloor(),
                            getLetterName(), 
                            getSerialNumber(), 
                            person.getSerialNumber(),
                            (person.getIntendedDirection() == 1 ? "up" : "down"),
                            person.getIntendedDestination()
                    );  
                }
            }
            
            // clean up and remove all the people 
            // that were previous marked for removal
            for(Interface_Person person: tempRemovalList)
            {
                int indexOfPersonToRemove = examineFloorList.indexOf(person);
                examineFloorList.remove(indexOfPersonToRemove);
                person.markEnterElevatorTime();
                focusFloor.getPersonsDeparted().add(person.getWaitTime());
            }
        }
        
        return newPassengerRequests;
    }
    
    /**
     * Returns the constant integer for the door opening time.
     * 
     * @since Version 1.0
     * @see #doorOpenTime
     * @return an int representing how long the door should be open
     */
    private int getDoorOpenTime() {
        return doorOpenTime;
    }

}

