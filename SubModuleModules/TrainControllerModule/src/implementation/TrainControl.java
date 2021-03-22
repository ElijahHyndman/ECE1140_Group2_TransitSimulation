package implementation;

import TrainModel.Train;
import systemData.trackData;


public class TrainControl {

    private final TrainMotor motor;
    private final Train trainModel;
    private final NonVitalComponents nonVitalComponents;
    private trackData track;

    private double velocityCmd; //the train's ideal velocity, units km/h
    private double trainVelocity; //the actual velocity of the train, units km/h
    private double inputVelocity; //the driver's inputted velocity, in mph
    private double power; // train power, in kW
    private double acceleration; //the trains ideal acceleration, in m/s
    private double authority; // the trains authority, in meters
    private double speedLimit; // train speed limit, in km/h
    private String beacon;
    private final double emergencyBrake; //emergency brake deceleration rate, in m/s
    private final double serviceBrake; //service brake deceleration rate, in m/s
    private String controlMode; //the current mode of the controller, either automatic or manual
    private double prevVelocity; //the last inputted train velocity
    private double shouldBrake;
    private double totalDistanceTraveled; //total train distance traveled, in meters
    private boolean eBrake;
    private boolean sBrake;
    private String alert;

    public TrainControl(){
      this(null);
    }

    public TrainControl(Train model){

        trainModel = model;
        controlMode = "Automatic";
        eBrake = false;
        sBrake = false;
        emergencyBrake = -2.73;
        serviceBrake = -1.2;
        velocityCmd = 0;
        trainVelocity = 0;
        prevVelocity = 0;
        inputVelocity  = 0;
        power = 0;
        acceleration = 0;
        authority = 0;
        totalDistanceTraveled = 0;
        speedLimit = 0;
        beacon = null;
        alert = null;
        shouldBrake = 0;
        motor = new TrainMotor();
        track = new trackData("Blue");
        nonVitalComponents = new NonVitalComponents(track);
        controlNonVital();
    }

    public NonVitalComponents getNonVitalComponents(){
        controlNonVital();
        return nonVitalComponents;
    }

    public TrainMotor getTrainMotor(){
        return motor;
    }

    public String getControlMode(){
        return controlMode;
    }

    public void switchMode(){
        if (controlMode.equals("Automatic")){
            inputVelocity = velocityCmd;
            controlMode = "Manual";
        }else if (controlMode.equals("Manual")){
            controlMode = "Automatic";
        }
    }

    public double getSafeBreakingDistance(){
        double velocityMeters = (trainVelocity/3.6);
        //shouldBrake is meters distance
        shouldBrake = (-1)*(Math.pow(velocityMeters,2))/(2*serviceBrake);
        return shouldBrake;
    }

    public void monitorDistance(){
        if (authority <= 0){
            velocityCmd = 0;
        }
        double breakingDist = getSafeBreakingDistance();
        if (breakingDist != 0){
            if (authority <= (breakingDist+5)){
                if (getControlMode().equals("Automatic")){
                    sBrake = true;
                }else{
                    alert = "WARNING: Speed exceeds safe limit for current authority";
                }
            }
        }
    }

    public String getSystemMessage(){
        return alert;
    }

    public void getIdealAcceleration(){
        double idealAcceleration = 0;
        //First check emergency brake
        if (eBrake){
            acceleration = emergencyBrake;
        }else if(sBrake){
            acceleration = serviceBrake;
        }else{
            idealAcceleration = ((velocityCmd/3.6) - (trainVelocity/3.6))/(1);
            if (idealAcceleration > .5){
                acceleration = .5;
            }else if (idealAcceleration < -1.2){
                acceleration = -1.2;
            }else{
                acceleration = idealAcceleration;
            }
        }
    }

    public double getAcceleration(){
        return acceleration;
    }

    public double getPower(){
        return power;
    }

    //Returns the train's actual speed, in Km/h
    public double getActualSpeed(){
        return trainVelocity;
    }

    //Returns the trains commanded speed, in Km/h
    public double getCommandedSpeed(){
        return velocityCmd;
    }

