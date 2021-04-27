import WaysideController.*;
import WaysideGUI.*;


public class Application {
    public static void main(String[] args) {
        WaysideSystem WS = new WaysideSystem();
        WS.generateTestController();
        WS.generateTestController();

        WaysideUIClass aUI = new WaysideUIClass(WS);
        aUI.start();
    }
}
