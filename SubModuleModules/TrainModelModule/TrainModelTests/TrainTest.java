import TrainModel.*;

import org.junit.jupiter.api.Test;

class TrainTest {

    @Test
    void getNumberOfCars() {
        Train train1 = new Train(5, 1);
        assert(5 == train1.getNumberOfCars());
    }

    @Test
    void getCommandedSpeed() {
    }

    @Test
    void getAuthority() {
    }

    @Test
    void setPower() {
        Train train1 = new Train(5,2);
        train1.setSpeed(50);
        train1.setAccel(.5);
        train1.setPower(10);
    }
}