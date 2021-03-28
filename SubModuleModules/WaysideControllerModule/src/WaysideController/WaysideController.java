package WaysideController;

//import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.InputValueSwitch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class WaysideController {

    //overall system defaults
    final private int DEFAULT_SPEEDLIMIT = 50;
    final private int DEFAULT_SPEED= 0;
    final private int DEFAULT_AUTHORITY = 1;
    final private boolean DEFAULT_STATUS = true;
    final private String DEFAULT_LINE = "BLUE";
    final private int DEFAULT_OUTPUTS = 0;
    final private int DEFAULT_BLOCKS = 1;

    String name;

    //inputs from the blocks that are a part of this system (these are placeholders until the track is made)
    private List<String> inputNames;
    private boolean[] inputValues;

    //outputs and their respective devices
    private List<String> outputTableDevices;
    private List<boolean[][]> outputTables;
    private List<Boolean> outputValues;

    //values to monitor
    private int speedLimit;
    private int[] speed;
    private int[] authority;
    private boolean isActive;

    //all connections
    private int[] blocks; //values corresponds to value in track model and index references to speed in arr
    private String currentLine;
    private boolean isSoftware;
    private List<String> PLCScript;

    public WaysideController(){
        name = "BASE";

        GPIO gpio = new GPIO();
        this.inputNames = new LinkedList<>();
        this.inputValues = new boolean[0];

        this.outputTableDevices = new LinkedList<>();
        this.outputTables = new LinkedList<>();
        this.outputValues = new LinkedList<>();

        this.blocks = new int[0];
        this.currentLine = DEFAULT_LINE;

        this.speedLimit = DEFAULT_SPEEDLIMIT;
        this.speed = new int[0];
        for(int i=0;i < blocks.length;i++){
            speed[i] = DEFAULT_SPEED;
        }
        this.authority = new int[0];
        for(int i=0;i < blocks.length;i++){
            authority[i] = DEFAULT_AUTHORITY;
        }
        this.isActive = DEFAULT_STATUS;
        isSoftware = true;
    }
    
    public WaysideController(int[] blocks, String currentLine, String name){
        this.name = name;

        this.inputNames = new LinkedList<>();
        this.inputValues = new boolean[0];

        this.outputTableDevices = new LinkedList<>();
        this.outputTables = new LinkedList<>();
        this.outputValues = new LinkedList<>();

        this.blocks = blocks;
        this.currentLine = currentLine;

        this.speedLimit = DEFAULT_SPEEDLIMIT;
        this.speed = new int[blocks.length];
        for(int i=0;i < blocks.length;i++){
            speed[i] = DEFAULT_SPEED;
        }
        this.authority = new int[blocks.length];
        for(int i=0;i < blocks.length;i++){
            authority[i] = DEFAULT_AUTHORITY;
        }
        this.isActive = DEFAULT_STATUS;
        isSoftware = true;
    }

    public WaysideController(int[] blocks, String currentLine, List<String> inputNames, boolean[] inputValues, String name){
        this.name = name;

        this.inputNames = inputNames;
        this.inputValues = inputValues;

        this.outputTableDevices = new LinkedList<>();
        this.outputTables = new LinkedList<>();
        this.outputValues = new LinkedList<>();

        this.blocks = blocks;
        this.currentLine = currentLine;

        this.speedLimit = DEFAULT_SPEEDLIMIT;
        this.speed = new int[blocks.length];
        for(int i=0;i < blocks.length;i++){
            speed[i] = DEFAULT_SPEED;
        }
        this.authority = new int[blocks.length];
        for(int i=0;i < blocks.length;i++){
            authority[i] = DEFAULT_AUTHORITY;
        }
        this.isActive = DEFAULT_STATUS;
        isSoftware = true;
    }

    public boolean containsInput(String str) throws IOException {
        if(!inputNames.contains(str)){
            throw new IOException("Generate Error: The controller input doesn't exist...");
        }
        return true;
    }

    public boolean getInputValue(String name) throws IOException {
        if(!inputNames.contains(name)){
            throw new IOException("Generate Error: The controller input doesn't exist...");
        }
        return inputValues[inputNames.indexOf(name)];
    }

    public void recalculatePLC(){
        //order of the inputs sent will return a table with the inputs in the same order

    }

    //helper functions
    public int checkSuggestedSpeed(int speed){
        if(speed > speedLimit){
            return speedLimit;
        }else{
            return speed;
        }
    }

    //no real way to have a safety net for authority (don't have the track model to check next blocks+...)
    public int checkSuggestedAuthority(int authority){
//        for(int i = 0;i < 4; i++){
//
//        }

        return authority;
    }

    public void addOutputSignal(String PLCFile, String outputType) throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();
        engine.createTokens(PLCFile);
        PLCScript = engine.getPLCString();

        outputTableDevices.add(outputType);
        outputTables.add(engine.calculateOutputMapNew(inputNames));
    }

    public void generateOutputSignal(String outputType) throws IOException {
        int outputIndex;
        boolean[][] outputMap;
        boolean[][] searchMap;

        outputIndex = outputTableDevices.indexOf(outputType);
        outputMap = outputTables.get(outputIndex);
        searchMap = new boolean[outputMap.length][outputMap[0].length-1];

//        if(inputValues.length != searchMap.length){
//            throw new IOException("Generate Error: Input values don't match the search map...");
//        }

        for(int i=0;i < outputMap.length;i++){
            for(int j=0;j < outputMap[0].length-1;j++)
            searchMap[i][j] = outputMap[i][j];
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
        outputValues.add(outputIndex, outputMap[index][outputMap[0].length-1]);
    }

    public void updateOutputSignal(String outputType) throws IOException {
        int outputIndex;
        boolean[][] outputMap;
        boolean[][] searchMap;

        outputIndex = outputTableDevices.indexOf(outputType);
        outputMap = outputTables.get(outputIndex);
        searchMap = new boolean[outputMap.length][outputMap[0].length-1];

//        if(inputValues.length != searchMap.length){
//            throw new IOException("Generate Error: Input values don't match the search map...");
//        }

        for(int i=0;i < outputMap.length;i++){
            for(int j=0;j < outputMap[0].length-1;j++)
                searchMap[i][j] = outputMap[i][j];
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
        outputValues.set(outputIndex, outputMap[index][outputMap[0].length-1]);
    }

    //will update the inputs from the blocks in the future, this will trigger if a block in the area is changed
    public void updateInputs(boolean[] values) throws IOException {
        inputValues = values;

        for(int i=0;i < outputTableDevices.size();i++){
            updateOutputSignal(outputTableDevices.get(i));
        }
    }

    public List<String> getAllNames(){
        List<String> temp = new LinkedList<>();

        temp.addAll(inputNames);
        for(int i=0;i < speed.length;i++){
            temp.add("speed " + i + " : ");
        }
        for(int i=0;i < authority.length;i++){
            temp.add("authority " + i + " : ");
        }
        temp.add("isActive");

        return temp;
    }

    public List<Object> getAllData(){
        List<Object> temp = new LinkedList<>();

        temp.addAll(Collections.singleton(inputValues));
        temp.addAll(Collections.singleton(speed));
        temp.addAll(Collections.singleton(authority));
        temp.addAll(Collections.singleton(isActive));

        return temp;
    }

    public boolean updateInputs(String valueString, int index) throws IOException {
        boolean temp;

        if(valueString.equals("false")){
            temp = false;
        }else if(valueString.equals("true")){
            temp = true;
        }else{
            return false; //doesn't work
        }

        for(int i=0;i < inputValues.length;i++){
            if(index == i){
                inputValues[i] = temp;
                break;
            }
        }

        for(int i=0;i < outputTables.size();i++){
            updateOutputSignal(outputTableDevices.get(i));
        }

        return true;
    }

    public int[] getSpeedAllBlocks(){
        return speed;
    }

    public void setSpeedAllBlocks(int newSpeed){
        for(int i=0;i < speed.length;i++){
            speed[i] = checkSuggestedSpeed(newSpeed);
        }
    }

    public int[] getAuthorityAllBlocks(){
        return authority;
    }

    public void setAuthorityAllBlocks(int newAuthority){
        for(int i=0;i < authority.length;i++){
            authority[i] = checkSuggestedAuthority(newAuthority);
        }
    }

    public void setSpeedPerBlock(int speed, int block){
        //speed;
    }

    public int getSpeedPerBlock(int speed, int block){
        return speed;
    }

    public HashMap<String, Vector<String>> generateDescriptionNodes() {
        HashMap<String, Vector<String>> hash = new HashMap<String, Vector<String>>();

        //inputs - input signals
        String inputCategory = "Input Signals";
        Vector<String> inputVector = new Vector<>();
        for(int i=0;i < inputValues.length;i++){
            inputVector.add("input "+i+" : "+inputValues[i]);
        }
        hash.put(inputCategory, inputVector);

        //outputs - output signals, speed, authority, active
        String outputCategory = "Output Signals";
        Vector<String> outputVector = new Vector<>();
        for(int i=0;i < outputValues.size();i++){
            outputVector.add("output "+i+" : "+outputValues.get(i));
        }
        hash.put(outputCategory, outputVector);

        String speedCategory = "Speed";
        Vector<String> speedVector = new Vector<>();
        for(int i=0;i < speed.length;i++){
            speedVector.add("block speed "+i+" : "+speed[i]);
        }
        hash.put(speedCategory, speedVector);

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

    //GETTERS AND SETTERS
    //these values will be received from the TRACK MODEL, CTC or set beforehand
    public void setInputNames(List<String> inputNames) { this.inputNames = inputNames; }
    public List<String> getInputNames(){ return inputNames; }

    public void setInputValues(boolean[] inputValues) { this.inputValues = inputValues; }
    public boolean[] getInputValues(){ return inputValues; }

    public void setSpeedLimit(int speedLimit){ this.speedLimit = speedLimit; }
    public int getSpeedLimit(){ return speedLimit; }

    public void setAllSpeed(int[] speed){ this.speed = speed; }
    public int[] getAllSpeed(){ return speed; }
    
    public void setAuthority(int[] authority) { this.authority = authority; }
    public int[] getAuthority() { return authority; }

    public void setActive(boolean isActive) { this.isActive = isActive; }
    public boolean getActive() { return isActive; }

    public boolean[] getOutputValues() {
        boolean[] outputs = new boolean[outputValues.size()];
        for(int i=0;i < outputs.length;i++){
            outputs[i] = outputValues.get(i);
        }
        return outputs;
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
    public List<String> getOutputNames() { return outputTableDevices;}
    public String toString() { return this.name; }
}
