/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ver3;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Kevin
 */
public class SE350UnitTests extends TestCase {
    
    public SE350UnitTests(String testName) {
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
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    
    /**
     * T1: This is a user-end test that does: _______
     */
    /*public void testT1() {
        fail("This test has not yet been written.");
    }*/
    
    /*
     //doing e.run() makes the test continually run and never stop.  Hard to test this
    public void testIsRunning() {
        Entity_WorkingElevator e = new Entity_WorkingElevator();
        e.run();
        assertTrue(e.getIsRunning());
        e.shutDown();
    }
    */
    /*public void testSendToFloorTwo(){
        Singl_WorkingController instance = Singl_WorkingController.getInstance();
        Singl_WorkingBuilding b = Singl_WorkingBuilding.getInstance();
        Interface_Elevator expected = new Entity_WorkingElevator();
        expected.startUp();
        
        expected.addToDestinationList(2);
        instance.disPatchElevator(2, 1);
        assertTrue(expected.equals(instance));
        //assertEquals(expected, b.getElevatorList().get(new Integer(1)));
        
        
        
        //expected.addToDestinationList(2);
        //System.out.println(b.getElevatorList().get(new Integer(1)).toString());
        
    }*/
    
    /**
     * Tests to see if elevator will accept a negative number
     */
    public void testNegativeElevatorNum() {
        Singl_WorkingController instance = Singl_WorkingController.getInstance();
        try {
            instance.disPatchElevator(1, -1);
            fail("Exception should have occurred, your dispatch accepted a negative elevator number!");
        }
        catch(Exception e) {
            
        }
            
    }
    
    /**
     * Tests to see if an elevator number outside range is accepted by elevator.
     * Attempts with getNumberOfElevators()+1.
     */
    public void testInvalidElevatorNum() {
        Singl_WorkingController instance = Singl_WorkingController.getInstance();
        Singl_WorkingBuilding b = Singl_WorkingBuilding.getInstance();
        try {
            instance.disPatchElevator(1, b.getNumberOfElevators()+1);
            fail("Exception should have occurred, elevator number is too high!");
        }
        catch(Exception e) {
            
        }
    }
    
    /**
     * Tests to see if elevator will accept a negative floor value
     */
    public void testNegativeFloorNum() {
        Singl_WorkingController instance = Singl_WorkingController.getInstance();
        try {
            instance.disPatchElevator(-1, 1);
            fail("Exception should have occurred your elevator accepted a negative floor!");
        }
        catch(Exception e) {
            
        }
    }
    
    /**
     * Tests to see if elevator will accept a floor value too high (that doesn't exist).
     * Attempts with getNumberOfFloors()+1
     */
    public void testInvalidFloorNum() {
        Singl_WorkingController instance = Singl_WorkingController.getInstance();
        Singl_WorkingBuilding b = Singl_WorkingBuilding.getInstance();
        try {
            instance.disPatchElevator(b.getNumberOfFloors()+1, 1);
            fail("Exception should have occurred, your elevator accepted an invalid floor number!");
        }
        catch(Exception e) {
            
        }

    }
    
    
}
    
    