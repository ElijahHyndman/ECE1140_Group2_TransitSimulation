package PLCOutput;

import PLCInput.PLCInput;
import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import WaysideController.PLCEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SwitchPLCOutputTest {


    /*
        Variables for all tests
     */


    // Engine for use with testing
    PLCEngine engine;
    // Simple PLC script for engine
    ArrayList<String> genericPLCScript = new ArrayList<>() {
        {
            add("LD A");
            add("LD B");
            add("AND");
            add("SET");

        }
    };
    // Simple inputs for PLC
    PLCInput a;
    PLCInput b;
    Switch sw;
    // It seems like the Switch's INDEX member is layout out like this:
    boolean def = false;
    boolean sec = true;

    @BeforeEach
    public void setup() throws Exception {
        // prepare PLC engine
        engine = new PLCEngine();
        engine.uploadPLC(genericPLCScript);
        a = new PLCInput("A");
        b = new PLCInput("B");
        engine.registerInputSource(a);
        engine.registerInputSource(b);
        // TrackElements whose occupations we will be referring to as input
        sw = new Switch("Green", 'A', 5, 100.0, -3.0, 55, "SWITCH (5-1; 5-2)",-3,0.5, new int[]{0,0,0},"n");
    }


    /*
            Tests
     */


    @Test
    @DisplayName("Switch status correctly determined based on inputs")
    public void test () throws Exception {
        SwitchPLCOutput outputProc = new SwitchPLCOutput(sw);
        engine.registerTarget(outputProc);

        // Testing default name generation
        assertTrue("Switch 5 PLC".equals(outputProc.variableName()));

        /*
            Default when true Tests
         */
        a.set(false);
        b.set(false);
        engine.evaluateLogic();
        assertEquals(sec, sw.getIndex());

        a.set(true);
        b.set(false);
        engine.evaluateLogic();
        assertEquals(sec, sw.getIndex());

        a.set(false);
        b.set(true);
        engine.evaluateLogic();
        assertEquals(sec, sw.getIndex());

        a.set(true);
        b.set(true);
        engine.evaluateLogic();
        assertEquals(def, sw.getIndex());

        /*
            Secondary when true tests
         */
        outputProc.setNewRule(SwitchPLCOutput.SwitchRule.SecondaryWhenTrue);

        a.set(false);
        b.set(false);
        engine.evaluateLogic();
        assertEquals(def, sw.getIndex());

        a.set(true);
        b.set(false);
        engine.evaluateLogic();
        assertEquals(def, sw.getIndex());

        a.set(false);
        b.set(true);
        engine.evaluateLogic();
        assertEquals(def, sw.getIndex());

        a.set(true);
        b.set(true);
        engine.evaluateLogic();
        assertEquals(sec, sw.getIndex());
    }
}