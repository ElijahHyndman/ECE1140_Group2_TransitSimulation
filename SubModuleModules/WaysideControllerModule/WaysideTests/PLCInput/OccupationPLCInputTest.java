package PLCInput;

import TrackConstruction.TrackBlock;
import WaysideController.PLCEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import PLCInput.OccupationPLCInput;

/**
 * @author elijah
 */
class OccupationPLCInputTest {
    // Engine for us with testing
    PLCEngine engine;
    // Simple PLC script
    ArrayList<String> genericPLCScript = new ArrayList<>() {
        {
            add("LD occ1");
            add("LD occ2");
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
        target1.setAuthority(1);
        target1.setCommandedSpeed(20);
        target1.setSpeedLimit(10);
        target1.setBlockNum(1);
        target2.setAuthority(1);
        target2.setCommandedSpeed(20);
        target2.setSpeedLimit(10);
        target2.setBlockNum(2);
    }

    @Test
    @DisplayName("Default settings work")
    public void test() throws Exception {
        OccupationPLCInput occ1 = new OccupationPLCInput("occ1",target1);
        OccupationPLCInput occ2 = new OccupationPLCInput("occ2",target2);
        engine.registerInputSource(occ1);
        engine.registerInputSource(occ2);

        // False False
        assertEquals(false, engine.evaluateLogic());

        target1.setOccupied(true);

        // True False
        assertEquals(false, engine.evaluateLogic());

        target2.setOccupied(true);

        // True True
        assertEquals(true,engine.evaluateLogic());

        target1.setOccupied(false);

        //  Fale true
        assertEquals(false, engine.evaluateLogic());
    }

    @Test
    @DisplayName("Setting a different rule works")
    public void test2() throws Exception {
        OccupationPLCInput occ1 = new OccupationPLCInput("occ1",target1);
        OccupationPLCInput occ2 = new OccupationPLCInput("occ2",target2);
        engine.registerInputSource(occ1);
        engine.registerInputSource(occ2);

        // False False
        assertEquals(false, engine.evaluateLogic());
        target1.setOccupied(true);
        // True False
        assertEquals(false, engine.evaluateLogic());
        target2.setOccupied(true);
        // True True
        assertEquals(true,engine.evaluateLogic());
        target1.setOccupied(false);
        //  False true
        assertEquals(false, engine.evaluateLogic());

        /*
            Change the rule for occ1
         */
        occ1.setNewRule(OccupationPLCInput.OccRule.FalseWhenOccupied);
        target1.setOccupied(false);
        target2.setOccupied(false);

        // !(False) False
        assertEquals(false,engine.evaluateLogic());
        target2.setOccupied(true);
        // !(False) True
        assertEquals(true,engine.evaluateLogic());
        target1.setOccupied(true);
        // !(True) True
        assertEquals(false,engine.evaluateLogic());
        target2.setOccupied(false);
        // !(True) False
        assertEquals(false, engine.evaluateLogic());

        /*
            Change the rule for occ2
         */
        occ2.setNewRule(OccupationPLCInput.OccRule.FalseWhenOccupied);
        target1.setOccupied(false);
        target2.setOccupied(false);

        // !(False) !(False)
        assertEquals(true,engine.evaluateLogic());
        target2.setOccupied(true);
        // !(False) !(True)
        assertEquals(false,engine.evaluateLogic());
        target1.setOccupied(true);
        // !(True) !(True)
        assertEquals(false,engine.evaluateLogic());
        target2.setOccupied(false);
        // !(True) !(False)
        assertEquals(false, engine.evaluateLogic());

    }

    @Test
    @DisplayName("Establishing rule in constructor works")
    public void test3 () throws Exception {
        OccupationPLCInput occ1 = new OccupationPLCInput("occ1",target1, OccupationPLCInput.OccRule.TrueWhenOccupied);
        OccupationPLCInput occ2 = new OccupationPLCInput("occ2",target2, OccupationPLCInput.OccRule.FalseWhenOccupied);
        engine.registerInputSource(occ1);
        engine.registerInputSource(occ2);

        target1.setOccupied(false);
        target2.setOccupied(false);

        // False !(False)
        assertEquals(false,engine.evaluateLogic());
        target2.setOccupied(true);
        // False !(True)
        assertEquals(false,engine.evaluateLogic());
        target1.setOccupied(true);
        // True !(True)
        assertEquals(false,engine.evaluateLogic());
        target2.setOccupied(false);
        // True !(False)
        assertEquals(true, engine.evaluateLogic());
    }
}