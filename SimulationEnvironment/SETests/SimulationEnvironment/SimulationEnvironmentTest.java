package SimulationEnvironment;

import Track.Track;
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
        Track System Tests
     */

    @Test
    @DisplayName("Track Import\t\t[SE can import the Green Line]")
    void importGreenLine() {
        String greenLinePath = "SEResources/GreenAndRedLine.csv";
        SE = new SimulationEnvironment();
        SE.importTrack(greenLinePath);

        Track importedTrack = new Track();
        importedTrack.importTrack(greenLinePath);

        // Tracks yield the same output strings
        assertEquals(true, importedTrack.toString().equals(SE.getTrackSystem().toString()));
        assertEquals(true, importedTrack.toString().equals(SE.getCTC().getTrack().toString()));

        // Track that SE points to is the same that CTC points to
        assertSame(SE.getTrackSystem(),SE.getCTC().getTrack());
    }

    /*
        Train Tests
     */

    @Test
    @DisplayName("Train Spawning\t\t[Running TrainUnit can be spawned on a synthesized yard]")
    void trainsCanBeSpawnedRunningWithinATrainYard() {
        SE = new SimulationEnvironment();
        TrackBlock TrainYard1 = new TrackBlock();
        TrainUnit runningTrain = SE.spawnRunningTrain(TrainYard1,new TrackBlock());

        // Train takes a few microseconds for changes to become apparent
        waitForTrainObjectToCatchUp();
        assertEquals(true, runningTrain.isRunning());

        runningTrain.halt();
    }

    @Test
    @DisplayName("Train Spawning\t\t[Running TrainUnit will listen to physics updates]]")
    void trainUnitPhysicsUpdates() {
        SE = new SimulationEnvironment();
        TrackBlock TrainYard1 = new TrackBlock();
        TrainUnit runningTrain = SE.spawnRunningTrain(TrainYard1,new TrackBlock());

        SE.startTime();
        runningTrain.updateFlag = false;

        // Wait for update to occur
        // If update does not occur, wait forever
        while(!runningTrain.updateFlag) {}

        SE.pauseTime();
        runningTrain.halt();
    }

    @Test
    @DisplayName("Train Spawning\t\t[TrainUnits will spawn with correct, incrementing TrainIDs]")
    void trainIDs() {

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