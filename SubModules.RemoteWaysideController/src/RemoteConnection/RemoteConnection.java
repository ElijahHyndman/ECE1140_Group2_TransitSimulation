package RemoteConnection;

import RemoteWaysideServer.RemoteWaysideStub;
import TrackConstruction.TrackElement;
import WaysideController.WaysideController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;


/** handles the remote connection between the local and remote machines to retrieve a usable remote WaysideController.
 *
 *
 *  System Layout:
 *  RemoteWaysideController (on Laptop) object that can be treated as if it were a local WaysideController, but instead manages a remote connection to a remote WaysideControllerService
 *  RemoteWaysideServer     (on Pi) hosts the RemoteWaysideService and makes it available for the Client across a network.
 (this)RemoteConnection    (on Laptop) manages the Client->Server connection for the client. If successful, provides the stub for remote objects
 *  RemoteWaysideStub   (created on Pi, used by Laptop) defines the methods which a Client may invoke remotely
 *  RemoteWaysideService    (on Pi) implements the methods defined by the RemoteWaysideStub
 *  RemoteTrackStub     (created on Laptop, used by Pi) defines the methods which a remote WaysideController may invoke remotely
 *  RemoteTrack         (on Laptop) implements the methods defined by the RemoteTrackStub
 *
 *  Useful References:
 *  RMIRegistry not started reference: https://stackoverflow.com/questions/1823305/rmi-connection-refused-with-localhost
 *
 * @author elijah
 */
public class RemoteConnection {
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


    /** retrieves remote WaysideController stub [from specified machine (IP address, TCP port) and returns it if it exists.]
     */
    public static RemoteWaysideStub retrieveRemoteWaysideController(String serverIP, int communicationPort, String stubKey) throws Exception {
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


    /** retrieves RMI stub from a specified registry and with the stub's corresponding Key.
     * @return T    RMI Stub object type retrieved from registry
     * @param registry  Registry, an RMI registry which contains the bound Remote Stub + Stub Key
     * @param stubKey   String, the key String which the stub was bound to the RMIRegistry using
     * @before RMI stub and stub key must be bound to rmi registry by server
     */
    public static <T> T getRegistryStub(Registry registry, String stubKey) throws RemoteException, NotBoundException {
        T stub = (T) registry.lookup(stubKey);
        return stub;
    }


    /** retrieves the RMI Registry from a machine specified by an IP address and on a specific tcp-communication port.
     * @return Registry     the RMI Registry found inside the communicationPort on the machine specified by the IPAddress
     * @param serverIP  String, the X.X.X.X format IP address which the server machine is accessible using
     * @param communicationPort     int, the number for the TCP port which the RMI Registry was created on using the server
     * @before RemoteWaysideServer is running on a machine on the local network, it has created an RMIRegistry on the communicationPort given here
     * @after the RMIRegistry has been retrieved from server machine and returned
     */
    public static Registry getServerRegistry(String serverIP, int communicationPort) throws Exception {
        Registry serverRegistry = null;
        serverRegistry = LocateRegistry.getRegistry(serverIP,communicationPort);
        return serverRegistry;
    }



    public static void main(String[] args) throws Exception {

        WaysideController localController = new WaysideController(new ArrayList<TrackElement>(),"Local Controller");
        RemoteWaysideStub remoteWaysideController = null;
        try {
            remoteWaysideController = retrieveRemoteWaysideController("192.168.0.111",5099,"Service");
            System.out.println(remoteWaysideController);
            System.out.println(remoteWaysideController.handshake("This is from Elijah"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            remoteWaysideController.spawnUI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
