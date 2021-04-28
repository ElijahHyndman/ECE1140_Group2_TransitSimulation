package RemoteBlock;

import Track.Track;
import TrackConstruction.Switch;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RemoteBlockServiceTest {
        Track tracksys = new Track();
        TrackElement targetBlock;
        RemoteBlockService wrapper;

        @BeforeEach
        public void setup() {
            tracksys.importTrack("RemoteWaysideResources/RedGreenUpdated.csv");
            // Targeting block 15
            targetBlock = tracksys.getGreenLine().get(15);
            targetBlock.setAuthority(10);
            targetBlock.setCommandedSpeed(33.0);
            wrapper = new RemoteBlockService(targetBlock);
        }

        @Test
        @DisplayName("Basic interaction across service and block")
        void controlTest() throws RemoteException {
            assertEquals(targetBlock.getBlockNum(), wrapper.getBlockNum());
            assertEquals(targetBlock.getDirection(0),wrapper.getDirection(0));
            assertEquals(targetBlock.getDirection(1),wrapper.getDirection(1));
            assertEquals(targetBlock.getDirection(2),wrapper.getDirection(2));

            assertEquals(targetBlock.getSpeedLimit(), wrapper.getSpeedLimit());
            targetBlock.setSpeedLimit(80);
            assertEquals(targetBlock.getSpeedLimit(), wrapper.getSpeedLimit());

            assertEquals(targetBlock.getOccupied(),wrapper.getOccupied());
            targetBlock.setOccupied(true);
            assertEquals(targetBlock.getOccupied(),wrapper.getOccupied());

            wrapper.setCommandedSpeed(2.0);
            assertEquals(targetBlock.getCommandedSpeed(),wrapper.getCommandedSpeed());
            targetBlock.setCommandedSpeed(33.0);
            assertEquals(targetBlock.getCommandedSpeed(),wrapper.getCommandedSpeed());

            wrapper.setAuthority(0);
            assertEquals(targetBlock.getAuthority(),wrapper.getAuthority());
            targetBlock.setAuthority(10);
            assertEquals(targetBlock.getAuthority(),wrapper.getAuthority());
        }

        @Test
        @DisplayName("Switch tests")
        void switchTest() throws RemoteException {
            Switch sw = (Switch) tracksys.getGreenLine().get(12);
            wrapper = new RemoteBlockService(sw);

            assertEquals(sw.getBlockNum(), wrapper.getBlockNum());
            assertEquals(sw.getDirection(0),wrapper.getDirection(0));
            assertEquals(sw.getDirection(1),wrapper.getDirection(1));
            assertEquals(sw.getDirection(2),wrapper.getDirection(2));

            assertEquals(sw.getSpeedLimit(), wrapper.getSpeedLimit());
            sw.setSpeedLimit(80);
            assertEquals(sw.getSpeedLimit(), wrapper.getSpeedLimit());

            assertEquals(sw.getOccupied(),wrapper.getOccupied());
            sw.setOccupied(true);
            assertEquals(sw.getOccupied(),wrapper.getOccupied());

            wrapper.setCommandedSpeed(2.0);
            assertEquals(sw.getCommandedSpeed(),wrapper.getCommandedSpeed());
            sw.setCommandedSpeed(33.0);
            assertEquals(sw.getCommandedSpeed(),wrapper.getCommandedSpeed());

            wrapper.setAuthority(0);
            assertEquals(sw.getAuthority(),wrapper.getAuthority());
            sw.setAuthority(10);
            assertEquals(sw.getAuthority(),wrapper.getAuthority());


        }
}