package WaysideController;

//import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.InputValueSwitch;

import PLCInput.*;
import PLCOutput.*;
import Track.Track;
import TrackConstruction.Switch;
import TrackConstruction.TrackElement;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;

import static java.lang.String.valueOf;

/** module class that uses boolean logic to dictate Track System controls. 
 * @author Harsh
 * @editor Elijah
 */
public class WaysideController implements Serializable {
    /***********************************************************************************************************************/
    /** Default Members
     */
    final private boolean DEFAULT_ISSOFTWARE = true;
    final static public String AUTHORITY_INPUT_PLC_VARIABLE_FORMAT = "Auth%d";
    final static public String OCCUPATION_INPUT_PLC_VARIABLE_FORMAT = "HasOcc%d";
    /** Global Members
     */
    private static int NUM_CONTROLLERS = 0;
    /** Controller Info
     * @member controllerIndex, the global index of this controller (given based on order of creation)
     * @member controllerName, a deterministic name for this controller given its index
     * @member contorllerAlias, a user-defined name for the controller if desired
     * @member isSoftware, boolean of whether this WaysideController is a software (WaysideController) or hardware (RemoteWaysideController) instance
     */
    private int controllerIndex = ++NUM_CONTROLLERS;
    private String controllerName = String.format("Controller %d",controllerIndex);
    private String controllerAlias = null;
    private boolean isSoftware = DEFAULT_ISSOFTWARE;
    /** Block-Relevant Lists
     * @member jurisdiction, an area of TrackElements which this wayside controller shall have output responsibilities for
     *      - jurisdiction only determines which blocks this controller outputs to, the controller may use any track within the provided system as input
     * @member inputPool, a pool of PLCInputs from which the
     */
    private ArrayList<TrackElement> jurisdiction = new ArrayList<TrackElement>(); //jurisdiction
    private ArrayList<PLCInput> inputPool = new ArrayList<PLCInput>();
    /** PLCScript Members
     */
    private ArrayList<PLCEngine> UserPLCScripts = new ArrayList<PLCEngine>();
    private ArrayList<PLCEngine> SafetyCriticalPLCScripts = new ArrayList<PLCEngine>();
    /** Test GUI Members
     */
    /***********************************************************************************************************************/

    // Default constructor is necessary for RemoteWaysideController inheritance
    protected WaysideController() {
        this.controllerAlias = null;
    }

    public WaysideController(String controllerAlias) {
        this.controllerAlias = controllerAlias;
    }

    public WaysideController(ArrayList<TrackElement> jurisdiction, String controllerAlias){
        this.controllerAlias = controllerAlias;
        giveJurisdiction(jurisdiction);
    }
    public WaysideController(ArrayList<TrackElement> fullTrackLine, ArrayList<TrackElement> jurisdictionForController, String controllerAlias) {
        this.jurisdiction = jurisdictionForController;
        this.controllerAlias = controllerAlias;
        // Wayside controller can use any track in the track line as a PLC Input
    }

    /** copies values from a target WaysideController into this wayside controller
     * @param target    WaysideController, the WaysideController we intend to copy
     */
    public void copy(WaysideController target) {
            this.controllerAlias = target.controllerAlias;
            this.isSoftware = target.isSoftware;
            this.jurisdiction = (ArrayList<TrackElement>) target.jurisdiction.clone();
    }

    /** gives this WaysideController jurisdiction over a set of blocks.
     *
     * @param blocks
     * @before WaysideController may or may not have jurisdiction
     * @after WaysideController has taken jurisdiction over new blocks, old jurisdiction has been overwritten
     */
    public void giveJurisdiction(ArrayList<TrackElement> blocks) {
        // Clear old PLC scripts
        this.UserPLCScripts = new ArrayList<PLCEngine>();
        this.SafetyCriticalPLCScripts = new ArrayList<PLCEngine>();
        // Accept new jurisdiction
        for (TrackElement block : blocks) {
            overseeBlock(block);
        }
    }

    public static ArrayList<PLCInput> generateInputPool(ArrayList<TrackElement> tracks) {
        ArrayList<PLCInput> generated = new ArrayList<PLCInput>();
        int trackBlockIndex;
        String authorityVariableName;
        String occupationVariableName;

        // Wayside controller can use any track in the track line as a PLC Input
        for (TrackElement element : tracks ) {
            trackBlockIndex = element.getBlockNum();
            // Referencable Variable Name
            authorityVariableName = String.format(AUTHORITY_INPUT_PLC_VARIABLE_FORMAT, trackBlockIndex);
            occupationVariableName = String.format(OCCUPATION_INPUT_PLC_VARIABLE_FORMAT, trackBlockIndex);
            // Create PLC Input Objects
            generated.add(new HasAuthorityPLCInput(authorityVariableName, element));
            generated.add(new OccupationPLCInput(occupationVariableName, element));
            // Add plc objects
        }
        return generated;
    }

