package WaysideController;

//import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.InputValueSwitch;

import PLCInput.*;
import PLCOutput.*;
import TrackConstruction.Switch;
import TrackConstruction.TrackElement;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;

/** module class that uses boolean logic to dictate Track System controls. 
 * @author Harsh
 * @editor Elijah
 */
public class WaysideController extends Thread implements Serializable {
    /***********************************************************************************************************************/
    /** Default Members
     */
    final private boolean DEFAULT_ISSOFTWARE = true;
    /** Global Members
     */
    private static int GLOBAL_NUM_CONTROLLERS = 0;
    /** Controller Info
     * @member controllerIndex, the global index of this controller (given based on order of creation)
     * @member controllerName, a deterministic name for this controller given its index
     * @member contorllerAlias, a user-defined name for the controller if desired
     * @member isSoftware, boolean of whether this WaysideController is a software (WaysideController) or hardware (RemoteWaysideController) instance
     */
    private int controllerIndex = ++GLOBAL_NUM_CONTROLLERS;
    private String controllerName = String.format("Controller %d",controllerIndex);
    private String controllerAlias = null;
    private boolean isSoftware = DEFAULT_ISSOFTWARE;
    private boolean running = false;
    private boolean outputing = true;
    /** Block-Relevant Lists
     * @member jurisdiction, an area of TrackElements which this wayside controller shall have output responsibilities for
     *      - jurisdiction only determines which blocks this controller outputs to, the controller may use any track within the provided system as input
     * @member fullTrack, the entire area over which the wayside system that this controller exists in oversees
     * @member inputPool, a pool of PLCInputs from which the
     */
    private ArrayList<TrackElement> jurisdiction = new ArrayList<TrackElement>(); //jurisdiction
    private ArrayList<TrackElement> fullTrack = new ArrayList<>();
    private ArrayList<PLCInput> inputPool = new ArrayList<PLCInput>();
    private HashMap<Integer, PLCOutput> switchPLCControls = new HashMap<Integer, PLCOutput>();
    /** PLCScript Members
     */
    volatile private ArrayList<PLCEngine> UserPLCScripts = new ArrayList<PLCEngine>();
    volatile private ArrayList<PLCEngine> SafetyCriticalPLCScripts = new ArrayList<PLCEngine>();
    /** Test GUI Members
     */
    /***********************************************************************************************************************/

    // Default constructor is necessary for RemoteWaysideController inheritance
    protected WaysideController() {
    }
    public WaysideController(String controllerAlias) {
        this.controllerAlias = controllerAlias;
    }
    public WaysideController(ArrayList<TrackElement> jurisdiction, String controllerAlias) throws Exception {
        this.controllerAlias = controllerAlias;
        giveJurisdiction(jurisdiction);
    }
    public WaysideController(ArrayList<TrackElement> fullTrackLine, ArrayList<TrackElement> jurisdictionForController) throws Exception {
        this.fullTrack = fullTrackLine;
        giveJurisdiction(jurisdictionForController);
    }
    public WaysideController(ArrayList<TrackElement> fullTrackLine, ArrayList<TrackElement> jurisdictionForController, String controllerAlias) throws Exception {
        this.controllerAlias = controllerAlias;
        this.fullTrack = fullTrackLine;
        giveJurisdiction(jurisdictionForController);
    }

    /** copies values from a target WaysideController into this wayside controller
     * @param target    WaysideController, the WaysideController we intend to copy
     */
    public void copy(WaysideController target) {
            this.controllerAlias = target.controllerAlias;
            this.isSoftware = target.isSoftware;
            this.jurisdiction = (ArrayList<TrackElement>) target.jurisdiction.clone();
    }


    /*
            Process
     */

