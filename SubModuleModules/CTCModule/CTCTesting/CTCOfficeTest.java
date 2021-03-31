import CTCOffice.DisplayLine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;

class CTCOfficeTest {

    @org.junit.jupiter.api.Test
    void dispatch() {
        DisplayLine instance = new DisplayLine();
        System.out.println("Dispatch");
        String dest = "Station B";
        String tNum = "Train 1";
        String timeD = "23:30";

        String expDispatchTime = "23:29";
        double expSpeed = 18.64113;
        int expAuthority = 10;
        Object[] speedAuth = new Object[3];
        speedAuth = instance.Dispatch(dest, tNum, timeD);
        String dispatchTime = speedAuth[2].toString();
        double speed = (Double)speedAuth[0];
        int authority = (Integer)speedAuth[1];
        assertEquals(expSpeed, speed);
        assertEquals(expAuthority, authority);
        assertEquals(expDispatchTime, dispatchTime);
    }

    @org.junit.jupiter.api.Test
    void loadSchedule() {
        System.out.println("Load Schedule");
        String filename = "/Users/haleighdefoor/blueline.csv";
        DisplayLine display = new DisplayLine();
        display.LoadSchedule(filename);
        ArrayList<DisplayLine> dispList = display.getDisps();

        System.out.println(dispList);
    }

    @org.junit.jupiter.api.Test
    void calcThroughput() {
        DisplayLine display = new DisplayLine();
        System.out.println("Calculate Throughput");
        int tix = 500;
        int tp = display.CalcThroughput(tix);
        System.out.println(tp);
    }

    @org.junit.jupiter.api.Test
    void displayTransit() {
    }

    @org.junit.jupiter.api.Test
    void displaySchedule() {
    }

    @org.junit.jupiter.api.Test
    void displayStatus() {
    }

    @org.junit.jupiter.api.Test
    void loadData() {
    }

    @org.junit.jupiter.api.Test
    void checkOccupancy() {
    }

    @org.junit.jupiter.api.Test
    void checkStatus() {
    }

    @org.junit.jupiter.api.Test
    void openTrack() {
    }

    @org.junit.jupiter.api.Test
    void closeTrack() {
    }

    @org.junit.jupiter.api.Test
    void getDisps() {
    }
}