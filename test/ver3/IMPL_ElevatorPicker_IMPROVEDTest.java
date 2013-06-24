/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ver3;

import static junit.framework.Assert.fail;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalFloorException;
import junit.framework.TestCase;

/**
 *
 * @author Kevin
 */
public class IMPL_ElevatorPicker_IMPROVEDTest extends TestCase {

    public IMPL_ElevatorPicker_IMPROVEDTest(String testName) {
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
     * Test of pick method, of class IMPL_ElevatorPicker_IMPROVED.
     * Passes a value of -1 for floor, which should cause an Illegal Floor Exception.
     * Direction is 1.
     * Test verifies that the IllegalFlooreException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException
     */
    public void testPick() throws IllegalFloorException, IllegalDirectionException {
        System.out.println("pick floor -1");
        int floor = -1;
        int direction = 1;
        IMPL_ElevatorPicker_IMPROVED instance = new IMPL_ElevatorPicker_IMPROVED();
        try {
            instance.pick(floor, direction);
            fail("Should have caused an Illegal Floor Exception");
        } catch (IllegalFloorException e) {
            
        }
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of pick method, of class IMPL_ElevatorPicker_IMPROVED.
     * Passes a value of MAX_VALUE for floor, which should cause an Illegal Floor Exception.
     * This is assuming that the number of floors is less than MAX_VALUE.
     * Realistically, buildings do not usually have more than 100-200 floors anyway.
     * Direction is 1.
     * Test verifies that the IllegalFlooreException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException
     */
    public void testPick2() throws IllegalFloorException, IllegalDirectionException {
        System.out.println("pick floor max value");
        int floor = Integer.MAX_VALUE;
        int direction = 1;
        IMPL_ElevatorPicker_IMPROVED instance = new IMPL_ElevatorPicker_IMPROVED();
        try {
            instance.pick(floor, direction);
            fail("Should have caused an Illegal Flooor Exception");
        } catch (IllegalFloorException e) {
            
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of pick method, of class IMPL_ElevatorPicker_IMPROVED.
     * Passes a value of 0 for floor.
     * Direction is set to Integer.MIN_VALUE, which should throw an Illegal Direction Exception.
     * Direction can only be 1 or -1.
     * Test verifies that the IllegalDirectionException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException
     */
    public void testPick3() throws IllegalFloorException, IllegalDirectionException {
        System.out.println("pick direction min value");
        int floor = 0;
        int direction = Integer.MIN_VALUE;
        IMPL_ElevatorPicker_IMPROVED instance = new IMPL_ElevatorPicker_IMPROVED();
        try {
            instance.pick(floor, direction);
            fail("Should have caused an Illegal Direction Exception");
        } catch (IllegalDirectionException e) {
            
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of pick method, of class IMPL_ElevatorPicker_IMPROVED.
     * Passes a value of 0 for floor.
     * Direction is set to Integer.MAX_VALUE, which should throw an Illegal Direction Exception.
     * Direction can only be 1 or -1.
     * Test verifies that the IllegalDirectionException is thrown.
     * @throws IllegalFloorException, IllegalDirectionException
     */
    public void testPick4() throws IllegalFloorException, IllegalDirectionException {
        System.out.println("pick direction max value");
        int floor = 0;
        int direction = Integer.MAX_VALUE;
        IMPL_ElevatorPicker_IMPROVED instance = new IMPL_ElevatorPicker_IMPROVED();
        try {
            instance.pick(floor, direction);
            fail("Should have caused an Illegal Direction Exception");
        } catch (IllegalDirectionException e) {
            
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
