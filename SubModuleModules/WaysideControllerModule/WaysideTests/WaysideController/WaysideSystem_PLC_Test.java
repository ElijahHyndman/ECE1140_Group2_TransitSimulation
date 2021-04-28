package WaysideController;

import PLCInput.*;
import PLCOutput.AuthorityPLCOutput;
import PLCOutput.PLCOutput;
import Track.Track;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaysideSystem_PLC_Test {
    Track trackSystem;
    ArrayList<TrackElement> greenLine;
    WaysideSystem sys = new WaysideSystem();

    WaysideSystem_PLC_Test() throws IOException {
    }

    @BeforeEach
    public void setup() {
        trackSystem = new Track();
        trackSystem.importTrack("Resources/RedGreenUpdated.csv");
        greenLine = trackSystem.getGreenLine();
    }

    @Test
    @DisplayName("Inputs generate without issue")
    void inputsGenerate() {
        ArrayList<PLCInput> inputs = sys.generateInputPool(greenLine);
        for (PLCInput input : inputs) {
            System.out.println(input);
        }
    }


    // Directly uses generateInputPool instead of registering track
    @Test
    @DisplayName("Inputs are useable in script (direct use generateInputPool)")
    void inputsUsable() throws Exception {
        // Get blocks
        TrackElement block1 = greenLine.get(1);
        TrackElement block2 = greenLine.get(2);
        TrackElement block3 = greenLine.get(3);
        System.out.printf("Block 1: %s\n",block1);
        System.out.printf("Block 2: %s\n",block2);
        System.out.printf("Block 3: %s\n",block3);

        // Sets block 3 authority to 0 when block 1 or 2 are occupied
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD OCC1");
                add("LD OCC2");
                add("OR");
                add("SET");
            }
        };
        ArrayList<PLCInput> inputs = sys.generateInputPool(greenLine);
