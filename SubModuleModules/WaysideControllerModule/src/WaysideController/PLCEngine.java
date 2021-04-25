package WaysideController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/** engine for compiling a structured PLC script of boolean operations using generic, evaluateable boolean input sources PLCInput.
 * One PLC engine will correspond to only one PLC script.
 * One output will be generated per PLCEngine.
 * PLCEngine operates independent of the source of boolean inputs. Deriving boolean input sources from unique source (such as for the boolean derivation from a TrackElement's occupancy)
 * @author Harsh Selokar
 * @author elijah
 */
public class PLCEngine {
    /***********************************************************************************************************************/
    public enum Token {
        AND, OR, NOT, SET, LD, LDN, ANB, ORB
    }

    /** Default Members
     */
    private static final int DEFAULT_OUTPUTS = 1;
    private static final int MINIMUM_LINES_IN_VALID_PLC_SCRIPT = 2;
    /** Members
     * @member PLCLines     Queue<PLCLine> list of all of the lines from the PLC text file, kept in order, stored in queue for logic evaluating
     * @member PLCList      List<String> PLCLines as String instead of PLCLine objects
     * @member filePath     String, path to the current file uploaded to this PLCEngine
     */
    // PLC Script
    private Queue<PLCLine> PLCLines = new LinkedList<PLCLine>();
    private List<String> PLCList = new LinkedList<String>();

    // Input Sources
    private List<PLCInput> PLCInputSources = new LinkedList<PLCInput>();

    // Output Source
    private PLCOutput outputTarget = null;

    private int numberOfInputs;
    private String filePath = "";
    /***********************************************************************************************************************/

    public PLCEngine() {
        super();
    }
    public PLCEngine(String pathToPLCFile) throws Exception {
        uploadPLC(pathToPLCFile);
    }

    // Get and Set
    public List<String> getPLCStringList() { return PLCList; }
    public String getPLCString() {
        String PLC = "";
        for (String line : PLCList) {
            // Each PLC command should be put on a new line
            PLC += line + "\n";
        }
        return PLC;
    }


    /*
        PLC Uploading
     */


    /** loads a list of PLC commands (Strings) into engine as new executable logic.
     *
     * generates Enum token list from given String token list
     * @param PLCCommands, List<String> list of PLC commands as Strings ex: <"LD A","LD B","SET">
     * @throws IOException unidentified command token in PLC file or invalid variable referenced
     * @throws URISyntaxException
     *
     * @before local PLC in PLCEngine may or may not exist
     * @after if new PLC has no errors, old local PLC has been overwritten by new PLC
     * @after empty proxy PLCinputs generated and ready to refer to new PLCInputs for logic calculations
     * @after PLCInput objects must be given for each proxy PLCInput defined in PLC list "PLCCommands" before calling .evaulateLogic()
     */
    public void uploadPLC(List<String> PLCCommands) throws Exception {
        // List<String> PLCCommands = readFileNew(file);
        String[] loadStr;
        String var;

        // Store PLC lines in temporary variable in case it is invalid
        Queue<PLCLine> tempLines = new LinkedList<PLCLine>();

        // Tokenize the PLCLines
        for(int i = 0;i < PLCCommands.size();i++){

            /*
            LDN Command
            */
            if(PLCCommands.get(i).startsWith("LDN")) {
                loadStr = PLCCommands.get(i).split(" ");
                var = loadStr[1]; //variables created by the user

                //checks to see if string is good
                if (!stringIsCharactersAndNumbers(var)) {
                    System.out.print(var);
                    throw new IOException("Compiler Failure: Variables must only use letters with no spaces...");
                }
                tempLines.add( new PLCLine(Token.LDN,new PLCInput(var)) );
                numberOfInputs++;

            /*
            LD Command
            */
            }else if(PLCCommands.get(i).startsWith("LD")) {
                loadStr = PLCCommands.get(i).split(" ");
                var = loadStr[1]; //variables created by the user

                //checks to see if string is good
                if (!stringIsCharactersAndNumbers(var)) {
                    System.out.print(var);
                    throw new IOException("Compiler Failure: Variables must only use letters with no spaces...");
                }
                tempLines.add( new PLCLine(Token.LD,new PLCInput(var)) );
                numberOfInputs++;

            /*
            Logic Command
            Do not take variable names as parameters (use null)
            */
            } else {
                switch(PLCCommands.get(i)) {
                    case "SET" :
                        //tokenQueue.add(Token.SET);
                        tempLines.add( new PLCLine(Token.SET,null) );
                        break;
                    case "ANB" :
                        //tokenQueue.add(Token.ANB);
                        tempLines.add( new PLCLine(Token.ANB,null) );
                        break;
                    case "AND" :
                        //tokenQueue.add(Token.AND);
                        tempLines.add( new PLCLine(Token.AND,null) );
                        break;
                    case "ORB" :
                        //tokenQueue.add(Token.ORB);
                        tempLines.add( new PLCLine(Token.ORB,null) );
                        break;
                    case "OR" :
                        //tokenQueue.add(Token.OR);
                        tempLines.add( new PLCLine(Token.OR,null) );
                        break;
                    case "NOT" :
                        //tokenQueue.add(Token.NOT);
                        tempLines.add( new PLCLine(Token.NOT,null) );
                        break;
                    default :
                        //System.out.print(PLCCommands.get(i));
                        throw new IOException("Compiler Failure: Commands do not exists or are misspelled...");
                }
            }
        }

        if (tempLines.size() < MINIMUM_LINES_IN_VALID_PLC_SCRIPT)
            throw new Exception(String.format("Attempting to load PLC script that does not meet the minimum number of lines for a valid PLC script (minimum number of lines = %d)",MINIMUM_LINES_IN_VALID_PLC_SCRIPT));
        if ( !tempLines.contains(new PLCLine(Token.SET, null)) )
            throw new Exception(String.format("Attempted to load PLC script that does not end with \"SET\". All PLC scripts must end with the command \"SET\""));


        // Store temporary list of commands if it passes tests
        this.PLCLines = tempLines;
        // Store the String version for later reference
        this.PLCList = PLCCommands;
    }


