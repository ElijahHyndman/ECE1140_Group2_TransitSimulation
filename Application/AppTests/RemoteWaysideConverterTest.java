import RemoteConnection.RemoteWaysideController;
import RemoteWaysideServer.RemoteWaysideServer;
import Track.Track;
import TrackConstruction.TrackElement;
import WaysideController.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RemoteWaysideConverterTest {
    RemoteWaysideServer server;
    WaysideController remoteFromServer;
    Track trackSys = new Track();
    ArrayList<TrackElement> greenLine;
    WaysideSystem ws;
    WaysideController localController;

    @BeforeEach
    void setup() throws Exception {
        trackSys.importTrack("Resources/RedGreenUpdated.csv");
        greenLine = trackSys.getGreenLine();
        ws = new WaysideSystem(greenLine,"Green");
        server = new RemoteWaysideServer("192.168.0.106",5099);
        remoteFromServer = server.getController();
        localController = ws.getControllers().get(0);
    }

    @Test
    @DisplayName("Local controller can be cast to remote controller")
    void casting() throws Exception {
        System.out.println("local: "+localController.toString());
        System.out.println("Remote: "+remoteFromServer.toString());
        //System.out.println(localController.toLongString());
        System.out.println("!!!!!!!!!Casting");
        RemoteWaysideController remoteFromClient = RemoteWaysideConverter.castToRemote(localController,"127.0.0.1",5099);
        System.out.println("local: "+localController.toString());
        System.out.println("Remote from server: "+remoteFromServer.toLongString());
        //System.out.println("Remote from client: "+remoteFromClient.toString());
    }

}