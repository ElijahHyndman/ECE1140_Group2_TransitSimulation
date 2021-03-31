package CTCOffice;//Haleigh DeFoor
//CTC Office

import java.util.*;
import java.io.*;
import java.time.*;
import WaysideController.WaysideSystem;
import SimulationEnvironment.*;

import javax.sound.midi.Track;

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
    public CharSequence timeNow;
    private LocalTime now;
    public Track trackObj;

    public CTCOffice()
    {
        waysides = new WaysideSystem();
    }

    public Object[] Dispatch(String dest, String tNum, String timeD)
    {
        //speedAuthority[0] is speed
        //speedAuthority[1] is authority
        //speedAuthority[2] is dispatch time

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
            trainNum = 1;

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

        if (speed<30)
        {
            speed = 30;
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

        //waysides.broadcastToControllers(speedArrG, authArr);

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

    public int CalcThroughput(int tix)
    {
        //trackObj = new Track();
        //int tix = trackObj.
        now = LocalTime.parse(timeNow);
        int hours = now.getHour();
        int mins = now.getMinute();
        double totalT = mins/60;
        totalT = totalT+hours;

        thruP = (int) Math.round(tix/totalT);
        return thruP;
    }

    /*public boolean CheckOccupancy(String t1, String t2, String t3, String t4, String t5, String t6, String t7, String t8, String t9, String t10)
    {
            LocalTime nowT = LocalTime.parse(timeNow);
            LocalTime t12 = LocalTime.parse(t1);
            LocalTime t22 = LocalTime.parse(t2);
            LocalTime t32 = LocalTime.parse(t3);
            LocalTime t42 = LocalTime.parse(t4);
            LocalTime t52 = LocalTime.parse(t5);
            LocalTime t62 = LocalTime.parse(t6);
            LocalTime t72 = LocalTime.parse(t7);
            LocalTime t82 = LocalTime.parse(t8);
            LocalTime t92 = LocalTime.parse(t9);
            LocalTime t102 = LocalTime.parse(t10);

            int val1 = nowT.compareTo(t12);
            int val2 = nowT.compareTo(t22);
            int val3 = nowT.compareTo(t32);
            int val4 = nowT.compareTo(t42);
            int val5 = nowT.compareTo(t52);
            int val6 = nowT.compareTo(t62);
            int val7 = nowT.compareTo(t72);
            int val8 = nowT.compareTo(t82);
            int val9 = nowT.compareTo(t92);
            int val10 = nowT.compareTo(t102);

            if(val1==0 || val2==0 || val3==0 || val4==0 || val5==0 || val6==0 || val7==0 || val8==0 || val9==0 || val10==0)
                occ = true;
            else
                occ = false;

        return occ;
    }*/
    public boolean CheckOcc(int blockNum, String lineCol)
    {
       /*if (lineCol.equals("Green"))
            occ = waysideG.getOccupancy(blockNum);
        else
            occ = waysideR.getOccupancy(blockNum);*/
        //status = waysides.getOccupancy(blockNum);

        return occ;
    }

    public boolean CheckSwitch(int blockNum)
    {
        boolean switchstat=false;
        //switchstat = waysides.getSwitchStatus(blockNum);
        return switchstat;
    }

    public void OpenTrack(int blockNum, String lineCol)
    {
        //waysides.setOpen(blockNum);
        /*if (lineCol.equals("Green"))
        {
            for(int i=1; i<4; i++)
            {
                if (blockNum==i)
                {
                    for(int j=0; j<3; j++)
                        waysideG.setOpen(j);
                }
            }
            for(int i=4; i<7; i++)
            {
                if (blockNum==i)
                {
                    for (int j=3; j<6; j++)
                        waysideG.setOpen(j);
                }
            }
            for(int i=7; i<13; i++)
            {
                if(blockNum==i)
                {
                    for (int j = 6; j<12; j++)
                        waysideG.setOpen(j);
                }
            }
        }*/

    }

    public void CloseTrack(int blockNum, String lineCol)
    {
        //waysides.setOpen(blockNum);
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
        if(bn==73 && lc.equals("Green")){
            for (int i=63; i<75; i++){
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
        int a = auth;
        int[] aArr  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i=0; i<150; i++){
            if (rA[i]!=0) {
                for (int j = i; j < auth+i; j++) {
                    aArr[j] = a;
                    a--;
                }
                break;
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


}