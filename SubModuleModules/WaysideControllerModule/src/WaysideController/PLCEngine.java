package WaysideController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/** engine for compiling a structured PLC script and evaluating the PLC logic given a set of inputs.
 * One PLC engine will correspond to only one PLC script.
 * @author Harsh Selokar
 */
public class PLCEngine {
    /***********************************************************************************************************************/
    public enum Token {
        AND, OR, NOT, SET, LD, LDN, ANB, ORB
    }
    //constants
    int DEFAULT_OUTPUTS = 1;
    /** Members
     * @member PLCLines     Queue<PLCLine> list of all of the lines from the PLC text file, kept in order, stored in queue for logic evaluating
     * @member PLCList      List<String> list of all of the lines from PLC text file (stored as String instead of PLCLine objects)
     * @member filePath     String, path to the current file uploaded to this PLCEngine
     */
    private Queue<PLCLine> PLCLines = new LinkedList<PLCLine>();
    private int numberOfInputs;
    private List<String> PLCList;
    private String filePath = "";
    /***********************************************************************************************************************/

    public PLCEngine() {
        super();
    }
    public PLCEngine(String pathToPLCFile) throws IOException, URISyntaxException {
        uploadPLC(pathToPLCFile);
    }

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
        Token Creation
     */


    /** loads a list of PLC commands (Strings) into engine as new executable logic.
     *
     * generates Enum token list from given String token list
     * @param data, List<String> list of PLC commands as Strings
     * @throws IOException unidentified command token in PLC file or invalid variable referenced
     * @throws URISyntaxException
     */
    public void uploadPLC(List<String> data) throws IOException, URISyntaxException {
        // List<String> data = readFileNew(file);
        String[] loadStr;
        String var;

        for(int i = 0;i < data.size();i++){

            /*
            LDN Command
            */
            if(data.get(i).startsWith("LDN")) {
                loadStr = data.get(i).split(" ");
                var = loadStr[1]; //variables created by the user

                //checks to see if string is good
                if (!stringIsCharactersAndNumbers(var)) {
                    System.out.print(var);
                    throw new IOException("Compiler Failure: Variables must only use letters with no spaces...");
                }
                //tokenQueue.add(Token.LDN);
                //varQueue.add(var);
                PLCLines.add( new PLCLine(Token.LDN,new PLCInput(var)) );
                numberOfInputs++;

            /*
            LD Command
            */
            }else if(data.get(i).startsWith("LD")) {
                loadStr = data.get(i).split(" ");
                var = loadStr[1]; //variables created by the user

                //checks to see if string is good
                if (!stringIsCharactersAndNumbers(var)) {
                    System.out.print(var);
                    throw new IOException("Compiler Failure: Variables must only use letters with no spaces...");
                }

                //tokenQueue.add(Token.LD);
                //varQueue.add(var);
                PLCLines.add( new PLCLine(Token.LD,new PLCInput(var)) );
                numberOfInputs++;

            /*
            Logic Command
            */
            } else {
                switch(data.get(i)) {
                    case "SET" :
                        //tokenQueue.add(Token.SET);
                        PLCLines.add( new PLCLine(Token.SET,null) );
                        break;
                    case "ANB" :
                        //tokenQueue.add(Token.ANB);
                        PLCLines.add( new PLCLine(Token.ANB,null) );
                        break;
                    case "AND" :
                        //tokenQueue.add(Token.AND);
                        PLCLines.add( new PLCLine(Token.AND,null) );
                        break;
                    case "ORB" :
                        //tokenQueue.add(Token.ORB);
                        PLCLines.add( new PLCLine(Token.ORB,null) );
                        break;
                    case "OR" :
                        //tokenQueue.add(Token.OR);
                        PLCLines.add( new PLCLine(Token.OR,null) );
                        break;
                    case "NOT" :
                        //tokenQueue.add(Token.NOT);
                        PLCLines.add( new PLCLine(Token.NOT,null) );
                        break;
                    default :
                        //System.out.print(data.get(i));
                        throw new IOException("Compiler Failure: Commands do not exists or are misspelled...");
                }
            }
        }
        // Store the list for later reference
        this.PLCList = data;
    }


    /** uploads PLC script into engine from specified PLC file.
     *
     *  generates Enum token list from PLC commands in PLC file
     * @param PLCFilePath
     * @throws IOException
     * @throws URISyntaxException
     */
    public void uploadPLC(String PLCFilePath) throws IOException, URISyntaxException {
        List<String> PLCCommands = stringTokensFromFile(PLCFilePath);
        uploadPLC(PLCCommands);

        // Store file path for later
        this.filePath = PLCFilePath;
    }


    /**
     * @param url
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public List<String> stringTokensFromFile(String url) throws IOException, URISyntaxException {
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

    /** evaluates the output of the current PLC script based on a given list of current inputs.
     *
     * @param in
     * @return
     * @throws IOException
     */
    public boolean evaluateLogic(List<PLCInput> in) throws Exception {
        boolean output = false;
        int stackSize;
        boolean logic;
        int tokenQueueSize;

        // Local calculation queues
        Stack<Boolean> evaluationStack = new Stack<>();
        Queue<PLCLine> source = new LinkedList<>(PLCLines);

        // Temporary variables
        PLCLine thisPLCLine;
        PLCInput varReference;
        PLCInput var;

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
                logic = var.evaluate();
                evaluationStack.push(logic);
            /***LDN operation */
            }else if(thisPLCLine.operation == Token.LDN){
                varReference = thisPLCLine.variable;
                var = variableReferenceBy(varReference,in);
                logic = !var.evaluate();
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
        try {
            actualVariable = variables.get( variables.indexOf(reference) );
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

    /** helper class for organizing PLC operations
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
    }
}
