package RemoteWaysideServer;

import PLCInput.PLCInput;
import RemoteBlock.RemoteBlockStub;
import TrackConstruction.TrackElement;
import WaysideController.WaysideController;
import WaysideController.WaysideSystem;
import WaysideGUI.WaysideSystemUI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;


/** wraps around a WaysideController on the remote device to provide a service without touching the internal code of the WaysideController class.
 *  Implements the methods defined in RemoteWaysideStub interface. The methods defined in the interface are the only methods the client may use to interact with the remote controller.
 *
 *  Goal: Implement all behavior which the client will want to invoke across the network onto the WaysideController on the Raspberry Pi
 *
 *  System Layout:
 *  RemoteWaysideController (on Laptop) object that can be treated as if it were a local WaysideController, but instead manages a remote connection to a remote WaysideControllerService
 *  RemoteWaysideServer     (on Pi) hosts the RemoteWaysideService and makes it available for the Client across a network.
 *  RemoteConnection    (on Laptop) manages the Client->Server connection for the client. If successful, provides the stub for remote objects
 *  RemoteWaysideStub   (created on Pi, used by Laptop) defines the methods which a Client may invoke remotely
 (this)RemoteWaysideService    (on Pi) implements the methods defined by the RemoteWaysideStub
 *  RemoteTrackStub     (created on Laptop, used by Pi) defines the methods which a remote WaysideController may invoke remotely
 *  RemoteTrack         (on Laptop) implements the methods defined by the RemoteTrackStub
 *
 *
 *  Definitions:
 *  "Local" (The laptop) the machine which the SimulationEnvironment is running on, which has the stub to control this remoteService. Likely a different machine than "Remote"
 *  "Remote" (The Raspberry Pi) the machine which this remoteService is running on. Likely a different machine than "Local"
 *      -In almost all cases "Remote" will be a different machine than "Local." (Such as local running on Laptop and Remote running on Raspberry pi)
 *      -A remoteService can also be run on the same machine as "Local" if desired (useful for testing!)
 *
 *  Usage:
 *      a RemoteWaysideService is a wrapper for a WaysideController. Things invoked on a RemoteWaysideService will get invoked on the WaysideController.
 *      A RemoteWaysideService allows us to interact with a WaysideController remotely (from laptop to raspberry pi) without changing any code inside the WaysideController.java.
 *      For a client (on a laptop) to interact with a RemoteWaysideService (on a raspberry pi,) the client shall need access to a stub (an RMI construct.) The RemoteWaysideServer makes the RMI stubs available for clients.
 *      ergo, using a RemoteWaysideService remotely requires using a RemoteWaysideServer to make it available to clients
 *
 *
 * @author Elijah
 */
public class RemoteWaysideService implements RemoteWaysideStub {
    /***********************************************************************************************************************/
    /** Wayside Members
     * @member controller  WaysideController, the wayside controller which shall run on this machine and which this service shall act as a wrapper for.
     */
    private WaysideController controller;

    /** Default Members
     */
    final private String SERVER_CONFIRMATION_STRING = "[from Service]";
    /***********************************************************************************************************************/


    /** creates a new RemoteWaysideService and the WaysideController object which it shall wrap.
     * @before no remoteWaysideService
     * @after RemoteWaysideService exists, RemoteWaysideService has created an empty wayside controller for servicing
     */
    public RemoteWaysideService() {
        // Create WaysideController object
        controller = new WaysideController("Remote Wayside Controller Service");
        //controller.setControllerAlias("Remote WaysideController Service");
    }


    /** simple response method for testing that RMI connection from client to this RemoteWaysideService is valid
     * @return String, original string from client with [from service] concatenated to the end to confirm that service is responding
     */
    @Override
    public String handshake(String fromClient) throws RemoteException {
        String response = fromClient + SERVER_CONFIRMATION_STRING;
        return response;
    }


    /** returns the WaysideController object located on the remote machine.
     * @assert local member "controller" is never null
     * @return WaysideController, the wayside controller object that the RemoteWaysideService is hosting
     */
    @Override
    public WaysideController getController() throws RemoteException {
        return controller;
    }


    /** turns this remote WaysideController into a copy of a given wayside controller.
     * @param ctrl  WaysideController, the controller which we will create a carbon copy of into the remote WaysideController
     * @before WaysideController hosted by RemoteService may be empty or filled with values
     * @after WaysideController hosted by RemoteService has been overwritten to be a carbon copy of given WaysideController object (deep copy)
     */
    @Override
    public void castController(WaysideController ctrl) throws RemoteException {
        if (ctrl == null) {
            System.out.println("Ignoring attempt to cast Remote Wayside Controller using null controller.");
            return;
        }
        System.out.println("Casting WaysideController operation:");
        System.out.println("==original: " + controller);
        controller.copy(ctrl);
        System.out.println("==new: " + controller);
    }


