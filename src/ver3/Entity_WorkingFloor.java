package ver3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a class that represents a floor class. It implements the floor interface
 * and has two private local variables that generate a serial number associated
 * with each floor object instantiated.
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 * @see Interface_Floor
 * @see Fact_FloorFactory
 */
public class Entity_WorkingFloor implements Interface_Floor {
    
    /**
    * The floor's serial number with regard to height.
    * 
    * @since Version 1.0
    * @see #setFloorNumber()
    */
    private int floorNumber;
    
    /**
    * The floor's callbox used to signal the controller
    * 
    * @since Version 1.0
    * @see #getCallboxUp()
    * @see #getCallboxDown()
    * @see #setCallboxUp()
    * @see #setCallboxDown()
    * @see #getLastCall()
    */
    private Interface_Callbox cBox;

    /**
    * The floor's serial number with regard to height.
    * 
    * @since Version 1.0
    * @see #peopleOnFloor
    * @see #getPeopleOnFloorList() 
    * @see #sendPassengersToElevator(int elevator) 
    */
    private ArrayList <Interface_Person> peopleOnFloor 
            = new ArrayList <Interface_Person>();

    /**
    * The floor's serial list of people who have exited on that floor
    * 
    * @since Version 1.0
    * @see #personsCompleted
    * @see #getPersonsCompleted() 
    * @see #getNumberOfCompletedPeopleCurrentlyOnFloor() 
    */
    private ArrayList <Interface_Person> personsCompleted 
            = new ArrayList <Interface_Person>();

    /**
    * The floor's serial list of people who have left from that floor
    * 
    * @since Version 1.0
    * @see #personsDeparted
    * @see #getPersonsDeparted() 
    * @see #getAvgWaitTime()() 
    * @see #getMinWaitTime()() 
    * @see #getMaxWaitTime()() 
    */    
    private ArrayList <Double> personsDeparted 
        = new ArrayList <Double>();
    
    /**
    * A static int used to label the floor number.
    *
    * @since Version 1.0
    * @see #Entity_WorkingFloor()
    */
    private static int floorCount = 0;
    
    /**
     * This variable SimpleDateFormat simply sets the format of our date and time
     * for tracking purposes.
     * @since Version 1.0
     */  
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * A public constructor used to create a floor object. It takes no arguments
     * and it calls the setFloorNumber method and instantiates a callbox. This 
     * throws any propagated exceptions that are generated by the setFloorNumber() 
     * method.
     * 
     * @see #setFloorNumber(int)
     * @since Version 1.0
     */
    public Entity_WorkingFloor() {
        
        try
        {
            setFloorNumber(++floorCount);
        } catch (IllegalFloorException ex) {
            ex.printStackTrace();
        }
        
        setcBox(Fact_CallboxFactory.build(getFloorNumber()));
        
    }

    /**
     * Returns floor's height number
     * 
     * @since Version 1.0
     * @see #floorNumber
     * @return floor's int meant to represent it height in the building
     */
    public int getFloorNumber() {
        return floorNumber;
    }    
    
    /**
     * Returns floor's callbox
     * 
     * @since Version 1.0
     * @see #cBox
     * @return floor's callbox used to signal the controller 
     */
    public Interface_Callbox getcBox() {
        return cBox;
    } 
    
    /**
     * Returns the current list of people on the floor
     * 
     * @since Version 1.0
     * @see #peopleOnFloor
     * @return floor's collection of people currently on the floor
     */
    public ArrayList<Interface_Person> getPeopleOnFloorList() {
        return peopleOnFloor;
    }
    
    /**
     * Returns the list of people who've completed their journey on the floor
     * 
     * @since Version 1.0
     * @see #personsCompleted
     * @return floor's collection of people finished on that floor
     */
    public ArrayList<Interface_Person> getPersonsCompleted() {
        return personsCompleted;
    }
    
    /**
     * Returns the list of people who've left from that floor
     * 
     * @since Version 1.0
     * @see #personsDeparted
     * @return floor's collection of people who've left from that floor
     */
    public ArrayList<Double> getPersonsDeparted() {
                
        Collections.sort(personsDeparted);
        
        return personsDeparted;
    }
    
    /**
     * Adds a person to the floor
     * 
     * @since Version 1.0
     * @see #getPeopleOnFloorList()
     */
    public void addPersonToFloor(Interface_Person theGuy){
        
        getPeopleOnFloorList().add(theGuy);
        
    }
    
