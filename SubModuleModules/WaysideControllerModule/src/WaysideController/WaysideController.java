package WaysideController;

//import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.InputValueSwitch;

import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static java.lang.String.valueOf;

public class WaysideController {

    //overall system defaults
    final private int DEFAULT_SPEEDLIMIT = 50;
    final private boolean DEFAULT_ISACTIVE = true;
    final private boolean DEFAULT_ISSOFTWARE = true;

    //controller information and statuses
    String name;
    GPIO gpio;
    boolean isActive;
    boolean isSoftware;
    int speedLimit;

    //input/output related data
    HashMap<TrackElement, boolean[][]> outputMap; //overall output map
    HashMap<TrackElement, List<String>> PLCScriptMap; //PLC saver output map
    ArrayList<TrackBlock> blocks; //jurisdiction
    ArrayList<TrackElement> allBlocks; //jurisdiction

    //testInputs/testOutputs
    HashMap<TrackElement, boolean[]> testInputs; //some test inputs!
    HashMap<TrackElement, Boolean> testOutputs; //overall output map

    private List<boolean[][]> testTables;
    private List<boolean[][]> outputTables;

    //not usable right now...
    public WaysideController(){
        name = "FAKE";
    }

    public WaysideController(ArrayList<TrackElement> allBlocks, ArrayList<TrackBlock> blocks, String name){
        this.isActive = DEFAULT_ISACTIVE;
        this.isSoftware = DEFAULT_ISSOFTWARE;
        this.speedLimit = DEFAULT_SPEEDLIMIT;
        this.PLCScriptMap = new HashMap<>();
        this.outputMap = new HashMap<>();
        this.testInputs = new HashMap<>();
        this.blocks = blocks;
        this.allBlocks = allBlocks;
        this.name = name;

        gpio = new GPIO(allBlocks, blocks, name);
    }

    /*
    Getters and setters for the name
     */
    public String getName(){ return name; }
    public void setName(String newName){ this.name = newName; }

    /*
    sets the speed for all blocks within the jurisdiction
     */
    public void setSpeed(double[] speeds) throws IOException {
        if(speeds.length != blocks.size()){
            throw new IOException("Controller Error: There are too many/too few input speed values...");
        }

        for(int i=0;i < blocks.size();i++){
            blocks.get(i).setCommandedSpeed(speeds[i]);
        }
    }

    /*
    sets the speed for all blocks within the jurisdiction
     */
    public void setSpeed(int blockNumber, double speeds) throws IOException {
        TrackElement trackElement = getBlockElement(blockNumber);
        allBlocks.get(allBlocks.indexOf(trackElement)).setCommandedSpeed(speeds);
    }

    /*
    gets the speed for all blocks within the jurisdiction
     */
    public double[] getSpeed() {
        double[] speeds = new double[blocks.size()];

        for(int i=0;i < blocks.size();i++){
            speeds[i] = blocks.get(i).getCommandedSpeed();
        }

        return speeds;
    }

    /*
    sets the authority for all blocks within the jurisdiction
     */
    public void setAuthority(int[] authorities) throws IOException {
        if(authorities.length != blocks.size()){
            throw new IOException("Controller Error: There are too many/too few input authories values...");
        }

        for(int i=0;i < blocks.size();i++){
            blocks.get(i).setAuthority(authorities[i]);
        }
    }

    public void setAuthority(int blockNumber, int authority) throws IOException {
        TrackElement trackElement = getBlockElement(blockNumber);
        allBlocks.get(allBlocks.indexOf(trackElement)).setAuthority(authority);
    }

