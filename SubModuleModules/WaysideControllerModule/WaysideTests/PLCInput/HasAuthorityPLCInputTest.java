package PLCInput;

import TrackConstruction.TrackBlock;
import WaysideController.PLCEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HasAuthorityPLCInputTest {

    HasAuthorityPLCInput listener;
    PLCEngine engine;
    TrackBlock testBlock;
    ArrayList<String> simplePLCScript = new ArrayList<>() {
        {
            add("LD Auth");
            add("SET");
        }
    };

    @BeforeEach
    public void setUp() throws Exception {
        engine = new PLCEngine();
        engine.uploadPLC(simplePLCScript);
        testBlock = new TrackBlock();
        testBlock.setAuthority(1);
        testBlock.setCommandedSpeed(20);
        testBlock.setSpeedLimit(10);
        testBlock.setBlockNum(1);
    }

    @Test
    @DisplayName("Testing default construction (authority > 0) ")
    public void test() throws Exception {
        listener = new HasAuthorityPLCInput("Auth",testBlock);
        engine.registerInputSource(listener);
        int testAuth;
        boolean result;

        testAuth = 0;
        testBlock.setAuthority(testAuth);
        result = engine.evaluateLogic();
        assertEquals(false, result);
        System.out.printf("Default Constructor: Authority %d -> %b\n",testAuth,result);

        testAuth = 1;
        testBlock.setAuthority(testAuth);
        result = engine.evaluateLogic();
        assertEquals(true, result);
        System.out.printf("Default Constructor: Authority %d -> %b\n",testAuth,result);


        testAuth = 2;
        testBlock.setAuthority(testAuth);
        result = engine.evaluateLogic();
        assertEquals(true, result);
        System.out.printf("Default Constructor: Authority %d -> %b\n",testAuth,result);


        testAuth = 3;
        testBlock.setAuthority(testAuth);
        result = engine.evaluateLogic();
        assertEquals(true, result);
        System.out.printf("Default Constructor: Authority %d -> %b\n",testAuth,result);

        // Authority will never be negative but it handles it anyhow

        testAuth = -1;
        testBlock.setAuthority(testAuth);
        result = engine.evaluateLogic();
        assertEquals(false, result);
        System.out.printf("Default Constructor: Authority %d -> %b\n",testAuth,result);


        testAuth = -2;
        testBlock.setAuthority(testAuth);
        result = engine.evaluateLogic();
        assertEquals(false, result);
        System.out.printf("Default Constructor: Authority %d -> %b\n",testAuth,result);
    }

    @Test
    @DisplayName("Testing custom criteria settings for GreaterThanOrEqual Rule")
    public void test2 () throws Exception {
        listener = new HasAuthorityPLCInput("Auth",testBlock, HasAuthorityPLCInput.AuthRule.TrueWhenGreaterOrEqual, 0);
        engine.registerInputSource(listener);
        boolean result;
        int nCriteria = 10;
        int nTestAuth = 20;

        for (int criteria =0; criteria<nCriteria; criteria++) {
            listener.setNewCriteria(criteria);
            for (int testAuth = 0; testAuth < nTestAuth; testAuth++) {

                // Correct >= usage of authority as input to PLC
                testBlock.setAuthority(testAuth);
                result=engine.evaluateLogic();
                assertEquals(testAuth >= criteria,result);
                System.out.printf("Auth[%d] (Rule %s) criteria[%d] -> PLC %b\n",testAuth,listener.rule(),listener.criteria(),result);

            }
        }
    }

    @Test
    @DisplayName("Testing custom criteria settings for LessThanOrEqual Rule")
    public void test3 () throws Exception {
        listener = new HasAuthorityPLCInput("Auth",testBlock, HasAuthorityPLCInput.AuthRule.TrueWhenLessOrEqual, 0);
        engine.registerInputSource(listener);
        boolean result;
        int nCriteria = 10;
        int nTestAuth = 20;

        for (int criteria =0; criteria<nCriteria; criteria++) {
            listener.setNewCriteria(criteria);
            for (int testAuth = 0; testAuth < nTestAuth; testAuth++) {

                // Correct <= usage of authority as input to PLC
                testBlock.setAuthority(testAuth);
                result=engine.evaluateLogic();
                assertEquals(testAuth <= criteria,result);
                System.out.printf("Auth[%d] (Rule %s) crit[%d] -> PLC %b\n",testAuth,listener.rule(),listener.criteria(),result);

            }
        }
    }
}