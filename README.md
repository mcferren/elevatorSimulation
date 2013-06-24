elevatorSimulation: object-oriented elevator simulator
======================================================

This simulator application models a building, its floors, its elevators, its call boxes, controllers, its people,
etc. in order to perform a variety of analyses that will help determine the optimal elevator configuration
for a given building. Additionally this application can predict the effects of changing the “default” floor
for one or more elevators, and can predict the expected effect of taking an elevator down for repairs on
the building’s population.

This simulation has setup, action and shutdown methods to prepare all the relevant objects, run them over a time 
interval determined in an external data file, and then shut them down. It also contains methods to run reports on 
the wait and ride times resulting from the simulation. 


Simulation Component Descriptions
=================================

** Person **
Person objects are created and initially/randomly placed on a floor of the building. They call
the elevator (by pressing the Up or Down call button), enter the elevator when it arrives, they
select a destination floor, they will ride the elevator to their destination, then exit the
elevator at their desired floor.

** Elevator **
Elevators travel up and down to the various floors of the building. Elevators have a
button panel that allows riders (Persons) to select a destination floor. Elevators have a maximum
passenger count (i.e., 10 people), they have a time in milliseconds per floor value (i.e., the speed
of the elevator – for example, 1000 ms per floor), and a door-operation-time in milliseconds (i.e.,
how long the elevator door takes to open at a floor, remain open for Persons to enter & exit, and
to close).

** Elevator Call Box **
Each building floor has an elevator call box – this has an up and down button
that is used to call an elevator to their floor.

** Floor **
Each floor of the building holds a number of Person objects. Some will be waiting for an
elevator (after pressing a button on the elevator call box). Others will remain on that after exiting
an elevator. Each floor has an elevator call box where an elevator can be requested (called) to go
up or down.

** Building **
Each building owns a certain number of floors and a certain number of elevators. A
building also owns an elevator controller that coordinates the actions of all elevators.

** Elevator Controller **
The elevator controller determines which elevator should respond to a
request made at an elevator call box, and when. The elevator controller instructs individual
elevators to go to certain floors to respond to elevator requests made by Person objects.


 
