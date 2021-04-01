import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.Assertions;
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

        ArrayList<TrackElement> trackElementsController1 = new ArrayList<>();
        trackElementsController1.add(trackSwitch);
        trackElementsController1.add(block1);
        trackElementsController1.add(block2);
        trackElementsController1.add(block3);

        ArrayList<TrackElement> trackElementsController2 = new ArrayList<>();
        trackElementsController2.add(block4);
        trackElementsController2.add(block5);
        trackElementsController2.add(block6);

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        //set the block numbers
        for(int i=0;i < trackElements.size();i++) {
            trackElements.get(i).setBlockNum(i);
        }

        //creation of the test system
        WaysideSystem system = new WaysideSystem(trackElements);
        system.addWaysideController(trackElementsController1);
        system.addWaysideController(trackElementsController2);
        system.broadcastToControllers(speed, authority);

        //test the speeds for broadcasting
        for(int i=0;i < system.getBlocks().size();i++){
            Assertions.assertEquals(speed[i], system.findTrackElement(i).getCommandedSpeed());
        }

        //test the authority for broadcasting
        for(int i=0;i < system.getBlocks().size();i++){
            Assertions.assertEquals(authority[i], system.findTrackElement(i).getAuthority());
        }

        //test the occupancy for broadcasting
        for(int i=0;i < system.getBlocks().size();i++){
            Assertions.assertEquals(occupiedElements[i], system.getOccupancy(system.findTrackElement(i)));
        }
    }

    @Test
    @DisplayName("Checks to see if all functionality is maintained")
    public void testWaysideSystemControllerPLCInputOutput() throws IOException, URISyntaxException {
        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, new int[]{0,0,0},"n");
        TrackBlock block1 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block4 = new TrackBlock();
        TrackBlock block5 = new TrackBlock();
        TrackBlock block6 = new TrackBlock();

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

        ArrayList<TrackElement> trackElementsController1 = new ArrayList<>();
        trackElementsController1.add(trackSwitch);
        trackElementsController1.add(block1);
        trackElementsController1.add(block2);
        trackElementsController1.add(block3);

        ArrayList<TrackElement> trackElementsController2 = new ArrayList<>();
        trackElementsController2.add(block4);
        trackElementsController2.add(block5);
        trackElementsController2.add(block6);

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        //set the block numbers
        for(int i=0;i < trackElements.size();i++) {
            trackElements.get(i).setBlockNum(i);
        }

        //creation of the test system
        WaysideSystem system = new WaysideSystem(trackElements);
        system.addWaysideController(trackElementsController1);
        system.addWaysideController(trackElementsController2);
        system.broadcastToControllers(speed, authority);

        Assertions.assertArrayEquals(speed, system.getSpeed());
        Assertions.assertArrayEquals(authority, system.getAuthority());
    }

    @Test
    @DisplayName("Check all data related to switches")
    public void testWaysideSystemSwitches() throws IOException, URISyntaxException {
        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, new int[]{0,0,0},"n");
        TrackBlock block1 = new TrackBlock();
        TrackBlock block2 = new TrackBlock();
        TrackBlock block3 = new TrackBlock();
        TrackBlock block4 = new TrackBlock();
        TrackBlock block5 = new TrackBlock();
        TrackBlock block6 = new TrackBlock();

        double[] speed = new double[]{10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0};
        int[] authority = new int[]{1, 2, 3, 4, 5, 6, 7};

        boolean[] occupiedElements = new boolean[]{false, true, true, true, true, true, true};

        //add the track elements
        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);
        trackElements.add(block2);
        trackElements.add(block3);
        trackElements.add(block4);
        trackElements.add(block5);
        trackElements.add(block6);

        ArrayList<TrackElement> trackElementsController1 = new ArrayList<>();
        trackElementsController1.add(trackSwitch);
        trackElementsController1.add(block1);
        trackElementsController1.add(block2);
        trackElementsController1.add(block3);

        ArrayList<TrackElement> trackElementsController2 = new ArrayList<>();
        trackElementsController2.add(block4);
        trackElementsController2.add(block5);
        trackElementsController2.add(block6);

        //set some blocks as occupied
        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupiedElements[i]);
        }

        //set the block numbers
        for(int i=0;i < trackElements.size();i++) {
            trackElements.get(i).setBlockNum(i);
        }

        //creation of the test system
        WaysideSystem system = new WaysideSystem(trackElements);
        system.addWaysideController(trackElementsController1);
        system.addOutputWaysideController(trackSwitch, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testPLC1");

        System.out.println(system.getSwitchStatus(trackSwitch));
        system.setSwitchStatus(trackSwitch, false);
        System.out.println(system.getSwitchStatus(trackSwitch));
        block1.setOccupied(false);
        system.updateOutputWaysideController(trackSwitch);
        System.out.println(system.getSwitchStatus(trackSwitch));
    }

    //DEPRECATED
//    @Test
//    @DisplayName("Dummy Test!")
//    public void testDummy() throws IOException, URISyntaxException {
//        String commandLine = "upload gaming potato";
//        String[] commands = commandLine.split(" ");
//
//        //test if valid input was inputed
////        if(commands[0].length() == 0 || commands[0].length() >= 4) {
////            throw new IOException("Command Error: The current command was invalid...");
////        }
//
//        for(int i=0;i < commands.length;i++){
//            System.out.println(commands[i]);
//        }
//    }
//
//    //DEPRECATED
//    @Test
//    @DisplayName("CommandLineTest!")
//    public void testCommandLine() throws IOException, URISyntaxException {
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
//    }
}