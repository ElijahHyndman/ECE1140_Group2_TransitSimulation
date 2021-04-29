package implementation;

import java.util.concurrent.TimeUnit;

public class BackupMotor implements TrainMotor{

    private double power;
    private double Kp;
    private double Ki;
    private SimplyPID thePID;
    private double setpoint;



    public BackupMotor(){
        power = 0;
        Kp = 8;//3; // Default Kp
        Ki = 5; // Default Ki
        setpoint = 0;


        thePID = new SimplyPID(0, Kp, Ki, 0);
        thePID.setOutputLimits(-120,120);
    }

    public double getPower(double deltaTime, double idealVelocity, double trainVelocity){
        double actual = trainVelocity;
        if (setpoint != idealVelocity){
            setpoint = idealVelocity;
            thePID.setSetpoint(setpoint);
        }

        if (idealVelocity == 0 && trainVelocity == 0){
            power = 0;
        }else if (thePID != null){
            power = thePID.getOutput(deltaTime, actual);
        }
        return power;
    }

    public void setKpKi(double newKp, double newKi){
        Kp = newKp;
        Ki = newKi;

        thePID.setkP(Kp);
        thePID.setkI(Ki);
        thePID.setOutputLimits(-120, 120);
    }

    public void switchCurrent(){
        //do nothing
    }

}


