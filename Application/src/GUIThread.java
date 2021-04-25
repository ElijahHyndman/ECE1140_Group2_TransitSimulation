import GUIInterface.AppGUIModule;

import java.util.concurrent.TimeUnit;

/** a runnable and stoppable thread for displaying an AppGUIModule JFrame window.
 */
public class GUIThread<T> extends Thread {

    final static int DEFAULT_JFRAME_REFRESH_RATE = 5;
    final static int MAXIMUM_REFRESH_RATE = 10;

    AppGUIModule<T> jFrameObject = null;
    private double updateHz = DEFAULT_JFRAME_REFRESH_RATE;
    private int updatePeriodMilliseconds = (int) Math.ceil( (1 / updateHz) * 1000 );
    private boolean running = false;

    public GUIThread(AppGUIModule<T> jFrame) {
        this.jFrameObject = jFrame;
        this.updateHz = DEFAULT_JFRAME_REFRESH_RATE;
    }

    public GUIThread(AppGUIModule<T> jFrame, double refreshHz) {
        this.jFrameObject = jFrame;
        if (refreshHz > MAXIMUM_REFRESH_RATE)
            this.updateHz = MAXIMUM_REFRESH_RATE;
        else
            this.updateHz = refreshHz;
        updatePeriodMilliseconds = (int) Math.ceil( (1 / updateHz) * 1000 );
    }

    @Override
    public void run() {
        running = true;
        jFrameObject.setVis(true);
        try {
            // Continually update/refresh the window
            while (running) {
                jFrameObject.update();
                TimeUnit.MILLISECONDS.sleep(updatePeriodMilliseconds);
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
