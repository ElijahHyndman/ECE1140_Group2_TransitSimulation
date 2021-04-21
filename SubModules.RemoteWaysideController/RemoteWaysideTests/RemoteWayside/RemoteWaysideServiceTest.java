package RemoteWayside;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RemoteWaysideServiceTest {
    RemoteWaysideService service;

    /*
        Interaction tests
     */

    @Test
    @DisplayName("Interaction\t\t[handshake outputs correctly]")
    public void testHandshake(){
        service = new RemoteWaysideService();

        String clientMessage = "connection test";
        String expectedResult = clientMessage + "[from service]";
        String response = "";
        try {
            response = service.handshake(clientMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.printf("Expected handshake result of (%s) ?= response of (%s)\n",expectedResult,response);
    }
}