import GUIInterface.AppGUIModule;

/** a runnable and stoppable thread for displaying an AppGUIModule JFrame window.
 */
public class GUIThread<T> extends Thread {

    final static int DEFAULT_JFRAME_REFRESH_RATE = 5;

    AppGUIModule<T> jFrameObject = null;
    int updateHz = DEFAULT_JFRAME_REFRESH_RATE;
    boolean running = false;

    public GUIThread(AppGUIModule<T> jFrame) {
        this.jFrameObject = jFrame;
    }

    public GUIThread(AppGUIModule<T> jFrame, int refreshHz) {
        this.jFrameObject = jFrame;
        this.updateHz = refreshHz;
    }

    @Override
    public void run() {
        running = true;
        jFrameObject.setVis(true);
        try {
            // Continually update/refresh the window
            while (running) {
                jFrameObject.update();
            }
        } catch (Exception e) {
            System.out.println("An issue with the JFrame object caused a failure with updating the window.");
            System.out.println("Ensure that the JFrame object has been .latch()ed with the correct object before launching Thread.");
            System.out.println("JFrame: " + jFrameObject);
        }
    }

    public void halt() {
        running = false;
    }
}
