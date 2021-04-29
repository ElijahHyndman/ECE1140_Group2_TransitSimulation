package WaysideController;

import Track.Track;
import TrackConstruction.TrackElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaysideSystem_Information_andStaticFunctions_Test {

    @Test
    @DisplayName("Correct Wayside System Index / Given Names")
    void givenNameTests() throws IOException {
        int createNSystems = 10;
        System.out.printf("The current number of Wayside Systems is %d\n",WaysideSystem.NUMBER_WAYSIDE_SYSTEMS);
        System.out.printf("Creating %d Wayside Systems\n",createNSystems);

        String expectedNameSignature = "Wayside System #%d";
        for (int i=1; i<=createNSystems; i++) {
            WaysideSystem newWS = new WaysideSystem();
            assertTrue(String.format(expectedNameSignature,i).equals(newWS.getIdentificationName()));
            System.out.printf("%d: (name) %s\n",i,newWS.getIdentificationName());
        }

        String name;
        System.out.println("Creating two more Wayside Systems manually");
        System.out.printf("new: %s\n",new WaysideSystem().getIdentificationName());
        System.out.printf("new: %s\n", new WaysideSystem().getIdentificationName());
    }


    // Testing out utility function for Haleigh
    public static WaysideSystem getWaysideSystem(String sectionName, ArrayList<WaysideSystem> WS) throws IOException {
        WaysideSystem proxy = new WaysideSystem(sectionName);
        int ind =  WS.indexOf(proxy);
        System.out.printf("System with name (%s) exists at index %d\n",sectionName,ind);
        if (ind == -1)
            return null;
        return WS.get(ind);
    }

    @Test
    @DisplayName("Testing Aliases")
    void aliasesWork() throws IOException {
        ArrayList<WaysideSystem> WS = new ArrayList<WaysideSystem>();
        ArrayList<String> names = new ArrayList<>() {
            {
                add("Green");
                add("Red");
                add("Blue");
                add("Nickelby");
                add("Cathy");
                add("Charlie");
            }
        };
        for (String name : names) {
            WaysideSystem ws = new WaysideSystem(name);
            WS.add( ws );
            System.out.printf("Creating System %s and given it section name (%s)\n", ws.getIdentificationName(),ws.getLineName());
        }
        for (String name : names) {
            WaysideSystem found = getWaysideSystem(name, WS);
            assertNotNull(found);
            System.out.printf("Found System %s using name (%s)\n",found.getIdentificationName(),name);
        }
    }

    /*
            partitioning test
     */

    @Test
    @DisplayName("Partitioning test passes (integers)")
    void partitionIntegers() {
        ArrayList<Integer> ints = new ArrayList<>() {
            {
                add(10);
                add(20);
                add(30);
                add(40);
                add(50);
                add(60);
                add(70);
                add(80);
                add(90);
                add(100);
            }
        };

        List<ArrayList<Integer>> by2 = new ArrayList<>() {
            {
                add(new ArrayList<Integer>(Arrays.asList(10,20,30,40,50)));
                add(new ArrayList<Integer>(Arrays.asList(60,70,80,90,100)));
            }
        };
        List<ArrayList<Integer>> by3 = new ArrayList<>() {
            {
                add(new ArrayList<Integer>(Arrays.asList(10,20,30,40)));
                add(new ArrayList<Integer>(Arrays.asList(50,60,70,80)));
                add(new ArrayList<Integer>(Arrays.asList(90,100)));
            }
        };
        List<ArrayList<Integer>> by4 = new ArrayList<>() {
            {
                add(new ArrayList<Integer>(Arrays.asList(10,20,30)));
                add(new ArrayList<Integer>(Arrays.asList(40,50,60)));
                add(new ArrayList<Integer>(Arrays.asList(70,80,90)));
                add(new ArrayList<Integer>(Arrays.asList(100)));
            }
        };
        List<ArrayList<Integer>> by13 = new ArrayList<>() {
            {
                add(new ArrayList<Integer>(Arrays.asList(10)));
                add(new ArrayList<Integer>(Arrays.asList(20)));
                add(new ArrayList<Integer>(Arrays.asList(30)));
                add(new ArrayList<Integer>(Arrays.asList(40)));
                add(new ArrayList<Integer>(Arrays.asList(50)));
                add(new ArrayList<Integer>(Arrays.asList(60)));
                add(new ArrayList<Integer>(Arrays.asList(70)));
                add(new ArrayList<Integer>(Arrays.asList(80)));
                add(new ArrayList<Integer>(Arrays.asList(90)));
                add(new ArrayList<Integer>(Arrays.asList(100)));
                add(new ArrayList<Integer>(Arrays.asList()));
                add(new ArrayList<Integer>(Arrays.asList()));
                add(new ArrayList<Integer>(Arrays.asList()));
            }
        };
        assertEquals(by2, WaysideSystem.partitionArrayList(ints,2));
        assertEquals(by3, WaysideSystem.partitionArrayList(ints,3));
        assertEquals(by4, WaysideSystem.partitionArrayList(ints,4));
        assertEquals(by13, WaysideSystem.partitionArrayList(ints,13));
        WaysideSystem.partitionArrayList(ints,0);
    }

    @Test
    @DisplayName("Partition test passes (TrackElements)")
    void partitionBlocks() {
        Track sys = new Track();
        //sys.importTrack("Resources/RedGreenUpdated.csv");
        //ArrayList<TrackElement> greenline = sys.getGreenLine();
        //ArrayList<ArrayList<TrackElement>>subsets = WaysideSystem.partitionArrayList(greenline,)
        //for (ArrayList<TrackElement> parition : subsets)
    }
}