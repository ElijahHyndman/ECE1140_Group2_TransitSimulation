package WaysideController;

import TrackConstruction.Switch;
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
            newblock.applyAuthorityToBlock(0);
            newblock.setCommandedSpeed(10.0);
            newblock.setSpeedLimit( (int) speedLimit);
            newblock.setOccupied(true);
            newblock.setFailureStatus(0);
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
    @DisplayName("CTC can set commanded speeds for track system using WaysideSystem")
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


    @Test
    @DisplayName("CTC can set Authorities for track system using WaysideSystem")
    void canSetAuthorities() throws Exception {
        int[] newAuth = new int[5];
        TrackElement block1 = section.get(0);
        TrackElement block2 = section.get(1);
        TrackElement block3 = section.get(2);
        TrackElement block4 = section.get(3);
        TrackElement block5 = section.get(4);
        assertEquals(0, block1.getAuthority());
        assertEquals(0, block1.getAuthority());
        assertEquals(0, block1.getAuthority());
        assertEquals(0, block1.getAuthority());
        assertEquals(0, block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",1,block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",2,block2.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",3,block3.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",4,block4.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",5,block5.getAuthority());



        System.out.println("\n===========");
        newAuth[0] = 1;
        newAuth[1] = 2;
        newAuth[2] = 3;
        newAuth[3] = 4;
        newAuth[4] = 5;
        sys.setAuthorities(newAuth);
        assertEquals(newAuth[0], block1.getAuthority());
        assertEquals(newAuth[1], block2.getAuthority());
        assertEquals(newAuth[2], block3.getAuthority());
        assertEquals(newAuth[3], block4.getAuthority());
        assertEquals(newAuth[4], block5.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",1,block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",2,block2.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",3,block3.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",4,block4.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",5,block5.getAuthority());

        System.out.println("\n===========");
        newAuth[0] = 6;
        newAuth[1] = 7;
        newAuth[2] = 8;
        newAuth[3] = 9;
        newAuth[4] = 10;
        sys.setAuthorities(newAuth);
        assertEquals(newAuth[0], block1.getAuthority());
        assertEquals(newAuth[1], block2.getAuthority());
        assertEquals(newAuth[2], block3.getAuthority());
        assertEquals(newAuth[3], block4.getAuthority());
        assertEquals(newAuth[4], block5.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",1,block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",2,block2.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",3,block3.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",4,block4.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",5,block5.getAuthority());

        System.out.println("\n===========testing large numbers");
        newAuth[0] = 888;
        newAuth[1] = 888;
        newAuth[2] = 888;
        newAuth[3] = 888;
        newAuth[4] = 888;
        sys.setAuthorities(newAuth);
        assertEquals(newAuth[0], block1.getAuthority());
        assertEquals(newAuth[1], block2.getAuthority());
        assertEquals(newAuth[2], block3.getAuthority());
        assertEquals(newAuth[3], block4.getAuthority());
        assertEquals(newAuth[4], block5.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",1,block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",2,block2.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",3,block3.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",4,block4.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",5,block5.getAuthority());

        System.out.println("\n===========testing negatives");
        newAuth[0] = -1;
        newAuth[1] = -2;
        newAuth[2] = -3;
        newAuth[3] = -4;
        newAuth[4] = -5;
        sys.setAuthorities(newAuth);
        assertEquals(newAuth[0], block1.getAuthority());
        assertEquals(newAuth[1], block2.getAuthority());
        assertEquals(newAuth[2], block3.getAuthority());
        assertEquals(newAuth[3], block4.getAuthority());
        assertEquals(newAuth[4], block5.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",1,block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",2,block2.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",3,block3.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",4,block4.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",5,block5.getAuthority());
    }

    @Test
    @DisplayName("CTC can use broadcast to controllers")
    void broadcast() throws Exception {
        double[] newSpeeds = new double[5];
        int[] newAuth = new int[5];
        TrackElement block1 = section.get(0);
        TrackElement block2 = section.get(1);
        TrackElement block3 = section.get(2);
        TrackElement block4 = section.get(3);
        TrackElement block5 = section.get(4);


        newAuth[0] = 1;
        newAuth[1] = 2;
        newAuth[2] = 3;
        newAuth[3] = 4;
        newAuth[4] = 5;
        newSpeeds[0] = 1.0;
        newSpeeds[1] = 2.0;
        newSpeeds[2] = 3.0;
        newSpeeds[3] = 4.0;
        newSpeeds[4] = 5.0;
        sys.broadcastToControllers(newSpeeds,newAuth);
        assertEquals(newAuth[0], block1.getAuthority());
        assertEquals(newAuth[1], block2.getAuthority());
        assertEquals(newAuth[2], block3.getAuthority());
        assertEquals(newAuth[3], block4.getAuthority());
        assertEquals(newAuth[4], block5.getAuthority());
        assertEquals(newSpeeds[0],block1.getCommandedSpeed());
        assertEquals(newSpeeds[1],block2.getCommandedSpeed());
        assertEquals(newSpeeds[2],block3.getCommandedSpeed());
        assertEquals(newSpeeds[3],block4.getCommandedSpeed());
        assertEquals(newSpeeds[4],block5.getCommandedSpeed());
        System.out.printf("Block %d , Auth=%d\n",1,block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",2,block2.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",3,block3.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",4,block4.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",5,block5.getAuthority());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());



        System.out.println("\n===========");
        newAuth[0] = 10;
        newAuth[1] = 10;
        newAuth[2] = 10;
        newAuth[3] = 10;
        newAuth[4] = 888;
        newSpeeds[0] = 20.0;
        newSpeeds[1] = 20.0;
        newSpeeds[2] = 10.0;
        newSpeeds[3] = 5.0;
        newSpeeds[4] = 20.0;
        sys.broadcastToControllers(newSpeeds,newAuth);
        assertEquals(newAuth[0], block1.getAuthority());
        assertEquals(newAuth[1], block2.getAuthority());
        assertEquals(newAuth[2], block3.getAuthority());
        assertEquals(newAuth[3], block4.getAuthority());
        assertEquals(newAuth[4], block5.getAuthority());
        assertEquals(newSpeeds[0],block1.getCommandedSpeed());
        assertEquals(newSpeeds[1],block2.getCommandedSpeed());
        assertEquals(newSpeeds[2],block3.getCommandedSpeed());
        assertEquals(newSpeeds[3],block4.getCommandedSpeed());
        assertEquals(newSpeeds[4],block5.getCommandedSpeed());
        System.out.printf("Block %d , Auth=%d\n",1,block1.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",2,block2.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",3,block3.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",4,block4.getAuthority());
        System.out.printf("Block %d , Auth=%d\n",5,block5.getAuthority());
        System.out.printf("Block %d , Speed=%f\n",1,block1.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",2,block2.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",3,block3.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",4,block4.getCommandedSpeed());
        System.out.printf("Block %d , Speed=%f\n",5,block5.getCommandedSpeed());
    }


    @Test
    @DisplayName("CTC can set blocks as closed and as open")
    void setCloseAndOpen() throws Exception {
        // Show that they are open
        System.out.printf("Block %d failure status: %s\n",1,section.get(0).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",2,section.get(1).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",3,section.get(2).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",4,section.get(3).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",5,section.get(4).getFailureStatus());
        // Test that getIsClosed identifies correct value
        assertEquals(section.get(0).getFailureStatus().equals("CLOSED"),sys.getIsClosed(1));
        assertEquals(section.get(1).getFailureStatus().equals("CLOSED"),sys.getIsClosed(2));
        assertEquals(section.get(2).getFailureStatus().equals("CLOSED"),sys.getIsClosed(3));
        assertEquals(section.get(3).getFailureStatus().equals("CLOSED"),sys.getIsClosed(4));
        assertEquals(section.get(4).getFailureStatus().equals("CLOSED"),sys.getIsClosed(5));

        sys.setClose(1);
        sys.setClose(2);
        sys.setClose(3);
        sys.setClose(4);
        sys.setClose(5);
        System.out.printf("Block %d failure status: %s\n",1,section.get(0).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",2,section.get(1).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",3,section.get(2).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",4,section.get(3).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",5,section.get(4).getFailureStatus());
        assertEquals(section.get(0).getFailureStatus().equals("CLOSED"),sys.getIsClosed(1));
        assertEquals(section.get(1).getFailureStatus().equals("CLOSED"),sys.getIsClosed(2));
        assertEquals(section.get(2).getFailureStatus().equals("CLOSED"),sys.getIsClosed(3));
        assertEquals(section.get(3).getFailureStatus().equals("CLOSED"),sys.getIsClosed(4));
        assertEquals(section.get(4).getFailureStatus().equals("CLOSED"),sys.getIsClosed(5));

        sys.setOpen(1);
        sys.setOpen(2);
        sys.setOpen(3);
        sys.setOpen(4);
        sys.setOpen(5);
        System.out.printf("Block %d failure status: %s\n",1,section.get(0).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",2,section.get(1).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",3,section.get(2).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",4,section.get(3).getFailureStatus());
        System.out.printf("Block %d failure status: %s\n",5,section.get(4).getFailureStatus());
        assertEquals(section.get(0).getFailureStatus().equals("CLOSED"),sys.getIsClosed(1));
        assertEquals(section.get(1).getFailureStatus().equals("CLOSED"),sys.getIsClosed(2));
        assertEquals(section.get(2).getFailureStatus().equals("CLOSED"),sys.getIsClosed(3));
        assertEquals(section.get(3).getFailureStatus().equals("CLOSED"),sys.getIsClosed(4));
        assertEquals(section.get(4).getFailureStatus().equals("CLOSED"),sys.getIsClosed(5));
    }


    @Test
    @DisplayName("")
    void canGetAndSetSwitchStatus () throws Exception {
        section.add(new Switch("Green", 'A', 6, 100.0, -3.0, 55, "SWITCH (0-1; 0-2)",-3,0.5, new int[]{0,0,0},"n"));
        sys.registerNewTrack(section);


        System.out.printf("Switch orientation is Secondary: %b\n",sys.getSwitchStatus(6));
        sys.setSwitchStatus(6,true);
        System.out.printf("Switch orientation is Secondary: %b\n",sys.getSwitchStatus(6));
        sys.setSwitchStatus(6,false);
        System.out.printf("Switch orientation is Secondary: %b\n",sys.getSwitchStatus(6));
        sys.setSwitchStatus(6,true);
        System.out.printf("Switch orientation is Secondary: %b\n",sys.getSwitchStatus(6));

    }
}