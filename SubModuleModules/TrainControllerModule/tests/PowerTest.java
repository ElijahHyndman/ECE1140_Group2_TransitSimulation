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
    public void powerShouldBePositive(){
        theTrain.setSpeed(0);
        theTrain.setCommandedSpeed(15);
        theTrain.setBeacon(null);
        theTrain.setAuthority(20);

        int count = 0;
        while(count < 300){
            updateTheTrain(.1);
            if (theTrain.getActualSpeed() < control.getCommandedSpeed()){
                boolean powerPositive = control.getPower() >= 0;
                System.out.println(count + " : Power : " + control.getPower());
                assertThat(powerPositive, is(true));
            }
            System.out.println(count + " : Train Speed: " + theTrain.getActualSpeed());
            count++;
        }
    }

    @Test
    public void powerShouldBeNegative(){
        theTrain.setSpeed(15);
        theTrain.setCommandedSpeed(0);
        theTrain.setBeacon(null);
        theTrain.setAuthority(20);

        int count = 0;
        while(count < 300){
            updateTheTrain(.1);
            if (theTrain.getActualSpeed() > control.getCommandedSpeed()){
                boolean powerPositive = control.getPower() <= 0;
                System.out.println(count + " : Power : " + control.getPower());
                assertThat(powerPositive, is(true));
            }
            System.out.println(count + " : Train Speed: " + theTrain.getActualSpeed());
            count++;
        }
    }

    @Test
    public void regulateTrainFrom0To15(){
            theTrain.setSpeed(0);
            theTrain.setCommandedSpeed(15);
            theTrain.setBeacon(null);
            theTrain.setAuthority(20);

            int count = 0;
            while(count < 5000){
                System.out.println(count + " : Train Speed: " + theTrain.getActualSpeed());
                updateTheTrain(.1);
                count++;
            }
            boolean speedRegulated = (theTrain.getActualSpeed() <= 15+.5) && (theTrain.getActualSpeed() >= 15 - .5);

            assertThat(speedRegulated, is(true));
    }

    @Test
    public void regulateTrainFrom0To40ShouldCap(){
            theTrain.setSpeed(0);
            theTrain.setCommandedSpeed(40); //this is greater than train's max speed, (19.44) INVALID
            theTrain.setBeacon(null);
            theTrain.setAuthority(20);

            int count = 0;
            while(count < 5000){
                System.out.println(count + " : Train Speed : " + theTrain.getActualSpeed());
                updateTheTrain(.1);
                count++;
            }
            boolean trainReach40 = (theTrain.getActualSpeed() >= 40 - .1) && (theTrain.getActualSpeed() <= 40 +.1);
            assertThat(trainReach40, is(false)); //Invalid EC should be false
            boolean trainShouldNotExceed = (theTrain.getActualSpeed() >= 19.44 - .1) && (theTrain.getActualSpeed() <= 19.44+.1);
            assertThat(trainShouldNotExceed, is(true));
    }

    @Test
    public void regulateTrainFrom15To0(){
        theTrain.setSpeed(15);
        theTrain.setCommandedSpeed(0);
        theTrain.setBeacon(null);
        theTrain.setAuthority(20);

        int count = 0;
        while(count < 1000){
            System.out.println(count + " : Train Speed : " + theTrain.getActualSpeed());
            updateTheTrain(.1);
            count++;
        }

        boolean trainReaches0 = theTrain.getActualSpeed() == 0;
        assertThat(trainReaches0, is(true));

    }

    @Test
    public void regulateTrainFrom15To8(){
        theTrain.setSpeed(15);
        theTrain.setCommandedSpeed(8);
        theTrain.setBeacon(null);
        theTrain.setAuthority(20);

        int count = 0;
        while(count < 5000){
            System.out.println(count + " : Train Speed : " + theTrain.getActualSpeed());
            updateTheTrain(.1);
            count++;
        }

        boolean trainReaches8 = (theTrain.getActualSpeed() >= 8 - .5) && (theTrain.getActualSpeed() <= 8 + .5);
        assertThat(trainReaches8, is(true));
    }

}
