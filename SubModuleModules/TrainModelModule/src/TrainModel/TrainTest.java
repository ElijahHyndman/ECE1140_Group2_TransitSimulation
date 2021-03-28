package TrainModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainTest {

    @Test
    void setDisplaySpeed() {
        Train t1 = new Train(5, 2);

        t1.setDisplaySpeed(1);
        assertEquals(.3047999902464003,t1.actualSpeed );
        assertEquals(1, t1.displayActualSpeed);
    }

    @Test
    void calculateSpeed() {
        Train t1 = new Train(5, 2);

        t1.setPower(50);
        t1.calculateSpeed(1);

        double force = (50 * 1000) / 1; //f is in Newtons = kg*m/s^2
        double newA = force/t1.mass; //A is in m/s^2
        double speed = 1 + (newA)/(2*1); // m/s + average of 2 accels / time;

        assertEquals(speed, t1.getActualSpeed());

    }

    @Test
    void calculateMass() {
    }

    @Test
    void setDisplayAccel() {
    }
}