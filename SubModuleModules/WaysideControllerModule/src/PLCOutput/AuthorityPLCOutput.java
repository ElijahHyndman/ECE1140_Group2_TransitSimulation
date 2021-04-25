package PLCOutput;

import TrackConstruction.TrackElement;

/** defines behavior of using PLC output to determine authority for a block
 * @author elijah
 */
public class AuthorityPLCOutput extends PLCOutput {
    /***********************************************************************************************************************/
    /** Enum
     */
    public static enum AuthOutRule {
        HaltWhenTrue, AllowWhenTrue
    }
    /** Default Members
     */
    private final AuthOutRule DEFAULT_AUTHORITY_APPLICATION_RULE = AuthOutRule.HaltWhenTrue;
    /** Members
     */
    private TrackElement target;
    private AuthOutRule applicationRule = DEFAULT_AUTHORITY_APPLICATION_RULE;
    /***********************************************************************************************************************/
    public AuthorityPLCOutput(TrackElement target) {
        this.target = target;
    }
    public AuthorityPLCOutput(TrackElement target, AuthOutRule rule) {
        this.target = target;
        this.applicationRule = rule;
    }

    @Override
    public void applyOutputRule(boolean value) throws Exception {
        this.value = value;
        switch(applicationRule) {
            case HaltWhenTrue:
                try {
                    if (value) {
                        // Halt the block
                        target.setAuthority(0);
                    } else {
                        // Leave authority as it is
                    }
                    return;
                } catch (Exception failureToApplyAuthority) {
                    failureToApplyAuthority.printStackTrace();
                    throw new Exception(String.format("Failure occurred when trying to invoke () during AuthorityPLCOutput application.\ntarget: Block#%d\nPLC Logical Output: %b\n",target.getBlockNum(),value));
                }
            case AllowWhenTrue:
                try {
                    if (value) {
                        // Leave authority as it is
                    } else {
                        // Halt the block
                        target.setAuthority(0);
                    }
                    return;
                } catch (Exception failureToApplyAuthority) {
                    failureToApplyAuthority.printStackTrace();
                    throw new Exception(String.format("Failure occurred when trying to invoke () during AuthorityPLCOutput application.\ntarget: Block#%d\nPLC Logical Output: %b\n",target.getBlockNum(),value));
                }
            default:
                // Attempting to use unknown rule
                throw new Exception(String.format("Unidentified rule in use for AuthorityPLCOutput application\nEnsure that case statement is written for any new rule evaluations in applyOutputRule()"));
        }
    }

    public String toString() {
        return String.format("Authority Rule for Block#%d",target.getBlockNum());
    }
}
