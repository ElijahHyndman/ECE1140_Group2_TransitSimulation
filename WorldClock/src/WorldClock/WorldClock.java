package WorldClock;
import java.util.concurrent.TimeUnit;

public class WorldClock extends Thread {
    /**
     * Class for handling synchronization of Simulation-World time events (physics updates, etc).
     * @author Elijah
     *
     * Definitions:
     * - World Second: a single second of the Simulation World's Time
     * - Real Second: a single second from the real world
     */

    /**
     * Class Members
     * @member resolution   number of updates per Simulation Second
     * @member ratio        number of World Seconds per Real Second (A sped up simulation means more World Seconds per each Real Second)
     * @member ticking      tracks whether the clock is ticking, used to halt clock from thread
     *
     * Derived Members
     * updatesPerWorldSecond: number of updates per World Second to obtain appropriate physics resolution within simulation time
     * updatesPerRealSecond: number of updates that must occur per an actual second, derived
     */
    private final double MAX_ALLOWABLE_RESOLUTION = 100.000;
    private final double MAX_ALLOWABLE_RATIO = 20.000;
    private final double MIN_ALLOWABLE_RATIO = 00.001;
    private final double DEFAULT_RESOLUTION = 10.000;
    private final double DEFAULT_RATIO = 01.000;
    private final int QUICK_ADVANCE_PERIODS = 1000;

    private double resolution = DEFAULT_RESOLUTION;
    private double ratio = DEFAULT_RATIO;

    private double updatesPerRealSecond;
    private double realSecondsPerUpdate;
    private int milliseconds;
    private int microseconds;

    private boolean ticking = false;


    public WorldClock() {
        // User has opted for default ratio and resolution
        configure();
    }


    public WorldClock(double resolution, double ratio) {
        // assert: default ratio will be used when setting resolution, then correct ratio will be used when setting ratio
        //  (both resolution and ratio call configure() upon completion)
        setResolution(resolution);
        setRatio(ratio);
    }


    public void configure() {
        /**
         * recalculates configuration-parameters for how often to update, used when a parameter value has changed.
         */
        updatesPerRealSecond        = resolution * ratio;                       // U/WS * WS/RS = U/RS
        realSecondsPerUpdate        = 1.0 / updatesPerRealSecond;               // 1/(U/RS) = RS/U
        milliseconds                = (int) (1000 * realSecondsPerUpdate);      // RS/U * (1000 Milliseconds / 1 sec) = mRS/U
        microseconds                = milliseconds * 1000 - 500;                // allows us to wait half a millisecond less than intended
    }


    public boolean setResolution(double resolution) {
        /**
         * Sets the resolution for the clock as physics-updates per Simulation-World Second.
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
        /**
         * Sets the ratio of Simulation-World Seconds which will occur per each Real-World Second, a faster simulation speed
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
        /**
         * Runs the World Clock, calls appropriate update function on physics objects.
         * Shall run on a new thread, so while(true) loop does not affect program execution
         *
         * @before time is not advancing, clock is still
         * @after time is advancing, clock is moving, physics update-calls are broadcast
         */
        System.out.println("Clock has started ticking");
        ticking = true;
        while (ticking) {
                // Traverse for period once per loop
                once();
        }
    }


    public void once() {
        /**
         * Traverses one period of clock-tick, useful for testing.
         *
         * @before nothing
         * @after exactly one period ("milliseconds" milliseconds of real time) has been waited
         */
        try {
            TimeUnit.MICROSECONDS.sleep(microseconds);
            //TimeUnit.MILLISECONDS.sleep(milliseconds);
            //System.out.println("tick");
        } catch (Exception e) {
            System.out.println("Clock failed while advancing a single period");
            e.printStackTrace();
        }
    }


    public void halt() {
        /**
         * Halts the ticking of the clock.
         * halt shall affect the while loop in run(), this function is called by the user who exists on another thread.
         * The user updates the "ticking" member from their thread, which affects the loop execution on the clock's thread.
         *
         * @before clock may or may not be running.
         * @after clock is definitely not ticking
         */
        ticking = false;
        System.out.println("Clock has halted ticking");
    }


    public String getConfiguration() {
        /**
         * Generates debugging string about the current configurations of the World Clock attributes
         */
        String reportString =   "resolution %.1f updates/WS\n".formatted(resolution) +
                                "ratio %.1f WS/RS \n".formatted(ratio) +
                                "%.1f updates per RS \n".formatted(updatesPerRealSecond)  +
                                "%d milliseconds per update".formatted(milliseconds) ;
        return reportString;
    }

    public double getResolution() {return resolution;}
    public double getRatio() {return ratio;}
    public int getMilliseconds() {return milliseconds;}
    public double getSeconds() {return realSecondsPerUpdate;}
}
