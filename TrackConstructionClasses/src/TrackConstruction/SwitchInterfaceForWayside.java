package TrackConstruction;

import java.io.Serializable;

/** Grace,
 * Sorry to include this in your module, but it's necessary for giving a Remote Wayside Controller access to contorlling a switch across a network
 *
 */
public interface SwitchInterfaceForWayside {
    public int getBlockNum();
    public int getDirectionStates(int index);
    public void setSwitchState(boolean ind);
    public boolean getIndex();
    public void setOccupied(boolean occupied);
    public String getSwitchState();
    public boolean getOccupied();
    public String toString();
}