    /** handles jurisdiction responsibility generation for an individual block
     *
     * @param block
     */
    public void overseeBlock(TrackElement block) {
        System.out.printf("Now overseeing block %d\n",block.getBlockNum());
    }


    /*
        Methods for CTC
     */
    public void setSpeed(double[] speeds) throws IOException {
    }


    public void setSpeed(int blockNumber, double speeds) throws IOException {
    }


    public double[] getSpeed() {
        return null;
    }


    public void setAuthority(int[] authorities) throws IOException {
    }


    public void setAuthority(int blockNumber, int authority) throws IOException {
    }


    public int[] getAuthority() {
        return null;
    }


    public boolean getSwitchStatus(int blockNumber) throws IOException {
        return false;
    }


    public void setSwitchStatus(int blockNumber, boolean status) throws IOException {
    }


    public void setClose(int blockNumber) throws IOException {
    }


    /** generates a String-Hashmap tree representation of this controller for use in the GUI.
     *  The tree is two layers deep after the controller name.
     *  Usage:
     *  Given a return Hashmap entry of:
     *  Hashmap = [<"Title 1" , <"Node 1: a value","Node 2: a value","Node 3: a value"> <"Title 2" , <"Node 22: a value","Node 23: a value","Node 28: a value">>]
     *  The resulting tree will be:
     *  ControllerName
     *  |_Title 1
     *      |_Node 1: a value
     *      |_Node 2: a value
     *      |_Node 3: a value
 *      |_Title 2
     *      |_Node 22: a value
     *      |_Node 23: a value
     *      |_Node 28: a value
     *
     *   Where the String key determines the title of a collapsible section in the Wayside Controller
     *   and each String-element in the string vector becomes an entry under the collapsible title
     *
     * @return HashMap<String,Vector<String>>, hashmap representation of <Title String, String-List-Under_Title>
     */
    public HashMap<String, Vector<String>> generateDescriptionNodes() {
        HashMap<String, Vector<String>> hash = new HashMap<String, Vector<String>>();
        /*
            Inputs Node
         */
        String inputCategory = "Input Signals";
        Vector<String> inputVector = new Vector<>();
//        boolean[] inputValues = gpio.getAllInputValues();
//        for(int i=0;i < inputValues.length;i++){
//            inputVector.add("input "+i+" : "+inputValues[i]);
//        }
        hash.put(inputCategory, inputVector);
        /*
            Outputs Node
         */
        String outputCategory = "Output Signals";
        Vector<String> outputVector = new Vector<>();
//        Boolean[] outputValues = gpio.getOutputValues();
//        for(int i=0;i < outputValues.length;i++){
//            outputVector.add("output "+i+" : "+outputValues[i]);
//        }
        hash.put(outputCategory, outputVector);
        /*
            Speed Node
         */
        String speedCategory = "Speed";
        Vector<String> speedVector = new Vector<>();
        double[] speed = getSpeed();
        for(int i=0;i < speed.length;i++){
            speedVector.add("block speed "+i+" : "+speed[i]);
        }
        hash.put(speedCategory, speedVector);
        /*
            Authority Node
         */
        int[] authority = getAuthority();
        String authorityCategory = "Authority";
        Vector<String> authorityVector = new Vector<>();
        for(int i=0;i < authority.length;i++){
            authorityVector.add("block authority "+i+" : "+authority[i]);
        }
        hash.put(authorityCategory, authorityVector);
        // Return tree format
        return hash;
    }


    public void setControllerAlias(String controllerAlias) {this.controllerAlias = controllerAlias;}
    public void setControllerName(String newName){ this.controllerAlias = newName; }
    public String getControllerAlias(){ return controllerAlias; }
    public TrackElement getBlockElement(int blockNumber) throws IOException {
        for(int i = 0; i < jurisdiction.size(); i++){
            if(blockNumber == jurisdiction.get(i).getBlockNum()){
                return jurisdiction.get(i);
            }
        }

        throw new IOException("Controller Error: No block with that number in controller - " + controllerAlias +  " " + Integer.toString(blockNumber));
    }

    /*
        String Representation
     */

    public String toString(){
        String profile = controllerName;
        if (controllerAlias != null)
            profile += String.format("[Alias \"%s\"]\n",controllerAlias);
        return profile;
    }
    public String toMedString() {
        String profile = toString();
        profile+= "Jurisdiction: TODO\n";
        return profile;
    }
}
