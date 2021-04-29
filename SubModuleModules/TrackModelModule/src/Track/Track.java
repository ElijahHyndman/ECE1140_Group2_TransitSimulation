package Track;


import TrackConstruction.*;
import WorldClock.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
/**Author Grace Henderson**/
    public class Track implements PhysicsUpdateListener {

        //Members in Class
        ArrayList<TrackElement> blockArrayList;
        ArrayList<Station> stationsArrayList;
        ArrayList<Switch> switchesArrayList;
        ArrayList<TrackElement> failureArrayList;
        ArrayList<TrackElement> beacons = new ArrayList<>();


        //Track Variables
        boolean trackHeater;
        int SIZE_LINE_A=0;
        int SIZE_LINE_B=0;
        double environmentalTemperature;
        double totalTime;
        String DISPATCH_LINE = "Red";

        //Creating Graphs for RED LINE and GREEN LINE
        ArrayList<TrackElement> redTrack;
        ArrayList<TrackElement> greenTrack;

        //Constructor Methhod
        public Track() {
            blockArrayList = new ArrayList<>();
            stationsArrayList = new ArrayList<>();
            switchesArrayList = new ArrayList<>();
            failureArrayList = new ArrayList<>();
            redTrack = new ArrayList<>();
            greenTrack = new ArrayList<>();
            environmentalTemperature = 60;
            trackHeater= false;
        }

        //Import Track from a File
        public boolean validFile( String filePath){
            //Testing if true or not
            boolean success = false;
            try {
                //Testing input the File -- Need to get input from MAIN file
                Scanner sc = new Scanner(new File(filePath));
                success = true;
            } catch(Exception e){}
            return success;
        }

        /*function that will import track through a CSV file*/
        public boolean importTrack(String filepath){
            boolean success = false;
            try {
                Scanner sc = new Scanner(new File(filepath));
                sc.useDelimiter(",|\\n");
                //creating wb object for xls file
                int count = 0;
                while(sc.hasNext()){

                    if(count < 15 ) {
                        sc.next();
                        count++;
                        continue;
                    }

                    //Parsing File into components

                    String line = sc.next();
                    char section = sc.next().charAt(0);
                    int blockNum = Integer.parseInt(sc.next());
                    double length = Double.parseDouble(sc.next());
                    double grade= Double.parseDouble(sc.next());
                    int speedLimit = Integer.parseInt(sc.next());
                    String infrastructure = sc.next();
                    sc.next(); //a BLANK
                    double elevation = Double.parseDouble(sc.next());
                    double cumulativeElevation = Double.parseDouble(sc.next());

                    //Adding the ToPointers and Bidirecitonality
                    int[] setDirection = new int[] {Integer.parseInt(sc.next()),Integer.parseInt(sc.next()),Integer.parseInt(sc.next())};
                    String setBiDirectional = sc.next();
                    String beacon = sc.next();


                    //Collecting Size information
                    if(line.equals("Red"))
                        SIZE_LINE_A ++;
                    else {
                        SIZE_LINE_B ++;
                    }

                    //For Yard -- for both red and green line yard block index 0
                    if(infrastructure.length()>3 && infrastructure.substring(0,3).equals("YARD")){
                        TrackBlock Test = new TrackBlock(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);
                        blockArrayList.add(Test);
                        redTrack.add(Test);
                        greenTrack.add(Test);
                    }


                    //Need to check to see if stations are here and if so need to set Beacon Value and calculate tickets
                    if(infrastructure.length()>7 && infrastructure.substring(0,7).equals("STATION")){
                        //creating new station block
                        Station Test = new Station(line, section, blockNum, length, grade, speedLimit, infrastructure, elevation, cumulativeElevation, setDirection, setBiDirectional);

                        stationsArrayList.add(Test); // add stations to station arraylist
                        blockArrayList.add(Test);

                        if (line.equals("Red")) //adding to red or green arraylist
                            redTrack.add(Test);
                        else
                            greenTrack.add(Test);
                    }
                    else if(infrastructure.length()>6 && infrastructure.substring(0,6).equals("SWITCH")){
                        //creating a new switch
                        Switch Test = new Switch(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);
                        switchesArrayList.add(Test); // adding to switches array list
                        blockArrayList.add(Test); // adding to block array list

                        if(line.equals("Red")) // adding to red or green arraylist
                            redTrack.add(Test);
                        else
                            greenTrack.add(Test);
                    }
                    else {
                        //if nothing else then a normal track block
                        TrackBlock Test = new TrackBlock(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);
                        blockArrayList.add(Test);
                        if(beacon.length()>2)
                          Test.setBeacon(beacon);
                        if(line.equals("Red")) {
                            if (redTrack.size() == 0)
                                redTrack.add(greenTrack.get(0)); // for whatever reason doesn't get assigned earlier
                            redTrack.add(Test);
                        }
                        else
                            greenTrack.add(Test);


                    }
                    count++;
                }
                //getting sheet object 0
                sc.close();
                success = true;
                blockArrayList.get(0).setAuthority(1);
            } catch(Exception e)
            {
                e.printStackTrace();
            }

            for(int i =0; i < switchesArrayList.size(); i++)
                  switchesArrayList.get(i).setSwitchState(false); //making sure all switches set to default

            updateSwitches(); // need to call to have directionality done
            setBeaconArray();
            return success;
        }

        //Return Track
        public ArrayList<TrackElement> getBlocks(){
            return blockArrayList;
        }

        public ArrayList<Station> getStations(){
            return stationsArrayList;
        }

        public ArrayList<Switch> getSwitches(){
            return switchesArrayList;
        }

        public ArrayList<TrackElement> getFailures(){
            return failureArrayList;
        }

        public ArrayList<TrackElement> getGreenLine(){return greenTrack;}

        public ArrayList<TrackElement> getRedLine() {return redTrack;}

        /*needed to add for direcitonality*/
        public void updateSwitches(){

            for(int i = 0; i <= 12; i++) {
                Switch switchT = switchesArrayList.get(i); // getting switch
                boolean index = switchT.getIndex(); // index -- false is default, true is secondary
                //all 4 parts of switch a-b and c-d -- getting for what is connected and direcitonality
                int blocka = switchT.getDirectionStates(0);
                int toA = switchT.getDirectionStates(1);
                int blockb = switchT.getDirectionStates(2);
                int toB = switchT.getDirectionStates(3);
                //here need to CHANGE getCurrent Direction this is for directionality of track
                if (index == false && i <7) { //for red line
                    redTrack.get(blockb).setCurrentDirection(-2);
                    redTrack.get(toB).setCurrentDirection(-2);
                    redTrack.get(blocka).setCurrentDirection(1);
                    redTrack.get(toA).setCurrentDirection(1);
                } else if (index == true && i<7) { // for red line
                    //Second Number Block Points to last number
                    redTrack.get(blocka).setCurrentDirection(-2);
                    redTrack.get(toA).setCurrentDirection(-2);
                    redTrack.get(blockb).setCurrentDirection(1);
                    redTrack.get(toB).setCurrentDirection(1);
                }
                if (index == false && i >6) { // for green line
                    greenTrack.get(blockb).setCurrentDirection(-2);
                    greenTrack.get(toB).setCurrentDirection(-2);
                    greenTrack.get(blocka).setCurrentDirection(1);
                    greenTrack.get(toA).setCurrentDirection(1);
                } else if (index == true && i>6) { // for green line
                    //Second Number Block Points to last number
                    greenTrack.get(blocka).setCurrentDirection(-2);
                    greenTrack.get(toA).setCurrentDirection(-2);
                    greenTrack.get(blockb).setCurrentDirection(1);
                    greenTrack.get(toB).setCurrentDirection(1);
                }
            }

        }

        /*getting next for red track*/
        public TrackElement getNextRed(TrackElement current, TrackElement previous) {
            int cur = current.getBlockNum();
            //checking if yard switch is on
            if (cur == 0) {
                if (switchesArrayList.get(0).getIndex() == true)
                    return redTrack.get(9);
                else
                    return null;
            }

            int prev = previous.getBlockNum();
            TrackElement ret = null;

            //Test cases of switches
            //switch to YARD -- special case
            if(cur == 9 && prev == 8 || cur ==9 && prev == 10) {
                if(switchesArrayList.get(0).getIndex() == true)
                    return redTrack.get(0);
            }



            //need to catch ones BEFORE switch
            if(current.getCurrentDirection() == -2 && !(current.getType().equals("Switch")) && cur != 16 && cur != 44 && cur != 33) {
                return null;
            }


            //chose direction FROM SWITCH
            if (current.getType().equals("Switch")) {
                int index = (current.getIndex()) ? 1 : 0;
                if((index == 0 && current.getDirectionStates(0) == prev)) // for 1 direction
                    return redTrack.get(current.getDirection(1)); // go in that direction
                else if((index == 0 && current.getDirectionStates(1) == (prev+1))) // for 0 direction
                    return redTrack.get(current.getDirection(0)); // go in 0 direction
                else if (index == 1 && current.getDirectionStates(2) == prev) {
                   if(cur == 15) // special edge case
                       return redTrack.get(16);
                    return redTrack.get(current.getDirection(index));
                }
                else if (index == 1 && current.getDirectionStates(3) == prev+1) // changing to the 3rd direction
                    return redTrack.get(current.getDirection(2)); // go direction 2
                //yard has special cases
                else if(cur == 9 && prev == 10)
                    return redTrack.get(current.getDirection(1));
                else if( cur == 9 && prev == 8 )
                    return redTrack.get(current.getDirection(0));
                else if( cur == 9 && prev == 0 )
                    return redTrack.get(current.getDirection(1));
                //special cases as well
                else if( cur == 43 && prev == 44 && index == 1)
                  return redTrack.get(current.getDirection(2));
                else if(cur == 32 && prev == 33 && index == 1)
                    return redTrack.get(current.getDirection(2));
                else
                    return null;
            }

            // If one way Track Simply go straight down
            if (!current.getBiDirectional()) // one way track {
                return redTrack.get(current.getDirection(0));

            //CORNER CASE to get directionality work in red line
            if((previous.getDirection(2) == cur) && cur == 76)
                return redTrack.get(75);
            if((previous.getDirection(2) == cur) && cur == 71)
                return redTrack.get(70);
            if((previous.getDirection(2) == cur) && cur == 66)
                return redTrack.get(65);
            //if 2 way track
            if(previous.getDirection(0) == cur|| (previous.getDirection(2) == cur))
                return redTrack.get(current.getDirection(0));
            else if (previous.getDirection(1) == cur)
                return redTrack.get(current.getDirection(1));

            return ret;
        }


        /*trying new getNext*/
        public TrackElement getNextGreen(TrackElement current, TrackElement previous) {
            int cur = current.getBlockNum();
            //checking if yard switch is on
            if (cur == 0) {
                if (switchesArrayList.get(10).getIndex() == true)
                    return greenTrack.get(62);
                else
                    return null;
            }
            int prev = previous.getBlockNum();

            if(prev == 0)
                return greenTrack.get(63);

            TrackElement ret = null; // returning value

            //special case for switch 7
            if(current.getCurrentDirection() == -2 && prev == 12)
                return greenTrack.get(current.getDirection(0));

            //need to catch ones BEFORE switch
            if(current.getCurrentDirection() == -2 && !(current.getType().equals("Switch"))) {
                return null;
            }

            //chose direction FROM SWITCH
            if (current.getType().equals("Switch")) {
                int index = (current.getIndex()) ? 1 : 0;
                if((index == 0 && current.getDirectionStates(0) == cur))
                    return greenTrack.get(current.getDirection(index)); // go same direction don't change
                else if (index == 1 && current.getDirectionStates(2) == prev)
                    return greenTrack.get(current.getDirection(index)); // go same direction don't change
                else if(cur == 58 || (cur == 62 && index ==0))
                    return greenTrack.get(current.getDirection(index));
                else
                    return null;
            }

            // If one way Track Simply go straight down
            if (!current.getBiDirectional()) // one way track {
                return greenTrack.get(current.getDirection(0));

            //if 2 way track
            if(previous.getDirection(0) == cur)
                return greenTrack.get(current.getDirection(0));
            else if (previous.getDirection(1) == cur)
                return greenTrack.get(current.getDirection(1));

            if(current.getCurrentDirection() > 0) // switch is on so OKAY to proceed
                return greenTrack.get(0);

            return ret;
        }

        /*dispatch line*/
        public void dispatchLine(int a){
            if(a == 0)
                DISPATCH_LINE = "Green"; // this tells sim environment which track to release train from yard
            else if(a == 1)
                DISPATCH_LINE = "Red";
        }

        /**
        adding getNext
         This function is called by simulation environment and from current and previous
         get next block. Need to check if green or red track and call functions accordingly
         **/
        public TrackElement getNext(TrackElement current, TrackElement previous) {
            TrackElement ret = null;
            if(current != null) {
                if (current.getLine().equals("Green") || current.getBlockNum() == 0 && DISPATCH_LINE.equals("Green"))
                    ret = getNextGreen(current, previous);
                else if (current.getLine().equals("Red") || current.getBlockNum() == 0 && DISPATCH_LINE.equals("Red"))
                    ret = getNextRed(current, previous);
            }

        return ret;
        }

        /*Get block - ALL BLOCKS from ALL LINES */
        public TrackElement getBlock(int a){
            if(a >= 0 && a < blockArrayList.size()+1){
                return blockArrayList.get(a);
            }

            return null;
        }

        /*Get block from a PARTICULAR LINE  */
        public TrackElement getBlockLine(int blockNum, String line){

            TrackElement temp = null;
            if(line.equals("Red")) {
                if(blockNum >=0 && blockNum <= SIZE_LINE_A)
                    temp = redTrack.get(blockNum);
            }
            else if(line.equals("Green")) {
                if(blockNum >=0 && blockNum <= SIZE_LINE_B)
                   temp =  greenTrack.get(blockNum);
            }
            return temp;

        }

        //getting track heater status
        public boolean getTrackHeaterStatus(){
            return trackHeater;
        }
        //updating tickets FOR THE CTC -- calculating total tickets sold
        public int updateTickets(){
            int ticketTotal = 0;
            for(int i=0; i<stationsArrayList.size();i++)
                ticketTotal += stationsArrayList.get(i).getTicketSales();

            return ticketTotal;
        }

        //FOR the module -- increase and setting ticket sales whenever sim environment calls
        public void increaseTickets(){
            //updating all ticketSales
            for(int i=0; i<stationsArrayList.size();i++)
                stationsArrayList.get(i).setTicketSales();
        }


        /**Set Environmental Temperature*
         * this function depending on temperature will set heater accordingly
         * chose 35 because can get close to freezing for safety precautions*/
        public void setEnvironmentalTemperature(double a){
            if( a > -20 && a < 130){
                this.environmentalTemperature = a;

                //Need to Turn Track Heater on if turn this on
                if(this.environmentalTemperature < 35)
                    trackHeater = true;
                if(this.environmentalTemperature > 35)
                    trackHeater = false;
            }
        }

        /*Get Environmental Temperature */
        public double getEnvironmentalTemperature(){
            return this.environmentalTemperature;
        }

        /*get size of overall track - both red and green */
        public int getSize(){
            return this.blockArrayList.size();
        }

        /*Need to set and fix failures for particular block on particular line*/
        public void setFailure(int blockNum, String line, int Failure){
            boolean contains = false;
            int location = -1;

            if(Failure >= 0 && Failure <= 4) {
                //to check if block is line A
                if(line.equals("Red") && blockNum <= SIZE_LINE_A) {
                    redTrack.get(blockNum).setFailureStatus(Failure);
                    //Check if already in failure if not add to failure array list
                    for (int i = 0; i < failureArrayList.size(); i++)  {
                        if (failureArrayList.get(i).getBlockNum() == blockNum && failureArrayList.get(i).getLine().equals("Red")) {
                            contains = true;
                            location = i;
                        }

                    }


                    if(Failure != 0 ) {
                        if(!contains) // do not want to add duplicates
                            failureArrayList.add(redTrack.get(blockNum));
                    }
                    else { //0 means no failure so remove from arraylist
                        if(contains) // remove it if it is in the list
                            failureArrayList.remove(location);
                    }
                }
                else if (line.equals("Green") && blockNum <= SIZE_LINE_B){
                    greenTrack.get(blockNum).setFailureStatus(Failure);

                    for (int i = 0; i < failureArrayList.size(); i++)  {
                        if (failureArrayList.get(i).getBlockNum() == blockNum && failureArrayList.get(i).getLine().equals("Green")) {
                            contains = true;
                            location = i;
                        }
                    }

                    if(Failure != 0 ) {
                        if(!contains)
                            failureArrayList.add(greenTrack.get(blockNum));
                    }
                    else {
                        if(contains)
                            failureArrayList.remove(location);
                    }


                }
            }
        }


        /**getting blocks in line
         * This function is needed for CTC if they want to close section
         * have a char section to close and will return ALL blocks in the section
         * */
        public ArrayList<Integer> blocksInSection(char section, char Line){
            ArrayList<Integer> blocks = new ArrayList<Integer>();

            if(Line == 'G') {
            for(int i=1; i<greenTrack.size(); i++){
                if(greenTrack.get(i).getSection() == section)
                    blocks.add(greenTrack.get(i).getBlockNum());
            }}
          if(Line == 'R'){
              for(int i=1; i<redTrack.size(); i++){
                  if(redTrack.get(i).getSection() == section)
                      blocks.add(redTrack.get(i).getBlockNum());
              }
          }

            return blocks;
        }

        /*getting beacons -- to load beacons */
        public void setBeaconArray(){
            for(int i =0; i< blockArrayList.size() ; i++){
                if(blockArrayList.get(i).getBeacon() != null)
                    beacons.add(blockArrayList.get(i));
            }
        }

        public ArrayList<TrackElement> getBeaconArray(){
            return beacons;
        }


        /*To String Methods */
        @Override
        public String toString() {
            return blockArrayList.toString();
        }

        /*used by sim environment to call and increase tickets */
        @Override
        public void updatePhysics(String currentTimeString, double deltaTime_inSeconds) {
            updateSwitches();
            totalTime += deltaTime_inSeconds;
            if((int)totalTime%15 == 0)
                increaseTickets();
        }
    }


