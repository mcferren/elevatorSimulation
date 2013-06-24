/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ver3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Kevin
 */
public class Singl_WorkingBuildingTest extends TestCase {
    
    public Singl_WorkingBuildingTest(String testName) {
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
     * Test of getInstance method, of class Singl_WorkingBuilding.
     * Verifies that Building is in fact Singleton, only creates one instance.
     */
    public void testGetInstance() {
        System.out.println("getInstance");
        Singl_WorkingBuilding expResult = Singl_WorkingBuilding.getInstance();
        Singl_WorkingBuilding result = Singl_WorkingBuilding.getInstance();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getNumberOfElevators method, of class Singl_WorkingBuilding.
     * Tests that number of elevators is 6 (this should be defined in the XML)
     */
    public void testGetNumberOfElevators() {
        System.out.println("getNumberOfElevators");
        Singl_WorkingBuilding instance = Singl_WorkingBuilding.getInstance();
        
        int expResult = 0;
        int result = instance.getNumberOfElevators();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getNumberOfFloors method, of class Singl_WorkingBuilding.
     * Tests that number of floors is 11 (this should be defined in the XML)
     */
    public void testGetNumberOfFloors() {
        System.out.println("getNumberOfFloors");
        Singl_WorkingBuilding instance = Singl_WorkingBuilding.getInstance();
        //DTO_WorkingBuilding dto = instance.getBuildingDTO();
        int expResult = 0;
        int result = instance.getNumberOfFloors();
        assertEquals(expResult, result);
    }

    /**
     * Test of getElevatorList method, of class Singl_WorkingBuilding.
     * Currently does not work.
     */
    public void testGetElevatorList() {
        System.out.println("getElevatorList");
        Singl_WorkingBuilding instance = Singl_WorkingBuilding.getInstance();
        List<Interface_Elevator> expResult = new ArrayList<Interface_Elevator>();
        //System.out.println(instance.getElevatorList().toString());
        List result = instance.getElevatorList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
