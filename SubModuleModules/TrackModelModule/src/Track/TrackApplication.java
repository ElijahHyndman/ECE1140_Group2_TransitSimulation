package Track;

import GUIInterface.AppGUIModule;
import TrackConstruction.TrackElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.Timer;

/**
 * AUTHOR: Grhen
 */

public class TrackApplication {

    public Track track = new Track();


    public Track getTrack(){
        return track;
    }

    public static void main(String[] args) {
        TrackApplication run = new TrackApplication();
        InputStream inputStream = TrackApplication.class.getResourceAsStream("/Track/RedGreenUpdated.csv");
        TrackGUI trackUI = new TrackGUI(run.getTrack());
        trackUI.setVisible(true);


        while (true) { }
    }
}


