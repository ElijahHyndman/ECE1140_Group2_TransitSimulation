package WaysideController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/** engine for compiling a structured PLC script and evaluating the PLC logic given a set of inputs.
 * @author Harsh Selokar
 */
public class PLCEngine {
    /***********************************************************************************************************************/
    public enum Token {
        AND, OR, NOT, SET, LD, LDN, ANB, ORB
    }
    //constants
    int DEFAULT_OUTPUTS = 1;
    //construction variables
    private Queue<Token> tokenQueue;
    private Queue<String> varQueue;
    private int numberOfInputs;
    private List<String> PLCList;
    /***********************************************************************************************************************/

    public PLCEngine() {
        this.tokenQueue = new LinkedList<>();
        this.varQueue = new LinkedList<>();
        this.numberOfInputs = numberOfInputs;
    }

    public Queue<String> getVarQueue(){
        return varQueue;
    }

    public Queue<Token> getTokenQueue(){
        return tokenQueue;
    }

    public List<String> getPLCStringList() { return PLCList; }



    /*
        Token Creation
     */


    /** loads a list of PLC commands (Strings) into engine as new executable logic.
     *
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
                tokenQueue.add(Token.LDN);
                varQueue.add(var);
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

                tokenQueue.add(Token.LD);
                varQueue.add(var);
                numberOfInputs++;
            /*
            Logic Command
            */
            } else {
                switch(data.get(i)) {
                    case "SET" :
                        tokenQueue.add(Token.SET);
                        break;
                    case "ANB" :
                        tokenQueue.add(Token.ANB);
                        break;
                    case "AND" :
                        tokenQueue.add(Token.AND);
                        break;
                    case "ORB" :
                        tokenQueue.add(Token.ORB);
                        break;
                    case "OR" :
                        tokenQueue.add(Token.OR);
                        break;
                    case "NOT" :
                        tokenQueue.add(Token.NOT);
                        break;
                    default :
                        System.out.print(data.get(i));
                        throw new IOException("Compiler Failure: Commands do not exists or are misspelled...");
                }
            }
        }
    }


    /** uploads PLC script into engine from specified PLC file.
     * @param PLCFilePath
     * @throws IOException
     * @throws URISyntaxException
     */
    public void uploadPLC(String PLCFilePath) throws IOException, URISyntaxException {
        List<String> PLCCommands = stringTokensFromFile(PLCFilePath);
        uploadPLC(PLCCommands);
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

        System.out.print("Reading - ");
        System.out.println(url);
        path = Paths.get(url);
        bytes = Files.readAllBytes(path);
        allData = Files.readAllLines(path, StandardCharsets.UTF_8);

        for(int i=0;i < allData.size();i++){
            if(allData.get(i).trim().isEmpty()){
                allData.remove(i);
            }
        }

        this.PLCList = allData;
        return allData;
    }


    /*
        Logic Calculation
     */


    /** generates an output using logic (DEPRECATED)
     *
     * @param inputNames List<String>
     * @pararm inputs, boolean[] list of current values for boolean inputs
     * @return boolean output value after logic calculation
     */
    public boolean calculateOutputLogic(List<String> inputNames, boolean[] inputs) throws IOException {
        Stack<String> stack = new Stack<>();
        Queue<String> var = new LinkedList<>(varQueue);
        Queue<Token> tok = new LinkedList<>(tokenQueue);
        Token temp;
        boolean output;
        int pos;
        int sizeTok;
        int sizeStack;

        if(checkTokenQueue(var, tok, inputNames, inputs)){
            throw new IOException("Generate Error: Bad Token Queue...");
        }

        /*
            First Token
         */
        //first token MUST be a load so there is some input
        tok.remove().toString();
        stack.push(var.remove());
        pos = inputNames.indexOf(stack.pop());
        if(pos == -1){
            throw new IOException("Generate Error: First command token in PLC file must be a LD token. First token is (%s)".formatted());
        }
        output = inputs[pos];

        /*
            Rest of Tokens
         */
        sizeTok = tok.size();
        for(int i = 0; i < sizeTok;i++){
            temp = tok.remove();

            if(temp == Token.LD){
                stack.push(var.remove());
            }else if(temp == Token.AND){
                sizeStack = stack.size();
                for(int j = 0;j < sizeStack;j++)  {
                    pos = inputNames.indexOf(stack.pop());
                    if(pos == -1){
                        throw new IOException("Generate Error: Bad Input Names...");
                    }
                    output = output & inputs[pos];
                }
            }else if(temp == Token.OR){
                sizeStack = stack.size();
                for(int j = 0;j < sizeStack;j++) {
                    pos = inputNames.indexOf(stack.pop());
                    if(pos == -1){
                        throw new IOException("Generate Error: Bad Input Names...");
                    }
                    output = output | inputs[pos];
                }
            }else if(temp == Token.NOT){
                output = !output;
            }else if(temp == Token.SET){
                break;
            }else{
                throw new IOException("Calculation Error: Token Corruption");
            }
        }

        return output;
    }


    /** generates an output using logic but optimized and offers more functions than V.1
     * @param inputNames
     * @param inputs
     * @param
     */
    public boolean calculateOutputLogicNew(List<String> inputNames, boolean[] inputs) throws IOException {
        Stack<Boolean> stackBoolean = new Stack<>();
        Queue<String> var = new LinkedList<>(varQueue);
        Queue<Token> token = new LinkedList<>(tokenQueue);
        Token temp;

        boolean logicOutput;
        boolean loadedBoolean;
        boolean output = false;
        int tokenQueueSize;
        int stackSize;
        int pos;
        String varName;

        if(checkTokenQueue(var, token, inputNames, inputs)){
            throw new IOException("Generate Error: Bad Token Queue...");
        }

        //first token MUST be a load so there is some input
        tokenQueueSize = token.size();
        for(int i = 0; i < tokenQueueSize; i++){
            temp = token.remove();
            //System.out.println(temp.toString());
            if(temp == Token.LD){ //needs catches in case the code isn't compiling correctly...
                varName = var.remove();
                pos = inputNames.indexOf(varName);
                loadedBoolean = inputs[pos];
                stackBoolean.push(loadedBoolean);
            }else if(temp == Token.LDN){
                varName = var.remove();
                pos = inputNames.indexOf(varName);
                loadedBoolean = !inputs[pos];
                stackBoolean.push(loadedBoolean);
            }else if(temp == Token.ANB){
                logicOutput = stackBoolean.pop() & stackBoolean.pop();
                stackBoolean.push(logicOutput);
            }else if(temp == Token.AND){
                stackSize = stackBoolean.size();
                if(stackSize < 2) {
                    throw new IOException("Generate Error: Trying to AND 1 member...");
                }

                logicOutput = stackBoolean.pop() & stackBoolean.pop();

                if(stackSize > 2){
                    for(int j=0;j < stackSize-2;j++){
                        logicOutput = logicOutput & stackBoolean.pop();
                    }
                }

                //System.out.println(logicOutput);
                stackBoolean.push(logicOutput);
            }else if(temp == Token.ORB){
                logicOutput = stackBoolean.pop() | stackBoolean.pop();
                //System.out.println(logicOutput);
                stackBoolean.push(logicOutput);
            }else if(temp == Token.OR){
                stackSize = stackBoolean.size();
                if(stackSize < 2) {
                    throw new IOException("Generate Error: Trying to OR 1 member...");
                }

                logicOutput = stackBoolean.pop() | stackBoolean.pop();

                if(stackSize > 2){
                    for(int j=0;j < stackSize-2;j++){
                        logicOutput = logicOutput | stackBoolean.pop();
                    }
                }

                //System.out.println(logicOutput);
                stackBoolean.push(logicOutput);
            }else if(temp == Token.NOT){
                logicOutput = !stackBoolean.pop();
                //System.out.println(logicOutput);
                stackBoolean.push(logicOutput);
            }else if (temp == Token.SET){
                if(stackBoolean.size() ==  1){
                    output = stackBoolean.pop();
                    break;
                }else{
                    throw new IOException("Generate Error: Stack not completely empty, make sure PLC script leads to 1 output.");
                }
            }
        }

        return output;
    }

    /**
     *
     * @param inputNames
     * @return
     * @throws IOException
     */
    public boolean[][] calculateOutputMap(List<String> inputNames) throws IOException {
        numberOfInputs = varQueue.toArray().length;
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
            outputTable[i][numberOfInputs] = calculateOutputLogic(inputNames, temp[i]);
        }

        return outputTable;
    }

    /** generates an output of all truths with optimized generator
     *
     * @param inputNames
     * @return
     * @throws IOException
     */
    public boolean[][] calculateOutputMapNew(List<String> inputNames) throws IOException {
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
            outputTable[i][numberOfInputs] = calculateOutputLogicNew(inputNames, temp[i]);
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
}
