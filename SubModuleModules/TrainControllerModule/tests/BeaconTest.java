import TrainModel.Train;
import implementation.TrainControl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BeaconTest {

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
    public void trainReadsBeaconAndStopsAtCorrectDistance(){
        theTrain.setSpeed(10);
        theTrain.setCommandedSpeed(10);
        theTrain.setBeacon("Dormont: 400 : L");
        theTrain.setAuthority(888);
        updateTheTrain(.1);
        updateTheTrain(.1);
        theTrain.setAuthority(20);
        theTrain.setBeacon(null);
        updateTheTrain(.1);

        boolean stop = false;
        while(!stop){
            updateTheTrain(.1);
            double power = control.getPower();
            assertThat(theTrain.getPower(), is(power));
            if (theTrain.getActualSpeed() == 0){
                stop = true;
            }
            System.out.println(control.getActualSpeed());
        }
        System.out.println("TOTAL DISTANCE: " + control.getTotalDistance());

        boolean trainStopped = theTrain.getActualSpeed() == 0;
        assertThat(trainStopped, is(true));

        boolean correctDistance = (control.getTotalDistance() >= 400 - 2) && (control.getTotalDistance() <= 400 +2);
        assertThat(correctDistance, is(true));
    }
}
