/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ver3;

import java.util.TreeMap;
import junit.framework.TestCase;
import ver3.project_exceptions.IllegalSimulationException;

/**
 *
 * @author Kevin
 */
public class Singl_WorkingSimulationTest extends TestCase {
    
    public Singl_WorkingSimulationTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        System.out.println("FIRST RUN WITH ORIGINAL ALGORITHMS\n");
        try {
            Singl_WorkingSimulation.getInstance().run("Original");
        } catch (IllegalSimulationException ex) {
            ex.printStackTrace();
        }
        String originalReportA = Singl_WorkingSimulation.getInstance().buildChartA();
        String originalReportB = Singl_WorkingSimulation.getInstance().buildChartB();
        String originalReportC = Singl_WorkingSimulation.getInstance().buildChartC();


        System.out.println("\n\n\n\nSECOND RUN WITH NEW ALGORITHMS\n");
        try {
            Singl_WorkingSimulation.getInstance().run("New");
        } catch (IllegalSimulationException ex) {
            ex.printStackTrace();
        }
        String newReportA = Singl_WorkingSimulation.getInstance().buildChartA();
        String newReportB = Singl_WorkingSimulation.getInstance().buildChartB();
        String newReportC = Singl_WorkingSimulation.getInstance().buildChartC();


        System.out.println("\n\n\n\nPRINTING REPORTS\n");
        System.out.println(originalReportA);
        System.out.println(newReportA);
        System.out.println(originalReportB);
        System.out.println(newReportB);
        System.out.println(originalReportC);
        System.out.println(newReportC);
        //super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getInstance method, of class Singl_WorkingSimulation.
     */
    public void testGetInstance() {
        System.out.println("getInstance");
        Singl_WorkingSimulation expResult = Singl_WorkingSimulation.getInstance();
        Singl_WorkingSimulation result = Singl_WorkingSimulation.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class Singl_WorkingSimulation.
     */
    public void testRun() throws IllegalSimulationException {
        System.out.println("run");
        String version = "Original";
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        instance.run(version);
        assertSame(instance.getVersion(), version);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of run method, of class Singl_WorkingSimulation.
     */
    public void testRun2() throws IllegalSimulationException {
        System.out.println("run");
        String version = "New";
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        instance.run(version);
        assertSame(instance.getVersion(), version);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of run method, of class Singl_WorkingSimulation.
     */
    public void testRun3() throws IllegalSimulationException {
        System.out.println("run");
        String version = "The cake is a lie!";
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        try {
            instance.run(version);
            fail("IllegalSimulationException should have occurred");
        }
        catch(IllegalSimulationException e) {
        
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of buildChartA method, of class Singl_WorkingSimulation.
     */
    /*public void testBuildChartA() {
        System.out.println("buildChartA");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        String expResult = "";
        String result = instance.buildChartA();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of buildChartB method, of class Singl_WorkingSimulation.
     */
    /*public void testBuildChartB() {
        System.out.println("buildChartB");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        String expResult = "";
        String result = instance.buildChartB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of buildChartC method, of class Singl_WorkingSimulation.
     */
    /*public void testBuildChartC() {
        System.out.println("buildChartC");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        String expResult = "";
        String result = instance.buildChartC();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of printSimulationStatus method, of class Singl_WorkingSimulation.
     */
    /*public void testPrintSimulationStatus() {
        System.out.println("printSimulationStatus");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        instance.printSimulationStatus();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getNumberOfElevators method, of class Singl_WorkingSimulation.
     */
    public void testGetNumberOfElevators() {
        System.out.println("getNumberOfElevators");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        int expResult = 4;
        int result = instance.getNumberOfElevators();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getNumberOfFloors method, of class Singl_WorkingSimulation.
     */
    public void testGetNumberOfFloors() {
        System.out.println("getNumberOfFloors");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        int expResult = 16;
        int result = instance.getNumberOfFloors();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxPersonsPerElevator method, of class Singl_WorkingSimulation.
     */
    public void testGetMaxPersonsPerElevator() {
        System.out.println("getMaxPersonsPerElevator");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        int expResult = 8;
        int result = instance.getMaxPersonsPerElevator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getTimeScaleFactor method, of class Singl_WorkingSimulation.
     */
    public void testGetTimeScaleFactor() {
        System.out.println("getTimeScaleFactor");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        int expResult = 200;
        int result = instance.getTimeScaleFactor();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getDefaultFloor method, of class Singl_WorkingSimulation.
     */
    public void testGetDefaultFloor() {
        System.out.println("getDefaultFloor");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        int expResult = 7;
        int result = instance.getDefaultFloor();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getFloorTravelTime method, of class Singl_WorkingSimulation.
     */
    public void testGetFloorTravelTime() {
        System.out.println("getFloorTravelTime");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        int expResult = 1000;
        int result = instance.getFloorTravelTime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getDoorOpenTime method, of class Singl_WorkingSimulation.
     */
    public void testGetDoorOpenTime() {
        System.out.println("getDoorOpenTime");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        int expResult = 3000;
        int result = instance.getDoorOpenTime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getElevatorDefaultFloorDetails method, of class Singl_WorkingSimulation.
     */
    public void testGetElevatorDefaultFloorDetails() {
        System.out.println("getElevatorDefaultFloorDetails");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        TreeMap<Integer,Integer> expResult = new TreeMap<Integer,Integer>();
//        expResult.put(1,7);
//        expResult.put(2,1);
//        expResult.put(3,1);
//        expResult.put(4,1);
        
        
        TreeMap result = instance.getElevatorDefaultFloorDetails();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getVersion method, of class Singl_WorkingSimulation.
     */
    public void testGetVersion() {
        System.out.println("getVersion");
        Singl_WorkingSimulation instance = Singl_WorkingSimulation.getInstance();
        String expResult = "Original";
        String result = instance.getVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