    /** function used to launch this WaysideController on a new thread and continually update defined PLC Scripts.
     * user defined plc scripts are evaluated, then safety critical scripts are updated to overwrite user scripts
     *
     * PLC Scripts will fail to run until input pool has been defined using .assignInputPool()
     * PLC Scripts will fail silently
     *
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            if (outputing) {
                for (PLCEngine userScript : UserPLCScripts) {
                    try {
                        userScript.evaluateLogic();
                    } catch (Exception failureToExecuteScript) {
                        failureToExecuteScript.printStackTrace();
                        //System.out.println("Failure occured when running script:\n" + userScript.getPLCString());
                    }
                }
                for (PLCEngine safetyCriticalScript : SafetyCriticalPLCScripts) {
                    try {
                        safetyCriticalScript.evaluateLogic();
                    } catch (Exception failureToExecuteScript) {
                        failureToExecuteScript.printStackTrace();
                        //System.out.println("Failure occured when running script:\n" + safetyCriticalScript.getPLCString());
                    }
                }
            }// end if
        }
    }

    /** (called from this thread) interrupts running loop (which exists on separate thread) so PLC stop continually updating
     *
     */
    public void halt() {
        this.running = false;
    }

    public void setOutputing(boolean generating) {
        outputing = generating;
    }



    public void uploadPLCScript(PLCEngine script) {
        script.setInputSources(inputPool);
        UserPLCScripts.add(script);
    }
    public void uploadSafetyPLCScript(PLCEngine script) {
        script.setInputSources(inputPool);
        SafetyCriticalPLCScripts.add(script);
    }

    /*
            Wayside System Methods
     */



    /** gives this WaysideController jurisdiction over a set of blocks, performs any necessary screening.
     *
     * @param blocks
     * @before WaysideController may or may not have jurisdiction
     * @after WaysideController has taken jurisdiction over new blocks, old jurisdiction has been overwritten
     */
    public void giveJurisdiction(ArrayList<TrackElement> blocks) throws Exception {

        // Clear old PLC scripts
        this.UserPLCScripts = new ArrayList<PLCEngine>();
        this.SafetyCriticalPLCScripts = new ArrayList<PLCEngine>();

        // Accept new jurisdiction
        for (TrackElement block : blocks) {
            overseeBlock(block);
        }
        /*
                Generate new default PLC Scripts
         */
    }


    /** handles jurisdiction responsibility generation for an individual block
     *
     * @param block, the track block whom this wayside controller shall take output responsibilities for
     */
    public void overseeBlock(TrackElement block) throws Exception {
        jurisdiction.add(block);
        //System.out.printf("%s now overseeing block %d\n",this.controllerName , block.getBlockNum());
        try {
            PLCEngine collisionPLC = generateCollisionAvoidanceScript(block);
            collisionPLC.setInputSources(inputPool);
            SafetyCriticalPLCScripts.add(collisionPLC);
        } catch (Exception failedToGenerateScript) {
            failedToGenerateScript.printStackTrace();
            System.out.println(String.format("Failure to generate default collision script for block index %d",block.getBlockNum()));
        }
        if (block instanceof Switch) {
            // TODO add switch orientation output to hashmap
            try {
                // TODO
                //PLCEngine switchPLC = generateSwitchConflictAvoidanceScript( (Switch) block);
                //switchPLC.setInputSources(inputPool);
                //SafetyCriticalPLCScripts.add(switchPLC);
            } catch (Exception failedToGenerateScript) {
                failedToGenerateScript.printStackTrace();
                System.out.println(String.format("Failure to generate default collision script for switch with block index %d", block.getBlockNum()));
            }
        }

    }

    /** gives this controller access to a pool of PLCInput variables generated by the WaysideSystem
     *
     * updates the pools for all plc scripts on WaysideController
     *
     * @param inputs, the pool of PLC inputs which scripts in this wayside controller may reference
     */
    public void assignInputPool(ArrayList<PLCInput> inputs) {
        this.inputPool = inputs;
        for (PLCEngine safetyScript : SafetyCriticalPLCScripts) {
            safetyScript.setInputSources(inputs);
        }
        for (PLCEngine userScript : UserPLCScripts) {
            userScript.setInputSources(inputs);
        }
    }

