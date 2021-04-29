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

    private TrainMotor main;
    private TrainMotor backup;
    private TrainMotor active;
    private double Kp;
    private double Ki;

    public ActiveMotor(TrainMotor mainMotor, TrainMotor backupMotor){
        main = mainMotor;
        backup = backupMotor;

        active = main;

        Kp = 10.5;
        Ki = 1;
    }

    public double getPower(double idealVelocity, double trainVelocity){

        double mainPower = main.getPower(idealVelocity, trainVelocity);
        double backupPower = backup.getPower(idealVelocity, trainVelocity);

        boolean powerShouldBeNegative = idealVelocity < (trainVelocity);
        boolean powerShouldBePositive = idealVelocity > trainVelocity;

        boolean mainPowerFail;
        boolean backupPowerFail;


        if (powerShouldBeNegative){
            mainPowerFail = mainPower > -1;
            backupPowerFail = backupPower > -1;
            if (mainPowerFail && !backupPowerFail){
                active = backup;
                //backup = main;
            }
        }else if (powerShouldBePositive){
            mainPowerFail = mainPower < 0;
            backupPowerFail = backupPower < 0;
            if (mainPowerFail && !backupPowerFail){
                active = backup;
            }
        }

        return mainPower;
    }

    public void setKpKi(double newKp, double newKi){

        Kp = newKp;
        Ki = newKi;

        main.setKpKi(newKp, newKi);
        backup.setKpKi(newKp, newKi);

    }

    public double getKp(){
        return Kp;
    }

    public double getKi(){
        return Ki;
    }

}
