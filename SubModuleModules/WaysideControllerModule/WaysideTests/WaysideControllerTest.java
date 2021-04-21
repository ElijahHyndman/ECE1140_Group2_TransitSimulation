import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import WaysideController.GPIO;
import WaysideController.WaysideController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


class WaysideControllerTest {

    @BeforeEach
    public void setUp(){
        WaysideController controller = new WaysideController();
    }

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
    }

    @Test
    @DisplayName("New Controller Creation w/ TrackElements and Track Blocks")
    public void testControllerSpeedAuthority() throws IOException, URISyntaxException {
        WaysideController controller;
        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, new int[]{0,0,0},"n");
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

        controller.addOutput(0, "/Users/elijah/IdeaProjects/ECE1140_Group2_TransitSimulation/SubModuleModules/WaysideControllerModule/Resources/testPLC1");
        controller.generateOutputSignal(0, false);

        Assertions.assertEquals(false, gpio.getOutput(0));
        block2.setOccupied(true);
        block3.setOccupied(true);
        controller.generateOutputSignal(0, false);
        Assertions.assertEquals(true, gpio.getOutput(0));
    }

    @Test
    @DisplayName("New Controller Creation w/ TrackElements and Track Blocks")
    public void testControllerExtraInputs() throws IOException, URISyntaxException {
        WaysideController controller;
        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, new int[]{0,0,0},"n");
        TrackBlock block1 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();
        TrackBlock block4 = new TrackBlock();

        int[] blockNumbers = new int[]{0, 1, 2, 3, 4};
        double[] speed = new double[]{10.0, 20.0, 30.0, 40.0, 50.0};
        int[] authority = new int[]{1, 2, 3, 4, 5};

        boolean[] occupiedElements = new boolean[]{false, true, false, false, false};
        boolean[] occupiedBlocks = new boolean[]{true, false, false};

        //add the track elements
        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);
        trackElements.add(block2);
        trackElements.add(block3);
        trackElements.add(block4);

        //add the track blocks
        ArrayList<TrackBlock> trackBlocks = new ArrayList<>();
        trackBlocks.add(block1);
        trackBlocks.add(block2);
        trackBlocks.add(block3);
        trackBlocks.add(block4);

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
        Switch newSwitch = new Switch();

        controller.addOutput(0, "/Users/elijah/IdeaProjects/ECE1140_Group2_TransitSimulation/SubModuleModules/WaysideControllerModule/Resources/testPLC1");
        controller.generateOutputSignal(0, false);

        Assertions.assertEquals(false, gpio.getOutput(0));
        newSwitch = (Switch) gpio.getBlockElement(0);
        System.out.println(newSwitch.getSwitchState());

        block2.setOccupied(true);
        block3.setOccupied(true);
        controller.generateOutputSignal(0, false);
        Assertions.assertEquals(true, gpio.getOutput(0));

        newSwitch = (Switch) gpio.getBlockElement(0);
        System.out.println(newSwitch.getSwitchState());
    }

    @Test
    public void testLoops() throws IOException, URISyntaxException {
        int[] controller1Blocks = new int[21];
        int[] controller2Blocks = new int[38];
        int[] controller3Blocks = new int[13];
        int[] controller4Blocks = new int[13];
        int[] controller5Blocks = new int[8];
        int[] controller6Blocks = new int[20];
        int[] controller7Blocks = new int[1];
        int[] controller8Blocks = new int[38];
        int j;

        //controller1
        for(int i=0;i <= 20;i++){
            controller1Blocks[i] = i;
        }

        //controller2
        j = 21;
        for(int i=0;i <= 26;i++){
            controller2Blocks[i] = j;
            j++;
        }
        j = 140;
        for(int i=27;i <= 37;i++){
            controller2Blocks[i] = j;
            j++;
        }

        //controller3
        j=48;
        for(int i=0;i <= 12;i++){
            controller3Blocks[i] = j;
            j++;
        }

        //controller4
        j=61;
        for(int i=0;i <= 12;i++){
            controller4Blocks[i] = j;
            j++;
        }

        //controller5
        j=74;
        for(int i=0;i <= 6;i++){
            controller5Blocks[i] = j;
            j++;
        }
        controller5Blocks[7] = 101;

        //controller6
        j=81;
        for(int i=0;i <= 19;i++){
            controller6Blocks[i] = j;
            j++;
        }

        //controller7
        controller7Blocks[0] = 0;

        //controller8
        j=102;
        for(int i=0;i <= 37;i++){
            controller8Blocks[i] = j;
            j++;
        }

        //tests!
        for(int i=0;i < controller1Blocks.length;i++){
            System.out.print(controller1Blocks[i] + " ");
        }
        System.out.println();
        for(int i=0;i < controller2Blocks.length;i++){
            System.out.print(controller2Blocks[i] + " ");
        }
        System.out.println();
        for(int i=0;i < controller3Blocks.length;i++){
            System.out.print(controller3Blocks[i] + " ");
        }
        System.out.println();
        for(int i=0;i < controller4Blocks.length;i++){
            System.out.print(controller4Blocks[i] + " ");
        }
        System.out.println();
        for(int i=0;i < controller5Blocks.length;i++){
            System.out.print(controller5Blocks[i] + " ");
        }
        System.out.println();
        for(int i=0;i < controller6Blocks.length;i++){
            System.out.print(controller6Blocks[i] + " ");
        }
        System.out.println();
        for(int i=0;i < controller7Blocks.length;i++){
            System.out.print(controller7Blocks[i] + " ");
        }
        System.out.println();
        for(int i=0;i < controller8Blocks.length;i++){
            System.out.print(controller8Blocks[i] + " ");
        }
        System.out.println();

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
}