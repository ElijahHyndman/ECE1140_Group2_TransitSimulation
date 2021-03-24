package SimulationEnvironment;

import TrackConstruction.TrackElement;
import TrainModel.Train;
import implementation.TrainControl;

import java.util.logging.*;

public class TrainUnit extends Thread implements PhysicsUpdateListener {
    /** class that instantiates a TrainModel and a TrainController together for the simulation.
     * a train model should never appear without a train controller, so this class will handle the spawning
     *  of the two together.
     *
     *  IMPORTANT: there is a several microsecond delay for communications to and from a train running on a thread.
     *      You must wait a few milliseconds for any changes to be reflected onto a TrainUnit object and then be
     *      readable from the object
     *
     *  Big Ideas
     *  - TrainUnit handles the train's location in the Simulation World. The TrainUnit will know what TrackElement it occupies
     *  and it will handle the movement around the track/transition between TrackElements during the physics-update. The TrainModel
     *  and TrainController will be unaware of anything about where the train physically occupies within the world
     * - TrainUnit is not Real-World deployable, because it is based on TrainModel which is purely a software construct and shall
     * be thrown out whenever the application gets deployed.
     * @ergo no vital or operations should be handled by the TrainUnit. If something must happen within the train, then it must be handled by the
     *          TrainController which IS Real-World Deployable
     * - TrainUnit will update the values the TrainController sees from the TrainModel's state (by state, I mean PURELY the physics state of the train model)
     * everytime the physics-update call is sent by the WorldClock. We want these to always be called in tandem, because the TrainController is a PID
     * who adjusts the values of its commands according to how much the train model's physics has changed according to its last command. If the train controller
     * is updating its physics-calculations faster than the train model is updating its physics-calculations, then the train controller will see that no changes have occured
     * in the train model for a few measurement-polls in a row, creating insanely out-of-proportion commands to try and force the train model to change.
     * - TrainUnit will update the train model with updates from the track using the run() loop (such as reading beacons, reading authority and speed, etc), while the train is set to running. The run() loop may or may not be faster than
     * the loop from the WorldClock's periodic updatePhysics() calls. A difference in their frequency shouldn't have any effect other than some lag between the communication
     * coming from the track about Authority,Speed,Stopping distance, etc being reflected onto the physics of the train objects. This lag shal be inconsequential
     *      - Note there should be ample time for the TrainUnit to read the beacon from the tracks for stopping distance and station information. If the chance to read the beacon
     *      is too short, the run() loop might take too long to ever pick up the information from the beacon.
     *      - We define:
     *          -run() handles all interactions with the track and outside world
     *          -updatePhysics() handles all internal physics and number crunching
     * @author elijah
     */
    /** Default values
     */
    private final int DEFAULT_NUM_CARS = 3;
    private final int DEFAULT_NUM_TRAIN_CREW = 10;

    /**
     * Class Members
     * @member control TrainControl, controller sub-module that shall be attached to this TrainUnit
     * @member hull Train, train model sub-module that shall track the physics and state of the train unit
     * @member occupies TrackElement, the TrackElement which this train is sitting on in the the Simulation World
     *          @further a Train Unit might be set on a track block, train yard, station, or track switch
     * @member lastOccupied TrackElement, the TrackElement which the train last occupied, in case this is useful to know
     */
    private String name = "NoName";

    private TrainControl control;
    private Train hull;
    private TrackElement occupies;
    private TrackElement lastOccupied;

    private double COMMANDED_SPEED=-1.0;
    private double COMMANDED_AUTHORITY=-1.0;

    /** Threading Members
     */
    // Volatile keeps data member in common CPU memory
    // so we can access it outside of thread
    volatile private boolean running;
    volatile public boolean updateFlag;

