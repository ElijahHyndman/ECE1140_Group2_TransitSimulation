package DefaultPLCEngines;

import Track.Track;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockCollisionPLCEngineTest {
    Track sys;
    TrackElement testBlock;

    @BeforeEach
    public void setUp() {
        sys = new Track();
        // Load red and green tracks
        sys.importTrack("Resources/RedGreenUpdated.csv");
    }

    /*
        Adjacency Tests
     */
    @Test
    @DisplayName("Adjacency detected ")
    public void adjacency() {
        for (int i=0; i<150; i++) {
            testBlock = sys.getBlock(i);
            BlockCollisionPLCEngine.getAdjacentTrackElements(testBlock);
        }
    }

}