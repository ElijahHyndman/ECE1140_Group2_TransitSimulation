package RemoteBlock;

import TrackConstruction.TrackElement;

import java.rmi.RemoteException;

public class RemoteBlockService implements RemoteBlockStub {
    /***********************************************************************************************************************/
    /** Members
     */
    TrackElement targetBlock = null;
    /***********************************************************************************************************************/
    /** default constructor only included for inheritance
     */
    protected RemoteBlockService() {
        super();
    }
    public RemoteBlockService(TrackElement targetBlock) {
        this.targetBlock = targetBlock;
        //System.out.printf("Made remote wrapper for block %s\n",targetBlock);
    }

    @Override
    public int getBlockNum() throws RemoteException {
        return targetBlock.getBlockNum();
    }

    @Override
    public int getDirection(int index) throws RemoteException {
        return targetBlock.getDirection(index);
    }

    @Override
    public int getSpeedLimit() throws RemoteException {
        return targetBlock.getSpeedLimit();
    }

    @Override
    public void setCommandedSpeed(double speed) throws RemoteException {
        targetBlock.setCommandedSpeed(speed);
    }

    @Override
    public double getCommandedSpeed() throws RemoteException {
        return targetBlock.getCommandedSpeed();
    }

    @Override
    public void setAuthority(int auth) throws RemoteException {
        targetBlock.setAuthority(auth);
    }

    @Override
    public int getAuthority() throws RemoteException {
        return targetBlock.getAuthority();
    }

    @Override
    public boolean getOccupied() throws RemoteException {
        return targetBlock.getOccupied();
    }

    @Override
    public void setFailureStatus(int failureStatus) throws RemoteException {
        targetBlock.setFailureStatus(failureStatus);
    }
}
