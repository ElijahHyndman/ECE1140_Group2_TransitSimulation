package SimulationEnvironment;

import CTCOffice.*;
import CTCUI.CTCJFrame;
import Track.*;
import TrainControlUI.DriverUI;
import TrainModel.trainGUI;
import WaysideController.WaysideSystem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import TrackConstruction.*;

import java.io.IOException;
import java.util.ArrayList;
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
        CTC tests
    */
    @Test
    @DisplayName("CTC\t\t[CTC will get new Track System when SE is updated with an import]")
    void ctcTrackSystemUpdates() {
        // Whenever the SE imports a new track system, the CTC will know
        SE = new SimulationEnvironment();
        SE.importTrack("SEResources/GreenAndRedLine.csv");
        assertEquals(SE.getTrackSystem(),SE.getCTC().getTrack());
    }

    @Test
    void ctcHasWaysideSystem() {
        SE = new SimulationEnvironment();
        // CTC has wayside system
        assertNotEquals(false,SE.getCTC().getWaysideSystem());
    }

    @Test
    @DisplayName("CTC\t\t[CTC will spawn a train in Yard when a new dispatch is called]")
    void ctcWillSpawnTrainAtYard() {
        SE = new SimulationEnvironment();
        SE.importTrack("SEResources/GreenAndRedLine.csv");
        CTCOffice ctc = SE.getCTC();

        // Create a single wayside controller to handle greenline
        int[] blockNumbers = new int[SE.getTrackSystem().getGreenLine().size()];
        for (int i=0; i<SE.getTrackSystem().getGreenLine().size();i++) {
            // BlockNum may be an arbitrary number, treat it as such
            // Skip i=0, train yard
            TrackElement element = SE.getTrackSystem().getGreenLine().get(i);
            blockNumbers[i] = element.getBlockNum();
        }
        try {
            // Give jurisdiction to controller
            SE.getCTC().getWaysideSystem().get(0).addWaysideController(blockNumbers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ctc.Dispatch("Dormont","new","00:00:20");
        SE.startTime();

        // The yard is the first element in the track system
        TrackElement spawningYard = SE.getTrackSystem().getBlock(0);

        // Assert that the first train created exists in spawning Yard
        TrainUnit spawnedTrain = SE.getTrains().get(0);
        assertEquals(spawningYard,spawnedTrain.getLocation());

        SE.pauseTime();
        spawnedTrain.halt();
    }

    @Test
    void ctcWillGenerateAuthorityAndSpeed() {
        SE = new SimulationEnvironment();
        SE.importTrack("SEResources/GreenAndRedLine.csv");
        CTCOffice ctc = SE.getCTC();

        // Create a single wayside controller to handle greenline
        int[] blockNumbers = new int[SE.getTrackSystem().getGreenLine().size()];
        for (int i=0; i<SE.getTrackSystem().getGreenLine().size();i++) {
            // BlockNum may be an arbitrary number, treat it as such
            // Skip i=0, train yard
            TrackElement element = SE.getTrackSystem().getGreenLine().get(i);
            blockNumbers[i] = element.getBlockNum();
        }
        try {
            // Give jurisdiction to controller
            // first wayside is green
            SE.getCTC().getWaysideSystem().get(0).addWaysideController(blockNumbers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ctc.Dispatch("Dormont","new","12:00:00");
        SE.startTime();

        // Get the train yard, set it to speed and authority
        SE.getTrackSystem().getBlock(0).setAuthority(2);
        SE.getTrackSystem().getBlock(0).setCommandedSpeed(10.0);

        TrackGUI trackui = new TrackGUI(SE.getTrackSystem());
        trackui.setVisible(true);
        trackui.latch(SE.getTrackSystem());

        while(true) {
            trackui.latch(SE.getTrackSystem());
        }
        //SE.getTrains().get(0).halt();
    }



    @Test
    @DisplayName("")
    void ctcTrainWillGetSpeedAuthority() {

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
    @DisplayName("Train Movement\t\t[TrainUnit will run around Green Line, not from yard]")
    void trainRunsGreenLine() {
        String greenLinePath = "SEResources/GreenAndRedLine.csv";
        SE = new SimulationEnvironment();
        SE.importTrack(greenLinePath);
        SE.setClockSpeed(10.0);
        SE.setClockResolution(5.0);
        //System.out.println(SE.getTrackSystem());

        ArrayList<TrackElement> greenLine = SE.getTrackSystem().getGreenLine();
        for(TrackElement block : greenLine) {
            block.setAuthority(10);
            block.setCommandedSpeed(15.0);
        }

        TrackElement startBlock = greenLine.get(60);
        TrackElement orientBlock = greenLine.get(59);
        TrainUnit runningTrain = SE.spawnRunningTrain(startBlock,orientBlock);


        SE.startTime();

        runningTrain.blockExceededFlag=false;

        DriverUI controllerUI = new DriverUI();
        trainGUI modelUI = new trainGUI(1);
        controllerUI.latch(runningTrain.getController());
        modelUI.latch(runningTrain.getHull());
        TrackGUI trackui = new TrackGUI(SE.getTrackSystem());
        trackui.setVisible(true);
        trackui.latch(SE.getTrackSystem());

        while(!(runningTrain.getHull().getActualSpeed() == 0.0)) {
            controllerUI.update();
            modelUI.updateDisplay();
            trackui.latch(SE.getTrackSystem());
        }
    }


    /*
        Green Line Tests
     */
    @Test
    void createGreenLine() throws IOException {
        SE = new SimulationEnvironment();
        SE.castGreenLine();
        //WaysideSystem ws = SE.getCTC().getWaysideSystems().get(0);
        WaysideSystem ws = new WaysideSystem();
        assertNotEquals(null,ws);
        assertNotEquals(null,ws.getController(0));

        //SE.getCTC().Dispatch("Mt Lebanon","new","01:01:01");
        SE.startTime();
        SE.setClockSpeed(10);

        TrackElement yard = SE.getTrackSystem().getGreenLine().get(0);
        // Switch out of yard
        SE.getTrackSystem().getSwitches().get(10).setSwitchState(true);
        SE.getTrackSystem().updateSwitches();
        yard.setAuthority(1);
        yard.setCommandedSpeed(10.0);
        //new CTCJFrame((DisplayLine)SE.getCTC()).setVisible(true);

        while(!(SE.getTrains().size() > 0)



        ) {}
        // Get the train unit for gui
        TrainUnit runningTrain = SE.getTrains().get(0);

        DriverUI controllerUI = new DriverUI();
        trainGUI modelUI = new trainGUI(1);
        controllerUI.latch(runningTrain.getController());
        modelUI.latch(runningTrain.getHull());
        TrackGUI trackui = new TrackGUI(SE.getTrackSystem());
        trackui.setVisible(true);
        trackui.latch(SE.getTrackSystem());


        while(true) {
            controllerUI.update();
            modelUI.updateDisplay();
            trackui.latch(SE.getTrackSystem());
        }
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