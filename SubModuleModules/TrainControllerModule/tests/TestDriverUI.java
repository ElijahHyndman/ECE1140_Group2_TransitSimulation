import TrainControlUI.DriverUI;
import TrainModel.Train;
import implementation.TrainControl;
import org.junit.Before;
import org.junit.Test;

public class TestDriverUI {

    Train theTrain;
    TrainControl control;

    @Before
    public void setUp(){
         theTrain = new Train(5, 4,0);
         control = new TrainControl(theTrain);
         theTrain.setBeacon("Dormont: 200");
         theTrain.setAuthority(10);
         theTrain.setCommandedSpeed(10);
         theTrain.setSpeed(0);
         control.updateCommandOutputs("test", 1);
    }

    @Test
    public void testSetup() {

/*

        DriverUI ui = new DriverUI();
        ui.latch(control);
        boolean run = true;
        while(run){
            ui.update();
        }
    }

 */
    }

}
