package SimulationEnvironment;

import java.io.IOException;
import java.util.Vector;

import CTCUI.CTCJFrame;
import Track.*;
import TrainControlUI.DriverUI;
import TrainModel.Train;
import TrainModel.trainGUI;
import WorldClock.*;
import CTCOffice.*;
import TrackConstruction.*;
import implementation.TrainControl;

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
    private Track trackSystem = new Track();

    /** World Object Variables
     */
    private Vector<TrainUnit> trains = new Vector<TrainUnit>();

    public SimulationEnvironment() {
        /** create a new Simulation Environment which contains a world clock and a ctc office (which has its own WaysideSystem on construction.)
         */
        clk= new WorldClock();
        ctc= new CTCOffice(trackSystem,this);
    }

    public void castGreenLine() {
        /** spawns a simulationEnvironment specifically built for the green line;
         */
        String greenLineFile = "/Users/elijah/IdeaProjects/ECE1140_Group2_TransitSimulation/SimulationEnvironment/SEResources/GreenAndRedLine.csv";
        importTrack(greenLineFile);

        // Create single wayside controller for GreenLine
        // Create bock jurisdiction
        int sizeOfGreenLine = trackSystem.getGreenLine().size();
        int[] blockNumbers = new int[sizeOfGreenLine];
        for (int i=0; i<sizeOfGreenLine;i++) {
            // BlockNum may be an arbitrary number, treat it as such
            // Skip i=0, train yard
            TrackElement element = trackSystem.getGreenLine().get(i);
            blockNumbers[i] = element.getBlockNum();
        }
        //
        try {
            // Give jurisdiction to controller
            ctc.getWaysideSystem().addWaysideController(blockNumbers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        World Time Methods
     */
    public void setWorldTime(double worldTimeInHours) {
        /** sets the current world time to a double of hours since midnight.
         *  effect is immediate, if clk is running then it will continue accumulating onto new time.
         *  Usage:
         *  setWorldTime(2.5) sets world time to 2:50am
         *  setWorldTime(14.0) sets world time to 2:00pm
         */
        clk.setWorldTime(worldTimeInHours);
    }

    /*
        Track Methods
    */
    public void setTrack(Track newTrackSystem) {
        /** sets the track system for the entire simulation environment, updates the CTC.
         */
        trackSystem = newTrackSystem;
        ctc.setTrack(trackSystem);
    }

    public boolean importTrack(String trackCSVFilePath) {
        /** attempts to build trackSystem from the given TrackCSVFilePath.
         */
        Track newTrack = new Track();
        try {
            newTrack.importTrack(trackCSVFilePath);
        } catch (Exception e) {
            return false;
        }
        setTrack(newTrack);
        return true;
    }


    /*
        Train Methods
     */
    public TrainUnit spawnRunningTrain(TrackElement location, TrackElement awayFrom) {
        /** spawns a brand new train, given train at a specific, already created TrackElement and DOES set it to running
         *  on new thread. Gives the TrainUnit an individual train index
         *  @before user has a TrainUnit Object and a TrackElement Object, TrainUnit does not exist within SE yet
         *  @after user's TrainUnit now exists within the SE, TrainUnit has been placed onto user's TrackElement
         */
        // Create Train, set it to running on start
        TrainUnit newTrain = new TrainUnit(true);
        newTrain.setID(trains.size());

        // Place train onto spawn location
        newTrain.spawnOn(location,awayFrom);
        addTrain(newTrain);
        if(trackSystem != null)
            newTrain.setReferenceTrack(trackSystem);

        // Add to physics listeners
        clk.addListener(newTrain);
        System.out.println(String.format("New running train spawned (%s) and placed on block %s",newTrain,newTrain.getLocation()));

        return newTrain;
    }


//    public void placeRunningTrain(TrainUnit newTrain, TrackElement location) {
//        /** places an already created, given train at a specific, already created TrackElement and DOES set it to running
//         *  on new thread. Gives the TrainUnit an individual train index
//         *  @before user has a TrainUnit Object and a TrackElement Object, TrainUnit does not exist within SE yet
//         *  @after user's TrainUnit now exists within the SE, TrainUnit has been placed onto user's TrackElement
//         */
//        placeTrain(newTrain,location);
//        newTrain.start();
//    }
//
//    public void placeTrain(TrainUnit newTrain, TrackElement location) {
//        /** spawns an already created, given train at a specific, already created TrackElement but doesn't set it to running
//         *  on new thread. Gives the TrainUnit an individual train index
//         *  @before user has a TrainUnit Object and a TrackElement Object, TrainUnit does not exist within SE yet
//         *  @after user's TrainUnit now exists within the SE, TrainUnit has been placed onto user's TrackElement
//         */
//        addTrain(newTrain);
//        newTrain.placeOn(location);
//    }

    /*
        GUI Spawning
     */
    public DriverUI spawnTrainControllerUI(TrainUnit unit) {
        TrainControl ctrl = unit.getController();
        DriverUI newUI = new DriverUI();
        newUI.latch(ctrl);
        return newUI;
    }
    public trainGUI spawnTrainModelGUI(TrainUnit unit) {
        Train model = unit.getHull();
        trainGUI newUI = new trainGUI(0);
        newUI.latch(model);
        return newUI;
    }
    public TrackGUI spawnTrackBuilderGUI(Track system) {
        TrackGUI newUI = new TrackGUI(system);
        return newUI;
    }
    public CTCJFrame spawnCTCGUI(CTCOffice ctc) {
        return new CTCJFrame(ctc);
    }


    private void addTrain(TrainUnit newTrain) {
        trains.add(newTrain);
    }

    public WorldClock getClock() {return clk;}
    public void pauseTime() {clk.halt();}
    public void startTime() {clk.start();}
    public void setClockSpeed(double speed) {clk.setRatio(speed);}
    public void setClockResolution(double res) {clk.setResolution(res);}
    public CTCOffice getCTC() {return ctc;}
    public Track getTrackSystem() {return trackSystem;}
    public Vector<TrainUnit> getTrains() {return trains;}
}
