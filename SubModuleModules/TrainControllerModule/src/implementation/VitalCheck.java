package implementation;


/**
 * Vital component for power calculation redundancy.
 * This class also cross checks TrainControl operations
 *
 * author: Reagan Dowling
 * Date: 03/22/2021
 */
public class VitalCheck {

    private PIDController PID;
    private double Kp;
    private double Ki;
    private double power;
    private double trainVelocity;

    private double shouldBrake;
    private double serviceBrake;
    private boolean beaconSet;
    private boolean useServiceBrake;
    private double stoppingDistance;
   // private double emergencyBrake;

    public VitalCheck(){

        power = 0;
        Kp = 10.5; // Default Kp
        Ki = 1; // Default Ki

        PID = new PIDController(Kp, Ki, 0);
        PID.setOutputLimits(120);

        shouldBrake = 0;
        serviceBrake = -1.2;
        beaconSet = false;
        useServiceBrake = false;
        stoppingDistance = 0;
    }
    //TO DO

    public double getPower(double idealVelocity, double trainVelocity){
        double setpoint =   idealVelocity;
        double actual =  trainVelocity;

        if (idealVelocity == 0 && trainVelocity == 0){
            power = 0;
        }else if (PID != null){
            power = PID.getOutput(actual, setpoint);
        }
        return power;
    }

    public void setKpKi(double newKp, double newKi){
        Kp = newKp;
        Ki = newKi;

        PID.setPID(Kp, Ki, 0);
        PID.setOutputLimits(120);
    }

    public double getSafeBreakingDistance(){
        double velocityMeters = (trainVelocity);
        //shouldBrake is meters distance
        shouldBrake = (-1)*(Math.pow(velocityMeters,2))/(2*serviceBrake);
        return shouldBrake;
    }

    public void stopApproaching(double stationDistance){
        beaconSet = true;
        stoppingDistance = stationDistance;
    }

    public void monitorDistance(){
        double breakingDist = getSafeBreakingDistance();
        if (breakingDist > 0) {
            if (beaconSet){
                if (stoppingDistance <= breakingDist) {
                        useServiceBrake = true;
                }
            }else{
                useServiceBrake = false;
            }
        }
    }
}
