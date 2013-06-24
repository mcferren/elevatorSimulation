package ver3;

import ver3.project_exceptions.IllegalSimulationException;


/**
 * This is the driver class for the Elevator project.  This driver has one function,
 * main() which will execute code to use the project.  The driver is responsible for
 * invoking the run method of the simulation singleton and then running its
 * chart building methods. It stores the results of those methods and then runs
 * the same process again (with different IMPL classes. It then prints out the 
 * strings it has accumulated in order to compare the two runs
 * 
 * 
 * @author Ben McFerren
 * @author Kevin Newhouse
 * @since Version 1.0
 */
public class Driver {

    /**
     * This is the main method for Driver.  This method is automatically run by
     * Java.
     * @since Version 1.0
     * @param args 
     */
    public static void main(String[] args) {
        
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
    }
    
}
