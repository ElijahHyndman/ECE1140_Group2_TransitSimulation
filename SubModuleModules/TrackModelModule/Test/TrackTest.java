import Track.Track;
import TrackConstruction.TrackElement;

import static org.junit.jupiter.api.Assertions.*;

class TrackTest {

    @org.junit.jupiter.api.Test
    void importTrack() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        boolean expResult = true;
        boolean result = instance.importTrack(filepath);
        assertEquals(expResult, result);
    }

    @org.junit.jupiter.api.Test
    void getGreenLine() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        boolean expResult = true;
        instance.importTrack(filepath);
        System.out.println(instance.getGreenLine());
    }

    @org.junit.jupiter.api.Test
    void getRedLine() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        System.out.println(instance.getRedLine());
    }

    @org.junit.jupiter.api.Test
    void getBlock() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        System.out.println(instance.getBlock(0));
        for(int i = 1; i < 77; i++) {
            assertEquals(instance.getBlock(i).getBlockNum(), i);
           assertEquals(instance.getBlock(i).getLine(), "Red");
       }
        for(int i = 77; i < 227 ; i++) {
            assertEquals(instance.getBlock(i).getBlockNum(), i-76);
            assertEquals(instance.getBlock(i).getLine(), "Green");
        }

    }

    @org.junit.jupiter.api.Test
    void getBlockLine() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
       System.out.println(instance.getBlockLine(0, "Red"));
       for(int i = 1; i < 76; i++) {
            assertEquals(instance.getBlockLine(i,"Red").getBlockNum(), i);
            assertEquals(instance.getBlock(i).getLine(), "Red");
        }
      System.out.println(instance.getBlockLine(0, "Green"));
        for(int i = 1; i < 150 ; i++) {
            assertEquals(instance.getBlockLine(i,"Green").getBlockNum(), i);
            assertEquals(instance.getBlockLine(i, "Green").getLine(), "Green");
        }
    }

    @org.junit.jupiter.api.Test
    void getTrackHeaterStatus() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        instance.setEnvironmentalTemperature(20);
        assertEquals(instance.getTrackHeaterStatus(), true);
        instance.setEnvironmentalTemperature(40);
        assertEquals(instance.getTrackHeaterStatus(), false);

    }

    @org.junit.jupiter.api.Test
    void setEnvironmentalTemperature() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        instance.setEnvironmentalTemperature(20);
        assertEquals(instance.getEnvironmentalTemperature(), 20);


        //Not Really Meant to be here -- NEED TO TAKE OUT !!
        for(int i = 0; i<=150; i++) {
            System.out.println(instance.getGreenLine().get(i).getBeacon());
        }
        System.out.println("71: " + instance.getGreenLine().get(71).getBeacon());


    }

    @org.junit.jupiter.api.Test
    void getSwitches() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        System.out.println(instance.getSwitches());
    }



    @org.junit.jupiter.api.Test
    void setFailure() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        instance.setFailure(1,"Red",1);
        System.out.println(instance.getFailures());
        instance.setFailure(2,"Red",2);
        instance.setFailure(3,"Red",3);
        instance.setFailure(4,"Red",2);
        System.out.println(instance.getFailures());
        instance.setFailure(2,"Red",0);
        System.out.println(instance.getFailures());
        instance.setFailure(3,"Red",0);
        instance.setFailure(4,"Red",0);
        instance.setFailure(1,"Red",0);
        System.out.println("This is it: " + instance.getFailures());
        instance.setFailure(1,"Green",1);
        System.out.println(instance.getFailures());
        instance.setFailure(2,"Green",2);
        instance.setFailure(3,"Green",3);
        instance.setFailure(4,"Green",2);
        System.out.println(instance.getFailures());
        instance.setFailure(2,"Green",0);
        System.out.println(instance.getFailures());
        instance.setFailure(3,"Green",0);
        instance.setFailure(4,"Green",0);
        instance.setFailure(1,"Green",0);
        System.out.println("This is it: " + instance.getFailures());

