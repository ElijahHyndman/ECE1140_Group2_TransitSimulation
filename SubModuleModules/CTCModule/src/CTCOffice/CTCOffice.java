package CTCOffice;//Haleigh DeFoor

import PLCOutput.*;
import PLCInput.*;
import SimulationEnvironment.SimulationEnvironment;
import Track.Track;
import TrackConstruction.Switch;
import TrackConstruction.TrackElement;
import WaysideController.PLCEngine;
import WaysideController.WaysideSystem;
import java.util.*;
import WorldClock.PhysicsUpdateListener;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CTCOffice implements PhysicsUpdateListener
{
    private int thruP;
    private Object[] speedAuthorityTime = new Object[3];
    private boolean status;
    private int blockNum;
    private String lineCol;
    ArrayList<DisplayLine> dispArr = new ArrayList<>();
    private int trainNum;
    private LocalTime timeDisp, timeDis;
    private int comp;
    private int routeLength;
    private char sect;
    private int blockL;
    private double bGrade;
    private int sLim;
    private double elev, cElev;
    private String inf;
    private String t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;
    private boolean occ;
    private double speed;
    private int authority;
    public ArrayList<WaysideSystem> waysides = new ArrayList<WaysideSystem>();// waysideG, waysideR;
    private double[] speedArrG = new double[151];
    private double[] speedArrR = new double[151];
    private int[] route = new int[151];
    private int[] authArr = new int[151];
    public CharSequence timeNow = "00:00:00";
    private LocalTime now;
    public Track trackObj = new Track();
    public SimulationEnvironment SEobj;
    public int[] positions = {429,429,429,429,429,429,429,429,429,429};
    public ArrayList<double[]> speedsR =new ArrayList<double[]>();
    public ArrayList<double[]> speedsG =new ArrayList<double[]>();
    public ArrayList<int[]> authorities = new ArrayList<int[]>();
    public ArrayList<LocalTime> times = new ArrayList<LocalTime>();
    public String UIcol;

    public int[] greenPath= {0,62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58};
    public int[] redPath = {0,9,8, 7, 6, 5, 4, 3, 2, 1, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 52, 51, 50, 49, 48, 47, 46, 45, 44, 43, 67, 68, 69, 70, 71, 38, 37, 36, 35, 34, 33, 32, 72, 73, 74, 75, 76, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9};
    public int[] greenPathNY= {62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58,59,60,61,62};

    public CTCOffice() {
        trackObj = new Track();
      //  SEobj = new SimulationEnvironment();
    }
    public CTCOffice (SimulationEnvironment SE) {
        this.SEobj = SE;
    }
    public CTCOffice(Track SEtrack, SimulationEnvironment SE) throws Exception {
        updateTrack(SEtrack);
        SEobj = SE;
    }

    public void updateCTC(Track trackSystem, SimulationEnvironment SE){
        SEobj = SE;
        trackObj = trackSystem;
    }
    public void updateTrack(Track trackSystem) throws Exception {
        // Assert: We will only be using red and green track lines from track
        ArrayList<TrackElement> redLine;
        ArrayList<TrackElement> greenLine;
        try {
            greenLine = trackSystem.getGreenLine();
            WaysideSystem greenWS = new WaysideSystem(greenLine, "Green");
            waysides.add(greenWS);
        } catch (Exception greenLineGenerationError) {
            greenLineGenerationError.printStackTrace();
        }
        try {
            redLine = trackSystem.getRedLine();
            WaysideSystem redWS = new WaysideSystem(redLine,"Red");
            waysides.add(redWS);
        } catch (Exception redLineGenerationError) {
            redLineGenerationError.printStackTrace();
        }
        this.trackObj = trackSystem;
    }

    public void setSE(SimulationEnvironment SE) {
        this.SEobj = SE;
    }

    public ArrayList<WaysideSystem> getWaysideSystem() {
        return waysides;
    }

    public void setWaysideSystem(ArrayList<WaysideSystem> SEws) {
        waysides = SEws;
    }

    public Track getTrack() {
        return trackObj;
    }

    public Object[] Dispatch(String dest, String tNum, String timeD) throws Exception {
        //speedAuthority[0] is speed
        //speedAuthority[1] is authority
        //speedAuthority[2] is dispatch time
        //Vector<TrainUnit> trains = SEobj.getTrains();

        if (tNum.equals("Train 1")) {
            trainNum = 1;
        }
        else if (tNum.equals("Train 2"))
            trainNum = 2;
        else if (tNum.equals("Train 3"))
            trainNum = 3;
        else if (tNum.equals("Train 4"))
            trainNum = 4;
        else if (tNum.equals("Train 5"))
            trainNum = 5;
        else if (tNum.equals("Train 6"))
            trainNum = 6;
        else if (tNum.equals("Train 7"))
            trainNum = 7;
        else if (tNum.equals("Train 8"))
            trainNum = 8;
        else if (tNum.equals("Train 9"))
            trainNum = 9;
        else if (tNum.equals("Train 10"))
            trainNum = 10;
        else {
            trainNum = 1;
            SEobj.spawnRunningTrain(trackObj.getBlock(0), trackObj.getBlock(0)); //why after the else
        }

        if (dest.equals("Station B")){
            blockNum = 10;
            lineCol = "Blue";}
        else if (dest.equals("Station C")){
            blockNum = 15;
            lineCol = "Blue";}
        else if (dest.equals("Shadyside")){
            blockNum = 7;
            lineCol = "Red";}
        else if (dest.equals("Herron Ave")){
            blockNum = 16;
            lineCol = "Red";}
        else if (dest.equals("Swissville")){
            blockNum = 21;
            lineCol = "Red";}
        else if (dest.equals("Penn Station")){
            blockNum = 25;
            lineCol = "Red";}
        else if (dest.equals("Steel Plaza")){
            blockNum = 35;
            lineCol = "Red";}
        else if (dest.equals("First Ave")){
            blockNum = 45;
            lineCol = "Red";}
        else if (dest.equals("Station Square")){
            blockNum = 48;
            lineCol = "Red";}
        else if (dest.equals("South Hills Junction")){
            blockNum = 60;
            lineCol = "Red";}
        else if(dest.equals("Pioneer")){
            blockNum = 2;
            lineCol = "Green";}
        else if(dest.equals("Edgebrook")){
            blockNum = 9;
            lineCol = "Green";}
        else if(dest.equals("Station")){
            blockNum = 16;
            lineCol = "Green";}
        else if(dest.equals("Whited")){
            blockNum = 22;
            lineCol = "Green";}
        else if(dest.equals("South Bank")){
            blockNum = 31;
            lineCol = "Green";}
        else if(dest.equals("Central")){
            blockNum = 39;
            lineCol = "Green";}
        else if(dest.equals("Inglewood")){
            blockNum = 48;
            lineCol = "Green";}
        else if(dest.equals("Overbrook")){
            blockNum = 57;
            lineCol = "Green";}
        else if(dest.equals("Glenbury")){
            blockNum = 65;
            lineCol = "Green";}
        else if(dest.equals("Dormont")){
            blockNum = 73;
            lineCol = "Green";}
        else if(dest.equals("Mt Lebanon")){
            blockNum = 77;
            lineCol = "Green";}
        else if(dest.equals("Poplar")){
            blockNum = 88;
            lineCol = "Green";}
        else if(dest.equals("Castle Shannon")){
            blockNum = 96;
            lineCol = "Green";}
        else if (dest.equals("OffShoot 1")){
            blockNum = 69;
            lineCol = "Red";
        }
        else if (dest.equals("OffShoot 2")){
            blockNum =74;
            lineCol = "Red";
        }
        else if (dest.equals("Yard")){
            blockNum = 0;
            lineCol = CheckColor();
        }
        else{
            blockNum = 0;
            lineCol = "Green";}

        CharSequence timeChar = timeD;
        timeDis = LocalTime.parse(timeChar);
        now = LocalTime.parse(timeNow);

        int h1 = timeDis.getHour();
        int h2 = now.getHour();
        int m1 = timeDis.getMinute();
        int m2 = now.getMinute();

        double temp = m1-m2;
        temp = temp/60;
        int hsub = h1-h2;
        if (m1<m2)
        {
            hsub = hsub-1;
        }
        temp = temp+hsub;


        int startNum = 0;

        if (positions[trainNum-1] == 429){
            startNum=0;
        }
        else{
            startNum = positions[trainNum-1];
        }

        int endNum = blockNum;

        if (lineCol.equals("Green")){
            route = routeGreen(startNum,endNum);
        }
        else if (lineCol.equals("Red")){
            route = routeRed(startNum,endNum);
        }

        positions[trainNum-1] = endNum;
        routeLength = calcRouteLength(route, lineCol);

        speed = routeLength/1000/temp;
        //speed = 15;
        authority = calcAuthority(route);
        authArr = createAuthArr(route, authority);

        if (speed<5)
        {
            speed = 5;
            double timeTravel = 1/(speed*1000/routeLength/60);
            long mins = (long)timeTravel;
            timeDisp = timeDis.minusMinutes(mins);
        }
        else {
            timeDisp = now;
        }

        if (lineCol.equals("Green"))
            speedArrG = createSpeedArr(route, speed);
        else if (lineCol.equals("Red"))
            speedArrR = createSpeedArr(route, speed);

        //if(LocalTime.now().isBefore(timeDisp) && speed<50)
        if (now.isBefore(timeDisp)) {
            speedAuthorityTime[0] = speed*0.621371;
            speedAuthorityTime[1] = authority;
            speedAuthorityTime[2] = timeDisp;

            if (lineCol.equals("Green")){
                speedsG.add(speedArrG);
                speedsR.add(speedArrR);
                times.add(timeDisp);
                authorities.add(authArr);
            }
            else if (lineCol.equals("Red")){
                speedsG.add(speedArrG);
                speedsR.add(speedArrR);
                times.add(timeDisp);
                authorities.add(authArr);
            }
        }
        else {
            speedAuthorityTime[0] = 0;
            speedAuthorityTime[1] = 0;
            speedAuthorityTime[2] = 0;
        }

        BroadcastingArrays();

        return speedAuthorityTime;
    }

    public void ClearQueues(){
        times.clear();
        speedsG.clear();
        speedsR.clear();
        authorities.clear();
    }

    public void GiveColor(String col){
        UIcol = col;
    }

    public String CheckColor(){
        return UIcol;
    }

    public void BroadcastingArrays() throws Exception {
        now = LocalTime.parse(timeNow);
        int count = 0;
        for (int i = 0; i<times.size(); i++){
            if(now.equals(times.get(i))){

                double[] speedsRholder = new double[150];
                int[] authsRholder = new int[150];
                authsRholder = authorities.get(i);
                int[] authsCut = new int[77];
                speedsRholder = speedsR.get(i);
                double[] speedsRcut = new double[77];
                for (int j=0; j<speedsRcut.length; j++){
                    speedsRcut[j] = speedsRholder[j];
                    if (speedsRholder[j] != 0){
                        count++;
                    }
                    authsCut[j] = authsRholder[j];
                }

                if (count!=0){
                    getWaysideSystem("Red").broadcastToControllers(speedsRcut, authsCut);
                }
                else{
                    getWaysideSystem("Green").broadcastToControllers(speedsG.get(i), authorities.get(i));
                }

                times.remove(i);
                speedsR.remove(i);
                speedsG.remove(i);
                authorities.remove(i);
            }
        }
    }

    public void LoadSchedule(String filename){
        try{
        Scanner input = new Scanner(new File(filename));
        //read files to get input and read data to UI
        input.useDelimiter(",|\\n");
        int count = 0;
        while (input.hasNext()) {

            if (count<19) {
                count++;
                input.next();
                continue;
            }

            lineCol = input.next();
            sect = input.next().charAt(0);
            blockNum = Integer.parseInt(input.next());
            blockL = Integer.parseInt(input.next());
            bGrade = Double.parseDouble(input.next());
            sLim = Integer.parseInt(input.next());
            inf = input.next();
            elev = Double.parseDouble(input.next());
            cElev = Double.parseDouble(input.next());
            t1 = input.next();
            t2 = input.next();
            t3 = input.next();
            t4 = input.next();
            t5 = input.next();
            t6 = input.next();
            t7 = input.next();
            t8 = input.next();
            t9 = input.next();
            t10 = input.next();

            DisplayLine disp = new DisplayLine(blockNum, lineCol, sect, blockL, sLim, bGrade, elev, cElev, inf, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            dispArr.add(disp);
            count++;
        }
        input.close();
    }catch(Exception e) {
        e.printStackTrace();
    }
    }

    public int CalcThroughput() {
        trackObj = new Track();
        int tix = getTickets();
        now = LocalTime.parse(timeNow);
        int hours = now.getHour();
        int mins = now.getMinute();
        double totalT = mins/60;
        totalT = totalT+hours;

        thruP = (int) Math.round(tix/totalT);
        return thruP;
    }

    public boolean CheckOcc(int blockNum, String lineCol) throws Exception {

        if (lineCol.equals("Green")){
            occ = getWaysideSystem("Green").getOccupancy(blockNum);
        }
        else if (lineCol.equals("Red")){
            occ = getWaysideSystem("Red").getOccupancy(blockNum);
        }

        return occ;
    }

    public boolean CheckSectOcc(int blockNum, String lineCol) throws Exception {
        char section = dispArr.get(blockNum).getSection();
        char lineChar = lineCol.charAt(0);
        ArrayList<Integer> blocks = trackObj.blocksInSection(section,lineChar);
        int length = blocks.size();
        boolean[] occs = new boolean[length];
        boolean totalocc = false;

        if (lineCol.equals("Green")) {
            for (int i = 0; i < length; i++) {
                occs[i] = getWaysideSystem("Green").getOccupancy(blocks.get(i));
            }
            for (int i = 0; i < length; i++) {
                if (occs[i]) {
                    totalocc = true;
                }
            }
        }
        else if (lineCol.equals("Red")){
            for (int i = 0; i < length; i++) {
                occs[i] = getWaysideSystem("Red").getOccupancy(blocks.get(i));
            }
            for (int i = 0; i < length; i++) {
                if (occs[i]) {
                    totalocc = true;
                }
            }
        }
        return totalocc;
    }

    public boolean CheckSwitch(int bNum, String lineColor) throws Exception {
        boolean switchstat = false;

        if (lineColor.equals("Green")){
            switchstat = getWaysideSystem("Green").getSwitchStatus(bNum);
        }
        else if (lineColor.equals("Red")){
            switchstat = getWaysideSystem("Red").getSwitchStatus(bNum);
        }

        return switchstat;
    }

    public void ToggleSwitch(int switchNum, boolean stat, String lineCol) throws Exception {
        boolean switchstat = !stat;
        if (lineCol.equals("Green")){
            getWaysideSystem("Green").setSwitchStatus(switchNum,switchstat);
        }
        else if (lineCol.equals("Red")){
            getWaysideSystem("Red").setSwitchStatus(switchNum,switchstat);
        }
    }

    public void OpenTrack(int blockNum, String lineCol) throws Exception {
        char section = dispArr.get(blockNum).getSection();
        char lineChar = lineCol.charAt(0);
        ArrayList<Integer> blocks = trackObj.blocksInSection(section,lineChar);
        int length = blocks.size();
        if (lineCol.equals("Green")) {
            for (int i = 0; i < length; i++) {
                getWaysideSystem("Green").setOpen(blocks.get(i));
            }
        }
        else if (lineCol.equals("Red")) {
            for (int i = 0; i < length; i++) {
                getWaysideSystem("Red").setOpen(blocks.get(i));
            }
        }
    }

    public void CloseTrack(int blockNum, String lineCol) throws Exception {
        char section = dispArr.get(blockNum).getSection();
        char lineChar = lineCol.charAt(0);
        ArrayList<Integer> blocks = trackObj.blocksInSection(section,lineChar);
        int length = blocks.size();

        if (lineCol.equals("Green")) {
            for (int i = 0; i < length; i++) {
                getWaysideSystem("Green").setClose(blocks.get(i));
            }
        }
        else if (lineCol.equals("Red")) {
            for (int i = 0; i < length; i++) {
                getWaysideSystem("Red").setClose(blocks.get(i));
            }
        }
    }

    public int calcRouteLength(int[] routeAr, String lineCol)
    {
        int routeLength=0;
        for(int i=0; i<routeAr.length; i++){

            if(routeAr[i] == 1){
                if(lineCol.equals("Green")) {
                    routeLength += trackObj.getGreenLine().get(i).getLength();
                }
                else{
                    routeLength += trackObj.getRedLine().get(i).getLength();
                }
            }

        }
        return routeLength;

    }

    /*GIH6 adding back to yard*/
    public int[] backToYardGreen(int bn){
        int[] newGreenLine = greenPath;
        int flag=0;
        for(int i = 0; i < 176; i++){
            if(newGreenLine[i] != bn) {
                newGreenLine[i] = 0;
            }
            if(newGreenLine[i] == bn)
                break;

        }


        int[] RouteAr = new int[150];
        for(int i=0; i < 176; i++) {
            if (newGreenLine[i] != 0)
                RouteAr[newGreenLine[i] - 1] = 1;
        }
        return RouteAr;
    }

    /*GIH6 adding dispatch - arbitrary block to next*/
    public int[] routeGreen(int start, int end){
        int[] newGreenLine = greenPathNY;
        int flag=0;
        int flag2 =0;
        int beacon2 =0;

        for(int i = 0; i < 180; i++){
            if(newGreenLine[i] != start && flag == 0) {
                newGreenLine[i] = 0;
            }
            if(newGreenLine[i] == start) {
                flag = 1;
            }
            else if(flag == 1 && newGreenLine[i] == end) {
                if(flag2 != 1)
                    beacon2 = newGreenLine[i-2];
                flag2=1;

            }
            else if(flag2 == 1) {
                newGreenLine[i] = 0;
            }
        }


        int[] RouteAr = new int[151];
        for(int i=0; i < 176; i++) {
            if (start == 0) {
                RouteAr[0] = 3;
                RouteAr[62]=1;
            }
            if(end == 0)
                RouteAr[0] =1;
            if(i == beacon2)
                RouteAr[i] = 2;

            if (newGreenLine[i] != 0)
                RouteAr[newGreenLine[i]] = 1;
        }

        return RouteAr;
    }

    /*GIH6 adding red route*/
    public int[] routeRed(int start, int end){
        int[] newRedLine = redPath;
        int flag=0;
        int flag2 =0;
        int counter =0;
        int beacon2=0;
        for(int i = 0; i < 106; i++){
            if(newRedLine[i] != start && flag == 0) {
                newRedLine[i] = 0;
            }
            if(newRedLine[i] == start && flag2 == 0) {
                flag = 1;
            }
            else if(flag == 1 && newRedLine[i] == end-1) {

                if(flag2 != 1)
                beacon2 = newRedLine[i-1];

                flag2=1;
            }
            else if(flag2 == 1) {
                newRedLine[i] = 0;
            }

        }

        int[] RouteAr = new int[151];
        for(int i = 0;i < RouteAr.length;i++){
            RouteAr[i] = 0;
        }

        for(int i=0; i < 106; i++) {
            if(start == 0) {
                RouteAr[0] = 3;
                RouteAr[9] = 1;
            }
            if(end == 0){
                RouteAr[0]=3;
                RouteAr[10]=1;
                RouteAr[9] =1;
            }
            if(i == beacon2)
                RouteAr[beacon2]=2;

            if (newRedLine[i] != 0)
                RouteAr[newRedLine[i]] = 1;
        }
        return RouteAr;
    }


    /*GIH6 adding back to yard*/
    public int[] backToYardRed(int bn){
        int[] newRedLine = redPath;
        int flag=0;
        for(int i = 0; i < 106; i++){
            if(newRedLine[i] != bn) {
                newRedLine[i] = 0;
            }
            if(newRedLine[i] == bn)
                break;

        }

        int[] RouteAr = new int[76];
        for(int i=0; i < 106; i++) {
            if (newRedLine[i] != 0)
                RouteAr[newRedLine[i] - 1] = 1;
        }
        return RouteAr;
    }

    public double[] calcRoute(int bn, String lc, int tnum) {
        double[] routeArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        return routeArr;
    }

    public double[] createSpeedArr(int[] rA, double sp) {
        double[] sArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i=0; i<151; i++)
        {
            if(rA[i] != 0)
                sArr[i] = sp;
        }

        return sArr;
    }

    public int calcAuthority(int[] routeArr) {
        int count=0;
        for (int i=0; i<151; i++){
            if(routeArr[i]!=0)
                count++;
        }
        return count;
    }

    public int[] createAuthArr(int[] rA, int auth) {
        int[] aArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i=0; i<151; i++){
            if (rA[i]==1) {
                /*for (int j = i; j < auth+i; j++) {
                    aArr[j] = a;
                    a--;
                }
                break;*/
                aArr[i] = auth;
            }
            else if (rA[i]==2) {
                aArr[i] = 888;
            }
        }

        return aArr;
    }

    public int getTickets() {
        int tix = trackObj.updateTickets();
        return tix;
    }

    public void updatePhysics(String currentTimeString, double deltaTime_inSeconds) throws Exception {

        this.timeNow = currentTimeString;
        BroadcastingArrays();
    }

    public ArrayList getDisps() {
        return dispArr;
    }

    public DisplayLine getDisplayLine(int index) {
        return dispArr.get(index);
    }

    /*adding display line to the CTC office displayline arraylist */
    public void addDisp(DisplayLine disp){
        dispArr.add(disp);
    }

    public SimulationEnvironment getSE() {
        return SEobj;
    }

    /*getting throughput */
    public int getThroughput(){
        return this.thruP;
    }
    /*setting throughput*/
    public void setThroughput(int test){
        thruP = test;
    }

    public WaysideSystem getWaysideSystem(String sectionName) throws Exception {
        WaysideSystem proxy = new WaysideSystem(sectionName);
        int ind =  waysides.indexOf(proxy);
        if (ind == -1)
            throw new Exception(String.format("CTC WaysideSystem search error: Searching for wayside named %s in waysides returned no result.\nWaysidesContains:\n%s\n",sectionName,waysides));
        return waysides.get(ind);
    }



    /*
            Elijah: PLC Scripts
     */
        //HERE TO JANK
        public void paintTrack(){
               System.out.println("PAINTING TRACK");

                for (int i=0; i<trackObj.getGreenLine().size(); i++){
                    if(route[i] == 1){
                        trackObj.getGreenLine().get(i).setAuthority(1);
                        trackObj.getGreenLine().get(i).setCommandedSpeed(15);
                    }
                    if(route[i] == 2){
                        trackObj.getGreenLine().get(i).setAuthority(888);
                        trackObj.getGreenLine().get(i).setCommandedSpeed(15);
                    }
                }

                for (int i=0; i<trackObj.getRedLine().size(); i++){
                    if(route[i] == 1){
                        trackObj.getRedLine().get(i).setAuthority(1);
                        trackObj.getRedLine().get(i).setCommandedSpeed(15);
                    }
                    if(route[i] == 2){
                        trackObj.getRedLine().get(i).setAuthority(888);
                        trackObj.getRedLine().get(i).setCommandedSpeed(15);
                    }
                }

        }


}