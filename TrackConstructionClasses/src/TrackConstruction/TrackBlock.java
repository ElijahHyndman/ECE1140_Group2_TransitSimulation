package TrackConstruction;

public class TrackBlock extends TrackElement {
    /*normal*/
    TrackBlock(){
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
    }


    boolean occupied;

    /*set Occupied*/
    @Override
    public void setOccupied(boolean occupied) {this.occupied = occupied;}

    /*get Occupied*/
    @Override
    public boolean getOccupied() {return this.occupied;}

    String getType(){
        return "Block";
    }

    @Override
    public String toString() {
        return String.format("Block Num: "+ blockNum +"\n Line: "+ line + "\n Length: "+ length + "\n Grade: " + grade + "\n speedLimit " + speedLimit + "\n elevation " + elevation + "\n cumulativeElevation " + cumulativeElevation + "\n section: " + section + "\n infrastructure: " + infrastructure + "\n Occupied: " + occupied + "\n Status: "+ failureStatus + " \n Block Direction: " + directionArray[0] + " " + directionArray[1] + " " + directionArray[2]);
    }
}
