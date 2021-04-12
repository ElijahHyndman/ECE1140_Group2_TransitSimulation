package WaysideGUI;

import WaysideController.WaysideController;
import WaysideController.WaysideSystem;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elijah
 */
public class WaysideUIClass extends Thread {
    /**
     * WaysideUIClass handles all of the actions necessary for spawning the GUI for interfacing with all Wayside Controllers
     *  in the Simulation Environment.
     *
     *  @author Elijah Hyndman
     *  @since 2021-02-27
     *
     * A list of Wayside Controller Objects must be continually handed to this class from the simulation environment so that changes to
     *  the Wayside controllers can be reflected in the GUI. The GUI can only display the parameters from the most recent update of wayside objects
     *
     */

    //Vector<WaysideController> controllers = new Vector<WaysideController>();
    WaysideSystem WS;
    WaysideUIJFrameWindow guiWindow;

    public WaysideUIClass() throws IOException {
        WS = new WaysideSystem();
        guiWindow = new WaysideUIJFrameWindow(WS);
    }

    public WaysideUIClass(WaysideSystem existingSystem) throws IOException {
        WS = existingSystem;
        guiWindow = new WaysideUIJFrameWindow(existingSystem);
    }

    public void run() {
        if (WS == null) {
            return;
        }
        guiWindow.setVisible(true);

        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (Exception e) {
                System.out.println("A problem occurred using the sleep function inside the WaysideUIClass");
            }

            //System.out.println("Sampling wayside system");
            Vector<WaysideController> samples = (Vector<WaysideController>) WS.getControllersVector();
            guiWindow.updateGUI(samples);
        }
    }

}
