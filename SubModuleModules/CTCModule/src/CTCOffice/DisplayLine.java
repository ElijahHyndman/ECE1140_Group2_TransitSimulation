package CTCOffice;//Haleigh DeFoor
//CTCOffice.CTCOffice.DisplayLine
//iteration 2

import SimulationEnvironment.SimulationEnvironment;
import Track.Track;
import WaysideController.WaysideSystem;

import java.io.*;
import java.util.*;

public class DisplayLine extends CTCOffice
{
    int throughput;
    int blockNumber;
    String lineColor;
    char section;
    int length;
    double grade;
    int speedLimit;
    double elevation;
    double cElevation;
    String infrastructure;
    String t1Time, t2Time, t3Time, t4Time, t5Time, t6Time, t7Time, t8Time, t9Time, t10Time;
    boolean occupancy;
    boolean status;

    public DisplayLine(Track SEtrack, SimulationEnvironment SE)
    {
        trackObj = SEtrack;
        try {
            waysides = new WaysideSystem(trackObj.getGreenLine(),"Green Line");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SEobj = SE;
    }

    public void copy(DisplayLine given) {
        this.trackObj = given.getTrack();
        this.waysides = given.waysides;
        this.SEobj = given.SEobj;
    }

    public DisplayLine()
    {
        this.throughput = 0;
        this.blockNumber = 0;
        this.lineColor = null;
        this.section = '-';
        this.length = 0;
        this.grade = 0;
        this.speedLimit = 0;
        this.elevation = 0;
        this.cElevation = 0;
        this.infrastructure = null;
        this.t1Time = this.t2Time = this.t3Time = this.t4Time = this.t5Time = null;
        this.t6Time = this.t7Time = this.t8Time = this.t9Time = this.t10Time = null;
        this.occupancy = false;
        this.status = true;
    }

    public DisplayLine(Track SEtrack, SimulationEnvironment SE)
    {
        trackObj = SEtrack;
        try {
            waysides = new WaysideSystem(trackObj.getGreenLine(),"Green Line");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SEobj = SE;
    }


    public DisplayLine(int bn, String lc, char s, int l, int sl, double g, double e, double ce, String i, String t1t, String t2t, String t3t, String t4t, String t5t, String t6t, String t7t, String t8t, String t9t, String t10t)
    {
        this.throughput = 0;
        this.blockNumber = bn;
        this.lineColor = lc;
        this.section = s;
        this.length = l;
        this.grade = g;
        this.speedLimit = sl;
        this.elevation = e;
        this.cElevation = ce;
        this.infrastructure = i;
        this.t1Time = t1t;
        this.t2Time = t2t;
        this.t3Time = t3t;
        this.t4Time = t4t;
        this.t5Time = t5t;
        this.t6Time = t6t;
        this.t7Time = t7t;
        this.t8Time = t8t;
        this.t9Time = t9t;
        this.t10Time = t10t;
        this.occupancy = false;
        this.status = true;
    }
    
    public boolean getStatus()
    {
        return this.status;
    }
    
    public void setStatus(boolean boo)
    {
        this.status = boo;
    }

    public boolean getOccupancy()
    {
        return this.occupancy;
    }
    
    public void setOccupancy(boolean boo)
    {
        this.occupancy = boo;
    }
    
    public int getBlockNumber()
    { 
        return this.blockNumber; 
    }

    public void setBlockNumber(int blockNum)
    {
        this.blockNumber = blockNum;
    }

    public String getLine()
    {
        return this.lineColor;
    }

    public void setLine(String color)
    {
        this.lineColor = color;
    }

    public char getSection()
    {
        return this.section;
    }
    
    public void setSection(char sect)
    {
        this.section = sect;
    }

    public int getLength()
    {
        return this.length;
    }

    public void setLength(int l)
    {
        this.length = l;
    }

    public double getGrade()
    {
        return this.grade;
    }

    public void setGrade(double gr)
    {
        this.grade = gr;
    }

    public int getSpeedLimit()
    { 
        return this.speedLimit; 
    }

    public void setSpeedLimit(int sl)
    {
        this.speedLimit = sl;
    }

    public double getElevation()
    {
        return this.elevation;
    }

    public void setElevation(double elev)
    {
        this.elevation = elev;
    }

    public double getCElevation()
    {
        return this.cElevation;
    }

    public void setCElevation(double ce)
    {
        this.cElevation = ce;
    }

    public String getInfrastructure()
    {
        return this.infrastructure;
    }

    public void setInfrastructure(String in)
    {
        this.infrastructure = in;
    }

    public String getT1()
    {
        return this.t1Time;
    }

    public void setT1(String t)
    {
        this.t1Time = t;
    }

    public String getT2()
    {
        return this.t2Time;
    }

    public void setT2(String t)
    {
        this.t2Time = t;
    }

    public String getT3()
    {
        return this.t3Time;
    }

    public void setT3(String t)
    {
        this.t3Time = t;
    }

    public String getT4()
    {
        return this.t4Time;
    }

    public void setT4(String t)
    {
        this.t4Time = t;
    }

    public String getT5()
    {
        return this.t5Time;
    }

    public void setT5(String t)
    {
        this.t5Time = t;
    }

    public String getT6()
    {
        return this.t6Time;
    }

    public void setT6(String t)
    {
        this.t6Time = t;
    }

    public String getT7()
    {
        return this.t7Time;
    }

    public void setT7(String t)
    {
        this.t7Time = t;
    }

    public String getT8()
    {
        return this.t8Time;
    }

    public void setT8(String t)
    {
        this.t8Time = t;
    }

    public String getT9()
    {
        return this.t9Time;
    }

    public void setT9(String t)
    {
        this.t9Time = t;
    }

    public String getT10()
    {
        return this.t10Time;
    }

    public void setT10(String t)
    {
        this.t10Time = t;
    }
    
    public int getThroughput()
    {
        return this.throughput;
    }
    
    public void setThroughput(int thru)
    {
        this.throughput = thru;
    }

    public String toString() 
    {
        return String.format("Line: "+ lineColor +"\n Section: "+ section + "\n Block Number: "+ blockNumber+ "\n Block Length: " + length + "\n Block Grade: " + grade + "\n Speed Limit: " + speedLimit + "\n Infrastructure: " + infrastructure + "\n Elevation: " + elevation + "\n Cumulative Elevation: " + cElevation + "\n Train 1 Arrival Time: " + t1Time + "\n Train 2 Arrival Time: "+ t2Time + " \n Train 3 Arrival Time: " + t3Time + " \n Train 4 Arrival Time: " + t4Time + " \n Train 5 Arrival Time: " + t5Time + "\n Train 6 Arrival Time: " + t6Time + " \n Train 7 Arrival Time: " + t7Time + " \n Train 8 Arrival Time: " + t8Time + " \n Train 9 Arrival Time: " + t9Time + " \n Train 10 Arrival Time: " + t10Time);
    }
}