package PLCInput;

import PLCOutput.AuthorityPLCOutput;
import TrackConstruction.TrackElement;

import java.io.Serializable;

/** defines behavior of using a block's authority as input for PLC
 * @author elijah
 */
public class HasAuthorityPLCInput extends PLCInput implements Serializable {
    /***********************************************************************************************************************/
    /** Enum
     */
    public static enum AuthRule {
        TrueWhenGreaterOrEqual, TrueWhenLessOrEqual
    }
    /** Default Members
     */
    private final AuthRule DEFAULT_EVALUATION_RULE = AuthRule.TrueWhenGreaterOrEqual;
    private final int DEFAULT_EVALUATION_POINT = 0;
    /** Members
     */
    protected TrackElement target = null;
    protected AuthRule evaluationRule = DEFAULT_EVALUATION_RULE;
    protected int evaluationPoint = DEFAULT_EVALUATION_POINT;
    /***********************************************************************************************************************/
    // Default constructor used to determine whether block has authority or doesn't
    public HasAuthorityPLCInput(String varName, TrackElement targetBlock) {
        this.variableName = varName;
        this.target = targetBlock;
        this.evaluationRule = AuthRule.TrueWhenGreaterOrEqual;
        this.evaluationPoint = 1;
    }
    public HasAuthorityPLCInput(String varName, TrackElement targetBlock, AuthRule rule, int criteria) {
        this.variableName = varName;
        this.target = targetBlock;
        this.evaluationRule = rule;
        this.evaluationPoint = criteria;
    }

    public void setNewRule(AuthRule newRule) {
        this.evaluationRule = newRule;
    }
    public void setNewCriteria(int criteria) {
        this.evaluationPoint = criteria;
    }
    public int criteria() {
        return evaluationPoint;
    }
    public AuthRule rule() {
        return evaluationRule;
    }
    public int targetBlockNum() {
        return target.getBlockNum();
    }

    @Override
    public boolean evaluate() throws Exception {
        int currentAuthority;
        try {
            currentAuthority = target.getAuthority();
        } catch (Exception failureToGetAuthorityFromBlock) {
            failureToGetAuthorityFromBlock.printStackTrace();
            throw new Exception(String.format("Failed to retrieve authority from block (%d) when evaluating authority input",target.getBlockNum()));
        }
        switch (evaluationRule) {
            case TrueWhenGreaterOrEqual:
                return currentAuthority >= evaluationPoint;
            case TrueWhenLessOrEqual:
                return currentAuthority <= evaluationPoint;
            default:
                // Neither rules are in use. If user has defined a new rule, they forgot to include it in the switch cases
                throw new Exception(String.format("Unidentified rule in use for HasAuthorityPLCInput detected\nEnsure that case statement is written for any new rule evaluations in evaluate()"));
        }
    }

    @Override
    public String toString() {
        return String.format("Auth Listener (Block:#%d), obj:%s",target.getBlockNum(),target);
    }
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof HasAuthorityPLCInput) )
            return false;
        HasAuthorityPLCInput other = (HasAuthorityPLCInput) o;
        if ( other.variableName.equals(this.variableName) && other.target.equals(this.target)) // We could require that evaluationRules match but I've made the decision not to && other.evaluationRule.equals(this.evaluationRule))
            return true;
        else return false;
    }
}
