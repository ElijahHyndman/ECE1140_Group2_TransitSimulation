package PLCInput;

/** allows PLCEngines to refer to generic inputs which can be specified elsewhere.
 *  This class is intended to be extended and overriden to define custom behavior for PLC input sources.
 *
 *  Using this class as-is lets you test the PLCEngine using generic true or false, but overriding the .evaluate() funciton allows you to devise your own custom sources for PLC Script boolean input gathering
 * @author elijah
 */
public class PLCInput {
    /** Default Members
     */
    private final boolean DEFAULT_INITIAL_VALUE = false;
    /** Members
     */
    protected String variableName = "NA";
    protected boolean value = DEFAULT_INITIAL_VALUE;

    /*
        Constructors
     */
    // Default only included for inheritance purposes
    protected PLCInput() {
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

    /** variable name is how we associate a variable name reference in a PLCScript (such as "LD variableA" referencing variableA) and the actual PLCInput variable we define later as new PLCInput(name=variableA, value=theValueOfTheActualObject)
     *
     * @return
     */
    public String variableName() {
        return variableName;
    }

    /** this is the meat and potatoes of the PLCInput class. Overriding of this function should apply some sort of boolean-resulting evaluation rule
     * PLCInput is meant to be overridden so you can define your own custom input sources for the the boolean inputs, whenever you extend PLCInput you will use evaulate() to define how a boolean input is generated for a specific source.
     * For instance, for the TrackOccupation input as a PLCInput, I would define TrackObject.isOccupied==true to return a true boolean whenever I evaluate a TrackOccupation as input to a PLC script
     *
     * @return
     */
    public boolean evaluate() throws Exception {
        return value;
    }

    /** being equal is considered as having the same variable name in a PLC script.
     *  Overriding the .equals() function allows us to use an empty, proxy PLCInput generated from a script to reference a real, given PLCInput which we can define later
     * @param o
     * @return boolean, whether the name of the other variable matches the name of this variable, true otherwise false (used for mapping a proxy to an actual)
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PLCInput))
            return false;
        PLCInput other = (PLCInput) o;
        return other.variableName.equals(this.variableName);
    }
}
