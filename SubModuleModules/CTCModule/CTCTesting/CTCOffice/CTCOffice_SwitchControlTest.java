package CTCOffice;

import PLCInput.OccupationPLCInput;
import PLCInput.*;
import SimulationEnvironment.SimulationEnvironment;
import Track.Track;
import TrackConstruction.Switch;
import TrackConstruction.TrackElement;
import WaysideController.PLCEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/** tests that CTC autouploads switch PLCs to Green adn Red Line
 *
 */
class CTCOffice_SwitchControlTest {
    Track tracksys = new Track();
    ArrayList<PLCInput> greenPool = new ArrayList<PLCInput>();
    ArrayList<PLCInput> redPool = new ArrayList<PLCInput>();


    @BeforeEach
    void setup() {
        tracksys.importTrack("Resources/RedGreenUpdated.csv");
        ArrayList<TrackElement> line = tracksys.getGreenLine();
        for (TrackElement block : line) {
            greenPool.add(new OccupationPLCInput(String.format("OCC%d",block.getBlockNum()),block));
            greenPool.add(new HasAuthorityPLCInput(String.format("HASAUTH%d",block.getBlockNum()),block));
        }
        line = tracksys.getRedLine();
        for (TrackElement block : line) {
            redPool.add(new OccupationPLCInput(String.format("OCC%d",block.getBlockNum()),block));
            redPool.add(new HasAuthorityPLCInput(String.format("HASAUTH%d",block.getBlockNum()),block));
        }
    }

    public TrackElement getGreen(int index) {
        return tracksys.getGreenLine().get(index);
    }
    public TrackElement getRed(int index) {
        return tracksys.getRedLine().get(index);
    }

    TrackElement dep1;
    TrackElement dep2;

    @Test
    @DisplayName("Switch 12 PLC works correctly")
    void Switch12 () throws Exception {
        Switch sw = (Switch)getGreen(12);
        dep1 = getGreen(13);
        PLCEngine engine = CTCOffice.switchGreen12PLC(sw);
        engine.setInputSources(greenPool);

        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
    }

    @Test
    @DisplayName("Switch 29 PLC works correctly")
    void Switch29 () throws Exception {
        Switch sw = (Switch)getGreen(29);
        dep1 = getGreen(150);
        PLCEngine engine = CTCOffice.switchGreen29PLC(sw);
        engine.setInputSources(greenPool);

        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
    }

    @Test
    @DisplayName("Switch 58 PLC works correctly")
    void Switch58 () throws Exception {
        Switch sw = (Switch)getGreen(58);
        dep1 = getGreen(59);
        PLCEngine engine = CTCOffice.switchGreen58PLC(sw);
        engine.setInputSources(greenPool);

        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getIndex());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getIndex());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getIndex());
    }

    @Test
    @DisplayName("Switch 62 PLC works correctly")
    void Switch62 () throws Exception {
        Switch sw = (Switch)getGreen(62);
        dep1 = getGreen(61);
        PLCEngine engine = CTCOffice.switchGreen62PLC(sw);
        engine.setInputSources(greenPool);

        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getIndex());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getIndex());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getIndex());
    }

    @Test
    @DisplayName("Switch 76 PLC works correctly")
    void Switch76 () throws Exception {
        Switch sw = (Switch)getGreen(76);
        dep1 = getGreen(77);
        PLCEngine engine = CTCOffice.switchGreen76PLC(sw);
        engine.setInputSources(greenPool);

        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
    }

    @Test
    @DisplayName("Switch 86 PLC works correctly")
    void Switch86 () throws Exception {
        Switch sw = (Switch)getGreen(86);
        dep1 = getGreen(100);
        PLCEngine engine = CTCOffice.switchGreen86PLC(sw);
        engine.setInputSources(greenPool);

        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("%b -> Switch state: %s\n",dep1.getOccupied(),sw.getSwitchState());
    }



    /*
            Red tests
     */

    @Test
    @DisplayName("Switch 9 PLC works correctly")
    void Switch9 () throws Exception {
        Switch sw = (Switch)getRed(9);
        dep1 = getRed(10);
        PLCEngine engine = CTCOffice.switchRed9PLC(sw);
        engine.setInputSources(redPool);

        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());


        System.out.println();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setOccupied(true);
        dep1.setAuthority(1);
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
    }


    @Test
    @DisplayName("Switch 15 PLC works correctly")
    void Switch15 () throws Exception {
        Switch sw = (Switch)getRed(15);
        dep1 = getRed(1);
        PLCEngine engine = CTCOffice.switchRed15PLC(sw);
        engine.setInputSources(redPool);

        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());


        System.out.println();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setOccupied(true);
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
    }


    @Test
    @DisplayName("Switch 27 PLC works correctly")
    void Switch27 () throws Exception {
        Switch sw = (Switch)getRed(27);
        dep1 = getRed(28);
        dep2 = getRed(26);
        PLCEngine engine = CTCOffice.switchRed27PLC(sw);
        engine.setInputSources(redPool);

        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        System.out.println("(Note: switches are default before logic is applied)");
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());


        System.out.println();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep2.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(1);
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
    }



    @Test
    @DisplayName("Switch 32 PLC works correctly")
    void Switch32 () throws Exception {
        Switch sw = (Switch)getRed(32);
        dep1 = getRed(33);
        dep2 = getRed(31);
        PLCEngine engine = CTCOffice.switchRed32PLC(sw);
        engine.setInputSources(redPool);

        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        System.out.println("(Note: switches are default before logic is applied)");
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());


        System.out.println();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep2.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(1);
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
    }


    @Test
    @DisplayName("Switch 38 PLC works correctly")
    void Switch38 () throws Exception {
        Switch sw = (Switch)getRed(38);
        dep1 = getRed(39);
        dep2 = getRed(37);
        PLCEngine engine = CTCOffice.switchRed38PLC(sw);
        engine.setInputSources(redPool);

        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        System.out.println("(Note: switches are default before logic is applied)");
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());


        System.out.println();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep2.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(1);
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
    }


    @Test
    @DisplayName("Switch 43 PLC works correctly")
    void Switch43 () throws Exception {
        Switch sw = (Switch)getRed(43);
        dep1 = getRed(44);
        dep2 = getRed(42);
        PLCEngine engine = CTCOffice.switchRed43PLC(sw);
        engine.setInputSources(redPool);

        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        System.out.println("(Note: switches are default before logic is applied)");
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());


        System.out.println();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep2.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(1);
        dep2.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("auth%d auth%d -> Switch state: %s\n",dep1.getAuthority(),dep2.getAuthority(),sw.getSwitchState());
    }


    @Test
    @DisplayName("Switch 52 PLC works correctly")
    void Switch52 () throws Exception {
        Switch sw = (Switch)getRed(52);
        dep1 = getRed(66);
        PLCEngine engine = CTCOffice.switchRed52PLC(sw);
        engine.setInputSources(redPool);

        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
        dep1.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());


        System.out.println();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setAuthority(0);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());

        dep1.setOccupied(true);
        dep1.setAuthority(1);
        engine.evaluateLogic();
        System.out.printf("occ%b auth%d -> Switch state: %s\n",dep1.getOccupied(),dep1.getAuthority(),sw.getSwitchState());
    }


    /*
            Overall test
     */
    @Test
    @DisplayName("CTC spawns correct plcs when instantiated")
    void SE() throws Exception {
        SimulationEnvironment SE = new SimulationEnvironment();
        SE.setTrack(tracksys);
    }
}
