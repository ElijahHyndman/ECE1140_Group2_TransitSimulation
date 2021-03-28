package SimulationEnvironment;

import Track.Track;
import TrackConstruction.TrackElement;
import TrainModel.Train;
import implementation.TrainControl;

import java.util.concurrent.TimeUnit;
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
     *  Usage:
     *      Instantiating a TrainUnit with:
     *      TrainUnit myTrain = new TrainUnit();
     *      will instantiate a TrainModel and TrainController working together.
     *
     *      TrainUnits are meant to exist on TrackElements. A TrainUnit can be placed on an existing TrackElement with
     *      myTrain.placeOn(myTrackElement);
     *
     *      There are two operations required to (1) interacting with the outside world; such as interacting with a TrackElement placed under it
     *      (2) have a train unit update physics, such as moving and calculating power
     *      (1) Having the train interact with the outside world
     *              This requires using the start() function on the train. This is useable because TrainUnit extends the Java Thread class.
     *              using:
     *              myTrain.start();
     *              will launch the train on a new Java thread and have it execute everything in the run() loop until you use:
     *              myTrain.halt();
     *
     *              The run() loop handles everything with interacting with the track block, such as pulling a new authority and speed into the TrainModel.
     *              once the .halt() method is executed, the train will stop interacting with the Track below it and the trainUnit will sustain the most recent
     *              speed and authority values read from the track
     *      (2) Having the train update physics, such as moving and calculating power
     *              Although the train may be interacting with the environment using run() (i.e. start()), time is still stagnant so the train will not move.
     *              movement and physics requires a world clock, which will advanced World-Time and call the TrainUnit to update its physics, such as calculating a new Actual Speed and Power.
     *              This can be accomplished by creating a new WorldClock object and linking the TrainUnit to it:
     *
     *              TrainUnit myTrain = new TrainUnit();
     *              double simulationSecondsPerRealSecond = 1.0;
     *              double physicsUpdatesPerSimulationSecond = 1.0;
     *              WorldClock physicsClock = new WorldClock(physicsUpdatesPerSimulationSecond,simulationSecondsPerRealSecond);
     *              physicsClock.addListener(myTrain);
     *              physicsClock.start();
     *
     *              Once this is executed, the physics clock will call the TrainUnit to updates its physics at constant intervals.
     *              For now, however, this train will never gain speed as it requires a speed and authority passed through a trackelement (as per requirements.)
     *              to get this train to a velocity of 5.0, you will need to place it on a TrackElement with commanded speed of 5.0 and a non-zero authority, then use myTrain.start() to begin pulling these values from the track
     *
     *  Big Ideas
     *  - TrainUnit handles the train's location in the Simulation World. The TrainUnit will know what TrackElement it occupies
     *  and it will handle the movement around the track/transition between TrackElements during the physics-update. The TrainModel
     *  and TrainController will be unaware of anything about where the train physically occupies within the world
     * - TrainUnit is not Real-World deployable, because it is based on TrainModel which is purely a software construct and shall
     * be thrown out whenever the application gets deployed.
     * @ergo no vital or control operations should be handled by the TrainUnit. If something must happen within the train, then it must be handled by the
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
    private Track trackLayout;

    private double blockLength;

    volatile private double COMMANDED_SPEED=-1.0;
    volatile private double COMMANDED_AUTHORITY=-1.0;

    /** Threading Members
     */
    // Volatile keeps data member in common CPU memory
    // so we can access it outside of thread
    volatile private boolean running;
    volatile public boolean updateFlag;
    volatile public boolean blockExceededFlag;

    volatile private double actualSpeed;

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
    public Level defaultConsoleVerboseness = Level.FINE;
    public Level defaultFileVerboseness = Level.FINER;

    /** Debugging Variables
     */
    private boolean controllerDisconnected = false;


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

    /*
            User defined Methods
     */

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

    /*
            Methods for operation
     */

    public boolean placeOn(TrackElement location) {
        /** places this Train Unit onto a specific TrackElement.
         *  handles the block occupation signal for the block which it is entering.
         *  does NOT handle the block occuption signal for the block which we were previously on.
         *
         * @param location TrackElement, the track element which the train shall inhabit.
         *          @more location shall be a TrackBlock, Train Yard, Station, or Switch
         *
         * @before Train may or may not be on a TrackElement
         * @after Train is guaranteed placed on a TrackElement (given param)
         * @after the new TrackElement's "occupy" signal has been set.
         * @after "occupy" signal for the block that the train is no longer occupying has NOT been unset
         * @after Block distance of the new TrackElement has been stored in "blocklength" class member
         */
        try {
            // Note what track we are occupying
            // Set that track as occupied
            // Note how long this new track is
            occupies = location;
            location.setOccupied(true);
            blockLength = occupies.getLength();
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
            // log event
            trainEventLogger.fine(String.format("TrainUnit (%s : %s) has moved from block %s to block %s",name, this.hashCode(),lastOccupied.hashCode(),occupies.hashCode()));
            return true;
        }catch (Exception e) {

        }
        return false;
    }

    @Override
    public void updatePhysics(String currentTimeString, double deltaTime_inSeconds) {
        /** updates physics of the TrainModel and TrainController, called on a regular basis externally by the World's
         * physics WorldClock.
         */
        // Update Hull's physics
        hull.updatePhysicalState(currentTimeString,deltaTime_inSeconds);

        trainEventLogger.fine(String.format("Physics Update TrainUnit (%s : %s) delta_T = %.4fsec, \nTrainModel update physics [actualSpeed,totalDist,blockDist] [%.2f,%.2f,%.2f] time (%s)",
                                                name,this.hashCode(),
                                                deltaTime_inSeconds,
                                                hull.getActualSpeed(),hull.getTotalDistance(),hull.getBlockDistance(),
                                                currentTimeString)
        );

        // Update Controller's physics
        if (!controllerDisconnected)
            control.updateCommandOutputs(currentTimeString,deltaTime_inSeconds);
        updateFlag = true;
    }

    @Override
    public void run() {
        /** runs the TrainUnit on a new Java Thread and handles interactions between the TrainUnit and the
         *  outside world (IE the Simulation Environment and the tracks)
         *
         * Overrides the Thread function run(), called using start(). Creates a new thread and runs
         * this on that thread
         *
         * @before TrainUnit still exists on the thread where it was created on
         * @before is not reactive to changes on the track, and does not manage distances or locations
         * @after TrainUnit is executing on a new thread
         * @after TrainUnit is polling TrackElement beneath it for new information
         * @after TrainUnit is managing distances on the TrackElement
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
            handleBlockTransitions();
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

    public void handleBlockTransitions() {
        /** monitors the distance traveled along a block.
         *  When TrainUnit's distance exceeds the Block's length, handles the transition to the next block.
         *  Called during run() processes.
         *
         * @before TrainUnit may have exceeded the length of the block since last PhysicsUpdate
         * @after If TrainUnit has not exceeded the length of the block, do nothing
         * @after If TrainUnit has exceeded the length of the block, get the next block connected to TrackElement and
         *  place train on it, starting at the appropriate distance. If next block does not exist, derail train and fail
         * @after Sets blockExceededFlag to true, if user wants to track when the train exceeds its current block
         */

        double currentDistanceOnBlock = hull.getBlockDistance();

        // If we have exceeded length of current block
        if (currentDistanceOnBlock > blockLength) {
            blockExceededFlag = true;
            // Find how far we are onto the next block (since physicsUpdates are spontaneous)
            double overshoot = currentDistanceOnBlock - blockLength;
            trainEventLogger.fine(String.format("TrainUnit (%s : %s) has exceeded TrackElement (%s) by (%f) meters",name,this.hashCode(),occupies.hashCode(),overshoot));

            // Next track block has to be retrieved from Track object
            if(trackLayout != null) {
                // Uses current block and last occupied block to determine appropriate next block
                TrackElement nextBlock = trackLayout.getNextSimple(occupies,lastOccupied);
                // Place Train onto next block
                transition(nextBlock);
                // Account for possible overshoot
                hull.setBlockDistance(overshoot);
                trainEventLogger.fine(String.format("TrainUnit (%s : %s) has been fast fowarded on block (%s) to account for overshoot of %.2f meters",name,this.hashCode(),occupies.hashCode(),overshoot));
            }

             }
    }

    /*
            Utility methods
     */

    private void instantiateLogger() {
        // Clear any default settings that logger may have come with
        LogManager.getLogManager().reset();
        trainEventLogger.setUseParentHandlers(false);

        // Set logger verboseness
        trainEventLogger.setLevel(logVerboseness);

        // Create console and file handlers, connect them to logger
        consoleHandler = new ConsoleHandler();
        setConsoleVerboseness(defaultConsoleVerboseness);
        trainEventLogger.addHandler(consoleHandler);
        if(CREATE_LOGGING_FILE) {
            try {
                // If chosen, create file handler
                //fileHandler = new FileHandler(TRAIN_LOGGING_FILE_NAME,true);
                fileHandler = new FileHandler(TRAIN_LOGGING_FILE_NAME,true);
                //fileHandler.setLevel(fileVerboseness);
                setFileVerboseness(defaultFileVerboseness);

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

    public String toString() {
        return String.format("TrainUnit (%s : %s)",name,this.hashCode());
    }
    public String informationString() {
        if(occupies == null || lastOccupied == null) {
            return String.format("%s\n" +
                            "Block Location (%s)\n" +
                            "Last Block Location (%s)\n" +
                            "Commanded Speed (%.1f)\n" +
                            "Authority (%.0f)\n" +
                            "Actual Speed (%.1f)\n",
                    toString(),
                    occupies, lastOccupied,
                    getCommandedSpeed(), getCommandedAuthority(), getActualSpeed());
        } else {
            return String.format("%s\n" +
                            "Block Location (%s%s)\n" +
                            "Last Block Location (%s%s)\n" +
                            "Commanded Speed (%.1f)\n" +
                            "Authority (%.0f)\n" +
                            "Actual Speed (%.1f)\n",
                    toString(),
                    occupies.getSection(), occupies.getBlockNum(),
                    lastOccupied.getSection(), lastOccupied.getBlockNum(),
                    getCommandedSpeed(), getCommandedAuthority(), getActualSpeed());
        }
    }
    public void setConsoleVerboseness(Level newLevel) {
        consoleHandler.setLevel(newLevel);
    }
    public void setFileVerboseness(Level newLevel) {
        fileHandler.setLevel(newLevel);
    }
    public void setControllerDisconnect(boolean isDisconnected) {
        controllerDisconnected = isDisconnected;
    }
    public void setReferenceTrack(Track referenceTrack) {this.trackLayout = referenceTrack;}
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
    public double getCommandedAuthority() {
        return COMMANDED_AUTHORITY;
    }
    public double getCommandedSpeed() {
        return COMMANDED_SPEED;
    }
    public double getActualSpeed() {
        actualSpeed = hull.getActualSpeed();
        return actualSpeed;
    }
}
