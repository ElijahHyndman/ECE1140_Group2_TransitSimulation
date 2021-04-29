/*
 * Copyright (c) 2018 Charles Grassin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package implementation;

/**
 * A simple PID closed control loop.
 * <br><br>
 * License : MIT
 * @author Charles Grassin
 */
public class SimplyPID {
    // PID coefficients
    private double setPoint;
    private double kP,kI,kD;

    /** Limit bound of the output. */
    private double minLimit = Double.NaN ,maxLimit = Double.NaN;

    // Dynamic variables
    private double lastError = 0;
    private double integralError = 0;
    private double power;

    /**
     * Constructs a new PID with set coefficients.
     *
     * @param setPoint The initial target value.
     * @param kP The proportional gain coefficient.
     * @param kI The integral gain coefficient.
     * @param kD The derivative gain coefficient.
     */
    public SimplyPID(final double setPoint, final double kP, final double kI, final double kD) {
        this.setSetpoint(setPoint);
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    /**
     * Updates the controller with the current time and value
     * and outputs the PID controller output.
     *
     * @param currentTime The current time (in arbitrary time unit, such as seconds).
     * If the PID is assumed to run at a constant frequency, you can simply put '1'.
     * @param currentValue The current, measured value.
     *
     * @return The PID controller output.
     */
    public double getOutput(final double dt, final double currentValue) {
        final double error = setPoint - currentValue;



        // Compute Integral error
        integralError += (error+lastError) * dt/2;

        lastError = error;

        power = (kP * error) + (kI * integralError);
        if (power < minLimit){
            power = minLimit;
        }else if(power>=maxLimit){
            power = maxLimit;
            integralError = integralError- ((error+lastError)*dt/2);
        }

        return power;
        //return checkLimits((kP * error) + (kI * integralError));
    }

    /**
     * Resets the integral and derivative errors.
     */
    public void reset() {
        lastError = 0;
        integralError = 0;
    }

    /**
     * Bounds the PID output between the lower limit
     * and the upper limit.
     *
     * @param output The target output value.
     * @return The output value, bounded to the limits.
     */
    private double checkLimits(final double output){
        if (!Double.isNaN(minLimit) && output < minLimit)
            return minLimit;
        else if (!Double.isNaN(maxLimit) && output > maxLimit)
            return maxLimit;
        else
            return output;
    }

    //Getters & Setters

    /**
     * Sets the output limits of the PID controller.
     * If the minLimit is superior to the maxLimit,
     * it will use the smallest as the minLimit.
     *
     *	@param minLimit The lower limit of the PID output.
     *	@param maxLimit The upper limit of the PID output.
     */
    public void setOutputLimits(final double minLimit, final double maxLimit) {
        if(minLimit < maxLimit) {
            this.minLimit = minLimit;
            this.maxLimit = maxLimit;
        }else{
            this.minLimit = maxLimit;
            this.maxLimit = minLimit;
        }
    }

    /**
     * Removes the output limits of the PID controller
     *
     * @param setPoint The new target point.
     */
    public void removeOutputLimits() {
        this.minLimit = Double.NaN;
        this.maxLimit = Double.NaN;
    }

    /**
     * @return the kP parameter
     */
    public double getkP() {
        return kP;
    }

    /**
     * @param kP the kP parameter to set
     */
    public void setkP(double kP) {
        this.kP = kP;
        reset();
    }

    /**
     * @return the kI parameter
     */
    public double getkI() {
        return kI;
    }

    /**
     * @param kI the kI parameter to set
     */
    public void setkI(double kI) {
        this.kI = kI;
        reset();
    }

    /**
     * @return the kD parameter
     */
    public double getkD() {
        return kD;
    }

    /**
     * @param kD the kD parameter to set
     */
    public void setkD(double kD) {
        this.kD = kD;
        reset();
    }

    /**
     * @return the setPoint
     */
    public double getSetPoint() {
        return setPoint;
    }

    /**
     * Establishes a new set point for the PID controller.
     *
     * @param setPoint The new target point.
     */
    public void setSetpoint(final double setPoint) {
        reset();
        this.setPoint = setPoint;
    }
}