    public void giveInput(PLCInput input) {
        this.inputPool.add(input);
        for (PLCEngine safetyScript : SafetyCriticalPLCScripts) {
            safetyScript.registerInputSource(input);
        }
        for (PLCEngine userScript : UserPLCScripts) {
            userScript.registerInputSource(input);
        }
    }


    /** writes the PLC for avoiding collision.
     * blocks are connected to a variable number of other blocks
     *      A one-directional block is only "connected" to one block
     *      A two-directional block is connected to two blocks
     *      A switch is connected to three blocks
     *
     * @param element
     * @return
     */
    public static PLCEngine generateCollisionAvoidanceScript(TrackElement element) throws Exception {
        // Switches have a third connection that need to be considered
        int thisBlockIndex;
        int blockAfterIndex;
        int blockBeforeIndex;
        int switchAfterIndex;
        try {
            thisBlockIndex = element.getBlockNum();
            blockAfterIndex = element.getDirection(0);
            blockBeforeIndex = element.getDirection(1);
            switchAfterIndex = element.getDirection(2);
        } catch (Exception failureToGetDirections) {
            failureToGetDirections.printStackTrace();
            System.out.printf("\"Failed to get directions for track element (index:%d), not going to generate collisin script\"\n",element.getBlockNum());
            return new PLCEngine();
        }
        ArrayList<String> PLCScript;
        if (blockAfterIndex!=0 && blockBeforeIndex==0 && switchAfterIndex==0) {
            // Case: One-Directional
            PLCScript = new ArrayList<>() {
                {
                    add(String.format("LD OCC%d", blockAfterIndex));
                    add(String.format("LD OCC%d", thisBlockIndex));
                    add(String.format("AND"));
                    add(String.format("SET"));
                }
            };
        }  else if (blockAfterIndex!=0 && blockBeforeIndex!=0 && switchAfterIndex==0){
            // Case: Two-Directional
            PLCScript = new ArrayList<>() {
                {
                    add(String.format("LD OCC%d", blockBeforeIndex));
                    add(String.format("LD OCC%d", blockAfterIndex));
                    add(String.format("OR"));
                    add(String.format("LD OCC%d", thisBlockIndex));
                    add(String.format("AND"));
                    add(String.format("SET"));
                }
            };
        } else {
            // Case: Switch
            PLCScript = new ArrayList<>() {
                {
                    add(String.format("LD OCC%d", blockBeforeIndex));
                    add(String.format("LD OCC%d", blockAfterIndex));
                    add(String.format("OR"));
                    add(String.format("LD OCC%d", thisBlockIndex));
                    add(String.format("AND"));
                    add(String.format("SET"));
                }
            };
        }
        AuthorityPLCOutput haltAuthorityOutput = new AuthorityPLCOutput(element, AuthorityPLCOutput.AuthOutRule.HaltWhenTrue);
        PLCEngine collisionAvoidance = new PLCEngine(PLCScript, haltAuthorityOutput);
        // Debug
        System.out.printf(".");
        return collisionAvoidance;
    }



    /*
        Methods for CTC
     */



    /** sets the speed for a TrackElement object if that object is within this controller's jurisdiction.
     *  This function will be called on this controller even if this controller does not have jurisdiction over the particular block,
     *  it only applies the output if it is within its jurisdiction
     *
     *  expected call frequency: only when the CTC makes a new dispatch (changes to current speed/authorities)
     *
     * @param targetBlockIndex, the int index of the target block. Assert: indexes of blocks within the same line (i.e. Green, Red) are unique (no other block within green has the same block index.) indexes are not globally unique
     * @param newCommandedSpeed, the double for the new speed commanded for trains on the block (gives in kilometers/hour, applied to track as meters/second)
     * @throws IOException
     *
     * @before current commanded speed on the track circuit is not up to date
     * @after the commanded speed on the track circuit has been set to the new value if it passes the checks
     */
    public void setBlockSpeed(int targetBlockIndex, double newCommandedSpeed) throws Exception {
        for (TrackElement block : jurisdiction) {
            // only if it is found...
            if(block.getBlockNum() == targetBlockIndex)
                applySpeedToBlock(block,newCommandedSpeed);
        }
    }


