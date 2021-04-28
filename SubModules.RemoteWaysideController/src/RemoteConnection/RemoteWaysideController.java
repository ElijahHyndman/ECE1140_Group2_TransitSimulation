package RemoteConnection;

import PLCInput.PLCInput;
import RemoteWaysideServer.RemoteWaysideStub;
import TrackConstruction.TrackElement;
import WaysideController.WaysideController;
import WaysideGUI.WaysideSystemUI;

import java.rmi.RemoteException;
import java.util.ArrayList;

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



    /*
            Wrapping methods using stub
     */



    public String handshake(String fromClient) throws RemoteException {
        return remoteCtrl.handshake(fromClient);
    }


    /** returns the WaysideController object located on the remote machine.
     * @assert local member "controller" is never null
     * @return WaysideController, the wayside controller object that the RemoteWaysideService is hosting
     */
    public WaysideController getController() throws RemoteException {
        return remoteCtrl.getController();
    }


    /** turns this remote WaysideController into a copy of a given wayside controller.
     * @param ctrl  WaysideController, the controller which we will create a carbon copy of into the remote WaysideController
     * @before WaysideController hosted by RemoteService may be empty or filled with values
     * @after WaysideController hosted by RemoteService has been overwritten to be a carbon copy of given WaysideController object (deep copy)
     */
    public void castController(WaysideController ctrl) throws RemoteException {
        remoteCtrl.castController(ctrl);
    }


    /** generates a User Interface Window on the hosting machine for the remote WaysideController.
     * @before remote wayside controller is not null, may have values in it.
     * @after a new WaysideController Jframe UI window has been spawned
     * @after new JFrame window is running and updating on a new thread
     */
    public void spawnUI() throws RemoteException {
        remoteCtrl.spawnUI();
    }

    @Override
    public void assignInputPool(ArrayList<PLCInput> inputs) {
        try {
            remoteCtrl.assignInputPool(inputs);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBlockSpeed(int targetBlockIndex, double newCommandedSpeed) throws RemoteException {
        remoteCtrl.setBlockSpeed(targetBlockIndex,newCommandedSpeed);
    }

    @Override
    public void setBlockAuthority(int targetBlockIndex, int newAuthority) throws RemoteException {
        remoteCtrl.setBlockAuthority(targetBlockIndex,newAuthority);
    }

    @Override
    public boolean getOccupancy(int targetBlockIndex) throws RemoteException {
        return remoteCtrl.getOccupancy(targetBlockIndex);
    }

    @Override
    public void setClose(int blockNumber) throws RemoteException {
        remoteCtrl.setClose(blockNumber);
    }

    @Override
    public void setOpen(int blockNumber) throws RemoteException {
        remoteCtrl.setOpen(blockNumber);
    }

    @Override
    public boolean getIsClosed(int blockNumber) throws RemoteException {
        return remoteCtrl.getIsClosed(blockNumber);
    }

    @Override
    public boolean getSwitchStatus(int blockNumber) throws RemoteException {
        return remoteCtrl.getSwitchStatus(blockNumber);
    }

    @Override
    public void setSwitchStatus(int blockNumber, boolean status) throws RemoteException {
        remoteCtrl.setSwitchStatus(blockNumber,status);
    }

    @Override
    public void setControllerAlias(String controllerAlias) {
        try {
            remoteCtrl.setControllerAlias(controllerAlias);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public String getControllerAlias() {
        try {
            return remoteCtrl.getControllerAlias();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "failed";
    }

    @Override
    public String getControllerName() {
        try {
           return remoteCtrl.getControllerName();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "failure";
    }

    @Override
    public void setControllerName(String name) {
        try {
            remoteCtrl.setControllerName(name);
            System.out.println("Setting controller name");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<TrackElement> getJurisdiction() {
        try {
            return remoteCtrl.getJurisdiction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void giveInput(PLCInput input) {
        try {
            remoteCtrl.giveInput(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toMedString() {
        try {
            return remoteCtrl.toMedString();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "failure";
    }

    @Override
    public void start() {
        try {
            remoteCtrl.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void overseeBlock(TrackElement remoteBlock) throws Exception {
        try {
            remoteCtrl.overseeBlock(remoteBlock);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
