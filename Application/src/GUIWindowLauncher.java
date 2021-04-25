import GUIInterface.AppGUIModule;

public class GUIWindowLauncher extends Thread {

    /** directly launches a given jframe window onto a new thread for displaying.
     * @param jframe, must implement AppGUIModule interface, the Java JFrame class which an instance shall be launched of
     * @return Runnable, a thread which the GUI is now running on. Allows the user to close or end thread from their code
     * @before Java JFrame object and has been created, everything has been instaniated inside Jframe (for instance, jframe has been .latch()ed onto the correct object beforehand)
     * @after Java JFrame now exists on a new thread, .update() is being invoked constantly
     */
    public static <T> GUIThread launchWindow(AppGUIModule<T> jframe) throws Exception {
        if (jframe == null) {
            throw new Exception("Null JFrame object passed to GUIWindowLauncher.launchWindow.\n Object: " + jframe);
        }
        // Generate the new Thread
        GUIThread runnableThread = null;
        try { runnableThread = generateThread(jframe); } catch (Exception e) { return null; }

        // Run the thread immediately
        try { runnableThread.start(); } catch (Exception e) { return null; }
        return runnableThread;
    }

    /** directly launches a given jframe window onto a new thread for displaying, updating the window at a specified refresh rate (given as Hz of type double).
     * @param jframe, must implement AppGUIModule interface, the Java JFrame class which an instance shall be launched of
     * @return Runnable, a thread which the GUI is now running on. Allows the user to close or end thread from their code
     * @before Java JFrame object and has been created, everything has been instaniated inside Jframe (for instance, jframe has been .latch()ed onto the correct object beforehand)
     * @after Java JFrame now exists on a new thread, .update() is being invoked constantly
     */
    public static <T> GUIThread launchWindowWithRate(AppGUIModule<T> jframe, double updateHz) throws Exception {
        if (jframe == null) {
            throw new Exception("Null JFrame object passed to GUIWindowLauncher.launchWindow.\n Object: " + jframe);
        }
        // Generate the new Thread
        GUIThread runnableThread = null;
        try { runnableThread = generateThreadWithRate(jframe, updateHz); } catch (Exception e) { return null; }

        // Run the thread immediately
        try { runnableThread.start(); } catch (Exception e) { return null; }
        return runnableThread;
    }

    /** creates a runnable GUIThread (Thread) object from a given AppGUIModule (JFrame) object without running the thread immediately.
     * @param jframe
     * @param <T>
     * @return
     */
    public static <T> GUIThread generateThread(AppGUIModule<T> jframe) throws Exception {
        if (jframe == null) {
            throw new Exception("Null JFrame object passed to GUIWindowLauncher.generateThread.\n Object: " + jframe);
        }
        GUIThread runnableThread = null;
        try {
           runnableThread = new GUIThread(jframe);
        } catch (Exception e) {
            System.out.println("Failure to create new GUIThread object from given jframe object");
            throw new Exception("GUIThread Spawning error for given object (AppGUIModule jFrame): " + jframe);
        }
        return runnableThread;
    }

    /** creates a runnable GUIThread (Thread) object from a given AppGUIModule (JFrame) object without running the thread immediately.
     * @param jframe
     * @param <T>
     * @return
     */
    public static <T> GUIThread generateThreadWithRate(AppGUIModule<T> jframe, double refreshHz) throws Exception {
        if (jframe == null) {
            throw new Exception("Null JFrame object passed to GUIWindowLauncher.generateThread.\n Object: " + jframe);
        }
        GUIThread runnableThread = null;
        try {
            runnableThread = new GUIThread(jframe,refreshHz);
        } catch (Exception e) {
            System.out.println("Failure to create new GUIThread object from given jframe object");
            throw new Exception("GUIThread Spawning error for given object (AppGUIModule jFrame): " + jframe);
        }
        return runnableThread;
    }
}
