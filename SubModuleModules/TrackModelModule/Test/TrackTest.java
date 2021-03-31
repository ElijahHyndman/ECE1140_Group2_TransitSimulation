import Track.Track;
import TrackConstruction.TrackElement;
import Track.TrackGUI;

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


    }

    @org.junit.jupiter.api.Test
    void getBeacon() {
        //Not Really Meant to be here -- NEED TO TAKE OUT !!
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        for(int i = 0; i<=150; i++) {
            System.out.println(instance.getGreenLine().get(i).getBeacon());
        }
        System.out.println("71: " + instance.getGreenLine().get(71).getBeacon());

    }

    @org.junit.jupiter.api.Test
    void getSwitches() {
        System.out.println("get Switches");
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
        instance.getSwitches().get(7).setSwitchState(true); // 12 1a to 13
        instance.getSwitches().get(8).setSwitchState(false); // 29 to -30
        instance.getSwitches().get(11).setSwitchState(false); // 77 to 76
        instance.getSwitches().get(12).setSwitchState(false); // 85 to 86
        instance.getSwitches().get(9).setSwitchState(true); //switch not to yard
        instance.getSwitches().get(10).setSwitchState(false); // switch not to yard
        instance.updateSwitches();


        for(int i = 0 ; i < 200 ; i++ ) {
            if(i == 90) {
                //Here we need to TOGGLE SWITCH
                instance.getSwitches().get(11).setSwitchState(true); // 76 to 150
                instance.getSwitches().get(8).setSwitchState(true); // 76 to 150
                instance.getSwitches().get(12).setSwitchState(true); // 100 - 85
                instance.updateSwitches();

            }
            if(i == 150) {
                instance.getSwitches().get(11).setSwitchState(true); // 150 to f
                instance.getSwitches().get(7).setSwitchState(false); // 12 to 13
                instance.updateSwitches();
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

        //setting Switchesst
        instance.getSwitches().get(10).setSwitchState(false);
        instance.updateSwitches();

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
    void updateTickets(){
        System.out.println("updateTickets");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        assertEquals(instance.updateTickets(),0);
        int sales =0;
        for(int i =0; i< instance.getStations().size(); i++) {
            instance.getStations().get(i).setTicketSales();
            sales = sales +  instance.getStations().get(i).getTicketSales();
           // System.out.println("new tickets: " + instance.getStations().get(i).getTicketSales() + " and sales " + sales);
        }
        assertEquals(instance.updateTickets(),sales);





    }

    @org.junit.jupiter.api.Test
    void setSwitch() {
        System.out.println("setSwitch");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        TrackGUI testGUI = new TrackGUI(instance);
        testGUI.setVisible(true);
        testGUI.latch(instance);




        //Testing for Green Line Switches
        instance.getSwitches().get(11).setSwitchState(false);
        instance.getSwitches().get(12).setSwitchState(false); // testing 86
        instance.getSwitches().get(7).setSwitchState(false);
        instance.getSwitches().get(8).setSwitchState(false);
        instance.getSwitches().get(8).setSwitchState(false);
        instance.getSwitches().get(9).setSwitchState(false);
        instance.getSwitches().get(10).setSwitchState(false);
        instance.updateSwitches();
        assertEquals(instance.getGreenLine().get(76).getCurrentDirection(), 77);
        assertEquals(instance.getGreenLine().get(77).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(85).getCurrentDirection(), 86);
        assertEquals(instance.getGreenLine().get(100).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(13).getCurrentDirection(), 12);
        assertEquals(instance.getGreenLine().get(1).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(29).getCurrentDirection(), 30);
        assertEquals(instance.getGreenLine().get(150).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(29).getCurrentDirection(), 30);
        assertEquals(instance.getGreenLine().get(150).getCurrentDirection(), -2);
        System.out.println(instance.getGreenLine().get(58).getCurrentDirection());
        System.out.println(instance.getGreenLine().get(62).getCurrentDirection());


        instance.getSwitches().get(11).setSwitchState(true);
        instance.getSwitches().get(12).setSwitchState(true);
        instance.getSwitches().get(7).setSwitchState(true);
        instance.getSwitches().get(8).setSwitchState(true);
        instance.getSwitches().get(9).setSwitchState(true);
        instance.getSwitches().get(10).setSwitchState(true);
        instance.updateSwitches();
        assertEquals(instance.getGreenLine().get(76).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(77).getCurrentDirection(), 101);

        //Testing Switch 86
        assertEquals(instance.getGreenLine().get(85).getCurrentDirection(), -2);
        assertEquals(instance.getGreenLine().get(100).getCurrentDirection(), 85);

        //Testing Switch 12
        assertEquals(instance.getGreenLine().get(1).getCurrentDirection(), 13);
        assertEquals(instance.getGreenLine().get(13).getCurrentDirection(), -2);

        //Testing Switch 29
        assertEquals(instance.getGreenLine().get(150).getCurrentDirection(), 28);
        assertEquals(instance.getGreenLine().get(29).getCurrentDirection(), -2);

        System.out.println(instance.getGreenLine().get(58).getCurrentDirection());
        System.out.println(instance.getGreenLine().get(62).getCurrentDirection());



    }

    @org.junit.jupiter.api.Test
    void testingGUIUpdate() {
        System.out.println("Testing UI ");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //Instantiate UI

            TrackGUI testGUI = new TrackGUI(instance);
            testGUI.setVisible(true);
            testGUI.draw();
            testGUI.latch(instance);
            instance.setFailure(2,"Green",1);
            testGUI.latch(instance);
            System.out.println(instance.getFailures());
            int i =0;
            while (true){
                testGUI.draw();
                instance.getGreenLine().get(2).setOccupied(true);
                testGUI.latch(instance);
                instance.getGreenLine().get(2).setOccupied(false);
                testGUI.latch(instance);
                instance.getGreenLine().get(2).setAuthority(i);
                testGUI.latch(instance);
                instance.getGreenLine().get(2).setCommandedSpeed(i);
                testGUI.latch(instance);
                    instance.setFailure(2,"Green",1);
                testGUI.latch(instance);
                instance.setFailure(2,"Green",2);
                testGUI.latch(instance);
                i++;


            }


    }
}

