package TrackConstruction;

import java.io.Serializable;

/**AUTHOR Grace Henderson**/
public class TrackBlock extends TrackElement implements Serializable {
    /*normal*/
    String beacon;
    public TrackBlock(){
        this.occupied = false;
    }

    /*Active Constructor*/
    public TrackBlock(String line, char sect, int blockNum, double length, double grade, int speedlimit, String infrastructure, double elevation, double cumelev, int[] setDirection, String bidirectional){
        this.line = line;
        this.section = sect;
        this.blockNum = blockNum;
        this.speedLimit  = speedlimit;
        this.cumulativeElevation = cumelev;
        this.infrastructure = infrastructure;
        this.elevation = elevation;
        this.grade = grade;
        this.length = length;
        this.authority = 0;
        this.commandedSpeed = 0;
        this.directionArray = setDirection;
        this.biDirecitional = bidirectional.charAt(0);
        this.currentDirection = -3; // THIS IS IMPORTANT
        this.type = "Block";
    }


    boolean occupied;

    /*set Occupied*/
    @Override
    public void setOccupied(boolean occupied) {this.occupied = occupied;}

    /*get Occupied*/
    @Override
    public boolean getOccupied() {return this.occupied;}


    @Override
    public String toString() {
        return String.format("Block #%d",this.blockNum);
    }
    // TODO
    public String totoString() {
        return String.format("Block Num: "+ blockNum +"\n Line: "+ line + "\n Length: "+ length + "\n Grade: " + grade + "\n speedLimit " + speedLimit + "\n elevation " + elevation + "\n cumulativeElevation " + cumulativeElevation + "\n section: " + section + "\n infrastructure: " + infrastructure + "\n Occupied: " + occupied + "\n Status: "+ failureStatus + " \n Block Direction: " + directionArray[0] + " " + directionArray[1] + " " + directionArray[2]);
    }
}
