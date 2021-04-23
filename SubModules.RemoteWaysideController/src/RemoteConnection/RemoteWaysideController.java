package RemoteConnection;

import RemoteWayside.RemoteWaysideStub;
import WaysideController.WaysideController;

/** object that allows us to use a WaysideController object running on a separate device (using a RemoteWaysideServer) as if it werre running locally on this machine.

 *
 *  System Layout:
 (this)RemoteWaysideController (on Laptop) object that can be treated as if it were a local WaysideController, but instead manages a remote connection to a remote WaysideControllerService
 *  RemoteWaysideServer     (on Pi) hosts the RemoteWaysideService and makes it available for the Client across a network.
 *  RemoteConnection    (on Laptop) manages the Client->Server connection for the client. If successful, provides the stub for remote objects
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
public class RemoteWaysideController extends WaysideController {
    /***********************************************************************************************************************/
    /** Networking Members
     */
    private RemoteWaysideStub remoteCtrl = null;
    private WaysideController localProxyController = new WaysideController("Proxy");

    /** Members
     */
    private String name = "Default";

    /** Default Members
     */
    final private String REMOTE_WAYSIDE_STUB_BINDING_KEY = "Service";
    final private String SERVER_CONFIRMATION_STRING = "[from Service]";
    /***********************************************************************************************************************/

    public RemoteWaysideController(String name) {
        super(name);
    }

    public RemoteWaysideController(String IPAddress, int communicationPort, String name) throws Exception {
        this.name = name;
        try {
            remoteCtrl = RemoteConnection.retrieveRemoteWaysideController(IPAddress, communicationPort, REMOTE_WAYSIDE_STUB_BINDING_KEY);
        } catch (Exception e) {
            System.out.printf("Could not find remote WaysideController at (IP:%s Port:%d).\n",IPAddress,communicationPort);
            System.out.printf("Make sure that the target machine is running the RemoteWaysideServer application.\n");
            e.printStackTrace();
            throw new Exception(String.format("Could not find remote WaysideController at (IP:%s Port:%d).\n",IPAddress,communicationPort));
        }
        boolean goodConnection = isValidConnection();
        if (!goodConnection) {
            System.out.printf("Incorrect handshake result from remote WaysideController at (IP:%s Port:%d).\n",IPAddress,communicationPort);
            throw new Exception(String.format("Incorrect Handshake Error\n"));
        }
    }


    /** performs a handshake with the referenced RemoteWaysideServer (interacted with through remoteCtrl) to determine if server is responding correctly.
     * @return boolean, server responded with the correct handshake response (connection is good)
     */
    private boolean isValidConnection() {
        if (remoteCtrl == null) {
            System.out.println("Attempted to validate RemoteWayside connection with a null RemoteWaysideStub.");
            return false;
        }
        String clientString = "Hello From Client.";
        String result = "";
        try{result = remoteCtrl.handshake(clientString);} catch (Exception e) {e.printStackTrace();}
        String expectedResult = clientString + SERVER_CONFIRMATION_STRING;
        return result.equals(expectedResult);
    }
}
