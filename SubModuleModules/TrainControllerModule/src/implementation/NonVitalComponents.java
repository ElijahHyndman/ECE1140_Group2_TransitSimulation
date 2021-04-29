package implementation;

//import systemData.*;

import systemData.Advertisements;

import java.time.LocalTime;

/** NonVitalComponents holds and updates the
 * statuses of a Train's non-vital components.
 * These components are controlled automatically,
 * and take no manual input.
 *
 * ECE1140
 * Reagan Dowling
 */

public class NonVitalComponents {

    private boolean cabinLights;
    private boolean headLights;
    private String station;
    private boolean externalLights;
    private boolean rightDoors;
    private boolean leftDoors;
    private int cabinTemp;
    private Advertisements adList;
    private boolean announcementProgress;
    private String theAnnouncement;

    public NonVitalComponents(){
        //Initialize non-vital components
        cabinLights = false;
        externalLights = false;
        headLights = true; //Headlights are always true for safety
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
    public String getAnnouncement(){
        return theAnnouncement;
    }

    public int getCurrentAdvertisement(){
        return adList.playAd();
        //return 0;
    }


    //========MUTATOR METHODS==========

    //Sets the cabin temperature based on the month of the year
    public void setTemperature(){
        if (java.time.LocalDate.now().getMonthValue() <= 3 || java.time.LocalDate.now().getMonthValue() >= 10){
            cabinTemp = 68;
        }else {
            cabinTemp = 66;
        }
    }

    //if beacon set (not null), gets the station name
    public void setNextStation(String currentBeacon){
        String stationName = null;
        if (currentBeacon != null){
            stationName = currentBeacon.substring(0, currentBeacon.indexOf(" "));
        }
        station = stationName;
    }

    //sets doors based on beacon, called during openDoorAtStation
    public void setDoors(String side){
        leftDoors = false;
        rightDoors = false;

        if (side != null){
            if (side.equals("L\r")){
                leftDoors = true;
                rightDoors = false;
            }else if (side.equals("R\r")){
                leftDoors = false;
                rightDoors = true;
            }else{
                leftDoors = true;
                rightDoors = true;
            }
        }
    }

    //Setting the train's announcement
    //If have statiaon name and traian still moving, approaching staation
    //If have station name and train not oving, approaching
    public void setAnnouncement(double authority, double trainSpeed){

        if (station!=null && authority>0){
            theAnnouncement = "Approaching Station At : " + station;
        }else if(station!=null && authority==0 && trainSpeed==0){
            theAnnouncement = "Arrived at Station : " + station;
        }
    }

    //Sets the train cabin lights based on world time
    public void setCabinLights(String time){

        if (time != null){
            String theTime = time.substring(0, time.indexOf(":"));
            int timeHour = Integer.parseInt(theTime);
            if (timeHour >= 18 || timeHour < 7){
                cabinLights = true;
            }
        }else{
            cabinLights = false;
        }
    }

    //Sets the train external lights based on world time
    public void setExternalLights(String time){

        if (time != null){
            String theTime = time.substring(0, time.indexOf(":"));
            int timeHour = Integer.parseInt(theTime);
            if (timeHour >= 18 || timeHour < 7){
                externalLights = true;
            }

        }else {
            externalLights = false;
        }
    }

}
