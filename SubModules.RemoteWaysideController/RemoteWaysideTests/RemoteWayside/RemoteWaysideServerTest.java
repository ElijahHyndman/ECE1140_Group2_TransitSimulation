package RemoteWayside;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoteWaysideServerTest {
    RemoteWaysideServer server;

    @Test
    @DisplayName("constructs default without error")
    public void testDefaultConstruction() {
        server = new RemoteWaysideServer();
    }

    @Test
    @DisplayName("constructs localhost without error")
    public void testLocalhostConstruction() {
        server = new RemoteWaysideServer("localhost",1099);
    }

    @Test
    @DisplayName("constructs with fake IP address")
    public void testFakeIPAddress() {
        server = new RemoteWaysideServer("8.8.8.8",0);
    }

    @Test
    @DisplayName("constructs with invalid form of IP Address")
    public void testInvalidIPAddress() {
        server = new RemoteWaysideServer("688.1234.0",0);
    }

    @Test
    @DisplayName("can fetch registry")
    public void testRegistryFetching() {
        server = new RemoteWaysideServer();
        try {
            System.out.println("RMI registry fetched at: "+server.getRMIRegistry());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}