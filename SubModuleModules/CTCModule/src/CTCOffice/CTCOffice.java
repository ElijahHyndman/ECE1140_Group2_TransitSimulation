package CTCOffice;//Haleigh DeFoor
//CTC Office
//Skeleton Code
//import java.io.*;

import java.util.*;
import java.io.*;
import java.time.*;

public class CTCOffice
{
    private int thruP;
    private Object[] speedAuthority = new Object[3];
    private boolean status;
    //private String fileName;
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
    
    public Object[] Dispatch(String dest, String tNum, String timeD)
    {
        //speedAuthority[0] is speed
        //speedAuthority[1] is authority
        //calculate speed and authority from destination for a specific train
        
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
       
       int h1 = timeDis.getHour();
       int h2 = LocalTime.now().getHour();
       int m1 = timeDis.getMinute();
       int m2 = LocalTime.now().getMinute();
       
       
       double temp = m1-m2;
       temp = temp/60;
       int hsub = h1-h2;
       if (m1<m2)
       {
           hsub = hsub-1;
       }
       temp = temp+hsub;
       
       if(dest.equals("Station B"))
           routeLength = 10*50;
       else if(dest.equals("Station C"))
           routeLength = 10*50;
       else
           routeLength = 0;
       
       speed = routeLength/1000/temp;
       if (speed<30)
       {
            speed = 30;
            double timeTravel = 1/(speed*1000/routeLength/60);
            long mins = (long)timeTravel;
            timeDisp = timeDis.minusMinutes(mins);
       }
       else
       {
           timeDisp = LocalTime.now();
       }
               
       if(LocalTime.now().isBefore(timeDis) && speed<50)    
       {
           speedAuthority[0] = speed*0.621371;
           speedAuthority[1] = 10;
           speedAuthority[2] = timeDisp;
       }
       else
       {
           speedAuthority[0] = 0;
           speedAuthority[1] = 0;
           speedAuthority[2] = 0;
       }

       return speedAuthority;
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
        int hours = LocalTime.now().getHour();
        int mins = LocalTime.now().getMinute();
        double totalT = mins/60;
        totalT = totalT+hours;
        
        thruP = (int) Math.round(tix/totalT);
        return thruP;
    }

    public void DisplayTransit()
    {
        //displays transit system
    }

    public void DisplaySchedule()
    {
        //read input from files to create schedule display
    }

    public void DisplayStatus()
    {
        //read input from files to create status display
    }

    public void LoadData(String line, int blockNum)
    {
        //display a certain block's data from file
        
    }
    
    public boolean CheckOccupancy(String t1, String t2, String t3, String t4, String t5, String t6, String t7, String t8, String t9, String t10)
    {
        if (!t1.equals(null))
        {
            LocalTime nowT = LocalTime.now();
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
        }
        else
            occ = false;
        
        return occ;
    }
    public boolean CheckStatus(String line, int blockNum)
    {
        //checks maintenance status of certain block number on a line
        this.lineCol = line;
        this.blockNum = blockNum;

        return status;
    }

    public int OpenTrack(int blockNum)
    {
        //communicate the block number that dispatcher wants to open to wayside controller
        this.blockNum = blockNum;
        return blockNum;
    }

    public int CloseTrack(int blockNum)
    {
        //communicate the block number that dispatcher wants to close to wayside controller
        this.blockNum = blockNum;
        return blockNum;
    }
    
    public ArrayList getDisps()
    {
        return dispArr;
    }


}