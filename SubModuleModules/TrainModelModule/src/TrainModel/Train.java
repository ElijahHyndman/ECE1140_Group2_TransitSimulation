/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainModel;

/**
 *
 * @author Devon
 */
public class Train {

    
    public Train(int numCars, int numCrew) {
        this.numberOfCars = numCars;
        this.crewCount = numCrew;
        this.emergencyBrake = false;
        this.passengerBrake = false;
        this.serviceBrake = false;
        
        this.calculateMass();
    }
    //Known info. will be set
    double mass; //total kg with passengers
    double trainMass = 37194; //kg of empty train
    int crewCount;
    int numberOfCars;
    double standardDecelLimit = 1.2 ; //  m/s^2
    double emergencyDecelLimit = 2.73; //  m/s^2
    //I/O
    boolean signalPickupFail;
    boolean engineFail;
    boolean brakeFail;
    boolean leftDoors; //close=0, open=1
    boolean rightDoors;
    int cabinTemp;// F
    double power;// watts
    String nextStop;
    double accel;//  m/s^2
    int advertisements;
    String announcements;
    Boolean passengerBrake;
    Boolean emergencyBrake;
    Boolean serviceBrake;
    double commandedSpeed; //  m/s
    int authority;
    int beacon;
    int passengerCount; //aka ticket sales
    boolean cabinLights;
    boolean outerLights;
    boolean headlights;
    double actualSpeed;// m/s
    
    //display variable with customary units
    double displayActualSpeed;  //  ft/s
    double displayCommandedSpeed; //  ft/s
    double displayAcceleration; //  ft/s^2


    //getters

    public int  getNumberOfCars() {
        return numberOfCars;
    }
    public double getCommandedSpeed() {
        return commandedSpeed;
    }
    public int getAuthority() {
        return authority;
    }
    public int getBeacon() {
        return beacon;
    }
    public double getActualSpeed() {
        return actualSpeed;
    }

    //setters


    public void setSpeed(double speed) {
        if(speed >= 0){
            this.actualSpeed = speed;  
            this.displayActualSpeed = this.actualSpeed * 3.28084;
        } else {
            this.actualSpeed = 0;
            this.displayActualSpeed = 0;
        }
    }
    public void setDisplaySpeed(double speed) {
        this.displayActualSpeed = speed; 
        this.actualSpeed = this.displayActualSpeed / 3.28084;
    }
    public void setPower(double pow) {
        if(this.engineFail != true){
            this.power = pow;
        }else{
            this.power = 0;
        }
        calculateSpeed();
    }
    public void calculateSpeed(){
        int sampleTime = 1; //need to determine if this is constant
        double F;
        double newV;
        double newA;
        
        //check for zero velocity
        if(this.actualSpeed == 0 && this.power > 0){
            this.actualSpeed = 1;
        }
            
        if((this.emergencyBrake == true)||this.passengerBrake == true){
            newV = this.actualSpeed - (this.emergencyDecelLimit*sampleTime); 
            newA = -1 * this.emergencyDecelLimit;
            if(this.actualSpeed > 0){
                setAccel(newA);
            } else {
                setAccel(0);
            }
            setSpeed(newV);
        }
        else if(this.serviceBrake == true){
            newV = this.actualSpeed - (this.standardDecelLimit*sampleTime);
            newA = -1 * this.standardDecelLimit;
            if(this.actualSpeed > 0){
                setAccel(newA);
            } else {
                setAccel(0);
            }
            setSpeed(newV);
        }
        else{
            F = (this.power * 1000) / this.actualSpeed; //f is in Newtons = kg*m/s^2
            newA = F/calculateMass(); //A is in m/s^2
            newV = this.actualSpeed + (newA+this.accel)/(2*sampleTime); // m/s + average of 2 accels / time
            setSpeed(newV);
            setAccel(newA);
        }
    }
    public double calculateMass(){
        this.mass = this.trainMass + 75*(passengerCount+crewCount);
        return this.mass;
    }
    public void setAccel(double acceleration) {
        this.accel = acceleration;
        this.displayAcceleration = this.accel * 3.28084;
    }
    public void setDisplayAccel(double acceleration) {
        this.displayAcceleration = acceleration;
        this.accel = this.displayAcceleration / 3.28084;
    }
    public void setCommandedSpeed(double commandedSpeed) {
        this.commandedSpeed = commandedSpeed;
        this.displayCommandedSpeed = this.commandedSpeed * 3.28084;
    }
    public void setDisplayCommandedSpeed(double commandedSpeed) {
        this.displayCommandedSpeed = commandedSpeed;
        this.commandedSpeed = this.displayCommandedSpeed / 3.28084;
    }
    public void setAuthority(int a) {
        this.authority = a;
    }
    public void setBeacon(int beaconVal) {
        this.beacon = beaconVal;
    }
    public void setPassengerBrake(Boolean brake) {
        if(this.brakeFail != true){
            this.passengerBrake = brake;
            setAccel(-1 * this.emergencyDecelLimit);
        }            
    }
    public void setEmergencyBrake(Boolean brake) {
        if(this.brakeFail != true){
            this.emergencyBrake = brake;
            setAccel(-1 * this.emergencyDecelLimit);
        }else{
            this.emergencyBrake = false;
        }
    }
    public void setServiceBrake(Boolean brake) {
        if(this.brakeFail != true){
            this.serviceBrake = brake;
            setAccel(-1 * this.standardDecelLimit);
        }else{
            this.serviceBrake = false;
        }
    }
    public void setSignalFail(Boolean fail) {
        this.signalPickupFail = fail;
    }
    public void setBrakeFail(Boolean fail) {
        this.brakeFail = fail;
    }
    public void setEngineFail(Boolean fail) {
        this.engineFail = fail;
    }
    public void setCabinTemp(int temp) {
        this.cabinTemp = temp;
    }
    public void setOuterLights(boolean status) {
        this.outerLights = status;
    }public void setCabinLights(boolean status) {
        this.cabinLights = status;
    }public void setHeadlights(boolean status) {
        this.headlights = status;
    }
    public void setNextStop(String name) {
        this.nextStop = name;
    }
    public void setAnnouncements(String name) {
        this.announcements = name;
    }
    public void setAdvertisements(int name) {
        this.advertisements = name;
    }
    public void setLeftDoors(boolean status){
        this.leftDoors = status;
    }
    public void setRightDoors(boolean status){
        this.rightDoors = status;
    }
    public void setPassengerCount(int count){
        this.passengerCount = count;
        recalc();
    }
    public void setMass(double m){
        this.mass = m;
    }

    public void recalc(){
        calculateMass();
        calculateSpeed();
    }
    
    public void convert(){
        this.displayActualSpeed = this.actualSpeed * 3.28084;
        this.displayCommandedSpeed = this.commandedSpeed * 3.28084;
        this.displayAcceleration = this.accel * 3.2808399;
        
    }
}
