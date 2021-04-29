package implementation;

import TrainModel.Train;

/** TrainControl serves as the main control
 * entity for a Train object. When provided a
 * Train object in the constructor, it controls
 * all vital and non vital components.
 * TrainControl uses a TrainMotor for calculating
 * the power output to the Train object.
 *
 * ECE1140
 * Reagan Dowling
 */

public class TrainControl {

    private final TrainMotor motor;
    //private final TrainMotor backupMotor;
    private final Train trainModel;
    private final NonVitalComponents nonVitalComponents;

    private double velocityCmd; //the train's ideal velocity, units m/s
    private double trainVelocity; //the actual velocity of the train, units m/s
    private double manualVelocity; //the driver's inputted velocity, in mph
    private double power; // train power, in kW
    private double acceleration; //the trains ideal acceleration, in m/s
    private int authority; // the trains authority, in blocks
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
    private double sampleTime;
    public double stoppingDistance;
    boolean beaconSet;
    private double route;
    String simTime;
    private String doors = null;
    private String nextStop = null;

    public TrainControl(){
      this(null);
    }

    public TrainControl(Train model){
        trainModel = model;
        //state design pattern
        motor = new ActiveMotor(new MainMotor(), new BackupMotor());

        simTime = null;

        beaconSet = false;
        controlMode = "Automatic";
        route = 0;
        eBrake = false;
        sBrake = false;
        emergencyBrake = -2.73;
        serviceBrake = -1.2;
        velocityCmd = 0;
        trainVelocity = 0;
        prevVelocity = 0;
        manualVelocity = 0;
        power = 0;
        acceleration = 0;
        authority = -2;
        totalDistanceTraveled = 0;
        stoppingDistance = -1;
        speedLimit = 0;
        beacon = null;
        alert = null;
        shouldBrake = 0;
        nonVitalComponents = new NonVitalComponents();
        sampleTime = .1;
        controlNonVital();
    }

    public NonVitalComponents getNonVitalComponents(){
        controlNonVital();
        return nonVitalComponents;
    }

    //Setting the Train objects non-vital components
    public void setNonVitalComponents(){
        controlNonVital();

        int temperature = nonVitalComponents.getTemperature();
        boolean headlights = nonVitalComponents.getHeadLights();
        boolean cabinLights = nonVitalComponents.getCabinLights();
        boolean externalLights = nonVitalComponents.getExternalLights();
        boolean rightDoors = nonVitalComponents.getRightDoors();
        boolean leftDoors = nonVitalComponents.getLeftDoors();
        String announcement = nonVitalComponents.getAnnouncements();

        trainModel.setAnnouncements(announcement);
        trainModel.setCabinTemp(temperature);
        trainModel.setHeadlights(headlights);
        trainModel.setCabinLights(cabinLights);
        trainModel.setOuterLights(externalLights);
        trainModel.setRightDoors(rightDoors);
        trainModel.setLeftDoors(leftDoors);
        if(beacon != null){
            trainModel.setNextStop(beacon.substring(0,beacon.indexOf(" ")));
        }
    }

    public TrainMotor getTrainMotor(){
        return motor;
    }

    public String getControlMode(){
        return controlMode;
    }

    public void switchMode(){
        if (controlMode.equals("Automatic")){
            manualVelocity = velocityCmd;
            controlMode = "Manual";
        }else if (controlMode.equals("Manual")){
            controlMode = "Automatic";
        }
    }

    public double getSafeBreakingDistance(){
        double velocityMeters = (trainVelocity);
        //shouldBrake is meters distance
        shouldBrake = (-1)*(Math.pow(velocityMeters,2))/(2*serviceBrake);
        return shouldBrake;
    }