    /** takes the lines from a file, tokenizes it, and stores it locally as executeable PLC code
     *
     *  generates Enum token list from PLC commands in PLC file
     * @param PLCFilePath
     * @throws IOException
     * @throws URISyntaxException
     *
     * @before local PLC may or may not exist
     * @after (if file is accessible and valid PLC format) file PLC has overwritten previous local PLC. new file PLC is executable with evaluateLogic()
     */
    public void uploadPLC(String PLCFilePath) throws Exception {
        List<String> PLCCommands = stringTokensFromFile(PLCFilePath);
        uploadPLC(PLCCommands);

        // Store file path for later
        this.filePath = PLCFilePath;
    }


    /** opens a file and turns each line into a list of Strings.
     *  Useful for PLC input from file, as the uploadPLC(List<String>) takes a list of strings as its input
     * @param url
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private List<String> stringTokensFromFile(String url) throws IOException, URISyntaxException {
        byte[] bytes;
        String str;
        List<String> allData;
        Path path;

        // Pull lines from File
        System.out.print("Reading - ");
        System.out.println(url);
        path = Paths.get(url);
        bytes = Files.readAllBytes(path);
        allData = Files.readAllLines(path, StandardCharsets.UTF_8);

        // Remove empty lines
        for(int i=0;i < allData.size();i++){
            if(allData.get(i).trim().isEmpty()){
                allData.remove(i);
            }
        }
        return allData;
    }



    /*
        Logic Calculation
     */


