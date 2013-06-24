package ver3;

import ver3.project_exceptions.IllegalDirectionException;

/**
 * This is an interface used so callboxes can be instantiated in an abstract way. 
 * We want to code to interface (role) and this interface allows us to do so for 
 * callboxes.
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public interface Interface_Callbox {
    
    /*
     * This method getDownButton returns the current value of the downButton.
     * True if the button is pressed, false if the button is unpressed.
     * @return boolean downButton indication of being lit or not
     * @see #downButton
     * @since Version 1.0
     */
    public boolean getDownButton();
    
    /*
     * This void method setDownButton sets the value of downButton to either true
     * if the button is pressed, or false if the upButton it unpressed.
     * @param dB
     * @see #downButton
     * @see #getDownButton
     * @throws IllegalDirectionException
     * @since Version 1.0
     */
    public void setDownButton(boolean dB) throws IllegalDirectionException;
    
    /*
     * This method getUpButton returns the current value of the upButton.
     * True if the button is pressed, false if the button is unpressed.
     * @return boolean upButton indication of being lit or not
     * @see #upButton
     * @since Version 1.0
     */
    public boolean getUpButton();
    
    /*
     * This void method setUpButton sets the value of upButton to either true
     * if the button is pressed, or false if the upButton it unpressed.
     * @param uB
     * @see #upButton
     * @see #getUpButton
     * @throws IllegalDirectionException
     * @since Version 1.0
     */
    public void setUpButton(boolean uB) throws IllegalDirectionException;
    
}
