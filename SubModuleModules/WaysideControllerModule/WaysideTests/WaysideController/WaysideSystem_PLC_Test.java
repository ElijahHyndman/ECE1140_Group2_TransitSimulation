package WaysideController;

import Track.Track;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaysideSystem_PLC_Test {
    Track trackSystem;
    ArrayList<TrackElement> greenLine;

    @BeforeEach
    public void setup() {
        trackSystem = new Track();
        trackSystem.importTrack("Resources/RedGreenUpdated.csv");
        greenLine = trackSystem.getGreenLine();
    }
}