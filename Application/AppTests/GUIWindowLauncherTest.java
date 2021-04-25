import CTCOffice.DisplayLine;
import Track.Track;
import Track.TrackGUI;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import TrainControlUI.DriverUI;
import TrainModel.Train;
import TrainModel.trainGUI;
import WaysideController.WaysideController;
import WaysideController.WaysideSystem;
import WaysideGUI.WaysideUIJFrameWindow;
import implementation.TrainControl;
import CTCUI.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author elijah
 */
class GUIWindowLauncherTest {

    /*
        Thread Generation
     */
    @Test
    @DisplayName("Generates nonnull Thread Object for TrainModel UI")
    public void launcherCreatesNonVoidTrainModelUI() throws Exception {
        trainGUI ui = new trainGUI(1);
        Runnable result = GUIWindowLauncher.generateThread(ui);
        System.out.println("Resulting Thread object: ");
        System.out.println(result);
        assertNotNull(result, "GUIWindow failed to create usable Thread Object");
    }

    @Test
    @DisplayName("Generates nonnull Thread Object for TrainController UI")
    public void launcherCreatesNonVoidTrainControllerUI() throws Exception {
        DriverUI ui = new DriverUI();
        Runnable result = GUIWindowLauncher.generateThread(ui);
        System.out.println("Resulting Thread object: ");
        System.out.println(result);
        assertNotNull(result, "GUIWindow failed to create usable Thread Object");
    }

    @Test
    @DisplayName("Generates nonnull Thread Object for Track UI")
    public void launcherCreatesNonVoidTrackUI() throws Exception {
        TrackGUI ui = new TrackGUI( new Track() );
        Runnable result = GUIWindowLauncher.generateThread(ui);
        System.out.println("Resulting Thread object: ");
        System.out.println(result);
        assertNotNull(result, "GUIWindow failed to create usable Thread Object");
    }

    @Test
    @DisplayName("Generates nonnull Thread Object for WaysideSystem UI")
    public void launcherCreatesNonVoidWaysideUI() throws Exception {
        WaysideUIJFrameWindow ui = new WaysideUIJFrameWindow();
        Runnable result = GUIWindowLauncher.generateThread(ui);
        System.out.println("Resulting Thread object: ");
        System.out.println(result);
        assertNotNull(result, "GUIWindow failed to create usable Thread Object");
    }

    @Test
    @DisplayName("Generates usable Thread Object for CTC UI")
    public void launcherCreatesNonVoidCTCUI() throws Exception {
        CTCJFrame ui = new CTCJFrame(new DisplayLine());
        Runnable result = GUIWindowLauncher.generateThread(ui);
        System.out.println("Resulting Thread object: ");
        System.out.println(result);
        assertNotNull(result, "GUIWindow failed to create usable Thread Object");
    }

    @Test
    @DisplayName("Thread generates catches null JFrame objects")
    public void launcherHandlesNullUI() throws Exception {
        TrackGUI ui = null;
        assertThrows(Exception.class, () -> GUIWindowLauncher.generateThread(ui));
    }

    /*
        Window Spawning
     */
    @Test
    @DisplayName("launches TrainModel UI")
    public void launchesTrainModelUIWindow() throws Exception {
        trainGUI ui = new trainGUI(1);
        // UI must be latched onto object
        ui.latch(new Train(2,2,1));
        System.out.println("Spawning Train Model UI for one second");
        Runnable result = GUIWindowLauncher.launchWindow(ui);
        try {TimeUnit.SECONDS.sleep(1);} catch(Exception e) {}
    }

    @Test
    @DisplayName("launches TrainController UI")
    public void launchesTrainControllerUIWindow() throws Exception {
        DriverUI ui = new DriverUI();
        // UI must be latched onto object
        ui.latch(new TrainControl());
        System.out.println("Spawning Train Controller UI for one second");
        Runnable result = GUIWindowLauncher.launchWindow(ui);
        try {TimeUnit.SECONDS.sleep(1);} catch(Exception e) {}
    }


    @Test
    @DisplayName("launches Track UI")
    public void launchesTrackUIWindow() throws Exception {
        // have to latch the track ui twice? doesn't have default constructor
        TrackGUI ui = new TrackGUI(new Track());
        // UI must be latched onto object
        ui.latch(new Track());
        System.out.println("Spawning Track UI for one second");
        Runnable result = GUIWindowLauncher.launchWindow(ui);
        try {TimeUnit.SECONDS.sleep(1);} catch(Exception e) {}
    }


    @Test
    @DisplayName("launches CTC UI")
    public void launchesCTCUIWindow() throws Exception {
        CTCJFrame ui = new CTCJFrame(new DisplayLine());
        // UI must be latched onto object
        ui.latch(new DisplayLine());
        System.out.println("Spawning CTC UI for one second");
        Runnable result = GUIWindowLauncher.launchWindow(ui);
        try {TimeUnit.SECONDS.sleep(1);} catch(Exception e) {}
    }

    // TODO Bug: somereason, waysidejframe is throwing a lot of errors when reinstating the scrollpane of the tree window
    @Test
    @DisplayName("launches WaysideSystem UI")
    public void launchesWaysideUIWindow() throws Exception {
        WaysideUIJFrameWindow ui = new WaysideUIJFrameWindow();
        // UI must be latched onto object
        WaysideSystem testingWaysideSystem = new WaysideSystem();
        ui.latch(testingWaysideSystem);
        System.out.println("Spawning Wayside UI for one second");
        Runnable result = GUIWindowLauncher.launchWindow(ui);
        try {TimeUnit.SECONDS.sleep(1);} catch(Exception e) {}
    }

    // TODO Bug: somereason, waysidejframe is throwing a lot of errors when reinstating the scrollpane of the tree window
    @Test
    @DisplayName("launches WaysideSystem UI")
    public void launchLongWaysideUIWindow() throws Exception {
        WaysideUIJFrameWindow ui = new WaysideUIJFrameWindow();

        LinkedList<WaysideController> ctrls = new LinkedList<WaysideController>();
        // UI must be latched onto object
        for (int controller=0; controller<3; controller++) {
            ArrayList<TrackElement> jurisdiction = new ArrayList<TrackElement>();
            for (int block = 0; block < 4; block++) {
                TrackBlock fakeBlock = new TrackBlock();
                fakeBlock.setOccupied(false);
                fakeBlock.setCommandedSpeed(10.0);
                fakeBlock.setAuthority(2);
                fakeBlock.setSpeedLimit(25);
                jurisdiction.add(fakeBlock);
            }
            WaysideController ctrl = new WaysideController(jurisdiction, String.format("Controller %d", controller+1));
            ctrls.add(ctrl);
        }
        WaysideSystem testingWaysideSystem = new WaysideSystem(ctrls);

        ui.latch(testingWaysideSystem);
        System.out.println("Spawning Wayside UI for one second");
        Runnable result = GUIWindowLauncher.launchWindowWithRate(ui,2);
        while(true) {}
    }
}
