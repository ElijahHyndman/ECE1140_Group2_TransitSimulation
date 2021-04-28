package TrackConstruction;
import java.util.Random;
/**AUTHOR:  Grace Henderson**/

public class Station extends TrackElement{
    String station;
    String beacon;
    int throughput;  //passengers Leaving Train
    int ticketSales; //Ticket sales per station
    String name;
    int people =0;
    boolean occupied;

    public Station(){
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
        this.currentDirection = -3;
        this.type = "Station";
    }

    /*Set Ticket Sales */
    public void setTicketSales(){
        Random rand = new Random();
        this.ticketSales += Math.abs(rand.nextInt()) % 11;
        people = this.ticketSales;
    }

    public void setName(String nameTemp){
        name = nameTemp;
    }

    public String getName() { return this.name;}

    /*set throughput*/
    @Override
    public void setThroughput(int throughput){
        this.throughput = throughput;
    }

    /*set Beacon*/
   // public void setBeacon(String station){this.beacon = station;}

    /*set Station*/
    void setStation(String station){
        this.station = station;
    }

    /*creating new occupied scenario*/
    @Override
    public void setOccupied(boolean occupiedVar){
        this.occupied = occupiedVar;

        /* changing updating ticketsales
        if(occupiedVar){
            this.setTicketSales();
        }*/
        this.occupied = occupiedVar;
    }

    /*get ticket sales*/
    public int getTicketSales(){ return this.ticketSales;}

    /*generate from ticket sales number 0 - ticket sales */


    /*get throughput*/
    public int getThroughput() { return this.throughput;}

    /*get Occupied*/
    @Override
    public boolean getOccupied() {return this.occupied;}

    /*get Station*/
    String getStation() { return this.station;}

    /*get Beacon*/
   // public String getBeacon() { return this.beacon;}

    //TODO exitTrain -- void and give an integer number of people
    public void exitTrain(int N){
        this.throughput = N;
    }

    //TODO boardTrain -- return between 0 and p people onTrack = 0, people board train random to people on track, people on track
    public int boardTrain(){
        Random rand = new Random();
        int leaving;
        leaving = Math.abs(rand.nextInt()) % people;
        people -= leaving;
        return leaving;
    }

    @Override
    public String toString() {
        return String.format("Block #%d",this.blockNum);
    }
    public String totoString() {
        return String.format("Block Num: "+ blockNum +"\n Line: "+ line + "\n Length: "+ length + "\n Grade: " + grade + "\n speedLimit " + speedLimit + "\n elevation " + elevation + "\n cumulativeElevation " + cumulativeElevation + "\n section: " + section + "\n infrastructure: " + infrastructure + "\n Occupied: " + occupied + "\n Status: "+ failureStatus + " \n Block Direction: " + directionArray[0] + " " + directionArray[1] + " " + directionArray[2]);
    }
}