    /** evaluates the boolean calculation defined by given PLCScript (Queue of PLCLines, in order of execution) using a given set of input statuses (in, collection of PLCInput objects).
     *
     * @param PLCScript, Queue<PLCLine> queue of PLCLine objects in their order of execution (order matters.) Can be defined and created and passed as ArrayList<PLCLine> so long as order is correct
     * @param in, The list of PLCInput variables which will be used as boolean input to evaluate PLC logic (there MUST be a PLCInput with a .variableName() for each variable referenced in the PLCScript stored locally "PLCLines")
     * @return boolean, the result of evaluation PLCScript logic using provided "in"
     * @throws IOException a variable name referenced in PLCScript (i.e. "LD aVariableName") does not correspond to any PLCInput variable names in "in" (i.e. in has a "new PLCInput(variableName="aVariableName",value=initialValueOfObject)")
     *
     * @after .evaluate() has been called on every PLCInput in "in" if they are referenced in PLCScript
     */
    public static boolean evaluateLogic(Queue<PLCLine> PLCScript, List<PLCInput> in) throws Exception {
        boolean output = false;
        int stackSize;
        boolean logic;
        int tokenQueueSize;

        // Local calculation queues
        // PLC Lines come from locally stored PLC script in PLCLines member
        Stack<Boolean> evaluationStack = new Stack<>();
        Queue<PLCLine> source = new LinkedList<>(PLCScript);//new LinkedList<>(PLCLines);

        // Temporary variables
        PLCLine thisPLCLine;
        PLCInput varReference;
        PLCInput var;

        if(PLCScript.size() < MINIMUM_LINES_IN_VALID_PLC_SCRIPT)
            throw new Exception(String.format("Tried to evaluate logic for a PLC script that does not meet the minimum valid number of PLC commands (minimum lines to be valid = %d)",MINIMUM_LINES_IN_VALID_PLC_SCRIPT));

        /* uses a stack to evaluate the logic of the PLC. PLC commands operate on themselves, the command result of the one before, or both so a queue will be used.
         * output: (Goal) the final value result
         * logic: the value we get after evaluating this line of the PLC script
         *
         * evaulationStack: the queue datastructure we use to load and unload values to track the progress of the calculation (will be empty by end)
         * source: a copy of the PLC Line queue that allows us to pop without affecting the class member (determines our next operation)
         */

        //first token MUST be a load so there is some input
        if(source.peek().operation != Token.LD) {
            String error = String.format("PLC Logic Error: The first line of the PLC script must be a LD to have boolean variables to work with.\n");
            error+=String.format("The operation PLC is detected as ENUM: %s\n",source.peek().operation);
            error+=String.format("The first line of PLC detected as: %s\n",source.peek());
            throw new Exception(error);
        }

        tokenQueueSize = source.size();
        for(int i = 0; i < tokenQueueSize; i++){
            // Get next operation
            thisPLCLine = source.remove();

            //System.out.println(temp.toString());
            /***LD operation */
            if(thisPLCLine.operation == Token.LD){ //needs catches in case the code isn't compiling correctly...
                varReference = thisPLCLine.variable;
                var = variableReferenceBy(varReference,in);
                try { logic = var.evaluate(); } catch (Exception failureToEvaulateInput) { failureToEvaulateInput.printStackTrace(); throw new Exception(String.format("Failure to evaluate PLCInput variable (%s) during LD operation.\nVariable Name: (%S)\n",thisPLCLine,var.variableName()));}
                evaluationStack.push(logic);
            /***LDN operation */
            }else if(thisPLCLine.operation == Token.LDN){
                varReference = thisPLCLine.variable;
                var = variableReferenceBy(varReference,in);
                try { logic = !var.evaluate(); } catch (Exception failureToEvaulateInput) { failureToEvaulateInput.printStackTrace(); throw new Exception(String.format("Failure to evaluate PLCInput variable (%s) during LDN operation.\nVariable Name: (%S)\n",thisPLCLine,var.variableName()));}
                evaluationStack.push(logic);
            /***ANB operation */
            }else if(thisPLCLine.operation == Token.ANB){
                // AND last two values, no loading
                logic = evaluationStack.pop() & evaluationStack.pop();
                evaluationStack.push(logic);
            /***AND operation */
            }else if(thisPLCLine.operation == Token.AND){
                stackSize = evaluationStack.size();
                if(stackSize < 2) {
                    throw new IOException("Generate Error: Trying to AND 1 member...");
                }
                // AND last two values, no loading
                logic = evaluationStack.pop() & evaluationStack.pop();
                // And the rest of the stack
                if(stackSize > 2){
                    for(int j=0;j < stackSize-2;j++){
                        logic = logic & evaluationStack.pop();
                    }
                }
                evaluationStack.push(logic);
            /***ORB operation */
            }else if(thisPLCLine.operation == Token.ORB){
                // OR the last two values, no loading
                logic = evaluationStack.pop() | evaluationStack.pop();
                evaluationStack.push(logic);
            /***OR operation */
            }else if(thisPLCLine.operation == Token.OR){
                stackSize = evaluationStack.size();
                if(stackSize < 2) {
                    throw new IOException("Generate Error: Trying to OR 1 member...");
                }
                logic = evaluationStack.pop() | evaluationStack.pop();
                if(stackSize > 2){
                    for(int j=0;j < stackSize-2;j++){
                        logic = logic | evaluationStack.pop();
                    }
                }

                //System.out.println(logicOutput);
                evaluationStack.push(logic);
            /*NOT operation */
            }else if(thisPLCLine.operation== Token.NOT){
                logic = !evaluationStack.pop();
                //System.out.println(logicOutput);
                evaluationStack.push(logic);
            /*SET operation*/
            }else if (thisPLCLine.operation == Token.SET){
                if(evaluationStack.size() ==  1){
                    output = evaluationStack.pop();
                    break;
                }else{
                    throw new IOException("Generate Error: Stack not completely empty, make sure PLC script leads to 1 output.");
                }
            }
        }

        return output;
    }

