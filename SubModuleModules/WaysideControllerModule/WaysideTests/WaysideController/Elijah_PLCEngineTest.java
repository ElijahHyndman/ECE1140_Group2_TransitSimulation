package WaysideController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author elijah
 */
class Elijah_PLCEngineTest {
    PLCEngine engine;

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
        assertThrows( java.lang.IndexOutOfBoundsException.class , () -> {PLCEngine.variableReferenceBy(proxy,variables);} );
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
}