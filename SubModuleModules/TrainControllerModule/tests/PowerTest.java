import TrainModel.Train;
import implementation.TrainControl;
import org.junit.*;

import java.time.LocalTime;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PowerTest {

    TrainControl control;
    Train theTrain;

    public void updateTheTrain(double deltaTime){
        theTrain.updatePhysicalState("nothing", deltaTime);
        control.updateCommandOutputs("06:30:55", deltaTime);
    }

    @Before
    public void setUp(){
        theTrain = new Train(5,10,1);
        control = new TrainControl(theTrain);

    }

    @Test
    public void regulateTrainFrom0To20(){
            theTrain.setSpeed(0);
            theTrain.setCommandedSpeed(15);
            theTrain.setBeacon(null);
            theTrain.setAuthority(20);

            int count = 0;
            while(count < 20000){
                System.out.println(count + " : Train Speed: " + theTrain.getActualSpeed());
                updateTheTrain(.1);
                count++;
            }
    }

    @Test
    public void regulateTrainFrom0To40(){

    }

    @Test
    public void regulateTrainFrom30To0(){

    }

    @Test
    public void regulateTrainFrom15To0(){

    }

}