    /**
     * This is for sending them to elevator when it arrives on floor. Floors
     * are asked to perform this method
     * 
     * @since Version 1.0
     * @see #getPeopleOnFloorList()
     */
    public TreeMap<Integer, Interface_Person> sendPassengersToElevator(int elevator) 
                                                throws IllegalElevatorException {
        
        if(elevator < 0 || elevator > Singl_WorkingSimulation.getInstance().getNumberOfElevators())
            throw new IllegalElevatorException("Invalid number of elevators "
                                                       + "encountered: " + elevator);
        
        // this list is used to collect all new passenger floor requests
        TreeMap<Integer, Interface_Person> newPassengerRequests 
                = new TreeMap<Integer, Interface_Person>();
        
        
        // acknowledge elevator you're sending passengers to
        Interface_Elevator focusElevator = Singl_WorkingBuilding.getInstance()
                                            .getElevatorList().get(elevator);
        
        
        // recognize max allowed people on elevator
        int maxPeople = Singl_WorkingSimulation.getInstance().getMaxPersonsPerElevator();
        
        
        // allow any passengers on the floor to enter elevator 
        // if they wish (match direction)
        if(focusElevator.getPassengerList().size() < maxPeople)
        {
            
            // this list is used to remove people from the 
            // floor list after they've entered elevator
            List <Interface_Person> tempRemovalList = new ArrayList <Interface_Person>();
            
            for(Interface_Person person : getPeopleOnFloorList())
            {                
                // if the person intends to go in the same direction as the elevator
                // then they will enter the elevator
                if(person.getIntendedDirection() == focusElevator.getDirection() 
                        && focusElevator.getPassengerList().size() < maxPeople)
                {
                    // we mark the person for removal from the floor's list of people and we
                    // use a temparraylist so we're not pulling up boards as we walk on bridge
                    tempRemovalList.add(person);
                    
                    // person enters elevator
                    focusElevator.addToPassengerList(person);
                    
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tPerson %d is now entering elevator %c%d floor %d.\n", 
                            person.getSerialNumber(),
                            focusElevator.getLetterName(), 
                            focusElevator.getSerialNumber(),
                            focusElevator.getCurrentFloor()
                    );              
                    
                    
                    // collect requests from new passengers
                    // if the floor has not yet been pressed in the elevator, then add to 
                    // the newPassengerRequests list that will be processed below
                    if(!focusElevator.getDestinationList().contains(person.getIntendedDestination())
                            && !newPassengerRequests.containsKey(person.getIntendedDestination())
                            )
                    {
                        System.out.printf(sdf.format(System.currentTimeMillis())
                        + "\tNow that he is in the elevator %c%d, person %d "
                        + "presses the button for floor %d\n", 
                        focusElevator.getLetterName(), 
                        focusElevator.getSerialNumber(),
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
                            focusElevator.getLetterName(), 
                            focusElevator.getSerialNumber(), 
                            person.getIntendedDestination());
                    }
                }
                else if(person.getIntendedDirection() == focusElevator.getDirection() 
                        && focusElevator.getPassengerList().size() == maxPeople)
                {
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tPerson %d on floor %d is frustrated with "
                            + "elevator %c%d\n\t\tbecause it has opened on his "
                            + "floor but it now is too full\n\t\tof people.\n", 
                            person.getSerialNumber(),
                            focusElevator.getCurrentFloor(),
                            focusElevator.getLetterName(), 
                            focusElevator.getSerialNumber()
                    );  
                    
                }
                else if(person.getIntendedDirection() != focusElevator.getDirection())
                {
                    System.out.printf(sdf.format(System.currentTimeMillis()) 
                            + "\tPerson %d on floor %d is frustrated with "
                            + "elevator %c%d \n\t\tbecause it has opened on his "
                            + "floor but it is going\n\t\tin the wrong direction. "
                            + "Person %d wants to go %s to floor %d\n", 
                            person.getSerialNumber(),
                            focusElevator.getCurrentFloor(),
                            focusElevator.getLetterName(), 
                            focusElevator.getSerialNumber(), 
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
                int indexOfPersonToRemove = getPeopleOnFloorList().indexOf(person);
                getPeopleOnFloorList().remove(indexOfPersonToRemove);
            }
        }
        
        return newPassengerRequests;
    }
    
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
    public boolean lastCall(){
        
        boolean elevatorsComplete = false;
        
        boolean phantomUpCovered = false;
        boolean phantomDownCovered = false;
        boolean ignoredUpCovered = false;
        boolean ignoredDownCovered = false;

        for(Interface_Person guy : getPeopleOnFloorList())
        {
            // is their callbox button lit? 
            // if not have them try to press it  
            if(guy.getIntendedDirection() == 1 && getcBox().getUpButton() == false
                    && phantomUpCovered == false)
            {
                guy.pressCallbox();
                phantomUpCovered = true;
                elevatorsComplete = false;
            }
            else if(guy.getIntendedDirection() == -1 && getcBox().getDownButton() == false
                    && phantomDownCovered == false)
            {
                guy.pressCallbox();
                phantomDownCovered = true;
                elevatorsComplete = false;
            }
            // Has the callbox button been ignored? if so, invoke pick  
            else if(guy.getIntendedDirection() == 1 && getcBox().getUpButton() == true
                    && ignoredUpCovered == false)
            {
                try {
                    Singl_WorkingController.getInstance().pick(getFloorNumber(), 1);
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                }catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
                
                ignoredUpCovered = true;
                elevatorsComplete = false;
            }
            else if(guy.getIntendedDirection() == -1 && getcBox().getDownButton() == true
                    && ignoredDownCovered == false)
            {
                try {
                    Singl_WorkingController.getInstance().pick(getFloorNumber(), -1);
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                }
                
                ignoredUpCovered = true;
                elevatorsComplete = false;
            }
        }
        
        return elevatorsComplete;
    }
    
