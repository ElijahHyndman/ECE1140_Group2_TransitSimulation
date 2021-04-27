/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainModel;
import java.util.Random;
/**
 *
 * @author Devon
 */
public class Train {

    
    public Train(int numCars, int numCrew, int id) {
        this.numberOfCars = numCars;
        this.crewCount = numCrew;
        this.emergencyBrake = false;
        this.passengerBrake = false;
        this.serviceBrake = false;
        this.id = id;
        this.calculateMass();
        this.sampleTime = 1; //default
    }
    //Known info. will be set
    double mass; //total kg with passengers
    double trainMass = 37194/5; //kg of empty train
    int crewCount;
    int numberOfCars;
    double standardDecelLimit = 1.2 ; //  m/s^2
    double emergencyDecelLimit = 2.73; //  m/s^2
    int id;
    int passengerCount; //aka ticket sales

    //Movement
    Boolean passengerBrake;
    Boolean emergencyBrake;
    Boolean serviceBrake;
    double commandedSpeed; //  m/s
    int authority;
    String beacon;
    double power;// watts
    double accel;//  m/s^2
    double maxServiceDecel = 1.2;
    double maxEmergencyDecel = 2.73;
    double maxAccel = 0.5;
    //TrackBlock myTrackBlock;
    double totalDistance; //meters
    double blockDistance;
    double blockGrade; //%
    double speedLimit = 19.4444; //m/s  (1 km/hr = .2778 m/s)

    //Fails
    boolean signalPickupFail;
    boolean engineFail;
    boolean brakeFail;
    boolean leftDoors; //close=0, open=1
    boolean rightDoors;
    int cabinTemp;// F
    String nextStop;
    int advertisements; //declares ad# to play
    String announcements;
    boolean cabinLights;
    boolean outerLights;
    boolean headlights;
    double actualSpeed;// m/s
    double sampleTime; //s
    
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
    public String getBeacon() {
        return beacon;
    }
    public double getActualSpeed() {
        return actualSpeed;
    }
    public double getMass(){return mass;}
    public double getAccel(){return accel;}

    //setters


    public void setSpeed(double speed) {
        if (speed > this.speedLimit){
            this.actualSpeed = this.speedLimit;
            this.displayActualSpeed = this.actualSpeed * 2.236936;
        }
        else if(speed >= 0){
            this.actualSpeed = speed;  
            this.displayActualSpeed = this.actualSpeed * 2.236936;
        } else {
            this.actualSpeed = 0;
            this.displayActualSpeed = 0;
        }
    }
    public void setDisplaySpeed(double speed) {
        this.displayActualSpeed = speed; 
        this.actualSpeed = this.displayActualSpeed / 2.236936;
    }
    public void setAccel(double acceleration) {
        this.maxAccel = 23330 / this.mass;
        if(acceleration > maxAccel){
            this.accel = this.maxAccel;
            this.displayAcceleration = this.maxAccel * 2.236936;
        }else {
            this.accel = acceleration;
            this.displayAcceleration = this.accel * 2.236936;
        }
    }
    public void setDisplayAccel(double acceleration) {
        this.displayAcceleration = acceleration;
        this.accel = this.displayAcceleration / 2.236936;
    }
    public void setCommandedSpeed(double commandedSpeed) {
        this.commandedSpeed = commandedSpeed;
        this.displayCommandedSpeed = this.commandedSpeed * 2.236936;
    }
    public void setDisplayCommandedSpeed(double commandedSpeed) {
        this.displayCommandedSpeed = commandedSpeed;
        this.commandedSpeed = this.displayCommandedSpeed / 2.236936;
    }
    public void setPower(double pow) {
        if(this.engineFail != true){
            this.power = pow;
        }else{
            this.power = 0;
        }
    }


    //ADDED FOR TESTING
    public double getPower(){
        return power;
    }

    public void calculateSpeed(double deltaTime){
        //int sampleTime = 1; //need to determine if this is constant
        double F;
        double newV;
        double newA;
        
        //check for zero velocity & power command

        totalDistance += this.actualSpeed*deltaTime;
        blockDistance += this.actualSpeed*deltaTime;
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
            F = F - (this.blockGrade/100) * this.mass * 9.81;
            newA = F/calculateMass(); //A is in m/s^2
            newV = this.actualSpeed + (newA+this.accel)*deltaTime*.5; // m/s + average of 2 accels * time
            setSpeed(newV);
            setAccel(newA);
        }
    }
    public double getTotalDistance(){
        return totalDistance;
    }
    public double getBlockDistance(){
        return blockDistance;
    }
    public void setBlockDistance(double distance){
        blockDistance = distance;
    }
    public double calculateMass() {
        this.mass = this.trainMass*this.numberOfCars + 75 * (passengerCount + crewCount);
        return this.mass;
    }

    public void setBlockGrade(double blockGrade) { //takes % value ( input 1 for 1%, 10 for 10% )
        this.blockGrade = blockGrade;
    }

    public void setSpeedLimit(double kmPerHour) { //takes in km/h
        this.speedLimit = kmPerHour / 3.6;        // converts & stores as m/s
    }

    public void setAuthority(int a) {
        this.authority = a;
    }
    public void setBeacon(String beaconVal) {
        this.beacon = beaconVal;
    }
    public void setPassengerBrake(Boolean brake) {
            this.passengerBrake = brake;
            setAccel(-1 * this.emergencyDecelLimit);
    }
    public void setEmergencyBrake(Boolean brake) {
            this.emergencyBrake = brake;
            setAccel(-1 * this.emergencyDecelLimit);

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
        if(this.leftDoors == true){
            disembark();
        }
    }
    public void setRightDoors(boolean status){
        this.rightDoors = status;
        if(this.rightDoors == true){
            disembark();
        }
    }

    public void setPassengerCount(int count){
        if(count<=0){
            this.passengerCount = 0;
        }
        this.passengerCount = count;
        calculateMass();
    }
    public void setPassengersBoarding(int count){
        setPassengerCount(this.passengerCount + count);
    }
    public void setMass(double m){
        this.mass = m;
    }

    public void updatePhysicalState(String currentTime, double deltaTime){
        calculateSpeed(deltaTime);
        this.sampleTime = deltaTime;
    }
    
    public void convert(){
        this.displayActualSpeed = this.actualSpeed * 3.28084;
        this.displayCommandedSpeed = this.commandedSpeed * 3.28084;
        this.displayAcceleration = this.accel * 3.2808399;
        
    }
    public int disembark(){
        Random rand = new Random(); //instance of random class
        int upperbound = (int) (.75 * this.passengerCount);
        int random = rand.nextInt(upperbound);
        setPassengerCount(this.passengerCount - random);
        return random;
    }

    public boolean getEmergencyBrake() {
        return emergencyBrake;
    }

    public boolean getPassengerBrake() {
        return passengerBrake;
    }

    boolean getSignalPickupFailure(){
        return this.signalPickupFail;
    }
    boolean getEngineFailure(){
        return this.engineFail;
    }
    boolean getBrakeFailure(){
        return this.brakeFail;
    }

    public int getPassengerCount() {
        return this.passengerCount;
    }
}
