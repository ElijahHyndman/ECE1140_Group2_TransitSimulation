package WaysideController;

//import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.InputValueSwitch;

import TrackConstruction.Switch;
import TrackConstruction.TrackElement;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;

import static java.lang.String.valueOf;

/** module class that compiles PLC boolean logic and applies it to the track system.
 * @author Harsh
 * @editor Elijah
 */
public class WaysideController implements Serializable {
    /***********************************************************************************************************************/
    /** Default Members
     */
    final private boolean DEFAULT_ISACTIVE = true;
    final private boolean DEFAULT_ISSOFTWARE = true;

    /** Controller Information Members
     *
     * @member controllerIndex, the global index of this controller (given based on order of creation)
     * @member controllerName, a deterministic name for this controller given its index
     * @member contorllerAlias, a user-defined name for the controller if desired
     * @member isSoftware, boolean of whether this WaysideController is a software (WaysideController) or hardware (RemoteWaysideController) instance
     */
    private int controllerIndex = ++NUM_CONTROLLERS;
    private String controllerName = String.format("Controller %d",controllerIndex);
    private String controllerAlias = null;
    private GPIO gpio;
    private boolean isActive = DEFAULT_ISACTIVE;
    private boolean isSoftware = DEFAULT_ISSOFTWARE;

    /** Boolean Logic Members
     */
    private HashMap<TrackElement, boolean[][]> outputMap = new HashMap<TrackElement, boolean[][]>(); //overall output map
    private HashMap<TrackElement, List<String>> PLCScriptMap = new HashMap<TrackElement, List<String>>(); //PLC saver output map
    private ArrayList<TrackElement> jurisdiction = new ArrayList<TrackElement>(); //jurisdiction

    /** Test GUI Members
     */
    private HashMap<TrackElement, boolean[]> testInputs = new HashMap<TrackElement, boolean[]>(); //some test inputs!
    private HashMap<TrackElement, Boolean> testOutputs = new HashMap<TrackElement, Boolean>(); //overall output map
    private List<boolean[][]> testTables = new ArrayList<boolean[][]>();
    private List<boolean[][]> outputTables = new ArrayList<boolean[][]>();

    /** Global Members
     */
    private static int NUM_CONTROLLERS = 0;
    /***********************************************************************************************************************/

    /*
        Constructors
     */

    // Default constructor is necessary for RemoteWaysideController inheritance
    protected WaysideController() {
        this.controllerAlias = null;
        gpio = new GPIO(jurisdiction, controllerAlias);
    }

    public WaysideController(String controllerAlias) {
        this.controllerAlias = controllerAlias;
        gpio = new GPIO(null, controllerAlias);
    }

    public WaysideController(ArrayList<TrackElement> jurisdiction, String controllerAlias){
        this.jurisdiction = jurisdiction;
        this.controllerAlias = controllerAlias;
        gpio = new GPIO(jurisdiction, controllerAlias);
    }

    /** copies values from a target WaysideController into this wayside controller
     * @param target    WaysideController, the WaysideController we intend to copy
     */
    public void copy(WaysideController target) {
            this.controllerAlias = target.controllerAlias;
            this.isActive = target.isActive;
            this.isSoftware = target.isSoftware;
            this.outputMap = (HashMap<TrackElement, boolean[][]>) target.outputMap.clone();
            this.PLCScriptMap = (HashMap<TrackElement, List<String>>) target.PLCScriptMap.clone();
            this.gpio.copy(target.gpio);
            this.jurisdiction = (ArrayList<TrackElement>) target.jurisdiction.clone();
            this.testInputs = (HashMap<TrackElement, boolean[]>) target.testInputs.clone();
    }


    /*
        Methods for CTC
     */

