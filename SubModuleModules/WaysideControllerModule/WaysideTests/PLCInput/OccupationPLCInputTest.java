package PLCInput;

import TrackConstruction.TrackBlock;
import WaysideController.PLCEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import PLCInput.OccupationPLCInput;

class OccupationPLCInputTest {
    // Engine for us with testing
    PLCEngine engine;
    // Simple PLC script
    ArrayList<String> genericPLCScript = new ArrayList<>() {
        {
            add("LD occ1");
            add("LD occ1");
            add("AND");
            add("SET");

        }
    };

    TrackBlock target1;
    TrackBlock target2;

    @BeforeEach
    public void setup() throws Exception {
        // Upload generic PLC to engine
        engine = new PLCEngine();
        engine.uploadPLC(genericPLCScript);
        // TrackElements whose occupations we will be referring to as input
        target1 = new TrackBlock();
        target2 = new TrackBlock();
        target1.setOccupied(false);
        target2.setOccupied(false);
    }

    @Test
    @DisplayName("Default constructors work and occupations are used by PLC engine for evaluating")
    public void test() throws Exception {
        OccupationPLCInput occ1 = new OccupationPLCInput("occ1",target1);
        OccupationPLCInput occ2 = new OccupationPLCInput("occ2",target2);
        engine.registerInputSource(occ1);
        engine.registerInputSource(occ2);

        assertEquals(false, engine.evaluateLogic());
    }
}