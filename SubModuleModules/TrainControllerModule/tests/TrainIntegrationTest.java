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

    @Before
    public void setUp(){
        theTrain = new Train(5,4);
        control = new TrainControl(theTrain);
    }

    @Test
    public void controlShouldUseTrainData(){

        theTrain.setAuthority(800);
        theTrain.setCommandedSpeed(30);
        control.getTrainData();
        assertThat(control.getAuthority(), is(800.0));
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
        theTrain.setSpeed(15);
        theTrain.setCommandedSpeed(30);

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
        theTrain.setSpeed(15);
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
        assertThat(changedActualSpeed, is(true));

    }


    @Test
    public void controlShouldRegulateTrainToCommandedSpeed(){

        //first set controller values
        theTrain.setAuthority(15000);
        theTrain.setSpeed(0);
        theTrain.setCommandedSpeed(30);

        control.getTrainData();

        double initialTrainVelocity = control.getActualSpeed();

        while(!(control.getActualSpeed() == control.getCommandedSpeed())){

            double power = control.getPower();

            control.setTrainData();
            assertThat(theTrain.getPower(), is(power));

            //Update train data, actual speed should increase
            control.getTrainData();
            if (control.getActualSpeed() < control.getCommandedSpeed()){
                boolean changedActualSpeed = control.getActualSpeed() > initialTrainVelocity;
                System.out.println(control.getActualSpeed());
                assertThat(changedActualSpeed, is(true));
            }

            initialTrainVelocity = control.getActualSpeed();
        }


        assertThat(control.getActualSpeed(), is(theTrain.getActualSpeed()));
        boolean speedRegulated = ((theTrain.getActualSpeed() == control.getCommandedSpeed()));
        assertThat(speedRegulated, is(true));

    }
}
