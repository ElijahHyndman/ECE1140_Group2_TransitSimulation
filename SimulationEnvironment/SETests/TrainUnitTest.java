import SimulationEnvironment.TrainUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainUnitTest {
    TrainUnit trn;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void trainUnitSpawnsAModelAndController() {
        trn = new TrainUnit();
        boolean controllerExists = trn.getControl() != null;
        boolean hullExists = trn.getHull() != null;

        assertEquals(true, controllerExists);
        assertEquals(true, hullExists);
    }

    @Test
    void trainUnitCanBePlacedOnATrackElement() {

    }

}