    /** performs all tests and filters to a new speed before its value is applied to a block
     *
     * Assert: backwards movement is allowed
     *
     * @param blockObject, the TrackElement block object which we are applying the speed to
     * @param newCommandedSpeed, the new, intended speed for the specified track object
     */
    public static void applySpeedToBlock(TrackElement blockObject, double newCommandedSpeed) throws Exception {
        double maximumForwardSpeed = (double) blockObject.getSpeedLimit();
        double maximumBackwardSpeed = -maximumForwardSpeed;
        if (newCommandedSpeed > maximumForwardSpeed) {
            try {
                blockObject.setCommandedSpeed(maximumForwardSpeed);
                return;
            } catch (Exception failureToApplySpeedToBlock) {
                throw new Exception(String.format("Failure occured when attempted to set speed for track object (index %d) to speed (%f)", blockObject.getBlockNum(), newCommandedSpeed));
            }
        } else if (newCommandedSpeed < maximumBackwardSpeed) {
            try {
                blockObject.setCommandedSpeed(maximumBackwardSpeed);
                return;
            } catch (Exception failureToApplySpeedToBlock) {
                throw new Exception(String.format("Failure occured when attempted to set speed for track object (index %d) to speed (%f)", blockObject.getBlockNum(), newCommandedSpeed));
            }
        }
        // Assert: passes filters if maximumBackwardSpeed < commandedSpeed <= 0 or 0 <= commandedSpeed < maximumForwardSpeed
        else {
            try {blockObject.setCommandedSpeed(newCommandedSpeed); } catch (Exception failureToApplySpeedToBlock) {
                throw new Exception (String.format("Failure occured when attempting to set speed for track object (index %d) to speed (%f)",blockObject.getBlockNum(),newCommandedSpeed));
            }
        }
    }


    /** sets the speed for a TrackElement object if that object is within this controller's jurisdiction.
     *  This function will be called on this controller even if this controller does not have jurisdiction over the particular block,
     *  it only applies the output if it is within its jurisdiction
     *
     *  expected call frequency: only when the CTC makes a new dispatch (changes to current speed/authorities)
     *
     * @param targetBlockIndex, the int index of the target block. Assert: indexes of blocks within the same line (i.e. Green, Red) are unique (no other block within green has the same block index.) indexes are not globally unique
     * @param newAuthority, the double for the new speed commanded for trains on the block (gives in kilometers/hour, applied to track as meters/second)
     * @throws IOException
     */
    public void setBlockAuthority(int targetBlockIndex, int newAuthority) throws Exception {
        // no checks are performed on authority value
        for (TrackElement block : jurisdiction) {
            // only if it is found...
            if(block.getBlockNum() == targetBlockIndex) {
                applyAuthorityToBlock(block, newAuthority);
            }
        }
    }

    /** performs all tests and filters to a new authority before its value is applied to a block
     *
     * Assert: backwards movement is allowed
     *
     * @param blockObject, the TrackElement block object which we are applying the speed to
     * @param newAuthority, the new, intended authority for the specified track object
     */
    public static void applyAuthorityToBlock(TrackElement blockObject, int newAuthority) throws Exception {
        // no checks performed on authority
        try {
            blockObject.setAuthority(newAuthority);
        } catch (Exception failureToApplyValueToBlock) {
            failureToApplyValueToBlock.printStackTrace();
            throw new Exception (String.format("Failure occured when attempting to set authority for track object (index %d) to speed (%d)",blockObject.getBlockNum(),newAuthority));
        }
    }


