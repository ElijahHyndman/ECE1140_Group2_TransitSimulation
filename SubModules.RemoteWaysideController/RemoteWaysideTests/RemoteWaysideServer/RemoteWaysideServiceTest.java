package RemoteWaysideServer;

import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import WaysideController.WaysideController;
import WaysideGUI.WaysideUIJFrameWindow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RemoteWaysideServiceTest {
    RemoteWaysideService service;

    /*
        Interaction tests
     */

    @Test
    @DisplayName("Interaction\t\t[handshake is correct given string]")
    public void testHandshake() throws RemoteException {
        service = new RemoteWaysideService();

        String clientMessage = "connection test";
        String expectedResponse = clientMessage + "[from service]";
        String response = "";
        response = service.handshake(clientMessage);
        System.out.printf("Expected handshake result of (%s) ?= response of (%s)\n",expectedResponse,response);
        assertTrue(expectedResponse.equals(response));
    }

    @Test
    @DisplayName("Interaction\t\t[handshake is correct given null]")
    public void handshakeHandlesNull() throws RemoteException {
        service = new RemoteWaysideService();

        String clientMessage = null;
        String expectedResponse = null + "[from service]";
        String response = "";
        response = service.handshake(clientMessage);
        System.out.printf("Expected handshake result of (%s) ?= response of (%s)\n",expectedResponse,response);
        assertTrue(expectedResponse.equals(response));
    }

    /*
        Wayside Controller Tests
     */

    @Test
    public void spawnsWithController() throws RemoteException {
        service = new RemoteWaysideService();
        assertNotNull(service.getController());
        WaysideController remoteWS = service.getController();
        System.out.println(remoteWS);
    }

    @Test
    @DisplayName("WaysideController\t\t[Remote Wayside Controller can be cast using a local controller with fake blocks]")
    public void controllerCanBeCast() throws RemoteException {
        // Create casting controller
        ArrayList<TrackElement> fakeBlocks = new ArrayList<TrackElement>();
        int numBlocks = 5;
        double defaultSpeed = 10.0;
        int defaultAuthority = 1;
        Switch switchBlock = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 0-2)",-3,0.5, new int[]{0,0,0},"n");
        fakeBlocks.add(switchBlock);
        for (int i=1; i<=numBlocks; i++) {
            TrackElement block = new TrackBlock();
            block.setAuthority(defaultAuthority);
            block.setCommandedSpeed(defaultSpeed);
            block.setOccupied(false);
            block.setBlockNum(i);
            fakeBlocks.add(block);
        }
        WaysideController localController = new WaysideController(fakeBlocks,"Local Controller");

        // Cast
        service = new RemoteWaysideService();
        WaysideController remoteWS = service.getController();
        System.out.println("Remote Wayside Before: ");
        System.out.println(remoteWS);

        service.castController(localController);

        System.out.println("Remote Wayside After:");
        System.out.println(remoteWS);
    }


    @Test
    @DisplayName("WaysideController\t\t[Remote Wayside Controller can be cast using a local controller with PLC and Blocks]")
    public void controllerCanBeCastWithPLC() throws IOException, URISyntaxException {
        // Create casting controller
        ArrayList<TrackElement> fakeBlocks = new ArrayList<TrackElement>();
        int numBlocks = 5;
        double defaultSpeed = 10.0;
        int defaultAuthority = 1;
        Switch switchBlock = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 0-2)",-3,0.5, new int[]{0,0,0},"n");
        fakeBlocks.add(switchBlock);
        for (int i=1; i<=numBlocks; i++) {
            TrackElement block = new TrackBlock();
            block.setAuthority(defaultAuthority);
            block.setCommandedSpeed(defaultSpeed);
            block.setOccupied(false);
            block.setBlockNum(i);
            fakeBlocks.add(block);
        }
        WaysideController localController = new WaysideController(fakeBlocks,"Local Controller");

        // Load and Compile PLC
        localController.addOutput(0,"RemoteWaysideResources/testtoken4");
        localController.generateOutputSignal(0,false);

        // Cast
        service = new RemoteWaysideService();
        WaysideController remoteWS = service.getController();
        System.out.println("Remote Wayside Before: ");
        System.out.println(remoteWS);
        service.castController(localController);
        System.out.println("Remote Wayside After:");
        System.out.println(remoteWS);

        // Confirm Copier does not just copy the references for wayside controller objects (i.e. performs deep copy instead of shallow copy)
        assertNotSame(localController.getGPIO(),remoteWS.getGPIO());
        assertNotSame(localController.getAllInputNames(),remoteWS.getInputNames());
        assertNotSame(localController.getAllNames(),remoteWS.getAllNames());
    }

    @Test
    public void castCatchesNullCasting() throws RemoteException {
        WaysideController nullController = null;
        service = new RemoteWaysideService();
        service.castController(nullController);
    }

    /*
        Static Method Testing
     */
    @Test
    public void staticJFrameFunctionWorks() {
        WaysideUIJFrameWindow result = null;
        result = RemoteWaysideService.getSingleControllerUI(new WaysideController("Empty Wayside Controller"));
        assertNotNull(result);
    }

    @Test
    public void canSpawnJFrameWindow() throws RemoteException {
        service = new RemoteWaysideService();
        service.spawnUI();
    }

}