package SimulationEnvironment;

import TrackConstruction.TrackElement;
import TrainModel.Train;
import implementation.TrainControl;

import java.util.concurrent.TimeUnit;

public class TrainUnit extends Thread {
    /** class that instantiates a TrainModel and a TrainController together for the simulation.
     * a train model should never appear without a train controller, so this class will handle the spawning
     *  of the two together.
     *
     *  Big Ideas
     *  - TrainUnit handles the train's location in the Simulation World. The TrainUnit will know what TrackElement it occupies
     *  and it will handle the movement around the track/transition between TrackElements during the physics-update. The TrainModel
     *  and TrainController will be unaware of anything about where the train physically occupies within the world
     * - TrainUnit is not Real-World deployable, because it is based on TrainModel which is purely a software construct and shall
     * be thrown out whenever the application gets deployed.
     * @ergo no vital or operations should be handled by the TrainUnit. If something must happen within the train, then it must be handled by the
     *          TrainController which IS Real-World Deployable
     *
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
    private String name = "No Name Given";

    private TrainControl control;
    private Train hull;
    private TrackElement occupies;
    private TrackElement lastOccupied;

    // TrainUnit does NOT process speed or authority
    private double COMMANDED_SPEED;
    private double COMMANDED_AUTHORITY;

    // Volatile keeps data member in common CPU memory
    // so we can access it outside of thread
    volatile private boolean running;


    public TrainUnit() {
        control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
    }

    public TrainUnit(String name) {
        this.name = name;
        control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
    }


    private void whileTraversingBlock(TrackElement thisBlock) {
        /** put any code you want to execute while traversing a TrackElement in this function.
         *  is called during the run() function, which executes on a new thread.
         * @param
         */
        // Slowly print Chuga-Chuga
        try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {}
        System.out.println("Chuga-Chuga");

        // Train only reads Speed and Authority while running
        readSpeedAuthority();
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
            if(isOnTrack()) {
                // Perform block-traversal functions
                whileTraversingBlock(occupies);
            } else {
                // Do not run if not on some sort of track
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

    public void readSpeedAuthority() {
        retrieveAuthority();
        retrieveSpeed();
        System.out.println("Authority read as %f, Speed %f".formatted(COMMANDED_AUTHORITY,COMMANDED_SPEED));
    }

    public void retrieveAuthority() {
        /** gets the authority from the track the TrainUnit is on and passes it to TrainModel
         * TrainModel will pass authority on to the TrainController
         */
        if (occupies == null) {
            return;
        }
        COMMANDED_SPEED = occupies.getCommandedSpeed();
        hull.setSpeed(COMMANDED_SPEED);
    }
    public void retrieveSpeed() {
        /** gets the speed from the track the TrainUnit is on and passes it to the TrainModel
         * TrainModel will pass speed on to the TrainController
         */
        if (occupies == null) {
            return;
        }
        COMMANDED_AUTHORITY = occupies.getAuthority();
        hull.setAuthority((int) COMMANDED_AUTHORITY);
    }

    public TrainControl getControl() {
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
    public double readAuthority() {
        return COMMANDED_AUTHORITY;
    }
    public double readSpeed() {
        return COMMANDED_SPEED;
    }
}
