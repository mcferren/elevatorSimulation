package ver3;

import java.util.List;
import ver3.project_exceptions.IllegalDirectionException;

/**
 * This is an interface used so elevators can be instantiated in an abstract way. 
 * We want to code to interface (role) and this interface allows us to do so for 
 * elevator. 
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public interface Interface_Elevator {
    
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
    public void addToDestinationList(int dest);  
    
    /**
     * Simulates the elevator starting up from creation.  This creates a new thread
     * for the elevator to run in.  Each elevator runs in its own thread.
     * 
     * @since Version 1.0
     * @see Thread
     */
    public void startUp();
    
    /**
     * Simulates the shut down of the elevator at the end of the program.  This 
     * sets the status to idle and then displays the shutdown message.
     * 
     * @since Version 1.0
     * @see #setIsRunning(boolean) 
     * @see #getSerialNumber() 
     */
    public void shutDown();
    
    /**
     * Determines if two elevators are equal or not, for unit testing purposes.
     * 
     * @since Version 1.0
     * @see #destinationList
     * @param e
     * @return a boolean determining the comparison
     */
    public boolean equals(Entity_WorkingElevator e);
    
    /**
     * Returns the floor the elevator is currently on.
     * 
     * @since Version 1.0
     * @see #currentFloor
     * @return an int where the elevator currently is located
     */
    public int getCurrentFloor();

    /**
     * Returns the current direction of the elevator represented as a positive
     * integer if up, a negative integer if down, and 0 if no direction.
     * 
     * @since Version 1.0
     * @see #direction
     * @return an int that represents the direction
     */
    public int getDirection();
    
    /**
     * Returns the serial number of the elevator.
     * 
     * @since Version 1.0
     * @see #serialNumber
     * @return an int the serial number associated
     */
    public int getSerialNumber();

    /**
     * Returns the current sorted destination list.  This is represented as an
     * ArrayList of Integers.
     * 
     * @since Version 1.0
     * @see #destinationList
     * @return a collection of destinations the elevator is to visit
     */
    public List <Integer> getDestinationList();
    
    /**
     * Returns elevator's distinguishing letter
     * 
     * @since Version 1.0
     * @see #letterName
     * @return a char that is the elevator's distinguisher
     */
    public char getLetterName();
    
    /**
     * Returns elevator door's status
     * 
     * @since Version 1.0
     * @see #doorsAreOpen
     * @return a bool that outline an elevator door status
     */
    public boolean getDoorsAreOpen();

    /**
     * Sets the direction of the elevator.
     * 
     * @since Version 1.0
     * @see #direction
     * @param dir
     * @throws InvalidDataException 
     */
    public void setDirection(int dir) throws IllegalDirectionException;
    
    /**
     * Returns the floor the elevator is currently on.
     * 
     * @since Version 1.0
     * @see #currentFloor
     * @return an int where the elevator currently is located
     */
    public int getDefaultFloor();   
    
    /**
     * Returns the elevator's passenger list
     * 
     * @since Version 1.0
     * @see #currentFloor
     * @return a list of Interface_Person objects
     */
    public List<Interface_Person> getPassengerList();

    /**
     * Prints the current destination list and keeps the list sorted according to
     * the direction of the elevator.
     * 
     * @since Version 1.0
     * @see #getDestinationList() 
     * @see Collections
     * @return a string that will be used to display the destination list
     */
    public String printDestinationList();
    
    /**
     * Adds a person to the elevator's passenger list
     * 
     * @since Version 1.0
     * @see #getPassengerList()
     * @param person
     */
    public void addToPassengerList(Interface_Person person);
}
