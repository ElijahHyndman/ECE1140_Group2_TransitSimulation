package WaysideController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author elijah
 */
class Elijah_PLCEngineTest {
    PLCEngine engine;

    /*
        Uploading Tests
     */

    @Test
    @DisplayName("PLCEngine vets PLC scripts and denies PLCScript without SET")
    public void PLCwithoutSET() {
        engine = new PLCEngine();
        // PLC Script that doesn't end with "SET"
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("AND");
                add("LD variable2");
                add("AND");
            }
        };
        assertThrows(Exception.class, () -> engine.uploadPLC(PLCScript));
    }

    @Test
    @DisplayName("PLCEngine vets PLC scripts and denies PLCScript without SET")
    public void PLCwithIncorrectSET() throws Exception {
        engine = new PLCEngine();
        // PLC Script that doesn't end with "SET"
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("AND");
                add("LD variable2");
                add("SET");
                add("AND");
            }
        };
        assertThrows(Exception.class, () -> engine.uploadPLC(PLCScript));
    }

    @Test
    @DisplayName("PLCEngine vets PLC scripts and denies PLCScript without SET")
    public void PLCwithTooFewLines() throws Exception {
        engine = new PLCEngine();
        // PLC Script that doesn't end with "SET"
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("SET");
            }
        };
        assertThrows(Exception.class, () -> engine.uploadPLC(PLCScript));
    }

    /*
        Helper Function Tests
     */
    @Test
    @DisplayName("VariableReferenceBy function gets the correct reference")
    public void PLCInputTest1 () throws Exception {
        // See that proxy can be used to fetch real
        // Set proxy to false to tell them apart
        PLCInput proxy = new PLCInput("Tim",false);
        PLCInput actual = new PLCInput("Tim",true);
        System.out.printf("Searching for real (%s) using proxy (%s)\n%s\n",actual.hashCode(),proxy.hashCode(),actual);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("Nickelby",true));
                add(new PLCInput("Pierogi",true));
                add(actual);
                add(new PLCInput("Delilah",true));
            }
        };
        PLCInput result = PLCEngine.variableReferenceBy(proxy,variables);
        System.out.printf("Result found PLCInput of:\n%s (%s)",result,result.hashCode());
        assertNotEquals(actual.hashCode(),proxy.hashCode());
        assertNotEquals(result.hashCode(),proxy.hashCode());
        assertEquals(result.hashCode(),actual.hashCode());
    }

    @Test
    @DisplayName("VariableReferenceBy function gets the correct reference")
    public void PLCInputTest2 () throws Exception {
        // See that proxy can be used to fetch real
        // Set proxy to false to tell them apart
        PLCInput proxy = new PLCInput("Jaques",false);
        PLCInput actual = new PLCInput("Jaques",true);
        System.out.printf("Searching for real (%s) using proxy (%s)\n%s\n",actual.hashCode(),proxy.hashCode(),actual);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("Nickelby",true));
                add(new PLCInput("Pierogi",true));
                add(new PLCInput("Tim",true));
                add(actual);
                add(new PLCInput("Delilah",true));
            }
        };
        PLCInput result = PLCEngine.variableReferenceBy(proxy,variables);
        System.out.printf("Result found PLCInput of:\n%s (%s)",result,result.hashCode());
        assertNotEquals(actual.hashCode(),proxy.hashCode());
        assertNotEquals(result.hashCode(),proxy.hashCode());
        assertEquals(result.hashCode(),actual.hashCode());
    }

    @Test
    @DisplayName("VariableReferenceBy function throws exception when no variables match reference")
    public void PLCInputTest3() throws Exception {
        // See that proxy can be used to fetch real
        // Set proxy to false to tell them apart
        PLCInput proxy = new PLCInput("reference that doesn't correspond to something in the list");
        System.out.printf("Searching for real using proxy (%s)\n",proxy);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("Nickelby",true));
                add(new PLCInput("Pierogi",true));
                add(new PLCInput("Tim",true));
                add(new PLCInput("Delilah",true));
            }
        };
        // variableReferenceBy will throw error if referenced variable was not found in the list of inputs
        assertThrows( Exception.class , () -> {PLCEngine.variableReferenceBy(proxy,variables);} );
    }

    @Test
    @DisplayName("VariableReferenceBy function handles null proxy")
    public void PLCInputTest4 () throws Exception {
        // See that proxy can be used to fetch real
        // Set proxy to false to tell them apart
        PLCInput proxy = null;
        System.out.printf("Searching for real using proxy (%s)\n",proxy);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("Nickelby",true));
                add(new PLCInput("Pierogi",true));
                add(new PLCInput("Tim",true));
                add(new PLCInput("Delilah",true));
            }
        };
        // variableReferenceBy will throw error if you search will null
        assertThrows( Exception.class, () -> PLCEngine.variableReferenceBy(proxy,variables));
    }

    @Test
    @DisplayName("Handles incorrect first line of PLC (i.e. not a LD command) [Not detected until trying to output logic]")
    public void incorrectPLCFormat() throws Exception {
        boolean input1 =false, input2 = false;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("AND");
                add("LD variable2");
                add("AND");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable",input1));
                add(new PLCInput("variable2",input2));
            }
        };
        assertThrows(Exception.class, () -> engine.evaluateLogic(variables));
    }


    /*
        Logic Tests
     */


    @Test
    @DisplayName("Using evaluateLogicGeneric does not throw error (manually providing input list)")
    public void evaluateLogicDoesntThrowError() throws Exception {
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable",false));
            }
        };
        System.out.println(engine.evaluateLogic(variables));
    }

    @Test
    @DisplayName("Logic test: AND")
    public void logicTestAND() throws Exception {
        boolean input1 = true;
        boolean input2 = false;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("LD variable2");
                add("AND");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable",input1));
                add(new PLCInput("variable2",input2));
            }
        };
        boolean result = engine.evaluateLogic(variables);
        System.out.println("AND operation logic output: " + result);
        assertEquals((input1 && input2) , result );
    }

    @Test
    @DisplayName("Logic test: AND (long)")
    public void logicTestAND2() throws Exception {
        boolean input1 = true;
        boolean input2 = true;
        boolean input3 = true;
        boolean input4 = true;
        boolean input5 = true;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("LD variable2");
                add("LD variable3");
                add("LD variable4");
                add("LD variable5");
                add("AND");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable", input1));
                add(new PLCInput("variable2", input2));
                add(new PLCInput("variable3", input3));
                add(new PLCInput("variable4", input4));
                add(new PLCInput("variable5", input5));
            }
        };
        boolean result = engine.evaluateLogic(variables);
        System.out.println("AND operation logic output (long): " + result);
        assertEquals((input1 && input2 && input3 && input4 && input5), result);
        System.out.println(engine.getPLCString());
    }

    @Test
    @DisplayName("Logic test: NOT")
    public void logicTestNOT() throws Exception {
        boolean input1 = false;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("NOT");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable",input1));
            }
        };
        boolean result = engine.evaluateLogic(variables);
        System.out.println("NOT operation logic output: " + result);
        assertEquals( !(input1) , result );
    }

    @Test
    @DisplayName("Logic test: OR")
    public void logicTestOR() throws Exception {
        boolean input1 = true;
        boolean input2 = false;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("LD variable2");
                add("OR");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable",input1));
                add(new PLCInput("variable2",input2));
            }
        };
        boolean result = engine.evaluateLogic(variables);
        System.out.println("OR operation logic output: " + result);
        assertEquals( (input1 || input2) , result );
    }

    @Test
    @DisplayName("Logic test: OR (long)")
    public void logicTestOR2() throws Exception {
        boolean input1 = true;
        boolean input2 = false;
        boolean input3 = false;
        boolean input4 = false;
        boolean input5 = false;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("LD variable2");
                add("LD variable3");
                add("LD variable4");
                add("LD variable5");
                add("OR");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable", input1));
                add(new PLCInput("variable2", input2));
                add(new PLCInput("variable3", input3));
                add(new PLCInput("variable4", input4));
                add(new PLCInput("variable5", input5));
            }
        };
        boolean result = engine.evaluateLogic(variables);
        System.out.println("AND operation logic output (long): " + result);
        assertEquals((input1 || input2 || input3 || input4 || input5), result);
    }

    @Test
    @DisplayName("Logic test: ANB (long)")
    public void logicTestANB() throws Exception {
        // ANB only ands the last two entries, while AND ands every single entry above
        boolean input1 = true;
        boolean input2 = true;
        boolean input3 = true;
        boolean input4 = true;
        boolean input5 = true;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("LD variable2");
                add("LD variable3");
                add("LD variable4");
                add("AND");  // Have to or them to clear the stack, can't leave them un-operated
                add("LD variable5");
                add("ANB");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable", input1));
                add(new PLCInput("variable2", input2));
                add(new PLCInput("variable3", input3));
                add(new PLCInput("variable4", input4));
                add(new PLCInput("variable5", input5));
            }
        };
        boolean result = engine.evaluateLogic(variables);
        System.out.println("ANB operation logic output (long): " + result);
        assertEquals(input5, result);
    }

    @Test
    @DisplayName("Logic test: ANB (long)")
    public void logicTestORB() throws Exception {
        // ANB only ands the last two entries, while AND ands every single entry above
        boolean input1 = true;
        boolean input2 = true;
        boolean input3 = true;
        boolean input4 = false;
        boolean input5 = false;
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>() {
            {
                add("LD variable");
                add("LD variable2");
                add("LD variable3");
                add("LD variable4");
                add("AND");  // Have to or them to clear the stack, can't leave them un-operated
                add("LD variable5");
                add("ORB");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> variables = new ArrayList<PLCInput>() {
            {
                add(new PLCInput("variable", input1));
                add(new PLCInput("variable2", input2));
                add(new PLCInput("variable3", input3));
                add(new PLCInput("variable4", input4));
                add(new PLCInput("variable5", input5));
            }
        };
        boolean result = engine.evaluateLogic(variables);
        System.out.println("ANB operation logic output (long): " + result);
        assertEquals((input1 && input2 && input3 && input4) || input5, result);
    }

    /*
        Overloading Tests
     */

    // Made an overloading class of PLCInput so we can use whatever we want as an input operation for a PLC script
    // I will use this for the next few tests
    // In practice, we will want to use this same overloading technique to allow us to define different sources of boolean inputs for the PLC engine (a track's occupancy, or elsewise)
    private class CustomPLCInputSource extends PLCInput {
        // Lesson learned: do NOT redefine variableName and value variables in child classes
        //String variableName;
        //boolean value = false;
        public CustomPLCInputSource(String varName, boolean value) {
            this.variableName = varName;
            this.value = value;
        }
        public boolean evaluate() {
            // 50/50 chance of being greater than zero
            boolean randBool = (new Random().nextInt() > 0);
            value = randBool;
            System.out.printf("My name is (%s) and I have been evaluated!! My value is now %b\n",variableName, value);
            return value;
        }
    }

    @Test
    @DisplayName("Overloading Inputs can be used with their own defined behavior")
    public void overloadedClassesCanBeUsed() throws Exception {
        engine = new PLCEngine();
        boolean input1 = false;
        boolean input2 = true;
        CustomPLCInputSource var1 = new CustomPLCInputSource("var1",input1);
        CustomPLCInputSource var2 = new CustomPLCInputSource("var2",input2);
        ArrayList<String> PLCScript = new ArrayList<>(){
            {
                add("LD var1");
                add("LD var2");
                add("OR");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        ArrayList<PLCInput> inputState = new ArrayList<PLCInput>() {
            {
                add(var1);
                add(var2);
            }
        };

        boolean result = engine.evaluateLogic(inputState);
        assertEquals((input1 || input2), result);
        System.out.printf("Operation Yielded: %b\n",result);
    }

    /*
        PLC input source defining Tests
     */

    @Test
    @DisplayName("PLC Engine can remember input source definitions when defined explicitly")
    public void sourceDefinition() throws Exception {
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>(){
            {
                add("LD var1");
                add("LD var2");
                add("OR");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        PLCInput var1 = new PLCInput("var1",true);
        PLCInput var2 = new PLCInput("var2",true);
        engine.definePLCInputSource(var1);
        engine.definePLCInputSource(var2);

        boolean engineIdentifiesThatPLCReferencesHaveDefinitions = engine.allPLCInputSourcesDefined();
        System.out.printf("Engine has stored input source definitions: %b\n",engineIdentifiesThatPLCReferencesHaveDefinitions);
        assertEquals(true, engineIdentifiesThatPLCReferencesHaveDefinitions);
    }

    @Test
    @DisplayName("PLC engine throws descriptive error whenever a variable is referenced in PLC script without definition")
    public void sourceDefinition2() throws Exception {
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>(){
            {
                add("LD var1");
                add("LD var2");
                // We will purposefully reference var3 without defining var3 input source
                add("LD var 3");
                add("OR");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        PLCInput var1 = new PLCInput("var1",true);
        PLCInput var2 = new PLCInput("var2",true);
        engine.definePLCInputSource(var1);
        engine.definePLCInputSource(var2);

        // var3 reference does not have defined input source, engine should throw error
        assertThrows(Exception.class, () -> engine.allPLCInputSourcesDefined());
    }

    @Test
    @DisplayName("PLC engine successfully remembers source definitions when called to calculate")
    public void sourceDefinitions3() throws Exception {
        engine = new PLCEngine();
        ArrayList<String> PLCScript = new ArrayList<>(){
            {
                add("LD var1");
                add("LD var2");
                add("OR");
                add("SET");
            }
        };
        engine.uploadPLC(PLCScript);
        PLCInput var1 = new CustomPLCInputSource("var1",true);
        PLCInput var2 = new CustomPLCInputSource("var2",true);
        engine.definePLCInputSource(var1);
        engine.definePLCInputSource(var2);
        boolean output = engine.evaluateLogic();
        System.out.printf("Output using remembered inputsources: %b\n",output);
    }

    @Test
    @DisplayName("Timing Characteristics [10 PLC engines]")
    public void timingCharacteristics1 () throws Exception {
        // Tests the timing characteristics of evaluateLogic for n PLCEngines
        int n = 10;
        int nanosecondsPerSecond = 1000000000;
        Vector<PLCEngine> engines = new Vector<PLCEngine>();

        ArrayList<String> genericPLCScript = new ArrayList<>(){
            {
                add("LD var1");
                add("LD var2");
                add("OR");
                add("SET");
            }
        };

        long startTime = System.nanoTime();
        for (int i=0; i<n; i++) {
            PLCEngine newEngine = new PLCEngine();
            newEngine.uploadPLC(genericPLCScript);
            // Each PLCEngine gets 2 unique PLCInput objects each
            newEngine.definePLCInputSource( new PLCInput("var1", true) );
            newEngine.definePLCInputSource( new PLCInput("var2", false) );
            engines.add(newEngine);
        }
        long elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Upload time (%d): %fsec\n", engines.size(), (double)elapsedTime / nanosecondsPerSecond);


        startTime = System.nanoTime();
        for (PLCEngine engine : engines) {
            engine.evaluateLogic();
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Calculation time (%d): %fsec\n", engines.size(), (double)elapsedTime / nanosecondsPerSecond);
    }

    @Test
    @DisplayName("Timing Characteristics [100 PLC engines]")
    public void timingCharacteristics2 () throws Exception {
        // Tests the timing characteristics of evaluateLogic for n PLCEngines
        int n = 100;
        int nanosecondsPerSecond = 1000000000;
        Vector<PLCEngine> engines = new Vector<PLCEngine>();

        ArrayList<String> genericPLCScript = new ArrayList<>(){
            {
                add("LD var1");
                add("LD var2");
                add("OR");
                add("SET");
            }
        };

        long startTime = System.nanoTime();
        for (int i=0; i<n; i++) {
            PLCEngine newEngine = new PLCEngine();
            newEngine.uploadPLC(genericPLCScript);
            // Each PLCEngine gets 2 unique PLCInput objects each
            newEngine.definePLCInputSource( new PLCInput("var1", true) );
            newEngine.definePLCInputSource( new PLCInput("var2", false) );
            engines.add(newEngine);
        }
        long elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Upload time (%d): %fsec\n", engines.size(), (double)elapsedTime / nanosecondsPerSecond);


        startTime = System.nanoTime();
        for (PLCEngine engine : engines) {
            engine.evaluateLogic();
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Calculation time (%d): %fsec\n", engines.size(), (double)elapsedTime / nanosecondsPerSecond);
    }

    @Test
    @DisplayName("Timing Characteristics [1000 PLC engines]")
    public void timingCharacteristics3 () throws Exception {
        // Tests the timing characteristics of evaluateLogic for n PLCEngines
        int n = 1000;
        int nanosecondsPerSecond = 1000000000;
        Vector<PLCEngine> engines = new Vector<PLCEngine>();

        ArrayList<String> genericPLCScript = new ArrayList<>(){
            {
                add("LD var1");
                add("LD var2");
                add("OR");
                add("SET");
            }
        };

        long startTime = System.nanoTime();
        for (int i=0; i<n; i++) {
            PLCEngine newEngine = new PLCEngine();
            newEngine.uploadPLC(genericPLCScript);
            // Each PLCEngine gets 2 unique PLCInput objects each
            newEngine.definePLCInputSource( new PLCInput("var1", true) );
            newEngine.definePLCInputSource( new PLCInput("var2", false) );
            engines.add(newEngine);
        }
        long elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Upload time (%d): %fsec\n", engines.size(), (double)elapsedTime / nanosecondsPerSecond);


        startTime = System.nanoTime();
        for (PLCEngine engine : engines) {
            engine.evaluateLogic();
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Calculation time (%d): %fsec\n", engines.size(), (double)elapsedTime / nanosecondsPerSecond);
    }
}