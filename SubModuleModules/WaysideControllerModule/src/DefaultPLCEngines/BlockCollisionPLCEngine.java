package DefaultPLCEngines;

import PLCOutput.PLCOutput;
import TrackConstruction.TrackElement;
import WaysideController.PLCEngine;

import java.util.ArrayList;


/** defines the default, safety-critical PLCEngine for avoiding collision
 *
 */
public class BlockCollisionPLCEngine extends PLCEngine {
    /** Members
     */
    private TrackElement target;
    private PLCOutput targetAuthority;
    public BlockCollisionPLCEngine (TrackElement target) {
        this.target = target;
    }

    /** generates the PLC script for avoiding collision
     *
     * @return
     */
    public static ArrayList<String> generateCollisionAvoidancePLCScript(TrackElement target) {
        return null;
    }

    public static ArrayList<TrackElement> getAdjacentTrackElements (TrackElement target) {
        System.out.printf("Target: %s is connected to blocks %s %s and %s\n",target.getBlockNum(),target.getDirection(0),target.getDirection(1),target.getDirection(2));
        return null;
    }
}
