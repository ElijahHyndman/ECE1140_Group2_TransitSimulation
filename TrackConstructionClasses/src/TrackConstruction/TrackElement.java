package TrackConstruction;
/**Author Grace Henderson **/
public class TrackElement {
    /**
     * Track Element: This defines an interface which every object that exists on a track will implement.
     *  All track elements will have these elements and behaviors in common.
     *
     *  Track elements for our simulation include:
     *      - Track Block
     *      - Switch
     *      - Station
     *      - Railroad Crossing
     *
     * A full combination of these track elements will compose all of a Track System.
     **/
    //Basic Block Identifiers (Every single Track Element Has These)
    int blockNum;                 //block number
    String line;                 //Red or Green
    char section;                //A,b,c etc.
    double grade;                //in whole number %
    double length;               //in meters
    int speedLimit;              //km/hr
    double elevation;            //meters
    double cumulativeElevation;  //meters
    int currentDirection;

    //for printing purposes
    String infrastructure;
    String beacon = null;

    //keeping track of failures
    String[] possibleStates = {"NONE","BROKEN RAIL", "POWER FAILURE", "CIRCUIT FAILURE", "CLOSED"};
    String failureStatus;


    //Information sent to individual blocks
    int  authority;          //authority if FINAL block destination?
    double commandedSpeed;     //km / hour

    //Information on node connections
    int[] directionArray;      //TO ARRAY, all possible edges - meaning from the current block to the next block
    char biDirecitional;
    int[] directionStates = {0,0,0,0};
    String type;




    public TrackElement(){
        this.blockNum = this.speedLimit  = -1;
        this.cumulativeElevation = this.elevation = this.grade = this.length = -1.0;
        this.section = '-';
        this.line = "-";
        this.authority = 0;
        this.commandedSpeed = 0.0;
        this.directionArray = null;
        this.biDirecitional = 'u';
        this.currentDirection = -3;
        type = "";
    }


    /* Setting Block Number */
    public void setBlockNum(int blockNum){this.blockNum = blockNum;}

    /*Setting Line */
    public void setLine(String line){this.line = line;}

    /*set Beacon*/
    public void setBeacon(String beacon){
        this.beacon = beacon;
    }

    /*Setting section */
    public void setSection(char section){this.section = section;}

    /*Setting grade*/
    public void setGrade(double grade){ this.grade = grade;}

    /*setting length*/
    public void setLength(double length){ this.length = length;}

    /*get Index */
    public boolean getIndex(){ return false; }

    /*Setting SpeedLimit*/
    public void setSpeedLimit(int speedLimit){this.speedLimit = speedLimit;}

    /*Setting elevation*/
    public void setElevation(double elevation){ this.elevation = elevation;}

    /*Setting cumulative elevation*/
    public void setCumulativeElevation(double cumulativeElevation){this.cumulativeElevation = cumulativeElevation;}

    /*Setting Authority*/
    public void setAuthority(int authority) { this.authority = authority;}

    /*Setting Commanded Speed*/
    public void setCommandedSpeed(double commandedSpeed) { this.commandedSpeed = commandedSpeed;}

    /*Setting direction array*/
    public void setDirection(int [] directionArray) {this.directionArray = directionArray;}

    /*setting bidirectionality*/
    public void setBiDirectional(String biDirection){this.biDirecitional = biDirection.charAt(0);}


    /*set Infrastructure*/
    public void setInfrastructure(String infrastructure){this.infrastructure = infrastructure;}

    /*set Current Direciton*/
    public void setCurrentDirection(int index){
        currentDirection = index;
    }

    /*set failure status */
    public void setFailureStatus(int index) {
        if(index >=0 && index <= 4)
            this.failureStatus = possibleStates[index];
    }

    /*get block Number */
    public int getBlockNum(){return this.blockNum;}

    /*get line */
    public String getLine(){ return this.line;}

    /*get section*/
    public char getSection(){ return this.section;}

    /*get Grade*/
    public double getGrade(){ return this.grade;}

    /*get Beacon*/
    public String getBeacon() { return this.beacon;}

    /*get length */
    public double getLength(){ return this.length;}

    /*get speed Limit*/
    public int getSpeedLimit(){ return this.speedLimit; }

    /*Get elevation*/
    public double getElevation(){ return this.elevation;}

    /*get cumulative elvation*/
    public double getCumulativeElevation(){ return this.cumulativeElevation;}

    /*direction*/
    public int getCurrentDirection() {return this.currentDirection;}


    /*get Authority */
    public int getAuthority() { return this.authority;}

    /*get CommandedSpeed*/
    public double getCommandedSpeed() { return this.commandedSpeed;}

    /*display commanded speed*/
    public double dispCommandedSpeed() {
        return commandedSpeed*2.23694;}

    /*get Direciton Array */
    public int getDirection(int index) {
        int ret = -2;
        if(index >= 0 && index <= 2)
            ret = directionArray[index];
        return ret;
    }

    public String getType(){
        return type;
    }

    /*get string directions */
    public String getDirectionString(){
        String ret = directionArray[0] + " , ";
        if(directionArray[1] != 0)
            ret += directionArray[1];

        if(directionArray[2] != 0)
            ret += " , " + directionArray[2];

        return ret;}

    /*get if track section is bidirectional*/
    public boolean getBiDirectional() {
        if(biDirecitional=='y')
            return true;
        return false;
    }


    //Methods to be overwritten by subclasses
    public boolean getOccupied(){ return false; }

    public void setOccupied(boolean occupied){}

    public void setThroughput(int throughput){

    }

    /*get infrastructure*/
    public String getInfrastructure(){return this.infrastructure;}

    /*get failure status*/
    public String getFailureStatus(){ return this.failureStatus;}

    /*get switch state*/
    public void setSwitchState(boolean switches) {}

    public int getDirectionStates(int index) { return 0;}
}
