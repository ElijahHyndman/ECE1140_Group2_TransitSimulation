package RemoteWayside;

import WaysideController.WaysideController;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteWaysideService implements RemoteWaysideStub {
    /** acts as a wrapper to the WaysideController whose methods can be invoked remotely using an RMI stub.
     *  Implements the methods defined in RemoteWaysideStub interface. The methods defined in the interface are the only methods which the client shall have access to through the stub.
     *
     *  Goal: Implement all behavior which the client will want to invoke across the network onto the WaysideController on the Raspberry Pi
     *
     *  System Layout:
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

    /***********************************************************************************************************************/
    /** Wayside Members
     * @member controller  WaysideController, the wayside controller which shall run on this machine and which this service shall act as a wrapper for.
     */
    private WaysideController controller;
    /***********************************************************************************************************************/

    public RemoteWaysideService() {
        /** creates a new RemoteWaysideService and the WaysideController object which it shall wrap.
         */
        // Create WaysideController object
        controller = new WaysideController();
    }

    @Override
    public String handshake(String fromClient) throws RemoteException {
        /** simple response method for testing that RMI connection from client to this RemoteWaysideService is valid
         */
        String response = fromClient + "[from service]";
        return response;
    }

    @Override
    public WaysideController getController() throws RemoteException {
        /** returns the WaysideController object located on the remote machine.
         * @assert local member "controller" is never null
         */
        return controller;
    }

    @Override
    public void castController(WaysideController ctrl) throws RemoteException {
        /** copies all information from a given controller ctrl onto the wayside controller on the remote machine.
         */
            String newName = ctrl.getControllerName();
            controller.setControllerName(newName);
            try {
                controller.setAuthority(ctrl.getAuthority());
            } catch (Exception e) {

            }
            try {
                controller.setSpeed(ctrl.getSpeed());
            } catch (Exception e) {

            }
    }
}
