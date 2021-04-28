package PLCInput;

import TrackConstruction.TrackElement;

import java.io.Serializable;

/** PLCInput type that monitors the occupation status of a TrackElement for use as a PLCInput.
 */
public class OccupationPLCInput extends PLCInput implements Serializable {
    /***********************************************************************************************************************/
    /** Enum
     */
    public static enum OccRule {
        TrueWhenOccupied, FalseWhenOccupied
    }
    /** Default Members
     */
    private final OccupationPLCInput.OccRule DEFAULT_EVALUATION_RULE = OccRule.TrueWhenOccupied;
    /** Members
     * @member target, TrackElement the TrackElement object which this PLCInput will check the occupation of to generate its boolean input value
     * @member evaluationRule, the rule that determines whether an occupation of "true" evaluates to a logical high (true) or logical low (false)
     */
    protected TrackElement target = null;
    protected OccupationPLCInput.OccRule evaluationRule = DEFAULT_EVALUATION_RULE;
    /***********************************************************************************************************************/

    public OccupationPLCInput(String varName, TrackElement targetBlock) {
        this.variableName = varName;
        this.target = targetBlock;
    }
    public OccupationPLCInput(String varName, TrackElement targetBlock, OccupationPLCInput.OccRule evaluationRule) {
        this.variableName = varName;
        this.target = targetBlock;
        this.evaluationRule = evaluationRule;
    }
    public void setNewRule(OccupationPLCInput.OccRule rule) {
        this.evaluationRule = rule;
    }
    @Override
    public boolean evaluate() throws Exception {
        /*
            Read Occupation of the block
         */
        boolean targetIsOccupied;
        try {
            // Occupation is true if occupied, elsewise false
            targetIsOccupied = target.getOccupied();
        } catch (Exception failureToReadOccupation) {
            failureToReadOccupation.printStackTrace();
            throw new Exception(String.format("Failure to read occupation of TrackElement (%s) when evaluating rule for PLC Input\n",target.getBlockNum()));
        }
        /*
            Evaluate Rule
         */
        switch (evaluationRule) {
            case TrueWhenOccupied:
                return targetIsOccupied;
            case FalseWhenOccupied:
                return !targetIsOccupied;
            default:
                // Neither rules are in use. If user has defined a new rule, they forgot to include it in the switch cases
                throw new Exception(String.format("Unidentified rule in use for OccupationPLCInput detected\nEnsure that case statement is written for any new rule evaluations in evaluate()"));
        }
    }

    @Override
    public String toString() {
        return String.format("Occ Listener (Block:#%d), obj:%s",target.getBlockNum(),target);
    }
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof OccupationPLCInput) ) {
            return false;
        }
        OccupationPLCInput other = (OccupationPLCInput) o;
        if ( other.variableName.equals(this.variableName) && other.target.equals(this.target)) // We could require that evaluationRules match but I've made the decision not to && other.evaluationRule.equals(this.evaluationRule))
            return true;
        else return false;
    }
}
