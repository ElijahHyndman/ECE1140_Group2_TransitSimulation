import SimulationEnvironment.*;
import TrackConstruction.*;

import TrainModel.Train;
import WorldClock.WorldClock;
import implementation.TrainControl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Spawns with a TrainModel and TrainController without issues")
    void trainUnitSpawnsAModelAndController() {
        trn = new TrainUnit();
        boolean controllerExists = trn.getController() != null;
        boolean hullExists = trn.getHull() != null;

        assertEquals(true, controllerExists);
        assertEquals(true, hullExists);
    }



    @Test
    @DisplayName("Controller can interact with Train Hull")
    void trainControllerInteractsWithTrainModel() {
        trn = new TrainUnit(true);
        TrainControl ctrl = trn.getController();
        Train hull = trn.getHull();

        // Confirm that control is allowed to pull speed from train hull
        ctrl.getTrainData();
        assertEquals(trn.getHull().getActualSpeed() , trn.getController().getActualSpeed() );

        // Set running, immobile train onto a fake track
        double expectedSpeed = 25.0;
        double expectedAuthority = 10.0;
        TrackBlock BlockGreenA = new TrackBlock();
        BlockGreenA.setCommandedSpeed(expectedSpeed);
        BlockGreenA.setAuthority(expectedAuthority);
        trn.placeOn(BlockGreenA);
        waitForTrainObjectToCatchUp();
        assertEquals(true,trn.isRunning());
        assertSame(BlockGreenA,trn.getLocation());

        // Have to manually call for Controller to retrieve speed from TrainModel
        trn.getController().getTrainData();

        // Confirm that hull has read speed,authority from track
        waitForTrainObjectToCatchUp();
        assertEquals(expectedSpeed,     hull.getCommandedSpeed());
        assertEquals(expectedAuthority, hull.getAuthority());

        // confirm controller has read speed,authority from hull
        //trn.getController().setAuthority(10);
        assertEquals(expectedSpeed,     ctrl.getCommandedSpeed());
        assertEquals(expectedAuthority, ctrl.getAuthority());

        trn.halt();
    }

    @Test
    @DisplayName("Controller measures authority as 50* amount somehow")
    void controllerGetsWrongAuthorityFromHull() {
        trn = new TrainUnit();
        TrainControl ctrl = trn.getController();
        Train hull = trn.getHull();
        double testAuthority = 1.0;
        double testSpeed = 1.0;

        hull.setAuthority((int) testAuthority);
        hull.setCommandedSpeed(testSpeed);
        // Must be manually updated when not running
        ctrl.getTrainData();
        assertEquals(testAuthority,hull.getAuthority());
        assertEquals(testSpeed,hull.getCommandedSpeed());
        // This is the problematic line
        assertEquals(testAuthority, ctrl.getAuthority());
        assertEquals(testSpeed, ctrl.getCommandedSpeed());

        testAuthority = 10.0;
        testSpeed = 10.0;
        hull.setAuthority((int) testAuthority);
        hull.setCommandedSpeed(testSpeed);
        // Must be manually updated when not running
        ctrl.getTrainData();
        assertEquals(testAuthority,hull.getAuthority());
        assertEquals(testSpeed,hull.getCommandedSpeed());
        // This is the problematic line
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
        // This is the problematic line
        assertEquals(testAuthority, ctrl.getAuthority());
        assertEquals(testSpeed, ctrl.getCommandedSpeed());
    }



    /*
            Train placement on Tracks
     */


    @Test
    @DisplayName("Can be placed on multiple TrackElements, accurately announces which block it is on")
    void trainUnitCanBePlacedOnATrackElement() {
        trn = new TrainUnit();
        // TODO: I made grace's default constructors public, make sure to let her know
        TrackBlock BlockGreenA = new TrackBlock();
        TrackBlock BlockGreenB = new TrackBlock();
        TrackBlock BlockGreenC = new TrackBlock();
        Switch SwitchGreenA = new Switch();
        Station DormontStation = new Station();


        trn.placeOn(BlockGreenA);
        TrackElement foundAt = trn.getLocation();
        assertSame(BlockGreenA,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());

        trn.placeOn(BlockGreenB);
        foundAt = trn.getLocation();
        assertSame(BlockGreenB,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());

        trn.placeOn(BlockGreenC);
        foundAt = trn.getLocation();
        assertSame(BlockGreenC,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());
        assertEquals(true,BlockGreenC.getOccupied());

        trn.placeOn(SwitchGreenA);
        foundAt = trn.getLocation();
        assertSame(SwitchGreenA,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());
        assertEquals(true,BlockGreenC.getOccupied());
        assertEquals(true,SwitchGreenA.getOccupied());

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
    @DisplayName("Can transition from TrackElement to TrackElement, occupation is correctly tracked")
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
    @DisplayName("Train will run on a thread and correctly read Speed and Authority from TrackElement on this thread")
    void trainRunsOnThread() {
        // Seconds to run for
        int runFor = 3;

        trn = new TrainUnit();
        TrackElement testBlock = new TrackBlock();

        // A train on no block will show authority,speed of -1.0
        trn.start();

        // While Running
        waitForTrainObjectToCatchUp();
        assertEquals(true,trn.isRunning());

        // A train on no track reads an invalid Speed/Authority
        assertEquals(-1.0,trn.getSpeed());
        assertEquals(-1.0,trn.getAuthority());

        // A train will accurately get speed and authority
        double fakeAuthority = 10.0;
        double fakeSpeed = 10.0;
        trn.placeOn(testBlock);
        testBlock.setAuthority(fakeAuthority);
        testBlock.setCommandedSpeed(fakeSpeed);
        // it takes a few milliseconds for the train to come back around in its sampling loop
        waitForTrainObjectToCatchUp();
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
    @DisplayName("Controller will continually pull values from Hull while TrainUnit is running")
    void theControllerWillUpdateFromTheHullWhileRunning() {
        trn = new TrainUnit(true);
        TrackBlock BlockGreenA = new TrackBlock();
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

        trn.halt();
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
