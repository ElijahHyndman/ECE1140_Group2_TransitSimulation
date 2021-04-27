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
     * @member rememberedAuthority, whenever we inhibit the authority of a block based on PLC output, we should remember what value it used to be so we can restore it when inhibition is lifted
     */
    private TrackElement target;
    private AuthOutRule applicationRule = DEFAULT_AUTHORITY_APPLICATION_RULE;
    private int rememberedAuthority = 0;
    private boolean isHalted = false;
    /***********************************************************************************************************************/
    public AuthorityPLCOutput(TrackElement target) {
        this.target = target;
    }
    public AuthorityPLCOutput(TrackElement target, AuthOutRule rule) {
        this.target = target;
        this.applicationRule = rule;
    }

    public void haltBlock(TrackElement target) {
        isHalted = true;
        rememberedAuthority = target.getAuthority();
        target.setAuthority(0);
    }
    public void releaseHalt(TrackElement target) {
        if (isHalted) {
            // release halt
            isHalted = false;
            target.setAuthority(rememberedAuthority);
            rememberedAuthority = 0;
        } else {
            // If we are not releasing a halt on it, then leave it be
        }
    }
    @Override
    public void applyOutputRule(boolean value) throws Exception {
        this.value = value;
        switch(applicationRule) {
            case HaltWhenTrue:
                try {
                    if (value) {
                        // Halt the block
                        haltBlock(target);
                    } else {
                        // reapply authority
                        releaseHalt(target);
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
                        releaseHalt(target);
                    } else {
                        // Halt the block
                        haltBlock(target);
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
