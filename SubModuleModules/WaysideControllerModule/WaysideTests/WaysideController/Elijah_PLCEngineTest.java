package WaysideController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
        System.out.println(engine.evaluateLogicGeneric(variables));
    }
}