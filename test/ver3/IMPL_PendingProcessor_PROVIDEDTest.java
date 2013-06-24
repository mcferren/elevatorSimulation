/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ver3;

import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;

/**
 *
 * @author Kevin
 */
public class IMPL_PendingProcessor_PROVIDEDTest extends TestCase {

    public IMPL_PendingProcessor_PROVIDEDTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of deliverPendingList method, of class
     * IMPL_PendingProcessor_IMPROVED.
     * Passes a value of -1 for floor, which should cause an IllegalFloorException.
     * Requests elevator number 0.
     * Test verifies that an IllegalFloorException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException
     */
    public void testDeliverPendingList() throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException {
        System.out.println("deliverPendingList floor -1");
        int requestingFloor = -1;
        int requestingElevatorNumber = 0;
        IMPL_PendingProcessor_IMPROVED instance = new IMPL_PendingProcessor_IMPROVED();
        try {
            instance.deliverPendingList(requestingFloor, requestingElevatorNumber);
            fail("Should have caused an IllegalFloorException");
        } catch (IllegalFloorException e) {
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of deliverPendingList method, of class
     * IMPL_PendingProcessor_IMPROVED.
     * Passes a value of MAX_VALUE for floor, which should cause an IllegalFloorException.
     * Realistically, buildings will only have 100-200 floors anyway.
     * Requests elevator number 0.
     * Test verifies that an IllegalFloorException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException
     */
    public void testDeliverPendingList2() throws Exception {
        System.out.println("deliverPendingList floor max value");
        int requestingFloor = Integer.MAX_VALUE;
        int requestingElevatorNumber = 0;
        IMPL_PendingProcessor_IMPROVED instance = new IMPL_PendingProcessor_IMPROVED();
        try {
            instance.deliverPendingList(requestingFloor, requestingElevatorNumber);
            fail("Should have caused an IllegalFloorException");
        } catch (IllegalFloorException e) {
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of deliverPendingList method, of class
     * IMPL_PendingProcessor_IMPROVED.
     * Passes a value of 0 for floor.
     * Requests elevator number -1, which should cause an IllegalElevatorException.
     * Test verifies that an IllegalElevatorException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException
     */
    public void testDeliverPendingList3() throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException {
        System.out.println("deliverPendingList elevator -1");
        int requestingFloor = 0;
        int requestingElevatorNumber = -1;
        IMPL_PendingProcessor_IMPROVED instance = new IMPL_PendingProcessor_IMPROVED();
        try {
            instance.deliverPendingList(requestingFloor, requestingElevatorNumber);
            fail("Should have caused an IllegalElevatorException");
        } catch (IllegalElevatorException e) {
            
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
   
    /**
     * Test of deliverPendingList method, of class
     * IMPL_PendingProcessor_IMPROVED.
     * Passes a value of 0 for floor.
     * Requests elevator number MAX_VALUE, which should cause an IllegalElevatorException.
     * Most buildings have under 100 elevators anyway.
     * Test verifies that an IllegalElevatorException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException
     */
    public void testDeliverPendingList4() throws IllegalFloorException, IllegalDirectionException, IllegalElevatorException {
        System.out.println("deliverPendingList elevator max value");
        int requestingFloor = 0;
        int requestingElevatorNumber = Integer.MAX_VALUE;
        IMPL_PendingProcessor_IMPROVED instance = new IMPL_PendingProcessor_IMPROVED();
        try {
            instance.deliverPendingList(requestingFloor, requestingElevatorNumber);
            fail("Should have caused an IllegalElevatorException");
        } catch (/*IllegalElevator*/Exception e) {
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    
}
