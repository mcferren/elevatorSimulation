package ver3;

import java.util.List;
import java.util.ArrayList;
import ver3.project_exceptions.IllegalElevatorException;
import ver3.project_exceptions.IllegalFloorException;

/**
 * This is a Singleton class that represents a building. The building owns a 
 * number of floors and elevators specified in an external xml file that the 
 * simulation reads. It is Singleton because we want to only have one building
 * that houses all information relative to the program. The class is a traditional 
 * Singleton that has a private constructor, a private local instance variable 
 * and a public static getInstance class. In addition, it has a series of local 
 * variables that decorate class information related to the quantity and 
 * collections of floor and elevator objects. 
 * 
 * @author Kevin Knewhouse 
 * @author Ben McFerren
 * @since Version 1.0
 */
public class Singl_WorkingBuilding {
    
    /**
     * This is a private static variable used to store the instance of the 
     * Singl_WorkingBuilding object. It is volatile because we are working with 
     * threads and do not want to cache copies. 
     * 
     * @since Version 1.0
     * @see #Singl_WorkingBuilding()
     */
    private volatile static Singl_WorkingBuilding instance;
    
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
    * A arrayList collection container that holds all of a building's floors.
    * 
    * @since Version 1.0
    * @see #setFloorList(int nF)
    */
    private List<Interface_Floor> floorList = new ArrayList<Interface_Floor>();
    
    /**
    * A arrayList collection container that holds all of a building's floors.
    * 
    * @since Version 1.0
    * @see #getElevatorList()
    * @see #setNumberOfFloors(int nOF)
    * @see #setElevatorList(int nE)
    */
    private List<Interface_Elevator> elevatorList = new ArrayList<Interface_Elevator>();
    
    /**
     * This is a private constructor so that noone on the outside can instantiate
     * an object from the class directly. Inside of the body of the constructor
     * there is a try / catch block that invokes the readXML method to populate 
     * floor and elevator quantity variables. It then invokes the setFloorList
     * and setElevatorList methods to populate the arrayLists with the specified
     * amount of floor and objects. These methods propgate exceptions that the 
     * constructor can catch and print a related stack trace
     * 
     * @since Version 1.0
     * @see readXML()
     * @see setFloorList(int nF)
     * @see #setElevatorList(int nE)
     */
    private Singl_WorkingBuilding() {
        
        try{
            setNumberOfFloors(Singl_WorkingSimulation.getInstance().getNumberOfFloors());
            setNumberOfElevators(Singl_WorkingSimulation.getInstance().getNumberOfElevators());
            
            setFloorList(getNumberOfFloors());
            setElevatorList(getNumberOfElevators());
            
        } catch (IllegalFloorException ex) {
            ex.printStackTrace();
        }  catch (IllegalElevatorException ex) {
            ex.printStackTrace();
        } 

    }
    
    /**
    * This is a public static method that returns an instance of the Building object.
    * The method contains conditionals to check if the instance has already been
    * instantiated. If so, then it returns the pre-existing object. If not, then
    * inside a synchronized block (to protect against multiple threads potentially
    * create two instantiations of the class)
    * 
    * @since Version 1.0
    * @return the instance variable which is the Building object
    */
    public static Singl_WorkingBuilding getInstance() {
        
        if(instance == null)
            synchronized(Singl_WorkingBuilding.class)
            {
                if(instance == null)
                {
                    instance = new Singl_WorkingBuilding();
                }
            }
        
        return instance;
        
    }
    
    /**
     * A public method used to get the local numberOfElevators variable. It returns
     * the local numberOfElevators variable
     * 
     * @since Version 1.0
     * @return an int which represents the elevators of floors in the building
     */  
    public int getNumberOfElevators() {
        return numberOfElevators;
    }

    /**
     * A public method used to get the local numberOfFloors variable. It returns
     * the local numberOfFloors variable
     * 
     * @since Version 1.0
     * @return an int which represents the number of floors in the building
     */ 
    public int getNumberOfFloors() {
        return numberOfFloors;
    }
    
    /**
     * A public method used to get the local elevatorList variable. It returns
     * the local elevatorList variable
     * 
     * @since Version 1.0
     * @return an a List collection of all the elevator objects in the building
     */ 
    public List<Interface_Elevator> getElevatorList() {
        return elevatorList;
    }
    
    /**
     * A public method used to get the local floorList variable. It returns
     * the local floorList variable
     * 
     * @since Version 1.0
     * @return an a List collection of all the floor objects in the building
     */ 
    public List<Interface_Floor> getFloorList() {
        return floorList;
    }
    
    /**
     * A private method used to set the local numberOfElevators variable. It 
     * takes an int as an argument and it checks to ensure that the int is 
     * positive. If not, propagates an InvalidDataException. Else, it sets the 
     * value of numberOfElevators to the value of the argument provided.
     * 
     * @see #numberOfElevators
     * @since Version 1.0
     * @param nOE the int to set the numberOfElevators with
     * @throws InvalidDataException if the argument provided is less that zero
     */  
    private void setNumberOfElevators(int nOE) throws IllegalElevatorException {
        if(nOE < 0)
            throw new IllegalElevatorException("Negative number of elevators "
                                                       + "encountered: " + nOE);
        else
            numberOfElevators = nOE;        
    }

    /**
     * A private method used to set the local numberOfFloors variable. It takes an
     * int as an argument and it checks to ensure that the int is positive. If not,
     * propagates an InvalidDataException. Else, it sets the value of numberOfFloors
     * to the value of the argument provided.
     * 
     * @see #numberOfFloors
     * @since Version 1.0
     * @param nOE the int to set the numberOfFloors with
     * @throws InvalidDataException if the argument provided is less that zero
     */
    private void setNumberOfFloors(int nOF) throws IllegalFloorException {
        
        if(nOF < 0 || nOF > Singl_WorkingSimulation.getInstance().getNumberOfFloors())
            throw new IllegalFloorException("Invalid number of floors "
                                                       + "encountered: " + nOF);
        else
            numberOfFloors = nOF;
    }

    /**
     * A private method used to populate the elevatorList arraylist. It takes an
     * int as an argument and it checks to ensure that the int is positive. If not,
     * propagates an InvalidDataException. Else, it populate the elevatorList 
     * arraylist with the value of the argument provided.
     * 
     * @see #elevatorList
     * @since Version 1.0
     * @param nOE the int to populate the elevatorList arraylist
     * @throws InvalidDataException if the argument provided is less that zero
     */
    private void setElevatorList(int nE) throws IllegalElevatorException {
        
        if(nE < 0)
            throw new IllegalElevatorException("Negative number of elevators to build "
                    + "collection encountered: " + nE);
        else
        {
            for(int i = 0; i < nE; ++i){ 
                elevatorList.add(Fact_ElevatorFactory.build());
            }
        }
    }

    /**
     * A private method used to populate the elevatorList floorList. It takes an
     * int as an argument and it checks to ensure that the int is positive. If not,
     * propagates an InvalidDataException. Else, it populate the elevatorList floorList
     * with the value of the argument provided.
     * 
     * @see #floorList
     * @since Version 1.0
     * @param nOE the int to populate the floorList arraylist
     * @throws InvalidDataException if the argument provided is less that zero
     */
    private void setFloorList(int nF) throws IllegalFloorException { 
        
        if(nF < 0)
            throw new IllegalFloorException("Negative number of elevators to build "
                    + "collection encountered: " + nF);
        else
        {
            for(int i = 0; i < nF; ++i){ 
                
                floorList.add(Fact_FloorFactory.build());
            }
        }
    }
    
}
