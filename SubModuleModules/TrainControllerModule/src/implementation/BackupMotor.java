package implementation;

import java.util.concurrent.TimeUnit;

public class BackupMotor implements TrainMotor{

    private double power;
    private double Kp;
    private double Ki;
    private double totalError;
    private double prevOutput;
    private double prevSpeed;
    private double maxIOutput=0;
    private double maxOutput=0;
    private double minOutput=0;
    private double iTerm = 0;
    private double lastError = 0;
    private double integral = 0;
    private double output = 0;
    boolean firstRun;
    private double Kd = 0;
    private double maxError=0;
    private double outputRampRate=0;

    public BackupMotor(){
        power = 0;
        Kp = 10.5;//3; // Default Kp
        Ki = 1; // Default Ki
        firstRun = true;
    }

    public double getPower(double idealVelocity, double trainVelocity){
        power = update(idealVelocity, trainVelocity);
        return power;
    }

    public void setKpKi(double newKp, double newKi){
        Kp = newKp;
        Ki = newKi;

        setBounds(-120, 120);
    }

    public Double update(double setPoint, double currValue){
        double pTerm;
        double output;

        double dt = 1; //(double)(currTime - lastTime) / TimeUnit.SECONDS.toNanos(1);

        double error = setPoint - currValue;
        double deriv = (error - lastError) / dt;

        // Calculate P term
        pTerm=Kp*error;

        if (firstRun) {
            lastError = setPoint - currValue;
            prevSpeed=currValue;
            prevOutput=pTerm;
            firstRun=false;
        }

        prevSpeed=currValue;

        //Calculate I term
        iTerm=Ki*integral;

        output = pTerm + iTerm;

        // Figure out what we're doing with the error.
        if(minOutput!=maxOutput && !isWithinBounds(output, minOutput,maxOutput) ){
            integral=error;
        } else{
            integral+=error*dt;
        }

        //lastTime = currTime;
        lastError = error;
        prevOutput = output;

        return output;
    }

    public void setBounds(double minimum,double maximum){
        if(maximum<minimum)return;
        maxOutput=maximum;
        minOutput=minimum;
    }

    private boolean isWithinBounds(double value, double min, double max){
        //check if power value is within max/min bounds
        return (min<value) && (value<max);
    }

}


