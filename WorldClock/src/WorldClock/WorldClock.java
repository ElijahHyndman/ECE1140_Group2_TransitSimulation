package WorldClock;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Vector;

import SimulationEnvironment.*;

public class WorldClock extends Thread {
    /**
     * Class for handling synchronization of Simulation-World time events (physics updates, etc).
     * @author Elijah
     *
     * Definitions:
     * - World Second: a single second of the Simulation World's Time
     * - Real Second: a single second from the real world
     *
     * Main Ideas:
     * -World Time is Typically faster than Real Time
     * @ergo World Seconds are typically shorter than real seconds
     *  @ergo many World Seconds occur per-each Real Second
     *      @ergo member "ratio" is typically greater than 1.0
     * -"resolution" does not affect how much faster the Simulation World is than the real world
     * @ergo when you want to speed up the simulation, you don't need to change resolution
     * -"resolution" affects the amount of World Time that passes before another physics update happens
     * -both "resolution" and "ratio", while changing independent things about the simulation-world timing, are both
     *      used to calculate real-world timing (real world frequency)
     * @further "resolution" and "ratio" together determine the Real Seconds we wait until the next update
     * -The clock can only update so fast
     * @ergo don't set the "ratio" and "resolution" too high
     */
    /**
     * Example settings:
     * resolution:
     *      In the simulation world....
     *      1.0: physics get updated once per simulation second
     *      2.0: physics get updated twice per simulated second
     *      50.0: physics get updated fifty-times per simulated second (excessive)
     *      0.5: physics get updated one per two simulation seconds
     *      0.3: physics get updated about once per three simulation seconds
     *      0.0166: (i.e. 1/60) physics get updated about once per every simulation minute
     *
     * ratio:
     *      For every one-second in the real world...
     *      1.0: one simulation-second also passes
     *      2.0: two simulation-seconds pass
     *      10.0: ten simulation-seconds pass
     *      0.1: a tenth of a simulation-second passes
     */

    /**
     * Class Members:
     * @member resolution   number of updates per Simulation Second
     * @member ratio        number of World Seconds per Real Second (A sped up simulation means more World Seconds per each Real Second)
     * @member ticking      tracks whether the clock is ticking, used to halt clock from thread
     *
     * Derived Members:
     * updatesPerRealSecond: number of physics-updates that must occur per an actual second, derived
     * realSecondsPerUpdate: fraction of a real second which a single physics-update's period (1/frequency) will occupy
     * worldSecodnsPerUpdate: fraction of a simulation second which a single physics-update's period will occupy
     * milliseconds: number of real milliseconds that a physics-update will occupy; determines how long we wait until the next update
     * microseconds: number of real microseconds that a physics-update will occupy
     *                  Decided to be slightly less than = to microseconds*1000, which means waiting a shorter
     *                  time, which allows us to account for system calls and other delays when waiting
     */
    // Clock-Pace Variables
    private final double MAX_ALLOWABLE_RESOLUTION = 50.000;
    private final double MIN_ALLOWABLE_RESOLUTION = 0.001;
    private final double MAX_ALLOWABLE_RATIO = 20.000;
    private final double MIN_ALLOWABLE_RATIO = 00.001;
    private final double DEFAULT_RESOLUTION = 10.000;
    private final double DEFAULT_RATIO = 01.000;
    private final int QUICK_ADVANCE_PERIODS = 1000;

    private double resolution = DEFAULT_RESOLUTION;
    private double ratio = DEFAULT_RATIO;

    private double updatesPerRealSecond;
    private double realSecondsPerUpdate;
    private double worldSecondsPerUpdate;
    private int milliseconds;
    private int microseconds;

    private boolean ticking = false;

    /**
     * Class Clock Members:
     * @member hourFormat                   date format that turns date into formatted string of hour, minutes, seconds
     * @member currentTimeInMilliseconds    current time in Simulation World in milliseconds, used for setting date using "Date.setTime(int milliseconds)"
     * @member currentDate                  date object used for tracking the time, used by hourFormat to generate Time String
     *
     *
     * @assert: Date is set at January 1, 1970 00:00:00 GMT, will not affect calculations since we only show date as hours.
     * @assert: Date defined at 0 millisec corresponds to 19:00:00 for some reason, therefore getting time 00:00:00 is five-hours of milliseconds after 19:00:00
     */
    // Clock-Time variables
    // Assert:
    private final int SECOND_MILLISECONDS = 1000;
    private final int MINUTE_MILLISECONDS = 60000;
    private final int HOUR_MILLISECONDS = 3600000;
    private final int TIME_ZERO = 5 * HOUR_MILLISECONDS;

    SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
    private int currentTimeInMilliseconds = TIME_ZERO;
    Date currentDate = new Date(currentTimeInMilliseconds);

    /** Physics Members
     * @member listeners Vector<PhysicsUpdateListener>, vector of all physics items in the world which shall have updatePhysics()
     *          called on in regular intervals
     */
    Vector<PhysicsUpdateListener> listeners = new Vector<PhysicsUpdateListener>();

    // Useful variables
    volatile public boolean flag;
    private boolean tick = false;


    public WorldClock() {
        // User has opted for keeping default ratio and resolution
        configure();
    }


    public WorldClock(double resolution, double ratio) {
        // assert: default ratio will be used when setting resolution, then correct ratio will be used when setting ratio
        //  (both resolution and ratio call configure() upon completion)
        setResolution(resolution);
        setRatio(ratio);
    }


