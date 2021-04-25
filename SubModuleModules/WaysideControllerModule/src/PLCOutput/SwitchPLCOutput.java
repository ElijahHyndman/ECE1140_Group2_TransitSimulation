package PLCOutput;

import TrackConstruction.Switch;

/** PLCOutput that determines the orientation of a Track System switch based on the output of a PLC boolean calculation and a determined rule
 *
 * Note about definitions:
 * switches are defined in the .csv file as Switch (1-0; 5-0)
 * Default refers to a switch being oriented in the first position defined (i.e. 1-0)
 * Secondary refers to a switch being oriented in the second position defined (i.e. 5-0)
 * @author elijah
 */
public class SwitchPLCOutput extends PLCOutput {
    /***********************************************************************************************************************/
    /** Enum
     * the boolean key values for each ORI is determined by Grace's Switch.java class.
     * It seems that she uses false to mean DEFAULT orientation and true to mean SECONDARY orientation
     */
    public static enum SwitchRule {
        DefaultWhenTrue, SecondaryWhenTrue
    }
    public static enum ORI {
        DEFAULT(false), SECONDARY(true);
        // Special definition to associate a key with an orientation
        public boolean key;
        ORI(boolean key) {this.key = key;}
    }
    /** Default Members
     * Secondary and Default orientation values for switch are derived from Switch.java code
     */
    private final SwitchRule DEFAULT_SWITCH_RULE = SwitchRule.DefaultWhenTrue;
    private final ORI DEFAULT_INITIAL_ORIENTATION = ORI.DEFAULT;
    private final String DEFAULT_VAR_FORMAT = "Switch %d PLC";
    /** Members
     * @member target, Switch   the switch object whose orientation shall be dictated by this PLCOutput
     * @member applicationRule, SwitchRule  the method we use to map a PLC boolean output of (true, false) to a switch orientation of (Default, Secondary)
     * @member orientation, ORI     the current orientatino of the switch. This variable is updated manually as the switch is manipulated, so there may be discrepancy between this representation and the real orientation if something goes wrong
     */
    private Switch target;
    private SwitchRule applicationRule = DEFAULT_SWITCH_RULE;
    private ORI orientation = DEFAULT_INITIAL_ORIENTATION;
    /***********************************************************************************************************************/
    public SwitchPLCOutput (Switch targetSwitch) {
        this.target = targetSwitch;
        this.variableName = String.format(DEFAULT_VAR_FORMAT,target.getBlockNum());
        orientation = (targetSwitch.getSwitchState().equals("DEFAULT")) ? ORI.DEFAULT : ORI.SECONDARY;
    }
    public SwitchPLCOutput (String varName, Switch targetSwitch) {
        this.variableName = varName;
        this.target = targetSwitch;
        orientation = (targetSwitch.getSwitchState().equals("DEFAULT")) ? ORI.DEFAULT : ORI.SECONDARY;
    }
    public SwitchPLCOutput (String varName, Switch targetSwitch, SwitchRule rule) {
        this.variableName = varName;
        this.target = targetSwitch;
        this.applicationRule = rule;
        orientation = (targetSwitch.getSwitchState().equals("DEFAULT")) ? ORI.DEFAULT : ORI.SECONDARY;
    }


    public ORI getOrientation() {
        return orientation;
    }
    public void setNewRule(SwitchRule newRule) {
        this.applicationRule = newRule;
    }


    /** method called whenever the PLC engine calculates a new output from a script; executes the intended behavior based on the PLC output and the selected application rule.
     *
     * @param value, the boolean output value given from the PLC calculation
     * @throws Exception if a failure occurs when trying to change the state of the switch
     */
    @Override
    public void applyOutputRule(boolean value) throws Exception {
        switch (applicationRule) {
            case DefaultWhenTrue:
                try {
                    // Set switch orientation based on rule
                    if (value) {
                        target.setSwitchState(ORI.DEFAULT.key);
                        this.orientation = ORI.DEFAULT;
                     } else {
                        target.setSwitchState(ORI.SECONDARY.key);
                        this.orientation = ORI.SECONDARY;
                    }
                    return;
                } catch (Exception failureToChangeSwitchState) {
                    failureToChangeSwitchState.printStackTrace();
                    throw new Exception(String.format("Failure occurred when trying to invoke Switch.setSwitchState() during SwitchPLCOutput application.\ntarget: Switch#%d\nPLC Logical Output: %b\n",target.getBlockNum(),value));
                }
            case SecondaryWhenTrue:
                try{
                    // Set switch orientation based on rule
                    if (value) {
                        target.setSwitchState(ORI.SECONDARY.key);
                        this.orientation = ORI.SECONDARY;
                    } else {
                        target.setSwitchState(ORI.DEFAULT.key);
                        this.orientation = ORI.DEFAULT;
                    }
                    return;
                } catch (Exception failureToChangeSwitchState) {
                    failureToChangeSwitchState.printStackTrace();
                    throw new Exception(String.format("Failure occurred when trying to invoke Switch.setSwitchState() during SwitchPLCOutput application.\ntarget: Switch#%d\nPLC Logical Output: %b\n",target.getBlockNum(),value));
                }
            default:
                // Attempting to use unknown rule
                throw new Exception(String.format("Unidentified rule in use for SwitchPLCOutput application\nEnsure that case statement is written for any new rule evaluations in applyOutputRule()"));
        }
    }
}
