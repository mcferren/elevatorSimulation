package ver3;

import java.text.SimpleDateFormat;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;
import ver3.project_exceptions.IllegalSimulationException;

/**
 *
 * This is a class representing a Person.  A person arrives on a floor, requests an elevator
 * either up or down using the Callbox, then rides the elevator and exits on their new floor.
 * @author Ben McFerren 
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class Entity_WorkingPerson implements Interface_Person {
 
    /*
     * startFloor is an integer variable that represents the original floor the person
     * gets on at.
     * @since Version 1.0
     */
    private int startFloor;
    /*
     * intendedDestination is an integer that represents the floor that the person wants to ride the elevator to.
     * @since Version 1.0
     */
    private int intendedDestination;
    /*
     * intendedDirection is an integer that represents the direction of travel that the person
     * wishes to ride the elevator.
     * @since Version 1.0
     */
    private int intendedDirection;
    /*
     * sdf is a SimpleDateFormat variable that represents the hour, minute, and second in the format
     * HH:mm:ss
     * @since Version 1.0
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    /*
     * serialNumber is an integer that represents the serial number of the person.
     * @since Version 1.0
     */
    private int serialNumber;
    /*
     * serialCount is a static integer that repsents the number of people created with serial numbers.
     * It is static because it is a global count for all people, incremented each time a new person
     * is created.
     * @since Version 1.0
     */
    private static int serialCount = 0; // static so we can use number to assign 
                                        // serial number to person object
    /*
     * startOnFloorTime is a long number that represents the starting time when the person requests their
     * elevator.
     * @since Version 1.0
     */
    private long startOnFloorTime;
    /*
     * enterElevatorTime is a long number that represents the time when the person enters the elevator.
     * @since Version 1.0
     */
    private long enterElevatorTime;
    /*
     * arrivalTime is a long number that represnts the time that the elevator reaches the person's destination
     * @since Version 1.0
     */
    private long arrivalTime;

    /*
     * This is the public constructor for the class Entity_WorkingPerson.  It takes parameters
     * cF and iD, cF is an integer that represents the starting floor and iD is an integer that
     * represents the intended destination floor.
     * @param cF, iD
     * @see #setSerialNumber
     * @see #serialCount
     * @see #setStartFloor
     * @see #setIntendedDestination
     * @see #setIntendedDirection
     * @see #setStartOnFloorTime
     * @see #getIntendedDestination
     * @since Version 1.0
     */
    public Entity_WorkingPerson(int cF, int iD) {

        try{
            setSerialNumber(++serialCount); // set first then increments
            setStartFloor(cF);
            setIntendedDestination(iD);
            setIntendedDirection();
            setStartOnFloorTime(System.currentTimeMillis());
            System.out.println(sdf.format(System.currentTimeMillis()) + "\tA new " 
                        + "person #" + getSerialNumber() + " has been created and "
                        + "placed on floor #" + getStartFloor()
                        + " - This person wants to go to floor " 
                        + getIntendedDestination());
            
        } catch (IllegalSimulationException ex) {
            ex.printStackTrace();
        } catch (IllegalFloorException ex) {
            ex.printStackTrace();
        }      
    }
    
    /*
     * This method allows the person to press a button on the callbox, either up or down.
     * @see #getIntendedDestination
     * @see #getStartFloor
     * @since Version 1.0
     */
    public void pressCallbox(){
        
        int myDir;
        if(getIntendedDestination() > getStartFloor())
            myDir = 1;
        else
            myDir = -1;
        
        try
        {
            Interface_Callbox myCallbox = Singl_WorkingBuilding.getInstance()
                                            .getFloorList()
                                            .get(getStartFloor() - 1)
                                            .getcBox();
                 
            // make sure my intended (direction) button is not already pressed
            if(myDir == 1 && myCallbox.getUpButton() == false)
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "Person #" + getSerialNumber() 
                        + " presses callbox " + (myDir == 1 ? "up" : "down") 
                        + " button on floor #" + getStartFloor());
                myCallbox.setUpButton(true);
            }
            else if(myDir == 1 && myCallbox.getUpButton() == true)
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "Person #" + getSerialNumber() + " wants to press callbox " 
                        + (myDir == 1 ? "up" : "down") 
                        + " button on floor #" + getStartFloor()
                        + ", but it has already been pressed");
            }

            if(myDir == -1 && myCallbox.getDownButton() == false)
            {
                System.out.println(sdf.format(System.currentTimeMillis()) 
                        + "\tPerson #" + getSerialNumber() + " presses callbox " 
                        + (myDir == 1 ? "up" : "down") 
                        + " button on floor #" + getStartFloor());
                myCallbox.setDownButton(true);
            }
            else if(myDir == -1 && myCallbox.getDownButton() == true)
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "Person #" + getSerialNumber() 
                        + " wants to press callbox " + (myDir == 1 ? "up" : "down") 
                        + " button on floor #" + getStartFloor()
                        + ", but it has already been pressed");
            }
        } catch (IllegalDirectionException ex) {
            ex.printStackTrace();
        } catch(IndexOutOfBoundsException ex) {
            System.out.printf("Error occurred while trying to get index %d of "
                    + "floor inside pressCallbox() method: %s\n", 
                    ex.getMessage(), (getStartFloor() - 1));
        } 
    }

    /*
     * This method returns the floor that the person started on represented as an integer.
     * @return int which is the floor that the person will start his journey
     * @see #startFloor
     * @since Version 1.0
     */
    public int getStartFloor() {
        return startFloor;
    }
    
    /*
     * This method represents the intended destination floor represented as an integer.
     * @return int which represents the floor that the person is going to
     * @see #intendedDestination
     * @since Version 1.0
     */
    public int getIntendedDestination() {
        return intendedDestination;
    }

    /*
     * This method returns the serial number for that specific person represented as an integer.
     * @return int which represents the serial number that identifies the person
     * @see #serialNumber
     * @since Version 1.0
     */
    public int getSerialNumber() {
        return serialNumber;
    }
    
    /*
     * This method returns the intended destination for a person represented as an integer.
     * @return int that determines the direction the person intends to head to
     * @see #intendedDirection
     * @since Version 1.0
     */
    public int getIntendedDirection() {
        return intendedDirection;
    }

    /*
     * This method marks the arrival time (when the person requests the elevator)
     * using the current system time.
     * @see #setArrivalTime
     * @since Version 1.0
     */
    public void markArrivalTime(){
        
        try {
            setArrivalTime(System.currentTimeMillis());
        } catch (IllegalSimulationException ex) {
            ex.printStackTrace();
        } 
    }
    
    /*
     * This method marks the time that the person enters their elevator using the current system time.
     * @see #setEnterElevatorTime
     * @since Version 1.0
     */
    public void markEnterElevatorTime(){
        
        try {
            setEnterElevatorTime(System.currentTimeMillis());
        } catch (IllegalElevatorException ex) {
            ex.printStackTrace();
        } 
    }
    
    /*
     * Method that returns the time the person waited on their floor before entering the elevator
     * represented as a double.
     * @return a double which is the calculated wait time the person had to expend
     * @see #getEnterElevatorTime
     * @since Version 1.0
     */
    public double getWaitTime(){
        
        double difference = (getEnterElevatorTime() - getStartOnFloorTime());
        
        int timeScale = Singl_WorkingSimulation.getInstance().getTimeScaleFactor();        
        
        double adjusted = (difference / 1000) * timeScale;
        
        return adjusted;
    }
    
    /*
     * This method returns the time the person rode the elevator for represented as a double.
     * @return double which is the calculated ride time that the person had to expend
     * @see #getArrivalTime
     * @since Version 1.0
     */
    public double getRideTime(){
        
        double difference = (getArrivalTime() - getEnterElevatorTime());
        
        int timeScale = Singl_WorkingSimulation.getInstance().getTimeScaleFactor();        
        
        double adjusted = (difference / 1000) * timeScale;
        
        return adjusted;
    }

    
    /*
     * This method returns the time the person left the elevator at represented as a long.
     * @see #arrivalTime
     * @return long which represents a timestamp when the person arrived at his destination
     * @since Version 1.0
     */
    public long getArrivalTime() {
        return arrivalTime;
    }

    /*
     * This method returns the time that the person entered the elevator at represented as a long.
     * @see #enterElevatorTime
     * @return long which represents a timestamp when the elevator got on the elevator
     * @since Version 1.0
     */
    public long getEnterElevatorTime() {
        return enterElevatorTime;
    }

    /*
     * This method returns the time that the person got on their starting floor and requested the elevator
     * represented as a long.
     * @see #startOnFloorTime
     * @return long that represents the timestamp when the person started on his journey
     * @since Version 1.0
     */
    public long getStartOnFloorTime() {
        return startOnFloorTime;
    }
    
    /*
     * This method sets the intended direction for the person based on the floor they
     * are intending to visit.
     * @throws IllegalFloorException
     * @see #getIntendedDestination
     * @see #getStartFloor
     * @see #getIntendedDestination
     * @see #intendedDestination
     * @since Version 1.0
     */
    private void setIntendedDirection() throws IllegalFloorException {
        
        if(getIntendedDestination() < 0 
                || getIntendedDestination() > Singl_WorkingSimulation
                                                           .getInstance()
                                                           .getNumberOfFloors())
        {
            throw new IllegalFloorException("While setting person intended direction"
                    + ", an invalid intended destination was received " 
                    + getIntendedDestination());
        }
        else if(getStartFloor() < 0 
                || getStartFloor() > Singl_WorkingSimulation.getInstance()
                                                           .getNumberOfFloors())
        {
            throw new IllegalFloorException("While setting person intended direction"
                    + ", an invalid start destination was received " 
                    + getStartFloor());
        }
        else if(getStartFloor() == getIntendedDestination())
        {
            throw new IllegalFloorException("While setting person intended direction"
                    + ", start destination cannot equal intended destination" 
                    + "\nStart: " + getStartFloor()
                    + "\nIntended Destinatino: " + getIntendedDestination());
        }
        else        
        {            
            if(getIntendedDestination() > getStartFloor())
                intendedDirection = 1;
            else
                intendedDirection = -1;   
        }
    }
    
    /*
     * This method sets the serial number of the person given the inputted serial number
     * @param sN
     * @throws IllegalSimulationException
     * @see #serialNumber
     * @since Version 1.0
     */
    private void setSerialNumber(int sN) throws IllegalSimulationException {
 
        if(sN < 0)
            throw new IllegalSimulationException("Negative person serial number "
                                                + "floor encountered: " + sN);
        else        
            serialNumber = sN;
    }

    /*
     * This method sets the intended destination floor of the person.
     * @param iD
     * @throws IllegalFloorException
     * @see #intendedDestination
     * @since Version 1.0
     */
    private void setIntendedDestination(int iD) throws IllegalFloorException {
        
        if(iD < 0 || iD > Singl_WorkingSimulation.getInstance().getNumberOfFloors())
            throw new IllegalFloorException("Invalid intended destination "
                                                 + " floor encountered: " + iD);
        else
            intendedDestination = iD;
    }

    /*
     * This method sets the floor that the person comes from given an integer as a parameter.
     * @param sF
     * @throws IllegalFloorException
     * @see #statFloor
     * @since Version 1.0
     */
    private void setStartFloor(int sF) throws IllegalFloorException {
        
        if(sF < 0 || sF > Singl_WorkingSimulation.getInstance().getNumberOfFloors())
            throw new IllegalFloorException("Invalid start floor encountered: " + sF);
        else
            startFloor = sF;
    }

    /*
     * This method sets the time that the person entered the elevator at given a long as a parameter.
     * @param eT
     * @throws IllegalElevatorException
     * @see #enterElevatorTime
     * @since Version 1.0
     */
    private void setEnterElevatorTime(long eT) throws IllegalElevatorException {
        
        if(eT < 0)
            throw new IllegalElevatorException("Invalid time encountered when "
                                                    + "setting EnterElevatorTime: " + eT);
        else
            enterElevatorTime = eT;
    }

    /*
     * This method sets the time that the person started on their floor through a parameter long.
     * @param sT
     * @throws IllegalFloorException
     * @see #startOnFloorTime
     * @since Version 1.0
     */
    private void setStartOnFloorTime(long sT) throws IllegalFloorException {

        if(sT < 0)
            throw new IllegalFloorException("Invalid time encountered when "
                                                    + "setting EnterElevatorTime: " + sT);
        else
            startOnFloorTime = sT;
    }

    /*
     * This method sets the time that the person arrived at their destined floor.
     * @param aT
     * @throws IllegalSimulationException
     * @see arrivalTime
     * @since Version 1.0
     */
    private void setArrivalTime(long aT) throws IllegalSimulationException {

        if(aT < 0)
            throw new IllegalSimulationException("Invalid time encountered when "
                                                    + "setting ArrivalTime: " + aT);
        else
            arrivalTime = aT;
    }

}
