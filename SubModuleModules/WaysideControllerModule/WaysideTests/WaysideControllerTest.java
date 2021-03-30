import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import WaysideController.*;

import java.io.IOException;
import java.net.URISyntaxException;
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
    }

}