//        for (PLCInput input : inputs) {
//            //System.out.println(input);
//            System.out.printf("%s\n",input.variableName());
//        }
        // Note from mess up: I was changing variable name earlier without messing with objec type. remember that variable name must be a form of object type
        int indexOcc1 = inputs.indexOf(new PLCInput("OCC1"));
        int indexOcc2 = inputs.indexOf(new PLCInput("OCC2"));
        assertNotEquals(-1, indexOcc1);
        assertNotEquals(-1, indexOcc2);

        // PLC Engine
        PLCOutput targetOutput = new AuthorityPLCOutput(block3, AuthorityPLCOutput.AuthOutRule.HaltWhenTrue);
        PLCEngine script = new PLCEngine(PLCScript, targetOutput);
        script.setInputSources(inputs);
        // assert that inputs are found and registered
        assertDoesNotThrow(() -> script.evaluateLogic());

        /*
            Assessing logic correctness
         */
        // false false -> allowed
        block1.setOccupied(false);
        block2.setOccupied(false);
        block3.applyAuthorityToBlock(3);
        script.evaluateLogic();
        assertEquals(3, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");

        // false true -> halt
        block1.setOccupied(false);
        block2.setOccupied(true);
        block3.applyAuthorityToBlock(3);
        script.evaluateLogic();
        assertEquals(0, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");

        // true false -> halt
        block1.setOccupied(true);
        block2.setOccupied(false);
        block3.applyAuthorityToBlock(3);
        script.evaluateLogic();
        assertEquals(0, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");

        // true true -> halt
        block1.setOccupied(true);
        block2.setOccupied(true);
        block3.applyAuthorityToBlock(3);
        script.evaluateLogic();
        assertEquals(0, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");


        /*
                After effects
         */


        System.out.println("=============Testing halt releasing: ");
        int appliedAuth = 3;
        // true true -> halt

        System.out.printf("Applying authority once: %d\n",appliedAuth);
        block3.applyAuthorityToBlock(appliedAuth);
        System.out.printf("Before halting: occ=%d (%s)\n",block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");
        System.out.println("Halting");
        block1.setOccupied(true);
        block2.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");
        System.out.println("Unhalting");
        block1.setOccupied(false);
        block2.setOccupied(false);
        script.evaluateLogic();
        assertEquals(appliedAuth, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");
        System.out.println("Halting");
        block1.setOccupied(true);
        block2.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");


        appliedAuth = 10;
        System.out.printf("\n=========Applying new authority once: %d [while block is halted]\n",appliedAuth);
        block3.applyAuthorityToBlock(10);
        System.out.printf("For a short instant before evaluating halt logic again: occ=%d (%s)\n",block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");
        script.evaluateLogic();
        assertEquals(0, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");
        System.out.println("Unhalting");
        block1.setOccupied(false);
        block2.setOccupied(false);
        script.evaluateLogic();
        assertEquals(appliedAuth, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");
        System.out.println("Halting");
        block1.setOccupied(true);
        block2.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");
        System.out.println("Unhalting");
        block1.setOccupied(false);
        block2.setOccupied(false);
        script.evaluateLogic();
        assertEquals(appliedAuth, block3.getAuthority());
        System.out.printf("%b %b -> occ=%d (%s)\n",block1.getOccupied(),block2.getOccupied(),block3.getAuthority(), block3.getAuthority() > 0 ? "ALLOW" : "HALT");

    }


    @Test
    @DisplayName("Inputs are usable in script (through controllers)")
    void inputsUsable2() throws Exception {
        TrackElement block;
        TrackElement before;
        TrackElement after;

        // One directional cases
        System.out.println("Single directional tests\n");
        block = greenLine.get(9);
        after = greenLine.get(8);
        before = greenLine.get(0);
        System.out.printf("Block %s is adjacent to blocks (%s,%s)\n",block.getBlockNum(), before.getBlockNum(), after.getBlockNum());
        PLCEngine script = WaysideController.generateCollisionAvoidanceScript(block);
        script.registerInputSource(new OccupationPLCInput("OCC8",after));
        script.registerInputSource(new OccupationPLCInput("OCC9",block));
        System.out.printf(script.getPLCString());
        block.setOccupied(true);
        block.setAuthority(10);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After occupancy (%b) mean auth=%d\n",after.getOccupied(),block.getAuthority());
        after.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0,block.getAuthority());
        System.out.printf("After occupancy (%b) mean auth=%d\n",after.getOccupied(),block.getAuthority());
        after.setOccupied(false);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After occupancy (%b) mean auth=%d\n",after.getOccupied(),block.getAuthority());



        System.out.println("==================");
        block = greenLine.get(3);
        after = greenLine.get(2);
        before = greenLine.get(0);
        System.out.printf("Block %s is adjacent to blocks (%s,%s)\n",block.getBlockNum(), before.getBlockNum(), after.getBlockNum());
        script = WaysideController.generateCollisionAvoidanceScript(block);
        script.registerInputSource(new OccupationPLCInput("OCC2",after));
        script.registerInputSource(new OccupationPLCInput("OCC3",block));
        System.out.printf(script.getPLCString());
        block.setOccupied(true);
        block.setAuthority(10);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After occupancy (%b) mean auth=%d\n",after.getOccupied(),block.getAuthority());
        after.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0,block.getAuthority());
        System.out.printf("After occupancy (%b) mean auth=%d\n",after.getOccupied(),block.getAuthority());
        after.setOccupied(false);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After occupancy (%b) mean auth=%d\n",after.getOccupied(),block.getAuthority());


        // Two directional cases
        System.out.println("\nTwo Directional Tests\n");
        System.out.println("==================");
        block = greenLine.get(13);
        after = greenLine.get(14);
        before = greenLine.get(12);
        System.out.printf("Block %s is adjacent to blocks (%s,%s)\n",block.getBlockNum(), before.getBlockNum(), after.getBlockNum());
        script = WaysideController.generateCollisionAvoidanceScript(block);
        script.registerInputSource(new OccupationPLCInput("OCC12",after));
        script.registerInputSource(new OccupationPLCInput("OCC13",block));
        script.registerInputSource(new OccupationPLCInput("OCC14",before));
        System.out.printf(script.getPLCString());

        block.setOccupied(true);
        before.setOccupied(false);
        after.setOccupied(false);
        block.setAuthority(10);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        after.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0,block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        after.setOccupied(false);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        before.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        before.setOccupied(false);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        before.setOccupied(true);
        after.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());



        System.out.println("==================");
        block = greenLine.get(76);
        after = greenLine.get(77);
        before = greenLine.get(101);
        System.out.printf("Block %s is adjacent to blocks (%s,%s)\n",block.getBlockNum(), before.getBlockNum(), after.getBlockNum());
        script = WaysideController.generateCollisionAvoidanceScript(block);
        script.registerInputSource(new OccupationPLCInput("OCC77",after));
        script.registerInputSource(new OccupationPLCInput("OCC76",block));
        script.registerInputSource(new OccupationPLCInput("OCC101",before));
        System.out.printf(script.getPLCString());

        block.setOccupied(true);
        before.setOccupied(false);
        after.setOccupied(false);
        block.setAuthority(10);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        after.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0,block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        after.setOccupied(false);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        before.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        before.setOccupied(false);
        script.evaluateLogic();
        assertEquals(10, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
        before.setOccupied(true);
        after.setOccupied(true);
        script.evaluateLogic();
        assertEquals(0, block.getAuthority());
        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());


        throw new Exception("DO THE SWITCH CASES");
        // Switch cases
//        System.out.println("\nSwitch Directional Tests\n");
//        System.out.println("==================");
//        block = greenLine.get(12);
//        after = greenLine.get(8);
//        TrackElement op1 = greenLine.get(101);
//        TrackElement op2 = greenLine.get(101);
//        System.out.printf("Block %s is adjacent to blocks (%s,%s,%s)\n",block.getBlockNum(), after.getBlockNum(),op1.getBlockNum(),op2.getBlockNum());
//        script = WaysideController.generateCollisionAvoidanceScript(block);
//        script.registerInputSource(new OccupationPLCInput("OCC77",after));
//        script.registerInputSource(new OccupationPLCInput("OCC76",block));
//        script.registerInputSource(new OccupationPLCInput("OCC101",before));
//        System.out.printf(script.getPLCString());
//
//        block.setOccupied(true);
//        before.setOccupied(false);
//        after.setOccupied(false);
//        block.setAuthority(10);
//        script.evaluateLogic();
//        assertEquals(10, block.getAuthority());
//        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
//        after.setOccupied(true);
//        script.evaluateLogic();
//        assertEquals(0,block.getAuthority());
//        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
//        after.setOccupied(false);
//        script.evaluateLogic();
//        assertEquals(10, block.getAuthority());
//        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
//        before.setOccupied(true);
//        script.evaluateLogic();
//        assertEquals(0, block.getAuthority());
//        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
//        before.setOccupied(false);
//        script.evaluateLogic();
//        assertEquals(10, block.getAuthority());
//        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
//        before.setOccupied(true);
//        after.setOccupied(true);
//        script.evaluateLogic();
//        assertEquals(0, block.getAuthority());
//        System.out.printf("After,Before occupancy (%b,%b) mean auth=%d\n",after.getOccupied(),before.getOccupied(),block.getAuthority());
    }
}