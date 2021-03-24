import SimulationEnvironment.*;
import TrackConstruction.*;

import TrainControlUI.DriverUI;
import TrainModel.Train;
import TrainModel.trainGUI;
import WorldClock.WorldClock;
import implementation.TrainControl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TrainUnitTest {
    TrainUnit trn;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    /*
        Spawning Tests
     */

    @Test
    @DisplayName("TrainUnit spawns with a TrainController and TrainModel without issues")
    void trainUnitSpawnsAModelAndController() {
        trn = new TrainUnit();
        boolean controllerExists = trn.getController() != null;
        boolean hullExists = trn.getHull() != null;

        assertEquals(true, controllerExists);
        assertEquals(true, hullExists);
    }


    /*
        Speed, Authority Tests
     */


    @Test
    @DisplayName("When not running, Speed/Authority can be manually fed to Hull and manually updated for Controller")
    void controllerGetsWrongAuthorityFromHull() {
        trn = new TrainUnit();
        TrainControl ctrl = trn.getController();
        Train hull = trn.getHull();
        double testAuthority = 1.0;
        double testSpeed = 1.0;

        // Manually set Hull's Speed Authority
        hull.setAuthority((int) testAuthority);
        hull.setCommandedSpeed(testSpeed);


        // When not Running, must manually read from hull
        ctrl.getTrainData();
        assertEquals(testAuthority,hull.getAuthority());
        assertEquals(testSpeed,hull.getCommandedSpeed());
        assertEquals(testAuthority, ctrl.getAuthority());
        assertEquals(testSpeed, ctrl.getCommandedSpeed());

        // Try new Speed/Authority
        testAuthority = 10.0;
        testSpeed = 10.0;
        hull.setAuthority((int) testAuthority);
        hull.setCommandedSpeed(testSpeed);
        // Must be manually updated when not running
        ctrl.getTrainData();
        assertEquals(testAuthority,hull.getAuthority());
        assertEquals(testSpeed,hull.getCommandedSpeed());
        assertEquals(testAuthority, ctrl.getAuthority());
        assertEquals(testSpeed, ctrl.getCommandedSpeed());

        testAuthority = 20.0;
        testSpeed = 20.0;
        hull.setAuthority((int) testAuthority);
        hull.setCommandedSpeed(testSpeed);
        // Must be manually updated when not running
        ctrl.getTrainData();
        assertEquals(testAuthority,hull.getAuthority());
        assertEquals(testSpeed,hull.getCommandedSpeed());
        assertEquals(testAuthority, ctrl.getAuthority());
        assertEquals(testSpeed, ctrl.getCommandedSpeed());
    }



    @Test
    @DisplayName("While running (whether on track or not), Controller constantly polls Speed/Authority from Hull, Hull polls Speed/Authority from Track")
    void trainControllerInteractsWithTrainModel() {
        trn = new TrainUnit(true);
        TrainControl ctrl = trn.getController();
        Train hull = trn.getHull();

        // Even when not on TrackElement, Controller is still getting values from Hull
        assertEquals(trn.getHull().getActualSpeed() , trn.getController().getActualSpeed() );

        // Set running, immobile train onto new TrackElement
        double expectedSpeed = 25.0;
        double expectedAuthority = 10.0;
        TrackBlock BlockGreenA = new TrackBlock();
        BlockGreenA.setCommandedSpeed(expectedSpeed);
        BlockGreenA.setAuthority(expectedAuthority);
        trn.placeOn(BlockGreenA);

        // Confirm Placement, Confirm isRunning
        waitForTrainObjectToCatchUp();
        assertEquals(true,trn.isRunning());
        assertSame(BlockGreenA,trn.getLocation());

        // Hull reads speed/authoirty from track
        waitForTrainObjectToCatchUp();
        assertEquals(expectedSpeed,     hull.getCommandedSpeed());
        assertEquals(expectedAuthority, hull.getAuthority());

        // Controller reads speed/authority from Hull
        assertEquals(expectedSpeed,     ctrl.getCommandedSpeed());
        assertEquals(expectedAuthority, ctrl.getAuthority());

        trn.halt();
    }


    /*
            Train placement on Tracks
     */


    @Test
    @DisplayName("Can be placed on TrackElements, accurately sets them as occupied according to contract")
    void trainUnitCanBePlacedOnATrackElement() {
        trn = new TrainUnit();
        // TODO: I made grace's default constructors public, make sure to let her know
        TrackBlock BlockGreenA = new TrackBlock();
        TrackBlock BlockGreenB = new TrackBlock();
        TrackBlock BlockGreenC = new TrackBlock();
        Switch SwitchGreenA = new Switch();
        Station DormontStation = new Station();

        // Place on block A
        trn.placeOn(BlockGreenA);
        TrackElement foundAt = trn.getLocation();
        assertSame(BlockGreenA,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());

        // Place on block B
        trn.placeOn(BlockGreenB);
        foundAt = trn.getLocation();
        assertSame(BlockGreenB,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());

        // Place on Block C
        trn.placeOn(BlockGreenC);
        foundAt = trn.getLocation();
        assertSame(BlockGreenC,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());
        assertEquals(true,BlockGreenC.getOccupied());

        // Place on Switch
        trn.placeOn(SwitchGreenA);
        foundAt = trn.getLocation();
        assertSame(SwitchGreenA,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());
        assertEquals(true,BlockGreenC.getOccupied());
        assertEquals(true,SwitchGreenA.getOccupied());

        // Place on Station
        trn.placeOn(DormontStation);
        foundAt = trn.getLocation();
        assertSame(DormontStation,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());
        assertEquals(true,BlockGreenC.getOccupied());
        assertEquals(true,SwitchGreenA.getOccupied());
        assertEquals(true,DormontStation.getOccupied());
    }



    @Test
    @DisplayName("Can transition from TrackElement to TrackElement, sets new one as occupied and last one as unoccupied")
    void trainUnitCanTransitionBetweenTrackElements() {
        trn = new TrainUnit();
        // TODO: I made grace's default constructors public, make sure to let her know
        TrackBlock BlockGreenA = new TrackBlock();
        TrackBlock BlockGreenB = new TrackBlock();
        TrackBlock BlockGreenC = new TrackBlock();
        Switch SwitchGreenA = new Switch();
        Station DormontStation = new Station();

        // Note: Constructed Trains are originally on nothing until they are placed on something
        trn.transition(BlockGreenA);
        TrackElement foundAt = trn.getLocation();
        TrackElement wasAt = trn.getLastOccupation();
        assertEquals(null,wasAt);
        assertSame(BlockGreenA,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(false,BlockGreenB.getOccupied());
        assertEquals(false,BlockGreenC.getOccupied());
        assertEquals(false,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.transition(BlockGreenB);
        foundAt = trn.getLocation();
        wasAt = trn.getLastOccupation();
        assertSame(BlockGreenA,wasAt);
        assertSame(BlockGreenB,foundAt);
        assertEquals(false,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());
        assertEquals(false,BlockGreenC.getOccupied());
        assertEquals(false,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.transition(BlockGreenC);
        foundAt = trn.getLocation();
        wasAt = trn.getLastOccupation();
        assertSame(BlockGreenB,wasAt);
        assertSame(BlockGreenC,foundAt);
        assertEquals(false,BlockGreenA.getOccupied());
        assertEquals(false,BlockGreenB.getOccupied());
        assertEquals(true,BlockGreenC.getOccupied());
        assertEquals(false,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.transition(SwitchGreenA);
        foundAt = trn.getLocation();
        wasAt = trn.getLastOccupation();
        assertSame(BlockGreenC,wasAt);
        assertSame(SwitchGreenA,foundAt);
        assertEquals(false,BlockGreenA.getOccupied());
        assertEquals(false,BlockGreenB.getOccupied());
        assertEquals(false,BlockGreenC.getOccupied());
        assertEquals(true,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.transition(DormontStation);
        foundAt = trn.getLocation();
        wasAt = trn.getLastOccupation();
        assertSame(SwitchGreenA,wasAt);
        assertSame(DormontStation,foundAt);
        assertEquals(false,BlockGreenA.getOccupied());
        assertEquals(false,BlockGreenB.getOccupied());
        assertEquals(false,BlockGreenC.getOccupied());
        assertEquals(false,SwitchGreenA.getOccupied());
        assertEquals(true,DormontStation.getOccupied());
    }



    @Test
    @DisplayName("If this test passes, then I have not solved the Chaser Problem")
    void chaserProblem() {
        // If a Front Train and Chaser Train are on blocks next to each other going the same direction,
        // then what happens when the Chaser enters the new block before the front one does?
        // IE front train is on A, A is occupied
        // chaser enters A, sets A occupied again
        // front train leaves A for B, sets A unoccupied
        // chaser won't set A to occupied again => chaser is on a track without occupying it
        trn = new TrainUnit("Front Train");
        TrainUnit chaser = new TrainUnit("Chaser Train");
        TrackElement GreenBlockA = new TrackBlock();
        TrackElement GreenBlockB = new TrackBlock();

        trn.placeOn(GreenBlockA);
        // Chaser enters A
        chaser.transition(GreenBlockA);
        // Front leaves A
        trn.transition(GreenBlockB);

        // Chaser is on A but A does not show occupied
        assertSame(GreenBlockA, chaser.getLocation());
        assertEquals(false, GreenBlockA.getOccupied());
    }


    /*
            Train Runs
     */


    @Test
    @DisplayName("Train will run on new thread, correctly pulls Speed/Authority from TrackElement on this thread to its thread")
    void trainRunsOnThread() {
        // Seconds to run for
        int runFor = 3;

        trn = new TrainUnit();
        TrackElement testBlock = new TrackBlock();

        // Start Train, not on block
        trn.start();
        // While Running
        waitForTrainObjectToCatchUp();
        assertEquals(true,trn.isRunning());
        // A train on no track reads an invalid Speed/Authority
        assertEquals(-1.0,trn.getSpeed());
        assertEquals(-1.0,trn.getAuthority());

        // Place Train onto Block
        double fakeAuthority = 10.0;
        double fakeSpeed = 10.0;
        trn.placeOn(testBlock);
        testBlock.setAuthority(fakeAuthority);
        testBlock.setCommandedSpeed(fakeSpeed);
        // it takes a few milliseconds for the train to come back around in its sampling loop
        waitForTrainObjectToCatchUp();

        // Train automatically pulls Speed/Authority
        double measuredSpeed = trn.getSpeed();
        double measuredAuthority = trn.getAuthority();
        assertSame(testBlock,trn.getLocation());
        assertEquals(fakeAuthority,measuredAuthority);
        assertEquals(fakeSpeed,measuredSpeed);

        // Updating speed and authority on the block will reflect onto the TrainUnit
        fakeSpeed = 40.0;
        fakeAuthority = 3.0;
        testBlock.setAuthority(fakeAuthority);
        testBlock.setCommandedSpeed(fakeSpeed);
        // it takes a few milliseconds for the train to come back around in its sampling loop
        waitForTrainObjectToCatchUp();
        measuredSpeed = trn.getSpeed();
        measuredAuthority = trn.getAuthority();
        assertEquals(fakeAuthority,measuredAuthority);
        assertEquals(fakeSpeed,measuredSpeed);

        // Removing TrainUnit from TrainBlock means that tracks do not register a speed or authority
        trn.placeOn(null);
        // it takes a few milliseconds for the train to come back around in its sampling loop
        waitForTrainObjectToCatchUp();
        measuredSpeed = trn.getSpeed();
        measuredAuthority = trn.getAuthority();
        assertEquals(-1.0,measuredAuthority);
        assertEquals(-1.0,measuredSpeed);

        trn.halt();
        assertEquals(false,trn.isRunning());
    }



    @Test
    @DisplayName("Train can immediately begin running after construction with special constructor")
    void startOnConstructionWorks() {
        trn = new TrainUnit(true);
        waitForTrainObjectToCatchUp();
        assertEquals(true, trn.isRunning());
        trn.halt();
        assertEquals(false, trn.isRunning());
    }



    @Test
    @DisplayName("Controller will continually poll Hull who polls TrackElement for speed and authority while TrainUnit is running on TrackElement")
    void theControllerWillUpdateFromTheHullWhileRunning() {
        trn = new TrainUnit(true);
        TrackBlock BlockGreenA = new TrackBlock();

        // Spawn GUI for each train
        //trainGUI traingui = new trainGUI(0);
        //traingui.setVisible(true);
        //traingui.giveTrain(trn.getHull());

        //DriverUI controlgui = new DriverUI();

        BlockGreenA.setAuthority(2.0);
        BlockGreenA.setCommandedSpeed(25.0);

        trn.placeOn(BlockGreenA);
        assertEquals(true,BlockGreenA.getOccupied());
        waitForTrainObjectToCatchUp();
        assertEquals(2.0, trn.getHull().getAuthority());
        assertEquals(25.0,trn.getHull().getCommandedSpeed());
        waitForTrainObjectToCatchUp();
        waitForTrainObjectToCatchUp();
        waitForTrainObjectToCatchUp();
        assertEquals(2.0, trn.getController().getAuthority());
        assertEquals(25.0,trn.getController().getCommandedSpeed());
        while(true) {}

        //trn.halt();
    }


    /*
            physics updates
     */
    @Test
    @DisplayName("TrainUnit will updatePhysics when called upon by WorldClock")
    void trainWillUpdatePhysicsWhenCalledByWorldClock() {
        WorldClock physicsCLK = new WorldClock(4.0,1.0);
        trn = new TrainUnit();
        physicsCLK.addListener(trn);

        // update 4 times a second
        physicsCLK.start();
        System.out.println("updating physics 4 times a second for 3 seconds");
        try{ TimeUnit.SECONDS.sleep(3); } catch(Exception e) {}
        // update 8 times a second
        physicsCLK.setResolution(8.0);
        System.out.println("updating physics 8 times a second for 3 seconds");
        try{ TimeUnit.SECONDS.sleep(3); } catch(Exception e) {}
        // update 10 times a second
        System.out.println("updating physics 10 times a second for 3 seconds");
        physicsCLK.setResolution(10.0);
        try{ TimeUnit.SECONDS.sleep(3); } catch(Exception e) {}
        physicsCLK.halt();
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
