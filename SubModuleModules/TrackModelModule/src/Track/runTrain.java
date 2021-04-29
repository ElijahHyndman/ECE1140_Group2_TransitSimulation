package Track;

import TrackConstruction.TrackElement;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class runTrain extends Thread {
    Track instance;
    public runTrain(Track trackList2){
        instance = trackList2;
    }

    public void run(){
        getNextGreen();
        /*
        Random rand = new Random();
        int num = Math.abs(rand.nextInt()) % 11 + 62;
        int num2 = Math.abs(rand.nextInt()) % 4;

        for(int i=62; i<= 73; i++ ) {
            if(i == num)
                trackList.increaseTickets();
            trackList.getGreenLine().get(i).setOccupied(true);

            if(i == 73)
                trackList.getGreenLine().get(i).setThroughput(num2);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            trackList.getGreenLine().get(i).setOccupied(false);

        }*/
    }

    void getNextGreen() {
        ;

        Random rand = new Random();
        int num = Math.abs(rand.nextInt()) % 11;



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
            int num2 = Math.abs(rand.nextInt()) % 15;
            next = instance.getNextGreen(cur, prev);

            if(i%11 == num)
                instance.increaseTickets();

            if (next.getType().equals("STATION")){
                next.setThroughput(num2);
            }
            if(next != null) {
                next.setOccupied(true);
                if(next.getBlockNum() == 18)
                    instance.getGreenLine().get(19).setLightRail(true);
                if(next.getBlockNum() == 20)
                    instance.getGreenLine().get(19).setLightRail(false);
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(next != null)
                next.setOccupied(false);
            if(i == 36) {
                System.out.println("update");
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

                break;
            }

        }
    }
}

