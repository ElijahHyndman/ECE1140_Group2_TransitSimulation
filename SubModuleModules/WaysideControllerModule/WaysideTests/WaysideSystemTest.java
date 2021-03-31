import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import WaysideController.*;
import WaysideGUI.*;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

class WaysideSystemTest {

    //DEPRECATED
    @Test
    @DisplayName("Dummy Test!")
    public void testDummy() throws IOException, URISyntaxException {
        String commandLine = "upload gaming potato";
        String[] commands = commandLine.split(" ");

        //test if valid input was inputed
//        if(commands[0].length() == 0 || commands[0].length() >= 4) {
//            throw new IOException("Command Error: The current command was invalid...");
//        }

        for(int i=0;i < commands.length;i++){
            System.out.println(commands[i]);
        }
    }

    //DEPRECATED
    @Test
    @DisplayName("CommandLineTest!")
    public void testCommandLine() throws IOException, URISyntaxException {
        WaysideSystem testSystem = new WaysideSystem();
        WaysideUIClass guiUpdater;
        guiUpdater = new WaysideUIClass(testSystem);
        //testSystem.generateTestController();

        System.out.println(testSystem.readConsole("upload Controller1 C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken3 Switch1"));
        System.out.println(testSystem.readConsole("compile Controller1 Switch1"));
        System.out.println("starting GUI");
        try {
            guiUpdater.start();
        } catch (Exception e) {
            System.out.println("failed when running");
        }
    }

    @Test
    @DisplayName("Block generation and assignment")
    public void testBlockCreationAndAssignment() throws IOException, URISyntaxException {
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

        //
        WaysideSystem testSystem = new WaysideSystem(trackElements, "testLine");
        testSystem.addWaysideController(blockNumbers);

        HashMap<TrackElement, WaysideController> lut = testSystem.getLut();
    }
}