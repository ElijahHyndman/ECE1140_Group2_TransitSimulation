package TrackConstruction;

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
    int currentDireciton;

    //for printing purposes
    String infrastructure;

    //keeping track of failures
    String[] possibleStates = {"NONE","BROKEN RAIL", "POWER FAILURE", "CIRCUIT FAILURE"};
    String failureStatus;


    //Information sent to individual blocks
    double authority;          //authority if FINAL block destination?
    double commandedSpeed;     //km / hour

    //Information on node connections
    int[] directionArray;      //TOARRAY, all possible edges - meaning from the current block to the next block
    char biDirecitional;



    TrackElement(){
        this.blockNum = this.speedLimit  = -1;
        this.cumulativeElevation = this.elevation = this.grade = this.length = -1.0;
        this.section = '-';
        this.line = "-";
        this.authority = 0;
        this.commandedSpeed = 0;
        this.directionArray = null;
        this.biDirecitional = 'u';
        this.currentDireciton = -1;
    }


    /* Setting Block Number */
    void setBlockNum(int blockNum){this.blockNum = blockNum;}

    /*Setting Line */
    void setLine(String line){this.line = line;}

    /*Setting section */
    void setSection(char section){this.section = section;}

    /*Setting grade*/
    void setGrade(double grade){ this.grade = grade;}

    /*setting length*/
    void setLength(double length){ this.length = length;}

    /*Setting SpeedLimit*/
    void setSpeedLimit(int speedLimit){this.speedLimit = speedLimit;}

    /*Setting elevation*/
    void setElevation(double elevation){ this.elevation = elevation;}

    /*Setting cumulative elevation*/
    void setCumulativeElevation(double cumulativeElevation){this.cumulativeElevation = cumulativeElevation;}


    /*Setting Authority*/
    public void setAuthority(double authority) { this.authority = authority;}

    /*Setting Commanded Speed*/
    public void setCommandedSpeed(double commandedSpeed) { this.commandedSpeed = commandedSpeed;}

    /*Setting direction array*/
    void setDirection(int [] directionArray) {this.directionArray = directionArray;}

    /*setting bidirectionality*/
    void setBiDirectional(String biDirection){this.biDirecitional = biDirection.charAt(0);}


    /*set Infrastructure*/
    void setInfrastructure(String infrastructure){this.infrastructure = infrastructure;}

    /*set failure status */
    public void setFailureStatus(int index) {
        if(index >=0 && index <= 3)
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

    /*get length */
    public double getLength(){ return this.length;}

    /*get speed Limit*/
    public int getSpeedLimit(){ return this.speedLimit; }

    /*Get elevation*/
    public double getElevation(){ return this.elevation;}

    /*get cumulative elvation*/
    public double getCumulativeElevation(){ return this.cumulativeElevation;}

    /*direction*/
    public int getCurrentDirection() {return this.currentDireciton;}


    /*get Authority */
    public double getAuthority() { return this.authority;}

    /*get CommandedSpeed*/
    public double getCommandedSpeed() { return this.commandedSpeed;}

    /*get Direciton Array */
    int[] getDirectionArray() { return this.directionArray;}

    /*get string directions */
    public String getDirectionString(){
        String ret = directionArray[0] + " , ";
        if(directionArray[1] != 0)
            ret += directionArray[1];

        if(directionArray[2] != 0)
            ret += " , " + directionArray[2];

        return ret;}

    /*get if track section is bidirectional*/
    boolean getBiDirectional() {
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

    /*get swithc state*/
    public void setSwitchState(int switches) {}
}