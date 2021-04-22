import TrainControlUI.DriverUI;
import TrainModel.Train;
import implementation.TrainControl;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
public class TestDriverUI {

    Train theTrain;
    TrainControl control;

    @Before
    public void setUp(){
          // This seems to be old values and old function calls
         //theTrain = new Train(5, 4);
         //control = new TrainControl(theTrain);
         //theTrain.setBeacon("Dormont: 11200");
         theTrain = new Train(5, 4,0);
         control = new TrainControl(theTrain);
         theTrain.setBeacon("Dormont: 200");
         theTrain.setAuthority(10);
         theTrain.setCommandedSpeed(10);
         theTrain.setSpeed(0);
         control.updateCommandOutputs("test", 1);
    }

    @Test
    public void testSetup() throws InterruptedException{

        DriverUI ui = new DriverUI();
        ui.latch(control);
        //first set controller values
        theTrain.setAuthority(150000);
        theTrain.setSpeed(0);
        theTrain.setCommandedSpeed(8);

        // theTrain.setCommandedSpeed(10);
        System.out.println("-----start over-----");
        control.updateCommandOutputs("first test", 1);

        double initialTrainVelocity = control.getActualSpeed();

        //int i = 0;
        boolean stop = false;
        while(!stop){
            TimeUnit.SECONDS.sleep(1);
            control.updateCommandOutputs("test time", 1);
            double power = control.getPower();
            assertThat(theTrain.getPower(), is(power));
            if (theTrain.getActualSpeed() == 0){
               // stop = true;
            }
            System.out.println(control.getActualSpeed());
           //initialTrainVelocity = control.getActualSpeed();

            ui.update();
        }

}
