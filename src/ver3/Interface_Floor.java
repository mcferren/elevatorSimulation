package ver3;

import java.util.ArrayList;
import java.util.TreeMap;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;

/**
 * This is an interface mostly used just so floors can be instantiated in an 
 * abstract way. We want to code to interface (role) and this interface allows us
 * to do so for floor.
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public interface Interface_Floor {  
    
    /**
     * Returns floor's callbox
     * 
     * @since Version 1.0
     * @see #cBox
     * @return floor's callbox used to signal the controller 
     */
    public Interface_Callbox getcBox();
    
    /**
     * Returns the current list of people on the floor
     * 
     * @since Version 1.0
     * @see #peopleOnFloor
     * @return floor's collection of people currently on the floor
     */
    public ArrayList<Interface_Person> getPeopleOnFloorList();

    /**
     * Returns floor's height number
     * 
     * @since Version 1.0
     * @see #floorNumber
     * @return floor's int meant to represent it height in the building
     */
    public int getFloorNumber();
    
    /**
     * Adds a person to the floor
     * 
     * @since Version 1.0
     * @see #getPeopleOnFloorList()
     */
    public void addPersonToFloor(Interface_Person theGuy);
    
    /**
     * This is for sending them to elevator when it arrives on floor. Floors
     * are asked to perform this method
     * 
     * @since Version 1.0
     * @see #getPeopleOnFloorList()
     */
    public TreeMap<Integer, Interface_Person> sendPassengersToElevator(int elevator) throws IllegalElevatorException;
    
    /**
     * Returns the amount of people on the floor list
     * 
     * @since Version 1.0
     * @see #getPeopleOnFloorList()
     * @return an int which is the amount of people on the floor list
     */
    public int getNumberOfWaitingPeopleCurrentlyOnFloor();
    
    /**
     * Returns the amount of people on the floor list who've completed
     * 
     * @since Version 1.0
     * @see #getPersonsCompleted()
     * @return an int which is the amount of people on the floor list who've completed
     */
    public int getNumberOfCompletedPeopleCurrentlyOnFloor();
    
    /**
     * This method iterates through each floor and checks
     * whether there are any people still waiting on the floor
     * It checks the scenarios where there are people and their
     * callbox request is still lit and other scenarios where
     * there are people on the floor and their callbox is no 
     * longer lit. 
     * 
     * The method returns a validation boolean for the 
     * elevatorsComplete variable. So it starts with the variable
     * as true and if it doesn't pass the conditional tests
     * then it marks that variable false. Finnally, it returns
     * that variable
     * 
     * @since Version 1.0
     * @see #getPeopleOnFloorList()
     * @return a boolean indicating whether there are people remaining on floor
     */
    public boolean lastCall();
    
    /**
     * Adds a person to list those who've completed
     * 
     * @since Version 1.0
     * @see #getPersonsCompleted()
     */
    public void addToCompletedList(Interface_Person person);
    
    /**
     * Returns the state of the up button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @return a boolean which is the state of the up button
     */
    public boolean getCallboxUp();
    
    /**
     * Returns the state of the down button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @return a boolean which is the state of the down button
     */
    public boolean getCallboxDown();
    
    /**
     * Sets the state of the up button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @param boolean direction
     * @throws IllegalDirectionException if the argument provided is not true or false
     */
    public void setCallboxUp(boolean direction) throws IllegalDirectionException;
    
    /**
     * Sets the state of the down button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @param boolean direction
     * @throws IllegalDirectionException if the argument provided is not true or false
     */
    public void setCallboxDown(boolean direction) throws IllegalDirectionException;
    
    /**
     * Locates a particular person in the floor's people list by index
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @param int pIndex
     */
    public Interface_Person findPersonIndexInWaitingList(int pIndex);
    
    /**
     * Returns the list of people who've completed their journey on the floor
     * 
     * @since Version 1.0
     * @see #personsCompleted
     * @return floor's collection of people finished on that floor
     */
    public ArrayList<Interface_Person> getPersonsCompleted();
    
    /**
     * Returns the average wait time after calculation
     * 
     * @since Version 1.0
     * @see #getPersonsDeparted()
     * @return a double which is the average wait time after calculation
     */
    public double getAvgWaitTime();
    
    /**
     * Returns the minimum wait time after calculation
     * 
     * @since Version 1.0
     * @see #getPersonsDeparted()
     * @return a double which is the minimum wait time after calculation
     */
    public double getMinWaitTime();
    
    /**
     * Returns the maximum wait time after calculation
     * 
     * @since Version 1.0
     * @see #getPersonsDeparted()
     * @return a double which is the maximum wait time after calculation
     */
    public double getMaxWaitTime();
    
    /**
     * Returns the list of people who've left from that floor
     * 
     * @since Version 1.0
     * @see #personsDeparted
     * @return floor's collection of people who've left from that floor
     */
    public ArrayList<Double> getPersonsDeparted();
}
