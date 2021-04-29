package implementation;

/**Active Motor implements the TrainMotor interface
 * It serves as the context object in the state
 * design pattern. It either uses the MainMotor
 * for calculating power, or BackupMotor if
 * an issue is detected. ActiveMotor cross
 * checks the two diverse power calculation
 * values.
 *
 * Used for Safety Critical Architecture
 *
 * ECE1140
 * Reagan Dowling
 */

public class ActiveMotor implements TrainMotor{

    private TrainMotor mainMotor;
    private TrainMotor backupMotor;
    private TrainMotor active;
    boolean useMain;
    private double Kp;
    private double Ki;

    public ActiveMotor(TrainMotor mainMotor, TrainMotor backupMotor){
        this.mainMotor = mainMotor;
        this.backupMotor = backupMotor;

        Kp = 7;
        Ki = .1;

        switchCurrent();
    }

    public double getPower(double deltaTime, double idealVelocity, double trainVelocity){

        double mainPower = mainMotor.getPower(deltaTime, idealVelocity, trainVelocity);
        double backupPower = backupMotor.getPower(deltaTime, idealVelocity, trainVelocity);

        boolean powerShouldBeNegative = idealVelocity < (trainVelocity);
        boolean powerShouldBePositive = idealVelocity > trainVelocity;

        boolean mainPowerFail;
        boolean backupPowerFail;


        if (powerShouldBeNegative){
            mainPowerFail = mainPower > -1;
            backupPowerFail = backupPower > -1;
            if (mainPowerFail && !backupPowerFail){
                //active = backup;
                //backup = main;
            }
        }else if (powerShouldBePositive){
            mainPowerFail = mainPower < 0;
            backupPowerFail = backupPower < 0;
            if (mainPowerFail && !backupPowerFail){
                //active = backup;
            }
        }

        return mainPower;
    }

    public void setKpKi(double newKp, double newKi){

        Kp = newKp;
        Ki = newKi;

        mainMotor.setKpKi(newKp, newKi);
        backupMotor.setKpKi(newKp, newKi);

    }

    public void switchCurrent(){
        TrainMotor temp = null;
        temp = mainMotor;
        mainMotor = backupMotor;
        backupMotor = temp;
    }

    public double getKp(){
        return Kp;
    }

    public double getKi(){
        return Ki;
    }

}
