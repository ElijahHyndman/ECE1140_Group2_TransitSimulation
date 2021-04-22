package SimulationEnvironment;

import java.util.Vector;

import WorldClock.*;
import CTCOffice.*;
import TrackConstruction.*;

public class SimulationEnvironment {
    /** hosts the entirety of a Simulation World, including all submodules, for a train simulation project.
     * SimulationEnvironment also handles synchronization using a WorldClock, as well as hosting a server to
     * send copies of objects down to local machines for viewing and manipulating.
     * TODO should the simulation environment be contained in a SimulationProject class, which tracks the admin's settings
     * TODO the project will host the server instead of the simulation environment? it would make more sense. Then, the GUI would be for the project which can exist without an SE existing yet
     *
     * @assert physicsUpdates wi
     * ll happen automatically and constinually so long as the WorldClock is running
     * @assert TrainUnits will update the controller's fields pulled from the TrainModel only while the TrainUnit is running
     */
    /** World Management Variables
     */
    private WorldClock clk;
    private CTCOffice ctc;

    /** World Object Variables
     */
    private Vector<TrainUnit> trains = new Vector<TrainUnit>();

    public SimulationEnvironment() {
        clk= new WorldClock();
    }

    public void spawnRunningTrain(TrainUnit newTrain, TrackElement spawnLocation) {
        /** spawns an already created, given train at a specific, already created TrackElement and DOES set it to running
         *  on new thread.
         *  @before user has a TrainUnit Object and a TrackElement Object, TrainUnit does not exist within SE yet
         *  @after user's TrainUnit now exists within the SE, TrainUnit has been placed onto user's TrackElement
         */
        spawnTrain(newTrain,spawnLocation);
        newTrain.start();
    }

    public void spawnTrain(TrainUnit newTrain, TrackElement spawnLocation) {
        /** spawns an already created, given train at a specific, already created TrackElement but doesn't set it to running
         *  on new thread=]]
         *  @before user has a TrainUnit Object and a TrackElement Object, TrainUnit does not exist within SE yet
         *  @after user's TrainUnit now exists within the SE, TrainUnit has been placed onto user's TrackElement
         */
        addTrain(newTrain);
        newTrain.placeOn(spawnLocation);
    }
    public void addTrain(TrainUnit newTrain) {
        trains.add(newTrain);
    }

    public WorldClock getClock() {
        return clk;
    }
}
