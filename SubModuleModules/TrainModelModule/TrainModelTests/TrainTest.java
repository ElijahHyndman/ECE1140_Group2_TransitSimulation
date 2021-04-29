import TrainModel.Train;
import org.junit.jupiter.api.Test;

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
    void calculateSpeedWithBlockIncline(){
        Train t1 = new Train(5,2,0);

        t1.setPower(100);
        t1.setBlockGrade(1);
        t1.calculateSpeed(1);

        double force = (100*1000)/1;
        force = force - (1/100)*9.81*t1.getMass();
        double newA = force/t1.getMass();
        double speed = 1 + newA/2;

        assertEquals(Math.round(speed), Math.round(t1.getActualSpeed()));
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
    void serviceBrakeAffectsSpeed() {
        Train t1 = new Train(5, 2, 0);

        t1.setSpeed(10);
        t1.setServiceBrake(true);
        t1.updatePhysicalState("", 1);

        assertEquals(8.8,t1.getActualSpeed() );
    }
    @Test
    void setPassengerBrake() {
        Train t1 = new Train(5, 2, 0);

        t1.setSpeed(10);
        t1.setPassengerBrake(true);
        t1.calculateSpeed(1);


        assertEquals(10-2.73, t1.getActualSpeed());
    }
    @Test
    void setEmergencyBrake() {
        Train t1 = new Train(5, 2, 0);

        t1.setSpeed(10);
        t1.setEmergencyBrake(true);
        t1.calculateSpeed(1);

        assertEquals(10-2.73,t1.getActualSpeed() );
    }
    @Test
    void changingAtStation() {
        Train t1 = new Train(5, 2, 0);

        t1.setPassengerCount(20);
        t1.setPassengersBoarding(10);
        int leaving = t1.disembark();

        assertEquals(30-leaving,t1.getPassengerCount());
    }

    @Test
    void engineFail() {
        Train t1 = new Train(5, 2, 0);

        t1.setPower(50);
        t1.setEngineFail(true);

        assertEquals(0,t1.getPower() );
    }

    @Test
    void brakeFail() {
        /*
        Testing two different trains to check whether a brake fail properly disables the service brake.

         */
        Train t1 = new Train(5, 2, 0);
        Train t2 = new Train(5, 2, 0);

        t1.setPower(50);
        t2.setPower(50);
        t1.updatePhysicalState(" ", 5);
        t2.updatePhysicalState(" ", 5);

        t1.setBrakeFail(true);
        t1.setServiceBrake(true);
        t1.updatePhysicalState("",1);
        t2.updatePhysicalState("",1);

        assertEquals(t2.getActualSpeed(),t1.getActualSpeed() );
    }

    @Test
    void setNonVitals(){
        /*
        Testing if non-vitals are properly set after being given command to do so
         */
        Train t1 = new Train(5, 2, 0);

        t1.setCabinTemp(1);
        t1.setAdvertisements(2);
        t1.setAnnouncements("Hi");
        t1.setNextStop("stopname");
        t1.setCabinLights(true);
        t1.setHeadlights(true);
        t1.setOuterLights(true);

        assertEquals(1,t1.getCabinTemp() );
        assertEquals(2,t1.getAdvertisements() );
        assertEquals("Hi",t1.getAnnouncements() );
        assertEquals("stopname",t1.getNextStop() );
        assertEquals(true,t1.getCabinLights() );
        assertEquals(true,t1.getOuterLights() );
        assertEquals(true,t1.getHeadlights() );

    }

}