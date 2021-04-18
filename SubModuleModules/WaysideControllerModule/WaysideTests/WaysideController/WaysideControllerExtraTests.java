package WaysideController;

import Track.Track;
import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaysideControllerExtraTests {
    @Test
    @DisplayName("More tests to show how everything works internally")
    public void testControllerCreation() throws IOException, URISyntaxException {
        WaysideController controller;

        Switch trackSwitch = new Switch("Green", 'A', 0, 100.0, -3.0, 55, "SWITCH (0-1; 2-3)",-3,0.5, new int[]{0,0,0},"n");
        TrackBlock block1 = new TrackBlock();

        int[] blockNumbers = new int[]{0, 1};
        double[] speed = new double[]{10.0, 20.0};
        int[] authority = new int[]{1, 2};

        boolean[] occupied = new boolean[]{false, false};

        ArrayList<TrackElement> trackElements = new ArrayList<>();
        trackElements.add(trackSwitch);
        trackElements.add(block1);

        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setBlockNum(blockNumbers[i]);
        }

        for(int i=0;i < trackElements.size();i++){
            trackElements.get(i).setOccupied(occupied[i]);
        }

        for(int i=0;i < speed.length;i++){
            trackElements.get(i).setCommandedSpeed(speed[i]);
            trackElements.get(i).setAuthority(authority[i]);
        }

        controller = new WaysideController(trackElements, "Controller 1");

        controller.addOutput(0, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken4");
        controller.generateOutputSignal(0, false);
        System.out.println("Original : " + trackSwitch.getSwitchState());

        for(int i=1;i < 10;i++){
            if(i%2==1){
                block1.setOccupied(false);
            }else{
                block1.setOccupied(true);
            }

            controller.generateOutputSignal(0, false);
            System.out.println("Track block : " + Boolean.toString(block1.getOccupied()));
            System.out.println(Integer.toString(i) + " : " + trackSwitch.getSwitchState());
        }
    }
}