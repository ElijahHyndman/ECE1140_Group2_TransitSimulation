package WaysideController;

import WaysideGUI.WaysideUIClass;
import TrackConstruction.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GPIO {

    int numberOfBlocks; //total blocks to read from
    int numberOfOutputs;
    String controllerName;

    ArrayList<TrackBlock> blocks;  //jurisdiction
    HashMap<TrackElement, Boolean> outputValues;

    public GPIO(){
        blocks = new ArrayList<>();
        outputValues = new HashMap<>();
        numberOfBlocks = 0;
    }

    public GPIO(ArrayList<TrackBlock> blocks, String controllerName){
        this.blocks = blocks;
        this.controllerName = controllerName;
        outputValues = new HashMap<>();
        numberOfBlocks = blocks.size();
        numberOfOutputs = outputValues.size();
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    /*
        Gets the values from the occupied blocks
         */
    public boolean[] getInputValues(){
        boolean[] inputs = new boolean[numberOfBlocks];

        for(int i=0;i < numberOfBlocks;i++){
            inputs[i] = blocks.get(i).getOccupied();
        }

        return inputs;
    }

    /*
    Generates an output, requires some value by default
     */
    public void addOutput(TrackElement trackElement, boolean defaultOutputValue) throws IOException {
        if(trackElement.getType().equalsIgnoreCase("switch")){ //needs a way to check if the trackelement is the correct one without the string
            outputValues.put(trackElement, defaultOutputValue);
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

        outputValues.replace(trackElement, value);
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

        for(int i=0;i < blocks.size();i++){
            if(blockNumber == blocks.get(i).getBlockNum()){
                temp = blocks.get(i);
                break;
            }
        }

        if(temp == null){
            throw new IOException("GPIO Error: Bad Block Number!");
        }

        return outputValues.get(temp);
    }

    public List<String> getOutputNames(){
        Integer[] outputs = outputValues.keySet().toArray(new Integer[outputValues.size()]);
        List<String> outputNames = new LinkedList<>();

        for(int i=0;i < outputs.length;i++){
            outputNames.add(outputs.toString());
        }

        return outputNames;
    }
    public Boolean[] getOutputValues(){
        return outputValues.values().toArray(new Boolean[outputValues.size()]);
    }

    public String getControllerName(){
        return controllerName;
    }
}
