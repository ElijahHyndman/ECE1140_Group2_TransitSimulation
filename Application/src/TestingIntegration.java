import CTCOffice.CTCOffice;
import SimulationEnvironment.SimulationEnvironment;
import Track.Track;

public class TestingIntegration {
    public static void main(String[] args) throws Exception {
        SimulationEnvironment SE = new SimulationEnvironment();
        CTCOffice ctc = SE.getCTC();

        Track trackSys = new Track();
        trackSys.importTrack("Application/Resources/RedGreenUpdated.csv");

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
