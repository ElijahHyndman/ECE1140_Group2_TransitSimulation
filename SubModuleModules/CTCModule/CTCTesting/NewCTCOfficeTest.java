import CTCOffice.CTCOffice;
import SimulationEnvironment.SimulationEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NewCTCOfficeTest {

    @Test
    @DisplayName("Load the CTCOffice")
    public void LoadSchedule() {
        SimulationEnvironment se = new SimulationEnvironment();
        //se.importTrack();
        CTCOffice ctc = se.getCTC();


        //ctcOffice.LoadSchedule("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\TrackModelModule\\src\\Track\\RedGreenUpdated.csv");
        //ArrayList<DisplayLine> schedule = ctcOffice.getDisps();

        //System.out.println(schedule);
    }
}
