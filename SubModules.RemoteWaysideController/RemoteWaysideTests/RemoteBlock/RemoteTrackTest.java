package RemoteBlock;

import PLCInput.*;
import PLCOutput.*;
import Track.Track;
import TrackConstruction.TrackElement;
import WaysideController.PLCEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RemoteTrackTest {
    Track tracksys = new Track();
    TrackElement target;

    @BeforeEach
    void setup() {
        tracksys.importTrack("RemoteWaysideResources/RedGreenUpdated.csv");
    }

    @Test
    @DisplayName("Remote block functionality")
    void remoteBlock() throws Exception {
        // block #15
        target = tracksys.getGreenLine().get(15);
        RemoteBlockStub stub = RemoteTrack.getRemoteConnection(target);


        assertEquals(target.getBlockNum(), stub.getBlockNum());
        assertEquals(target.getDirection(0),stub.getDirection(0));
        assertEquals(target.getDirection(1),stub.getDirection(1));
        assertEquals(target.getDirection(2),stub.getDirection(2));

        assertEquals(target.getSpeedLimit(), stub.getSpeedLimit());
        target.setSpeedLimit(80);
        assertEquals(target.getSpeedLimit(), stub.getSpeedLimit());

        assertEquals(target.getOccupied(),stub.getOccupied());
        target.setOccupied(true);
        assertEquals(target.getOccupied(),stub.getOccupied());

        stub.setCommandedSpeed(2.0);
        assertEquals(target.getCommandedSpeed(),stub.getCommandedSpeed());
        target.setCommandedSpeed(33.0);
        assertEquals(target.getCommandedSpeed(),stub.getCommandedSpeed());

        stub.setAuthority(0);
        assertEquals(target.getAuthority(),stub.getAuthority());
        target.setAuthority(10);
        assertEquals(target.getAuthority(),stub.getAuthority());
    }



    @Test
    @DisplayName("Remote switch functionality")
    void remoteSwitch() throws Exception {
        // switch index #12
        target = tracksys.getGreenLine().get(12);
        RemoteBlockStub stub = RemoteTrack.getRemoteConnection(target);


        assertEquals(target.getBlockNum(), stub.getBlockNum());
        assertEquals(target.getDirection(0),stub.getDirection(0));
        assertEquals(target.getDirection(1),stub.getDirection(1));
        assertEquals(target.getDirection(2),stub.getDirection(2));

        assertEquals(target.getSpeedLimit(), stub.getSpeedLimit());
        target.setSpeedLimit(80);
        assertEquals(target.getSpeedLimit(), stub.getSpeedLimit());

        assertEquals(target.getOccupied(),stub.getOccupied());
        target.setOccupied(true);
        assertEquals(target.getOccupied(),stub.getOccupied());

        stub.setCommandedSpeed(2.0);
        assertEquals(target.getCommandedSpeed(),stub.getCommandedSpeed());
        target.setCommandedSpeed(33.0);
        assertEquals(target.getCommandedSpeed(),stub.getCommandedSpeed());

        stub.setAuthority(0);
        assertEquals(target.getAuthority(),stub.getAuthority());
        target.setAuthority(10);
        assertEquals(target.getAuthority(),stub.getAuthority());
    }

    @Test
    @DisplayName("PLCScript will interact with remote block")
    void controllerBlock () throws Exception {
        target = tracksys.getGreenLine().get(15);
        RemoteTrack element = new RemoteTrack(target);
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD OCC15");
                add("SET");
            }
        };

        // PLC script will set output object a to true when 15 occupation=true and false when 15 occupation = false
        PLCInput blockInput = new OccupationPLCInput("OCC15",element, OccupationPLCInput.OccRule.TrueWhenOccupied);
        PLCOutput a = new PLCOutput("result");
        PLCEngine engine = new PLCEngine(PLCScript,a);
        engine.registerInputSource(blockInput);


        target.setOccupied(true);
        engine.evaluateLogic();
        assertTrue(a.value());
        System.out.printf("(Block:%b) gives (stub:%b) gives (a:%b)\n",target.getOccupied(),element.getStub().getOccupied(),a.value());

        target.setOccupied(false);
        engine.evaluateLogic();
        assertFalse(a.value());
        System.out.printf("(Block:%b) gives (stub:%b) gives (a:%b)\n",target.getOccupied(),element.getStub().getOccupied(),a.value());
    }

    @Test
    @DisplayName("PLCScript will interact with remote switch")
    void controllerSwitch () throws Exception {
        target = tracksys.getGreenLine().get(12);
        RemoteTrack element = new RemoteTrack(target);
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD OCC12");
                add("SET");
            }
        };

        // PLC script will set output object a to true when 15 occupation=true and false when 15 occupation = false
        PLCInput blockInput = new OccupationPLCInput("OCC12",element, OccupationPLCInput.OccRule.TrueWhenOccupied);
        //PLCOutput a = new PLCOutput("result");
        SwitchPLCOutput switchOutput = new SwitchPLCOutput("Switch Orientation",element, SwitchPLCOutput.SwitchRule.DefaultWhenTrue);
        PLCEngine engine = new PLCEngine(PLCScript,switchOutput);
        engine.registerInputSource(blockInput);


        target.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%s\n",element.getSwitchState());

        target.setOccupied(false);
        engine.evaluateLogic();
        System.out.printf("%s\n",element.getSwitchState());


        target.setOccupied(true);
        engine.evaluateLogic();
        System.out.printf("%s\n",element.getSwitchState());
    }

}