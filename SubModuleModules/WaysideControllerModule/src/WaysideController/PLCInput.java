package WaysideController;

/** allows PLCEngines to refer to generic inputs which can be specified elsewhere.
 * @author elijah
 */
public class PLCInput {

    private final boolean DEFAULT_INITIAL_VALUE = false;
    private String variableName = "NA";

    private boolean value = DEFAULT_INITIAL_VALUE;

    /*
        Constructors
     */
    protected PLCInput() {
        this.variableName = "Inherited";

    }
    public PLCInput(String name) {
        this.variableName = name;
    }
    public PLCInput(String name, boolean initialValue) {
        this.variableName = name;
        this.value = initialValue;
    }

    /*
        Behavior
     */
    public String variableName() {
        return variableName;
    }

    public boolean evaluate() {
        return value;
    }

    /** being equal is considered as having the same variable name in a PLC script.
     *  Overriding the .equals() function allows us to use an empty, proxy PLCInput generated from a script to reference a real, given PLCInput which we can define later
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PLCInput))
            return false;
        PLCInput other = (PLCInput) o;
        return other.variableName.equals(this.variableName);
    }
}
