import SimulationEnvironment.*;
import TrackConstruction.*;
import implementation.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        boolean controllerExists = trn.getControl() != null;
        boolean hullExists = trn.getHull() != null;

        assertEquals(true, controllerExists);
        assertEquals(true, hullExists);
    }

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
}