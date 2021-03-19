package systemData;

public class trackData {

    public String name;
    public trackData(String trackName){
        name = trackName;
    }

    //For iteration #2
    public String getStationSide(String station){
        if (station.equals("StationB")){
            return "Right";
        }else if(station.equals("StationC")) {
            return "Right";
        }
        return null;
    }

    public static double getDistance(double stationBlock){
        double distance = 0;
        distance = stationBlock * 50;

        /*
        if (stationBlock == 10){
            distance = 500;
        }else if (stationBlock == 15){
            distance = 500;
        }
         */

        return distance;
    }
}
