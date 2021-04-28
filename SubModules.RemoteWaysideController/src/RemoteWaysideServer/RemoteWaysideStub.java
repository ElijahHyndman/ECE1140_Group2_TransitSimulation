package RemoteWaysideServer;

import PLCInput.PLCInput;
import TrackConstruction.TrackElement;
import WaysideController.WaysideController;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/** defines all of the behavior that is invocable on a remote waysideservice from a local context.
 *
 *  The client machine only gets access to a stub (this) to interact with a remote service. that means that if the client wants a behavior, it must be defined here.
 *
 *  Making Updates:
 *  If new functions are added to WaysideController, and we want the remote WaysideController to be indistinguishable, then we must:
 *      -add those functions to this interface
 *      -implement those functions in RemoteWaysideService
 *      -override those function calls within RemoteWaysideController
 *  If we do not add those functions to this interface, then we can never invoke them on a remote wayside.
 *  If we do not override those functions in RemoteWaysideController then function calls on a RemoteWaysideController object will invoke the local version and never invoke the intended, remote version
 *
 *  remote wayside controllers are imagined to be on another machine, though a local one can also be instantiated.
 *  Assert: RemoteWayside is the datatype of the stub which the local machine will use to communicate to the Remote Wayside Controller
 *  Assert: All data sent to RemoteWayside controller must capable of and will be serialized. This means everything is passed by copy
 */
public interface RemoteWaysideStub extends Remote {

    /** Remote specific functions
     */
    public String handshake(String fromClient) throws RemoteException;
    public WaysideController getController() throws RemoteException;
    public void castController(WaysideController ctrl) throws RemoteException;
    public void spawnUI() throws RemoteException;

    /** WaysideController actions
     */
    public void assignInputPool(ArrayList<PLCInput> inputs) throws RemoteException;
    public void setBlockSpeed(int targetBlockIndex, double newCommandedSpeed) throws RemoteException;
    public void setBlockAuthority(int targetBlockIndex, int newAuthority) throws RemoteException;
    public boolean getOccupancy(int targetBlockIndex) throws RemoteException;
    public void setClose(int blockNumber) throws RemoteException;
    public void setOpen(int blockNumber) throws RemoteException;
    public boolean getIsClosed(int blockNumber) throws RemoteException;
    public boolean getSwitchStatus(int blockNumber) throws RemoteException;
    public void setSwitchStatus(int blockNumber, boolean status) throws RemoteException;
    public void setControllerAlias(String controllerAlias) throws RemoteException;
    public String getControllerAlias() throws RemoteException;
    public String getControllerName() throws RemoteException;
    public ArrayList<TrackElement> getJurisdiction() throws RemoteException;
    public String toString();
    public String toMedString() throws RemoteException;
    public void start() throws RemoteException;
}