    /*
    gets the authority for all blocks within the jurisdiction
     */
    public double[] getAuthority() {
        double[] authority = new double[blocks.size()];

        for(int i=0;i < blocks.size();i++){
            authority[i] = blocks.get(i).getAuthority();
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
        engine.createTokens(PLCFile);

        PLCScriptMap.put(trackElement, engine.getPLCString());
        outputMap.put(trackElement, engine.calculateOutputMapNew(getAllInputNames()));
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
        engine.createTokens(PLCFile);

        PLCScriptMap.replace(trackElement, engine.getPLCString());
        outputMap.replace(trackElement, engine.calculateOutputMapNew(getInputNames()));

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
        boolean[][] searchMap = new boolean[outputs.length][outputs[0].length-1];
        boolean[] inputValues = gpio.getAllInputValues();

        if(outputs == null){
            throw new IOException("Controller Error: The element doesn't exist or bad block number!");
        }

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

    //GUI **************************************************************************************************************
    /*
    Gets all the names for each of the following variables - inputValues, speed, authority, isActive for GUI
     */
    public List<String> getAllNames() throws IOException {
        List<String> temp = new LinkedList<>();

        temp.addAll(getInputNames());
        for(int i=0;i < blocks.size();i++){
            temp.add("speed " + i + " : ");
        }
        for(int i=0;i < blocks.size();i++){
            temp.add("authority " + i + " : ");
        }
        temp.add("isActive");

        return temp;
    }

    public List<Object> getAllData() throws IOException {
        List<Object> temp = new LinkedList<>();

        temp.addAll(Collections.singleton(gpio.getInputValues()));
        temp.addAll(Collections.singleton(getSpeed()));
        temp.addAll(Collections.singleton(getAuthority()));
        temp.addAll(Collections.singleton(isActive));

        return temp;
    }

    public HashMap<String, Vector<String>> generateDescriptionNodes() {
        HashMap<String, Vector<String>> hash = new HashMap<String, Vector<String>>();

        //inputs - input signals
        String inputCategory = "Input Signals";
        Vector<String> inputVector = new Vector<>();
        boolean[] inputValues = gpio.getInputValues();
        for(int i=0;i < inputValues.length;i++){
            inputVector.add("input "+i+" : "+inputValues[i]);
        }
        hash.put(inputCategory, inputVector);

        //outputs - output signals, speed, authority, active
        String outputCategory = "Output Signals";
        Vector<String> outputVector = new Vector<>();
        Boolean[] outputValues = gpio.getOutputValues();
        for(int i=0;i < outputValues.length;i++){
            outputVector.add("output "+i+" : "+outputValues[i]);
        }
        hash.put(outputCategory, outputVector);

        String speedCategory = "Speed";
        Vector<String> speedVector = new Vector<>();
        double[] speed = getSpeed();
        for(int i=0;i < speed.length;i++){
            speedVector.add("block speed "+i+" : "+speed[i]);
        }
        hash.put(speedCategory, speedVector);

        double[] authority = getAuthority();
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

    //Testing **********************************************************************************************************
    /*
    add,get,set TestInputs
     */
    public void addTestInput(TrackElement trackElement){
        boolean[] totalInputs = new boolean[gpio.getNumberOfBlocks()];

        for(int i=0;i < blocks.size();i++){
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

        boolean[] inputValues = gpio.getInputValues();
        for(int i=0;i < inputValues.length;i++){
            if(index == i){
                inputValues[i] = temp;
                break;
            }
        }

//        for(int i=0;i < outputTables.size();i++){
//            updateOutputSignal(outputTableDevices.get(i));
//        }

        return true;
    }

    //GPIO *************************************************************************************************************
    /*
    Gets the GPIO//GPIO functions!
     */
    public GPIO getGPIO(){
        return gpio;
    }

    //Helper Functions *************************************************************************************************
    /*
    helper function - gets all the inputs of the blocks
     */
    public List<String> getInputNames(){
        List<String> inputNames = new LinkedList<>();

        for(int i=0;i < blocks.size();i++){
            inputNames.add(valueOf(blocks.get(i).getBlockNum()));
        }

        return inputNames;
    }

    /*
    helper function - gets all the inputs of the blocks
     */
    public List<String> getAllInputNames(){
        List<String> inputNames = new LinkedList<>();

        for(int i=0;i < allBlocks.size();i++){
            inputNames.add(valueOf(allBlocks.get(i).getBlockNum()));
        }

        return inputNames;
    }

    /*
    helper function - finds the specific block element from the block number
     */
    public TrackElement getBlockElement(int blockNumber) throws IOException {
        for(int i = 0; i < allBlocks.size(); i++){
            if(blockNumber == allBlocks.get(i).getBlockNum()){
                return allBlocks.get(i);
            }
        }

        throw new IOException("Controller Error: No block with that number in controller - " + name);
    }
}