    //Returns the track speed limit, in Km/h
    public double getSpeedLimit(){
        return speedLimit;
    }

    //Returns the current authority, in blocks?
    public double getAuthority(){
        return authority;
    }

    //Returns the current beacon
    public String getBeacon(){
        return beacon;
    }


    public void useServiceBrake(boolean inUse){
          if (inUse){
              sBrake = true;
          }else{
              sBrake = false;
          }
    }

    public void useEmergencyBrake(boolean inUse){
        if(inUse){
            eBrake = true;
        }
        else{
            eBrake = false;
        }
    }

    //Manual setpoint speed
    public String inputSpeed(double newSpeed){
        //Convert the driver input speed to Km/h
        double newMetricSpeed = newSpeed * 1.60934;
        if (getControlMode().equals("Automatic")){
            return "In Automatic Mode. Please switch to manual";
        }
        if (newMetricSpeed > speedLimit){
            return "Input speed exceeds speed limit";
        }
        if (getControlMode().equals("Manual")){
            inputVelocity = newMetricSpeed;
            return "Success";
        }else{
            return "In Automatic Mode. Please switch to manual";
        }
    }

    //Commanded Speed input from Train Model, in km/h
    public void setCommandedSpeed(double comSpeed){
        //First check emergency brake
        if (eBrake){
            velocityCmd = 3.6*(velocityCmd/3.6 + emergencyBrake*(1));
            if (velocityCmd <= 0){
                velocityCmd = 0;
            }
            inputVelocity = velocityCmd;
        //Check if service brake in use
        }else if (sBrake){
            velocityCmd = 3.6*(velocityCmd/3.6 + serviceBrake*(1));
            if (velocityCmd <= 0){
                velocityCmd = 0;
            }
            inputVelocity = velocityCmd;
        }else {
                velocityCmd = comSpeed;

        }
    }

    //Actual Speed input from Train Model, in km/h
    public void setActualSpeed(double speed){
        //ACTUAL acceleration in m/s/s
        double distanceTraveled;
        double actualAcceleration;
        //1 s sample time
        actualAcceleration = ((speed/3.6) - (prevVelocity/3.6))/(1);
        distanceTraveled = ((prevVelocity/3.6) + .5*(actualAcceleration*(Math.pow(1,2))));
        if (distanceTraveled > authority){
            authority = 0;
        }else {
            if (distanceTraveled >= 0){
                totalDistanceTraveled += distanceTraveled;
                authority = authority - distanceTraveled;
            }
        }
        prevVelocity = trainVelocity;
        trainVelocity = speed;
        monitorDistance();
        getIdealAcceleration();
    }

    //Speed Limit input from Train Model, in km/h
    public void setSpeedLimit(double theLimit){ speedLimit = theLimit; }

    //Authority input from Train Model, in blocks
    public void setAuthority(double dist){
        authority = trackData.getDistance(dist);
        //IN METERS
    }

    //Setting the train's nonVital Components
    public void controlNonVital(){
        nonVitalComponents.setTemperature();
        nonVitalComponents.setCabinLights();
        nonVitalComponents.setExternalLights();
        nonVitalComponents.setNextStation(beacon);
        nonVitalComponents.setAnnouncement(authority, beacon);

        if (authority  == 0){
            nonVitalComponents.setDoors(beacon);
        }
    }

    //Replicating inputs from the Train Model, used by TestingUI
    public void newTrainInput(TrainModelInput currentInput){
        setActualSpeed(currentInput.getActualSpeed());

        if (getControlMode().equals("Automatic")){
            setCommandedSpeed(currentInput.getCommandedVelocity());
        }else{
                setCommandedSpeed(inputVelocity);
            }

        setSpeedLimit(currentInput.getSpeedLimit());
        beacon = currentInput.getBeacon();

        //monitorDistance();
        power = motor.getPower(velocityCmd, trainVelocity);
    }


    /**
     * NEW METHODS FOR TRAIN MODEL
     **/

    public void getTrainData(){
        setActualSpeed(trainModel.getActualSpeed());
    }

    public void setTrainData(){
        trainModel.setPower(power);
    }

}
