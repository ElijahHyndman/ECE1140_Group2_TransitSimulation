package TrackConstruction;
import java.util.Random;

public class Station extends TrackElement{
    String station;
    String beacon;
    int throughput;  //passengers Leaving Train
    int ticketSales; //Ticket sales per station
    boolean occupied;

    Station(){
        this.occupied = false;
    }

    /*Active Constructor*/
    public Station(String line, char sect, int blockNum, double length, double grade, int speedlimit, String infrastructure, double elevation, double cumelev, int[] setDirection, String bidirectional){
        this.line = line;
        this.section = sect;
        this.blockNum = blockNum;
        this.speedLimit  = speedlimit;
        this.infrastructure = infrastructure;
        this.cumulativeElevation = cumelev;
        this.elevation = elevation;
        this.grade = grade;
        this.length = length;
        this.authority = 0;
        this.commandedSpeed = 0;
        this.directionArray = setDirection;
        this.biDirecitional = bidirectional.charAt(0);
        this.currentDireciton =setDirection[0];
    }

    /*Set Ticket Sales */
    public void setTicketSales(){
        Random rand = new Random();
        this.ticketSales = Math.abs(rand.nextInt()) % 80;
    }

    /*set throughput*/
    @Override
    public void setThroughput(int throughput){
        this.throughput = throughput;
    }

    /*set Beacon*/
    public void setBeacon(String station){
        this.beacon = station;
    }

    /*set Station*/
    void setStation(String station){
        this.station = station;
    }

    /*creating new occupied scenario*/
    @Override
    public void setOccupied(boolean occupiedVar){
        this.occupied = occupiedVar;

        if(occupiedVar){
            this.setTicketSales();
        }
        this.occupied = occupiedVar;
    }

    /*get ticket sales*/
    public int getTicketSales(){ return this.ticketSales;}

    /*get throughput*/
    public int getThroughput() { return this.throughput;}

    /*get Occupied*/
    @Override
    public boolean getOccupied() {return this.occupied;}

    /*get Station*/
    String getStation() { return this.station;}

    /*get Beacon*/
    public String getBeacon() { return this.beacon;}

    /*get type of block*/
    String getType(){
        return "Station";
    }

    @Override
    public String toString() {
        return String.format("Block Num: "+ blockNum +"\n Line: "+ line + "\n Length: "+ length + "\n Grade: " + grade + "\n speedLimit " + speedLimit + "\n elevation " + elevation + "\n cumulativeElevation " + cumulativeElevation + "\n section: " + section + "\n infrastructure: " + infrastructure + "\n Occupied: " + occupied + "\n Status: "+ failureStatus + " \n Block Direction: " + directionArray[0] + " " + directionArray[1] + " " + directionArray[2]);
    }
}
