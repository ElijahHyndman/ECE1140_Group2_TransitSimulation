import TrainModel.Train;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainTest {

    @Test
    void setDisplaySpeed() {
        Train t1 = new Train(5, 2, 0);

        t1.setDisplaySpeed(1);
        assertEquals(0.44704005836555,t1.getActualSpeed() );
    }

    @Test
    void calculateSpeed() {
        Train t1 = new Train(5, 2, 0);

        t1.setPower(50);
        t1.calculateSpeed(1);

        double force = (50 * 1000) / 1; //f is in Newtons = kg*m/s^2
        double newA = force/t1.getMass(); //A is in m/s^2
        double speed = 1 + (newA)/(2*1); // m/s + average of 2 accels / time

        assertEquals(speed, t1.getActualSpeed());

    }

    @Test
    void calculateMass() {
        Train t1 = new Train(5, 2, 0);

        t1.setPassengerCount(100);
        double mass = 37194 + 75*(102) ;

        assertEquals(44840.0, t1.getMass());
    }

    @Test
    void setDisplayAccel() {
        Train t1 = new Train(5, 2, 0);

        t1.setDisplayAccel(1);
        assertEquals(0.44704005836555,t1.getAccel() );
    }
    @Test
    void setPassengerBrake() {
        Train t1 = new Train(5, 2, 0);

        t1.setPassengerBrake(true);
        assertEquals(true, t1.getPassengerBrake());
    }
    @Test
    void setEmergencyBrake() {
        Train t1 = new Train(5, 2, 0);

        t1.setEmergencyBrake(true);
        assertEquals(true,t1.getEmergencyBrake() );
    }
}