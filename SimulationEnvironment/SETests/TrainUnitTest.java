import SimulationEnvironment.*;
import TrackConstruction.*;
import Track.*;

import TrainModel.Train;
import TrainModel.trainGUI;
import WorldClock.WorldClock;
import implementation.TrainControl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class TrainUnitTest {
    TrainUnit trn;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    public int[] greenPathNY= {62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58,59,60,61,62};
    public int[] redPath = {0,9,8, 7, 6, 5, 4, 3, 2, 1, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 52, 51, 50, 49, 48, 47, 46, 45, 44, 43, 67, 68, 69, 70, 71, 38, 37, 36, 35, 34, 33, 32, 72, 73, 74, 75, 76, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9};

    //routing for green line
    public int[] routeGreen(int start, int end, Track inst){
    int[] newGreenLine = greenPathNY;
    int flag=0;
    int flag2 =0;
    int beacon2 =0;

        for(int i = 0; i < 180; i++){
        if(newGreenLine[i] != start && flag == 0) {
            newGreenLine[i] = 0;
        }
        if(newGreenLine[i] == start) {
            flag = 1;
        }
        else if(flag == 1 && newGreenLine[i] == end-1) {
            if(flag2 != 1)
                beacon2 = newGreenLine[i-1];
            flag2=1;

        }
        else if(flag2 == 1) {
            newGreenLine[i] = 0;
        }
    }
        int[] RouteAr = new int[151];
        for(int i=0; i < 176; i++) {
            if (start == 0) {
                RouteAr[0] = 3;
                RouteAr[62]=1;
            }
            if(end == 0)
                RouteAr[0] =1;
            if(i == beacon2)
                RouteAr[i] = 2;

            if (newGreenLine[i] != 0)
                RouteAr[newGreenLine[i]] = 1;
        }

        return RouteAr;
    }

    public int[] routeRed(int start, int end, Track instance){
        int[] newRedLine = redPath;
        int flag=0;
        int flag2 =0;
        int counter =0;
        int beacon2=0;
        for(int i = 0; i < 106; i++){

            if(newRedLine[i] != start && flag == 0) {
                newRedLine[i] = 0;

            }
            if(newRedLine[i] == start && flag2 == 0) {
                flag = 1;

            }
            else if(flag == 1 && newRedLine[i] == end-1) {

                if(flag2 != 1)
                    beacon2 = newRedLine[i-1];

                flag2=1;

            }
            else if(flag2 == 1) {
                newRedLine[i] = 0;
            }

        }



        int[] RouteAr = new int[77];
        for(int i=0; i < 106; i++) {
            if(start == 0) {
                RouteAr[0] = 666;
                RouteAr[9] = 1;
            }
            if(end == 0){
                RouteAr[0]=666;
                RouteAr[10]=1;
                RouteAr[9] =1;
            }
            if(i == beacon2)
                RouteAr[beacon2]=2;

            if (newRedLine[i] != 0)
                RouteAr[newRedLine[i]] = 1;
        }
        return RouteAr;
    }

    public void calc(int[] route, Track inst){
        for (int i=0; i<inst.getGreenLine().size(); i++){
            if(route[i] == 1){
                inst.getGreenLine().get(i).setAuthority(1);
                inst.getGreenLine().get(i).setCommandedSpeed(15);
            }
            if(route[i] == 2){
                inst.getGreenLine().get(i).setAuthority(888);
                inst.getGreenLine().get(i).setCommandedSpeed(15);
            }
        }
    }

    public void calcR(int[] route, Track inst){
        for (int i=0; i<inst.getRedLine().size(); i++){
            if(route[i] == 1){
                inst.getRedLine().get(i).setAuthority(1);
                inst.getRedLine().get(i).setCommandedSpeed(15);
            }
            if(route[i] == 2){
                inst.getRedLine().get(i).setAuthority(888);
                inst.getRedLine().get(i).setCommandedSpeed(15);
            }
        }
    }
    public int getTotalLengthR(int[] route, Track inst){
        int total=0;
        for (int i=0; i<inst.getRedLine().size(); i++){
            if(route[i] == 1){
                total += inst.getRedLine().get(i).getLength();
            }
            if(route[i] == 2){
                total += inst.getRedLine().get(i).getLength();
            }
        }
        return total;
    }
    public int getTotalLengthG(int[] route, Track inst){
        int total=0;
        for (int i=0; i<inst.getGreenLine().size(); i++){
            if(route[i] == 1){
                total += inst.getGreenLine().get(i).getLength();
            }
            if(route[i] == 2){
                total += inst.getGreenLine().get(i).getLength();
            }
        }
        return total;
    }



    /*
    Authority and speed
     */
    @Test
    @DisplayName("Construction\t\t[TrainUnit spawns with a TrainController and TrainModel without issues]")
    void trainleavesGreenLineStation1() {
        trn = new TrainUnit(true);
        WorldClock clk = new WorldClock(10.0,4.0);
        clk.addListener(trn);
        clk.start();
        //need to get path
        Track instance = new Track();
        instance.importTrack("C:\\Users\\Devon's Laptop\\OneDrive\\Documents\\RedGreenUpdated.csv");
        int[] route = routeGreen(0,73,instance);
        instance.getGreenLine().get(0).setCommandedSpeed(15);
        instance.getGreenLine().get(0).setAuthority(1);
        calc(route,instance);
        int length = getTotalLengthG(route,instance);
        TrackGUI test = new TrackGUI(instance);
        test.setVisible(true);
        instance.dispatchLine(0);
        instance.getSwitches().get(10).setSwitchState(true);
        instance.updateSwitches();
        trainGUI train = new trainGUI(0);
        train.setVisible(true);
        train.latch(trn.getHull());
        trainGUI.setTrainTotalAuthority(length);
        trn.setReferenceTrack(instance);
//        trn.run();
        trn.spawnOn(instance.getGreenLine().get(0),instance.getGreenLine().get(0));
        int lastTime = clk.getTimeInSeconds();
        int currentTime;
        int time888 = 0;
        boolean arrive = false;
        trn.getController().setNextStation("DORMONT");
        while(true){
            train.update();
            currentTime = clk.getTimeInSeconds();
            instance.updatePhysics(clk.getTimeString(), currentTime-lastTime);
            lastTime = currentTime;
            if(trn.getHull().getAuthority()==888){
                time888 = clk.getTimeInSeconds();
                arrive = true;
            }
            if(arrive && clk.getTimeInSeconds()-time888==30){
                trn.getController().openDoorAtStation(false);
                break;
            }
        }
        trn.getController().setNextStation("CASTLE SHANNON");
        trn.getHull().setTotalDistance(0);
        //rerouting
        int length2 = 0;
        int[] route2 = routeGreen(73,96,instance);
        for (int i=73; i<96; i++){
                instance.getGreenLine().get(i).setAuthority(1);
                instance.getGreenLine().get(i).setCommandedSpeed(15);
                length2 += instance.getGreenLine().get(i).getLength();
            }
        instance.getGreenLine().get(94).setAuthority(1);
        instance.getGreenLine().get(94).setCommandedSpeed(1);

        instance.dispatchLine(73);
//        for(int i=1; i<13;i++) {
//                instance.getSwitches().get(i).setSwitchState(true);
//        }
        instance.updateSwitches();
        trn.setReferenceTrack(instance);
        lastTime = clk.getTimeInSeconds();
        time888 = 0;
        arrive = false;
        trainGUI.setTrainTotalAuthority(length2);
        while(true){
            train.update();
            currentTime = clk.getTimeInSeconds();
            instance.updatePhysics(clk.getTimeString(), currentTime-lastTime);
            lastTime = currentTime;
            if(trn.getHull().getAuthority()==888){
                time888 = clk.getTimeInSeconds();
                arrive = true;
            }
            if(arrive && clk.getTimeInSeconds()-time888==30){
                trn.getController().openDoorAtStation(false);
                break;
            }
        }




    }

    @Test
    @DisplayName("Construction\t\t[TrainUnit spawns with a TrainController and TrainModel without issues]")
    void trainleavesRedLineStation1() {
        trn = new TrainUnit(true);
        WorldClock clk = new WorldClock(10.0,2.0);
        clk.addListener(trn);
        clk.start();
        //need to get path
        Track instance = new Track();
        instance.importTrack("C:\\Users\\grhen\\OneDrive\\Documents\\RedGreenUpdated.csv");
        int[] route = routeRed(0,2,instance);
        instance.getGreenLine().get(0).setCommandedSpeed(15);
        instance.getGreenLine().get(0).setAuthority(1);
        calcR(route,instance);
        TrackGUI test = new TrackGUI(instance);
        test.setVisible(true);
        instance.dispatchLine(1);
        instance.getSwitches().get(0).setSwitchState(true);
        instance.updateSwitches();
        trainGUI train = new trainGUI(0);
        train.setVisible(true);
        trn.setReferenceTrack(instance);
//        trn.run();
        trn.spawnOn(instance.getGreenLine().get(0),instance.getGreenLine().get(0));
        while(true){}


    }

    /*
        Spawning Tests
     */

    @Test
    @DisplayName("Construction\t\t[TrainUnit spawns with a TrainController and TrainModel without issues]")
    void trainUnitSpawnsAModelAndController() {
        trn = new TrainUnit();
        trn.start();
        WorldClock clk = new WorldClock();
        clk.addListener(trn);
        clk.start();

        //trn.spawnOn(theblockyoureon,thebloockyourenotgoingto)
        boolean controllerExists    = trn.getController() != null;
        boolean hullExists          = trn.getHull() != null;

        assertEquals(true, controllerExists, "Train Controller spawned with TrainUnit");
        assertEquals(true, hullExists, "Train Model spawned with TrainUnit");
    }

    @Test
    @DisplayName("Construction\t\t[TrainUnit can spawn running without issues]")
    void trainUnitCanSpawnRunning() {
            // Use constructor of runningOnStart = true
            trn = new TrainUnit(true);

            // Is running?
            waitForTrainObjectToCatchUp();
            assertEquals(true, trn.isRunning());

            // Stop train
            trn.halt();
            assertEquals(false, trn.isRunning());
    }


    /*
        Speed, Authority Tests
     */


    @Test
    @DisplayName("Manual Data-Flow\t\t[When not running, Speed/Authority can be manually fed into Hull then manually updated for Controller]")
    void controllerGetsWrongAuthorityFromHull() {
        // This test is just to verify that the dataflow functions work correctly without the added complication of running/timed events/communication between threads

        // Create train, get TrainController and TrainHull components for observation
        trn = new TrainUnit();
        TrainControl ctrl = trn.getController();
        Train hull = trn.getHull();


        // Manually feed speed/Authority to hull
        double testAuthority = 1.0;
        double testSpeed = 1.0;
        hull.setAuthority((int) testAuthority);
        hull.setCommandedSpeed(testSpeed);


        // When not Running, must manually read from hull
        ctrl.getTrainData();
        assertEquals(testAuthority,hull.getAuthority());
        assertEquals(testSpeed,hull.getCommandedSpeed());
        assertEquals(testAuthority, ctrl.getAuthority());
        assertEquals(testSpeed, ctrl.getCommandedSpeed());

        // Try different Speed/Authority
        testAuthority = 10.0;
        testSpeed = 10.0;
        // Set hull's authority manually again
        hull.setAuthority((int) testAuthority);
        hull.setCommandedSpeed(testSpeed);
        // Must be manually updated when not running
        ctrl.getTrainData();
        assertEquals(testAuthority,hull.getAuthority());
        assertEquals(testSpeed,hull.getCommandedSpeed());
        assertEquals(testAuthority, ctrl.getAuthority());
        assertEquals(testSpeed, ctrl.getCommandedSpeed());

        // One last Speed/Authority
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
    @DisplayName("Automatic Polling Track\t\t[While running (whether on track or not), Controller constantly polls Speed/Authority from Hull, and Hull polls Speed/Authority from Track]")
    void trainControllerInteractsWithTrainModel() {
        // Create train, make it run on construction, retrieve TrainModel and TrainController
        trn = new TrainUnit(true);
        TrainControl ctrl = trn.getController();
        Train hull = trn.getHull();
        waitForTrainObjectToCatchUp();
        assertEquals(true,trn.isRunning());

        // Basic Communication Test
        assertEquals(trn.getHull().getActualSpeed() , trn.getController().getActualSpeed() );

        // Without TrackElement, Train Hull and Train Controller read invalid Speed/Authority
        // Note: For some reason, control only gets value of -1.0 whenever there is ample time before measuring
        double invalidValue = -1.0;
        waitForTrainObjectToCatchUp();
        assertEquals(invalidValue,hull.getCommandedSpeed());
        assertEquals(invalidValue,hull.getAuthority());
        assertEquals(invalidValue,ctrl.getCommandedSpeed());
        assertEquals(invalidValue,ctrl.getAuthority());

        // Make TrackElement, put Train on it
        double expectedSpeed = 25.0;
        int expectedAuthority = 10;
        TrackBlock BlockGreenA = new TrackBlock();
        BlockGreenA.setCommandedSpeed(expectedSpeed);
        BlockGreenA.applyAuthorityToBlock(expectedAuthority);
        trn.placeOn(BlockGreenA);
        // Confirm Placement, Confirm isRunning
        waitForTrainObjectToCatchUp();
        assertEquals(true,trn.isRunning());
        assertSame(BlockGreenA,trn.getLocation());

        // Hull reads speed/authority from track
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
    @DisplayName("Block Placement\t\t[Can be placed on TrackElements, accurately sets them as occupied according to contract]")
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
    @DisplayName("Block Transitions\t\t[Can transition from TrackElement to TrackElement, sets new one as occupied and last one as unoccupied]")
    void trainUnitCanTransitionBetweenTrackElements() {
        trn = new TrainUnit();
        // TODO: I made grace's default constructors public, make sure to let her know
        TrackBlock BlockGreenA = new TrackBlock();
        TrackBlock BlockGreenB = new TrackBlock();
        TrackBlock BlockGreenC = new TrackBlock();
        Switch SwitchGreenA = new Switch();
        Station DormontStation = new Station();

        // Note: Constructed Trains are originally on nothing until they are placed on something
        trn.executeBlockTransition(BlockGreenA);
        TrackElement foundAt = trn.getLocation();
        TrackElement wasAt = trn.getLastOccupation();
        assertEquals(null,wasAt);
        assertSame(BlockGreenA,foundAt);
        assertEquals(true,BlockGreenA.getOccupied());
        assertEquals(false,BlockGreenB.getOccupied());
        assertEquals(false,BlockGreenC.getOccupied());
        assertEquals(false,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.executeBlockTransition(BlockGreenB);
        foundAt = trn.getLocation();
        wasAt = trn.getLastOccupation();
        assertSame(BlockGreenA,wasAt);
        assertSame(BlockGreenB,foundAt);
        assertEquals(false,BlockGreenA.getOccupied());
        assertEquals(true,BlockGreenB.getOccupied());
        assertEquals(false,BlockGreenC.getOccupied());
        assertEquals(false,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.executeBlockTransition(BlockGreenC);
        foundAt = trn.getLocation();
        wasAt = trn.getLastOccupation();
        assertSame(BlockGreenB,wasAt);
        assertSame(BlockGreenC,foundAt);
        assertEquals(false,BlockGreenA.getOccupied());
        assertEquals(false,BlockGreenB.getOccupied());
        assertEquals(true,BlockGreenC.getOccupied());
        assertEquals(false,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.executeBlockTransition(SwitchGreenA);
        foundAt = trn.getLocation();
        wasAt = trn.getLastOccupation();
        assertSame(BlockGreenC,wasAt);
        assertSame(SwitchGreenA,foundAt);
        assertEquals(false,BlockGreenA.getOccupied());
        assertEquals(false,BlockGreenB.getOccupied());
        assertEquals(false,BlockGreenC.getOccupied());
        assertEquals(true,SwitchGreenA.getOccupied());
        assertEquals(false,DormontStation.getOccupied());

        trn.executeBlockTransition(DormontStation);
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
    @DisplayName("PROBLEM\t\t[If this test passes, then I have not solved the Chaser Problem]")
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
        chaser.executeBlockTransition(GreenBlockA);
        // Front leaves A
        trn.executeBlockTransition(GreenBlockB);

        // Chaser is on A but A does not show occupied
        assertSame(GreenBlockA, chaser.getLocation());
        assertEquals(false, GreenBlockA.getOccupied());
    }



    /*
            Train Runs
     */


    @Test
    @DisplayName("Thread Communication\t\t[Train on Thread A will correctly and continually read Speed/Authority from TrackElement on Thread B]")
    void trainRunsOnThread() {
        // Seconds to run for
        int runFor = 2;

        trn = new TrainUnit();
        TrackElement testBlock = new TrackBlock();

        // Start Train, not on block
        trn.start();
        // While Running
        waitForTrainObjectToCatchUp();
        waitForTrainObjectToCatchUp();

        assertEquals(true,trn.isRunning());
        // A train on no track reads an invalid Speed/Authority
        assertEquals(-1.0,trn.getCommandedSpeed());
        assertEquals(-1.0,trn.getCommandedAuthority());

        // Place Train onto Block
        int fakeAuthority = 10;
        double fakeSpeed = 10.0;
        trn.placeOn(testBlock);
        testBlock.applyAuthorityToBlock(fakeAuthority);
        testBlock.setCommandedSpeed(fakeSpeed);

        // it takes a few milliseconds for the train to come back around in its sampling loop
        waitForTrainObjectToCatchUp();
        // Train automatically pulls Speed/Authority
        double measuredSpeed = trn.getCommandedSpeed();
        double measuredAuthority = trn.getCommandedAuthority();
        assertSame(testBlock,trn.getLocation());
        assertEquals(fakeAuthority,measuredAuthority);
        assertEquals(fakeSpeed,measuredSpeed);

        // Updating speed and authority on the block will reflect onto the TrainUnit
        fakeSpeed = 40.0;
        fakeAuthority = 3;
        testBlock.applyAuthorityToBlock(fakeAuthority);
        testBlock.setCommandedSpeed(fakeSpeed);
        // it takes a few milliseconds for the train to come back around in its sampling loop
        waitForTrainObjectToCatchUp();
        measuredSpeed = trn.getCommandedSpeed();
        measuredAuthority = trn.getCommandedAuthority();
        assertEquals(fakeAuthority,measuredAuthority);
        assertEquals(fakeSpeed,measuredSpeed);

        // Removing TrainUnit from TrainBlock means that tracks do not register a speed or authority
        trn.placeOn(null);
        // it takes a few milliseconds for the train to come back around in its sampling loop
        waitForTrainObjectToCatchUp();
        measuredSpeed = trn.getCommandedSpeed();
        measuredAuthority = trn.getCommandedAuthority();
        assertEquals(-1.0,measuredAuthority);
        assertEquals(-1.0,measuredSpeed);

        trn.halt();
        assertEquals(false,trn.isRunning());
    }



    @Test
    @DisplayName("Automatic Polling\t\t[while TrainUnit is running on TrackElement, Controller will continually poll Hull who polls TrackElement for speed and authority]")
    void theControllerWillUpdateFromTheHullWhileRunning() {
        trn = new TrainUnit(true);
        TrackBlock BlockGreenA = new TrackBlock();

        // Spawn GUI for each train
        //trainGUI traingui = new trainGUI(0);
        //traingui.setVisible(true);
        //traingui.giveTrain(trn.getHull());

        //DriverUI controlgui = new DriverUI();

        BlockGreenA.applyAuthorityToBlock(2);
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
            physics Update Tests
     */
    @Test
    @DisplayName("Physics Updates\t\t[TrainUnit will update physics when listening to WorldClock]")
    void trainWillUpdatePhysicsWhenCalledByWorldClock() {
        WorldClock physicsCLK = new WorldClock(4.0,1.0);
        trn = new TrainUnit();
        physicsCLK.addListener(trn);
        trn.setConsoleVerboseness(Level.INFO);

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
        Movement Tests
     */


    @Test
    @DisplayName("Movement\t\t[Disconnected Train Hull, manual velocity will display correct Distance Traveled]")
    void trainHullWillMoveAtConstantVelocity() {
        // Create train and world clock for commanding train
        // Note: Train will not begin performing physics updates until it begins interacting with the world
        trn = new TrainUnit("Moving Hull",true);
        // Disconnect the controller so the hull is freeform
        trn.setControllerDisconnect(true);

        // World clock for physics calls
        WorldClock physicsClk = new WorldClock(1.0,10.0);
        physicsClk.addListener(trn);

        // Display more information about trainLogs, since physics updates are "Finer" level
        trn.defaultConsoleVerboseness = Level.ALL;

        // Get Hull
        // Manually set a velocity
        // Hand calculated values for {Actual Speed, Total Distance, Block Distance}
        Train hull = trn.getHull();
        hull.setSpeed(10.0);
        double[][] expectedValues =
        {       // First four are with speed 10.0, updated once per second
                {10.0,10.0,10.0}, {10.0,20.0,20.0}, {10.0, 30.0, 30.0}, {10.0, 40.0, 40.0},
                // Second four are with speed 20.0, updated once per second
                {20.0, 60.0, 60.0}, {20.0, 80.0, 80.0}, {20.0, 100.0, 100.0}, {20.0, 120.0, 120.0},
        };

        // Begin physics update calls
        physicsClk.start();

        // For each physics update...
        trn.updateFlag = false;
        for (int index=0; index<expectedValues.length; index++) {

            System.out.printf("ebrake:%b sbrake:%b\n",hull.getEmergencyBrake(),hull.getPassengerBrake());

            // Wait for next update to occur
            while(trn.updateFlag==false) {}
            trn.updateFlag=false;

            // Every update, hull values match expected values
            System.out.println(String.format("%f %f %f",hull.getActualSpeed(),hull.getTotalDistance(),hull.getBlockDistance()));
            assertTrue( aboutEqual(expectedValues[index][0], hull.getActualSpeed(), 1.0));
            assertTrue( aboutEqual(expectedValues[index][1], hull.getTotalDistance(), 5.0));
            assertTrue( aboutEqual(expectedValues[index][2], hull.getBlockDistance(), 5.0));

            // Speed up after fourth iteration
            if(index == 3) {hull.setSpeed(20.0);}
        }

        // end physics update calls
        physicsClk.halt();
    }



    @Test
    @DisplayName("[DEPRICATED: TrainModel calculates speed differently now] Movement\t\t[Disconnected Train Hull under constant power will display correct velocities]")
    void TrainHullWillAccelerate() {
        // Create train and world clock for commanding train
        trn = new TrainUnit("Moving Hull", true);
        trn.setControllerDisconnect(true);
        // Physics Clock for physics update calls
        WorldClock physicsClk = new WorldClock(1.0,10.0);
        physicsClk.addListener(trn);
        // Display more information about trainLogs, since physics updates are "Finer" level
        //trn.consoleVerboseness = Level.ALL;
        // Get Hull
        Train hull = trn.getHull();

        // Put hull under constant power command of 100KW
        // I pulled these values directly from output of a test run
        hull.setPower(100);
        double[] velocities = {3.17, 6.02, 7.06, 7.73, 8.32, 8.86, 9.37, 9.84, 10.29, 10.72, 11.14, 11.53, 11.92};

        // Begin physics update calls
        physicsClk.start();

        // For each physics update...
        trn.updateFlag = false;
        for (int index =0; index < velocities.length; index++) {
            // Wait for next update to occur
            while (!trn.updateFlag) {}
            trn.updateFlag = false;
            System.out.println(trn.toMovementString());
            // Velocity from power calculations matches expected velocity by +-1.0
            assertEquals(true, aboutEqual(velocities[index],hull.getActualSpeed(),1.0));
        }

        // end physics update calls
        physicsClk.halt();
    }


    @Test
    @DisplayName("Commanded Movement\t\t[TrainUnit will not move if authority is 0.0]")
    void trainWithZeroAuthority() {
        // Test Train
        trn = new TrainUnit("Train without Authority");

        // Test block with 0 authority
        TrackBlock testBlock = new TrackBlock();
        testBlock.setCommandedSpeed(10.0);
        testBlock.applyAuthorityToBlock(0);

        // Physics clock for update commands
        WorldClock physicsClk = new WorldClock(4.0,1.0);
        physicsClk.addListener(trn);

        trn.placeOn(testBlock);

        // starting train will start pulling Speed and Authority
        trn.start();
        // start physics clock will start calling physics updates
        physicsClk.start();

        // Wait 5 seconds to see if train moves
        try{TimeUnit.SECONDS.sleep(5);} catch(Exception e) {}

        // Train should not have any velocity
        //assertEquals(0.0, trn.getHull().getActualSpeed());

        trn.halt();
        physicsClk.halt();
    }



    @Test
    @DisplayName("Commanded Movement\t\t[TrainUnit will speed up to Commanded velocity if given non-zero authority]")
    void trainWithNonZeroAuthority() {
        double desiredSpeed = 10.0;
        boolean hitDesiredSpeed = false;

        // Test train
        trn = new TrainUnit("Train with Authority");
        trn.defaultConsoleVerboseness = Level.ALL;
        trn.getController().setKpKi(1.5,.001);

        /*
        trainGUI traingui = new trainGUI(0);
        traingui.setVisible(true);
        traingui.giveTrain(trn.getHull());

        DriverUI controlgui = new DriverUI(trn.getController());

         */


        // Test Block
        TrackBlock testBlock = new TrackBlock();
        testBlock.setCommandedSpeed(desiredSpeed);
        testBlock.applyAuthorityToBlock(10000);

        // Physics clock for update commands
        WorldClock physicsClk = new WorldClock(1.0,1.0);
        physicsClk.addListener(trn);

        trn.placeOn(testBlock);

        // starting train will start pulling Speed and Authority
        trn.start();
        // start physics clock will start calling physics updates
        physicsClk.start();

        // Wait until train reaches desired speed
        while(true) {
            /*
            traingui.updateDisplay();
            controlgui.updateDisplay();
            */
            try{TimeUnit.SECONDS.sleep(1);} catch (Exception e) {}
            System.out.println(trn.toMovementString());

            // if desired speed is reached
            if(trn.getActualSpeed() >= desiredSpeed) {
                System.out.printf("TrainUnit has reached desired speed of %f and is moving %f m/s\n",desiredSpeed, trn.getActualSpeed());

                System.out.println("Allowing train to maintain speed for 5sec");
                // Wait five seconds to ensure that train maintains commanded speed
                try{TimeUnit.SECONDS.sleep(10);} catch(Exception e) {}

                //assertEquals(true, aboutEqual(trn.getActualSpeed(), desiredSpeed, 1.0));
                break;
            }
        }
        trn.halt();
        physicsClk.halt();
    }

    @Test
    @DisplayName("Commanded Movement\t\t[TrainUnit moving at 10.0m/s will slow down to 5.0m/s when commanded]")
    void trainWillSlowDownToCommandedSpeed() {
        double desiredSpeed = 5.0;
        boolean hitDesiredSpeed = false;

        // Test train
        trn = new TrainUnit("Train that slows down");
        //trn.defaultConsoleVerboseness = Level.ALL;

        // Test Block
        TrackBlock testBlock = new TrackBlock();
        testBlock.setCommandedSpeed(desiredSpeed);
        testBlock.applyAuthorityToBlock(10000);
        trn.placeOn(testBlock);

        // Physics clock for update commands
        WorldClock physicsClk = new WorldClock(2.0,1.0);
        physicsClk.addListener(trn);

        // Force train up to 10.0 m/s
        trn.getHull().setSpeed(10.0);

        // starting train will start pulling Speed and Authority
        trn.start();
        // start physics clock will start calling physics updates
        physicsClk.start();

        // Wait until train reaches desired speed
        while(true) {
            try{TimeUnit.SECONDS.sleep(1);} catch (Exception e) {}
            System.out.println(trn.toMovementString());
            // if desired speed is reached
            if(trn.getActualSpeed() >= desiredSpeed) {
                System.out.printf("TrainUnit has reached desired speed of %f and is moving %f m/s\n",desiredSpeed, trn.getActualSpeed());

                System.out.println("Allowing train to maintain speed for 5sec");
                // Wait five seconds to ensure that train maintains commanded speed
                try{TimeUnit.SECONDS.sleep(5);} catch(Exception e) {}
                // Assert train is within 0.5 of commanded speed
                //assertEquals(true, aboutEqual(trn.getActualSpeed(), desiredSpeed, 1.0));
                break;
            }
        }
        trn.halt();
        physicsClk.halt();
    }


    /*
        Movement across Track Blocks
     */


    @Test
    @DisplayName("Block Movement\t\t[TrainUnit will track how far it has overexceeded a TrackBlock]")
    void trainWillContinueBeyondTrackBlock() {
        double blockLength=100.0;
        // Handle block transitions will only occur whenever train is running and physics is updating
        trn = new TrainUnit("Overtraveling TrainUnit");
        trn.blockExceededFlag = false;
        // World Physics set-up
        WorldClock physicsClk = new WorldClock(1.0,1.0);
        physicsClk.addListener(trn);
        // Block Set-up
        TrackBlock shortBlock = new TrackBlock();
        shortBlock.setLength(blockLength);
        shortBlock.applyAuthorityToBlock(1000);
        shortBlock.setCommandedSpeed(20.0);
        // Train placement
        trn.placeOn(shortBlock);
        trn.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {

        }

        // Begin moving
        physicsClk.start();

        // Wait for train to exceed current block
        System.out.printf("Please wait a few moments for train to exceed current block length (length=%f)...\n",blockLength);
        while(!trn.blockExceededFlag) {
            try{TimeUnit.SECONDS.sleep(1);} catch (Exception e) {}
            System.out.println(trn.toMovementString());
        }
        // Announce
        System.out.println("***Train has exceeded block length***");
        // Print status while exceeding
        for(int seconds=0; seconds<10; seconds++) {
            // Wait for one second
            try{TimeUnit.SECONDS.sleep(1);} catch (Exception e) {}
            // print train status
            System.out.println(trn.toMovementString());
        }

        trn.halt();
        physicsClk.halt();
        System.out.println("Test complete.");
    }

    @Test
    @DisplayName("Block Movement\t\t[TrainUnit will correctly move around a circlular, two block track]")
    void trainMovesAroundTwoBlockTrack() {
        try {
            // Import track for use
            String filepath = "SEResources/TwoBlockCircle.csv";
            Track circleTrack = new Track();
            circleTrack.importTrack(filepath);

            // Create Train
            trn = new TrainUnit("Two Circle Train");
            trn.setReferenceTrack(circleTrack);
            trn.blockExceededFlag = false;
            trn.configureForSimpleBlockLayout();

            // Create physics clock
            WorldClock physicsClk = new WorldClock();
            physicsClk.addListener(trn);

            // print info to console
            System.out.println(trn.informationString());
            System.out.println(circleTrack);

            // get Blocks of the track circuit
            TrackElement BlockA = circleTrack.getBlock(0);
            TrackElement BlockB = circleTrack.getBlock(1);
            BlockA.setLength(50);
            BlockA.applyAuthorityToBlock(1000);
            BlockA.setCommandedSpeed(10.0);
            BlockB.setLength(50);
            BlockB.applyAuthorityToBlock(1000);
            BlockB.setCommandedSpeed(10.0);

            trn.placeOn(BlockA);

            trn.start();
            physicsClk.start();

            while (!trn.blockExceededFlag) {
            }
            trn.blockExceededFlag = false;
            while (!trn.blockExceededFlag) {
            }

            trn.halt();
            physicsClk.halt();
        } catch (Exception e) {
            System.out.println("Still failing to uplaod custom tracks to Track....");
        }
    }



    @Test
    @DisplayName("Block Movement\t\t[TrainUnit will correctly move around a circlular, three block track]")
    void trainMovesAroundThreeBlockTrack() {
        try {
            // Import track for use
            String filepath = "SEResources/ThreeBlockCircle.csv";
            Track circleTrack = new Track();
            circleTrack.importTrack(filepath);

            // Create Train
            trn = new TrainUnit("Three Circle Train");
            trn.setReferenceTrack(circleTrack);
            trn.blockExceededFlag = false;
            trn.configureForSimpleBlockLayout();

            // Create TrainModel UI
            //trainGUI trainModelUI = new trainGUI(0);
            //trainModelUI.giveTrain(trn.getHull());

            // Create TrainController UI
            //DriverUI trainControllerUI = new DriverUI(trn.getController());

            // Create physics clock
            WorldClock physicsClk = new WorldClock(1.0, 10.0);
            physicsClk.addListener(trn);

            // print info to console
            System.out.println(trn.informationString());
            System.out.println(circleTrack);

            // get Blocks of the track circuit
            TrackElement BlockA = circleTrack.getBlock(0);
            TrackElement BlockB = circleTrack.getBlock(1);
            TrackElement BlockC = circleTrack.getBlock(2);
            BlockA.setLength(50);
            BlockA.applyAuthorityToBlock(1000);
            BlockA.setCommandedSpeed(10.0);
            BlockB.setLength(50);
            BlockB.applyAuthorityToBlock(1000);
            BlockB.setCommandedSpeed(10.0);
            BlockC.setLength(120);
            BlockC.applyAuthorityToBlock(1000);
            BlockC.setCommandedSpeed(10.0);

            trn.placeOn(BlockA);

            trn.start();
            physicsClk.start();

            //while(!trn.blockExceededFlag) {}
            //while(true) {
            //try{TimeUnit.MILLISECONDS.sleep(100);} catch (Exception e) {}
            //trainControllerUI.updateDisplay();
            //trainModelUI.updateDisplay();
            //}
            // Wait until next block transition
            assertEquals(BlockA, trn.getLocation());
            while (!trn.blockExceededFlag) {
            }
            trn.blockExceededFlag = false;
            waitForTrainObjectToCatchUp();
            assertEquals(BlockC, trn.getLocation());
            while (!trn.blockExceededFlag) {
            }
            waitForTrainObjectToCatchUp();
            assertEquals(BlockB, trn.getLocation());

            System.out.println(trn.informationString());

            trn.halt();
            physicsClk.halt();
        } catch (Exception e) {
            System.out.println("Still failing to uplaod custom tracks to Track....");
        }
    }


    @Test
    @DisplayName("Block Movement\t\t[TrainUnit will correctly move around green line]")
    void trainMovesAroundGreenLine() {
//        // Import track for use
//        String filepath = "SEResources/GreenAndRedLine.csv";
//        Track NorthShoreExtension = new Track();
//        NorthShoreExtension.importTrack(filepath);
//
//        // Create Train
//        trn = new TrainUnit("Greenline Train");
//        trn.setReferenceTrack(NorthShoreExtension);
//        trn.blockExceededFlag = false;
//        trn.setConsoleVerboseness(Level.INFO);
//
//        ArrayList<TrackElement> greenLine = NorthShoreExtension.getGreenLine();
//        TrackElement spawnBlock = greenLine.get(11);
//        TrackElement orientBlock = greenLine.get(12);
//        trn.spawnOn(spawnBlock,orientBlock);
//
//        for(TrackElement block : greenLine) {
//            block.setAuthority(1000);
//            block.setCommandedSpeed(10.0);
//        }
//
//        // Setting switches from Grace's test file
//        /*
//        NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(7),1); // 12 is connecting 1A to 13
//        NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(8),0); //switch is 29 -30
//        NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(11),0); // switch from 77 to 76
//        NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(12),0); // switch from 85 to 86
//        NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(9),1); // switch NOT to the yard
//        NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(10),0); // switch NOT to the yard
//        */
//        /*for(int i = 0 ; i < 200 ; i++ ) {
//            if(i == 90) {
//                //Here we need to TOGGLE SWITCH
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(11),1); // setting 76 to 150
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(8),1); // setting 76 to 150
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(12),1); // switch from 100 - 85 ????? (should be 1 but 0 )
//            }
//            if(i == 150) {
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(11), 1); // setting 150 to be connected to F
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(7), 0); // 12 to  13
//            }
//        }*/
//
//        // Create TrainModel UI
//        //trainGUI trainModelUI = new trainGUI(0);
//        //trainModelUI.giveTrain(trn.getHull());
//
//        // Create TrainController UI
//        //DriverUI trainControllerUI = new DriverUI();
//
//        // Create physics clock
//        WorldClock physicsClk = new WorldClock(1.0,10.0);
//        physicsClk.addListener(trn);
//
//        // print info to console
//        System.out.println(trn.informationString());
//        System.out.println(NorthShoreExtension);
//
//        // get Blocks of the track circuit
//        /*TrackElement BlockA = circleTrack.getBlock(0);
//        TrackElement BlockB = circleTrack.getBlock(1);
//        TrackElement BlockC = circleTrack.getBlock(2);
//        BlockA.setLength(50);
//        BlockA.setAuthority(1000);
//        BlockA.setCommandedSpeed(10.0);
//        BlockB.setLength(50);
//        BlockB.setAuthority(1000);
//        BlockB.setCommandedSpeed(10.0);
//        BlockC.setLength(120);
//        BlockC.setAuthority(1000);
//        BlockC.setCommandedSpeed(10.0);*/
//
//        trn.start();
//        physicsClk.start();
//
//        while(true) {
//            /*
//            if(trn.getLocation().getBlockNum() == 90) {
//                //Here we need to TOGGLE SWITCH
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(11),1); // setting 76 to 150
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(8),1); // setting 76 to 150
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(12),1); // switch from 100 - 85 ????? (should be 1 but 0 )
//            }
//            if(trn.getLocation().getBlockNum() == 150) {
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(11), 1); // setting 150 to be connected to F
//                NorthShoreExtension.setSwitch(NorthShoreExtension.getSwitches().get(7), 0); // 12 to  13
//            }
//
//             */
//            if(trn.blockExceededFlag)
//                System.out.println(trn.getLocation());
//            trn.blockExceededFlag = false;
//
//            //trainModelUI.updateDisplay();
//            //trainControllerUI.updateDisplay();
//        }
//
//        //trn.halt();
//        //physicsClk.halt();

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

    public boolean aboutEqual(double a, double b, double eps) {
        return Math.abs(a-b)<eps;
    }
}
