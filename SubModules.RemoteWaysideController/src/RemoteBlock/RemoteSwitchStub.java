package RemoteBlock;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteSwitchStub extends RemoteBlockStub {
    public boolean getIndex() throws RemoteException;
    public String getSwitchState() throws RemoteException;
    public void setSwitchState(boolean ori) throws RemoteException;
}
