package WaysideController;

import Track.Track;
import TrackConstruction.TrackElement;

import java.util.ArrayList;

/**
 *
 * @author elijah
 */
public class JurisdictionBlock {
    /***********************************************************************************************************************/
    /** Members
     */
    TrackElement target;
    ArrayList<PLCEngine> associatedOutputs;
    /***********************************************************************************************************************/

    public JurisdictionBlock(TrackElement block) {
        this.target = block;
    }
}
