package SimulationEnvironment;

import TrackConstruction.TrackElement;
import TrainModel.Train;
import implementation.TrainControl;

public class TrainUnit {
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
    private TrainControl control;
    private Train hull;
    private TrackElement occupies;
    private TrackElement lastOccupied;


    public TrainUnit() {
        control = new TrainControl();
        hull = new Train(DEFAULT_NUM_CARS,DEFAULT_NUM_TRAIN_CREW);
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
            return true;
        }catch (Exception e) {

        }
        return false;
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
}