    public void configure() {
        /**recalculates configuration-parameters for how often to update, used when a parameter value has changed.
         */
        worldSecondsPerUpdate       = 1.0 / resolution;                         // 1/(U/WS) = WS/U
        updatesPerRealSecond        = resolution * ratio;                       // U/WS * WS/RS = U/RS
        realSecondsPerUpdate        = 1.0 / updatesPerRealSecond;               // 1/(U/RS) = RS/U
        milliseconds                = (int) (1000 * realSecondsPerUpdate);      // RS/U * (1000 Milliseconds / 1 sec) = mRS/U
        microseconds                = milliseconds * 1000 - (int) (0.5 * 1000); // allows us to wait half a millisecond less than intended
    }


    public boolean setResolution(double resolution) {
        /**sets the resolution for the clock as physics-updates per Simulation-World Second.
         * Shall filter for maximum resolution (MAX_ALLOWABLE_RESOLUTION) and minimum resolution (1)
         *
         * @param resolution number of physics-updates per each Simulation-World Second
         * @return boolean operation successfully set to desired resolution / resolution was illegal, using closest default
         */
        if (resolution < MAX_ALLOWABLE_RESOLUTION) {
            if (resolution > 0) {
                // Set within appropriate resolution bounds
                this.resolution = resolution;
                configure();
                return true;
            } else {
                this.resolution = 1;
            }
        } else {
            this.resolution = MAX_ALLOWABLE_RESOLUTION;
        }
        // Attempted to set an illegal resolution
        configure();
        return false;
    }


    public boolean setRatio(double ratio) {
        /**sets the ratio of Simulation-World Seconds which will occur per each Real-World Second, a faster simulation speed
         * means more Simulation-World Seconds per each Real-World Second.
         * Shall filter for maximum speed (MAX_ALLOWABLE_RATIO) and minimum speed (MIN_ALLOWABLE_RATIO)
         *
         * @param ratio number of Simulation-World Seconds which occur per Real-World Second
         * @return boolean operation successfully set to desired ratio / ratio was illegal, using closest default
         */
        if (ratio <= MAX_ALLOWABLE_RATIO) {
            if (ratio >= MIN_ALLOWABLE_RATIO) {
                // Set within appropriate ratio bounds
                this.ratio = ratio;
                configure();
                return true;
            } else {
                this.ratio = MIN_ALLOWABLE_RATIO;
            }
        } else {
            this.ratio = MAX_ALLOWABLE_RATIO;
        }
        // Attempted to set an illegal ratio
        configure();
        return false;
    }


    @Override
    public void run() {
        /**runs the World Clock, calls appropriate update function on physics objects.
         * Shall run on a new thread, so while(true) loop does not affect program execution
         *
         * @before time is not advancing, clock is still
         * @before if time is already advancing, nothing new happens
         * @after time is advancing, clock is moving, physics update-calls are broadcast
         */
        System.out.println("Clock has started ticking");
        ticking = true;
        while (ticking) {
                flag = true;
                // update Physics
                updateAllPhysics();
                // Traverse for period once per loop
                once();
        }
    }


    public void halt() {
        /**halts the ticking of the clock.
         * halt shall affect the while loop in run(), this function is called by the user who exists on another thread.
         * The user updates the "ticking" member from their thread, which affects the loop execution on the clock's thread.
         *
         * @before clock may or may not be running.
         * @after clock is definitely not running
         */
        ticking = false;
        System.out.println("Clock has halted ticking");
    }


    public void once() {
        /**traverses one period of clock-tick, useful for testing. Updates current time.
         * We intend for a period to last n milliseconds, but we implement it in microseconds to allow
         *  us to wait n-0.5 milliseconds, to allow for system calls and other time consuming things
         * Raises the update-indication flag
         *
         * Waits "milliseconds" real seconds, which traverses "worldSecondsPerUpdate" world seconds
         * @before nothing
         * @after exactly one period ("milliseconds" milliseconds of real time) has been waited
         * @after an update has occurred
         * @after the update-indication flag has been set
         *          - if flag was lowered before this update, then user can determine that time-update has occurred
         */
        try {
            // Traverse one period
            TimeUnit.MICROSECONDS.sleep(microseconds);

            // Update current time
            currentTimeInMilliseconds += 1000 * worldSecondsPerUpdate;
            currentDate.setTime(currentTimeInMilliseconds);

            // Lift indication flag
            //setFlag();
            //flag = true;

            if(tick) System.out.println("tick, flag is %b".formatted(flag));
        } catch (Exception e) {
            System.out.println("Clock failed while advancing a single period");
            e.printStackTrace();
        }
    }


    public void updateAllPhysics() {
        for (PhysicsUpdateListener listener : listeners) {
            listener.updatePhysics(getTime(), microseconds*1000);
        }
    }

    public boolean addListener(PhysicsUpdateListener physicsItem) {
        try {
            listeners.add(physicsItem);
            return true;
        } catch (Exception e) {
        }
        return false;
    }


    public String getConfiguration() {
        /**generates debugging string about the current configurations of the World Clock attributes
         *
         * @return String formatted string filled with most relevant configuration values, plain english
         */
        String reportString =   "resolution %.1f updates/WS\n".formatted(resolution) +
                                "ratio %.1f WS/RS \n".formatted(ratio) +
                                "%.1f updates per RS \n".formatted(updatesPerRealSecond)  +
                                "%d milliseconds per update".formatted(milliseconds) ;
        return reportString;
    }

    public void setFlag() {
        flag = true;
    }
    public void lowerFlag() {
        flag = false;
    }
    public boolean getFlag() {
        //System.out.printf(flag ? "t" : "");
        return flag;
    }
    public void setTickingDebug(boolean willTick) {
        tick = willTick;
    }
    public String getTime() {
        return hourFormat.format(currentDate);
    }
    public double getResolution() {
        return resolution;
    }
    public double getRatio() {
        return ratio;
    }
    public int getMilliseconds() {
        return milliseconds;
    }
    public double getSeconds() {
        return realSecondsPerUpdate;
    }
}
