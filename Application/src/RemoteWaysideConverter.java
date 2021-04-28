import PLCInput.HasAuthorityPLCInput;
import PLCInput.OccupationPLCInput;
import RemoteBlock.RemoteTrack;
import RemoteConnection.RemoteWaysideController;
import RemoteWaysideServer.RemoteWaysideServer;
import Track.Track;
import TrackConstruction.TrackElement;
import WaysideController.*;

import java.util.ArrayList;

public class RemoteWaysideConverter {
    final static public String AUTHORITY_INPUT_PLC_VARIABLE_FORMAT = "HASAUTH%d";
    final static public String OCCUPATION_INPUT_PLC_VARIABLE_FORMAT = "OCC%d";
    public static HasAuthorityPLCInput.AuthRule DEFAULT_AUTHORITY_RULE = HasAuthorityPLCInput.AuthRule.TrueWhenGreaterOrEqual;
    public static int DEFAULT_AUTHORITY_CRITERIA = 1;
    public static OccupationPLCInput.OccRule DEFAULT_OCCUPATION_RULE = OccupationPLCInput.OccRule.TrueWhenOccupied;

    /** takes the contents of a wayside controller and casts the remote controller to it
     *
     * @param ctrl
     * @param ipAddress
     * @param portNumber
     * @return
     */
    public static RemoteWaysideController castToRemote(WaysideController ctrl, String ipAddress, int portNumber) throws Exception {
        RemoteWaysideController remoteController;
        try {
            remoteController = new RemoteWaysideController(ipAddress,portNumber,String.format("Remote(%s)",ctrl));
        } catch (Exception failureToRetrieveRemoteWaysideService) {
            failureToRetrieveRemoteWaysideService.printStackTrace();
            throw new Exception(String.format("Failure to retrieve remote WaysideService from ip:%s on port number:%d for casting controller:%s\nEnsure that RemoteWaysideServer is running on device with IP:%s and that ip matches the ip found in device's ifconfig\n",
                                                    ipAddress,portNumber,ctrl,ipAddress));
        }


        // Give track jurisdiction
        HasAuthorityPLCInput auth;
        OccupationPLCInput occ;
        for(TrackElement block : ctrl.getJurisdiction()) {
            // Convert block to remote block
            RemoteTrack remoteBlock = new RemoteTrack(block);
            // Create PLCInputs
            auth = new HasAuthorityPLCInput(String.format(AUTHORITY_INPUT_PLC_VARIABLE_FORMAT,remoteBlock.getBlockNum()), remoteBlock, DEFAULT_AUTHORITY_RULE, DEFAULT_AUTHORITY_CRITERIA);
            occ = new OccupationPLCInput(String.format(OCCUPATION_INPUT_PLC_VARIABLE_FORMAT,remoteBlock.getBlockNum()), remoteBlock, DEFAULT_OCCUPATION_RULE);
            // Give Inputs
            remoteController.giveInput(auth);
            remoteController.giveInput(occ);
            // Give Block
            remoteController.overseeBlock(remoteBlock);
        }
        remoteController.setControllerAlias(ctrl.getControllerAlias());
        remoteController.setControllerName(ctrl.getName());

        System.out.println(remoteController);

        // Stop the local controller from performing processes
        ctrl.halt();

        return remoteController;
    }


    public static void main(String[] args) throws Exception {
        RemoteWaysideServer server;
        Track trackSys = new Track();
        ArrayList<TrackElement> greenLine;
        WaysideSystem ws;
        WaysideController localController;


        trackSys.importTrack("/Users/elijah/IdeaProjects/ECE1140_Group2_TransitSimulation/Application/Resources/RedGreenUpdated.csv");
        greenLine = trackSys.getGreenLine();
        ws = new WaysideSystem(greenLine,"Green");
        localController = ws.getControllers().get(0);


        RemoteWaysideController remoteFromClient = RemoteWaysideConverter.castToRemote(localController,"192.168.0.106",5099);
        System.out.println("Remote from client: "+remoteFromClient.toLongString());
    }
}
