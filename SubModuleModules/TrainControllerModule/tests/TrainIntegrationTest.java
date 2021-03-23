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
    public void trainSetControlData(){

        theTrain.setAuthority(800);
        theTrain.setSpeed(15);
        theTrain.setCommandedSpeed(30);

        control.getTrainData();

        assertThat(control.getAuthority(), is(800.0));
        assertThat(control.getCommandedSpeed(), is(30.0));
        assertThat(control.getActualSpeed(), is(15.0));

        double power = control.getPower();

        control.setTrainData();

        assertThat(theTrain.getPower(), is(power));
    }

    @Test
    public void shouldDothis(){

    }

}
