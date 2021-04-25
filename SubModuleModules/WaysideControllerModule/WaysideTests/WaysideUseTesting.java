import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import WaysideController.WaysideController;
import WaysideGUI.WaysideUIClass;
import WaysideController.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class WaysideUseTesting {
//    public static void main(String[] args) throws IOException, URISyntaxException {
//        // Reading from terminal
//        Scanner in = new Scanner(System.in);
//        double start,elapsed;
//
//
//        start = System.nanoTime();
//        // Simulate Blocks
//        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 0-2)",-3,0.5, new int[]{0,0,0},"n");
//        TrackBlock block1 = new TrackBlock();
//        TrackBlock block2 = new TrackBlock();
//        int[] blockNumbers = new int[]{0, 1, 2};
//        double[] speed = new double[]{10.0, 20.0, 20.0};
//        int[] authority = new int[]{1, 2, 2};
//        boolean[] occupied = new boolean[]{false, false, false};
//        ArrayList<TrackElement> trackElements = new ArrayList<>();
//        trackElements.add(trackSwitch);
//        trackElements.add(block1);
//        trackElements.add(block2);
//        for(int i=0;i < trackElements.size();i++){
//            trackElements.get(i).setBlockNum(blockNumbers[i]);
//        }
//
//        for(int i=0;i < trackElements.size();i++){
//            trackElements.get(i).setOccupied(occupied[i]);
//        }
//
//        for(int i=0;i < speed.length;i++){
//            trackElements.get(i).setCommandedSpeed(speed[i]);
//            trackElements.get(i).setAuthority(authority[i]);
//        }
//        elapsed = System.nanoTime()-start;
//        System.out.printf("Block generation: %fs\n",elapsed/1000000000);
//
//        start = System.nanoTime();
//        // Create controller, upload plc
//        WaysideController controller;
//        controller = new WaysideController(trackElements, "Controller 1");
//        controller.addOutput(0, "SubModuleModules/WaysideControllerModule/Resources/testtoken4");
//        controller.generateOutputSignal(0, false);
//        elapsed = System.nanoTime()-start;
//        System.out.printf("Controller and PLC generation: %fs\n",elapsed/1000000000);
//
//
//        // Create Controller gui
//        LinkedList<WaysideController> controllers = new LinkedList<WaysideController>();
//        controllers.add(controller);
//        new WaysideUIClass(new WaysideSystem(controllers)).start();
//
//        // contine tests
//        while(true) {
//            // Measure time to generate signals
//            start = System.nanoTime();
//            controller.generateOutputSignals();
//            elapsed = System.nanoTime() - start;
//            System.out.println(String.format("Occupation 1,2 (%s,%s) gives Switch (%s) (%fs)",Boolean.toString(block1.getOccupied()),Boolean.toString(block2.getOccupied()),trackSwitch.getSwitchState(),elapsed/1000000000));
//
//            // Wait for user to indicate anything
//            String s = in.nextLine();
//            // Toggle block occupied
//            Random rnd = new Random();
//            // Toggle random block
//            if(rnd.nextInt() > 0)
//                block1.setOccupied(!block1.getOccupied());
//            else
//                block2.setOccupied(!block2.getOccupied());
//        }
//    }
}
