package ver3;

import java.text.SimpleDateFormat;
import java.util.Random;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ver3.project_exceptions.IllegalDirectionException;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;
import ver3.project_exceptions.IllegalSimulationException;

/**
 * This simulation class used to run the elevator application. It has setup,
 * action and shutdown methods to prepare all the relevant objects, run them 
 * over a time interval determined in an external data file, and then shut them
 * down. It also contains methods to run reports on the wait and ride times
 * resulting from the simulation. This class is singleton so that the objects
 * can access a reliable constant version of simulation data points
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public class Singl_WorkingSimulation {
    
    private volatile static Singl_WorkingSimulation instance;
    
    /**
    * The building's count of all its elevators.
    * 
    * @since Version 1.0
    * @see #getNumberOfElevators()
    * @see #setNumberOfElevators(int nOE)
    */
    private int numberOfElevators;
    
    /**
    * The building's count of all its floors.
    * 
    * @since Version 1.0
    * @see #getNumberOfFloors()
    * @see #setNumberOfFloors(int nOF)
    */
    private int numberOfFloors;
    
    /**
     * An integer used to store the length of time (in milliseconds)
     * it takes for the elevator to travel one floor.
     * 
     * @since Version 1.0
     * @see #getFloorTravelTime() 
     */
    private int floorTravelTime;
    
    /**
     * An integer used to store the default floor for the elevator to 
     * rest at while idle.
     * 
     * @since Version 1.0
     * @see #getDefaultFloor() 
     */
    private int defaultFloor;
    
    /**
     * A integer used to store the length of time (in milliseconds) 
     * for the doors to be open on a given floor.
     * 
     * @since Version 1.0
     * @see #getDoorOpenTime() 
     */
    private int doorOpenTime;
    
    /**
     * A integer used to store the length of time (in milliseconds) 
     * for the simulation.
     * 
     * @since Version 1.0
     * @see #getSimulationDuration()
     * @see #setSimulationDuration(int sD)
     */
    private int simulationDuration;
    
    /**
     * A integer used to store the time scale factor 
     * for the simulation.
     * 
     * @since Version 1.0
     * @see #getTimeScaleFactor()
     * @see #setTimeScaleFactor(int tSF)
     */
    private int timeScaleFactor;
    
    /**
     * A integer used to store the maximum people allowed in an elevator
     * for the simulation.
     * 
     * @since Version 1.0
     * @see #getMaxPersonsPerElevator()
     * @see #setMaxPersonsPerElevator(int mPPE)
     */
    private int maxPersonsPerElevator;
    
    /**
     * A integer used to store the number of person requests for an elevator
     * for the simulation.
     * 
     * @since Version 1.0
     * @see #getNumberOfPersonRequestingElevatorByTime()
     * @see #setNumberOfPersonRequestingElevatorByTime(int nOPREBT)
     * @see #generatePeople()
     */
    private int numberOfPersonRequestingElevatorByTime;
    
    /**
     * A String used to store the version that we'll use to later pick IMPL's 
     * 
     * @since Version 1.0
     * @see #getVersion()
     * @see #setVersion(String v)
     * @see #run(String version)
     * @see #buildChartA()
     * @see #buildChartB()
     * @see #buildChartC()
     */
    private String version;
    
    /**
     * A Treemap used to store the probabilities that people start on particular
     * floors
     * 
     * @since Version 1.0
     * @see #generatePeople()
     * @see #getFloorStatSpecStarts()
     * @see #readXML()
     */
    private TreeMap<Integer, Double> floorStatSpecStarts 
            = new TreeMap<Integer, Double>();
    
    /**
     * A Treemap used to store the probabilities that people end on particular
     * floors
     * 
     * @since Version 1.0
     * @see #generatePeople()
     * @see #getFloorStatSpecDestinations()
     * @see #readXML()
     */
    private TreeMap<Integer, Double> floorStatSpecDestinations 
            = new TreeMap<Integer, Double>();
    
    /**
     * A Treemap used to store the default floors for each elevator
     * 
     * @since Version 1.0
     * @see #getElevatorDefaultFloorDetails()
     * @see #readXML()
     */
    private TreeMap<Integer, Integer> elevatorDefaultFloorDetails
            = new TreeMap<Integer, Integer>();
    
    /**
     * This variable SimpleDateFormat simply sets the format of our date and time
     * for tracking purposes.
     * @since Version 1.0
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * A private contstuctor bc this class is a singleton. It simply prints out
     * acknowledgement
     * 
     * @since Version 1.0
     */
    private Singl_WorkingSimulation() {
        
        System.out.println(sdf.format(System.currentTimeMillis()) 
                + "\tStarting Elevator Simulation…");
        
    }
    
    /**
    * This is a public static method that returns an instance of the Simulation object.
    * The method contains conditionals to check if the instance has already been
    * instantiated. If so, then it returns the pre-existing object. If not, then
    * inside a synchronized block (to protect against multiple threads potentially
    * create two instantiations of the class)
    * 
    * @since Version 1.0
    * @return the instance variable which is the Simulation object
    */
    public static Singl_WorkingSimulation getInstance() {
        
        if(instance == null)
            synchronized(Singl_WorkingController.class)
            {
                if(instance == null)
                {
                    instance = new Singl_WorkingSimulation();
                }
            }
        
        return instance;
        
    }
    
    /**
     * A method that setups the simulation, reads, the xml file, and populates
     * the building. It then triggers the action method which begins putting people
     * on floors. Finnally it shutsdown all elevators and closes the simulation
     * 
     * @see #setUp()
     * @see #action()
     * @see #shutdown()
     * @since Version 1.0
     * @param version used to later select the IMPL's
     * @throws IllegalSimulationException if the String argument provided is not Original or New
     */  
    public void run(String version) throws IllegalSimulationException {
        
        if(!(version.equals("Original") || version.equals("New")))
            throw new IllegalSimulationException("Invalid version argument "
                                  + " encountered when setting up: " + version);
                
        try {
            setUp(version);
        } catch (IllegalSimulationException ex) {
            ex.printStackTrace();
        }
       action();
       shutdown();
       
    }
    
    /**
     * A method that builds a report based on wait time per floor and stores it
     * in a return string
     * 
     * @see #getAvgWaitTime()
     * @see #getMinWaitTime()
     * @see #getMaxWaitTime()
     * @since Version 1.0
     * @return String used to later print out a report
     */  
    public String buildChartA(){
        
        String chart = "";
        
        int spot;
        if(getVersion().equals("Original"))
            spot = 1;
        else
            spot = 2;
        
        chart += "a" + spot + ") Average wait time by floor (" + getVersion() + " Algorithm)\n\n";
        
        chart += "Floor    \t\tAverage Wait Time\tMin Wait Time\t\tMax Wait Time\n";
        
        // examine each floor
        for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
        {
            chart += ("Floor " + floor.getFloorNumber()
                      + "    \t\t" + String.format("%1$,.0f", floor.getAvgWaitTime()) + " seconds"
                      + "\t\t" + String.format("%1$,.0f", floor.getMinWaitTime()) + " seconds"
                      + "\t\t" + String.format("%1$,.0f", floor.getMaxWaitTime()) + " seconds\n");
        }
        
        chart += "\n\n";
        
        return chart;
        
    }
    
    /**
     * A method that builds a report based on ride time to and from each floor 
     * and stores it in a return string
     * 
     * @see #getRideTime()
     * @since Version 1.0
     * @return String used to later print out a report
     */  
    public String buildChartB(){
        
        String chart = "";
        
        int spot;
        if(getVersion().equals("Original"))
            spot = 1;
        else
            spot = 2;
        
        chart += "b" + spot + ") Ride Time from Floor to Floor by Person (" + getVersion() + " Algorithm)\n\n";
        
        chart += "Floor   ";
                
        for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
        {
            chart += "\t[" + floor.getFloorNumber() + "]";
        }
        
        chart += "\n";
        
        // create matrix
        // create a treemap where each floor is a key and each floor has a treemap as a value
        TreeMap<Integer, TreeMap<Integer, ArrayList<Interface_Person>>> peopleByStartFloor  // key=floor, value=listofpeople
                            = new TreeMap<Integer, TreeMap<Integer, ArrayList<Interface_Person>>>();
        
        
        // iterate over each building floor create new treemap for each start floor
        for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
        {
            peopleByStartFloor.put(floor.getFloorNumber()
                    , new TreeMap<Integer, ArrayList<Interface_Person>>());
        } 
        
        
        // iterate over each startfloor and to create a new Arraylist for each end floor
        for (Map.Entry<Integer, TreeMap<Integer, ArrayList<Interface_Person>>> startFloor : peopleByStartFloor.entrySet())
        {
            
            // recognize start floor's treemap
            TreeMap<Integer, ArrayList<Interface_Person>> mapOfStartFloor = startFloor.getValue();
            
            // iterate over each building floor to create new Arraylist for each end floor
            for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
            {
                mapOfStartFloor.put(floor.getFloorNumber(), new ArrayList<Interface_Person>());
            } 
        }
        
        
        //collect people from floor's completed list and put them into an arraylist
        ArrayList<Interface_Person> roster = new ArrayList<Interface_Person>();
        
        for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
        {
            //take people from personCompletedList and put them into treemap
            for(Interface_Person theGuy : floor.getPersonsCompleted())
            {
                roster.add(theGuy);
            }
        } 
        
        
        // now iterate over roster and put people in the correct floor's arraylist
        for (Interface_Person theGuy : roster)
        {    
            // store guy's floor numbers in a local variables
            int startFloorNumberOfGuy = theGuy.getStartFloor();
            int endFloorNumberOfGuy = theGuy.getIntendedDestination();
            
            // locate guys treemap of endfloors by using his start floor
            TreeMap<Integer, ArrayList<Interface_Person>> endfloors 
                                     = peopleByStartFloor.get(startFloorNumberOfGuy);
                    
                    
            // locate the guys's arrayList based on his end floor
            ArrayList<Interface_Person> guysInEndFloor = endfloors.get(endFloorNumberOfGuy);
            
            
            //add guy's ride time to this arraylist
            guysInEndFloor.add(theGuy);
        }
        
        // create a nested treemap for the final average values
        TreeMap<Integer, TreeMap<Integer, Double>> avgsByStartFloor  // key=floor, value=listofpeople
                            = new TreeMap<Integer, TreeMap<Integer, Double>>();
        
        // iterate over each building floor to create new TreeMap for each end floor
        for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
        {
            avgsByStartFloor.put(floor.getFloorNumber(), new TreeMap<Integer, Double>());
        } 
        
        
        for (Map.Entry<Integer, TreeMap<Integer, ArrayList<Interface_Person>>> startFloor : peopleByStartFloor.entrySet())
        {
            // recognize the end floor number specific to this iteration
            int startFloorSerialNumber = startFloor.getKey();
            
            // recognize start floor's treemap
            TreeMap<Integer, ArrayList<Interface_Person>> mapOfStartFloor = startFloor.getValue();
            
            for (Map.Entry<Integer, ArrayList<Interface_Person>> endFloor : mapOfStartFloor.entrySet())
            {
                //create local variable to store total ride time (so we can later get average)
                double totalRideTime = 0;

                for(Interface_Person theGuy : endFloor.getValue())
                {
                    totalRideTime += theGuy.getRideTime();
                }

                // get average ride time
                double averageRideTime;
                if(!endFloor.getValue().isEmpty())
                    averageRideTime = (totalRideTime / endFloor.getValue().size());
                else
                    averageRideTime = 0;

                // recognize the end floor number specific to this iteration
                int endFloorSerialNumber = endFloor.getKey();
                
                // locate the guys's treemap based on his end floor
                TreeMap<Integer, Double> endfloors 
                         = avgsByStartFloor.get(startFloorSerialNumber);
            
                //System.out.println("endFloorSerialNumber: " + endFloorSerialNumber);
                //add guy's ride time to this 
                endfloors.put(endFloorSerialNumber, averageRideTime);
            }     
            
        }
        
        
        // now we iterate through the avgsByStartFloor treemap and buildup the return string
        for (Map.Entry<Integer, TreeMap<Integer, Double>> startFloor : avgsByStartFloor.entrySet())
        {
            
            chart += "Floor [" + startFloor.getKey() + "]";
            
            // recognize start floor's treemap
            TreeMap<Integer, Double> mapOfStartFloor = startFloor.getValue();
            
            for (Map.Entry<Integer, Double> endFloor : mapOfStartFloor.entrySet())
            {
                if(startFloor.getKey() != endFloor.getKey())
                    chart += "\t " + String.format("%1$,.0f", endFloor.getValue()) + "";
                else
                    chart += "\t X";
            }
            
            chart += "\n";
            
        }
        
        
        
        chart += "\n\n";
        
        return chart;

    }
    
    /**
     * A method that builds a report based on ride time and wait time per person
     * and stores it in a return string
     * 
     * @see #getRideTime()
     * @see #getWaitTime()
     * @since Version 1.0
     * @return String used to later print out a report
     */ 
    public String buildChartC(){
        
        String chart = "";
        
        int spot;
        if(getVersion().equals("Original"))
            spot = 1;
        else
            spot = 2;
        
        chart += "c" + spot + ") Wait & Ride Time by Person (" + getVersion() + " Algorithm)\n\n";
        
        chart += "Person\t\tWait Time\tStart Floor\tDestination Floor\tRide Time\n";
        
        // examine each floor
        TreeMap<Integer, Interface_Person> roster = new TreeMap<Integer, Interface_Person>();
        
        for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
        {
            for(Interface_Person theGuy : floor.getPersonsCompleted())
            {
                roster.put(theGuy.getSerialNumber(), theGuy);
            }
        }
        
        double totalWaitTime = 0;
        double totalRideTime = 0;
        int increment = 0;
        for (Map.Entry<Integer, Interface_Person> theGuy : roster.entrySet())
        {    
            totalWaitTime += theGuy.getValue().getWaitTime();
            totalRideTime += theGuy.getValue().getRideTime();
            ++increment;
            chart += ("Person " + theGuy.getValue().getSerialNumber()
                      + "\t" + String.format("%1$,.0f", theGuy.getValue().getWaitTime()) + " seconds"
                      + "\t" + theGuy.getValue().getStartFloor()
                      + "\t\t" + theGuy.getValue().getIntendedDestination()
                      + "\t\t\t" + String.format("%1$,.0f", theGuy.getValue().getRideTime()) + " seconds\n");
        } 
        
        chart += "\nAVG WAIT TIME: " + String.format("%1$,.2f", (totalWaitTime / increment));
        
        chart += "\nAVG RIDE TIME: " + String.format("%1$,.2f", (totalRideTime / increment)) + "\n\n";
        
        chart += "\n\n";
        
        // cleanout floors completed list for future runs of the simulation
        for(Interface_Floor floor : Singl_WorkingBuilding.getInstance().getFloorList())
        {
            floor.getPersonsCompleted().clear();
        }
        
        return chart;
    }   
    
    /**
     * A method that builds a debug report showing where all the elevators are,
     * how many people they currently have, and their direction. The report also
     * details the floors based on how many people are waiting, how many people
     * have exited there, and what the status is of the call buttons there
     * 
     * @see #printDestinationList()
     * @see #getPendingList()
     * @since Version 1.0
     */ 
    public synchronized void printSimulationStatus(){
        
        // put conditional here based on xml configuration
        System.out.println("");
        for(Interface_Elevator elevator : Singl_WorkingBuilding.getInstance().getElevatorList())
        {
            System.out.println("ELEVATOR #" + elevator.getLetterName() 
                + elevator.getSerialNumber() + " IS ON FLOOR #" 
                + elevator.getCurrentFloor() + ", its direction is "
                + (elevator.getDirection() == 1 ? "up" : (elevator.getDirection() == -1 ? "down" : "idle")) 
                + " , AND IT HAS " + elevator.getPassengerList().size() + " "
                + "PASSENGERS \t   DESTINATION LIST: " + elevator.printDestinationList());
        }
        List<Interface_Floor> fList = Singl_WorkingBuilding.getInstance().getFloorList();
        for(int i = fList.size() - 1; i >= 0; --i)
        {
            System.out.println("FLOOR " + fList.get(i).getFloorNumber() + "  \tHAS " 
                + fList.get(i).getNumberOfWaitingPeopleCurrentlyOnFloor() + " WAITING PEOPLE\t AND "
                + fList.get(i).getNumberOfCompletedPeopleCurrentlyOnFloor() + " ALREADY COMPLETED"
                + "\t" + fList.get(i).getFloorNumber() + "Up: " 
                + (fList.get(i).getcBox().getUpButton() == true ? "<<TRUE>>" : "false")
                + "\t\t" + fList.get(i).getFloorNumber() + "Down: " 
                + (fList.get(i).getcBox().getDownButton() == true ? "<<TRUE>>" : "false")
                );
        }
        System.out.print("PRINTING CONTROLLERS PENDING LIST: [");
        for(Map.Entry<Integer, Integer> request : Singl_WorkingController.getInstance()
                                                          .getPendingList().entrySet())
        {
            System.out.print(" (" + request.getKey() + "-" 
                + (request.getValue() == 1 ? "up" : (request.getValue() == 2 ? "up&down" : "down")) 
                + "), ");
        }
        System.out.print("]\n");
        System.out.println("");
    }
    
    /**
     * A method used to read input data from the xml file and populate people on 
     * random floors relative to those percentages.
     * 
     * @see #getNumberOfPersonRequestingElevatorByTime()
     * @see #getFloorStatSpecStarts()
     * @see #getFloorStatSpecDestinations()
     * @since Version 1.0
     */ 
    private void generatePeople() {
        
        int startFloor;
        int intendedDestination; 
        Random randomNumber;
        double randomFloor;
        
        List<Interface_Person> guysWhoGetToPressCallboxButton 
                = new ArrayList<Interface_Person>();
        
        List<Interface_Person> allTheGuysInTheBatch 
                = new ArrayList<Interface_Person>();
        
        
        // use random generator to collect person's attributes
        for(int i = 0; i < getNumberOfPersonRequestingElevatorByTime(); ++i)
        {
            startFloor = 1;
            intendedDestination = 0; 
            randomNumber = new Random();
            randomFloor = randomNumber.nextDouble();
            for (Map.Entry<Integer, Double> entry : getFloorStatSpecStarts().entrySet())
            {
                if(entry.getValue() >= randomFloor) 
                {
                    break;
                }
                
                ++startFloor;
            }
            
            intendedDestination = startFloor;
            while(intendedDestination == startFloor) // loop tries to define startFloor
            {
                randomNumber = new Random();
                randomFloor = randomNumber.nextDouble();
                intendedDestination = 1;
                for (Map.Entry<Integer, Double> entry : getFloorStatSpecDestinations().entrySet())
                {
                    if(entry.getValue() >= randomFloor) 
                    {
                        break;
                    }
                    ++intendedDestination;
                }
            }

                 
            // create new person 
            Interface_Person theGuy = Fact_PersonFactory.build(
                                                            startFloor, 
                                                            intendedDestination);
            
            
            // add the new person to the batch set
            allTheGuysInTheBatch.add(theGuy);
            
            
            // get floor from building and put the new person in his start floor's collection
            Singl_WorkingBuilding.getInstance().getFloorList().get(startFloor - 1) 
                    .addPersonToFloor(theGuy);
            
            
            // add to the list of people who get to press the callbox button - NO DUPLICATES
            boolean okToAdd = true;
            if(guysWhoGetToPressCallboxButton.size() > 0)
            {
                for(Interface_Person individual : guysWhoGetToPressCallboxButton)
                {
                    
                    if(theGuy.getStartFloor() == individual.getStartFloor()
                            && theGuy.getIntendedDirection() == individual.getIntendedDirection())
                    {
                        okToAdd = false;
                        break;
                    }
                }
            }
            if(okToAdd == true)
            {
                guysWhoGetToPressCallboxButton.add(theGuy);
            } 
            
        }
        
        for(Interface_Person individualGuy : allTheGuysInTheBatch)
        {
            if(guysWhoGetToPressCallboxButton.contains(individualGuy))
                individualGuy.pressCallbox();
            else
            {
                System.out.println(sdf.format(System.currentTimeMillis()) + "\tPerson #" 
                    + individualGuy.getSerialNumber() + " wants to press callbox " 
                    + (individualGuy.getIntendedDirection() == 1 ? "up" : "down") 
                    + " button on floor #" + individualGuy.getStartFloor()
                    + ", but notices it has already been pressed");
            }
        }

    }
    
    /**
     * A simple accessor method that returns the number of elevators
     * 
     * @see #numberOfElevators
     * @since Version 1.0
     * @return int representing the amount of elevators
     */ 
    public int getNumberOfElevators() {
        return numberOfElevators;
    }
    
    /**
     * A simple accessor method that returns the number of floors
     * 
     * @see #numberOfFloors
     * @since Version 1.0
     * @return int representing the amount of floors
     */ 
    public int getNumberOfFloors() {
        return numberOfFloors;
    }
    
    /**
     * A simple accessor method that returns the maximum number of people allowed
     * in an elevator at the same time
     * 
     * @see #maxPersonsPerElevator
     * @since Version 1.0
     * @return int representing the max people in an elevator allowed
     */ 
    public int getMaxPersonsPerElevator() {
        return maxPersonsPerElevator;
    }
    
    /**
     * A simple accessor method that returns the time scale factor
     * 
     * @see #timeScaleFactor
     * @since Version 1.0
     * @return int representing the time scale factor
     */ 
    public int getTimeScaleFactor() {
        return timeScaleFactor;
    }
    
    /**
     * A simple accessor method that returns the default floor
     * 
     * @see #defaultFloor
     * @since Version 1.0
     * @return int representing the default floor
     */ 
    public int getDefaultFloor() {
        return defaultFloor;
    }
    
    /**
     * A simple accessor method that returns amount of travel time needed btw floors
     * 
     * @see #floorTravelTime
     * @since Version 1.0
     * @return int representing the amount of travel time needed btw floors
     */ 
    public int getFloorTravelTime() {
        return floorTravelTime;
    }
    
    /**
     * A simple accessor method that returns amount of time needed to open doors
     * 
     * @see #doorOpenTime
     * @since Version 1.0
     * @return int representing the amount of time needed to open doors
     */ 
    public int getDoorOpenTime() {
        return doorOpenTime;
    }
    
    /**
     * A simple accessor method that returns elevator's default floor details
     * 
     * @see #elevatorDefaultFloorDetails
     * @since Version 1.0
     * @return TreeMap representing the elevator's default floor details
     */ 
    public TreeMap<Integer, Integer> getElevatorDefaultFloorDetails() {
        return elevatorDefaultFloorDetails;
    }
    
    /**
     * A simple accessor method that returns which IMPL to select
     * 
     * @see #version
     * @since Version 1.0
     * @return String representing which IMPL to select
     */ 
    public String getVersion() {
        return version;
    }
    
    /**
     * Sets the version variable
     * 
     * @since Version 1.0
     * @see #version
     * @param String v
     * @throws IllegalSimulationException if the argument provided is not Original or New
     */
    private void setVersion(String v) throws IllegalSimulationException{
        
        if(!(v.equals("Original") || v.equals("New")))
            throw new IllegalSimulationException("Invalid version argument "
                                  + " encountered when setting up: " + version);
        else
            version = v;
    }
    
    /**
     * A simple accessor method that returns the floor spec destination probabilities
     * 
     * @see #floorStatSpecDestinations
     * @since Version 1.0
     * @return TreeMap<Integer, Double> representing the floor spec destination probabilities
     */   
    private TreeMap<Integer, Double> getFloorStatSpecDestinations() {
        return floorStatSpecDestinations;
    }
    
    /**
     * A simple accessor method that returns the floor spec start probabilities
     * 
     * @see #floorStatSpecStarts
     * @since Version 1.0
     * @return TreeMap<Integer, Double> representing the floor spec start probabilities
     */  
    private TreeMap<Integer, Double> getFloorStatSpecStarts() {
        return floorStatSpecStarts;
    }
    
    /**
     * Sets the timeScaleFactor variable
     * 
     * @since Version 1.0
     * @see #timeScaleFactor
     * @param int tSF
     * @throws IllegalSimulationException if the argument less than zero
     */
    private void setTimeScaleFactor(int tSF) throws IllegalSimulationException {
                                
        if(tSF < 0)
            throw new IllegalSimulationException("Negative nOPREBTime encountered: " + tSF);
        else
        {
            timeScaleFactor = tSF;
        }
    }
    
    /**
     * Sets the doorOpenTime variable
     * 
     * @since Version 1.0
     * @see #doorOpenTime
     * @param int dOP
     * @throws IllegalSimulationException if the argument less than zero
     */
    private void setDoorOpenTime(int dOP) throws IllegalSimulationException {
        if(dOP < 0)
            throw new IllegalSimulationException("Negative doorOpenTime "
                                                       + "encountered: " + dOP);
        else
            doorOpenTime = dOP;
    }
    
    /**
     * Sets the floorTravelTime variable
     * 
     * @since Version 1.0
     * @see #floorTravelTime
     * @param int fTT
     * @throws IllegalSimulationException if the argument less than zero
     */
    private void setFloorTravelTime(int fTT) throws IllegalSimulationException {
        if(fTT < 0)
            throw new IllegalSimulationException("Negative floorTravelTime "
                                                       + "encountered: " + fTT);
        else
            floorTravelTime = fTT;
    }
    
    /**
     * Sets the defaultFloor variable
     * 
     * @since Version 1.0
     * @see #defaultFloor
     * @param int dF
     * @throws IllegalFloorException if the argument less than zero
     */
    private void setDefaultFloor(int dF) throws IllegalFloorException {
        if(dF < 0)
            throw new IllegalFloorException("Out of bounds defaultFloor "
                                                        + "encountered: " + dF);
        else
            defaultFloor = dF;
    }
    
    /**
     * Sets the setMaxPersonsPerElevator variable
     * 
     * @since Version 1.0
     * @see #setMaxPersonsPerElevator
     * @param int mPPE
     * @throws IllegalSimulationException if the argument less than zero
     */
    private void setMaxPersonsPerElevator(int mPPE) throws IllegalSimulationException {
                               
        if(mPPE < 0)
            throw new IllegalSimulationException("Negative maxPersonsPerElevator "
                                                    + "encountered: " + mPPE);
        else
        {
            maxPersonsPerElevator = mPPE;
        }
    }
    
    /**
     * A simple accessor method that returns the amount of people planned to call 
     * an elevator by time
     * 
     * @see #numberOfPersonRequestingElevatorByTime
     * @since Version 1.0
     * @return String representing numberOfPersonRequestingElevatorByTime
     */
    private int getNumberOfPersonRequestingElevatorByTime() {
        return numberOfPersonRequestingElevatorByTime;
    }
    
    /**
     * Sets the numberOfPersonRequestingElevatorByTime variable
     * 
     * @since Version 1.0
     * @see #numberOfPersonRequestingElevatorByTime
     * @param int nOPREBT
     * @throws IllegalSimulationException if the argument less than zero
     */
    private void setNumberOfPersonRequestingElevatorByTime(int nOPREBT) throws IllegalSimulationException {
                        
        if(nOPREBT < 0)
            throw new IllegalSimulationException("Negative nOPREBTime encountered: " + nOPREBT);
        else
        {
            numberOfPersonRequestingElevatorByTime = nOPREBT;
        }
    }
    
    /**
     * A simple accessor method that returns the amount of time the simulation
     * will run
     * 
     * @see #simulationDuration
     * @since Version 1.0
     * @return String representing the amount of time the simulation will run
     */
    private int getSimulationDuration() {
        return simulationDuration;
    }
    
    /**
     * Sets the simulationDuration variable
     * 
     * @since Version 1.0
     * @see #simulationDuration
     * @param int sD
     * @throws IllegalSimulationException if the argument less than zero
     */
    private void setSimulationDuration(int sD) throws IllegalSimulationException {
                
        if(sD < 0)
            throw new IllegalSimulationException("Negative simulation duration encountered: " + sD);
        else
        {
            simulationDuration = sD * 60000;
        }
    }
    
    /**
     * Sets the numberOfElevators variable
     * 
     * @since Version 1.0
     * @see #numberOfElevators
     * @param int nOE
     * @throws IllegalElevatorException if the argument less than zero
     */
    private void setNumberOfElevators(int nOE) throws IllegalElevatorException {
                
        if(nOE < 0)
            throw new IllegalElevatorException("Negative number of elevators encountered: " + nOE);
        else
        {
            numberOfElevators = nOE;
        }
    }
    
    /**
     * Sets the numberOfFloors variable
     * 
     * @since Version 1.0
     * @see #numberOfFloors
     * @param int nOF
     * @throws IllegalFloorException if the argument less than zero
     */
    private void setNumberOfFloors(int nOF) throws IllegalFloorException{
        
        if(nOF < 0)
            throw new IllegalFloorException("Negative number of floors encountered: " + nOF);
        else
        {
            numberOfFloors = nOF;
        }
    }
    
    /**
     * This method generates people while the simulation time hasn't run out.
     * 
     * @since Version 1.0
     * @see #getTimeScaleFactor()
     * @see #getSimulationDuration()
     */
    private void action(){
        
        long startTime = System.currentTimeMillis();
        
        // while simulation is running, create People, assign them to 
        // a floor, let them press floor callbox
        while((System.currentTimeMillis() - startTime) 
                    < (getSimulationDuration() / getTimeScaleFactor())) 
        {
            generatePeople();
            
            try {
                Thread.sleep(60000  / getTimeScaleFactor()); // sleep for a minute
            } catch (InterruptedException ex) {
                System.out.println("Interruption Occurred " + ex.getMessage());
            }
        }
    }
    
    /**
     * This method shuts down all the elevators after the simulation is complete
     * 
     * @since Version 1.0
     * @see #getFloorList()
     * @see #getElevatorList()
     */
    private void shutdown(){
        
        //shutdown up elevators
        System.out.println("\n" + sdf.format(System.currentTimeMillis()) 
                + "\tElevators starting shutting down mode\n");
        
        List<Interface_Floor> fList = Singl_WorkingBuilding.getInstance()
                                                                .getFloorList();
        
        List<Interface_Elevator> eList = Singl_WorkingBuilding.getInstance()
                                                             .getElevatorList();
        
        boolean elevatorsComplete = false;
        boolean floorsComplete = false;
        boolean pendingListComplete = false;     

        while(elevatorsComplete == false
                || floorsComplete == false
                || pendingListComplete == false) 
        {
            // check that each elevator considers itself complete
            while(elevatorsComplete == false)
            {
                elevatorsComplete = true;

                for(Interface_Elevator elevator : eList)
                {
                    if(!elevator.getDestinationList().isEmpty() // no more destinations
                        || elevator.getCurrentFloor() != elevator.getDefaultFloor() // at defaultFloor
                        || elevator.getDirection() != 0) // idle
                    {
                        elevatorsComplete = false;
                        System.out.println(sdf.format(System.currentTimeMillis()) 
                               + "\tElevator " + elevator.getLetterName() 
                               + elevator.getSerialNumber() + " is not complete yet");
                    }
                }
                
                try {
                    Thread.sleep(10000 / getTimeScaleFactor());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            
            
            // now test that noone is stranded on a floor -- loop is separate because
            // the program must first wait until the elevators first think theyre complete
            // before it can send the elevators to any lingering floors
            
            // check for items still in the pending list
            pendingListComplete = true;
            TreeMap<Integer,Integer> pList = Singl_WorkingController
                                                .getInstance()
                                                .getPendingList();

            if(!pList.isEmpty())
            {
                pendingListComplete = false;

                System.out.println(sdf.format(System.currentTimeMillis()) 
                               + "\tPending list is not yet complete");

                // if so, distribute the remaining pending 
                // requests equally amongst elevators
                for (Map.Entry<Integer, Integer> entry : pList.entrySet())
                {   
                    if(entry.getKey() == 2)
                    {
                        try {
                            Singl_WorkingController.getInstance().pick(entry.getKey(), -1);
                            Singl_WorkingController.getInstance().pick(entry.getKey(), 1);
                        }  catch (IllegalDirectionException ex) {
                            ex.printStackTrace();
                        } catch (IllegalFloorException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else // it already has a 1 or -1
                    {
                        try {
                           Singl_WorkingController.getInstance().pick(
                                                                   entry.getKey(), 
                                                                   entry.getValue());
                        }  catch (IllegalDirectionException ex) {
                            ex.printStackTrace();
                        } catch (IllegalFloorException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                
                continue;
            }
            
            
            // now clear out all callboxes that are lit but are on floors that 
            // don't currently have people
            for(Interface_Floor floor : fList)
            {
                // now check if the clabox buttons are 
                // still lit when noone is on the floor
                if(floor.getNumberOfWaitingPeopleCurrentlyOnFloor() == 0
                     && (floor.getcBox().getDownButton() == true 
                        || floor.getcBox().getUpButton() == true))
                {
                    
                    System.out.println("FLOOR #" + floor.getFloorNumber() 
                            + " STILL HAS CALLBOX BUTTONS LIT WHEN THERE'S "
                            + "NOONE ON THE FLOOR");
                    try {
                        floor.getcBox().setDownButton(false);
                        floor.getcBox().setUpButton(false);
                    } catch (IllegalDirectionException ex) {
                        ex.printStackTrace();
                    }
                    
                }
            }
            
            
            // now service all people that are somehow still one a floor
            floorsComplete = true;
            
            for(Interface_Floor floor : fList)
            {
                if(floor.getNumberOfWaitingPeopleCurrentlyOnFloor() != 0)
                {
                    floorsComplete = false;
                    
                    System.out.println(sdf.format(System.currentTimeMillis()) 
                            + "\tFloor #" + floor.getFloorNumber() + " is"
                            + " not done yet -- still people present there");
                    
                    // this method iterates through each floor and checks
                    // whether there are any people still waiting on the floor
                    // It checks the scenarios where there are people and their
                    // callbox request is still lit and other scenarios where
                    // there are people on the floor and their callbox is no 
                    // longer lit. 
                    
                    // The method returns a validation boolean for the 
                    // elevatorsComplete variable. So it starts with the variable
                    // as true and if it doesn't pass the conditional tests
                    // then it marks that variable false. Finnally, it returns
                    // that variable
                    elevatorsComplete = floor.lastCall();
                    continue;
                }
                
            }
            
            
            try {
                Thread.sleep(15000 / getTimeScaleFactor());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
        
        // everything is now complete and its ok to 
        // invoke the shutdown method for each elevator
        for(int i = 0; i < Singl_WorkingBuilding.getInstance()
                                                   .getNumberOfElevators(); ++i)
        {
            try
            {
                Singl_WorkingBuilding.getInstance()
                        .getElevatorList().get(i).shutDown();

            } catch(IndexOutOfBoundsException ex) {
                System.out.println("Error occurred while trying to get "
                        + "illegal index #" + i + " of elevator list while "
                        + "shutting down: " + ex.getMessage());
            }
        }
        
        System.out.println(sdf.format(System.currentTimeMillis()) 
                + "\tAll elevators have now been shutdown");
        
        printSimulationStatus();
        
        System.out.println(sdf.format(System.currentTimeMillis()) 
                + "\tProgram Completed\n\n\n\n");
    }

    /**
     * A private method used to set the numOfFloors and the numOfElevators variables. 
     * It opens an xml file, parses it, and invokes the set methods mentioned with
     * the specific nodes used as arguments for those set methods
     * 
     * @see #setNumberOfElevators(int nOE)
     * @see #setNumberOfFloors(int nOF)
     * @see <a href="http://www.developerfusion.com/code/2064/a-simple-way-to-read-an-xml-file-in-java/">information on closing file</a>
     * @since Version 1.0
     * @param nOE the int to populate the floorList arraylist
     * @throws SAXException if has trouble extracting from the xml file
     * @throws ParserConfigurationException if has trouble extracting from the xml file
     * @throws InvalidDataException if the data doesn't match the variable type
     */  
    private void readXML() throws SAXException, ParserConfigurationException,
                            IllegalElevatorException, IllegalFloorException {
        try {
            FileInputStream file = new FileInputStream("src/ver3/programData.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList dataList = doc.getElementsByTagName("building");
            
            for (int i = 0; i < dataList.getLength(); ++i) {
                
                Node nNode = dataList.item(i);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element eElement = (Element) nNode;
                    
                        try {
                                setDefaultFloor(Integer.parseInt(
                                eElement.getElementsByTagName("defaultFloor")
                                .item(0).getTextContent()));
                                
                                setDoorOpenTime(Integer.parseInt(
                                eElement.getElementsByTagName("elevatorOpenTime")
                                .item(0).getTextContent())); 
                                
                                setFloorTravelTime(Integer.parseInt(
                                eElement.getElementsByTagName("timeToTravelFloor")
                                .item(0).getTextContent())); 
                                
                                setSimulationDuration(Integer.parseInt(
                                eElement.getElementsByTagName("simulationDuration")
                                .item(0).getTextContent()));
                                
                                setMaxPersonsPerElevator(Integer.parseInt(
                                eElement.getElementsByTagName("maxPersonsPerElevator")
                                .item(0).getTextContent()));
                                
                                setNumberOfPersonRequestingElevatorByTime(Integer.parseInt(
                                eElement.getElementsByTagName("numberOfPersonRequestingElevatorByTime")
                                .item(0).getTextContent()));
                                
                                setTimeScaleFactor(Integer.parseInt(
                                eElement.getElementsByTagName("timeScaleFactor")
                                .item(0).getTextContent()));
                                
                                setNumberOfElevators(Integer.parseInt(
                                eElement.getElementsByTagName("numOfElevators")
                                .item(0).getTextContent()));

                                setNumberOfFloors(Integer.parseInt(
                                eElement.getElementsByTagName("numOfFloors")
                                .item(0).getTextContent())); 
                                
                        } catch (IllegalFloorException ex) {
                            ex.printStackTrace();
                        } catch (IllegalSimulationException ex) {
                            ex.printStackTrace();
                        } catch (IllegalElevatorException ex) {
                            ex.printStackTrace();
                        }
                        
                        //now gather each elevator's default floor
                        NodeList elevatorList = eElement.getElementsByTagName("elevator");
                        
                        if(elevatorList.getLength() != getNumberOfElevators())
                            throw new IllegalElevatorException("You have a different amount of "
                                    + "default floor listings in your xml file than you have a "
                                    + "listed number of elevators");

                        for (int k = 0; k < elevatorList.getLength(); ++k) {

                            Node kNode = elevatorList.item(k);

                            Element kElement = (Element) kNode;
                            
                            elevatorDefaultFloorDetails.put(
                                  Integer.parseInt(kElement.getAttribute("serialNumber")),
                                  Integer.parseInt(kElement
                                                    .getElementsByTagName("defaultFloor")
                                                    .item(0).getTextContent())
                                  );
                        }
                        
                    
                        // now gather stats for each floor
                        NodeList floorList = eElement.getElementsByTagName("floor");
                        
                        if(floorList.getLength() != getNumberOfFloors())
                            throw new IllegalFloorException("You have a different amount of "
                                    + "floor stat listings in your xml file than you have a "
                                    + "listed number of floors");

                        double startRangeIncrement = 0.0;
                        double destinationRangeIncrement = 0.0;

                        for (int j = 0; j < floorList.getLength(); ++j) {

                            Node jNode = floorList.item(j);

                            Element jElement = (Element) jNode;

                            startRangeIncrement += Double.parseDouble(
                                jElement.getElementsByTagName("startingPercentage")
                                    .item(0).getTextContent()) * .01;

                            floorStatSpecStarts.put(Integer.parseInt(
                                            jElement.getAttribute("floorNumber")), 
                                            startRangeIncrement);

                            destinationRangeIncrement += Double.parseDouble(
                                jElement.getElementsByTagName("endingPercentage")
                                    .item(0).getTextContent()) * .01;

                            floorStatSpecDestinations.put(Integer.parseInt(
                                           jElement.getAttribute("floorNumber")), 
                                           destinationRangeIncrement);

                        }
                }
 
            }          
 
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
    }
    
    /**
     * This method invokes the readXML method and it build out all the 
     * elevators and floors
     * 
     * @since Version 1.0
     * @see #readXML()
     * @see #setVersion()
     */
    private void setUp(String version) throws IllegalSimulationException {
        
        if(!(version.equals("Original") || version.equals("New")))
            throw new IllegalSimulationException("Invalid version argument "
                                  + " encountered when setting up: " + version);
                
        System.out.println(sdf.format(System.currentTimeMillis()) 
                + "\tGetting Input Data…");
        
        
        try{
            
            setVersion(version);
            
            readXML();
            //Information on floor quantity and elevator 
            // quantity is found in programData.xml
            
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }  catch (IllegalElevatorException ex) {
                ex.printStackTrace();
        } catch (IllegalFloorException ex) {
            ex.printStackTrace();
        }
                          
                        
        System.out.println(sdf.format(System.currentTimeMillis()) 
                + "\tCreating Building…");
        
        //instatiate building, populate floors and elevators into building
        Singl_WorkingBuilding bInfo = Singl_WorkingBuilding.getInstance();
        
        System.out.println(sdf.format(System.currentTimeMillis()) 
                + "\tBuilding created, " + getNumberOfFloors() + " Floors, " 
                + getNumberOfElevators() + " Elevators.…");
        
        //starting up elevators
        System.out.println(sdf.format(System.currentTimeMillis()) 
                + "\tElevators starting up");
        
        for(int i = 0; i < bInfo.getNumberOfElevators(); ++i)
        {
            try
            {
                bInfo.getElevatorList().get(i).startUp();

            } catch(IndexOutOfBoundsException ex) {
                    System.out.println("Error occurred while trying to get "
                        + "illegal index #" + i + " of elevator list while starting "
                        + "up: " + ex.getMessage());
            }
        }
    }
           
}
