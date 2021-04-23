package TrackConstruction;

import org.junit.Test;

class SwitchTest {

    @Test
    void getDirectionStates() {
            String infrastructure = "SWITCH TO YARD (57-yard)";
            String test = infrastructure.substring(7);
            String[] dirStates;
            String[] switches;
            switches = test.split(";");
            switches[0] = switches[0].replace("(", "");
            switches[0] = switches[0].replace(")","");

            System.out.println(switches[0].substring(4));
    }
}