    /** sets the commandedSpeed for the blocks under this jurisdiction.
     *
     * @param speeds, an array of commanded speeds where each element corresponds to the commanded speed for an intended block in the jurisdiction
     * @throws IOException, length of speeds array does not match the length of jurisdiction
     */
    public void setSpeed(double[] speeds) throws IOException {
        if(speeds.length != jurisdiction.size()){
            throw new IOException(String.format("setSpeed() error in controller: tried to apply commandedSpeed array (length %d) to controller array (length %d.)", speeds.length,jurisdiction.size()));
        }
        if(speeds.length == 0) {
            System.out.println("Tried to apply setSpeed() to wayside controller using empty speed array");
            return;
        }
        for(int i = 0; i < jurisdiction.size(); i++){
            jurisdiction.get(i).setCommandedSpeed(speeds[i]);
        }
    }

    /*
    sets the speed for all blocks within the jurisdiction
     */
    public void setSpeed(int blockNumber, double speeds) throws IOException {
        TrackElement trackElement = getBlockElement(blockNumber);
        double speedLimit = (double) trackElement.getSpeedLimit();

        if(speeds > speedLimit){
            speeds = speedLimit;
        }

        jurisdiction.get(jurisdiction.indexOf(trackElement)).setCommandedSpeed(speeds);
    }

    /*
    gets the speed for all blocks within the jurisdiction
     */
    public double[] getSpeed() {
        double[] speeds = new double[jurisdiction.size()];

        for(int i = 0; i < jurisdiction.size(); i++){
            speeds[i] = jurisdiction.get(i).getCommandedSpeed();
        }

        return speeds;
    }

    /*
    sets the authority for all blocks within the jurisdiction
     */
    public void setAuthority(int[] authorities) throws IOException {
        if(authorities.length != jurisdiction.size()){
            throw new IOException("Controller Error: There are too many/too few input authories values...");
        }

        for(int i = 0; i < jurisdiction.size(); i++){
            jurisdiction.get(i).setAuthority(authorities[i]);
        }
    }

    public void setAuthority(int blockNumber, int authority) throws IOException {
        TrackElement trackElement = getBlockElement(blockNumber);
        jurisdiction.get(jurisdiction.indexOf(trackElement)).setAuthority(authority);
    }

    /*
    gets the authority for all blocks within the jurisdiction
     */
    // TODO make this take ints instead of doubles
    public int[] getAuthority() {
        int[] authority = new int[jurisdiction.size()];

        for(int i = 0; i < jurisdiction.size(); i++){
            authority[i] = jurisdiction.get(i).getAuthority();
        }

        return authority;
    }

    /*

     */
    public boolean getSwitchStatus(int blockNumber) throws IOException {
        Switch aSwitch = (Switch) getBlockElement(blockNumber);
        return aSwitch.getIndex();
    }

    /*

     */
    public void setSwitchStatus(int blockNumber, boolean status) throws IOException {
        Switch aSwitch = (Switch) getBlockElement(blockNumber);
        aSwitch.setSwitchState(status);
    }

    /*
    sets block close
     */
    public void setClose(int blockNumber) throws IOException {
        TrackElement trackElement = getBlockElement(blockNumber);

        //trackElement.setFailureStatus();
    }

