package Track;

import static org.junit.jupiter.api.Assertions.*;

class TrackTest {

    @org.junit.jupiter.api.Test
    void importTrack() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        boolean expResult = true;
        boolean result = instance.importTrack(filepath);
        assertEquals(expResult, result);
    }

    @org.junit.jupiter.api.Test
    void getGreenLine() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        boolean expResult = true;
        instance.importTrack(filepath);
        System.out.println(instance.getGreenLine());
    }

    @org.junit.jupiter.api.Test
    void getBlock() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
        System.out.println(instance.getBlock(0));
        for(int i = 1; i < 77; i++) {
            assertEquals(instance.getBlock(i).getBlockNum(), i);
           assertEquals(instance.getBlock(i).getLine(), "Red");
       }
        for(int i = 77; i < 227 ; i++) {
            assertEquals(instance.getBlock(i).getBlockNum(), i-76);
            assertEquals(instance.getBlock(i).getLine(), "Green");
        }

    }

    @org.junit.jupiter.api.Test
    void getBlockLine() {
        System.out.println("importTrack");
        String filepath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);
       System.out.println(instance.getBlockLine(0, "Red"));
       for(int i = 1; i < 76; i++) {
            assertEquals(instance.getBlockLine(i,"Red").getBlockNum(), i);
            assertEquals(instance.getBlock(i).getLine(), "Red");
        }
      System.out.println(instance.getBlockLine(0, "Green"));
        for(int i = 1; i < 150 ; i++) {
            assertEquals(instance.getBlockLine(i,"Green").getBlockNum(), i);
            assertEquals(instance.getBlockLine(i, "Green").getLine(), "Green");
        }
    }

    @org.junit.jupiter.api.Test
    void getTrackHeaterStatus() {
    }

    @org.junit.jupiter.api.Test
    void setEnvironmentalTemperature() {
    }

    @org.junit.jupiter.api.Test
    void getEnvironmentalTemperature() {
    }

    @org.junit.jupiter.api.Test
    void getSize() {
    }

    @org.junit.jupiter.api.Test
    void setFailure() {
    }

    @org.junit.jupiter.api.Test
    void validFile() {
        String filePath = "THISISINVALID";
        Track instance = new Track();
        boolean expResult = false;
        boolean result = instance.validFile(filePath);
        assertEquals(expResult, result);
        filePath = "C:\\Users\\grhen\\OneDrive\\Documents\\Test.csv";
        expResult = true;
        result = instance.validFile(filePath);
        assertEquals(expResult,result);
    }
}