    /** returns the occupancy of a TrackElement within this controller's jurisdiction.
     *  WaysideSystem ensures that this function is only called whenever the track exists within this controller jurisdiction
     *
     * @param targetBlockIndex
     * @return
     */
    public boolean getOccupancy(int targetBlockIndex) throws Exception {
        // For all blocks under jurisdiction...
        for (TrackElement block : jurisdiction) {
            // If this is the right block...
            if (block.getBlockNum() == targetBlockIndex)
                try {
                    // return it's status
                    return block.getOccupied();
                } catch (Exception failureToGetOccupation) {
                    // Complain if status is unreachable
                    failureToGetOccupation.printStackTrace();
                    throw new Exception(String.format("Failed to retrieve occupation of block (index %d)",targetBlockIndex));
                }
        }
        // If not within jurisdiction, then wayside system has made an error and its lookup-table is outdated
        throw new Exception(String.format("Failure occurred when retrieving occupancy from track system: Wayside Controller (%s) does not contain block index %d.",controllerAlias,targetBlockIndex));
    }


    /** designates a specific block within this controller's jurisdiction to closed
     *
     * @param blockNumber
     * @throws Exception
     */
    public void setClose(int blockNumber) throws Exception {
        int FailureStatusKey_Closed = 4;
        try {
            // no checks are performed on authority value
            // only if it is found...
            for (TrackElement block : jurisdiction) {
                if(block.getBlockNum() == blockNumber) {
                    block.setFailureStatus(FailureStatusKey_Closed);
                }
            }
        } catch (Exception failureToApplyClosureToBlock) {
            failureToApplyClosureToBlock.printStackTrace();
            throw new Exception(String.format("Failure when attempting to apply \"closure\" to TrackElement object (index=%d)\n",blockNumber));
        }
    }


    /** designates a specific block within this controller's jurisdiction to closed
     *
     * @param blockNumber
     * @throws Exception
     */
    public void setOpen(int blockNumber) throws Exception {
        int FailureStatusKey_Open = 0;
        try {
            // no checks are performed on authority value
            // only if it is found...
            for (TrackElement block : jurisdiction) {
                if(block.getBlockNum() == blockNumber) {
                    block.setFailureStatus(FailureStatusKey_Open);
                }
            }
        } catch (Exception failureToReopenBlock) {
            failureToReopenBlock.printStackTrace();
            throw new Exception(String.format("Failure when attempting to apply \"reopen\" TrackElement object (index=%d)\n",blockNumber));
        }
    }


    /** gets the closure status for a specified block within jurisdiction
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public boolean getIsClosed(int blockNumber) throws Exception {
        String IS_CLOSED_KEY = "CLOSED";
        try {
            // no checks are performed on authority value
            // only if it is found...
            for (TrackElement block : jurisdiction) {
                if(block.getBlockNum() == blockNumber) {
                    String status = block.getFailureStatus();
                    return status.equals(IS_CLOSED_KEY);
                }
            }
        } catch (Exception failureToReopenBlock) {
            failureToReopenBlock.printStackTrace();
            throw new Exception(String.format("Failure when attempting to apply \"reopen\" TrackElement object (index=%d)\n",blockNumber));
        }
        return true;
    }

    public double[] getSpeed() {
        // TODO
        return null;
    }
    public int[] getAuthority() {
        // TODO
        return null;
    }

    /** gets the status of a switch from within this controller's jurisdiction
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public boolean getSwitchStatus(int blockNumber) throws Exception {
        try {
            // no checks are performed on authority value
            // only if it is found...
            // only if it is a switch...
            for (TrackElement block : jurisdiction) {
                if(block.getBlockNum() == blockNumber) {
                    if (block instanceof Switch) {
                        Switch sw = (Switch) block;
                        return sw.getIndex();
                    } else {
                        throw new Exception(String.format("Failure when attempting to get switch status of block (index=%d) because specified block is not a switch\n",blockNumber));
                    }
                }
            }
        } catch (Exception failureToReopenBlock) {
            failureToReopenBlock.printStackTrace();
            throw new Exception(String.format("Failure when attempting to get switch status of object (index=%d)\n",blockNumber));
        }
        return true;
    }


    /** sets the orientation of a switch within this controller's jurisdiction (False=Default true=Secondary I believe?)
     *
     * @param blockNumber
     * @param status
     * @throws IOException
     */
    public void setSwitchStatus(int blockNumber, boolean status) throws Exception {
        try {
            // no checks are performed on authority value
            // only if it is found...
            // only if it is a switch...
            for (TrackElement block : jurisdiction) {
                if(block.getBlockNum() == blockNumber) {
                    if (block instanceof Switch) {
                        // Manually override switch
                        // TODO
                        // PLCOutput switchControl = switchPLCControl.get(blockNumber);
                        // switchControl.setManualOverride thing
                        // Set the orientation
                        Switch sw = (Switch) block;
                        sw.setSwitchState(status);
                    } else {
                        throw new Exception(String.format("Failure when attempting to set switch status of block (index=%d) because specified block is not a switch\n",blockNumber));
                    }
                }
            }
        } catch (Exception failureToReopenBlock) {
            failureToReopenBlock.printStackTrace();
            throw new Exception(String.format("Failure when attempting to set switch status of object (index=%d)\n",blockNumber));
        }
    }



