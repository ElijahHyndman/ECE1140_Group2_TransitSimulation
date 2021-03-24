package implementation;

public interface TrainMotor {

    public double getPower(double idealVelocity, double trainVelocity);

    public void setKpKi(double newKp, double newKi);
}
