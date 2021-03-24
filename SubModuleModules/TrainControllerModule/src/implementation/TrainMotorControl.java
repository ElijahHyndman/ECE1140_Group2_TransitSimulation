package implementation;

public class TrainMotorControl implements TrainMotor{

    private TrainMotor mainMotor;
    private TrainMotor backupMotor;
    private double prevCommandedVelocity;
    private double currentPower;


    public TrainMotorControl(TrainMotor primaryMotor, TrainMotor secondaryMotor){
        mainMotor = primaryMotor;
        backupMotor = secondaryMotor;
        prevCommandedVelocity = 0;
        currentPower = 0;
    }

    public double getPower(double idealVelocity, double trainVelocity){

        double mainPower = mainMotor.getPower(idealVelocity, trainVelocity);
        double backupPower = backupMotor.getPower(idealVelocity, trainVelocity);

        boolean samePower = mainPower == backupPower;

        if (samePower){
            return mainPower;
        }else if (mainPower > (backupPower - 10) && mainPower < (backupPower + 10)){
            return mainPower;
        }

        //TO DO: checking for errors, switching to backup if needed.

        return mainPower;
    }

    public void setKpKi(double newKp, double newKi){

    }
}
