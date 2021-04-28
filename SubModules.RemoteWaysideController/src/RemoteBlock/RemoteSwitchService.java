package RemoteBlock;

import TrackConstruction.Switch;
import TrackConstruction.TrackElement;

import java.rmi.RemoteException;

public class RemoteSwitchService extends RemoteBlockService implements RemoteSwitchStub {
    /***********************************************************************************************************************/
    Switch targetSwitch;
    /***********************************************************************************************************************/

    public RemoteSwitchService(Switch targetSwitch) {
        this.targetBlock = (TrackElement) targetSwitch;
        this.targetSwitch = targetSwitch;
    }

    @Override
    public boolean getIndex() throws RemoteException {
        return targetSwitch.getIndex();
    }

    @Override
    public String getSwitchState() throws RemoteException {
        return targetSwitch.getSwitchState();
    }

    @Override
    public void setSwitchState(boolean ori) throws RemoteException {
        targetSwitch.setSwitchState(ori);
    }
}
