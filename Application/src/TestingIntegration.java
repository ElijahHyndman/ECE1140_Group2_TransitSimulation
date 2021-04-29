import CTCOffice.CTCOffice;
import SimulationEnvironment.SimulationEnvironment;
import Track.Track;
import TrackConstruction.Switch;
import TrackConstruction.TrackElement;

public class TestingIntegration {
    public static void main(String[] args) throws Exception {
        SimulationEnvironment SE = new SimulationEnvironment();
        CTCOffice ctc = SE.getCTC();

        Track trackSys = new Track();
        trackSys.importTrack("Application/Resources/RedGreenUpdated.csv");
        for (TrackElement block : trackSys.getBlocks()) {
            if (block instanceof Switch) {
                ((Switch)block).setSwitchState(true);
                System.out.println(((Switch) block).totoString());
            }
        }
        ctc.updateTrack(trackSys);

        System.out.println(SE.getCTC().getTrack());
        System.out.println(SE.getCTC().getWaysideSystem());

        CTCOffice newCTC = new CTCOffice(trackSys,SE);
        System.out.println("\nNew CTC\n"+newCTC.getTrack());

        SimulationEnvironmentUI jFrame = new SimulationEnvironmentUI(SE);
        jFrame.latch(SE);
        GUIWindowLauncher.launchWindow(jFrame);
    }
}