    /*
            Get Set
     */

    public void setControllerAlias(String controllerAlias) {this.controllerAlias = controllerAlias;}
    public void setControllerName(String newName){ this.controllerName = newName; }
    public String getControllerAlias(){ return controllerAlias; }
    public String getControllerName() {return controllerName; }
    public ArrayList<TrackElement> getJurisdiction() {return jurisdiction;}



    /*
            GUI Methods
     */



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
//        String inputCategory = "Input Signals";
//        Vector<String> inputVector = new Vector<>();
////        boolean[] inputValues = gpio.getAllInputValues();
////        for(int i=0;i < inputValues.length;i++){
////            inputVector.add("input "+i+" : "+inputValues[i]);
////        }
//        hash.put(inputCategory, inputVector);
//        /*
//            Outputs Node
//         */
//        String outputCategory = "Output Signals";
//        Vector<String> outputVector = new Vector<>();
////        Boolean[] outputValues = gpio.getOutputValues();
////        for(int i=0;i < outputValues.length;i++){
////            outputVector.add("output "+i+" : "+outputValues[i]);
////        }
//        hash.put(outputCategory, outputVector);
//        /*
//            Speed Node
//         */
//        String speedCategory = "Speed";
//        Vector<String> speedVector = new Vector<>();
//        double[] speed = getSpeed();
//        for(int i=0;i < speed.length;i++){
//            speedVector.add("block speed "+i+" : "+speed[i]);
//        }
//        hash.put(speedCategory, speedVector);
//        /*
//            Authority Node
//         */
//        int[] authority = getAuthority();
//        String authorityCategory = "Authority";
//        Vector<String> authorityVector = new Vector<>();
//        for(int i=0;i < authority.length;i++){
//            authorityVector.add("block authority "+i+" : "+authority[i]);
//        }
//        hash.put(authorityCategory, authorityVector);
//        // Return tree format
        return hash;
    }



    /*
        String Representation
     */



    public String toString(){
        String profile = controllerName;
//        if (controllerAlias != null)
//            profile = profile + String.format("[Alias \"%s\"]\n",controllerAlias);
        return profile;
    }
    public String toMedString() {
        String profile = toString();
        profile+= "Jurisdiction: TODO\n";
        return profile;
    }
    public String toLongString() {
        String profile = controllerName;
        if (controllerAlias != null)
            profile += String.format("[Alias \"%s\"]\n",controllerAlias);
        profile += "\n";
        profile+="=====Jurisdiction:\n";
        for (TrackElement block : jurisdiction) {
            profile+= block.toString()+"\n";
        }
        profile+="=====InputPool:\n";
        for(PLCInput input : inputPool) {
            profile+=input.toString()+"\n";
        }
        return profile;
    }
}
