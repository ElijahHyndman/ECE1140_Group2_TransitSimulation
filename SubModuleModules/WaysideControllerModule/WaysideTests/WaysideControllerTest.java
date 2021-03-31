import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import WaysideController.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class WaysideControllerTest {

    @BeforeEach
    public void setUp(){
        WaysideController controller = new WaysideController();
    }

    //DEPRECATED!
//    @Test
//    @DisplayName("Testing Read Helper Function")
//    public void testReader() throws IOException, URISyntaxException {
//        int[] blocks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
//        String currentLine = "Blue";
//        List<String> currentInputNames = Arrays.asList("A", "C", "D");
//        boolean[] inputs = new boolean[]{false, true, true};
//        boolean[] outputs;
//
//        WaysideController waysideController = new WaysideController(blocks, currentLine, currentInputNames, inputs, "First Controller");
//        waysideController.addOutputSignal("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken3", "Switch1");
//        waysideController.generateOutputSignal("Switch1");
//        outputs = waysideController.getOutputValues();
//
//        for(int i=0;i < outputs.length;i++){
//            System.out.println("This is " + i + " : " + outputs[i]);
//        }
//
//        System.out.println("Finished");
//    }

    //DEPRECATED!
//    @Test
//    @DisplayName("Testing Read Helper Function")
//    public void testControllerFunctions() throws IOException, URISyntaxException {
//        int[] blocks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
//        String currentLine = "Blue";
//        List<String> currentInputNames = Arrays.asList("A", "C", "D");
//        boolean[] inputs = new boolean[]{false, true, true};
//        boolean[] outputs;
//
//        WaysideController waysideController = new WaysideController(blocks, currentLine, currentInputNames, inputs, "First Controller");
//        waysideController.addOutputSignal("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken3", "Switch1");
//        waysideController.generateOutputSignal("Switch1");
//        outputs = waysideController.getOutputValues();
//
//        for(int i=0;i < outputs.length;i++){
//            System.out.println("This is " + i + " : " + outputs[i]);
//        }
//
//        System.out.println("Finished");
//    }

    /*

     */
    @Test
    @DisplayName("New Controller Creation w/ TrackElements and Track Blocks")
    public void testControllerCreation() throws IOException, URISyntaxException {
        WaysideController controller;
        Switch trackSwitch = new Switch();
        TrackBlock block1 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();
        int[] blockNumbers = new int[]{0, 1, 2, 3};
        boolean[] occupiedElements = new boolean[]{false, true, false, false};
        boolean[] occupiedBlocks = new boolean[]{true, false, false};

        //add the track elements
        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);
        trackElements.add(block2);
        trackElements.add(block3);

        //add the track blocks
        ArrayList<TrackBlock> trackBlocks = new ArrayList<>();
        trackBlocks.add(block1);
        trackBlocks.add(block2);
        trackBlocks.add(block3);

        //set the block numbers
        for(int i=0;i < blockNumbers.length;i++){
            trackElements.get(i).setBlockNum(blockNumbers[i]);
        }

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        controller = new WaysideController(trackElements, trackBlocks, "Controller 1");

        //testing GPIO functionality
        GPIO gpio = controller.getGPIO();

        Assertions.assertArrayEquals(occupiedElements, gpio.getAllInputValues());
        Assertions.assertArrayEquals(occupiedBlocks, gpio.getInputValues());
    }

    @Test
    @DisplayName("New Controller Creation w/ TrackElements and Track Blocks")
    public void testControllerSpeedAuthority() throws IOException, URISyntaxException {
        WaysideController controller;
        Switch trackSwitch = new Switch();
        TrackBlock block1 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();

        int[] blockNumbers = new int[]{0, 1, 2, 3};
        double[] speed = new double[]{10.0, 20.0, 30.0, 40.0};
        int[] authority = new int[]{1, 2, 3, 4};

        boolean[] occupiedElements = new boolean[]{false, true, false, false};
        boolean[] occupiedBlocks = new boolean[]{true, false, false};

        //add the track elements
        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);
        trackElements.add(block2);
        trackElements.add(block3);

        //add the track blocks
        ArrayList<TrackBlock> trackBlocks = new ArrayList<>();
        trackBlocks.add(block1);
        trackBlocks.add(block2);
        trackBlocks.add(block3);

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        //set the block numbers
        for(int i=0;i < blockNumbers.length;i++){
            trackElements.get(i).setBlockNum(blockNumbers[i]);
        }

        //set some blocks as occupied
        for(int i=0;i < speed.length;i++){
            trackElements.get(i).setCommandedSpeed(speed[i]);
        }
        for(int i=0;i < authority.length;i++){
            trackElements.get(i).setAuthority(authority[i]);
        }

        controller = new WaysideController(trackElements, trackBlocks, "Controller 1");

        //testing GPIO functionality
        GPIO gpio = controller.getGPIO();

        controller.addOutput(0, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testPLC1");
        controller.generateOutputSignal(0, false);

        Assertions.assertEquals(false, gpio.getOutput(0));
        block2.setOccupied(true);
        block3.setOccupied(true);
        controller.generateOutputSignal(0, false);
        Assertions.assertEquals(true, gpio.getOutput(0));
    }

}