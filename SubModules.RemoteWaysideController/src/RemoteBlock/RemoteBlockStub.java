package RemoteBlock;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** interface for the stub that allows a remote wayside controller to control blocks from a remote interface
 *
 */
public interface RemoteBlockStub extends Remote {
    public int getBlockNum() throws RemoteException;
    public int getDirection(int index) throws RemoteException;
    public int getSpeedLimit() throws RemoteException;
    public void setCommandedSpeed(double speed) throws RemoteException;
    public double getCommandedSpeed() throws RemoteException;
    public void setAuthority(int auth) throws RemoteException;
    public int getAuthority() throws RemoteException;
    public boolean getOccupied() throws RemoteException;
    public void setFailureStatus(int failureStatus) throws RemoteException;
}
