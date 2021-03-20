import WorldClock.WorldClock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WorldClockTest {
    WorldClock clk;

    // acceptable number of milliseconds which a clk period is allowed to vary by
    int HardAccept = 3;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Clock calculates the correct wait periods from a given ratio and resolution in the constructor")
    void testParameterizedConstructor() {
        // The values of these updates and ratios have been hand calculated and hard coded to test
        // that it gives the expected results
        String predictedConfiguration;
        String actualResult;
        double resolution;
        double ratio;

        ratio = 1.0;

        resolution = 1.0;
        predictedConfiguration = generatePredictionString(1.0,1.0,1.0,1000);
        clk = new WorldClock(resolution, ratio);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        resolution = 2.0;
        predictedConfiguration = generatePredictionString(2.0,1.0,2.0,500);
        clk = new WorldClock(resolution, ratio);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        resolution = 3.0;
        predictedConfiguration = generatePredictionString(3.0,1.0,3.0,333);
        clk = new WorldClock(resolution, ratio);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        // New Ratio
        ratio = 2.0;

        resolution = 1.0;
        predictedConfiguration = generatePredictionString(1.0,2.0,2.0,500);
        clk = new WorldClock(resolution, ratio);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        resolution = 2.0;
        predictedConfiguration = generatePredictionString(2.0,2.0,4.0,250);
        clk = new WorldClock(resolution, ratio);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        resolution = 3.0;
        predictedConfiguration =    generatePredictionString(3.0,2.0,6.0,166);
        clk = new WorldClock(resolution, ratio);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));
    }

    @Test
    @DisplayName("Clock calculates the correct configurations from a given ratio or resolution from setters and default constructor")
    void testSetterAndDefaultConstructor() {
        String predictedConfiguration;
        String actualResult;

        // Check default constructor
        clk = new WorldClock();
        predictedConfiguration = generatePredictionString(10.0, 1.0, 10.0, 100);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        // Note: MIN_ALLOWABLE_RATIO
        clk.setRatio(0.001);
        predictedConfiguration = generatePredictionString(10.0,0.0,0.0,100000);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        clk.setRatio(2.0);
        predictedConfiguration = generatePredictionString(10.0,2.0,20.0,50);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        // Note: MAX_ALLOWABLE_RATIO
        clk.setRatio(20.0);
        predictedConfiguration = generatePredictionString(10.0,20.0,200.0,5);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        clk.setRatio(3.0);
        predictedConfiguration = generatePredictionString(10.0,3.0,30.0,33);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        // Remember: Ratio was last configured to 3.0
        clk.setResolution(2.0);
        predictedConfiguration = generatePredictionString(2.0,3.0,6.0,166);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        clk.setResolution(4.0);
        predictedConfiguration = generatePredictionString(4.0,3.0,12.0,83);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));

        // NOTE: Max allowable resolution
        clk.setResolution(100.0);
        predictedConfiguration = generatePredictionString(100.0,3.0,300.0,3);
        actualResult = clk.getConfiguration();
        assert (clk.getConfiguration().equals(predictedConfiguration));
    }

    @Test
    @DisplayName("One measured Clock-Period correctly matches predicted single-period")
    void correctTimePeriod() {
        int measuredTime;
        double acceptablePercentage = 0.1;

        // periods of milliseconds to test
        // We will hold resolution constant (20 per Wsecond,) adjust ratio as needed to achieve period
        int[] periods = {3,5,10,20,30,80,100,160,200,1000};
        double resolution = 20.0;
        double ratio;

        for (int period : periods) {
            // Calculate appropriate period to yield desired period in milliseconds
            ratio = 1000 / ( (double) period * resolution);
            clk = new WorldClock(resolution, ratio);
            measuredTime = measureOnePeriod();
            isAbout(measuredTime, clk.getMilliseconds(), acceptablePercentage);
        }
        //System.out.println("Total time waited: %d milliseconds".formatted(endTime-startTime));
    }


    /*

    Utility Functions

     */


    public String generatePredictionString(double resolution, double ratio, double updates, int milliSec) {
        /**
         * generates configurationString with Hard-Coded values (same format as expected from WorldClock Configuration string)
         */
        String predictedConfiguration =    "resolution %.1f updates/WS\n".formatted(resolution) +
                                            "ratio %.1f WS/RS \n".formatted(ratio) +
                                            "%.1f updates per RS \n".formatted(updates)  +
                                            "%d milliseconds per update".formatted(milliSec);
        return predictedConfiguration;
    }

    public boolean isAbout(int reality, int expected,  double criterion) {
        /**
         * assesses whether reality value is acceptably close to expected.
         *
         * @param expected the value you calculated should occur in the measurement
         * @param reality the actual value you measure from the measurement
         * @param criterion the percent of the exepcted value which the reality value is allowed to vary above/below
         *                  while still being considered acceptable
         * Note: integer arithmetic will occur during errorBound calculation
         */
        int errorBound = (int) (criterion * expected);
        int upperBound = expected + errorBound;
        int lowerBound = expected - errorBound;

        // Within percentage-acceptable range
        // Within HardAccept-acceptable range
        // not within acceptable range
        if (reality >= lowerBound && reality <= upperBound) {
            System.out.println("%dms is acceptably close to %dms, within %.0f%% (+-%d)".formatted(reality, expected, criterion * 100, errorBound));
            return true;
        } else if (reality >= expected - HardAccept && reality <= expected + HardAccept) {
            System.out.println("%dms is acceptably close to %dms, within HardAccept boundary (+-%d)".formatted(reality, expected, HardAccept));
            return true;
        } else {
            System.out.println("%dms is NOT acceptably close to %dms, within %.0f%% (+-%d)".formatted(reality, expected, criterion*100, errorBound));
            return false;
        }
    }

    public int measureOnePeriod() {
        /**
         * measures the elapsed time in one period of the clk WorldClock object.
         * @return int number of milliseconds that clk.once() has mesured to take
         * @before clk must exist and be a valid WorldClock object, should be configured with desired ratio and resolution before
         * @after clk has progressed one period
         */
        long startTime;
        long measuredTime;

        startTime = System.currentTimeMillis();
        clk.once();
        measuredTime = System.currentTimeMillis() - startTime;

        return (int) measuredTime;
    }
}