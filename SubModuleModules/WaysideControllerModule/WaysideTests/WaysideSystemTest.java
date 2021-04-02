import Track.*;
import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import WaysideController.*;
import WaysideGUI.*;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

class WaysideSystemTest {

    //DEPRECATED
    @Test
    @DisplayName("Dummy Test to test commandline functionality!")
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
//        WaysideSystem testSystem = new WaysideSystem();
//        WaysideUIClass guiUpdater;
//        guiUpdater = new WaysideUIClass(testSystem);
//        //testSystem.generateTestController();
//
//        System.out.println(testSystem.readConsole("upload Controller1 C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken3 Switch1"));
//        System.out.println(testSystem.readConsole("compile Controller1 Switch1"));
//        System.out.println("starting GUI");
//        try {
//            guiUpdater.start();
//        } catch (Exception e) {
//            System.out.println("failed when running");
//        }
    }

    @Test
    @DisplayName("Block generation and assignment")
    public void testWaysideSystemBlockCreationAndAssignment() throws IOException, URISyntaxException {
        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, new int[]{0,0,0},"n");
        TrackBlock block1 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block4 = new TrackBlock();
        TrackBlock block5 = new TrackBlock();
        TrackBlock block6 = new TrackBlock();

        int[] blockNumbersController1 = new int[]{0, 1, 2, 3};
        int[] blockNumbersController2 = new int[]{4, 5, 6};
        double[] speed = new double[]{10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0};
        int[] authority = new int[]{1, 2, 3, 4, 5, 6, 7};

        boolean[] occupiedElements = new boolean[]{false, true, false, false, true, true, true};

        //add the track elements
        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);
        trackElements.add(block2);
        trackElements.add(block3);
        trackElements.add(block4);
        trackElements.add(block5);
        trackElements.add(block6);

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        //set the block numbers
        for(int i=0;i < blockNumbersController1.length;i++){
            trackElements.get(i).setBlockNum(blockNumbersController1[i]);
        }

        for(int i=0;i < blockNumbersController2.length;i++){
            trackElements.get(i+4).setBlockNum(blockNumbersController2[i]);
        }

        //creation of the test system
        WaysideSystem system = new WaysideSystem(trackElements, "testLine");
        system.addWaysideController(blockNumbersController1);
        system.addWaysideController(blockNumbersController2);
        system.broadcastToControllers(speed, authority);

        //test the speeds for broadcasting
        for(int i=0;i < system.getBlocks().size();i++){
            Assertions.assertEquals(speed[i], system.findBlock(i).getCommandedSpeed());
        }

        //test the authority for broadcasting
        for(int i=0;i < system.getBlocks().size();i++){
            Assertions.assertEquals(authority[i], system.findBlock(i).getAuthority());
        }

        //test the occupancy for broadcasting
        for(int i=0;i < system.getBlocks().size();i++){
            Assertions.assertEquals(occupiedElements[i], system.getOccupancy(i));
        }
    }

    @Test
    @DisplayName("Checks to see if all functionality is maintained")
    public void testWaysideSystemControllerPLCInputOutput() throws IOException, URISyntaxException {
        WaysideController controller;
        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, new int[]{0,0,0},"n");
        TrackBlock block1 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block4 = new TrackBlock();
        TrackBlock block5 = new TrackBlock();
        TrackBlock block6 = new TrackBlock();

        int[] blockNumbersController1 = new int[]{0, 1, 2, 3};
        int[] blockNumbersController2 = new int[]{4, 5, 6};
        double[] speed = new double[]{10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0};
        int[] authority = new int[]{1, 2, 3, 4, 5, 6, 7};

        boolean[] occupiedElements = new boolean[]{false, true, false, false, true, true, true};

        //add the track elements
        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);
        trackElements.add(block2);
        trackElements.add(block3);
        trackElements.add(block4);
        trackElements.add(block5);
        trackElements.add(block6);

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        //set the block numbers
        for(int i=0;i < blockNumbersController1.length;i++){
            trackElements.get(i).setBlockNum(blockNumbersController1[i]);
        }

        for(int i=0;i < blockNumbersController2.length;i++){
            trackElements.get(i+4).setBlockNum(blockNumbersController2[i]);
        }

        //creation of the test system
        WaysideSystem system = new WaysideSystem(trackElements, "testLine");
        system.addWaysideController(blockNumbersController1);
        system.addWaysideController(blockNumbersController2);
        system.broadcastToControllers(speed, authority);

        system.addWaysideController(blockNumbersController1);
        system.addOutputWaysideController(0, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testPLC1");

        System.out.println(system.getSwitchStatus(0));
        system.setSwitchStatus(0, false);
        System.out.println(system.getSwitchStatus(0));
        block1.setOccupied(false);
        system.updateOutputWaysideController(0);
        System.out.println(system.getSwitchStatus(0));
        block1.setOccupied(true);
        system.updateAllOutputsWaysideController();
        System.out.println(system.getSwitchStatus(0));
    }

    @Test
    @DisplayName("Check all data related to switches")
    public void testWaysideSystemSwitches() throws IOException, URISyntaxException {
        int[] setDirection = new int[]{0,0,0};
        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, setDirection,"n");
        TrackBlock block1 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block4 = new TrackBlock();
        TrackBlock block5 = new TrackBlock();
        TrackBlock block6 = new TrackBlock();

        int[] blockNumbersController1 = new int[]{0, 1, 2, 3};
        int[] blockNumbersController2 = new int[]{4, 5, 6};
        double[] speed = new double[]{10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0};
        int[] authority = new int[]{1, 2, 3, 4, 5, 6, 7};

        boolean[] occupiedElements = new boolean[]{false, true, false, false, true, true, true};

        //add the track elements
        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);
        trackElements.add(block2);
        trackElements.add(block3);
        trackElements.add(block4);
        trackElements.add(block5);
        trackElements.add(block6);

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        //set the block numbers
        for(int i=0;i < blockNumbersController1.length;i++){
            trackElements.get(i).setBlockNum(blockNumbersController1[i]);
        }

        for(int i=0;i < blockNumbersController2.length;i++){
            trackElements.get(i+4).setBlockNum(blockNumbersController2[i]);
        }

        //creation of the test system
        WaysideSystem system = new WaysideSystem(trackElements, "testLine");
        system.addWaysideController(blockNumbersController1);
        system.addOutputWaysideController(0, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testPLC1");

        System.out.println(system.getSwitchStatus(0));
        system.setSwitchStatus(0, false);
        System.out.println(system.getSwitchStatus(0));
        block1.setOccupied(true);
        block2.setOccupied(true);
        block3.setOccupied(true);
        system.updateAllOutputsWaysideController();
        system.setSwitchStatus(0, true);
        System.out.println(system.getSwitchStatus(0));
    }

    @Test
    @DisplayName("Testing the track green line!")
    public void InterModuleTests() throws IOException, URISyntaxException {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\TrackModelModule\\src\\Track\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //creation of the test system
        WaysideSystem system = new WaysideSystem(instance.getGreenLine(), "testLine");

        int[] controller1Blocks = new int[20];
        int[] controller2Blocks = new int[38];
        int[] controller3Blocks = new int[13];
        int[] controller4Blocks = new int[13];
        int[] controller5Blocks = new int[8];
        int[] controller6Blocks = new int[20];
        int[] controller7Blocks = new int[1];
        int[] controller8Blocks = new int[38];
        int j;

        //controller1
        j = 1;
        for(int i=0;i <= 19;i++){
            controller1Blocks[i] = j;
            j++;
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

        // Create controllers from jurisdictions
        system.addWaysideController(controller1Blocks);
        system.addWaysideController(controller2Blocks);
        system.addWaysideController(controller3Blocks);
        system.addWaysideController(controller4Blocks);
        system.addWaysideController(controller5Blocks);
        system.addWaysideController(controller6Blocks);
        system.addWaysideController(controller7Blocks);
        system.addWaysideController(controller8Blocks);

    }
}