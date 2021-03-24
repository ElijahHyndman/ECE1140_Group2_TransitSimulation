package SimulationEnvironment;

import TrackConstruction.TrackElement;
import TrainModel.Train;
import implementation.TrainControl;

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
    private String name = "No Name";

    private TrainControl control;
    private Train hull;
    private TrackElement occupies;
    private TrackElement lastOccupied;

    // TrainUnit does NOT process speed or authority
    private double COMMANDED_SPEED=-1.0;
    private double COMMANDED_AUTHORITY=-1.0;

    // Volatile keeps data member in common CPU memory
    // so we can access it outside of thread
    volatile private boolean running;


    public TrainUnit() {
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
    }

    public TrainUnit(String name) {
        this.name = name;
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
    }

    public TrainUnit(boolean runOnStart) {
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
        if (runOnStart) start();
    }

    public TrainUnit(String name, boolean runOnStart) {
        this.name = name;
        //control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
        control = new TrainControl(hull);
        if (runOnStart) start();
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
            return true;
        }catch (Exception e) {

        }
        return false;
    }

    @Override
    public void updatePhysics(String currentTimeString, double deltaTime_inSeconds) {
        // println's are a big no-bo in physics updates
        System.out.println("updating physics");
        // TODO Train model crunches numbers to update its physics, calculate new actual speed
        // TODO Train model calcualtes its actual speed, and any other values Reagan requires for the Reagan's TODO below this
        // Train Controller gets updates from the TrainModel about Commanded Speed,(Commanded Authority,Actual Speed) to do
        // something like: public void updatePhysicalState(String currentTimeString, double deltaTime_inSeconds) {}
        control.getTrainData();
        // TODO Train Controller crunches numbers to updates its control commands, calculate power and control stuff
        // TODO Reagan: This one should only handle Brake, accel/decel,Power
        // something like: public void updateCommandOutputs(String currentTimeString,double deltaTime_inSeconds) {}
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
        System.out.println("Train has started running");
        running=true;
        // Uses AtomicBoolean running to allow us to end thread
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
        }
        System.out.println("Train has stopped running");
    }

    public void halt() {
        /** halts a train from running, exits the thread.
         *  This function is called by a user. The user is on a separate thread than the TrainUnit performing
         *  the run() function, so a user imposing a running=false will hault the run()'s while loop and exit
         *  @before TrainUnit may or may not be running
         *  @after TrainUnit is definitely not running
         */
        running=false;
    }

    public void retrieveSpeedAuthority() {
        /** Quick Method for gathering Speed,Authority from TrackElement, displays in one log
         */
        // Feed Speed and Authority from track to Train Hull
        retrieveAuthorityFromTrack();
        retrieveSpeedFromTrack();
        // Have Train Controller fetch Commanded Speed, Commanded Authority, and Actual Speed
        control.getTrainData();
        System.out.printf("Hull (%d,%f) control (%f,%f)\n",hull.getAuthority(),hull.getCommandedSpeed(),control.getAuthority(),control.getCommandedSpeed());
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
            hull.setAuthority((int) COMMANDED_AUTHORITY);
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
            return;
        }
        // Pull Commanded Speed from Track
        COMMANDED_SPEED = occupies.getCommandedSpeed();
        // Give Speed to Hull
        hull.setCommandedSpeed(COMMANDED_SPEED);
        // Control will pull these values during run() function
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
