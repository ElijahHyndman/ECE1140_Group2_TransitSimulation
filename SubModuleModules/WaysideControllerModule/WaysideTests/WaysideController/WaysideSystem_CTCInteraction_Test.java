package WaysideController;

import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaysideSystem_CTCInteraction_Test {

    /*
            Tests using single controller
     */

    // Blocks
    ArrayList<TrackElement> section = new ArrayList<>();
    int nBlocks = 5;

    // Controllers
    WaysideSystem sys = new WaysideSystem("TestingSection");
    WaysideController ctrl1;

    WaysideSystem_CTCInteraction_Test() throws IOException {
    }

    @BeforeEach
    public void setup() {
        for (int i =0; i<nBlocks; i++) {
            TrackBlock newblock = new TrackBlock();
            newblock.setBlockNum(i+1);
            newblock.setAuthority(0);
            newblock.setCommandedSpeed(10.0);
            newblock.setSpeedLimit(25);
            section.add(newblock);
        }
        ctrl1 = new WaysideController(section,"TestCtrl1");
        sys.addController(ctrl1);
    }

    @Test
    @DisplayName("Authority is settable using system")
    void authorityRetrievable() throws Exception {
        int[] authority = {1,2,3,4,5};
        sys.setAuthorities(authority);
        for (TrackElement block : section) {
            System.out.printf("Track index %d authority set (%d)\n",block.getBlockNum(), block.getAuthority());
        }
    }
}