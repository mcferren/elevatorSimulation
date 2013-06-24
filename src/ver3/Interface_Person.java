package ver3;

/**
 *
 * @author benjamin
 */
public interface Interface_Person {
    
    /*
     * This method represents the intended destination floor represented as an integer.
     * @return int which represents the floor that the person is going to
     * @see #intendedDestination
     * @since Version 1.0
     */
    public int getIntendedDestination();

    /*
     * This method returns the serial number for that specific person represented as an integer.
     * @return int which represents the serial number that identifies the person
     * @see #serialNumber
     * @since Version 1.0
     */
    public int getSerialNumber();
    
    /*
     * This method allows the person to press a button on the callbox, either up or down.
     * @see #getIntendedDestination
     * @see #getStartFloor
     * @since Version 1.0
     */
    public void pressCallbox();
    
    /*
     * This method returns the intended destination for a person represented as an integer.
     * @return int that determines the direction the person intends to head to
     * @see #intendedDirection
     * @since Version 1.0
     */
    public int getIntendedDirection();

    /*
     * This method returns the floor that the person started on represented as an integer.
     * @return int which is the floor that the person will start his journey
     * @see #startFloor
     * @since Version 1.0
     */
    public int getStartFloor();
    
    /*
     * This method marks the time that the person enters their elevator using the current system time.
     * @see #setEnterElevatorTime
     * @since Version 1.0
     */
    public void markEnterElevatorTime();

    /*
     * This method marks the arrival time (when the person requests the elevator)
     * using the current system time.
     * @see #setArrivalTime
     * @since Version 1.0
     */
    public void markArrivalTime();
    
    /*
     * Method that returns the time the person waited on their floor before entering the elevator
     * represented as a double.
     * @return a double which is the calculated wait time the person had to expend
     * @see #getEnterElevatorTime
     * @since Version 1.0
     */
    public double getWaitTime();
    
    /*
     * This method returns the time the person rode the elevator for represented as a double.
     * @return double which is the calculated ride time that the person had to expend
     * @see #getArrivalTime
     * @since Version 1.0
     */
    public double getRideTime();
    
}
