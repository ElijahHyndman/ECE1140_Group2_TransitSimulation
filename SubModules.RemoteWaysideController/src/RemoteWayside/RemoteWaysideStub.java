package RemoteWayside;

import WaysideController.WaysideController;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteWaysideStub extends Remote {
    /** defines every command which we can issue to a remote waysidecontroller from our local scope.
     *  remote wayside controllers are imagined to be on another machine, though a local one can also be instantiated.
     *  Assert: RemoteWayside is the datatype of the stub which the local machine will use to communicate to the Remote Wayside Controller
     *  Assert: All data sent to RemoteWayside controller must capable of and will be serialized. This means everything is passed by copy
     */
    public String handshake(String fromClient) throws RemoteException;
    public WaysideController getController() throws RemoteException;
    public void castController(WaysideController ctrl) throws RemoteException;
}
