package ver3;

import java.text.SimpleDateFormat;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a class that represents a working Callbox. The callbox is the device 
 * that calls the elevator, such as a button on each floor. The callbox sends a 
 * request to the controller, and the controller dispatches an elevator to the 
 * floor respective to the callbox.
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class Entity_WorkingCallbox implements Interface_Callbox {

    /**
     * This variable "upButton" represents the physical button to press 'up' on the Callbox.
     * It is set to false if unpressed, and true if pressed.
     * @since Version 1.0
     */
    private boolean upButton; 
    /**
     * This variable "downButton" represents the physical button to press 'down' on the Callbox.
     * It is set to false if unpressed, and true if pressed.
     * @since Version 1.0
     */
    private boolean downButton;
    /**
     * This variable "floor" represents the floor number used by the Callbox.
     * @since Version 1.0
     */
    private int floor;
    /**
     * This variable SimpleDateFormat simply sets the format of our date and time
     * for tracking purposes.
     * @since Version 1.0
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * This is the public constructor for the Callbox.
     * It will by default set both buttons to 'false', or unpressed,
     * and set the requested floor to the parameter 'flr'.
     * @param flr 
     * @since Version 1.0
     * @see #setDownButton
     * @see #setUpButton
     * @see #setFloor
     */
    public Entity_WorkingCallbox(int flr) {
        
        try
        {
            setDownButton(false);
            setUpButton(false);
            setFloor(flr);
        }   catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
        } catch (IllegalFloorException ex) {
            ex.printStackTrace();
        }
        
    }

    /*
     * This method getDownButton returns the current value of the downButton.
     * True if the button is pressed, false if the button is unpressed.
     * @return boolean downButton indication of being lit or not
     * @see #downButton
     * @since Version 1.0
     */
    public boolean getDownButton() {
        return downButton;
    }
    
    /*
     * This method getUpButton returns the current value of the upButton.
     * True if the button is pressed, false if the button is unpressed.
     * @return boolean upButton indication of being lit or not
     * @see #upButton
     * @since Version 1.0
     */
    public boolean getUpButton() {
        return upButton;
    }

    /*
     * This void method setUpButton sets the value of upButton to either true
     * if the button is pressed, or false if the upButton it unpressed.
     * @param uB
     * @see #upButton
     * @see #getUpButton
     * @throws IllegalDirectionException
     * @since Version 1.0
     */
    public void setUpButton(boolean uB) throws IllegalDirectionException {

        if(!(uB == true || uB == false))
            throw new IllegalDirectionException("Incorrect direction type encountered "
                                            + "when setting down button: " + uB);
        else  
        {
            if(getUpButton() == false && uB == true)
            {
                upButton = uB;

                System.out.println(sdf.format(System.currentTimeMillis()) + "\t"
                        + "Callbox on floor " + getFloor() + " is ringing"
                        + " with an up request");
                
                try {    
                    signal(getFloor(), 1);
                    
                } catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
            }
            else
            {
                upButton = uB;
            }
        }
    }
    
    /*
     * This void method setDownButton sets the value of downButton to either true
     * if the button is pressed, or false if the upButton it unpressed.
     * @param dB
     * @see #downButton
     * @see #getDownButton
     * @throws IllegalDirectionException
     * @since Version 1.0
     */
    public void setDownButton(boolean dB) throws IllegalDirectionException {

        if(!(dB == true || dB == false))
            throw new IllegalDirectionException("Incorrect direction type encountered "
                                            + "when setting down button: " + dB);
        else
        {
            if(getDownButton() == false && dB == true)
            {
                downButton = dB;
                
                System.out.println(sdf.format(System.currentTimeMillis()) + "\t" 
                        + "Callbox on floor " + getFloor() + " is ringing"
                        + " with a down request");
                    
                try {
                    signal(getFloor(), -1);
                }  catch (IllegalDirectionException ex) {
                    ex.printStackTrace();
                } catch (IllegalFloorException ex) {
                    ex.printStackTrace();
                }
            }
            else
            {
                downButton = dB;
            }
        }
    }

    /*
     * This is the setFloor method, which takes an integer as a parameter (flr)
     * and sets the variable floor to flr.
     * @param flr
     * @see #floor
     * @see #getFloor
     * @throws IllegalFloorException
     * @since Version 1.0
     */
    private void setFloor(int flr) throws IllegalFloorException{
        
        if(flr < 0 || flr > Singl_WorkingSimulation.getInstance().getNumberOfFloors())
            throw new IllegalFloorException("Invalid floor encountered"
                                               + " when setting floor: " + flr);
        else
            floor = flr;
    }
    
    
    /*
     * This method getFloor returns the current value of Floor as an integer.
     * @see #floor
     * @since Version 1.0
     * @return an int that represents the floor
     */  
    private int getFloor() {
        return floor;
    }
    
    /*
     * This method triggers communication between the callbox and the controller
     * It is invoked any time a person clicks a callbox button. The method includes
     * a floor and a direction parameter that it uses to help the controller
     * determine which elevator to send to the floor.
     * 
     * 
     * @param floor, direction
     * @throws IllegalDirectionException, IllegalFloorException
     * @since Version 1.0
     */
    private void signal(int floor, int direction) throws IllegalDirectionException
                                            , IllegalFloorException {
        
        if(floor < 0 || floor > Singl_WorkingSimulation.getInstance().getNumberOfFloors())
            throw new IllegalFloorException ("Invalid floor encountered "
                                            + "when trying to signal: " + floor);
        else if(!(direction == 1 || direction == -1))
            throw new IllegalDirectionException ("Invalid direction encountered "
                                            + "when trying to signal: " + direction);
        try{
            Singl_WorkingController.getInstance().pick(floor, direction);
        } catch (IllegalDirectionException ex) {
            ex.printStackTrace();
        } catch (IllegalFloorException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