    /** Logging Members
     *  Instead of System.out.println for all of the debugging for this file, I will be using a logger.
     *  I will have it output to the console, and have the option to output to a logging file.
     *  you can adjust the level of logs being displayed using the verboseness variables
     *
     * @member trainEventLogger     Logger, used to store holds logs while the program runs
     * @member TRAIN_LOGGING_FILE_NAME  String, name of the file where logs will be stored if CREATE_LOGGING_FILE is set to true
     *          @assert If two trains share the same name, or have not had their names set, then they will write to the same log file
     * @member consoleHandler   Handler, log handler that writes logs from trainEventLogger to console
     * @member fileHandler   Handler, log handler that writes logs from trainEventLogger to output file
     *
     * @member logVerboseness   Level, minimum level for log to be stored into logger while running
     * @member consoleVerboseness   Level, minimum level for stored logs to be written to console
     * @member fileVerboseness  Level, minimum level for stored logs to be written to output file
     * Level Hierarchy:
     * • SEVERE (highest level)
     * • WARNING
     * • INFO
     * • CONFIG
     * • FINE
     * • FINER
     * • FINEST (lowest level)
     */
    private Logger trainEventLogger = Logger.getLogger("TrainUnit_"+name+"_Logger");
    private String TRAIN_LOGGING_FILE_NAME = "TrainUnit_"+name+"_Logs.txt";
    // Creating logging file may slow down the thread-starting. be aware that this may incur a few microseconds of lag
    private boolean CREATE_LOGGING_FILE = false;
    private Handler consoleHandler;
    private Handler fileHandler;

    public Level logVerboseness = Level.ALL;
    public Level consoleVerboseness = Level.FINE;
    public Level fileVerboseness = Level.FINER;


    public TrainUnit() {
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
        instantiateLogger();
        trainEventLogger.info(String.format("Train Unit (%S) Constructed", name));
    }

    public TrainUnit(String name) {
        this.name = name;
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
        instantiateLogger();
        trainEventLogger.info(String.format("Train Unit (%S) Constructed", name));
    }

    public TrainUnit(boolean runOnStart) {
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
        instantiateLogger();
        if (runOnStart) start();
        trainEventLogger.info(String.format("Train Unit (%S) Constructed", name));
    }

    public TrainUnit(String name, boolean runOnStart) {
        this.name = name;
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
        instantiateLogger();
        if (runOnStart) start();
        trainEventLogger.info(String.format("Train Unit (%S) Constructed", name));
    }

    protected void finalize () {
        // If this is even called when train is destroyed, we should halt the thread
        halt();
    }

    private void whileTraversingBlock(TrackElement thisBlock) {
        /** put any code you want to execute while traversing a TrackElement in this function.
         *  is called during the run() function, which executes on a new thread.
         * @param
         */
    }

    private void onBlockTransition(TrackElement NewBlock, TrackElement oldBlock) {
        /** put any code you want to execute during a transition between blocks in this function.
         *
         * @param NewBlock TrackElement, the block we are entering, if you need to use it in this function
         * @param OldBlock TrackElement, the block we are leaving, if you need to use it in this function
         * @before nothing
         * @after train has transitioned from one block to another, whatever in this function has also executed
         */
    }