    /** calculate boolean logic using locally stored PLC Script (provided using .uploadPLCScript()) and provided input statuses (in)
     *
     * @param in
     * @return
     * @throws Exception
     */
    public boolean evaluateLogic(List<PLCInput> in) throws Exception {
        boolean calculatedOutput = evaluateLogic(PLCLines, in);
        return calculatedOutput;
    }


    /** generic form of evaluateLogic(inputStatuses), using locally stored member PLCLines as PLCScript (set using .uploadPLCScript()) and locally stored member PLCInputSources (set using .registerPLCInputSource) to perform calculation
     * every single variable reference in locally stored PLC script must have a corresponding inputsource definition set using .definePLCInputSource(PLCInputObjectThatHasSameNameAsPLCVariableReference)
     * uses evaluateLogic(inputs) to calculate values, passes locally defined input sources
     *
     * This is the function intended for use in WaysideController
     *
     * @return boolean, output of local PLCScript calculation (member PLCLines) using registered input sources (PLCInputs registered with .registerPLCInputSource())
     */
    public boolean evaluateLogic() throws Exception {
        // All input sources must be defined
        if (!allPLCInputSourcesDefined()) {
            throw new Exception("Called generic evaluateLogic() before all variable references in PLCScript have been given a corresponding input source using PLCEngine.definePLCInputSource()\nEnsure that all input sources have been defined.");
        }
        boolean calculatedOutputResult = evaluateLogic(PLCInputSources);
        if (outputTarget != null)
            try {
                outputTarget.applyOutputRule(calculatedOutputResult);
            } catch (Exception outputApplicationFailed) {
                outputApplicationFailed.printStackTrace();
                throw new Exception(String.format("Failure occurred when trying to apply output rule to PLCOutput object (%S) after evaluated PLCEngine logic for PLCEngine (%S)\n",outputTarget,this));
            }
        return calculatedOutputResult;
    }


    /** generates an output of all truths with optimized generator
     *
     * @param inputNames
     * @return
     * @throws IOException
     */
    public boolean[][] generateLogicTable(List<String> inputNames) throws IOException {
        numberOfInputs = inputNames.toArray().length;
        int rows = (int) Math.pow(2,numberOfInputs);
        boolean[][] temp = new boolean[rows][numberOfInputs];
        boolean[][] outputTable = new boolean[rows][numberOfInputs+1];

        for(int i=0;i < rows;i++){
            for(int j=numberOfInputs-1;j >= 0;j--){
                if((i/(int) Math.pow(2, j))%2 == 0){
                    temp[i][numberOfInputs-j-1] = false;
                }else if((i/(int) Math.pow(2, j))%2 == 1){
                    temp[i][numberOfInputs-j-1] = true;
                }else{
                    throw new IOException("Generate Error: You messed up bad...");
                }
            }
        }

        for(int i=0;i < rows;i++){
            for(int j=0;j < numberOfInputs;j++){
                outputTable[i][j] = temp[i][j];
            }
            //outputTable[i][numberOfInputs] = evaluateLogic(inputNames, temp[i]);
        }

        return outputTable;
    }


