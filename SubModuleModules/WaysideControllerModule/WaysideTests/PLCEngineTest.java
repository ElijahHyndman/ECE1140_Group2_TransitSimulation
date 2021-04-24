import WaysideController.PLCEngine;
import WaysideController.PLCInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PLCEngineTest {

    String PLC1Path = "Resources/testtoken";
    String PLC2Path = "Resources/testtoken2";
    String PLC3Path = "Resources/testtoken3";

    PLCEngine engine;
    @BeforeEach
    public void setUp(){
        PLCEngine engine = new PLCEngine();
    }

    /*
        Tokenizing Tests
     */
    @Test
    @DisplayName("Testing Read Helper Function")
    public void testReader() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();

        List<String> testList = engine.stringTokensFromFile(PLC1Path);
        for(int i = 0; i < testList.size(); i++){
            System.out.println(testList.get(i));
        }

        System.out.println("Finished code.");
    }

    @Test
    @DisplayName("Testing Read Helper Function New Version")
    public void testReaderNew() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();

        List<String> testList = engine.stringTokensFromFile(PLC1Path);
        for(int i = 0; i < testList.size(); i++){
            System.out.println(testList.get(i));
        }

        System.out.println("Finished code.");
    }

    @Test
    @DisplayName("Testing String Checker Helper")
    public void testString() {
        PLCEngine engine = new PLCEngine();

        assertTrue(engine.stringIsCharactersAndNumbers("A"));
        assertTrue(engine.stringIsCharactersAndNumbers("1"));
        assertFalse(engine.stringIsCharactersAndNumbers("ABC DEF"));
        assertFalse(engine.stringIsCharactersAndNumbers("ABC:DEF"));
        assertTrue(engine.stringIsCharactersAndNumbers("ABC10231DEF"));
        assertTrue(engine.stringIsCharactersAndNumbers("ABCDEF"));
    }

    @Test
    @DisplayName("Testing Token Creator Functions")
    public void testTokens() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();
        PLCEngine engine2 = new PLCEngine();

        List<String> fileTokens = engine.stringTokensFromFile(PLC1Path);
        engine.uploadPLC(fileTokens);
        Queue<String> que = engine.getVarQueue();
        Queue<PLCEngine.Token> tok = engine.getTokenQueue();

        for(String s : que){
            System.out.println(s);
        }

        for(PLCEngine.Token t : tok){
            System.out.println(t.toString());
        }

        List<String> fileTokens2 = engine2.stringTokensFromFile(PLC2Path);
        engine2.uploadPLC(fileTokens);
        Queue<String> que2 = engine2.getVarQueue();
        Queue<PLCEngine.Token> tok2 = engine2.getTokenQueue();

        for(String s : que2){
            System.out.println(s);
        }

        for(PLCEngine.Token t : tok2){
            System.out.println(t.toString());
        }
    }


    /*
        Logic Tests
     */


    /*
    @Test
    @DisplayName("Testing the output generation")
    public void testOutput() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();

        List<String> fileTokens = engine.stringTokensFromFile(PLC1Path);
        engine.uploadPLC(fileTokens);
        Queue<String> que = engine.getVarQueue();
        Queue<PLCEngine.Token> tok = engine.getTokenQueue();

        for(String s : que){
            System.out.println(s);
        }

        for(PLCEngine.Token t : tok){
            System.out.println(t.toString());
        }

        List<String> inputNames = Arrays.asList("A", "C", "D");
        assertEquals(false, engine.calculateOutputLogic(inputNames, new boolean[]{true, false, true}));

        List<String> inputNames2 = Arrays.asList("A", "C", "D");
        assertEquals(true, engine.calculateOutputLogic(inputNames2, new boolean[]{true, true, true}));

        List<String> inputNames3 = Arrays.asList("D", "B", "C");
        Exception exception = assertThrows(IOException.class, () -> engine.calculateOutputLogic(inputNames3, new boolean[]{true, false, true}));
        assertEquals("Generate Error: Bad Input Names...", exception.getMessage());
    }
    */

    @Test
    @DisplayName("Testing the output generation")
    public void testOutputNEW() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();
        List<String> fileTokens = engine.stringTokensFromFile(PLC2Path);
        engine.uploadPLC(fileTokens);

        PLCEngine engine2 = new PLCEngine();
        List<String> fileTokens2 = engine2.stringTokensFromFile(PLC1Path);
        engine2.uploadPLC(fileTokens2);

        Queue<String> que = engine2.getVarQueue();
        Queue<PLCEngine.Token> tok = engine2.getTokenQueue();

        for(String s : que){
            System.out.println(s);
        }

        for(PLCEngine.Token t : tok){
            System.out.println(t.toString());
        }

        System.out.println();

        List<String> inputNames = Arrays.asList("A", "B", "C", "D");
        assertEquals(false, engine.evaluateLogic(inputNames, new boolean[]{false, false, false, false}));

        System.out.println();

        assertEquals(true, engine.evaluateLogic(inputNames, new boolean[]{true, false, true, true}));

        System.out.println();

        //List<String> inputNames2 = Arrays.asList("A", "C", "D");
       // assertEquals(true, engine2.calculateOutputLogicNew(inputNames2, new boolean[]{true, true, true}));
    }

    @Test
    @DisplayName("Elijah Test")
    public void singleCalculation() throws IOException, URISyntaxException {
        engine = new PLCEngine();
        // Writing PLC Script in editor
        ArrayList<String> PLCScript = new ArrayList<String>() {
            {
                add("LD A");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);

        List<String> inputNames = Arrays.asList("A");
        boolean[] inputValues = {false};
        boolean output = false;
        output = engine.evaluateLogic(inputNames,inputValues);
        System.out.println(output);
    }

    /*
    //THIS TEST IS SUPPOSE TO FAIL, THIS IS A DEPRECATED METHOD AND SYSTEM. TEST EXISTS TO SHOW FAILURES
    @Test
    @DisplayName("calculating the table")
    public void testOutputTable() throws IOException, URISyntaxException{
        PLCEngine engine = new PLCEngine();
        engine.uploadPLC(engine.stringTokensFromFile(PLC2Path));

        List<String> inputNames = Arrays.asList("A", "B", "C", "D");
        try{
            boolean[][] outputTable = engine.calculateOutputMap(inputNames);
        }catch (IOException e){
            assertEquals("Calculation Error: Token Corruption",e.getMessage());
            e.printStackTrace();
        }
    }
    */

    @Test
    @DisplayName("calculating the table")
    public void testOutputTableNew() throws IOException, URISyntaxException{
        PLCEngine engine = new PLCEngine();

        List<String> fileTokens = engine.stringTokensFromFile(PLC2Path);
        engine.uploadPLC(fileTokens);

        List<String> inputNames = Arrays.asList("A", "B", "C", "D");
        boolean[][] outputTable = engine.generateLogicTable(inputNames);

        for(int i=0;i < Math.pow(2,inputNames.size());i++){
            for(int j=0;j < inputNames.size()+1;j++){
                System.out.print(outputTable[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Test
    @DisplayName("calculating the table")
    public void testOutputTableNewTwo() throws IOException, URISyntaxException{
        PLCEngine engine = new PLCEngine();

        List<String> fileTokens = engine.stringTokensFromFile(PLC3Path);
        engine.uploadPLC(fileTokens);

        List<String> inputNames = Arrays.asList("A", "C", "D");
        boolean[][] outputTable = engine.generateLogicTable(inputNames);

        for(int i=0;i < Math.pow(2,inputNames.size());i++){
            for(int j=0;j < inputNames.size()+1;j++){
                System.out.print(outputTable[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Test
    @DisplayName("calculating the table")
    public void testOutputTableNewThree() throws IOException, URISyntaxException{
        PLCEngine engine = new PLCEngine();
        List<String> fileTokens = engine.stringTokensFromFile(PLC3Path);
        engine.uploadPLC(fileTokens);

        List<String> inputNames = Arrays.asList("A", "B", "C", "D");
        boolean[][] outputTable = engine.generateLogicTable(inputNames);

        for(int i=0;i < Math.pow(2,inputNames.size());i++){
            for(int j=0;j < inputNames.size()+1;j++){
                System.out.print(outputTable[i][j] + " ");
            }
            System.out.println();
        }
    }
}