    public boolean placeOn(TrackElement location) {
        /** places a Train Unit onto a specific TrackElement.
         *  handles the block occupation signal for the block which it is entering.
         * @param location TrackElement, the track element which the train shall inhabit.
         *          @more location shall be a TrackBlock, Train Yard, Station, or Switch
         *
         * @before Train may or may not be on a TrackElement
         * @after Train is guaranteed placed on a TrackElement (given param)
         * @after the new TrackElement's "occupy" signal has been set.
         * @after "occupy" signal for the block that the train is no longer occupying has NOT been unset
         */
        try {
            occupies = location;
            location.setOccupied(true);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean transition(TrackElement location) {
        /** handles transition from current TrackElement to next TrackElement
         * @before Train may or may not be on a TrackElement
         * @after Train occupies new, given TrackElement and new TrackElement's occupation has been set
         * @after Train has remembered and stored the TrackElement which it was just on
         * @after the TrackElement Train used to be on's occupation has been unset
         *      TODO what if trains are back-to-back on block?
         *      TODO ask Grace to have setOccupation() to take TrainUnit as input, and then implement a isOccupied() to return boolean (i.e. TrainUnit on me == null)
         */
        try {
            // Remember last TrackElement occupation
            lastOccupied = occupies;
            // Set old TrackElement as unoccupied
            if (occupies != null) {
                occupies.setOccupied(false);
            }
            // Enter TrackElement block
            placeOn(location);
            // Call transition function, user may want something to happen
            onBlockTransition(occupies,lastOccupied);
            trainEventLogger.fine(String.format("TrainUnit (%s : %s) has moved from block %s to block %s",name, this.hashCode(),lastOccupied.hashCode(),occupies.hashCode()));
            return true;
        }catch (Exception e) {

        }
        return false;
    }

    @Override
    public void updatePhysics(String currentTimeString, double deltaTime_inSeconds) {
        // println's are a big no-bo in physics updates
        //System.out.println("updating physics");
        trainEventLogger.finest("Physics Update");
        // Update Hull's physics
        hull.updatePhysicalState(currentTimeString,deltaTime_inSeconds);
        trainEventLogger.finer(String.format("Physics Update TrainUnit (%s : %s) delta_T = %.4fsec \nTrainModel update physics [actualSpeed,totalDist,blockDist] [%.2f,%.2f,%.2f] ",
                                                name,this.hashCode(),
                                                deltaTime_inSeconds,
                                                hull.getActualSpeed(),hull.getBlockDistance(),hull.getTotalDistance()));
        control.getTrainData();
        // TODO Train Controller crunches numbers to updates its control commands, calculate power and control stuff
        // TODO Reagan: This one should only handle Brake, accel/decel,Power
        // something like: public void updateCommandOutputs(String currentTimeString,double deltaTime_inSeconds) {}
        updateFlag = true;
    }

    @Override
    public void run() {
        /** runs all processes that a TrainUnit must do while the application is running.
         * Overrides the Thread function run(), called using start(). Creates a new thread and runs
         * this on that thread
         * @before Train exists on Simulation Thread; has been created, is not reactive to events unless
         *  specifically acted upon/has a function invoked
         * @after Train is executing on a new thread; all code within run() is being executed while simulation is
         *  also running on an adjacent thread
         */
        //System.out.println("Train has started running");
        trainEventLogger.info(String.format("TrainUnit (%s : %s) has started running",name,this.hashCode()));
        running=true;
        while(running) {
            // Speed and Authority are checked, regardless of being on a track
            retrieveSpeedAuthority();

            if(isOnTrack()) {
                // Perform block-traversal functions
                whileTraversingBlock(occupies);
            } else {
                // Do not do track tasks if not on some sort of track
                continue;
            }

            // Check if we should progress to the next block

        }
        //System.out.println("Train has stopped running");
        trainEventLogger.info(String.format("TrainUnit (%s : %s) has stopped running",name,this.hashCode()));
    }

    public void halt() {
        /** halts a train from running, exits the thread.
         *  This function is called by a user. The user is on a separate thread than the TrainUnit performing
         *  the run() function, so a user imposing a running=false will hault the run()'s while loop and exit
         *  @before TrainUnit may or may not be running
         *  @after TrainUnit is definitely not running
         */
        running=false;
        trainEventLogger.info(String.format("TrainUnit (%s : %s) has been commanded to stop",name,this.hashCode()));
    }

    public void retrieveSpeedAuthority() {
        /** Quick Method for gathering Speed,Authority from TrackElement, displays in one log
         */
        // Feed Speed and Authority from track to Train Hull
        retrieveAuthorityFromTrack();
        retrieveSpeedFromTrack();
        trainEventLogger.finer(String.format("TrainUnit (%s : %s) TrainModel has pulled Speed/Authority (%f,%f) from the Track Circuit",name,this.hashCode(),hull.getCommandedSpeed(),hull.getAuthority()));
        // Have Train Controller fetch Commanded Speed, Commanded Authority, and Actual Speed
        control.getTrainData();
        trainEventLogger.finer(String.format("TrainUnit (%s : %s) TrainController has pulled Speed/Authority/ActualSpeed (%f,%f,%f) from TrainModel",name,this.hashCode(),control.getCommandedSpeed(),control.getAuthority(),control.getActualSpeed()));
        //System.out.printf("Hull (%f,%f) control (%f,%f)\n",hull.getAuthority(),hull.getCommandedSpeed(),control.getAuthority(),control.getCommandedSpeed());
      }

    private void retrieveAuthorityFromTrack() {
        /** gets the authority from the track the TrainUnit occupies and passes it to TrainModel and only the TrainModel.
         * The Controller will be responsible for getting the Authority from the TrainModel later
         * @before TrainUnit is or is not on a TrackElement
         * @before if Train is on a TrackElement, TrackElement has an authority for the train
         * @after TrainUnit's TrainModel "Hull" now has the Authority
         */
        if (occupies == null) {
            COMMANDED_AUTHORITY = -1.0;
            hull.setAuthority(COMMANDED_AUTHORITY);
            trainEventLogger.finest(String.format("TrainUnit (%s,%s) TrainModel is not on a rail, pulling invalid CommandedSpeed=-1.0",name,this.hashCode()));
            return;
        }
        // Pull Commanded Authority from Track
        COMMANDED_AUTHORITY = occupies.getAuthority();
        // Give Authority to Hull
        hull.setAuthority((int) COMMANDED_AUTHORITY);
        // Control will pull these values during run() function
    }
    private void retrieveSpeedFromTrack() {
        /** gets the speed from the track the TrainUnit occupies and passes it to TrainModel and only the TrainModel.
         * The Controller will be responsible for getting the Authority from the TrainModel later
         * @before TrainUnit is or is not on a TrackElement
         * @before if Train is on a TrackElement, TrackElement has a commanded speed for the train
         * @after TrainUnit's TrainModel "Hull" now has the commanded speed
         */
        if (occupies == null) {
            COMMANDED_SPEED = -1.0;
            hull.setCommandedSpeed(COMMANDED_SPEED);
            trainEventLogger.finest(String.format("TrainUnit (%s,%s) TrainModel is not on a rail, pulling invalid CommandedSpeed=-1.0",name,this.hashCode()));
            return;
        }
        // Pull Commanded Speed from Track
        COMMANDED_SPEED = occupies.getCommandedSpeed();
        // Give Speed to Hull
        hull.setCommandedSpeed(COMMANDED_SPEED);
        // Control will pull these values during run() function
    }

    public void instantiateLogger() {
        // Clear any default settings that logger may have come with
        LogManager.getLogManager().reset();
        trainEventLogger.setUseParentHandlers(false);

        // Set logger verboseness
        trainEventLogger.setLevel(logVerboseness);

        // Create console and file handlers, connect them to logger
        consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(consoleVerboseness);
        trainEventLogger.addHandler(consoleHandler);
        if(CREATE_LOGGING_FILE) {
            try {
                // If chosen, create file handler
                //fileHandler = new FileHandler(TRAIN_LOGGING_FILE_NAME,true);
                fileHandler = new FileHandler(TRAIN_LOGGING_FILE_NAME,true);
                fileHandler.setLevel(fileVerboseness);

                // Use the simple format for log style in file
                SimpleFormatter easyToRead = new SimpleFormatter();
                fileHandler.setFormatter(easyToRead);
                trainEventLogger.addHandler(fileHandler);
            } catch (Exception e) {
                trainEventLogger.severe("Failed to instantiate file logger");
                e.printStackTrace();
            }
        }
    }

    public TrainControl getController() {
        return control;
    }
    public Train getHull() {
        return hull;
    }
    public TrackElement getLocation() {
        return occupies;
    }
    public TrackElement getLastOccupation() {
        return lastOccupied;
    }
    public boolean isRunning() {
        return running;
    }
    public boolean isOnTrack() {
        return occupies != null;
    }
    public double getAuthority() {
        return COMMANDED_AUTHORITY;
    }
    public double getSpeed() {
        return COMMANDED_SPEED;
    }
}
