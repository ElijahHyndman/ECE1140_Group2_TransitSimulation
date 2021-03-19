package implementation;

import java.time.LocalDate;
import java.time.LocalTime;


public class SystemTime {

    //Returns the current local time
    public LocalTime getTime(){
        return java.time.LocalTime.now();
    }

    //Returns the current local date
    public LocalDate getDate(){
        return java.time.LocalDate.now();
    }

}