    /**
     * Returns the amount of people on the floor list
     * 
     * @since Version 1.0
     * @see #getPeopleOnFloorList()
     * @return an int which is the amount of people on the floor list
     */
    public int getNumberOfWaitingPeopleCurrentlyOnFloor(){
        return getPeopleOnFloorList().size();
    }
    
    /**
     * Returns the amount of people on the floor list who've completed
     * 
     * @since Version 1.0
     * @see #getPersonsCompleted()
     * @return an int which is the amount of people on the floor list who've completed
     */
    public int getNumberOfCompletedPeopleCurrentlyOnFloor(){
        return getPersonsCompleted().size();
    }
    
    /**
     * Adds a person to list those who've completed
     * 
     * @since Version 1.0
     * @see #getPersonsCompleted()
     */
    public void addToCompletedList(Interface_Person person){
        getPersonsCompleted().add(person);
    }
    
    /**
     * Returns the state of the up button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @return a boolean which is the state of the up button
     */
    public boolean getCallboxUp(){
        return getcBox().getUpButton();
    }
    
    /**
     * Returns the state of the down button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @return a boolean which is the state of the down button
     */
    public boolean getCallboxDown(){
        return getcBox().getDownButton();
    }
    
    /**
     * Sets the state of the up button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @param boolean direction
     * @throws IllegalDirectionException if the argument provided is not true or false
     */
    public void setCallboxUp(boolean direction) throws IllegalDirectionException {
        if(!(direction == true || direction == false))
            throw new IllegalDirectionException("Incorrect direction type encountered "
                                 + "when invoking setCallboxDown: " + direction);
        else
        {
            try {
                getcBox().setUpButton(direction);
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Sets the state of the down button
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @param boolean direction
     * @throws IllegalDirectionException if the argument provided is not true or false
     */
    public void setCallboxDown(boolean direction) throws IllegalDirectionException {

        if(!(direction == true || direction == false))
            throw new IllegalDirectionException("Incorrect direction type encountered "
                                 + "when invoking setCallboxDown: " + direction);
        else
        {
            try {
                getcBox().setUpButton(direction);
            } catch (IllegalDirectionException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Locates a particular person in the floor's people list by index
     * 
     * @since Version 1.0
     * @see #getcBox()
     * @param int pIndex
     */
    public Interface_Person findPersonIndexInWaitingList(int pIndex){
        return getPeopleOnFloorList().get(pIndex);
    }
    
    /**
     * Returns the average wait time after calculation
     * 
     * @since Version 1.0
     * @see #getPersonsDeparted()
     * @return a double which is the average wait time after calculation
     */
    public double getAvgWaitTime(){
        
        double totalTime = 0;
        int totalEntries = 0;

        
        if(!getPersonsDeparted().isEmpty())
        {
            for(Double time : getPersonsDeparted())
            {
                totalTime += time;
                ++totalEntries;
            }            
            
            if(totalEntries != 0)
                return (totalTime / totalEntries);
            else
                return 0;
        }
        else
            return 0;
    }
    
    /**
     * Returns the minimum wait time after calculation
     * 
     * @since Version 1.0
     * @see #getPersonsDeparted()
     * @return a double which is the minimum wait time after calculation
     */
    public double getMinWaitTime(){
        
        if(!getPersonsDeparted().isEmpty())
            return getPersonsDeparted().get(0);
        else
            return 0;
    }
    
    /**
     * Returns the maximum wait time after calculation
     * 
     * @since Version 1.0
     * @see #getPersonsDeparted()
     * @return a double which is the maximum wait time after calculation
     */
    public double getMaxWaitTime(){
        
        int totalEntries = getPersonsDeparted().size();
        
        if(!getPersonsDeparted().isEmpty())
            return getPersonsDeparted().get(totalEntries - 1);
        else
            return 0;
    }
    
    /**
     * A private method used to set the local floorNumber variable. It takes an
     * int as an argument and it checks to ensure that the int is positive. If not,
     * propagates an InvalidDataException. Else, it sets the value of floorNumber
     * to the value of the argument provided.
     * 
     * @see #floorNumber
     * @since Version 1.0
     * @param fN the int to set the floor with
     * @throws InvalidDataException if the argument provided is less that zero
     */    
    private void setFloorNumber(int fN) throws IllegalFloorException {
        
        if(fN < 0)
            throw new IllegalFloorException("Negative number of floors encountered: " + fN);
        else
        {
            floorNumber = fN;
        }
    }
    
    /**
     * Sets the cBox with a Interface_Callbox object
     * 
     * @since Version 1.0
     * @see #cBox
     * @param Interface_Callbox callbox
     */
    private void setcBox(Interface_Callbox callbox) {
        cBox = callbox;
    }

    
}
