package PLCOutput;

/**
 * @author elijah
 */
public class PLCOutput {
    /** Default Members
     */
    protected final static boolean DEFAULT_INITIAL_BOOLEAN_STATE = false;
    /** Members
     */
    protected String variableName = "Default output variable name";
    protected boolean value = DEFAULT_INITIAL_BOOLEAN_STATE;
    /*
        Constructors
     */
    // Default only included for inheritance purposes
    protected PLCOutput() {

    }
    public PLCOutput(String variableName) {
        this.variableName = variableName;
    }

    public boolean value() {
        return value;
    }
    public String variableName() {
        return variableName;
    }
    /** operation to invoke an output rule based on a PLC output evaluation.
     * The generic form of PLCoutput.applyOutput will just system.out.println that output has been set, but the intended usage is for PLCOutput to be extended and overriden to implement unique behaviors for output rule application
     *
     * @param value
     */
    public void applyOutputRule(boolean value) {
        this.value = value;
        System.out.printf("My name is Output Variable \"%s\" and I have been set to %b!\n",this.variableName,this.value);
    }
}
