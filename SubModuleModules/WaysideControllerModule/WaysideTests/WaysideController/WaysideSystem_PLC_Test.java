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
        sys.registerNewTrack(greenLine);
    }
}