package RemoteBlock;

import TrackConstruction.Switch;
import TrackConstruction.SwitchInterfaceForWayside;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** we must determine the correct remote connection to provide based on the given block, this function ascertains that
 *
 */
public class RemoteTrack extends TrackElement implements SwitchInterfaceForWayside {
    /***********************************************************************************************************************/
    /** Members
     */
    TrackElement localTargetBlock;
    RemoteBlockStub stub;
    /***********************************************************************************************************************/
    public RemoteTrack(TrackElement targetBlock) throws Exception {
        this.localTargetBlock = targetBlock;
        stub = getRemoteConnection(targetBlock);
    }


    /** one function
     *
     * @param targetBlock
     * @return
     */
    public static RemoteBlockStub getRemoteConnection(TrackElement targetBlock) throws Exception {
        int useAnyPortKey = 0;

        if (targetBlock instanceof TrackBlock) {
            RemoteBlockService service = new RemoteBlockService(targetBlock);
            RemoteBlockStub stub = (RemoteBlockStub) UnicastRemoteObject.exportObject(service,useAnyPortKey);
            System.out.printf("===Type:Block %s\n", stub.toString());
            return stub;

        } else if (targetBlock instanceof Switch) {
            Switch sw = (Switch) targetBlock;
            RemoteSwitchService service = new RemoteSwitchService(sw);
            // using inheritance
            RemoteBlockStub stub = (RemoteSwitchStub) UnicastRemoteObject.exportObject(service,useAnyPortKey);
            System.out.printf("===Type:Switch %s\n", stub.toString());
            return stub;
        }
        else {
            throw new Exception(String.format("Tried to create remote stub for item (%s) but there is no stub defined for that type yet.",targetBlock));
        }
    }

    public RemoteBlockStub getStub() {
        return stub;
    }

    /*
        Overloading TrackElement methods to use remote stubs
     */

    @Override
    public int getBlockNum() {
        try {
            return stub.getBlockNum();
        } catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getDirection(int index) {
        try {
            return stub.getDirection(index);
        }  catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getSpeedLimit() {
        try {
            return stub.getSpeedLimit();
        }  catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
        return -1;
    }

    @Override
    public void setCommandedSpeed(double speed) {
        try {
            stub.setCommandedSpeed(speed);
        }  catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
    }

    @Override
    public double getCommandedSpeed() {
        try {
            return stub.getCommandedSpeed();
        }  catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
        return -1.0;
    }

    @Override
    public void setAuthority(int auth) {
        try {
            stub.setAuthority(auth);
        }  catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
    }

    @Override
    public int getAuthority() {
        try {
            return stub.getAuthority();
        } catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean getOccupied() {
        try {
            return stub.getOccupied();
        }  catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
        return true;
    }

    @Override
    public void setFailureStatus(int failureStatus) {
        try {
            stub.setFailureStatus(failureStatus);
        }  catch (RemoteException failureToInteractWithRemoteBlock) {
            failureToInteractWithRemoteBlock.printStackTrace();
        }
    }

    /*
            Switch specific methods
     */


    @Override
    public void setSwitchState(boolean ind) {
        if (localTargetBlock instanceof Switch) {
            Switch sw = (Switch) localTargetBlock;
            sw.setSwitchState(ind);
        }
    }

    @Override
    public String getSwitchState() {
        if (localTargetBlock instanceof Switch) {
            Switch sw = (Switch) localTargetBlock;
            return sw.getSwitchState();
        }
        return "Invalid response from remote switch";
    }

}
