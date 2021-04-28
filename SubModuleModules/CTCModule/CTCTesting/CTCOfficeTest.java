import CTCOffice.CTCOffice;
import CTCOffice.DisplayLine;

import javax.swing.Timer;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;

class CTCOfficeTest {

    @org.junit.jupiter.api.Test
    void backToYard() {
        CTCOffice instance = new CTCOffice();
       int[] test = instance.backToYardGreen(105);

       for(int i =0; i<150; i++) {
           int testi = i+1;
           System.out.println("BN" + testi + " " + test[i]);
       }
    }
    @org.junit.jupiter.api.Test
    void routeGreen() {
        CTCOffice instance = new CTCOffice();

        int[] stations = {65, 73, 65, 77, 65, 88, 65, 96, 65, 105, 65, 114, 65, 122, 65, 131, 65, 140};

        for (int j = 0; j < 16; j=j+2) {
            int sum = 0;
            int start = stations[j];
            int end = stations[j+1];
            int[] test = instance.routeGreen(start,end);
            int answer =0;
            System.out.println("Start and end " + start + " " + end);
            for (int i = start; i <= end; i++) {
                answer += i;
            }
            for (int i = 0; i < 150; i++) {
                int testi = i + 1;
                //System.out.println("BN" + testi + " " + test[i]);
                if (test[i] != 0)
                    sum += testi;
            }
            assertEquals(sum, answer);
        }
    }
    @org.junit.jupiter.api.Test
    void routeGreen2() {
        CTCOffice instance = new CTCOffice();

        int[] stations = {65, 73, 65, 77, 65, 88, 65, 96, 65, 105, 65, 114, 65, 122, 65, 131, 65, 140};

            int sum = 0;
            int start = stations[8];
            int end = stations[9];
            int[] test = instance.routeGreen(start,end);
            int answer =0;
            System.out.println("Start and end " + start + " " + end);
            for (int i = start; i <= end; i++) {
                answer += i;
            }
            for (int i = 0; i < 150; i++) {
                int testi = i + 1;
                System.out.println("BN" + testi + " " + test[i]);
                if (test[i] != 0)
                    sum += testi;
            }
            //assertEquals(sum, answer);
        }

    @org.junit.jupiter.api.Test
    void routeRed2() {
        CTCOffice instance = new CTCOffice();

        int[] stations = {16, 25, 35, 45, 60};

        int sum = 0;
        int start = stations[0];
        int end = stations[1];
        int[] test = instance.routeGreen(start,end);
        int answer =0;
        System.out.println("Start and end " + start + " " + end);
        for (int i = start; i <= end; i++) {
            answer += i;
        }
        for (int i = 0; i < 150; i++) {
            int testi = i + 1;
            System.out.println("BN" + testi + " " + test[i]);
            if (test[i] != 0)
                sum += testi;
        }
        //assertEquals(sum, answer);
    }




    @org.junit.jupiter.api.Test
    void backToYardRed() {
        CTCOffice instance = new CTCOffice();
        int[] test = instance.backToYardRed(21);

        for(int i =0; i<76; i++) {
            int testi = i+1;
            System.out.println("BN" + testi + " " + test[i]);
        }
    }


    @org.junit.jupiter.api.Test
    void dispatch() {
        CTCOffice instance = new CTCOffice();
        System.out.println("Dispatch");

        //String dest = "Station B";
        String dest = "Dormont";
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

        //Testing
        System.out.println(speed);
        System.out.println(authority);
        System.out.println(dispatchTime);

     //   assertEquals(expSpeed, speed);
      //  assertEquals(expAuthority, authority);
       // assertEquals(expDispatchTime, dispatchTime);
    }

    @org.junit.jupiter.api.Test
    void loadSchedule() {
        System.out.println("Load Schedule");
        String filename = "/Users/haleighdefoor/blueline.csv";
        CTCOffice display = new CTCOffice();
        display.LoadSchedule(filename);
        ArrayList<DisplayLine> dispList = display.getDisps();

        System.out.println(dispList);
    }

    @org.junit.jupiter.api.Test
    void calcThroughput() {
        CTCOffice display = new CTCOffice();
        System.out.println("Calculate Throughput");
        int tp = display.CalcThroughput();
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