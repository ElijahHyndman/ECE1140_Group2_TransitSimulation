package RemoteConnection;

import RemoteWayside.RemoteWaysideServer;
import WaysideController.WaysideController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoteWaysideControllerTest {
    RemoteWaysideServer localServer = null;

    @BeforeEach
    void setup() {
        localServer = new RemoteWaysideServer("127.0.0.1",5099);
    }

    @Test
    @DisplayName("Basic construction works with local RemoteWaysideController service")
    public void localRemoteWaysideController() throws Exception {
        RemoteWaysideController remote = new RemoteWaysideController("127.0.0.1",5099,"First Try");
    }

    @Test
    @DisplayName("Remote Wayside Controller can be encapsulated by a local wayside controller")
    public void interchangeable() throws Exception {
        WaysideController remote = new RemoteWaysideController("127.0.0.1",5099,"Imposter");
    }
}