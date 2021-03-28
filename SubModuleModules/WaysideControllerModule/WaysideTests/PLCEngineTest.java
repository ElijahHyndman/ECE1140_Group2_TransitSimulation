import WaysideController.PLCEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PLCEngineTest {

    @BeforeEach
    public void setUp(){
        PLCEngine engine = new PLCEngine();
    }

    @Test
    @DisplayName("Testing Read Helper Function")
    public void testReader() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();

        List<String> testList = engine.readFileNew("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken");
        for(int i = 0; i < testList.size(); i++){
            System.out.println(testList.get(i));
        }

        System.out.println("Finished code.");
    }

    @Test
    @DisplayName("Testing Read Helper Function New Version")
    public void testReaderNew() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();

        List<String> testList = engine.readFileNew("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken");
        for(int i = 0; i < testList.size(); i++){
            System.out.println(testList.get(i));
        }

        System.out.println("Finished code.");
    }

    @Test
    @DisplayName("Testing String Checker Helper")
    public void testString() {
        PLCEngine engine = new PLCEngine();

        assertTrue(engine.verifyString("A"));
        assertFalse(engine.verifyString("ABC DEF"));
        assertFalse(engine.verifyString("ABC:DEF"));
        assertFalse(engine.verifyString("ABC10231DEF"));
        assertTrue(engine.verifyString("ABCDEF"));
    }

    @Test
    @DisplayName("Testing Token Creator Functions")
    public void testTokens() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();
        PLCEngine engine2 = new PLCEngine();

        engine.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken");
        Queue<String> que = engine.getVarQueue();
        Queue<PLCEngine.Token> tok = engine.getTokenQueue();

        for(String s : que){
            System.out.println(s);
        }

        for(PLCEngine.Token t : tok){
            System.out.println(t.toString());
        }

        engine2.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken2");
        Queue<String> que2 = engine2.getVarQueue();
        Queue<PLCEngine.Token> tok2 = engine2.getTokenQueue();

        for(String s : que2){
            System.out.println(s);
        }

        for(PLCEngine.Token t : tok2){
            System.out.println(t.toString());
        }
    }

    @Test
    @DisplayName("Testing the output generation")
    public void testOutput() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();

        engine.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken");
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

    @Test
    @DisplayName("Testing the output generation")
    public void testOutputNEW() throws IOException, URISyntaxException {
        PLCEngine engine = new PLCEngine();
        engine.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken2");

        PLCEngine engine2 = new PLCEngine();
        engine2.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken");

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
        assertEquals(false, engine.calculateOutputLogicNew(inputNames, new boolean[]{false, false, false, false}));

        System.out.println();

        assertEquals(true, engine.calculateOutputLogicNew(inputNames, new boolean[]{true, false, true, true}));

        System.out.println();

        List<String> inputNames2 = Arrays.asList("A", "C", "D");
        assertEquals(true, engine2.calculateOutputLogicNew(inputNames2, new boolean[]{true, true, true}));
    }

    //THIS TEST IS SUPPOSE TO FAIL, THIS IS A DEPRECATED METHOD AND SYSTEM. TEST EXISTS TO SHOW FAILURES
    @Test
    @DisplayName("calculating the table")
    public void testOutputTable() throws IOException, URISyntaxException{
        PLCEngine engine = new PLCEngine();

        engine.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken2");

        List<String> inputNames = Arrays.asList("A", "B", "C", "D");
        try{
            boolean[][] outputTable = engine.calculateOutputMap(inputNames);
        }catch (IOException e){
            assertEquals("Calculation Error: Token Corruption",e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("calculating the table")
    public void testOutputTableNew() throws IOException, URISyntaxException{
        PLCEngine engine = new PLCEngine();

        engine.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken2");

        List<String> inputNames = Arrays.asList("A", "B", "C", "D");
        boolean[][] outputTable = engine.calculateOutputMapNew(inputNames);

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

        engine.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken3");

        List<String> inputNames = Arrays.asList("A", "C", "D");
        boolean[][] outputTable = engine.calculateOutputMapNew(inputNames);

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

        engine.createTokens("C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken");

        List<String> inputNames = Arrays.asList("A", "B", "C", "D");
        boolean[][] outputTable = engine.calculateOutputMapNew(inputNames);

        for(int i=0;i < Math.pow(2,inputNames.size());i++){
            for(int j=0;j < inputNames.size()+1;j++){
                System.out.print(outputTable[i][j] + " ");
            }
            System.out.println();
        }
    }
}