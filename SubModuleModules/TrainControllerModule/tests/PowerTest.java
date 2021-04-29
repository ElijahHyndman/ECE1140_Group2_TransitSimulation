import TrainModel.Train;
import implementation.TrainControl;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PowerTest {

    TrainControl control;
    Train theTrain;


    @Before
    public void setUp(){
        theTrain = new Train(5,10,1);


    }
}