    /** defines a possible input source (PLCInput) for PLCScripts to reference when uploaded to this PLCEngine.
     *  the name of PLCInput variableName member must match the variable names referenced in PLCScript
     *  A PLCInputSource is allowed to be registered and not actually referenced by PLCScript. Registering an InputSource just adds it to the pool of searchable inputs
     *  i.e. if PLC references a variable such as:
     *  "
     *  LD aVariableName
     *  "
     *  then the PLC Engine must have an input source defined:
     *  "
     *  PLCInput actualVariable = new PLCInput("aVariableName", value=TheLogicalValueOfInputSource)
     *  PLCEngine.definePLCInputSource( actualVariable  )
     *  "
     *  before we can use the parameterless .evaluateLogic() function on PLCengine
     *
     * @param inputSource, PLCInput a PLCInput or object that extends PLCInput; inputSource.variableName will match variable(s) written in PLCScript
     * @before .evaluateLogic() does not know what $VARNAME in PLC script refers to
     * @after if inputSource variable name is $VARNAME, then .evaluateLogic will use inputSource.evaluate() to generate boolean inputs
     */
    public void registerPLCInputSource(PLCInput inputSource) {
        PLCInputSources.add(inputSource);
    }

    /** defines an output target object to whom an output rule will automatically be applied when .evaluateLogic() is called.
     *  custom behavior for applying outputRule can be defined by extending and overriding the PLCOutput class and its .applyOutputRule(boolean) function
     *
     * @param target, PLCOutput the nw target output for this PLCEngine's script (the one whom is referred to by the final "SET" command)
     * @before a target output for this PLCEngine may or may not be defined
     * @after the old target for this PLCEngine has been overwritten by the new, provided target
     * @after .applyOutputLogic will be called on target everytime generic .evaluateLogic() function is called on PLCEngine
     */
    public void registerPLCOutputTarget(PLCOutput target) {
        this.outputTarget = target;
    }


    /** checks that every variable reference in local PLCScript (PLCLines member) has a corresponding PLCInput (variable reference and PLCInput.variableName matches.)
     *  must be true before we can call generic .evaluateLogic() [ if we don't, then PLC script references variable that does not have a source definition]
     *
     * @return boolean, whether every PLC reference has a corresponding PLCInput registered locally with .registerPLCInputSource, true otherwise false
     * @throws Exception atleast one variable name referenced in PLC script does not have a corresponding PLCInput definition whose PLCInput.variableName matches reference
     */
    public boolean allPLCInputSourcesDefined() throws Exception {
        boolean thisReferenceHasSourceDefined = false;

        // Look through PLCLines for all variables, make sure each one has a local definition
        PLCLine line;
        for (Object o : PLCLines.toArray()) {
            // Had to .toArray to check all lines, but need to cast objects back to PLCLines :(
            if (! (o instanceof PLCLine))
                throw new Exception("Grave error when checking lines of PLC Script for local variable definitions. This exception should never be thrown.");
            line = (PLCLine) o;
            thisReferenceHasSourceDefined = PLCInputSources.contains(line.variable);

            // A PLC line that has a null variable name is a logic command (AND, OR, ANB, etc etc) so no variable definition is needed
            // Skip this line
            if (line.variable == null)
                continue;

            if (!thisReferenceHasSourceDefined)
                throw new Exception(String.format("No Input Source has been defined to PLCEngine using .definedPLCInputSource(PLCInputObject) for variable reference in PLC Script: %s\n",line.variable.variableName()));
            // Debug
//            if (thisReferenceHasSourceDefined)
//                System.out.printf("Variable reference (%s) has a defined input source \n",line.variable);
        }
        return true;
    }



    /*
        Helper Functions
     */

