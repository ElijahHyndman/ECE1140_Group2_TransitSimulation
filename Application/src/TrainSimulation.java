import SimulationEnvironment.SimulationEnvironment;
import WaysideController.WaysideController;
import WaysideGUI.WaysideUIJFrameWindow;

import java.util.Vector;

public class TrainSimulation extends Thread {


    SimulationEnvironment SE;
    SimulationEnvironmentJFrame appGUI;

    volatile boolean running = false;

    public TrainSimulation() {
        SE = new SimulationEnvironment();
        spawnSEGUI();
    }

    public void spawnSEGUI() {
        appGUI = new SimulationEnvironmentJFrame(SE);
        appGUI.latch(SE);
    }

    public void run() {
        running = true;
        while(running) {
            appGUI.update();
        }
    }

    public void halt() {
        running = false;
    }

    public SimulationEnvironment getSE() {
        return SE;
    }

    public SimulationEnvironmentJFrame getJFrame() {
        return appGUI;
    }

    public static void main(String[] args) {
        TrainSimulation run = new TrainSimulation();
        run.getSE().castGreenLine();
        run.getJFrame().setGreenLine();
        run.start();
    }
}
