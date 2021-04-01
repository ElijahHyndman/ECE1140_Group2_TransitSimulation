package WaysideController;

import WaysideGUI.WaysideUIClass;
import TrackConstruction.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GPIO {

    //int numberOfBlocks; //total blocks to read from
    int numberOfOutputs;
    String controllerName;

    ArrayList<TrackElement> allBlocks;  //jurisdiction
    //ArrayList<TrackBlock> blocks;  //jurisdiction
    HashMap<TrackElement, Boolean> outputValues;

    public GPIO(){
        //blocks = new ArrayList<>();
        outputValues = new HashMap<>();
        //numberOfBlocks = 0;
    }

    public GPIO(ArrayList<TrackElement> allBlocks, String controllerName){
        this.allBlocks = allBlocks;
        //this.blocks = blocks;
        this.controllerName = controllerName;
        outputValues = new HashMap<>();
        //numberOfBlocks = this.blocks.size();
        numberOfOutputs = outputValues.size();
    }

    public int getNumberOfBlocks() {
        return allBlocks.size();
    }

    /*
        Gets the values from the occupied blocks - DEPRECATED!
         */
//    public boolean[] getInputValues(){
//        boolean[] inputs = new boolean[blocks.size()];
//
//        for(int i=0;i < blocks.size();i++){
//            inputs[i] = blocks.get(i).getOccupied();
//        }
//
//        return inputs;
//    }

    public boolean[] getAllInputValues(){
        boolean[] inputs = new boolean[allBlocks.size()];

        for(int i=0;i < allBlocks.size();i++){
            inputs[i] = allBlocks.get(i).getOccupied();
        }

        return inputs;
    }

    public boolean getOccupancy(int blockNumber) throws IOException {
        TrackElement trackElement = getBlockElement(blockNumber);

        return trackElement.getOccupied();
    }


    /*
    Generates an output, requires some value by default
     */
    public void addOutput(TrackElement trackElement, boolean defaultOutputValue) throws IOException {
        if(trackElement.getType().equalsIgnoreCase("switch")){ //needs a way to check if the trackelement is the correct one without the string
            Switch trackSwitch = (Switch) trackElement;
            outputValues.put(trackElement, defaultOutputValue);
            trackSwitch.setSwitchState(defaultOutputValue);
            //numberOfBlocks++;
        }else{
            throw new IOException("GPIO Error: Bad Output Type!");
        }
    }

    /*
    updates an output, throws error if there is no PLC(no way to generate that output value)
     */
    public void updateOutput(TrackElement trackElement, boolean value) throws IOException {
        if(!outputValues.containsKey(trackElement)){
            throw new IOException("GPIO Error: Bad Track Element!");
        }

        if(trackElement.getType().equalsIgnoreCase("switch")){ //needs a way to check if the trackelement is the correct one without the string
            Switch trackSwitch = (Switch) trackElement;
            outputValues.replace(trackElement, value);
            trackSwitch.setSwitchState(value);
            //numberOfBlocks++;
        }else{
            throw new IOException("GPIO Error: Bad Output Type!");
        }
    }

    /*
    Gets an output from the track element
     */
    public boolean getOutput(TrackElement trackElement) throws IOException {
        return outputValues.get(trackElement);
    }

    /*
    Gets an output from the block number
     */
    public boolean getOutput(int blockNumber) throws IOException {
        TrackElement temp = null;

        for(int i=0;i < allBlocks.size();i++){
            if(blockNumber == allBlocks.get(i).getBlockNum()){
                temp = allBlocks.get(i);
                break;
            }
        }

        if(temp == null){
            throw new IOException("GPIO Error: Bad Block Number!");
        }

        return outputValues.get(temp);
    }

    /*

     */
    public List<String> getOutputNames(){
        Integer[] outputs = outputValues.keySet().toArray(new Integer[outputValues.size()]);
        List<String> outputNames = new LinkedList<>();

        for(int i=0;i < outputs.length;i++){
            outputNames.add(outputs.toString());
        }

        return outputNames;
    }

    /*

     */
    public Boolean[] getOutputValues(){
        return outputValues.values().toArray(new Boolean[outputValues.size()]);
    }

    /*

     */
    public TrackElement getBlockElement(int blockNumber) throws IOException {
        for(int i = 0; i < allBlocks.size(); i++){
            if(blockNumber == allBlocks.get(i).getBlockNum()){
                return allBlocks.get(i);
            }
        }

        throw new IOException("Controller Error: No block with that number in the wayside...");
    }

    /*
    gets the controller name
     */
    public String getControllerName(){
        return controllerName;
    }
}
