package WaysideController;

import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaysideSystem_CTCInteraction_Test {

    /*
            Tests using single controller
     */

    // Blocks
    ArrayList<TrackElement> section = new ArrayList<>();
    int nBlocks = 5;
    double speedLimit = 25.0;

    // Controllers
    WaysideSystem sys;
    WaysideController ctrl1;

    WaysideSystem_CTCInteraction_Test() throws IOException {
    }

    @BeforeEach
    public void setup() throws IOException, URISyntaxException {
        for (int i =0; i<nBlocks; i++) {
            TrackBlock newblock = new TrackBlock();
            newblock.setBlockNum(i+1);
            newblock.setAuthority(0);
            newblock.setCommandedSpeed(10.0);
            newblock.setSpeedLimit( (int) speedLimit);
            newblock.setOccupied(true);
            section.add(newblock);
        }
        sys = new WaysideSystem(section,"TestingSection");
    }

    @Test
    @DisplayName("CTC can get occupation")
    void occupation() throws Exception {
        assertEquals(true,sys.getOccupancy(1));
        assertEquals(true,sys.getOccupancy(2));
        assertEquals(true,sys.getOccupancy(3));
        assertEquals(true,sys.getOccupancy(4));
        assertEquals(true,sys.getOccupancy(5));
        System.out.printf("Block %d occ: %b\n",1,sys.getOccupancy(1));
        System.out.printf("Block %d occ: %b\n",2,sys.getOccupancy(2));
        System.out.printf("Block %d occ: %b\n",3,sys.getOccupancy(3));
        System.out.printf("Block %d occ: %b\n",4,sys.getOccupancy(4));
        System.out.printf("Block %d occ: %b\n",5,sys.getOccupancy(5));

        section.get(0).setOccupied(false);


        System.out.println("===");
        assertEquals(false,sys.getOccupancy(1));
        assertEquals(true,sys.getOccupancy(2));
        assertEquals(true,sys.getOccupancy(3));
        assertEquals(true,sys.getOccupancy(4));
        assertEquals(true,sys.getOccupancy(5));
        System.out.printf("Block %d occ: %b\n",1,sys.getOccupancy(1));
        System.out.printf("Block %d occ: %b\n",2,sys.getOccupancy(2));
        System.out.printf("Block %d occ: %b\n",3,sys.getOccupancy(3));
        System.out.printf("Block %d occ: %b\n",4,sys.getOccupancy(4));
        System.out.printf("Block %d occ: %b\n",5,sys.getOccupancy(5));

        section.get(1).setOccupied(false);


        System.out.println("===");
        assertEquals(false,sys.getOccupancy(1));
        assertEquals(false,sys.getOccupancy(2));
        assertEquals(true,sys.getOccupancy(3));
        assertEquals(true,sys.getOccupancy(4));
        assertEquals(true,sys.getOccupancy(5));
        System.out.printf("Block %d occ: %b\n",1,sys.getOccupancy(1));
        System.out.printf("Block %d occ: %b\n",2,sys.getOccupancy(2));
        System.out.printf("Block %d occ: %b\n",3,sys.getOccupancy(3));
        System.out.printf("Block %d occ: %b\n",4,sys.getOccupancy(4));
        System.out.printf("Block %d occ: %b\n",5,sys.getOccupancy(5));

        section.get(2).setOccupied(false);


        System.out.println("===");
        assertEquals(false,sys.getOccupancy(1));
        assertEquals(false,sys.getOccupancy(2));
        assertEquals(false,sys.getOccupancy(3));
        assertEquals(true,sys.getOccupancy(4));
        assertEquals(true,sys.getOccupancy(5));
        System.out.printf("Block %d occ: %b\n",1,sys.getOccupancy(1));
        System.out.printf("Block %d occ: %b\n",2,sys.getOccupancy(2));
        System.out.printf("Block %d occ: %b\n",3,sys.getOccupancy(3));
        System.out.printf("Block %d occ: %b\n",4,sys.getOccupancy(4));
        System.out.printf("Block %d occ: %b\n",5,sys.getOccupancy(5));

        section.get(3).setOccupied(false);


        System.out.println("===");
        assertEquals(false,sys.getOccupancy(1));
        assertEquals(false,sys.getOccupancy(2));
        assertEquals(false,sys.getOccupancy(3));
        assertEquals(false,sys.getOccupancy(4));
        assertEquals(true,sys.getOccupancy(5));
        System.out.printf("Block %d occ: %b\n",1,sys.getOccupancy(1));
        System.out.printf("Block %d occ: %b\n",2,sys.getOccupancy(2));
        System.out.printf("Block %d occ: %b\n",3,sys.getOccupancy(3));
        System.out.printf("Block %d occ: %b\n",4,sys.getOccupancy(4));
        System.out.printf("Block %d occ: %b\n",5,sys.getOccupancy(5));

        section.get(4).setOccupied(false);


        System.out.println("===");
        assertEquals(false,sys.getOccupancy(1));
        assertEquals(false,sys.getOccupancy(2));
        assertEquals(false,sys.getOccupancy(3));
        assertEquals(false,sys.getOccupancy(4));
        assertEquals(false,sys.getOccupancy(5));
        System.out.printf("Block %d occ: %b\n",1,sys.getOccupancy(1));
        System.out.printf("Block %d occ: %b\n",2,sys.getOccupancy(2));
        System.out.printf("Block %d occ: %b\n",3,sys.getOccupancy(3));
        System.out.printf("Block %d occ: %b\n",4,sys.getOccupancy(4));
        System.out.printf("Block %d occ: %b\n",5,sys.getOccupancy(5));
    }



    @Test
    @DisplayName("")
    void canSetSpeeds() {
        double[] newSpeeds = new double[5];
        TrackElement block1 = section.get(0);
        TrackElement block2 = section.get(1);
        TrackElement block3 = section.get(2);
        TrackElement block4 = section.get(3);
        TrackElement block5 = section.get(4);
        assertEquals(10.0,block1.getCommandedSpeed());
        assertEquals(10.0,block2.getCommandedSpeed());
        assertEquals(10.0,block3.getCommandedSpeed());
        assertEquals(10.0,block4.getCommandedSpeed());
        assertEquals(10.0,block5.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());

        System.out.println("\n===========");
        newSpeeds[0] = 1.0;
        newSpeeds[1] = 2.0;
        newSpeeds[2] = 3.0;
        newSpeeds[3] = 4.0;
        newSpeeds[4] = 5.0;
        assertDoesNotThrow(() -> sys.setCommandedSpeeds(newSpeeds));
        assertEquals(newSpeeds[0],block1.getCommandedSpeed());
        assertEquals(newSpeeds[1],block2.getCommandedSpeed());
        assertEquals(newSpeeds[2],block3.getCommandedSpeed());
        assertEquals(newSpeeds[3],block4.getCommandedSpeed());
        assertEquals(newSpeeds[4],block5.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());

        System.out.println("\n===========");
        newSpeeds[0] = 6.0;
        newSpeeds[1] = 7.0;
        newSpeeds[2] = 8.0;
        newSpeeds[3] = 9.0;
        newSpeeds[4] = 10.0;
        assertDoesNotThrow(() -> sys.setCommandedSpeeds(newSpeeds));
        assertEquals(newSpeeds[0],block1.getCommandedSpeed());
        assertEquals(newSpeeds[1],block2.getCommandedSpeed());
        assertEquals(newSpeeds[2],block3.getCommandedSpeed());
        assertEquals(newSpeeds[3],block4.getCommandedSpeed());
        assertEquals(newSpeeds[4],block5.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());


        System.out.println("\n===========Testing speed limit filterig");
        newSpeeds[0] = 10000.0;
        newSpeeds[1] = 10000.0;
        newSpeeds[2] = 10000.0;
        newSpeeds[3] = 10000.0;
        newSpeeds[4] = 10000.0;
        assertDoesNotThrow(() -> sys.setCommandedSpeeds(newSpeeds));
        assertEquals(speedLimit,block1.getCommandedSpeed());
        assertEquals(speedLimit,block2.getCommandedSpeed());
        assertEquals(speedLimit,block3.getCommandedSpeed());
        assertEquals(speedLimit,block4.getCommandedSpeed());
        assertEquals(speedLimit,block5.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());

        System.out.println("\n===========Testing zeroing");
        newSpeeds[0] = 0.0;
        newSpeeds[1] = 0.0;
        newSpeeds[2] = 0.0;
        newSpeeds[3] = 0.0;
        newSpeeds[4] = 0.0;
        assertDoesNotThrow(() -> sys.setCommandedSpeeds(newSpeeds));
        assertEquals(newSpeeds[0],block1.getCommandedSpeed());
        assertEquals(newSpeeds[1],block2.getCommandedSpeed());
        assertEquals(newSpeeds[2],block3.getCommandedSpeed());
        assertEquals(newSpeeds[3],block4.getCommandedSpeed());
        assertEquals(newSpeeds[4],block5.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());

        System.out.println("\n===========Testing negative speeds");
        newSpeeds[0] = -1.0;
        newSpeeds[1] = -2.0;
        newSpeeds[2] = -3.0;
        newSpeeds[3] = -4.0;
        newSpeeds[4] = -5.0;
        assertDoesNotThrow(() -> sys.setCommandedSpeeds(newSpeeds));
        assertEquals(newSpeeds[0],block1.getCommandedSpeed());
        assertEquals(newSpeeds[1],block2.getCommandedSpeed());
        assertEquals(newSpeeds[2],block3.getCommandedSpeed());
        assertEquals(newSpeeds[3],block4.getCommandedSpeed());
        assertEquals(newSpeeds[4],block5.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());

        System.out.println("\n===========Testing negative speed limit");
        newSpeeds[0] = -1000.0;
        newSpeeds[1] = -2000.0;
        newSpeeds[2] = -3000.0;
        newSpeeds[3] = -4000.0;
        newSpeeds[4] = -5000.0;
        assertDoesNotThrow(() -> sys.setCommandedSpeeds(newSpeeds));
        assertEquals(-speedLimit,block1.getCommandedSpeed());
        assertEquals(-speedLimit,block2.getCommandedSpeed());
        assertEquals(-speedLimit,block3.getCommandedSpeed());
        assertEquals(-speedLimit,block4.getCommandedSpeed());
        assertEquals(-speedLimit,block5.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());
    }
}