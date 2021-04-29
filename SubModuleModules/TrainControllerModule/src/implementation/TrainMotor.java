package implementation;

/**TrainMotor to use for the Train's
 * power calculation. Returns the
 * train's power and allows for setting
 * the Kp and Ki value.
 *
 * ECE1140
 * Reagan Dowling
 */

public interface TrainMotor {

    /**
     *
     *
     * @param idealVelocity
     * @param trainVelocity
     * @return
     */
    public double getPower(double deltaTime, double idealVelocity, double trainVelocity);

    /**
     *
     *
     * @param newKp
     * @param newKi
     */
    public void setKpKi(double newKp, double newKi);

    public void switchCurrent();
}
