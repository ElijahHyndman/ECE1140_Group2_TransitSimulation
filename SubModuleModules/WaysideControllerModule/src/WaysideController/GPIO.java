package WaysideController;

import WaysideGUI.WaysideUIClass;
import TrackConstruction.*;

import java.io.IOException;
import java.util.ArrayList;

public class GPIO {

    int numberOfBlocks; //total blocks to read from
    int numberOfOutputs;
    ArrayList<TrackBlock> blocks;  //jurisdiction
    ArrayList<TrackElement> outputs;

    public GPIO(){
        blocks = new ArrayList<>();
        outputs = new ArrayList<>();
        numberOfBlocks = 0;
    }

    public GPIO(ArrayList<TrackBlock> blocks){
        this.blocks = blocks;
        outputs = new ArrayList<>();
        numberOfBlocks = blocks.size();
        numberOfOutputs = outputs.size();
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
    Generates an output, there is no value set by default
     */
    public void addOutput(String type, TrackElement output) throws IOException {
        if(type.equals("Switch")){ //needs a way to check if the trackelement is the correct one without the string
            outputs.add(output);
        }else{
            throw new IOException("GPIO Error: Bad Output Type");
        }
    }

    /*
    Gets an output, throws error if there is no PLC(no way to generate that output value)
     */
    public void setOutput(String name){

    }
}
