package PLCInput;

import TrackConstruction.TrackElement;

/** PLCInput type that monitors the occupation status of a TrackElement for use as a PLCInput.
 */
public class OccupationPLCInput extends PLCInput {
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

    public OccupationPLCInput(String varName, TrackElement targetBlock) {
        this.variableName = varName;
        this.target = targetBlock;
    }
    public OccupationPLCInput(String varName, TrackElement targetBlock, OccupationPLCInput.OccRule evaluationRule) {
        this.variableName = varName;
        this.target = targetBlock;
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
            throw new Exception(String.format("Failure to read occupation of TrackElement (%s) when evaluating rule for PLC Input\n",target));
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

}