    /** gets the actual PLCInput referenced by a proxy PLCInput from a given list of PLCInput variables.
     *
     * takes advantage of the .equals() operation within .indexOf() List operation. reference is a proxy that shares the same name as a desired input "Var".
     * when we override .equals() to evaluate true when both PLCInput.variableName equals eachother. Since proxy shares the same name as Var, searching using proxy will return the index of Var.
     * use index of Var to return Var object.
     * @param reference, PLCInput a proxy input variable who references a specific, real input variable which can be found within variables (they share the same name)
     * @param variables, List<PLCInput> list of PLCInput variables to pull from, contains one PLCInput whose name matches the reference PLCInput
     * @return PLCInput of the actual, referenced variable if found (throw exception if not)
     * @throws IndexOutOfBoundsException none of the PLCInputs in "variables" share the same variableName as reference
     * @author elijah
     */
    public static PLCInput variableReferenceBy(PLCInput reference, List<PLCInput> variables) throws Exception {
        if (reference == null) {
            throw new Exception(String.format("Attempted to search reference between PLC reference and corresponding variable using NULL reference\nReference: %s\nList:%s",reference,variables));
        }
        if (variables.size() == 0) {
            throw new Exception(String.format("Attempted to search reference between PLC refereence and corresponding variable using empty, provided list\nReference: \"%s\"\nList:%s",reference,variables));
        }
        PLCInput actualVariable = null;
        int indexOfReferencedVariable = variables.indexOf(reference);
        if (indexOfReferencedVariable == -1) {
            // Variable referenced by proxy was not found amongst the variables list
            throw new Exception(String.format("Searched provided input list for variable named (%s) but failed to find it.\nEnsure that .equals() was defined if overloading PLCInput class",reference.variableName()));
        }
        try {
            actualVariable = variables.get( indexOfReferencedVariable );
        } catch (java.lang.IndexOutOfBoundsException variableReferencedByPLCWasNotInTheList) {
            // In the case that it is not found
            System.out.println("Attempted to search reference between PLC reference and corresponding variable but the variable was not found");
            System.out.println("Ensure that the names of the variables mentioned in PLC text file match the variable names written in code");
            System.out.printf("Reference in PLC: \"%s\"\n",reference);
            System.out.printf("Provided variable list:\n%s\n",variables);
            variableReferencedByPLCWasNotInTheList.printStackTrace();
            throw new java.lang.IndexOutOfBoundsException("Attempted to search reference between PLC reference and corresponding variable but the variable was not found");
        }
        return actualVariable;
    }


    /** checks if the Token Queue is generally valid
     * @param s, Queue<String> queue of PLC variables
     * @param q, Queue<String> queue of tokens
     * @param inputNames, List<String> names of inputs options
     * @param inputs, boolean[] list of boolean input values
     * @return bool if the data is good and checks tests, true otherwise false
     */
    public boolean checkTokenQueue(Queue<String> s, Queue<Token> q, List<String> inputNames, boolean[] inputs){
        Queue<String> temp = new LinkedList<>(s);

        for(int i = 0;i < temp.size();i++){
            if(inputNames.contains(temp.remove())){
                return false;
            }
        }

        if(!(q.peek() == Token.LD)){
            return false;
        }

        if(q.isEmpty()) {
            return false;
        }

        if(!q.contains(Token.SET)) {
            return false;
        }

        if(inputs.length == inputNames.size()){
            return false;
        }

        return true;
    }


    /*
        Other Functions
     */

    /** checks if string is just letters
     * @param str, String string that will be tested
     * @return bool whether string contains only Letters/Digits, true otherwise false
     */
    public boolean stringIsCharactersAndNumbers(String str){
        char[] chars = str.toCharArray();

        for(char c : chars) {
            if(!Character.isLetter(c) && !Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    /** represents one line of a PLC script (helper class for organizing PLC operations.)
     */
    private class PLCLine {
        public PLCInput variable = null;
        public Token operation = null;
        public PLCLine(Token operation, PLCInput inputVariable) {
            this.operation = operation;
            this.variable = inputVariable;
        }
        public String toString() {
            return String.format("%s %s", operation, variable);
        }
        @Override
        public boolean equals(Object o) {
            if(!(o instanceof PLCLine))
                return false;
            PLCLine other = (PLCLine) o;
            return (other.operation == this.operation && other.variable == this.variable);
        }
    }
}
