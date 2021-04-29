package WaysideController;

import Track.Track;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class WaysideSystem_GreenLineTest {
    Track tracksys = new Track();
    ArrayList<TrackElement> greenLine;

    @BeforeEach
    void setup() throws Exception {
        tracksys.importTrack("Resources/RedGreenUpdated.csv");
        greenLine = tracksys.getGreenLine();
        WaysideSystem newSys = new WaysideSystem(greenLine,"Green");
    }


    @Test
    @DisplayName("Collision avoidance scripts happen")
    void collisions() {
        TrackElement blockBefore;
        TrackElement blockAfter;
        double applicationTime;
        double timeToUpdate;
        int nanosecondsPerSec = 1000000000;

        for (TrackElement element : greenLine) {
            element.setAuthority(10);
        }
        for (TrackElement element : greenLine) {
            assertTrue(element.getAuthority() > 0);
        }

        // wait for all controllers to start up
        try { TimeUnit.SECONDS.sleep(1); } catch (Exception e) { }



        /*
            One-directional track test
         */
        // adjacent blocks detect
        blockBefore = greenLine.get(2);
        blockAfter = greenLine.get(1);
        blockBefore.setOccupied(true);
        assertEquals(10,blockBefore.getAuthority());
        assertEquals(10,blockAfter.getAuthority());
        applicationTime = System.nanoTime();


        blockAfter.setOccupied(true);
        // wait for rule to be applied
        while(!(blockBefore.getAuthority()==0)) {}
        timeToUpdate = System.nanoTime() - applicationTime;
        System.out.printf("%f seconds to apply time\n",timeToUpdate/nanosecondsPerSec);

        assertEquals(0,blockBefore.getAuthority());
        System.out.println("***************Read authority:"+blockBefore.getAuthority());

        blockAfter.setOccupied(false);

        applicationTime = System.nanoTime();
        while(!(blockBefore.getAuthority()==10)) {}
        timeToUpdate = System.nanoTime() - applicationTime;
        System.out.printf("%f seconds to apply time\n",timeToUpdate/nanosecondsPerSec);


        assertEquals(10,blockBefore.getAuthority());
        System.out.println("***************Read authority:"+blockBefore.getAuthority());
        blockBefore.setOccupied(false);
        blockAfter.setOccupied(false);


        /*
            Two directional case
         */
        blockBefore = greenLine.get(13);
        TrackElement blockThis = greenLine.get(14);
        blockAfter = greenLine.get(15);
        blockThis.setOccupied(true);
        assertEquals(10,blockBefore.getAuthority());
        assertEquals(10,blockThis.getAuthority());
        assertEquals(10,blockAfter.getAuthority());
        System.out.printf("Block before (%s) occ(%b) auth=%d\n",blockBefore,blockBefore.getOccupied(),blockBefore.getAuthority());
        System.out.printf("Block [this] (%s) occ(%b) auth=%d\n",blockThis,blockThis.getOccupied(),blockThis.getAuthority());
        System.out.printf("Block after (%s) occ(%b) auth=%d\n",blockAfter,blockAfter.getOccupied(),blockAfter.getAuthority());


        applicationTime = System.nanoTime();
        blockAfter.setOccupied(true);
        // wait for rule to be applied
        while(!(blockThis.getAuthority()==0)) {}
        timeToUpdate = System.nanoTime() - applicationTime;
        System.out.printf("\n%f seconds to apply time\n",timeToUpdate/nanosecondsPerSec);
        System.out.printf("Block before (%s) occ(%b) auth=%d\n",blockBefore,blockBefore.getOccupied(),blockBefore.getAuthority());
        System.out.printf("Block [this] (%s) occ(%b) auth=%d\n",blockThis,blockThis.getOccupied(),blockThis.getAuthority());
        System.out.printf("Block after (%s) occ(%b) auth=%d\n",blockAfter,blockAfter.getOccupied(),blockAfter.getAuthority());

        applicationTime = System.nanoTime();
        blockAfter.setOccupied(false);
        // wait for rule to be applied
        while(!(blockThis.getAuthority()==10)) {}
        timeToUpdate = System.nanoTime() - applicationTime;
        System.out.printf("\n%f seconds to apply time\n",timeToUpdate/nanosecondsPerSec);
        System.out.printf("Block before (%s) occ(%b) auth=%d\n",blockBefore,blockBefore.getOccupied(),blockBefore.getAuthority());
        System.out.printf("Block [this] (%s) occ(%b) auth=%d\n",blockThis,blockThis.getOccupied(),blockThis.getAuthority());
        System.out.printf("Block after (%s) occ(%b) auth=%d\n",blockAfter,blockAfter.getOccupied(),blockAfter.getAuthority());

        applicationTime = System.nanoTime();
        blockBefore.setOccupied(true);
        // wait for rule to be applied
        while(!(blockThis.getAuthority()==0)) {}
        timeToUpdate = System.nanoTime() - applicationTime;
        System.out.printf("\n%f seconds to apply time\n",timeToUpdate/nanosecondsPerSec);
        System.out.printf("Block before (%s) occ(%b) auth=%d\n",blockBefore,blockBefore.getOccupied(),blockBefore.getAuthority());
        System.out.printf("Block [this] (%s) occ(%b) auth=%d\n",blockThis,blockThis.getOccupied(),blockThis.getAuthority());
        System.out.printf("Block after (%s) occ(%b) auth=%d\n",blockAfter,blockAfter.getOccupied(),blockAfter.getAuthority());
    }
}