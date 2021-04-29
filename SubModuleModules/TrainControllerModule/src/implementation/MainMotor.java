package implementation;

/**MainMotor is the primary motor to
 * use for the Train's power calculation.
 * MainMotor utilizes a third party PID
 * Controller object to calculate the power
 * output. Default Kp and Ki values are
 * 10.5 and 1.
 *
 * ECE1140
 * Reagan Dowling
 */

public class MainMotor implements TrainMotor {

    private double acceleration;
    private double power;
    private double Kp;
    private double Ki;
    private PIDController PID;


    public MainMotor(){

        acceleration = 0;
        power = 0;
        Kp = 10.5;//3; // Default Kp
        Ki = 1; // Default Ki

        PID = new PIDController(Kp, Ki, 0);
        PID.setOutputLimits(120);
    }

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

    public double getKp(){
        return Kp;
    }

    public double getKi(){
        return Ki;
    }

}
