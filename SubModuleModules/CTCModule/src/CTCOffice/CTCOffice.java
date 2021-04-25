package CTCOffice;//Haleigh DeFoor

import java.util.*;
import java.io.*;
import java.time.*;
import WaysideController.WaysideSystem;
import SimulationEnvironment.*;
import Track.Track;
import WorldClock.PhysicsUpdateListener;

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
    public ArrayList<WaysideSystem> waysides, waysideG, waysideR;
    private double[] speedArrG = new double[150];
    private double[] speedArrR = new double[150];
    private double[] route = new double[150];
    private int[] authArr = new int[150];
    public CharSequence timeNow = "07:09"; //TODO
    private LocalTime now;
    public Track trackObj;
    public SimulationEnvironment SEobj;
    public int[] positions = new int[10];
    public ArrayList<double[]> speedsR =new ArrayList<double[]>();
    public ArrayList<double[]> speedsG =new ArrayList<double[]>();
    public ArrayList<int[]> authorities = new ArrayList<int[]>();
    public ArrayList<LocalTime> times = new ArrayList<LocalTime>();
    public int[] greenPath= {62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58};

    public CTCOffice()
    {


        waysides = GenerateWaysideSystems(trackObj);

        SEobj = new SimulationEnvironment();
        trackObj = new Track();
        trackObj.importTrack("C:\\Users\\grhen\\OneDrive\\Documents\\RedGreenUpdated.csv");
    }

    public static ArrayList<WaysideSystem> GenerateWaysideSystems(Track trackSystem)
    {
        ArrayList<WaysideSystem> generatedWaysides = new ArrayList<WaysideSystem>();
        WaysideSystem greenWS = null;
        WaysideSystem redWS = null;

        if (trackSystem == null) {
            return new ArrayList<WaysideSystem>();
        }
        try {
            greenWS = new WaysideSystem("Green",trackSystem.getGreenLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            redWS = new WaysideSystem("Red", trackSystem.getRedLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        generatedWaysides.add(greenWS);
        generatedWaysides.add(redWS);

        return generatedWaysides;
    }

    public CTCOffice(Track SEtrack, SimulationEnvironment SE)
    {
        waysides = GenerateWaysideSystems(SEtrack);
        trackObj = SEtrack;
        SEobj = SE;
    }

    public ArrayList<WaysideSystem> getWaysideSystem()
    {
        return waysides;
    }

    public void setWaysideSystem(ArrayList<WaysideSystem> SEws)
    {
        waysides = SEws;
    }

    public Track getTrack()
    {
        return trackObj;
    }

    public void setTrack(Track SEt)
    {
        trackObj = SEt;
    }

    public Object[] Dispatch(String dest, String tNum, String timeD)
    {
        //speedAuthority[0] is speed
        //speedAuthority[1] is authority
        //speedAuthority[2] is dispatch time
        Vector<TrainUnit> trains = SEobj.getTrains();

        if (tNum.equals("Train 1"))
            trainNum = 1;
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
        else
            SEobj.spawnRunningTrain(trackObj.getBlock(0),trackObj.getBlock(9));

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

        route = calcRoute(blockNum, lineCol, trainNum);

        routeLength = calcRouteLength(blockNum, lineCol, trainNum);

        speed = routeLength/1000/temp;

        authority = calcAuthority(route);
        authArr = createAuthArr(route, authority);

        if (speed<5)
        {
            speed = 5;
            double timeTravel = 1/(speed*1000/routeLength/60);
            long mins = (long)timeTravel;
            timeDisp = timeDis.minusMinutes(mins);
        }
        else
        {
            timeDisp = now;
        }

        if (lineCol.equals("Green"))
            speedArrG = createSpeedArr(route, speed);
        else if (lineCol.equals("Red"))
            speedArrR = createSpeedArr(route, speed);

        if(LocalTime.now().isBefore(timeDis) && speed<50)
        {
            speedAuthorityTime[0] = speed*0.621371;
            speedAuthorityTime[1] = authority;
            speedAuthorityTime[2] = timeDisp;
        }
        else
        {
            speedAuthorityTime[0] = 0;
            speedAuthorityTime[1] = 0;
            speedAuthorityTime[2] = 0;
        }

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

        //TODO -- NEED TO UNCOMMENT

        try {
            // For now, Just get the greenline wayside system
            // TODO make this an if statement so we can call the right Wayside Controller instead of only green
            if (lineCol.equals("Green")) {
                waysides.get(0).broadcastToControllers(speedArrG, authArr);
            }
            else if (lineCol.equals("Red")){
                waysides.get(1).broadcastToControllers(speedArrR, authArr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //BroadcastingArrays();

        return speedAuthorityTime;
    }

    public void ClearQueues(){
        for (int i= 0; i<times.size(); i++){
            times.remove(i);
        }
        for (int i = 0; i<speedsR.size(); i++){
            speedsR.remove(i);
        }
        for (int i = 0; i<speedsG.size(); i++){
            speedsG.remove(i);
        }
        for (int i = 0; i<authorities.size(); i++){
            authorities.remove(i);
        }
    }

    public Object[] AutoDispatch(String dest, String tNum, String timeD)
    {
        //speedAuthority[0] is speed
        //speedAuthority[1] is authority
        //speedAuthority[2] is dispatch time
        Vector<TrainUnit> trains = SEobj.getTrains();

        if (tNum.equals("Train 1"))
            trainNum = 1;
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
        else
            SEobj.spawnRunningTrain(trackObj.getBlock(0),trackObj.getBlock(9));

        if (dest.equals("Station B")){
            blockNum = 10;
            lineCol = "Blue";}

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

        route = calcAutoRoute(blockNum, lineCol, trainNum);

        routeLength = calcAutoRouteLength(blockNum, lineCol, trainNum);

        speed = routeLength/1000/temp;

        authority = calcAuthority(route);
        authArr = createAuthArr(route, authority);

        if (speed<5)
        {
            speed = 5;
            double timeTravel = 1/(speed*1000/routeLength/60);
            long mins = (long)timeTravel;
            timeDisp = timeDis.minusMinutes(mins);
        }
        else
        {
            timeDisp = now;
        }

        if (lineCol.equals("Green"))
            speedArrG = createSpeedArr(route, speed);
        if (lineCol.equals("Red"))
            speedArrR = createSpeedArr(route, speed);

        if(LocalTime.now().isBefore(timeDis) && speed<50)
        {
            speedAuthorityTime[0] = speed*0.621371;
            speedAuthorityTime[1] = authority;
            speedAuthorityTime[2] = timeDisp;
        }
        else
        {
            speedAuthorityTime[0] = 0;
            speedAuthorityTime[1] = 0;
            speedAuthorityTime[2] = 0;
        }

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

        try {
            // For now, Just get the greenline wayside system
            // TODO make this an if statement so we can call the right Wayside Controller instead of only green
            if (lineCol.equals("Green")) {
                waysides.get(0).broadcastToControllers(speedArrG, authArr);
            }
            else if (lineCol.equals("Red")){
                waysides.get(1).broadcastToControllers(speedArrR, authArr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //BroadcastingArrays();

        return speedAuthorityTime;
    }

    /*
    public void BroadcastingArrays(){
        now = LocalTime.parse(timeNow);
        for (int i = 0; i<times.size(); i++){
            if(now.equals(times)){
                waysides.broadcastToControllers(speedsR, authorities);
                waysides.broadcastToControllers(speedsG, authorities);
                times.remove(i);
                speedsR.remove(i);
                speedsG.remove(i);
                authorities.remove(i);
            }
        }
    }*/

    public void LoadSchedule(String filename)
    {try{
        Scanner input = new Scanner(new File(filename));
        //read files to get input and read data to UI
        input.useDelimiter(",|\\n");
        int count = 0;
        while (input.hasNext())
        {

            if (count<19)
            {
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
    }catch(Exception e)
    {
        e.printStackTrace();
    }
    }

    public int CalcThroughput()
    {
        trackObj = new Track();
        int tix = trackObj.updateTickets();
        now = LocalTime.parse(timeNow);
        int hours = now.getHour();
        int mins = now.getMinute();
        double totalT = mins/60;
        totalT = totalT+hours;

        thruP = (int) Math.round(tix/totalT);
        return thruP;
    }

    public boolean CheckOcc(int blockNum, String lineCol)
    {

        try {
            for (WaysideSystem ws : waysides) {
                if(ws.getLine() == lineCol)  {
                    //If this is the WaysideSystem that controls the corresponding line
                    occ = ws.getOccupancy(blockNum);
                } else {
                    occ = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return occ;
    }

    public boolean CheckSwitch(int blockNum, String lineCol)
    {
        boolean switchstat=false;
        try {
            // For now, Just get the greenline wayside system
            // TODO make this an if statement so we can call the right Wayside Controller instead of only green
            switchstat = waysides.get(0).getSwitchStatus(blockNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return switchstat;
    }

    public void OpenTrack(int blockNum, String lineCol)
    {
        char section = dispArr.get(blockNum).getSection();
        char lineChar = lineCol.charAt(0);
        ArrayList<Integer> blocks = trackObj.blocksInSection(section,lineChar);
        int length = blocks.size();

        for (int i=0; i<length; i++){
            // For now, Just get the greenline wayside system
            // TODO make this an if statement so we can call the right Wayside Controller instead of only green
            try {
                waysides.get(0).setOpen(blocks.get(i));
            } catch (IOException e) {
                System.out.println("Failed to set open for block");
                e.printStackTrace();
            }
        }
    }

    public void CloseTrack(int blockNum, String lineCol)
    {
        char section = dispArr.get(blockNum).getSection();
        char lineChar = lineCol.charAt(0);
        ArrayList<Integer> blocks = trackObj.blocksInSection(section,lineChar);
        int length = blocks.size();

        for (int i=0; i<length; i++){
            // For now, Just get the greenline wayside system
            // TODO make this an if statement so we can call the right Wayside Controller instead of only green
            try {
                waysides.get(0).setClose(blocks.get(i));
            } catch (IOException e) {
                System.out.println("Failed to set closed for block");
                e.printStackTrace();
            }
        }
    }

    public int calcRouteLength(int bn, String lc, int tnum)
    {
        int rl;
        if(bn==10 && lc.equals("Blue"))
            rl = 10*50;
        else if(bn==15 && lc.equals("Blue"))
            rl = 10*50;
        else if (bn==65 && lc.equals("Green")){ //Glenbury
            if (positions[tnum-1]>=65 && positions[tnum-1]<114)
            {
                rl = 9554;
            }
            else {
                rl = 400;
            }
        }
        else if(bn==73 && lc.equals("Green")){ //Dormont
            if (positions[tnum-1]>=73 && positions[tnum-1]<105)
            {
                rl = 8602;
            }
            else {
                rl = 1300;
            }
        }
        else if (bn==77 && lc.equals("Green")){ //Mt Lebanon
            if (positions[tnum-1] == 88 || positions[tnum-1] == 96)
            {
                rl = 8187;
            }
            else {
                rl = 1900;
            }
        }
        else if(bn==88 && lc.equals("Green")){//Poplar
            rl = 4587;
        }
        else if(bn==96 && lc.equals("Green")){//Castle Shannon
            rl = 5187;
        }
        else if(bn==57 && lc.equals("Green")){//Overbrook
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<123)) {
                rl = 19803;
            }
            else{
                rl = 10094;
            }
        }
        else if(bn==48 && lc.equals("Green"))//Inglewood
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<132)) {
                rl = 19353;
            }
            else{
                rl = 10544;
            }
        }
        else if(bn==39 && lc.equals("Green"))//Central
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<141)) {
                rl = 18903;
            }
            else{
                rl = 10994;
            }
        }
        else if(bn==31 && lc.equals("Green")){//South Bank
            rl = 18503;
        }
        else if(bn==22 && lc.equals("Green")){//Whited
            if (positions[tnum-1]<22)
            {
                rl = 17353;
            }
            else{
                rl= 12853;
            }
        }
        else if (bn==16 && lc.equals("Green")){ //Station
            if (positions[tnum-1]<16)
            {
                rl = 16153;
            }
            else{
                rl = 13903;
            }
        }
        else if (bn==9 && lc.equals("Green")){//Edgebrook
            rl = 14753;
        }
        else if (bn==2 && lc.equals("Green")) {//Pioneer
            rl = 15453;
        }
        if (bn==7 && lc.equals("Red")){//Shadyside
            rl = 225-50;
        }
        if(bn==16 && lc.equals("Red")){//Herron Ave
            rl= 350+225-50;
        }
        if (bn==21 && lc.equals("Red")) {//Swissville
            rl = 1300+350+225-50;
        }
        if (bn == 25 && lc.equals("Red")){//Penn Station
            rl = 300+1300+350+225-50;
        }
        if (bn==35 && lc.equals("Red")) {//Steel Plaza
            rl = 520+300+1300+350+225-50;
        }
        if (bn==45 && lc.equals("Red")) {//First Ave
            rl = 520+520+300+1300+350+225-50;
        }
        if (bn==48 && lc.equals("Red")) {//Station Square
            rl = 225+520+520+300+1300+350+225-50;
        }
        if (bn==60 &&lc.equals("Red")) {//South Hills Junction
            rl = 743+225+520+520+300+1300+350+225-50;
        }
        else
            rl = 0;

        rl+=50;
        return rl;
    }

    /*GIH6 adding back to yard*/
    public int[] backToYard(int bn){
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
        for(int i=0; i < 176; i++)
            if(newGreenLine[i]!=0)
                RouteAr[newGreenLine[i]-1] = 1;
        return RouteAr;
    }

    public int calcAutoRouteLength(int bn, String lc, int tnum)
    {
        int rl;
        if(bn==10 && lc.equals("Blue"))
            rl = 10*50;
        else if(bn==15 && lc.equals("Blue"))
            rl = 10*50;
        else if (bn==65 && lc.equals("Green")){ //Glenbury
            if (positions[tnum-1]>=65 && positions[tnum-1]<114)
            {
                rl = 890+162;
            }
            else {
                rl = 450;
            }
        }
        else if(bn==73 && lc.equals("Green")){ //Dormont
            if (positions[tnum-1]>=73 && positions[tnum-1]<105)
            {
                rl = (75*4) + 35 + 380 + (300*9);
            }
            else {
                rl = 900;
            }
        }
        else if (bn==77 && lc.equals("Green")){ //Mt Lebanon
            if (positions[tnum-1] == 88 || positions[tnum-1] == 96)
            {
                rl = (75*4) + (300*9);
            }
            else {
                rl = 600;
            }
        }
        else if(bn==88 && lc.equals("Green")){//Poplar
            rl = (300*8) + 200 + 87;
        }
        else if(bn==96 && lc.equals("Green")){//Castle Shannon
            rl = 75*8;
        }
        else if(bn==57 && lc.equals("Green")){//Overbrook
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<123)) {
                rl = 9*50;
            }
            else{
                rl = 540;
            }
        }
        else if(bn==48 && lc.equals("Green"))//Inglewood
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<132)) {
                rl = 450;
            }
            else{
                rl = 450;
            }
        }
        else if(bn==39 && lc.equals("Green"))//Central
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<141)) {
                rl = 450;
            }
            else{
                rl = 450;
            }
        }
        else if(bn==31 && lc.equals("Green")){//South Bank
            rl = 1150;
        }
        else if(bn==22 && lc.equals("Green")){//Whited
            if (positions[tnum-1]<22)
            {
                rl = (150*4) + 600;
            }
            else{
                rl= 484+40+35+1300;
            }
        }
        else if (bn==16 && lc.equals("Green")){ //Station
            if (positions[tnum-1]<16)
            {
                rl = (4*150)*100;
            }
            else{
                rl = 1050;
            }
        }
        else if (bn==9 && lc.equals("Green")){//Edgebrook
            rl = 850;
        }
        else if (bn==2 && lc.equals("Green")) {//Pioneer
            rl = 700;
        }
        else
            rl = 0;

        //Start RED LINE

        if (bn==7 && lc.equals("Red")){//Shadyside
            rl = 225;
        }
        if(bn==16 && lc.equals("Red")){//Herron Ave
            rl= 350;
        }
        if (bn==21 && lc.equals("Red")) {//Swissville
            rl = 1300;
        }
        if (bn == 25 && lc.equals("Red")){//Penn Station
            rl = 300;
        }
        if (bn==35 && lc.equals("Red")) {//Steel Plaza
            rl = 520;
        }
        if (bn==45 && lc.equals("Red")) {//First Ave
            rl = 520;
        }
        if (bn==48 && lc.equals("Red")) {//Station Square
            rl = 225;
        }
        if (bn==60 &&lc.equals("Red")) {//South Hills Junction
            rl = 743;
        }

        return rl;
    }

    public double[] calcAutoRoute(int bn, String lc, int tnum)
    {
        double[] routeArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        if (bn==65 && lc.equals("Green")){ // Yard - Glenbury
            if (positions[tnum-1]>=65 && positions[tnum-1]<114)
            {
                for(int i=104; i<114; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[111] = 2;
                positions[tnum-1] = 114;
            }
            else {
                for (int i = 61; i < 65; i++) {
                    routeArr[i] = 1;
                }
                routeArr[62] = 2;
                positions[tnum-1] = 65;
            }
        }
        if(bn==73 && lc.equals("Green")){ //GLEN - Dormont
            if (positions[tnum-1]>=73 && positions[tnum-1]<105)
            {
                for(int i=95; i<105; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i=76; i<85; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[102] = 2;
                positions[tnum-1] = 105;
            }
            else {
                for (int i = 64; i < 73; i++) {
                    routeArr[i] = 1;
                }
                routeArr[70] = 2;
                positions[tnum-1] = 73;
            }
        }
        if (bn==77 && lc.equals("Green")){ //Mt Lebanon
            if (positions[tnum-1] == 88 || positions[tnum-1] == 96)
            {
                for (int i=95; i<100; i++)
                {
                    routeArr[i] = 1;
                }
                for(int i=84; i>=76; i--)
                {
                    routeArr[i] = 1;
                }
                routeArr[78] = 2;
            }
            else {
                for (int i = 72; i < 77; i++) {
                    routeArr[i] = 1;
                }
                routeArr[74] = 2;
            }
            positions[tnum-1] = 77;
        }
        if(bn==88 && lc.equals("Green")){//Poplar
            for (int i=76; i<88; i++){
                routeArr[i] = 1;
            }
            routeArr[85] = 2;
            positions[tnum-1] = 88;
        }
        if(bn==96 && lc.equals("Green")){//Castle Shannon
            for (int i=87; i<96; i++){
                routeArr[i] = 1;
            }
            routeArr[93] = 2;
            positions[tnum-1] = 96;
        }
        if(bn==57 && lc.equals("Green")){//Overbrook
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<123)) {
                for (int i = 47; i<57; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[54] = 2;
                positions[tnum-1] = 57;
            }
            else{
                for (int i = 113; i < 123; i++) {
                    routeArr[i] = 1;
                }
                routeArr[120] = 2;
                positions[tnum-1] = 123;
            }
        }
        if(bn==48 && lc.equals("Green"))//Inglewood
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<132)) {
                for (int i=38; i<48; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[45] = 2;
                positions[tnum-1] = 48;
            }
            else{
                for (int i=122; i<132; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[129] = 2;
                positions[tnum-1] = 132;
            }
        }
        if(bn==39 && lc.equals("Green"))//Central
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<141)) {
                for (int i=30; i<39; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[36] = 2;
                positions[tnum-1] = 39;
            }
            else{
                for (int i=131; i<141; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[138] = 2;
                positions[tnum-1] = 141;
            }
        }
        if(bn==31 && lc.equals("Green")){//South Bank
            for (int i = 21; i<31; i++)
            {
                routeArr[i] = 1;
            }
            routeArr[28] = 2;
            positions[tnum-1] = 31;
        }
        if(bn==22 && lc.equals("Green")){//Whited
            if (positions[tnum-1]<22)
            {
                for (int i = 15; i<22; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[19] = 2;
            }
            else{
                for (int i = 140; i <150; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i = 27; i>=21; i--)
                {
                    routeArr[i] = 1;
                }
                routeArr[23] = 2;
            }
            positions[tnum-1] = 22;
        }
        if (bn==16 && lc.equals("Green")){ //Station
            if (positions[tnum-1]<16)
            {
                for (int i=1; i>=0; i--){
                    routeArr[i] = 1;
                }
                for (int i = 12; i<16; i++){
                    routeArr[i] = 1;
                }
                routeArr[13] = 2;
            }
            else{
                for (int i = 21; i>=15; i--)
                {
                    routeArr[i] = 1;
                }
                routeArr[17] = 2;
            }
            positions[tnum-1] = 16;
        }
        if (bn==9 && lc.equals("Green")){//Edgebrook
            for (int i = 15; i>=8; i--)
            {
                routeArr[i] = 1;
            }
            routeArr[10] = 2;
            positions[tnum-1] = 9;
        }
        if (bn==2 && lc.equals("Green")) {//Pioneer
            for (int i = 8; i>=1; i--){
                routeArr[i] = 1;
            }
            routeArr[3] = 2;
            positions[tnum-1] = 2;
        }

        //Start RED LINE

        if (bn==7 && lc.equals("Red")){//Shadyside
            for (int i = 6; i<9; i++){
                routeArr[i] = 1;
            }
            routeArr[7] = 2;
            positions[tnum-1] = 9;
        }
        if(bn==16 && lc.equals("Red")){//Herron Ave
            for (int i = 0; i < 7; i++){
                routeArr[i] = 1;
            }
            routeArr[15] = 1;

            routeArr[14] = 2;
            positions[tnum-1] = 16;
        }
        if (bn==21 && lc.equals("Red")) {//Swissville
            for (int i = 15; i<21; i++)
            {
                routeArr[i]= 1;
            }
            routeArr[19] = 2;
            positions[tnum-1] = 21;
        }
        if (bn == 25 && lc.equals("Red")){//Penn Station
            for (int i = 20; i<25; i++){
                routeArr[i] = 1;
            }
            routeArr[23] = 2;
            positions[tnum-1] = 25;
        }
        if (bn==35 && lc.equals("Red")) {//Steel Plaza
            for (int i = 24; i<35; i++){
                routeArr[i] = 1;
            }
            routeArr[33] = 2;
            positions[tnum-1] = 35;
        }
        if (bn==45 && lc.equals("Red")) {//First Ave
            for (int i = 34; i<45; i++){
                routeArr[i] = 1;
            }
            routeArr[43]= 2;
            positions[tnum-1] = 45;
        }
        if (bn==48 && lc.equals("Red")) {//Station Square
            for (int i = 44; i<48; i++){
                routeArr[i] = 1;
            }
            routeArr[46] = 2;
            positions[tnum-1] = 48;
        }
        if (bn==60 &&lc.equals("Red")) {//South Hills Junction
            for (int i = 47; i<60; i++){
                routeArr[i] = 1;
            }
            routeArr[58] = 2;
            positions[tnum-1] = 60;
        }

        return routeArr;
    }

    public double[] calcRoute(int bn, String lc, int tnum)
    {
        double[] routeArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        if (bn==65 && lc.equals("Green")){ //Glenbury
            if (positions[tnum-1]>=65 && positions[tnum-1]<114)
            {
                for(int i=61; i<114; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[111] = 2;
                positions[tnum-1] = 114;
            }
            else {
                for (int i = 61; i < 65; i++) {
                    routeArr[i] = 1;
                }
                routeArr[62] = 2;
                positions[tnum-1] = 65;
            }
        }
        if(bn==73 && lc.equals("Green")){ //Dormont
            if (positions[tnum-1]>=73 && positions[tnum-1]<105)
            {
                //for(int i=95; i<105; i++)
                //{
                //    routeArr[i] = 1;
                //}
                //for (int i=76; i<85; i++)
                for(int i=61; i<105; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[102] = 2;
                positions[tnum-1] = 105;
            }
            else {
                //for (int i = 64; i < 73; i++) {
                for (int i = 61; i < 73; i++) {
                    routeArr[i] = 1;
                }
                routeArr[70] = 2;
                positions[tnum-1] = 73;
            }
        }
        if (bn==77 && lc.equals("Green")){ //Mt Lebanon
            if (positions[tnum-1] == 88 || positions[tnum-1] == 96)
            {
                //for (int i=95; i<100; i++)
                //{
                //    routeArr[i] = 1;
                //}
                //for(int i=84; i>=76; i--)
                for (int i=61; i<100; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[78] = 2;
            }
            else {
                //for (int i = 72; i < 77; i++) {
                for (int i = 61; i < 77; i++) {
                    routeArr[i] = 1;
                }
                routeArr[74] = 2;
            }
            positions[tnum-1] = 77;
        }
        if(bn==88 && lc.equals("Green")){//Poplar
            //for (int i=76; i<88; i++){
            for (int i=61; i<88; i++){
                routeArr[i] = 1;
            }
            routeArr[85] = 2;
            positions[tnum-1] = 88;
        }
        if(bn==96 && lc.equals("Green")){//Castle Shannon
            //for (int i=87; i<96; i++){
            for (int i=61; i<96; i++){
                routeArr[i] = 1;
            }
            routeArr[93] = 2;
            positions[tnum-1] = 96;
        }
        if(bn==57 && lc.equals("Green")){//Overbrook
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<123)) {
                //for (int i = 47; i<57; i++)
                for (int i = 0; i<57; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i = 61; i<150; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[54] = 2;
                positions[tnum-1] = 57;
            }
            else{
                //for (int i = 113; i < 123; i++) {
                for (int i = 61; i < 123; i++) {
                    routeArr[i] = 1;
                }
                routeArr[120] = 2;
                positions[tnum-1] = 123;
            }
        }
        if(bn==48 && lc.equals("Green"))//Inglewood
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<132)) {
                //for (int i=38; i<48; i++)
                for (int i=0; i<48; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i = 61; i<150; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[45] = 2;
                positions[tnum-1] = 48;
            }
            else{
                //for (int i=122; i<132; i++)
                for (int i=61; i<132; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[129] = 2;
                positions[tnum-1] = 132;
            }
        }
        if(bn==39 && lc.equals("Green"))//Central
        {
            if(!(positions[tnum-1]>=62 && positions[tnum-1]<141)) {
               // for (int i=30; i<39; i++)
               // {
               //     routeArr[i] = 1;
               // }
                for (int i=0; i<39; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i = 61; i<150; i++){
                    routeArr[i] = 1;
                }
                routeArr[36] = 2;
                positions[tnum-1] = 39;
            }
            else{
                //for (int i=131; i<141; i++)
                for (int i=61; i<141; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[138] = 2;
                positions[tnum-1] = 141;
            }
        }
        if(bn==31 && lc.equals("Green")){//South Bank
            //for (int i = 21; i<31; i++)
            //{
            //    routeArr[i] = 1;
            //}
            for (int i = 0; i<31; i++)
            {
                routeArr[i] = 1;
            }
            for (int i = 61; i<150; i++){
                routeArr[i] = 1;
            }
            routeArr[28] = 2;
            positions[tnum-1] = 31;
        }
        if(bn==22 && lc.equals("Green")){//Whited
            if (positions[tnum-1]<22)
            {
           //     for (int i = 15; i<22; i++)
           //     {
           //         routeArr[i] = 1;
           //     }
           //     routeArr[19] = 2;
           // }
           // else{
           //     for (int i = 140; i <150; i++)
                for (int i = 0; i<28; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i = 61; i<150; i++){
                    routeArr[i] = 1;
                }
                routeArr[19] = 2;
            }
            else{
                for (int i = 61; i <150; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i = 27; i>=21; i--)
                {
                    routeArr[i] = 1;
                }
                routeArr[23] = 2;
            }
            positions[tnum-1] = 22;
        }
        if (bn==16 && lc.equals("Green")){ //Station
            if (positions[tnum-1]<16)
            {
                //for (int i=1; i>=0; i--){
                //    routeArr[i] = 1;
               // }
                //for (int i = 12; i<16; i++){
                for (int i=61; i<150; i++){
                    routeArr[i] = 1;
                }
                for (int i = 0; i<28; i++){
                    routeArr[i] = 1;
                }
                routeArr[13] = 2;
            }
            else{
                //for (int i = 21; i>=15; i--)
                for (int i = 61; i<150; i++)
                {
                    routeArr[i] = 1;
                }
                for (int i = 15; i<28; i++)
                {
                    routeArr[i] = 1;
                }
                routeArr[17] = 2;
            }
            positions[tnum-1] = 16;
        }
        if (bn==9 && lc.equals("Green")){//Edgebrook
        //    for (int i = 15; i>=8; i--)
        //    {
        //        routeArr[i] = 1;
        //    }
        //    routeArr[10] = 2;
        //    positions[tnum-1] = 9;
        //}
        //if (bn==2 && lc.equals("Green")) {//Pioneer
        //    for (int i = 8; i>=1; i--){
            for (int i = 8; i<28; i++)
            {
                routeArr[i] = 1;
            }
            for (int i = 61; i<150; i++){
                routeArr[i] = 1;
            }
            routeArr[10] = 2;
            positions[tnum-1] = 9;
        }
        if (bn==2 && lc.equals("Green")) {//Pioneer
            for (int i = 1; i<28; i++){
                routeArr[i] = 1;
            }
            for (int i = 61; i<150; i++){
                routeArr[i] = 1;
            }
            routeArr[3] = 2;
            positions[tnum-1] = 2;
        }

        //Start RED LINE

        if (bn==7 && lc.equals("Red")){//Shadyside
            for (int i = 6; i<9; i++){
                routeArr[i] = 1;
            }
            routeArr[7] = 2;
            positions[tnum-1] = 9;
        }
        if(bn==16 && lc.equals("Red")){//Herron Ave
            for (int i = 0; i < 9; i++){
                routeArr[i] = 1;
            }
            routeArr[15] = 1;

            routeArr[14] = 2;
            positions[tnum-1] = 16;
        }
        if (bn==21 && lc.equals("Red")) {//Swissville
            for (int i = 0; i < 9; i++){
                routeArr[i] = 1;
            }
            for (int i = 15; i<21; i++)
            {
                routeArr[i]= 1;
            }
            routeArr[19] = 2;
            positions[tnum-1] = 21;
        }
        if (bn == 25 && lc.equals("Red")){//Penn Station
            for (int i = 0; i < 9; i++){
                routeArr[i] = 1;
            }
            for (int i = 15; i<25; i++){
                routeArr[i] = 1;
            }
            routeArr[23] = 2;
            positions[tnum-1] = 25;
        }
        if (bn==35 && lc.equals("Red")) {//Steel Plaza
            for (int i = 0; i < 9; i++){
                routeArr[i] = 1;
            }
            for (int i = 15; i<35; i++){
                routeArr[i] = 1;
            }
            routeArr[33] = 2;
            positions[tnum-1] = 35;
        }
        if (bn==45 && lc.equals("Red")) {//First Ave
            for (int i = 0; i < 9; i++){
                routeArr[i] = 1;
            }
            for (int i = 15; i<45; i++){
                routeArr[i] = 1;
            }
            routeArr[43]= 2;
            positions[tnum-1] = 45;
        }
        if (bn==48 && lc.equals("Red")) {//Station Square
            for (int i = 0; i < 9; i++){
                routeArr[i] = 1;
            }
            for (int i = 15; i<48; i++){
                routeArr[i] = 1;
            }
            routeArr[46] = 2;
            positions[tnum-1] = 48;
        }
        if (bn==60 &&lc.equals("Red")) {//South Hills Junction
            for (int i = 0; i < 9; i++){
                routeArr[i] = 1;
            }
            for (int i = 15; i<60; i++){
                routeArr[i] = 1;
            }
            routeArr[58] = 2;
            positions[tnum-1] = 60;
        }

        return routeArr;
    }

    public double[] createSpeedArr(double[] rA, double sp)
    {
        double[] sArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i=0; i<150; i++)
        {
            if(rA[i] != 0)
                sArr[i] = sp;
        }

        return sArr;
    }

    public int calcAuthority(double[] routeArr)
    {
        int count=0;
        for (int i=0; i<150; i++){
            if(routeArr[i]!=0)
                count++;
        }
        return count;
    }

    public int[] createAuthArr(double[] rA, int auth)
    {
        int[] aArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i=0; i<150; i++){
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

    public int getTickets()
    {
        int tix = 0;
        //tix = Track.updateTix();
        return tix;
    }

    public void updatePhysics(String currentTimeString, double deltaTime_inSeconds)
    {
        this.timeNow = currentTimeString;
    }

    public ArrayList getDisps()
    {
        return dispArr;
    }

    public DisplayLine getDisplayLine(int index)
    {
        return dispArr.get(index);
    }

    /*adding display line to the CTC office displayline arraylist */
    public void addDisp(DisplayLine disp){
        dispArr.add(disp);
    }

    public SimulationEnvironment getSE()
    {
        return SEobj;
    }

    /*getting throuput */
    public int getThroughput(){
        return this.thruP;
    }
    /*setting throughput*/
    public void setThroughput(int test){
        thruP = test;
    }

}