;    }

    @org.junit.jupiter.api.Test
    void validFile() {
        String filePath = "THISISINVALID";
        Track instance = new Track();
        boolean expResult = false;
        boolean result = instance.validFile(filePath);
        assertEquals(expResult, result);
        filePath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        expResult = true;
        result = instance.validFile(filePath);
        assertEquals(expResult,result);
        //testing to make sure switches are gotten right
    }


    @org.junit.jupiter.api.Test
    void getNext() {
        System.out.println("getNext");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //Starting at 12C going to 11C is current
        TrackElement prev = instance.getGreenLine().get(12);
        TrackElement cur = instance.getGreenLine().get(11);

        //setting Switches
        instance.setSwitch(instance.getSwitches().get(7),1); // 12 is connecting 1A to 13
        instance.setSwitch(instance.getSwitches().get(8),0); //switch is 29 -30
        instance.setSwitch(instance.getSwitches().get(11),0); // switch from 77 to 76
        instance.setSwitch(instance.getSwitches().get(12),0); // switch from 85 to 86
        instance.setSwitch(instance.getSwitches().get(9),1); // switch NOT to the yard
        instance.setSwitch(instance.getSwitches().get(10),0); // switch NOT to the yard

        for(int i = 0 ; i < 200 ; i++ ) {
            if(i == 90) {
                //Here we need to TOGGLE SWITCH
                instance.setSwitch(instance.getSwitches().get(11),1); // setting 76 to 150
                instance.setSwitch(instance.getSwitches().get(8),1); // setting 76 to 150
                instance.setSwitch(instance.getSwitches().get(12),1); // switch from 100 - 85 ????? (should be 1 but 0 )
            }
            if(i == 150) {
                instance.setSwitch(instance.getSwitches().get(11), 1); // setting 150 to be connected to F
                instance.setSwitch(instance.getSwitches().get(7), 0); // 12 to  13
            }
            TrackElement test = instance.getNext(cur,prev);
            if(test != null) {
                System.out.println(i + "Prev: " + prev.getBlockNum() + "Cur: " + cur.getBlockNum() + "Next:  " + test.getBlockNum() + test.getSection());


                prev = cur;
                cur = test;

            }
            else
                System.out.println("Done");

        }

        //Testing Iteration 3 -- WILL ONLY GET NEXT BLOCK ONCE SWITCH IS ON (automatically 0 and 62) won't go until switch set.
        System.out.println("--------------------------------------------------------------");
         prev = instance.getGreenLine().get(0);
         cur = instance.getGreenLine().get(62);

        //setting Switches
        instance.setSwitch(instance.getSwitches().get(10),0); // switch NOT to the yard

        for(int i = 0 ; i < 20 ; i++ ) {
            TrackElement test = instance.getNext(cur,prev);
            if(test != null) {
                System.out.println(i + "Prev: " + prev.getBlockNum() + "Cur: " + cur.getBlockNum() + "Next:  " + test.getBlockNum() + test.getSection());


                prev = cur;
                cur = test;

            }
            else
                System.out.println("Done");

        }

    }

    @org.junit.jupiter.api.Test
    void setSwitch() {
        System.out.println("setSwitch");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        //Testing for Green Line Switches
        instance.setSwitch(instance.getSwitches().get(11),0);
        assertEquals(instance.getGreenLine().get(76).getCurrentDirection(), 77);
        assertEquals(instance.getGreenLine().get(77).getCurrentDirection(), -2);
        instance.setSwitch(instance.getSwitches().get(11),1);
        assertEquals(instance.getGreenLine().get(76).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(77).getCurrentDirection(), 101);

        //Testing Switch 86
        instance.setSwitch(instance.getSwitches().get(12),0);
        assertEquals(instance.getGreenLine().get(85).getCurrentDirection(), 86);
        assertEquals(instance.getGreenLine().get(100).getCurrentDirection(), -2);
        instance.setSwitch(instance.getSwitches().get(12),1);
        assertEquals(instance.getGreenLine().get(85).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(100).getCurrentDirection(), 85);




        //Testing Switch 12
        instance.setSwitch(instance.getSwitches().get(7),0);
        assertEquals(instance.getGreenLine().get(13).getCurrentDirection(), 12);
        assertEquals(instance.getGreenLine().get(1).getCurrentDirection(), -2);
        instance.setSwitch(instance.getSwitches().get(7),1);
        assertEquals(instance.getGreenLine().get(1).getCurrentDirection(), 13);
        assertEquals(instance.getGreenLine().get(13).getCurrentDirection(), -2);

        //Testing Switch 29
        instance.setSwitch(instance.getSwitches().get(8),0);
        assertEquals(instance.getGreenLine().get(29).getCurrentDirection(), 30);
        assertEquals(instance.getGreenLine().get(150).getCurrentDirection(), -2);
        instance.setSwitch(instance.getSwitches().get(8),1);
        assertEquals(instance.getGreenLine().get(150).getCurrentDirection(), 28);
        assertEquals(instance.getGreenLine().get(29).getCurrentDirection(), -2);


        //Testing Switch 58 & 62 are YARD will do after
        for(int i =0 ; i < 4; i ++)
            System.out.println(instance.getGreenLine().get(58).getDirectionStates(i));
        System.out.println("--");
        for(int i =0 ; i < 4; i ++)
            System.out.println(instance.getGreenLine().get(62).getDirectionStates(i));
        System.out.println("--");
        instance.setSwitch(instance.getSwitches().get(9),0);
        System.out.println(instance.getGreenLine().get(58).getCurrentDirection());
        instance.setSwitch(instance.getSwitches().get(9),1);
        System.out.println(instance.getGreenLine().get(58).getCurrentDirection());
        System.out.println("--");
        instance.setSwitch(instance.getSwitches().get(10),0);
        System.out.println(instance.getGreenLine().get(62).getCurrentDirection());
        instance.setSwitch(instance.getSwitches().get(10),1);
        System.out.println(instance.getGreenLine().get(62).getCurrentDirection());

    }
}