    /*
    add an output under the jurisdiction of this controller, a PLCFile MUST be associated during creation. It can be later updated too!
     */
    public void addOutput(int blockNumber, String PLCFile) throws IOException, URISyntaxException {
        boolean bool;
        TrackElement trackElement = getBlockElement(blockNumber);
        PLCEngine engine = new PLCEngine();
        //List<String> fileTokens = engine.stringTokensFromFile(PLCFile);
        // TODO
        List<String> fileTokens = null;
        try {
            engine.uploadPLC(fileTokens);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PLCScriptMap.put(trackElement, engine.getPLCStringList());
        outputMap.put(trackElement, engine.generateLogicTable(getAllInputNames()));
        addTestInput(trackElement);

        gpio.addOutput(trackElement, false);
        bool = generateOutputSignal(trackElement.getBlockNum(), false);
        gpio.addOutput(trackElement, bool);
    }

    /*
    Updates the current block number with a new PLCFile
     */
    public void updateOutput(int blockNumber, String PLCFile) throws IOException, URISyntaxException {
        boolean bool;
        TrackElement trackElement = getBlockElement(blockNumber);
        PLCEngine engine = new PLCEngine();
        //List<String> fileTokens = engine.stringTokensFromFile(PLCFile);
        // TODO
        List<String> fileTokens = null;
        try {
            engine.uploadPLC(fileTokens);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PLCScriptMap.replace(trackElement, engine.getPLCStringList());
        outputMap.replace(trackElement, engine.generateLogicTable(getInputNames()));

        bool = generateOutputSignal(trackElement.getBlockNum(), false);
        gpio.updateOutput(trackElement, bool);
    }

    /*
    Updates the test current block number with a new PLCFile
     */
    public void updateTestOutput() throws IOException, URISyntaxException {
//        for(int i=0;i < outputMap.size();i++){
//            outputMap.
//        }
//
//        PLCScriptMap.replace(trackElement, engine.getPLCString());
//        outputMap.replace(trackElement, engine.calculateOutputMapNew(getInputNames()));
//
//        bool = generateOutputSignal(trackElement.getBlockNum(), false);
//        gpio.updateOutput(trackElement, bool);
    }

    /*
    Generates an output single for a specific block number
     */
    public boolean generateOutputSignal(int blockNumber, boolean isTest) throws IOException { //or any unique identifier, change for other iterations...
        TrackElement trackElement = getBlockElement(blockNumber);
        boolean[][] outputs = outputMap.get(trackElement);

        if(outputs == null){
            throw new IOException("Controller Error: The element doesn't exist or bad block number!");
        }

        boolean[][] searchMap = new boolean[outputs.length][outputs[0].length-1];
        boolean[] inputValues = gpio.getAllInputValues();

        for(int i=0;i < outputs.length;i++){
            for(int j=0;j < outputs[0].length-1;j++)
                searchMap[i][j] = outputs[i][j];
        }

        boolean matched;
        int index = -1;
        for(int i=0;i < searchMap.length;i++){
            matched = true;
            for(int j = 0;j < searchMap[i].length;j++){
                if(searchMap[i][j] != inputValues[j]){
                    matched = false;
                    break;
                }
            }

            if(matched){
                index = i;
                break;
            }
        }

        if(index == -1){
            throw new IOException("Generate Error: Bad inputs...");
        }

        if(isTest){
            testOutputs.put(trackElement, outputs[index][outputs[0].length-1]);
        }else{
            //updates the gpio object with the new values
            gpio.updateOutput(trackElement, outputs[index][outputs[0].length-1]);
        }

        //returns the updated value too!
        return outputs[index][outputs[0].length-1];
    }

    public void generateOutputSignals() {
        boolean isTest = false;
        // Update all known outputs
        for(TrackElement block : jurisdiction) {
            try {
                generateOutputSignal(block.getBlockNum(), isTest);
            } catch(Exception e) {
                // was not able to generate output signal for this block, move on
            }
        }
    }

    public void run() {

    }

    /*
        GUI Functions
     */

    /**
     * @return
     */
    public List<String> getAllNames() throws IOException {
        List<String> temp = new LinkedList<>();

        temp.addAll(getInputNames());
        for(int i = 0; i < jurisdiction.size(); i++){
            temp.add("speed " + i + " : ");
        }
        for(int i = 0; i < jurisdiction.size(); i++){
            temp.add("authority " + i + " : ");
        }
        temp.add("isActive");

        return temp;
    }

    /**
     * @return
     * @throws IOException
     */
    public List<Object> getAllData() throws IOException {
        List<Object> temp = new LinkedList<>();

        temp.addAll(Collections.singleton(gpio.getAllInputValues()));
        temp.addAll(Collections.singleton(getSpeed()));
        temp.addAll(Collections.singleton(getAuthority()));
        temp.addAll(Collections.singleton(isActive));

        return temp;
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
            Inputs
         */
        String inputCategory = "Input Signals";
        Vector<String> inputVector = new Vector<>();
        boolean[] inputValues = gpio.getAllInputValues();
        for(int i=0;i < inputValues.length;i++){
            inputVector.add("input "+i+" : "+inputValues[i]);
        }
        hash.put(inputCategory, inputVector);

        /*
            Outputs
         */
        String outputCategory = "Output Signals";
        Vector<String> outputVector = new Vector<>();
        Boolean[] outputValues = gpio.getOutputValues();
        for(int i=0;i < outputValues.length;i++){
            outputVector.add("output "+i+" : "+outputValues[i]);
        }
        hash.put(outputCategory, outputVector);

        /*
            Speed
         */
        String speedCategory = "Speed";
        Vector<String> speedVector = new Vector<>();
        double[] speed = getSpeed();
        for(int i=0;i < speed.length;i++){
            speedVector.add("block speed "+i+" : "+speed[i]);
        }
        hash.put(speedCategory, speedVector);

        /*
            Authority
         */
        int[] authority = getAuthority();
        String authorityCategory = "Authority";
        Vector<String> authorityVector = new Vector<>();
        for(int i=0;i < authority.length;i++){
            authorityVector.add("block authority "+i+" : "+authority[i]);
        }
        hash.put(authorityCategory, authorityVector);

        String activeCategory = "Active";
        Vector<String> activeVector = new Vector<>();
        activeVector.add("Controller status : " + isActive);
        hash.put(activeCategory, activeVector);

        return hash;
    }

    /*
        Testing Methods
     */
    public void addTestInput(TrackElement trackElement){
        boolean[] totalInputs = new boolean[gpio.getNumberOfBlocks()];

        for(int i = 0; i < jurisdiction.size(); i++){
            totalInputs[i] = false;
        }

        testInputs.put(trackElement, totalInputs);
    }

    /*
    NEEDS TO BE UPDATED FOR CURRENT CONTROLLER!
     */
    public boolean updateTestInputs(String valueString, int index){
        boolean temp;

        if(valueString.equals("false")){
            temp = false;
        }else if(valueString.equals("true")){
            temp = true;
        }else{
            return false; //doesn't work
        }

        boolean[] inputValues = gpio.getAllInputValues();
        for(int i=0;i < inputValues.length;i++){
            if(index == i){
                inputValues[i] = temp;
                break;
            }
        }
        return true;
    }


    public GPIO getGPIO(){
        return gpio;
    }
    public void setControllerAlias(String controllerAlias) {this.controllerAlias = controllerAlias;}
    public void setControllerName(String newName){ this.controllerAlias = newName; }
    public List<String> getInputNames(){
        List<String> inputNames = new LinkedList<>();

        for(int i = 0; i < jurisdiction.size(); i++){
            inputNames.add(valueOf(jurisdiction.get(i).getBlockNum()));
        }

        return inputNames;
    }
    public List<String> getAllInputNames(){
        List<String> inputNames = new LinkedList<>();

        for(int i = 0; i < jurisdiction.size(); i++){
            inputNames.add(valueOf(jurisdiction.get(i).getBlockNum()));
        }

        return inputNames;
    }
    public String getControllerAlias(){ return controllerAlias; }
    public TrackElement getBlockElement(int blockNumber) throws IOException {
        for(int i = 0; i < jurisdiction.size(); i++){
            if(blockNumber == jurisdiction.get(i).getBlockNum()){
                return jurisdiction.get(i);
            }
        }

        throw new IOException("Controller Error: No block with that number in controller - " + controllerAlias +  " " + Integer.toString(blockNumber));
    }
    public TrackElement getTrackElement(TrackElement trackElement) {
        // TODO implement
        return new TrackElement();
    }

    /*
        String Representation
     */
    public String toString(){
        return controllerName;
    }
    public String toMedString() {
        String profile = String.format("Wayside Controller: %s\n", controllerName);
        profile += String.format("Alias: %s\n",controllerAlias);
        profile += String.format("isActive: %b\n",isActive);
        profile += String.format("isSoftware: %b\n",isSoftware);
        profile += String.format("outputMap: %s\n",outputMap);
        profile += String.format("PLCScriptMap: %s\n",PLCScriptMap);
        return profile;
    }
}