    public void monitorDistance(){

        double breakingDist = getSafeBreakingDistance();
        if (breakingDist > 0) {
            if (stoppingDistance != -1){
               // TODO I could not determine how to resolve between this line and the one below it. Someone better than me, check this out and choose the correct one
               // if (stoppingDistance <= breakingDist) {
               if (stoppingDistance-(sampleTime*prevVelocity) <= breakingDist) {
                    if (getControlMode().equals("Automatic")) {
                        useServiceBrake(true);
                    } else {
                        alert = "WARNING: Speed exceeds safe limit for current authority";
                    }
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
            idealAcceleration = ((velocityCmd) - (trainVelocity))/(sampleTime);
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

    //Returns the train's actual speed, in m/s
    public double getActualSpeed(){
        return trainVelocity;
    }

    //Returns the trains commanded speed, in m/s
    public double getCommandedSpeed(){
        return velocityCmd;
    }

    //Returns the track speed limit, in m/s
    public double getSpeedLimit(){
        return speedLimit;
    }

    //Returns the current authority, in blocks?
    public int getAuthority(){
        return authority;
    }

    //Returns the current beacon
    public String getBeacon(){
        return beacon;
    }


    public void useServiceBrake(boolean inUse){
          if (inUse){
              sBrake = true;
              trainModel.setServiceBrake(true);
          }else{
              sBrake = false;
              trainModel.setServiceBrake(false);
          }
    }

    public void useEmergencyBrake(boolean inUse){
        if(inUse){
            eBrake = true;
            trainModel.setEmergencyBrake(true);
        }
        else{
            eBrake = false;
            trainModel.setEmergencyBrake(false);
        }
    }

    //Manual setpoint speed
    public String inputSpeed(double newSpeed){
        //Convert the driver input speed to Km/h
        double newMetricSpeed = newSpeed/2.237;
        if (getControlMode().equals("Automatic")){
            return "In Automatic Mode. Please switch to manual";
        }
        if (newMetricSpeed > speedLimit){
            return "Input speed exceeds speed limit";
        }
        if (getControlMode().equals("Manual")){
            manualVelocity = newMetricSpeed;
            return "Success";
        }else{
            return "In Automatic Mode. Please switch to manual";
        }
    }

    //Commanded Speed input from Train Model
    public void setCommandedSpeed(double comSpeed){
        //First check emergency brake
        if (authority >= 0){
            if (eBrake){
                velocityCmd = (velocityCmd + emergencyBrake*(sampleTime));
                if (velocityCmd <= 0){
                    velocityCmd = 0;
                }
                manualVelocity = velocityCmd;
                //Check if service brake in use
            }else if (sBrake){
                velocityCmd = (velocityCmd + serviceBrake*(sampleTime));
                if (velocityCmd <= 0){
                    velocityCmd = 0;
                }
                manualVelocity = velocityCmd;
            }else {
                velocityCmd = comSpeed;
            }
        }
    }

    //Actual Speed input from Train Model, in km/h
    public void setActualSpeed(double speed){
        //ACTUAL acceleration in m/s/s
        double distanceTraveled;
        double actualAcceleration;
        //1 s sample time

        // TODO this was commented out, should it be commented out?
        /*
        actualAcceleration = ((speed) - (prevVelocity))/(sampleTime);
        distanceTraveled = ((prevVelocity*sampleTime) + .5*(actualAcceleration*(Math.pow(sampleTime,2))));
        //System.out.println(distanceTraveled + " from " + totalDistanceTraveled);
        totalDistanceTraveled += distanceTraveled;

        if (beaconSet){
            stoppingDistance = stoppingDistance - distanceTraveled;
        }
        */

        prevVelocity = trainVelocity;
        trainVelocity = speed;


        distanceTraveled = trainVelocity * sampleTime;
        totalDistanceTraveled += distanceTraveled;
        if (beaconSet){
            stoppingDistance = stoppingDistance - distanceTraveled;
        }

        monitorDistance();
        getIdealAcceleration();
    }

    public double getTotalDistance(){
        return totalDistanceTraveled;
    }
    public void setPower(){
        power = (motor.getPower(sampleTime, velocityCmd, trainVelocity));

    }

    //Speed Limit input from Train Model, in km/h
    public void setSpeedLimit(double theLimit){ speedLimit = theLimit; }

    //Authority input from Train Model, in blocks
    public void setAuthority(int distBlock){
        authority = distBlock;
        if (authority == 0 && !beaconSet){
            useEmergencyBrake(true);
        }else if(authority != 0 && beaconSet && trainVelocity == 0){
            beaconSet = false;
            stoppingDistance = -1;
            useEmergencyBrake(false);
            useServiceBrake(false);
        }
    }

    public void setBeacon(String currentBeacon){
        beacon = currentBeacon;
        //Check if beacon not null and authority is 888
        if (!(beacon==null) && !beaconSet && authority==888){
            beaconSet = true;
            int start = beacon.indexOf(" ");
            String half = beacon.substring(start+1);
            double stop = Double.parseDouble(half.substring(0, half.indexOf(":")));
            int doorString = half.indexOf(" ");
            doors = half.substring(doorString + 1);
            System.out.println(stop);
            stoppingDistance = stop;
        }else if (beacon == null && !beaconSet && authority!=888){
            stoppingDistance = -1;
            beaconSet = false;
            doors = null;
        }else if(beacon == null && authority == 0){
//            beaconSet = false;
//            stoppingDistance = -1;
        }
    }

    public double getStoppingDistance(){
        return stoppingDistance;
    }

    public void setKpKi(double newKp, double newKi){
        motor.setKpKi(newKp, newKi);
    }

    //Setting the train's nonVital Components
    public void controlNonVital(){
        nonVitalComponents.setTemperature();
        nonVitalComponents.setCabinLights(simTime);
        nonVitalComponents.setExternalLights(simTime);
        nonVitalComponents.setNextStation(beacon);
        nonVitalComponents.setAnnouncement(authority, trainVelocity);
    }

    public void openDoorAtStation(boolean door){
        if (door){
            nonVitalComponents.setDoors(doors);
        }else {
            nonVitalComponents.setDoors(null);
        }
        trainModel.setRightDoors(nonVitalComponents.getRightDoors());
        trainModel.setLeftDoors(nonVitalComponents.getLeftDoors());
    }

    /**
     * NEW METHODS FOR TRAIN MODEL
     **/

    public void updateCommandOutputs(String currentTime, double deltaTime){
        sampleTime = deltaTime;
        simTime = currentTime;
        getTrainData();
        setTrainData();
    }

    public void getTrainData() {
        boolean passengerBrake = trainModel.getEmergencyBrake();
        if (passengerBrake || trainModel.getBrakeFailure() || trainModel.getEngineFailure() || trainModel.getSignalPickupFailure()) {
            useEmergencyBrake(true);
        }
        setAuthority(trainModel.getAuthority());
        if (!beaconSet){
            setBeacon(trainModel.getBeacon());
        }
        setActualSpeed(trainModel.getActualSpeed());
        if (controlMode.equals("Automatic")){
            setCommandedSpeed(trainModel.getCommandedSpeed());
        }else{
            setCommandedSpeed(manualVelocity);
        }
        this.setSpeedLimit(60/3.6);
        this.setPower();
    }

    public void setTrainData(){
        trainModel.setPower(power);
        setNonVitalComponents();
    }

    public void setNextStation(String next){
        this.nextStop = next;
        trainModel.setNextStop(next);
    }
}
