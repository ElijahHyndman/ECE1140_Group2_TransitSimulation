package SimulationEnvironment;

import TrainModel.Train;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import TrackConstruction.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SimulationEnvironmentTest {
    SimulationEnvironment SE;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }



    /*
        Train Tests
     */


    @Test
    @DisplayName("Trains can be constructed without being set to run")
    void trainsCanBeSpawnedWithinATrainYard() {
        SE = new SimulationEnvironment();
        // TODO will a trainyard always be a trackblock? wil we make one specifically for the trainyard?
        TrackBlock TrainYard1 = new TrackBlock();
        TrainUnit nonRunningTrain = new TrainUnit();
        SE.spawnTrain(nonRunningTrain, TrainYard1);

        assertEquals(false,nonRunningTrain.isRunning());
        assertEquals(TrainYard1,nonRunningTrain.getLocation());
    }


    @Test
    @DisplayName("Trains can be be constructed and set to run on construction")
    void trainsCanBeSpawnedRunningWithinATrainYard() {
        SE = new SimulationEnvironment();
        // TODO will a trainyard always be a trackblock? wil we make one specifically for the trainyard?
        TrackBlock TrainYard1 = new TrackBlock();
        TrainUnit runningTrain = new TrainUnit();
        SE.spawnRunningTrain(runningTrain,TrainYard1);

        // Train takes a few microseconds for changes to become apparent
        waitForTrainObjectToCatchUp();
        assertEquals(true, runningTrain.isRunning());

        runningTrain.halt();
    }


    /*
        Helper Functions
     */


    public static void waitForTrainObjectToCatchUp() {
        /** waits a short amount of time doing nothing to wait for the TrainUnit object to catch up.
         */
        // A time delay of one microsecond
        try { TimeUnit.MICROSECONDS.sleep(1); } catch (Exception e ){}
    }
}