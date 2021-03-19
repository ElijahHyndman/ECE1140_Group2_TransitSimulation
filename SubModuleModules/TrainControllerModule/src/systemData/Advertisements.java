package systemData;

public class Advertisements {

    private String[] ads;
    private int currentAd;

    public Advertisements() {

        ads = new String[]{"none", "PORT Authority", "State Farm", "Dunkin", "Chase Bank", "McDonalds"};
        currentAd = -1;

    }
        public int playAd() {
            getNextAd();
            return currentAd;
        }

        public void getNextAd(){
            if (currentAd <= ads.length) {
                currentAd++;
            } else {
                currentAd = 0;
            }
        }
}

