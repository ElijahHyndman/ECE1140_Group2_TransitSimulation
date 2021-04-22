import GUIInterface.AppGUIModule;

public class GUIWindowLauncher<T> extends Thread {
    AppGUIModule<T> gui;

    public void launchWindow(AppGUIModule<T> jframe) {
        gui = jframe;
        start();
    }

    public void run() {
        if (gui == null)
            return;
        while(true) {
            gui.update();
        }
    }
}
