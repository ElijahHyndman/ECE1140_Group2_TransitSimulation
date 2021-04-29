import CTCOffice.CTCOffice;
import CTCOffice.DisplayLine;
import SimulationEnvironment.SimulationEnvironment;
import Track.Track;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class NewCTCOfficeTest {

    @Test
    @DisplayName("Load the schedule of the CTCOffice")
    public void LoadSchedule() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);

        System.out.println(se.getCTC().getTrack());
        System.out.println(se.getCTC().getWaysideSystem());

        ctc.LoadSchedule("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\schedule.csv");
        ArrayList<DisplayLine> schedule = ctc.getDisps();

        for(int i=0;i < schedule.size();i++){
            System.out.println(schedule.get(i).getT10());
        }
        System.out.println(schedule);
    }

    @Test
    @DisplayName("Dispatch something from two locations (Green Line)")
    public void DispatchTrainGreen() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);

        System.out.println(se.getCTC().getTrack());
        System.out.println(se.getCTC().getWaysideSystem());

        System.out.println();
        ctc.timeNow = "00:00:00";

        System.out.println(Arrays.toString(ctc.Dispatch("Pioneer","Train 1","23:30")));
    }

    @Test
    @DisplayName("Dispatch something from two locations (Red Line)")
    public void DispatchTrainRed() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);

        System.out.println(se.getCTC().getTrack());
        System.out.println(se.getCTC().getWaysideSystem());

        System.out.println();
        ctc.timeNow = "00:00:00";

        System.out.println(Arrays.toString(ctc.Dispatch("First Ave","Train 3","04:30")));
    }

    @Test
    @DisplayName("Dispatch and testing the broadcast")
    public void DispatchTrainBroadCast() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);

        System.out.println(se.getCTC().getTrack());
        System.out.println(se.getCTC().getWaysideSystem());

        ctc.timeNow = "00:00:00";

        for(int i=0;i < trackSys.getRedLine().size();i++){
            trackSys.getBlock(i).setOccupied(false);
        }

        for(int i=0;i < trackSys.getRedLine().size();i++){
            System.out.println("Block " + i + " : " + trackSys.getBlock(i).getAuthority());
        }
        System.out.println();

        System.out.println(Arrays.toString(ctc.Dispatch("First Ave","Train 3","04:30")));

        ctc.timeNow = "03:51:00";
        ctc.BroadcastingArrays();

        for(int i=0;i < trackSys.getRedLine().size();i++){
            System.out.println("Block " + i + " : " + trackSys.getBlock(i).getAuthority());
        }
    }

    @Test
    @DisplayName("Testing Calc Tix Sales per Hour which is Throughput")
    public void CalculateThroughput() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);

        System.out.println(se.getCTC().getTrack());
        System.out.println(se.getCTC().getWaysideSystem());

        ctc.timeNow = "04:50:00";

        for(int i=0;i < trackSys.getRedLine().size();i++){
            System.out.println("Block " + i + " : " + trackSys.getBlock(i).getAuthority());
        }

        System.out.println(Arrays.toString(ctc.Dispatch("First Ave","Train 3","05:30")));
        System.out.println(ctc.CalcThroughput());
    }

    @Test
    @DisplayName("Testing occupancy of various blocks from wayside")
    public void CheckOcc() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);

        trackSys.getRedLine().get(17).setOccupied(true);
        trackSys.getRedLine().get(25).setOccupied(true);
        trackSys.getRedLine().get(33).setOccupied(true);

        System.out.println(se.getCTC().getTrack());
        System.out.println(se.getCTC().getWaysideSystem());

        ctc.timeNow = "00:00:00";

        for(int i = 0;i < trackSys.getRedLine().size();i++){
            System.out.println("Block " + i + " : " + ctc.CheckOcc(i, "Red"));
        }
    }

    @Test
    @DisplayName("Testing the check section occupancy")
    public void checkSectionOccupancyForClose() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.LoadSchedule("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\schedule.csv");
        ctc.updateTrack(trackSys);
        ctc.timeNow = "00:00:00";

        trackSys.getGreenLine().get(17).setOccupied(true);

        Assertions.assertTrue(ctc.CheckSectOcc(17, "Green"));
        Assertions.assertTrue(ctc.CheckSectOcc(19, "Green"));
    }

    @Test
    @DisplayName("Testing the ability to get switching position")
    public void checkSwitchStatus() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);
        ctc.timeNow = "00:00:00";

        Assertions.assertFalse(ctc.CheckSwitch(12, "Green"));
        ctc.ToggleSwitch(12, ctc.CheckSwitch(12, "Green"), "Green");
        Assertions.assertTrue(ctc.CheckSwitch(12, "Green"));
    }

    @Test
    @DisplayName("Testing the ability to get switching position")
    public void checkCloseTrack() throws Exception {
        SimulationEnvironment se = new SimulationEnvironment();
        CTCOffice ctc = se.getCTC();
        Track trackSys = new Track();
        trackSys.importTrack("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\Application\\Resources\\RedGreenUpdated.csv");

        ctc.updateTrack(trackSys);
        ctc.timeNow = "00:00:00";
        ctc.CloseTrack(10, "Green");

        Assertions.assertEquals(trackSys.getBlock(10).getFailureStatus(),"CLOSED");
        Assertions.assertEquals(trackSys.getBlock(12).getFailureStatus(),"CLOSED");
        Assertions.assertEquals(trackSys.getBlock(13).getFailureStatus(),"NONE");
    }
}
