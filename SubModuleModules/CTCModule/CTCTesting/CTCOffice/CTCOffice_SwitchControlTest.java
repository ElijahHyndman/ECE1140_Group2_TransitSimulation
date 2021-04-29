package CTCOffice;

import PLCInput.OccupationPLCInput;
import Track.Track;
import TrackConstruction.Switch;
import WaysideController.PLCEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/** tests that CTC autouploads switch PLCs to Green adn Red Line
 *
 */
class CTCOffice_SwitchControlTest {
    Track tracksys = new Track();


    @BeforeEach
    void setup() {
        tracksys.importTrack("Resources/RedGreenUpdated.csv");
    }

    @Test
    @DisplayName("Switch 12 PLC works correctly")
    void Switch12 () throws Exception {
//        Switch switch12 = (Switch) tracksys.getGreenLine().get(12);
//        PLCEngine swCtrl = CTCOffice.switchGreen12PLC(switch12);
//        OccupationPLCInput block3 = new OccupationPLCInput("OCC3",tracksys.getGreenLine().get(12));
//        swCtrl.registerInputSource(block3);
    }
}