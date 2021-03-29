package WaysideController;

import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static java.lang.String.valueOf;

public class Controller {

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
    private List<boolean[][]> outputTables;

    public Controller(){
        name = "FAKE";
    }

    public Controller(ArrayList<TrackBlock> blocks, String name){
        this.isActive = DEFAULT_ISACTIVE;
        this.isSoftware = DEFAULT_ISSOFTWARE;
        this.speedLimit = DEFAULT_SPEEDLIMIT;
        this.blocks = blocks;
        this.name = name;

        gpio = new GPIO(blocks, name);
    }

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
    gets the speed for all blocks within the jurisdiction
     */
    public double[] getSpeed() throws IOException {
        double[] speeds = new double[blocks.size()];

        for(int i=0;i < blocks.size();i++){
            speeds[i] = blocks.get(i).getCommandedSpeed();
        }

        return speeds;
    }

    /*
    sets the authority for all blocks within the jurisdiction
     */
    public void setAuthority(double[] authorities) throws IOException {
        if(authorities.length != blocks.size()){
            throw new IOException("Controller Error: There are too many/too few input authories values...");
        }

        for(int i=0;i < blocks.size();i++){
            blocks.get(i).setAuthority(authorities[i]);
        }
    }

    /*
    gets the authority for all blocks within the jurisdiction
     */
    public double[] getAuthority() throws IOException {
        double[] authority = new double[blocks.size()];

        for(int i=0;i < blocks.size();i++){
            authority[i] = blocks.get(i).getAuthority();
        }

        return authority;
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
        outputMap.put(trackElement, engine.calculateOutputMapNew(getInputNames()));

        bool = generateOutputSignal(trackElement.getBlockNum());
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

        PLCScriptMap.put(trackElement, engine.getPLCString());
        outputMap.put(trackElement, engine.calculateOutputMapNew(getInputNames()));

        bool = generateOutputSignal(trackElement.getBlockNum());
        gpio.updateOutput(trackElement, bool);
    }

    /*
    Generates an output single for a specific block number
     */
    public boolean generateOutputSignal(int blockNumber) throws IOException { //or any unique identifier, change for other iterations...
        TrackElement element = getBlockElement(blockNumber);
        boolean[][] outputs = outputMap.get(element);
        boolean[][] searchMap = new boolean[outputs.length][outputs[0].length-1];
        boolean[] inputValues = gpio.getInputValues();

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

        //updates the gpio object with the new values
        gpio.updateOutput(element, outputs[index][outputs[0].length-1]);

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
    helper function - finds the specific block element from the block number
     */
    public TrackElement getBlockElement(int blockNumber) throws IOException {
        for(int i=0;i < blocks.size();i++){
            if(blockNumber == blocks.get(i).getBlockNum()){
                return blocks.get(i);
            }
        }

        throw new IOException("Controller Error: No block with that number in controller - " + name);
    }

    /*
    helper function - finds the specific block element from the block number
     */
    public int getBlockNumber(TrackElement trackElement) throws IOException {
        for(int i=0;i < blocks.size();i++){
            if(trackElement == blocks.get(i)){
                return blocks.get(i).getBlockNum();
            }
        }

        throw new IOException("Controller Error: No block with that number in controller - " + name);
    }
}
