package SimulationEnvironment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Vector;

import CTCUI.CTCJFrame;
import Track.*;
import TrainControlUI.DriverUI;
import TrainModel.Train;
import TrainModel.trainGUI;
import WaysideController.WaysideSystem;
import WaysideGUI.WaysideUIJFrameWindow;
import WorldClock.*;
import CTCOffice.*;
import TrackConstruction.*;
import implementation.TrainControl;

public class SimulationEnvironment extends Thread {
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
    private DisplayLine ctc;
    private Track trackSystem = new Track();

    /** World Object Variables
     */
    private Vector<TrainUnit> trains = new Vector<TrainUnit>();

    public SimulationEnvironment() {
        /** create a new Simulation Environment which contains a world clock and a ctc office (which has its own WaysideSystem on construction.)
         */
        clk= new WorldClock();
        ctc= new DisplayLine(trackSystem,this);
        this.start();
    }

    public void castGreenLine() {
        /** spawns a simulationEnvironment specifically built for the green line;
         */
        String greenLineFile = "/Users/elijah/IdeaProjects/ECE1140_Group2_TransitSimulation/SimulationEnvironment/SEResources/GreenAndRedLine.csv";
        importTrack(greenLineFile);

        WaysideSystem system = ctc.getWaysideSystem();
        // Create single wayside controller for GreenLine
        // Create bock jurisdiction
        /* Old code for one controller with all of green line
        int sizeOfGreenLine = trackSystem.getGreenLine().size();
        int[] blockNumbers = new int[sizeOfGreenLine];
        for (int i=0; i<sizeOfGreenLine;i++) {
            // BlockNum may be an arbitrary number, treat it as such
            // Skip i=0, train yard
            TrackElement element = trackSystem.getGreenLine().get(i);
            blockNumbers[i] = element.getBlockNum();
        }
        */
        int[] controller1Blocks = new int[21];
        int[] controller2Blocks = new int[38];
        int[] controller3Blocks = new int[13];
        int[] controller4Blocks = new int[13];
        int[] controller5Blocks = new int[8];
        int[] controller6Blocks = new int[20];
        int[] controller7Blocks = new int[1];
        int[] controller8Blocks = new int[38];
        int j;

        //controller1
        for(int i=0;i <= 20;i++){
            controller1Blocks[i] = i;
        }

        //controller2
        j = 21;
        for(int i=0;i <= 26;i++){
            controller2Blocks[i] = j;
            j++;
        }
        j = 140;
        for(int i=27;i <= 37;i++){
            controller2Blocks[i] = j;
            j++;
        }

        //controller3
        j=48;
        for(int i=0;i <= 12;i++){
            controller3Blocks[i] = j;
            j++;
        }

        //controller4
        j=61;
        for(int i=0;i <= 12;i++){
            controller4Blocks[i] = j;
            j++;
        }

        //controller5
        j=74;
        for(int i=0;i <= 6;i++){
            controller5Blocks[i] = j;
            j++;
        }
        controller5Blocks[7] = 101;

        //controller6
        j=81;
        for(int i=0;i <= 19;i++){
            controller6Blocks[i] = j;
            j++;
        }

        //controller7
        controller7Blocks[0] = 0;

        //controller8
        j=102;
        for(int i=0;i <= 37;i++){
            controller8Blocks[i] = j;
            j++;
        }
        //
        try {
            // Create controllers from jurisdictions
            system.addWaysideController(controller1Blocks);
            system.addWaysideController(controller2Blocks);
            system.addWaysideController(controller3Blocks);
            system.addWaysideController(controller4Blocks);
            system.addWaysideController(controller5Blocks);
            system.addWaysideController(controller6Blocks);
            system.addWaysideController(controller7Blocks);
            system.addWaysideController(controller8Blocks);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String scheduleFilename = "/Users/elijah/IdeaProjects/ECE1140_Group2_TransitSimulation/Application/Resources/schedule.csv";

        // Add PLCs to controller
        try {
            system.addOutputWaysideController(12, "SwitchBlock12PLC");
            system.addOutputWaysideController(29, "SwitchBlock29PLC");
            system.addOutputWaysideController(58, "SwitchBlock58PLC");
            system.addOutputWaysideController(62, "SwitchBlock62PLC");
            system.addOutputWaysideController(76, "SwitchBlock76PLC");
            system.addOutputWaysideController(85, "SwitchBlock85PLC");
            system.addOutputWaysideController(0, "SwitchYardPLC");
            system.updateAllOutputsWaysideController();
        } catch(Exception e) {
            System.out.println(":(");
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
    public CTCJFrame spawnCTCGUI(DisplayLine ctc) {
        return new CTCJFrame(ctc);
    }

    public WaysideUIJFrameWindow spawnWaysideGUI (WaysideSystem ws) {
        WaysideUIJFrameWindow newUI = null;
        try {
            newUI = new WaysideUIJFrameWindow(ws);
            newUI.latch(ws);
            return newUI;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void addTrain(TrainUnit newTrain) {
        trains.add(newTrain);
    }

    public void run() {
        while(true) {
            Vector<WaysideSystem> ws = ctc.getWaysideSystems();
            for (WaysideSystem system : ws) {
                try {
                    system.updateAllOutputsWaysideController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public WorldClock getClock() {return clk;}
    public void pauseTime() {clk.halt();}
    public void startTime() {clk.start();}
    public void setClockSpeed(double speed) {clk.setRatio(speed);}
    public void setClockResolution(double res) {clk.setResolution(res);}
    public DisplayLine getCTC() {return ctc;}
    public Track getTrackSystem() {return trackSystem;}
    public Vector<TrainUnit> getTrains() {return trains;}
}
