package RemoteConnection;

import RemoteWayside.RemoteWaysideStub;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteConnection {
    /** handles the remote connection between the local and remote machines to retrieve a usable remote WaysideController.
     */
    /***********************************************************************************************************************/
    /** Network Members
     */
    private int communicationPort;
    private String serverIP;

    /** RMI Members
     */
    private final String REMOTE_WAYSIDE_KEY = "Service";
    private Registry serverRegistry = null;
    private RemoteWaysideStub remoteWaysideController = null;
    /***********************************************************************************************************************/

    public static RemoteWaysideStub retrieveRemoteWaysideController(String serverIP, int communicationPort, String stubKey) throws Exception {
        /** retrieves remote WaysideController stub from specified machine (IP address, TCP port) and returns it if it exists.
         */
        Registry serverRMIRegistry;
        RemoteWaysideStub stub;
        try {
            serverRMIRegistry = getServerRegistry(serverIP,communicationPort);
        } catch (Exception e) {
            System.out.printf("Cannot retrieve RMI Registry from Machine (%S) on TCP port (%d)\n",serverIP,communicationPort);
            throw new Exception("Failure to retrieve RMI Registry for client.");
        }
        try {
            stub = getRegistryStub(serverRMIRegistry,stubKey);
        } catch (Exception e) {
            System.out.printf("Cannot retrieve RemoteWaysideStub from Machine (%S) on TCP port (%d)\nRegistry:\t%s\n",serverIP,communicationPort,serverRMIRegistry);
            throw new Exception("Failure to retrieve RMI Registry for client.");
        }
        return stub;
    }

    public static <T> T getRegistryStub(Registry registry, String stubKey) throws RemoteException, NotBoundException {
        /** retrieves RMI stub from a specified registry and with the stub's corresponding Key.
         * @return T    RMI Stub object type retrieved from registry
         * @param registry  Registry, an RMI registry which contains the bound Remote Stub + Stub Key
         * @param stubKey   String, the key String which the stub was bound to the RMIRegistry using
         * @before RMI stub and stub key must be bound to rmi registry by server
         */
        T stub = (T) registry.lookup(stubKey);
        return stub;
    }

    public static Registry getServerRegistry(String serverIP, int communicationPort) throws Exception {
        /** retrieves the RMI Registry from a machine specified by an IP address and on a specific tcp-communication port.
         * @return Registry     the RMI Registry found inside the communicationPort on the machine specified by the IPAddress
         * @param serverIP  String, the X.X.X.X format IP address which the server machine is accessible using
         * @param communicationPort     int, the number for the TCP port which the RMI Registry was created on using the server
         * @before RemoteWaysideServer is running on a machine on the local network, it has created an RMIRegistry on the communicationPort given here
         * @after the RMIRegistry has been retrieved from server machine and returned
         */
        Registry serverRegistry = null;
        serverRegistry = LocateRegistry.getRegistry(serverIP,communicationPort);
        return serverRegistry;
    }



    public static void main(String[] args) {
        try {
            RemoteWaysideStub remoteWaysideController = retrieveRemoteWaysideController("192.168.0.143",5099,"Service");
            System.out.println(remoteWaysideController);
            System.out.println(remoteWaysideController.handshake("This is from Elijah"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
