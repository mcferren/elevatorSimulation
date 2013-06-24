/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ver3;

import java.text.SimpleDateFormat;
import junit.framework.TestCase;

/**
 *
 * @author Kevin
 */
public class Singl_WorkingControllerTest extends TestCase {
    
    public Singl_WorkingControllerTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
        System.out.println(sdf.format(System.currentTimeMillis()) + "\tCreating Building…");
        
        //instatiate building, populate floors and elevators into building
        Singl_WorkingBuilding bInfo = Singl_WorkingBuilding.getInstance();
        //We do not pass arguments bc Singl_WorkingBuiling is a Singleton
        //A singleton with parameters is not a singleton.
        //Information on floor quantity and elevator quantity is found in programData.xml
        
        int totalElevators = bInfo.getNumberOfElevators();
        int totalFloors = bInfo.getNumberOfFloors();
        
        System.out.println(sdf.format(System.currentTimeMillis()) + "\tBuilding created, "
                          + totalFloors + " Floors, " + totalElevators + " Elevators.…");
                
        //just starting up elevators 1 and 2
        try
        {
            bInfo.getElevatorList().get(0).startUp(); 
            bInfo.getElevatorList().get(1).startUp(); 
            
        } catch(IndexOutOfBoundsException ex) {
                        System.out.println("Error occurred while trying to get "
                                + "illegal index of elevator list while starting "
                                + "up: " + ex.getMessage());
        }
        //super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getInstance method, of class Singl_WorkingController.
     * Makes sure Singl_WorkingController is Singleton and the two instances are the same.
     */
    public void testGetInstance() {
        System.out.println("getInstance");
        Singl_WorkingController expResult = Singl_WorkingController.getInstance();
        Singl_WorkingController result = Singl_WorkingController.getInstance();
        assertEquals(expResult, result);
    }

    /**
     * Test of disPatchElevator method, of class Singl_WorkingController.
     */
    /*
    public void testDisPatchElevator() {
        System.out.println("disPatchElevator");
        int floorRequested = 2;
        int elevatorNumber = 1;
        Singl_WorkingController instance = Singl_WorkingController.getInstance();
        instance.disPatchElevator(floorRequested, elevatorNumber);
        //Test to make sure the elevator goes to the floor requested here
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
