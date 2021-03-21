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
     * @member occupies TrackElement, the track block which this train is sitting on in the the Simulation World
     *          @further a Train Unit might be set on a track block, train yard, station, or track switch
     *
     */
    private TrainControl control;
    private Train hull;
    private TrackElement occupies;


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
         * @before Train may or may not be occupying a block
         * @after Train is "placed" onto a block, the block's "occupy" signal has been set.
         * @after "occupy" signal for the block that the train is no longer occupying has NOT been unset
         */
        try {
            occupies = location;
            return true;
        } catch (Exception e) {

        }
        return false;
    }


    public TrainControl getControl() {
        return control;
    }
    public Train getHull() {
        return hull;
    }
}