    /** generates a User Interface Window on the hosting machine for the remote WaysideController.
     * @before remote wayside controller is not null, may have values in it.
     * @after a new WaysideController Jframe UI window has been spawned
     * @after new JFrame window is running and updating on a new thread
     */
    public void spawnUI() throws RemoteException {
        System.out.println("Spawning WaysideController window.");

        // Define Thread and its behavior
        Thread one = new Thread() {
            public void run() {
                WaysideSystemUI ui = null;
                try {
                    ui = getSingleControllerUI(controller);
                    ui.setVisible(true);
                } catch (Exception e) {
                    System.out.printf("Failure to spawn Wayside UI from RemoteWaysideService.\n===given wayside: %s\n===derived ui: %s\n",controller,ui);
                }
                while (true) {
                    ui.updateGUI(singleWaysideVector(controller));
                    System.out.println("updating");
                }
            }
        };
        // Run Thread
        one.start();
    }

    @Override
    public void assignInputPool(ArrayList<PLCInput> inputs) throws RemoteException {
        this.controller.assignInputPool(inputs);
    }

    @Override
    public void setBlockSpeed(int targetBlockIndex, double newCommandedSpeed) throws RemoteException {
        try {
            this.controller.setBlockSpeed(targetBlockIndex, newCommandedSpeed);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
    }

    @Override
    public void setBlockAuthority(int targetBlockIndex, int newAuthority) throws RemoteException {
        try {
            this.controller.setBlockAuthority(targetBlockIndex, newAuthority);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
    }

    @Override
    public boolean getOccupancy(int targetBlockIndex) throws RemoteException {
        try {
            return this.controller.getOccupancy(targetBlockIndex);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
        throw new RemoteException("Failure occurred on remote wayside controller to get block occupancy status");
    }

    @Override
    public void setClose(int blockNumber) throws RemoteException {
        try {
            this.controller.setClose(blockNumber);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
    }

    @Override
    public void setOpen(int blockNumber) throws RemoteException {
        try {
            this.controller.setOpen(blockNumber);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
    }

    @Override
    public boolean getIsClosed(int blockNumber) throws RemoteException {
        try {
            return this.controller.getIsClosed(blockNumber);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
        throw new RemoteException("Failure occurred on remote wayside controller to get block closure status");
    }

    @Override
    public boolean getSwitchStatus(int blockNumber) throws RemoteException {
        try {
            return this.controller.getSwitchStatus(blockNumber);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
        throw new RemoteException("Failure occurred on remote wayside controller to get switch status");
    }

    @Override
    public void setSwitchStatus(int blockNumber, boolean status) throws RemoteException {
        try {
            this.controller.setSwitchStatus(blockNumber, status);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
    }

    @Override
    public void setControllerAlias(String controllerAlias) throws RemoteException {
        try {
            this.controller.setControllerAlias(controllerAlias);
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
    }

    @Override
    public String getControllerAlias() throws RemoteException {
        try {
            return this.controller.getControllerAlias();
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
        return "Failure to get remote Controller Alias";
    }

    @Override
    public String getControllerName() throws RemoteException {
        try {
            return this.controller.getControllerName();
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
        return "Failure to get remote Controller Name";
    }

    @Override
    public void setControllerName(String name) throws RemoteException {
        this.controller.setControllerName(name);
    }

    @Override
    public ArrayList<TrackElement> getJurisdiction() throws RemoteException {
        try {
            return this.controller.getJurisdiction();
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
        throw new RemoteException("Failure occurred on remote wayside controller to retrieve jurisdiction");
    }

    @Override
    public String toMedString() throws RemoteException {
        try {
            return "Remote: " + this.controller.toMedString();
        } catch (Exception controllerError) {
            controllerError.printStackTrace();
        }
        return null;
    }

    @Override
    public void start() throws RemoteException {
        this.controller.start();
    }

    @Override
    public void overseeBlock(TrackElement remoteBlock) throws Exception {
        this.controller.overseeBlock(remoteBlock);
    }

    @Override
    public void giveInput(PLCInput input) throws RemoteException {
        this.controller.giveInput(input);
    }



    /*
        Static Helper Functions
     */



    /** creates a vector which contains just the given wayside controller.
     */
    public static Vector<WaysideController> singleWaysideVector(WaysideController controller) {
        Vector <WaysideController> ctrl = new Vector<WaysideController>();
        ctrl.add(controller);
        return ctrl;
    }


    /** creates a jframe window that allows for a single WaysideController (instead of a full wayside system).
     */
    public static WaysideSystemUI getSingleControllerUI(WaysideController controller) {
        LinkedList<WaysideController> ctrl = new LinkedList<WaysideController>();
        ctrl.add(controller);
        WaysideSystem ws = null;
        try {
            // TODO
            ws = new WaysideSystem();
        } catch (IOException e) {
            System.out.println("Failure to create Wayside System for wayside gui window.");
            e.printStackTrace();
        }
        try {
            WaysideSystemUI window = new WaysideSystemUI(ws);
            System.out.println(window);
            return window;
        } catch (IOException e) {
            System.out.println("Failure to generate new Wayside Controller JFrame Window.");
            return null;
        }
    }
}

