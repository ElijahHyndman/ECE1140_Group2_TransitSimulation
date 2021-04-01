package CTCOffice;//Haleigh DeFoor
//CTC Office

import java.util.*;
import java.io.*;
import java.time.*;

import TrackConstruction.TrackElement;
import WaysideController.WaysideSystem;
import SimulationEnvironment.*;
import Track.Track;

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
    public WaysideSystem waysides, waysideG, waysideR;
    private double[] speedArrG = new double[150];
    private double[] speedArrR = new double[150];
    private double[] route = new double[150];
    private int[] authArr = new int[150];
    public CharSequence timeNow = "00:00:00";
    private LocalTime now;
    public Track trackObj;
    public SimulationEnvironment SEobj;

    public CTCOffice()
    {
        try {
            waysides = new WaysideSystem(new Track().getBlocks(), "Green Line");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SEobj = null;
        trackObj = null;
    }

    public CTCOffice(Track SEtrack, SimulationEnvironment SE)
    {
        trackObj = SEtrack;
        try {
            waysides = new WaysideSystem(trackObj.getGreenLine(),"Green Line");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SEobj = SE;
    }

    public WaysideSystem getWaysideSystem()
    {
        return waysides;
    }

    public void setWaysideSystem(WaysideSystem SEws)
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
        try {
            waysides = new WaysideSystem(trackObj.getGreenLine(), "Green Line");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            SEobj.spawnRunningTrain(trackObj.getGreenLine().get(0),trackObj.getGreenLine().get(0));

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

        route = calcRoute(blockNum, lineCol);

        routeLength = calcRouteLength(blockNum, lineCol);

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

        try {
            waysides.broadcastToControllers(speedArrG, authArr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return speedAuthorityTime;
    }

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
       /*if (lineCol.equals("Green"))
            occ = waysideG.getOccupancy(blockNum);
        else
            occ = waysideR.getOccupancy(blockNum);*/
        try {
            occ = waysides.getOccupancy(blockNum);//blockNum,lineCol));//blockNum, lineCol);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return occ;
    }

    public boolean CheckSwitch(int blockNum, String lineCol)
    {
        boolean switchstat=false;
        try {
            switchstat = waysides.getSwitchStatus(blockNum);//findBlockObject(blockNum,lineCol));//blockNum, lineCol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return switchstat;
    }

    public void OpenTrack(int blockNum, String lineCol)
    {
        char section = dispArr.get(blockNum).getSection();
        ArrayList<Integer> blocks = trackObj.blocksInSection(section);
        int length = blocks.size();

        for (int i=0; i<length; i++){
            try {
                waysides.openClose(blocks.get(i));//findBlockObject(blocks.get(i),lineCol));//blocks.get(i), lineCol);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void CloseTrack(int blockNum, String lineCol)
    {
        char section = dispArr.get(blockNum).getSection();
        ArrayList<Integer> blocks = trackObj.blocksInSection(section);
        int length = blocks.size();

        for (int i=0; i<length; i++){
            try {
                waysides.setClose(blocks.get(i));//findBlockObject(blocks.get(i),lineCol));//
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int calcRouteLength(int bn, String lc)
    {
        int rl;
        if(bn==10 && lc.equals("Blue"))
            rl = 10*50;
        else if(bn==15 && lc.equals("Blue"))
            rl = 10*50;
        else if(bn==73 && lc.equals("Green"))
            rl = 50+200+400+700;
        else
            rl = 0;
        return rl;
    }

    public double[] calcRoute(int bn, String lc)
    {


        double[] routeArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        // Always add yard to route
        // TODO this won't be the case when we don't dispatch non-new train
        routeArr[0] = 1;
        if(bn==73 && lc.equals("Green")){
            for (int i=62; i<75; i++){
                routeArr[i] = 1;
            }
        }
        if (bn==65 && lc.equals("Green")){
            for (int i=62; i<67; i++){
                routeArr[i] = 1;
            }
        }
        if (bn==77 && lc.equals("Green")){
            for (int i=62; i<79; i++){
                routeArr[i] = 1;
            }
        }
        if(bn==88 && lc.equals("Green")){
            for (int i=62; i<90; i++){
                routeArr[i] = 1;
            }
        }
        if(bn==96 && lc.equals("Green")){
            for (int i=62; i<98; i++){
                routeArr[i] = 1;
            }
        }
        if(bn==123 && lc.equals("Green")){
            for (int i=62; i<125; i++){
                routeArr[i] = 1;
            }
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
            if (rA[i]!=0) {
                /*for (int j = i; j < auth+i; j++) {
                    aArr[j] = a;
                    a--;
                }
                break;*/
                aArr[i] = auth;
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

    private TrackElement findBlockObject(int blockIndex, String fromLine) {
        ArrayList<TrackElement> allBlocks = trackObj.getBlocks();
        // Search for block in array list
        for (TrackElement block : allBlocks ) {
            if(block.getBlockNum() == blockIndex && block.getLine() == fromLine)
                return block;
        }
        // Was not found in array list
        return null;
    }


    public Vector<WaysideSystem> getWaysideSystems() {
        Vector<WaysideSystem> ws = new Vector<WaysideSystem>();
        ws.add(waysides);
        return ws;
    }
}