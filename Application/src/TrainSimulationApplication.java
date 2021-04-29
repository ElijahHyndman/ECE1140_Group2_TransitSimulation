//import CTCOffice.CTCOffice;
//import CTCUI.CTCJFrame;
import CTCOffice.CTCOffice;
import CTCUI.CTCJFrame;
import GUIInterface.AppGUIModule;
import SimulationEnvironment.*;
import Track.Track;
import Track.TrackGUI;
import TrackConstruction.TrackElement;
import TrainControlUI.DriverUI;
import TrainModel.Train;
import TrainModel.trainGUI;
import WaysideController.WaysideSystem;
import implementation.TrainControl;

public class TrainSimulationApplication extends Thread {


    SimulationEnvironment SE;
    SimulationEnvironmentJFrame appGUI; // changing it to jframe

    volatile boolean running = false;

    public TrainSimulationApplication() {
        SE = new SimulationEnvironment();
        spawnSEGUI();
    }
    public TrainSimulationApplication(String line) throws Exception {
        SE = new SimulationEnvironment();
      //  SE.importTrack(line); // adding to load
      //  SE.setTrack(SE.getTrackSystem());
        spawnSEGUI();

    }

    public void spawnSEGUI() {
        appGUI = new SimulationEnvironmentJFrame(SE); // Think this is the one to use Now
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


    public AppGUIModule spawnGUI(Object item) {
        if(item instanceof Train) {
            try {
                AppGUIModule<Train> newWindow = new trainGUI(0);
                newWindow.latch((Train) item);
                //newWindow.start();
                return newWindow;
            } catch (Exception e) {
                System.out.println("GUI failed");
                e.printStackTrace();
            }
        } else if (item instanceof TrainControl) {
            try {
                AppGUIModule<TrainControl> newWindow = new DriverUI();
                newWindow.latch((TrainControl) item);
                return newWindow;
            } catch (Exception e) {
                System.out.println("GUI failed");
                e.printStackTrace();
            }
        } else if (item instanceof CTCOffice) {
            try {
                AppGUIModule<CTCOffice> newWindow = new CTCJFrame((CTCOffice)item);
                //newWindow.latch(item);
                return null;
            } catch (Exception e) {
                System.out.println("GUI failed");
                e.printStackTrace();
            }
        } else if (item instanceof WaysideSystem) {
            try {
                System.out.println("Spawning wayside system gui");
              //  AppGUIModule<WaysideSystem> newWindow = new WaysideUIJFrameWindow();
              //  newWindow.latch((WaysideSystem) item);
            //    return newWindow;
            } catch (Exception e) {
                System.out.println("GUI failed");
                e.printStackTrace();
            }
        } else if (item instanceof Track) {
            try {
                AppGUIModule<Track> newWindow = new TrackGUI((Track)item);
                newWindow.latch((Track) item);
                return newWindow;
            } catch (Exception e) {
                System.out.println("GUI failed");
                e.printStackTrace();
            }
        }
        return null;
    }

    public DriverUI spawnControllerUI(TrainControl ctrl) {
        DriverUI newUI = new DriverUI();
        newUI.latch(ctrl);
        return newUI;
    }



    public static void main(String[] args) throws Exception {
        TrainSimulationApplication run = new TrainSimulationApplication("SubModuleModules/TrackModelModule/src/Track/RedGreenUpdated.csv");
       // run.getJFrame().setGreenLine(); // ???
    //   run.start();

      //  CTCJFrame ctcui = new CTCJFrame(run.getSE().getCTC());
       // ctcui.setVisible(true);

        //run.spawnGUI(run.getSE().getCTC());

        //run.getSE().getCTC().Dispatch("Dormont","new","00:08:00");

        /*
        CTCJFrame ctcui = new CTCJFrame(run.getSE().getCTC());
        ctcui.setVisible(true);
        System.out.println("SimulationEnvironment: " + run.getSE().getCTC().hashCode());
        */
        TrackElement yard = run.getSE().getTrackSystem().getBlock(0);
        //yard.setCommandedSpeed(10.0);
        //yard.setAuthority(1);
        //System.out.println(yard);

        run.getSE().getTrackSystem().getSwitches().get(10).setSwitchState(true);
        run.getSE().getTrackSystem().updateSwitches();

        // wait for train to spawn
       /* while(run.getSE().getTrains().size() == 0) {}
        TrainUnit runningTrain = run.getSE().getTrains().get(0);
        DriverUI ctrlUI = run.getSE().spawnTrainControllerUI(runningTrain);
        trainGUI modelUI = run.getSE().spawnTrainModelGUI(runningTrain);
        TrackGUI trackUI = run.getSE().spawnTrackBuilderGUI(run.getSE().getTrackSystem());
        trackUI.setVisible(true);

        while(true) {
            ctrlUI.update();
            modelUI.updateDisplay();
            modelUI.update();
            trackUI.latch(run.getSE().getTrackSystem());
        }*/
    }
}
