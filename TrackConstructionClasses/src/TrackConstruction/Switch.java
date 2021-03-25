package TrackConstruction;

public class Switch extends TrackElement{
    /**
     * Switch: The switch is a junction between three track objects.
     *  Switches can be visualized as a Y. There are always three track objects connected to a switch.
     *  There will always be one in-going track object, and two out-going track objects.
     *  The in-going track object may only be routed (or Oriented) to one of the two outgoing tracks.
     *  Of the two out-going objects, one will be named the "Through Route" and the other will be named the "Turnout Route"
     *
     */
    boolean occupied;
    String[] switches;
    String switchState;
    int[] directionStates = {0,0,0,0};

    /*normal*/
    public Switch(){
        this.occupied = false;
    }

    /*Active Constructor*/
    public Switch(String line, char sect, int blockNum, double length, double grade, int speedlimit, String infrastructure, double elevation, double cumelev, int[] setDirection, String bidirectional){
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
        this.currentDirection = -1;

        //need to add swithces here

        if(infrastructure.length() > 8) {
            String test = infrastructure.substring(7);
            String[] dirStates;
            switches = test.split(";");
            switches[0] = switches[0].replace("(", "");
            switches[0] = switches[0].replace(")","");

           //can only do if NOT Yard
            if(switches[0].length() <= 5) {
                dirStates = switches[0].split("-");
                directionStates[0] = Integer.parseInt(dirStates[0]);
                directionStates[1] = Integer.parseInt(dirStates[1]);
            }
            switches[0] += " DEFAULT ";

            if(switches.length>1){
                switches[1] = switches[1].replace(")","");
                switches[1] = switches[1].replaceAll("\\s","");
                if(switches[1].length() <= 6) {
                    dirStates = switches[1].split("-");
                    directionStates[2] = Integer.parseInt(dirStates[0].replaceAll("\\s",""));
                    directionStates[3] = Integer.parseInt(dirStates[1]);
                }
                switches[1] += " SECONDARY";
            }
        }

        //Setting switch state to normal
        this.switchState = switches[0];



    }

    @Override
    public int getDirectionStates(int index){
        int ret = -1;
        if(index >=0 && index <= 3)
            ret =  directionStates[index];
        return ret;
    }

    /*set Switch State*/
    @Override
    public void setSwitchState(int index){
        if(index >=0 && index < switches.length)
            this.switchState = switches[index];
    }

    /*set Occupied*/
    @Override
    public void setOccupied(boolean occupied) {this.occupied = occupied;}

    /*get Siwtch State()*/
    public String getSwitchState() {return this.switchState;}

    /*get Occupied*/
    @Override
    public boolean getOccupied() {return this.occupied;}

    /*get type*/
    String getType(){
        return "Switch";
    }

    @Override
    public String toString() {
        return String.format("Block Num: "+ blockNum +"\n Line: "+ line + "\n Length: "+ length + "\n Grade: " + grade + "\n speedLimit " + speedLimit + "\n elevation " + elevation + "\n cumulativeElevation " + cumulativeElevation + "\n section: " + section + "\n infrastructure: " + infrastructure + "\n Occupied: " + occupied + "\n Status: "+ failureStatus + " \n Block Direction: " + directionArray[0] + " " + directionArray[1] + " " + directionArray[2]);
    }
}
