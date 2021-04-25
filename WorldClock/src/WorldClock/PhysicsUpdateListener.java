package WorldClock;

public interface PhysicsUpdateListener {
    /** All physics based classes must implement updatePhysics call. In this function, they must handle any physics update
     *  which need to occur. These physics updates will be called by the WorldClock on regular intervals.
     *  The content of the implementer's function MUST be as short as possible.
     *  NO System.out.println() statements are allowed within updatePhysics (too slow).
     *
     * @param currentTimeString String, String format of date in format of HH:mm:ss, used for displaying world time
     * @param deltaTime_inSeconds double, number of seconds which have passed since the last physics update was called
     */
    void updatePhysics(String currentTimeString, double deltaTime_inSeconds);
}
