import implementation.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;



/** Test class for Train Control. This class
 * independently tests the train control methods
 * without coupling to a train model.
 *
 * Reagan Dowling
 * 03/23/2021
 */

public class TrainControlTest {

    private TrainControl control;

    @Before
    public void setUp(){
        control = new TrainControl();
    }

    @Test
    public void testSetKpKi(){

        MainMotor motor = (MainMotor) control.getTrainMotor();

        control.setKpKi(5,.001);
        double Kp = motor.getKp();
        double Ki = motor.getKi();

        assertThat(Kp, is(5.0));
        assertThat(Ki, is(.001));

    }

    @Test
    public void testManualServiceBrake(){
        assertThat(control.getControlMode(), is("Automatic"));

        control.setCommandedSpeed(35);
        double velocityCmd = control.getCommandedSpeed();
        assertThat(velocityCmd, is(35.0));

        control.useServiceBrake(true);

        //test that commanded speed is decreasing until 0
        while (velocityCmd > 0){
            control.setCommandedSpeed(velocityCmd);
            double velocityDec = control.getCommandedSpeed();
            boolean decreased = (velocityDec < velocityCmd);
            assertThat(decreased, is(true));
            velocityCmd = velocityDec;
        }

        assertThat(velocityCmd, is(0.0));
    }
}
