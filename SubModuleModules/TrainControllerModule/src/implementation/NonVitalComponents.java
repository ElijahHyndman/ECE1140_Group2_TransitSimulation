package implementation;

import systemData.*;

import java.time.LocalTime;

public class NonVitalComponents {

    private SystemTime theTime;
    private boolean cabinLights;
    private boolean headLights;
    private String station;
    private boolean externalLights;
    private boolean rightDoors;
    private boolean leftDoors;
    private int cabinTemp;
    private trackData track;
    private Advertisements adList;
    private boolean announcementProgress;
    private String theAnnouncement;


    public NonVitalComponents(trackData theTrack){

        theTime = new SystemTime();
        track = theTrack;

        //Initialize non-vital components
        cabinLights = false;
        externalLights = false;
        headLights = true;
        rightDoors = false;
        leftDoors = false;
        cabinTemp = 68;
        station = null;
        announcementProgress = false;
        theAnnouncement = null;
        adList = new Advertisements();


    }

    //=======ACCESSOR METHODS========

    public String getNextStation(){
        return station;
    }

    //Returns right door status, true is open, false is closed
    public boolean getRightDoors(){
        return rightDoors;
    }

    //Returns left door status, true is open, false is closed
    public boolean getLeftDoors(){
        return leftDoors;
    }

    //Returns cabin temperature in fahrenheit
    public int getTemperature(){
        return cabinTemp;
    }

    //Returns cabin light status, true is on, false is off
    public boolean getCabinLights(){

        return cabinLights;
    }

    //Returns external light status, true is on, false is off
    public boolean getExternalLights(){
        return externalLights;
    }

    public boolean getHeadLights(){
        return headLights;
    }

    //Returns whether an announcement is in progress or not
    public boolean announcementInProgress(){
        return announcementProgress;
    }

    public int getCurrentAdvertisement(){
        return adList.playAd();
    }


    //========MUTATOR METHODS==========

    public void setTemperature(){
        if (theTime.getDate().getMonthValue() <= 3 || theTime.getDate().getMonthValue() >= 10){
            cabinTemp = 68;
        }else {
            cabinTemp = 66;
        }
    }

    public void setNextStation(String currentBeacon){
        String stationName = null;
        if (currentBeacon != null){
            stationName = currentBeacon.substring(0, currentBeacon.indexOf(" "));
        }
        station = stationName;
    }

    public void setDoors(String currentBeacon){
        String doors = null;
        if (currentBeacon != null){
            String stationName = currentBeacon.substring(0, currentBeacon.indexOf(" "));
            trackData trackData = new trackData("Blue");
            doors = trackData.getStationSide(stationName);
        }
        if (doors != null){
            if (doors.equals("Left")){
                leftDoors = true;
                rightDoors = false;
            }else if(doors.equals("Right")){
                rightDoors = true;
                leftDoors = false;
            }
        } else{
            rightDoors = false;
            leftDoors = false;
        }
    }

    public void setAnnouncement(double authority, String beacon){
        String stationName = null;
        if (authority < 200 && authority > 0){
            if (beacon != null){
                String station = beacon.substring(0, beacon.indexOf(" "));
                stationName = station;
                theAnnouncement = "Approaching " + stationName;
                announcementProgress = true;
            }else{
                theAnnouncement = null;
                announcementProgress = false;
            }
        }else if (authority == 0 && beacon !=null){
            String station = beacon.substring(0, beacon.indexOf(" "));
            stationName = station;
            theAnnouncement = "Arrived at " + stationName;
            announcementProgress = true;
        }else{
            announcementProgress = false;
            theAnnouncement = null;
        }
    }

    public void setCabinLights(){

        if (theTime.getTime().getHour() >= 18 || theTime.getTime().getHour() < 7){
            cabinLights = true;
        } else {
            cabinLights = false;
        }
    }

    public void setExternalLights(){

        if (theTime.getTime().getHour() >= 18 || theTime.getTime().getHour() < 7){
            externalLights = true;
        } else {
            externalLights = false;
        }
    }

}
