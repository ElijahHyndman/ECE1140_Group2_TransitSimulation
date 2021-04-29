
package Track;

        import TrackConstruction.TrackElement;

        import java.util.Random;
        import java.util.concurrent.TimeUnit;

public class runTrainRed extends Thread {
    Track instance;
    public runTrainRed(Track trackList2){
        instance = trackList2;
    }

    public void run(){
        getNextRed();
    }
    void getNextRed() {
        Random rand = new Random();
        int num = Math.abs(rand.nextInt()) % 11;

        TrackElement cur = instance.getGreenLine().get(0);
        TrackElement prev = instance.getGreenLine().get(0);
        TrackElement next = null;
        instance.getSwitches().get(0).setSwitchState(true);
        instance.getSwitches().get(1).setSwitchState(true);

        instance.updateSwitches();
        int test = 0;
        System.out.println("*****");
        for (int i = 0; i < 120; i++) {
            int num2 = Math.abs(rand.nextInt()) % 15;
            next = instance.getNextRed(cur, prev);

            if(i%11 == num)
                instance.increaseTickets();

            if (next.getType().equals("STATION")){
                next.setThroughput(num2);
            }
            if(next != null) {
                next.setOccupied(true);
                //This is for the rail road crossing lights to be switched on accordingly
                if(next.getBlockNum() == 46)
                    instance.getRedLine().get(47).setLightRail(true);
                if(next.getBlockNum() == 48)
                    instance.getRedLine().get(47).setLightRail(false);
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(next != null)
                next.setOccupied(false);

            //For switches here setting them to route around the track
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




}

