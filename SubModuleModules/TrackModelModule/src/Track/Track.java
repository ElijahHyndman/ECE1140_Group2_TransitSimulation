package Track;


import TrackConstruction.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

    public class Track {

        //Members in Class
        ArrayList<TrackElement> blockArrayList;
        ArrayList<Station> stationsArrayList;
        ArrayList<Switch> switchesArrayList;
        ArrayList<TrackElement> failureArrayList;

        //Track Variables
        boolean TRACK_HEATER;
        int SIZE_LINE_A =0;
        int SIZE_LINE_B=0;
        double environmentalTemperature;

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
            TRACK_HEATER= false;
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

                    //For Yard
                    if(infrastructure.length()>3 && infrastructure.substring(0,3).equals("YARD")){
                        TrackBlock Test = new TrackBlock(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);
                        blockArrayList.add(Test);
                        redTrack.add(Test);
                        greenTrack.add(Test);
                    }


                    //Need to check to see if stations are here and if so need to set Beacon Value and calculate tickets
                    if(infrastructure.length()>7 && infrastructure.substring(0,7).equals("STATION")){
                        Station Test = new Station(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);

                        stationsArrayList.add(Test);
                        blockArrayList.add(Test);

                        if(line.equals("Red"))
                            redTrack.add(Test);
                        else
                            greenTrack.add(Test);

                    }
                    else if(infrastructure.length()>6 && infrastructure.substring(0,6).equals("SWITCH")){
                        Switch Test = new Switch(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);
                        switchesArrayList.add(Test);
                        blockArrayList.add(Test);

                        if(line.equals("Red"))
                            redTrack.add(Test);
                        else
                            greenTrack.add(Test);
                    }
                    else {
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
            } catch(Exception e)
            {
                e.printStackTrace();
            }
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

            for(int i = 7; i <= 12; i++) {
                Switch switchT = switchesArrayList.get(i);
                boolean index = switchT.getIndex();
                int blocka = switchT.getDirectionStates(0);
                int toA = switchT.getDirectionStates(1);
                int blockb = switchT.getDirectionStates(2);
                int toB = switchT.getDirectionStates(3);
                //here need to CHANGE getCurrent Direction
                if (index == false) {
                    if (blocka != 0 && blocka != -1) {
                        greenTrack.get(blocka).setCurrentDirection(toA); // toA
                        if (!(blocka == blockb) && blockb != 0)
                            greenTrack.get(blockb).setCurrentDirection(-2); // -2 -- means need this switch to work
                    } else if (blocka == -1) { // this is switching TO THE YARD
                        switchT.setCurrentDirection(-1);
                    } else if (blockb == -1 && blocka == 0) { // this is switching TO THE YARD
                        switchT.setCurrentDirection(-2);
                    }
                } else if (index == true) {
                    //Second Number Block Points to last number
                    if (blockb != 0 && blockb != -1) {
                        greenTrack.get(blockb).setCurrentDirection(toB); // toB

                        if (blocka != blockb)
                            greenTrack.get(blocka).setCurrentDirection(-2); //-2 means need this switch to work
                    } else if (blockb == 0 && blocka == -1) { // this is switching TO THE YARD
                        switchT.setCurrentDirection(-2);
                    } else if (blockb == -1 && blocka == 0) { // this is switching TO THE YARD
                        switchT.setCurrentDirection(-1);
                    }
                }
            }

        }

        /*adding getNext */
        public TrackElement getNext(TrackElement current, TrackElement previous) {
            int cur = current.getBlockNum();
            int prev = previous.getBlockNum();
            char sectionPrev = previous.getSection();
            char sectionCur = current.getSection();
            TrackElement ret = null;

            //dealing with the yard
             if(current.getCurrentDirection() == -1 && current.getDirectionStates(0) == -1){
                return greenTrack.get(0); // This returns the YARD BLOCK when switch is on !!
            }else if (current.getCurrentDirection() == -1)
                return null;

             //dealing with directionality
            if(current.getCurrentDirection() > 0) // switch is on
                ret = greenTrack.get(current.getCurrentDirection());
            else if(!current.getBiDirectional()) // one way track
                   ret = greenTrack.get(current.getDirection(0));
            else if(previous.getDirection(0) == cur) { // continue go in positive diretion
                    if(current.getLine().equals("Green"))
                        ret = greenTrack.get(current.getDirection(0));
                    else
                        ret = redTrack.get(current.getDirection(0));
                }
            else if(previous.getDirection(1) == cur){ // continue to go in negative direction
                    if(current.getLine().equals("Green"))
                         ret = greenTrack.get(current.getDirection(1));
                    else
                         ret = redTrack.get(current.getDirection(1));
            }
            else if(previous.getDirection(0) == 0)
                ret = greenTrack.get(current.getDirection(0));


            //Green Track Edge Cases -- Where Directions Change
            if(prev == 100 && cur == 85){
                ret = greenTrack.get(current.getDirection(1));
                return ret;
            }
            if(prev == 150 && cur == 28){
                ret = greenTrack.get(current.getDirection(1));
            }
            if(prev == 1 && cur == 13){
                ret = greenTrack.get(current.getDirection(0));
            }


            //Checking if switch statuses are correct
            if(current.getCurrentDirection() == -2 && current.getDirection(0) == 0) {
                return null;
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


        public boolean getTrackHeaterStatus(){
            return TRACK_HEATER;
        }



        /*Set Environmental Temperature*/
        public void setEnvironmentalTemperature(double a){
            if( a > -20 && a < 130){
                this.environmentalTemperature = a;

                //Need to Turn Track Heater on if turn this on
                if(this.environmentalTemperature < 35)
                    TRACK_HEATER = true;
                if(this.environmentalTemperature > 35)
                    TRACK_HEATER = false;
            }
        }

        /*Get Environmental Temperature */
        public double getEnvironmentalTemperature(){
            return this.environmentalTemperature;
        }

        int getSize(){
            return this.blockArrayList.size();
        }

        /*Need to set and fix failures for particular block on particular line*/
        public void setFailure(int blockNum, String line, int Failure){
            boolean contains = false;
            int location = -1;

            if(Failure >= 0 && Failure <= 3) {
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
                        if(!contains)
                            failureArrayList.add(redTrack.get(blockNum));
                    }
                    else { //0 means no failure so remove from arraylist
                        if(contains)
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



        /*May Need a Method for Occupy -- Here send out beacon (if station), and authority / command speed?? */

        /*To String Methods */
        @Override
        public String toString() {
            return blockArrayList.toString();
        }




    }


