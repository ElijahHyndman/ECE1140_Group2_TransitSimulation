import Track.Track;
import TrackConstruction.Station;
import TrackConstruction.Switch;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import Track.TrackGUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TrackTest {

    /*
    Testing getting next for red and green
    make sure that the red and green
     */
    @org.junit.jupiter.api.Test
    void testgetNextRedGreenTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        TrackElement cur = instance.getRedLine().get(3);
        TrackElement prev = instance.getRedLine().get(2);
        System.out.println("This is red " + instance.getNext(cur,prev));
        cur = instance.getGreenLine().get(3);
        prev = instance.getGreenLine().get(2);
        System.out.println("This is Green " + instance.getNext(cur,prev));

        instance.dispatchLine(1);
        cur = instance.getRedLine().get(0);
        prev = instance.getRedLine().get(0);
        instance.getSwitches().get(0).setSwitchState(true);
        instance.updateSwitches();
        System.out.println("This is red " + instance.getNext(cur,prev));
        instance.dispatchLine(0);
        instance.getSwitches().get(10).setSwitchState(true);
        instance.updateSwitches();
        cur = instance.getGreenLine().get(0);
        prev = instance.getGreenLine().get(0);
        System.out.println("This is Green " + instance.getNext(cur,prev));
    }


    /*
    testing the path with switch 0 being on or off
     */
    @org.junit.jupiter.api.Test
    void switch0RedTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        int test =0;
        instance.getSwitches().get(0).setSwitchState(false);
        instance.updateSwitches();
        test = iterateNextRed(instance,8,7,6);
        assertEquals(test,92);
        test = iterateNextRed(instance,8,10,11);
        assertEquals(test,44);
        instance.getSwitches().get(0).setSwitchState(true);
        instance.updateSwitches();
        test = iterateNextRed(instance,4,7,6);
        assertEquals(test,26);
        test = iterateNextRed(instance,3,10,11);
        assertEquals(test,18);

    }

    /*
    testing the path with switch 1 being on or off
     */
    @org.junit.jupiter.api.Test
    void switch1RedTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //testing switch 16 - 15 and 1 - 15 ( want it to go to 16 after 1 - 15 - 16
        int test =0;
        instance.getSwitches().get(1).setSwitchState(false);
        instance.updateSwitches();

        test = iterateNextRed(instance,5,14,13); // 13 - 14 - 15 - 16 - 17
        assertEquals(test,85);
        test = iterateNextRed(instance,5,17,18);  // 18 - 17 - 16 - 15 - 14
        assertEquals(test,70);
        test = iterateNextRed(instance,4,2,3);    // 2 - 1 - NULL
        assertEquals(test,1);
        instance.getSwitches().get(1).setSwitchState(true);
        instance.updateSwitches();
        test = iterateNextRed(instance,5,14,13); // 13 - 14 - 15 - 1 - 2 - 3
        assertEquals(test,25);
        test = iterateNextRed(instance,5,17,18);  // 18 - 17 - 16 - 15 - 1 - 2 - 3
        assertEquals(test,31);
        test = iterateNextRed(instance,8,2,3); // 2 - 1 - 15 - 16
       assertEquals(test,127);

    }

    /*
    testing the path with switch 2 being on or off
     */
    @org.junit.jupiter.api.Test
    void switch2RedTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //testing switch 27 - 28 and 27 - 76
        //
        int test =0;
        instance.getSwitches().get(2).setSwitchState(false);
        instance.updateSwitches();
        //These three are all correct
        test = iterateNextRed(instance,5,25,24); // 27 - 28
        assertEquals(test,140);
        test = iterateNextRed(instance,5,29,30); // 27 - 76
        assertEquals(test,130);
        test = iterateNextRed(instance,5,75,74); // 27 - 76
        assertEquals(test,76);
        instance.getSwitches().get(2).setSwitchState(true);
        instance.updateSwitches();
        //these three are not all correct
       test = iterateNextRed(instance,5,75,74); // 27 - 76 // This is correct
        assertEquals(test,178);
        test = iterateNextRed(instance,7,25,24); // 25 - 26 - 27 - 76
        assertEquals(test,423);
        test = iterateNextRed(instance,5,29,30); // shouldn't go on 27th !!
        assertEquals(test,28);

        // switch 3 ( 32 - 33 - 32 - 72)
        // -- switch 4 (38-39 - 38 - 71)
        // -- switch 5 (43-44 - 43-67)
        // -- switch 6 (52-53-52-66)
    }

    /*
    testing the path with switch 3 being on or off
     */
    @org.junit.jupiter.api.Test
    void switch3RedTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        // switch 3 ( 32 - 33 - 32 - 72)
        int test =0;
        instance.getSwitches().get(3).setSwitchState(false);
        instance.updateSwitches();
        //These three are all correct
        test = iterateNextRed(instance,5,30,29);
        assertEquals(test,165);
        test = iterateNextRed(instance,5,34,35);
        assertEquals(test,155);
        test = iterateNextRed(instance,5,73,74);
        assertEquals(test,72);
        instance.getSwitches().get(3).setSwitchState(true);
        instance.updateSwitches();
        test = iterateNextRed(instance,5,30,29);
        assertEquals(test,282);
        test = iterateNextRed(instance,5,34,35);
        assertEquals(test,284);
        test = iterateNextRed(instance,5,73,74);
        assertEquals(test,194);


        // -- switch 4 (38-39 - 38 - 71)
        // -- switch 5 (43-44 - 43-67)
        // -- switch 6 (52-53-52-66)
    }

    //T 76 (28-27) and Q 71 (38-39)



    /*
    testing the path with switch 4 being on or off
     */
    @org.junit.jupiter.api.Test
    void switch4RedTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        // switch 4 (38-39 - 38 - 71)
        int test =0;
        instance.getSwitches().get(4).setSwitchState(false);
        instance.updateSwitches();
        //These three are all correct
        test = iterateNextRed(instance,5,37,36);
        assertEquals(test,200);
        test = iterateNextRed(instance,5,40,41);
        assertEquals(test,185);
        test = iterateNextRed(instance,5,70,69);
        assertEquals(test,71);
        instance.getSwitches().get(4).setSwitchState(true);
        instance.updateSwitches();
        test = iterateNextRed(instance,5,37,36);
        assertEquals(test,316);
        test = iterateNextRed(instance,5,40,41);
        assertEquals(test,39);
        test = iterateNextRed(instance,5,70,69);
        assertEquals(test,217);

        // -- switch 5 (43-44 - 43-67)
        // -- switch 6 (52-53-52-66)
    }

    /*
    testing the path with switch 5 being on or off
     */
    @org.junit.jupiter.api.Test
    void switch5RedTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        // switch 5 (43-44 - 43-67)
        int test =0;
        instance.getSwitches().get(5).setSwitchState(false);
        instance.updateSwitches();
        //These three are all correct

        test = iterateNextRed(instance,5,42,41);
        assertEquals(test,225);
        test = iterateNextRed(instance,5,45,46);
        assertEquals(test,210);
        test = iterateNextRed(instance,5,68,69);
        assertEquals(test,67);
        instance.getSwitches().get(5).setSwitchState(true);
        instance.updateSwitches();
      test = iterateNextRed(instance,5,42,41);
       assertEquals(test,317);
        test = iterateNextRed(instance,5,45,46);
        assertEquals(test,291);
        test = iterateNextRed(instance,5,68,69);
        assertEquals(test,233);
        // -- switch 6 (52-53-52-66)
    }

    /*
    testing the path with switch 6 being on or off
     */
    @org.junit.jupiter.api.Test
    void switch6RedTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        // switch 6 (52-53 : 52-66)
        int test =0;
        instance.getSwitches().get(6).setSwitchState(false);
        instance.updateSwitches();
        //These three are all correct

        test = iterateNextRed(instance,5,51,50);
        assertEquals(test,270);
        test = iterateNextRed(instance,5,54,55);
        assertEquals(test,255);
        test = iterateNextRed(instance,5,65,64);
        assertEquals(test,66);
        instance.getSwitches().get(6).setSwitchState(true);
        instance.updateSwitches();
        test = iterateNextRed(instance,5,51,50); // fail
        assertEquals(test,310);
        test = iterateNextRed(instance,5,54,55);
        assertEquals(test,53);
        test = iterateNextRed(instance,5,65,64); // fail
        assertEquals(test,268);

    }

    /*
    testing the entire path of the red entire path of the redline
     */
    @org.junit.jupiter.api.Test
    void getNextRedCircleTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        TrackElement cur = instance.getGreenLine().get(0);
        TrackElement prev = instance.getGreenLine().get(0);
        TrackElement next = null;
        instance.getSwitches().get(0).setSwitchState(true);
        instance.getSwitches().get(1).setSwitchState(true);

        //IF only loop to T - S - R and pack up 43 and 38
     //   instance.getSwitches().get(4).setSwitchState(true);
      //  instance.getSwitches().get(5).setSwitchState(true);

        //IF looping to Q - P - O and back up
      //  instance.getSwitches().get(3).setSwitchState(true);
      //  instance.getSwitches().get(2).setSwitchState(true);
        instance.updateSwitches();
        int test = 0;
        System.out.println("*****");
        for (int i = 0; i < 120; i++) {
            next = instance.getNextRed(cur, prev);

            if(i == 30) {
                instance.getSwitches().get(2).setSwitchState(false); // for loop around q - p - o
                instance.updateSwitches();
            }
            if(i == 41){

                instance.getSwitches().get(1).setSwitchState(false);
                instance.getSwitches().get(0).setSwitchState(false);
                instance.getSwitches().get(4).setSwitchState(false); // for loop around q - p - o

                //testing weird loop switches
                instance.updateSwitches();
            }
            if(i == 51){
                instance.getSwitches().get(6).setSwitchState(true);
                instance.updateSwitches();
            }

            if(i == 70) { // This isn't working so need to rechange / update switches
                instance.getSwitches().get(5).setSwitchState(true);
                instance.getSwitches().get(4).setSwitchState(true);
                instance.getSwitches().get(3).setSwitchState(true);
                instance.getSwitches().get(2).setSwitchState(true);
                instance.getSwitches().get(0).setSwitchState(true);
                instance.updateSwitches();
            }


            if (next != null) {
                test += next.getBlockNum();
                System.out.println(cur.getBlockNum() + ", " + i );
                prev = cur;
                cur = next;
            }


            if (next == null) {
                System.out.println(cur.getBlockNum() + "" +  cur.getSection() + " " + test);
                System.out.println("NULL");
                break;
            }
        }


    }

    /*
    testing import path
     */
    @org.junit.jupiter.api.Test
    void importTrackTest() {
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        boolean expResult = true;
        boolean result = instance.importTrack(filepath);
        assertEquals(expResult, result);
    }

    /*
    testing the green Line
     */
    @org.junit.jupiter.api.Test
    void getGreenLineTest() {
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        boolean expResult = true;
        instance.importTrack(filepath);
        System.out.println(instance.getGreenLine());
    }

    /*
    testing the getting a train from yard to the green line
     */
    @org.junit.jupiter.api.Test
    void getNextGreenYardSwitchTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        instance.getSwitches().get(10).setSwitchState(true); // switch not to yard
        TrackElement cur = instance.getGreenLine().get(0);
        TrackElement prev = instance.getGreenLine().get(0);
        TrackElement next = null;
        int test = 0;
        for (int i = 0; i < 5; i++) {
            next = instance.getNextGreen(cur, prev);

            if (next != null) {
                test += next.getBlockNum();
            }
            prev = cur;
            cur = next;

        }
        assertEquals(test,320);
        //Testing switch is now off
        instance.getSwitches().get(10).setSwitchState(false); // switch not to yard
         cur = instance.getGreenLine().get(0);
         prev = instance.getGreenLine().get(0);
         test = 0;
        for (int i = 0; i < 5; i++) {
            next = instance.getNextGreen(cur, prev);

            if (next != null) {
                test += next.getBlockNum();
                prev = cur;
                cur = next;
            }
            else
                break;

        }
        assertEquals(test,0);
    }

    /*
    iterate through method for the green line
     */
    public int iterateNext(Track instance, int iter, int curI, int prevI) {
        TrackElement cur = instance.getGreenLine().get(curI);
        TrackElement prev = instance.getGreenLine().get(prevI);
        TrackElement next = null;
        int test = 0;
        System.out.println("*****");
        for (int i = 0; i < iter; i++) {
            next = instance.getNextGreen(cur, prev);

            if (next != null) {
                test += next.getBlockNum();
                System.out.println(cur.getBlockNum() + "" +  cur.getSection() + " " + test);
                prev = cur;
                cur = next;
            }


            if (next == null) {
                System.out.println(cur.getBlockNum() + "" +  cur.getSection() + " " + test);
                System.out.println("NULL");
                break;
            }
        }
        return test;
    }

    /*
    iterating through red line for testing red switches
     */
    public int iterateNextRed(Track instance, int iter, int curI, int prevI) {
        TrackElement cur = instance.getRedLine().get(curI);
        TrackElement prev = instance.getRedLine().get(prevI);
        TrackElement next = null;
        int test = 0;
        System.out.println("*****");
        for (int i = 0; i < iter; i++) {
            next = instance.getNextRed(cur, prev);

            if (next != null) {
                test += next.getBlockNum();
                System.out.println(cur.getBlockNum() + "" +  cur.getSection() + " " + test);
                prev = cur;
                cur = next;
            }


            if (next == null) {
                System.out.println(cur.getBlockNum() + "" +  cur.getSection() + " " + test);
                System.out.println("NULL");
                break;
            }
        }
        return test;
    }

    /*
    testing switches directions when updating switches
     */
    @org.junit.jupiter.api.Test
    void testingSwitchTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        for(int i = 0; i < instance.getSwitches().size(); i++)
            System.out.println(instance.getSwitches().get(i).getInfrastructure() + " " + i);


        instance.getSwitches().get(11).setSwitchState(false); // testing switch to 76 - 77 or 77 - 101
        instance.updateSwitches();

        System.out.println("false " + instance.getGreenLine().get(76).getCurrentDirection());
        instance.getSwitches().get(11).setSwitchState(true); // testing switch to 76 - 77 or 77 - 101
        instance.updateSwitches();
        System.out.println("true" + instance.getGreenLine().get(76).getCurrentDirection());

    }

    /*
    testing green switch 11
     */
    @org.junit.jupiter.api.Test
    void getNextGreenSwitches11Test() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        instance.getSwitches().get(10).setSwitchState(true); // switch not to yard
        instance.getSwitches().get(11).setSwitchState(false); // testing switch to 76 - 77 or 77 - 101
        instance.updateSwitches();

        int test =0;
        test = iterateNext(instance,18,0,0);
        instance.updateSwitches();
        assertEquals(test,1269);
        instance.getSwitches().get(11).setSwitchState(true); // testing switch to 76 - 77 or 77 - 101
        instance.updateSwitches();
        test = iterateNext(instance,18,0,0);
        assertEquals(test,1035);

        //testing going back around
        System.out.println("***");
        test = iterateNext(instance,15,83,84);
        assertEquals(test,1389);

        instance.getSwitches().get(11).setSwitchState(false); // testing switch to 76 - 77 or 77 - 101
        instance.updateSwitches();
        System.out.println("***");
        test = iterateNext(instance,15,83,84);
        assertEquals(test,1197);

    }

    /*
    testing the path with switch 8 being toggled
     */
    @org.junit.jupiter.api.Test
    void getNextGreenSwitches8Test() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        instance.getSwitches().get(8).setSwitchState(true); // switch not to yard
        instance.updateSwitches();

        int test =0;
        test = iterateNext(instance,18,142,143);
        assertEquals(test,1417);
        instance.getSwitches().get(8).setSwitchState(false); // testing switch to 76 - 77 or 77 - 101
        instance.updateSwitches();
        test = iterateNext(instance,18,142,143);
        assertEquals(test,1172);


        //test back around
        test = iterateNext(instance,18,27,26);
        assertEquals(test,657);

        instance.getSwitches().get(8).setSwitchState(true); // testing switch to 76 - 77 or 77 - 101
        instance.updateSwitches();
        test = iterateNext(instance,18,27,26);
        assertEquals(test,57);

    }

    /*
    testing the path with switch 9 being toggled
     */
    @org.junit.jupiter.api.Test
    void getNextGreenSwitches9Test() {
        //switch to yard
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        instance.getSwitches().get(9).setSwitchState(true); // switch not to yard
        instance.updateSwitches();
        int test;
        test = iterateNext(instance,4,57,56);
    }

    /*
    testing the path with switch 7 being toggled
     */
    @org.junit.jupiter.api.Test
    void getNextGreenSwitches7Test() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        instance.getSwitches().get(7).setSwitchState(false); // switch 1 to 12
        instance.updateSwitches();

        int test =0;
        test = iterateNext(instance,18,3,4);
        assertEquals(test,315);
        instance.getSwitches().get(7).setSwitchState(true); // switch 12 to 13s
        instance.updateSwitches();
        test = iterateNext(instance,18,3,4);
        assertEquals(test,3);

        //test from coming from other way
        test = iterateNext(instance,10,14,15);
        assertEquals(test,85);

        instance.getSwitches().get(7).setSwitchState(false); // switch 1 to 12
        instance.updateSwitches();
        test = iterateNext(instance,18,14,15);
        assertEquals(test,13);


    }

    /*
    testing the path with switch 12 being toggled
     */
    @org.junit.jupiter.api.Test
    void getNextGreenSwitches12Test() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        instance.getSwitches().get(10).setSwitchState(true); // switch not to yard
        instance.getSwitches().get(11).setSwitchState(false); // testing switch to 76 - 77 or 77 - 101
        instance.getSwitches().get(12).setSwitchState(false); //86-85 or 100-85
        instance.updateSwitches();
        int test;


        test = iterateNext(instance,27,0,0);
        assertEquals(test,2025);
        instance.getSwitches().get(12).setSwitchState(true); //86-85 or 100-85
        instance.updateSwitches();
        test = iterateNext(instance,27,0,0);
        assertEquals(test,1850);


        //Now testing on way back if switched or NOT
        System.out.println("***");
        test = iterateNext(instance,20,87,86);
        assertEquals(test,1803);
        instance.getSwitches().get(12).setSwitchState(false); //86-85 or 100-85
        instance.updateSwitches();
        System.out.println("***");
        test = iterateNext(instance,20,87,86);
        assertEquals(test,1222);
            //src / track / update
    }

    /*getting the path for the green line*/
    @org.junit.jupiter.api.Test
    void getNextGreenTest() {
        System.out.println("getNextGreen");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        instance.getSwitches().get(10).setSwitchState(true); // switch not to yard
        instance.getSwitches().get(11).setSwitchState(false); // testing switch to 76 - 77 or 77 - 101
        instance.getSwitches().get(12).setSwitchState(false); //86-85 or 100-85
        instance.getSwitches().get(8).setSwitchState(true); // z to G -- WORKING (1/2)
        instance.getSwitches().get(7).setSwitchState(true); // 12 -13 -- working (1/2)
        instance.updateSwitches();

        TrackElement cur = instance.getGreenLine().get(0);
        TrackElement prev = instance.getGreenLine().get(0);
        TrackElement next = null;
        int test=0;
        for(int i=0; i<177;i++) {
            next = instance.getNextGreen(cur, prev);
           // System.out.println("prev and cur"+ prev.getBlockNum() + " " + cur.getBlockNum() + " next " + next.getBlockNum());
            if(i == 36) {
                instance.getSwitches().get(12).setSwitchState(true); // testing switch to 101
                instance.getSwitches().get(11).setSwitchState(true); //86-85 or 100-85
           //    System.out.println("**" + instance.getSwitches().get(12).getDirectionStates(2) + " ** index " + instance.getSwitches().get(12).getIndex());
                instance.updateSwitches();
            }

            if(i == 127) {
                instance.getSwitches().get(7).setSwitchState(false);
                instance.getSwitches().get(8).setSwitchState(false);
                instance.getSwitches().get(9).setSwitchState(false);
                instance.getSwitches().get(10).setSwitchState(false); // switch not to yard
                instance.updateSwitches();
            }
            if (next != null) {
                test += next.getBlockNum();
                System.out.print(cur.getBlockNum() + ", ");
                prev = cur;
                cur = next;
            }


            if(next == null) {
                System.out.println(cur.getBlockNum() + "" +  cur.getSection() + ", ");
                System.out.println("null");
                break;
            }

        }
    }

    /*testing getting red line*/
    @org.junit.jupiter.api.Test
    void getRedLineTest() {
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        System.out.println(instance.getRedLine());
    }

    /*getting the block*/
    @org.junit.jupiter.api.Test
    void getBlockTest() {
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
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

    /*testing getting blockLine*/
    @org.junit.jupiter.api.Test
    void getBlockLineTest() {
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
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

    /*testing the track heater*/
    @org.junit.jupiter.api.Test
    void setTrackHeaterStatusTest() {
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        instance.setEnvironmentalTemperature(20);
        assertEquals(instance.getTrackHeaterStatus(), true);
        instance.setEnvironmentalTemperature(40);
        assertEquals(instance.getTrackHeaterStatus(), false);

    }




    /*getting beacons for the green line*/
    @org.junit.jupiter.api.Test
    void getBeaconTest() {
        //Not Really Meant to be here -- NEED TO TAKE OUT !!
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        for(int i = 0; i<=150; i++) {
            System.out.println(instance.getGreenLine().get(i).getBeacon());
        }
        System.out.println("71: " + instance.getGreenLine().get(71).getBeacon());

    }

    /*test getting switches*/
    @org.junit.jupiter.api.Test
    void getSwitchesTest() {
        System.out.println("get Switches");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        System.out.println(instance.getSwitches());
    }


    /*setting and fixing failures */
    @org.junit.jupiter.api.Test
    void setFailureTest() {
        System.out.println("importTrack");
        String filepath = "src/Track/RedGreenUpdated.csv";
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

    }


    @org.junit.jupiter.api.Test
    void validFileTest() {
        String filePath = "THISISINVALID";
        Track instance = new Track();
        boolean expResult = false;
        boolean result = instance.validFile(filePath);
        assertEquals(expResult, result);
        filePath = "src/Track/RedGreenUpdated.csv";
        expResult = true;
        result = instance.validFile(filePath);
        assertEquals(expResult,result);
        //testing to make sure switches are gotten right
    }

    @org.junit.jupiter.api.Test
    void testDispatchYardTest(){
        System.out.println("getNext");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);


        instance.getSwitches().get(10).setSwitchState(true); // switch not to yard
        instance.updateSwitches();

        TrackElement cur = instance.getGreenLine().get(0);
        TrackElement prev = instance.getGreenLine().get(0);
        TrackElement next = null;
        //here we want to dispatch from the yard
        for(int i=0; i < 16; i++) {
            next = instance.getNext(cur,prev);
            if(next != null)
                System.out.println(next.getBlockNum());
            prev = cur;
            cur = next;
            if(cur == null) {
                System.out.println("DONE");
                break;
            }

        }

    }

    @org.junit.jupiter.api.Test
    void getNextTest() {
        System.out.println("getNext");
        String filepath = "src/Track/RedGreenUpdated.csv";
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
    void updateTicketsTest(){
        System.out.println("updateTickets");
        String filepath = "src/Track/RedGreenUpdated.csv";
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



    /*setting switch and getting direcitonality test*/
    @org.junit.jupiter.api.Test
    void setSwitchTest() {
        System.out.println("setSwitch");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);



        //Testing for Green Line Switches
        instance.getSwitches().get(12).setSwitchState(true);
        System.out.println(instance.getGreenLine().get(86).getCurrentDirection());
        System.out.println(instance.getGreenLine().get(86).getDirectionStates(0));
        System.out.println(instance.getGreenLine().get(86).getDirectionStates(1));
        System.out.println(instance.getGreenLine().get(86).getDirectionStates(2));
        System.out.println(instance.getGreenLine().get(86).getDirectionStates(3));
        instance.updateSwitches();
        instance.getSwitches().get(12).setSwitchState(false); // testing 86
        instance.updateSwitches();
        System.out.println(instance.getGreenLine().get(86).getCurrentDirection());

    }

    /*getting the list of stations*/
    @org.junit.jupiter.api.Test
    void getStationsTest() {
        System.out.println("getting blocks in track ");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        ArrayList<Station> test = instance.getStations();
        for(int i = 0 ; i < test.size();i++)
            System.out.println(test.get(i).getInfrastructure());
    }

    /*
    testing the beacons
     */
    @org.junit.jupiter.api.Test
    void testBeaconsTest(){
        System.out.println("getting blocks in track ");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        for(int i=0;i<instance.getBeaconArray().size();i++)
            System.out.println(instance.getBeaconArray().get(i).getBeacon());
    }

    /*
    Testing opening and closing track functionality
     */
    @org.junit.jupiter.api.Test
    void closeAndOpenTrackTest(){
        System.out.println("getting blocks in track ");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        instance.getGreenLine().get(3).setFailureStatus(4);
        System.out.println(instance.getGreenLine().get(3).getFailureStatus());
        instance.getGreenLine().get(3).setFailureStatus(0);
        System.out.println(instance.getGreenLine().get(3).getFailureStatus());

    }

    /*
    Testing display switch
     */
    @org.junit.jupiter.api.Test
    void displaySwitchTest() {
        System.out.println("getting blocks in track ");
       // String filepath ="C:\\Users\\grhen\\OneDrive\\Documents\\RedGreenUpdated.csv";
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        instance.getSwitches().get(2).setSwitchState(true);
        System.out.println(instance.getSwitches().get(2).getSwitchState());
        instance.getSwitches().get(2).setSwitchState(false);
        System.out.println(instance.getSwitches().get(2).getSwitchState());
    }

    /*
    function that is grabbing all blocks in a section -- implemented to support the CTC
     */
    @org.junit.jupiter.api.Test
    void blocksInSectionTest() {
        System.out.println("getting blocks in track ");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //Red Line
        System.out.println("sectionA: " + instance.blocksInSection('A','R').toString());
        System.out.println("sectionB: " + instance.blocksInSection('B','R').toString());
        System.out.println("sectionC: " + instance.blocksInSection('C','R').toString());
        System.out.println("sectionD: " + instance.blocksInSection('D','R').toString());
        System.out.println("sectionE: " + instance.blocksInSection('E','R').toString());
        System.out.println("sectionF: " + instance.blocksInSection('F','R').toString());
        System.out.println("sectionG: " + instance.blocksInSection('G','R').toString());
        System.out.println("sectionH: " + instance.blocksInSection('H','R').toString());
        System.out.println("sectionI: " + instance.blocksInSection('I','R').toString());
        System.out.println("sectionJ: " + instance.blocksInSection('J','R').toString());
        System.out.println("sectionK: " + instance.blocksInSection('K','R').toString());
        System.out.println("sectionL: " + instance.blocksInSection('L','R').toString());
        System.out.println("sectionM: " + instance.blocksInSection('M','R').toString());
        System.out.println("sectionN: " + instance.blocksInSection('N','R').toString());
        System.out.println("sectionO: " + instance.blocksInSection('O','R').toString());
        System.out.println("sectionP: " + instance.blocksInSection('P','R').toString());
        System.out.println("sectionQ: " + instance.blocksInSection('Q','R').toString());
        System.out.println("sectionR: " + instance.blocksInSection('R','R').toString());
        System.out.println("sectionS: " + instance.blocksInSection('S','R').toString());
        System.out.println("sectionT: " + instance.blocksInSection('T','R').toString());

        //Green Line
        System.out.println("sectionA: " + instance.blocksInSection('A','G').toString());
        System.out.println("sectionB: " + instance.blocksInSection('B','G').toString());
        System.out.println("sectionC: " + instance.blocksInSection('C','G').toString());
        System.out.println("sectionD: " + instance.blocksInSection('D','G').toString());
        System.out.println("sectionE: " + instance.blocksInSection('E','G').toString());
        System.out.println("sectionF: " + instance.blocksInSection('F','G').toString());
        System.out.println("sectionG: " + instance.blocksInSection('G','G').toString());
        System.out.println("sectionH: " + instance.blocksInSection('H','G').toString());
        System.out.println("sectionI: " + instance.blocksInSection('I','G').toString());
        System.out.println("sectionJ: " + instance.blocksInSection('J','G').toString());
        System.out.println("sectionK: " + instance.blocksInSection('K','G').toString());
        System.out.println("sectionL: " + instance.blocksInSection('L','G').toString());
        System.out.println("sectionM: " + instance.blocksInSection('M','G').toString());
        System.out.println("sectionN: " + instance.blocksInSection('N','G').toString());
        System.out.println("sectionO: " + instance.blocksInSection('O','G').toString());
        System.out.println("sectionP: " + instance.blocksInSection('P','G').toString());
        System.out.println("sectionQ: " + instance.blocksInSection('Q','G').toString());
        System.out.println("sectionR: " + instance.blocksInSection('R','G').toString());
        System.out.println("sectionS: " + instance.blocksInSection('S','G').toString());
        System.out.println("sectionT: " + instance.blocksInSection('T','G').toString());
        System.out.println("sectionU: " + instance.blocksInSection('U','G').toString());
        System.out.println("sectionV: " + instance.blocksInSection('V','G').toString());
        System.out.println("sectionW: " + instance.blocksInSection('W','G').toString());
        System.out.println("sectionX: " + instance.blocksInSection('X','G').toString());
        System.out.println("sectionY: " + instance.blocksInSection('Y','G').toString());
        System.out.println("sectionZ: " + instance.blocksInSection('Z','G').toString());

    }

    /*test of UI being updated*/
    @org.junit.jupiter.api.Test
    void GUIUpdateTest() {
        System.out.println("Testing UI ");
        String filepath = "src/Track/RedGreenUpdated.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //Instantiate UI

            TrackGUI testGUI = new TrackGUI(instance);
            testGUI.setVisible(true);
            testGUI.draw();
            testGUI.latch(instance);
         //  while(true){}
          /*  instance.setFailure(2,"Green",1);
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
                instance.getGreenLine().get(2).setCommandedSpeed(15);
                testGUI.latch(instance);
                    instance.setFailure(2,"Green",1);
                testGUI.latch(instance);
                instance.setFailure(2,"Green",2);
                testGUI.latch(instance);
                i++;


            }

    */
    }



}

