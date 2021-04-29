import TrainModel.Train;
import implementation.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;



/** Skeleton class for TrainControl test cases
 *
 * Reagan Dowling
 * 02/22/2021
 */

public class TrainIntegrationTest {

    private TrainControl control;
    private Train theTrain;

    public void updateTheTrain(double deltaTime){
        theTrain.updatePhysicalState("nothing", deltaTime);
        control.updateCommandOutputs("06:30:55", deltaTime);
    }


    @Before
    public void setUp(){
        theTrain = new Train(5,4,0);
        control = new TrainControl(theTrain);
        theTrain.setBeacon("Dormont: 350 : L");
        //theTrain.setBeacon("Dormont: 200");
    }

    @Test
    public void controlShouldUseTrainData(){

        theTrain.setAuthority(800);
        theTrain.setCommandedSpeed(30);
        control.getTrainData();
        assertThat(control.getAuthority(), is(800));
        assertThat(control.getCommandedSpeed(), is(30.0));

        theTrain.setSpeed(15);
        theTrain.setCommandedSpeed(30);
        control.getTrainData();
        assertThat(control.getActualSpeed(), is(15.0));
    }

    @Test
    public void controlShouldSetTrainPower() {

        //first set controller values
        theTrain.setAuthority(800);
        theTrain.setSpeed(0);
        theTrain.setCommandedSpeed(8);

        control.getTrainData();

        double power = control.getPower();

        boolean powerNotZero = power > 0;
        assertThat(powerNotZero, is(true));

        control.setTrainData();
        assertThat(theTrain.getPower(), is(power));

    }

    @Test
    public void controlShouldGetUpdatedActualSpeed(){
        //first set controller values
        theTrain.setAuthority(800);
        theTrain.setSpeed(0);
        theTrain.setCommandedSpeed(30);

        control.getTrainData();
        double initialTrainVelocity = control.getActualSpeed();

        double power = control.getPower();

        boolean powerNotZero = power > 0;
        assertThat(powerNotZero, is(true));

        control.setTrainData();
        assertThat(theTrain.getPower(), is(power));

        //Update train data, actual speed should increase
        control.getTrainData();
        boolean changedActualSpeed = control.getActualSpeed() > initialTrainVelocity;
        System.out.println(control.getActualSpeed());
        //assertThat(changedActualSpeed, is(true));

    }



    @Test
    public void runSpeedRegulation(){

        //first set controller values
        theTrain.setAuthority(150000);
        theTrain.setSpeed(0);
        theTrain.setCommandedSpeed(6);

       // theTrain.setCommandedSpeed(10);
        System.out.println("-----start over-----");
        updateTheTrain(.1);

        double initialTrainVelocity = control.getActualSpeed();

        //int i = 0;
        boolean stop = false;
        while(!stop){
            updateTheTrain(.1);
            double power = control.getPower();
            assertThat(theTrain.getPower(), is(power));
            if (theTrain.getActualSpeed() == 0){
                stop = true;
            }
            System.out.println(control.getActualSpeed());
            initialTrainVelocity = control.getActualSpeed();
        }

        System.out.println("TOTAL DISTANCE: " + control.getTotalDistance());
       // control.updateCommandOutputs("test time", 1.0);
       // boolean speedRegulated = ((control.getActualSpeed() >= control.getCommandedSpeed() - .001) &&
                //control.getActualSpeed() <= control.getCommandedSpeed() + .001);
        //assertThat(speedRegulated, is(true));

    }
}
