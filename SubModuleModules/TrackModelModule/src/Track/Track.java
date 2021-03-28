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
        int SIZE_LINE_A=0;
        int SIZE_LINE_B=0;
        String lineA;
        String lineB;
        double environmentalTemperature;

        //Constructor Methhod
        Track() {
            blockArrayList = new ArrayList<>();
            stationsArrayList = new ArrayList<>();
            switchesArrayList = new ArrayList<>();
            failureArrayList = new ArrayList<>();
            environmentalTemperature = 60;
            TRACK_HEATER= false;
        }

        //Import Track from a File
        boolean validFile( String filePath){
            //Testing if true or not
            boolean success = false;
            try {
                //Testing input the File -- Need to get input from MAIN file
                Scanner sc = new Scanner(new File(filePath));
                success = true;
            } catch(Exception e){}
            return success;
        }


        boolean importTrack(String filepath){
            boolean success = false;
            try {
                Scanner sc = new Scanner(new File(filepath));
                sc.useDelimiter(",|\\n");
                //creating wb object for xls file
                int count = 0;
                while(sc.hasNext()){

                    if(count < 14 ) {
                        sc.next();
                        count++;
                        continue;
                    }

                    //Parsing File into components

                    String line = sc.next();
                    if(count == 14)
                        lineA = line; //Grabbing the name of the first line
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


                    //Collecting Size information
                    if(line.equals(lineA))
                        SIZE_LINE_A ++;
                    else {
                        lineB = line;
                        SIZE_LINE_B ++;
                    }

                    //For Yard
                    if(infrastructure.length()>3 && infrastructure.substring(0,3).equals("YARD")){
                        Station Test = new Station(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);

                        blockArrayList.add(Test);

                    }


                    //Need to check to see if stations are here and if so need to set Beacon Value and calculate tickets
                    if(infrastructure.length()>7 && infrastructure.substring(0,7).equals("STATION")){
                        Station Test = new Station(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);

                        Test.setBeacon(Test.getInfrastructure().substring(9));
                        stationsArrayList.add(Test);
                        blockArrayList.add(Test);

                    }
                    else if(infrastructure.length()>6 && infrastructure.substring(0,6).equals("SWITCH")){
                        Switch Test = new Switch(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);
                        switchesArrayList.add(Test);
                        blockArrayList.add(Test);
                    }
                    else {
                        TrackBlock Test = new TrackBlock(line, section,blockNum,length,grade,speedLimit,infrastructure,elevation,cumulativeElevation,setDirection,setBiDirectional);
                        blockArrayList.add(Test);
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
        ArrayList<TrackElement> getBlocks(){
            return blockArrayList;
        }

        ArrayList<Station> getStations(){
            return stationsArrayList;
        }

        ArrayList<Switch> getSwitches(){
            return switchesArrayList;
        }

        ArrayList<TrackElement> getFailures(){
            return failureArrayList;
        }



        /*Get block - ALL BLOCKS from ALL LINES */
        TrackElement getBlock(int a){
            if(a > 0 && a < blockArrayList.size()+1){
                return blockArrayList.get(a-1);
            }
            return null;
        }

        /*Get block from a PARTICULAR LINE  */
        TrackElement getBlockLine(int blockNum, String line){

            TrackElement temp = null;
            if(line.equals(lineA)) {
                if(blockNum >0 && blockNum <= SIZE_LINE_A)
                    temp = blockArrayList.get(blockNum-1);
            }
            else if(line.equals(lineB)) {
                if(blockNum >0 && blockNum <= SIZE_LINE_B)
                    temp = blockArrayList.get(blockNum+SIZE_LINE_A-1);
            }
            return temp;

        }


        boolean getTrackHeaterStatus(){
            return TRACK_HEATER;
        }



        /*Set Environmental Temperature*/
        void setEnvironmentalTemperature(double a){
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
        double getEnvironmentalTemperature(){
            return this.environmentalTemperature;
        }

        int getSize(){
            return this.blockArrayList.size();
        }

        /*Need to set and fix failures for particular block on particular line*/
        void setFailure(int blockNum, String line, int Failure){
            boolean contains = false;
            int location = -1;

            if(Failure >= 0 && Failure <= 3) {
                //to check if block is line A
                if(line.equals(lineA) && blockNum <= SIZE_LINE_A) {
                    blockArrayList.get(blockNum-1).setFailureStatus(Failure);
                    //Check if already in failure if not add to failure array list
                    for (int i = 0; i < failureArrayList.size(); i++)  {
                        if (failureArrayList.get(i).getBlockNum() == blockNum && failureArrayList.get(i).getLine().equals(lineA)) {
                            contains = true;
                            location = i;
                        }

                    }


                    if(Failure != 0 ) {
                        if(!contains)
                            failureArrayList.add(blockArrayList.get(blockNum-1));
                    }
                    else { //0 means no failure so remove from arraylist
                        if(contains)
                            failureArrayList.remove(location);
                    }
                }
                else if (line.equals(lineB) && blockNum <= SIZE_LINE_B){
                    blockArrayList.get(SIZE_LINE_A+blockNum-1).setFailureStatus(Failure);

                    for (int i = 0; i < failureArrayList.size(); i++)  {
                        if (failureArrayList.get(i).getBlockNum() == blockNum && failureArrayList.get(i).getLine().equals(lineB)) {
                            contains = true;
                            location = i;
                        }
                    }

                    if(Failure != 0 ) {
                        if(!contains)
                            failureArrayList.add(blockArrayList.get(blockNum-1+SIZE_LINE_A));
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

        String printStations(){
            return stationsArrayList.toString();
        }

        String printSwitches(){
            return switchesArrayList.toString();
        }



    }


