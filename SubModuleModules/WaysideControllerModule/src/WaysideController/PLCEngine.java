package WaysideController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Harsh Selokar
 */

public class PLCEngine {
    public enum Token {
        AND, OR, NOT, SET, LD, LDN, ANB, ORB
    }

    //constants
    int DEFAULT_OUTPUTS = 1;

    //construction variables
    private boolean[] outputs;

    //use these for now
    private Queue<Token> tokenQueue;
    private Queue<String> varQueue;
    private int numberOfInputs;
    private List<String> PLCString;

    public PLCEngine() {
        this.outputs = new boolean[DEFAULT_OUTPUTS];
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

    public List<String> getPLCString() { return PLCString; }

    /*
    Function -
        Generators an output using logic (DEPRECATED)
    Input -
        inputNames: A list of string whose index correspond to specific index of the inputs
        inputs: A arr of boolean which store the input at a given time
    Output -
        output: boolean values of the output based on inputs
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

        //first token MUST be a load so there is some input
        tok.remove().toString();
        stack.push(var.remove());
        pos = inputNames.indexOf(stack.pop());
        if(pos == -1){
            throw new IOException("Generate Error: Bad Input Names...");
        }
        output = inputs[pos];

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

    /*
    Function -
        Generators an output using logic. The inputs are assumed to be in order from left to right.(DEPRECATED)
    Input -
        inputs: A arr of boolean which store the input at a given time
    Output -
        output: boolean values of the output based on inputs
     */
    public boolean calculateOutputLogic(boolean[] inputs) throws IOException {
        Stack<Token> stack = new Stack<>();
        Queue<Token> tok = new LinkedList<>(tokenQueue);
        Token temp;
        boolean output;
        int pos = 0;
        int sizeTok;
        int sizeStack;

        //first token MUST be a load so there is some input
        stack.push(tok.remove());
        output = inputs[pos];

        sizeTok = tok.size();
        for(int i = 0; i < sizeTok;i++){
            temp = tok.remove();
            if(temp == Token.LD){
                stack.push(temp);
            }else if(temp == Token.AND){
                sizeStack = stack.size();
                for(int j = 0;j < sizeStack;j++)  {
                    stack.pop();
                    output = output & inputs[pos];
                    pos++;
                }
            }else if(temp == Token.OR){
                sizeStack = stack.size();
                for(int j = 0;j < sizeStack;j++)  {
                    stack.pop();
                    output = output | inputs[pos];
                    pos++;
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

    /*
    Function -
        Generators an output using logic but optimized and offers more functions than V.1
    Input -
        inputNames: A list of string whose index correspond to specific index of the inputs
        inputs: A arr of boolean which store the input at a given time
    Output -
        output: boolean values of the output based on inputs
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

    /*
    Function -
        Generators an output of all truths (DEPRECATED)
    Input -
        inputNames: A list of string whose index correspond to specific index of the inputs
    Output -
        outputTablese: boolean values of the output based on inputs of all inputs, creating a truth table of sorts
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

    /*
    Function -
        Generators an output of all truths with optimized generator
    Input -
        inputNames: A list of string whose index correspond to specific index of the inputs
    Output -
        output tables: boolean values of the output based on inputs of all inputs, creating a truth table of sorts
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

    /*
    Function -
        Initializes the tokens for the queues and objectifizes the PLC script.
    Input -
        file: name of the file that is being searched for, stored in txt in same folder as WaysideController.PLCEngine
    Output - None
     */
    public void createTokens(String file) throws IOException, URISyntaxException {
        List<String> data = readFileNew(file);
        String[] loadStr;
        String var;

        for(int i = 0;i < data.size();i++){
            if(data.get(i).startsWith("LDN")) {
                loadStr = data.get(i).split(" ");
                var = loadStr[1]; //variables created by the user

                //checks to see if string is good
                if (!verifyString(var)) {
                    System.out.print(var);
                    throw new IOException("Compiler Failure: Variables must only use letters with no spaces...");
                }

                tokenQueue.add(Token.LDN);
                varQueue.add(var);
                numberOfInputs++;
            }else if(data.get(i).startsWith("LD")) {
                loadStr = data.get(i).split(" ");
                var = loadStr[1]; //variables created by the user

                //checks to see if string is good
                if (!verifyString(var)) {
                    System.out.print(var);
                    throw new IOException("Compiler Failure: Variables must only use letters with no spaces...");
                }

                tokenQueue.add(Token.LD);
                varQueue.add(var);
                numberOfInputs++;
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


    /*
    Function -
        Finds the file and writes all data into a List, file cannot have spaces at the end.
    Input -
        file: name of the file that is being searched for, stored in txt in same folder as WaysideController.PLCEngine
    Output -
        allData: all data in the file in order, in a list form
     */
    public List<String> readFile(String file) throws IOException, URISyntaxException {
        byte[] bytes;
        String str;
        List<String> allData;

        URL url = getClass().getResource(file);
        System.out.println(url.toURI().toString());
        Path path = Paths.get(url.toURI());
        bytes = Files.readAllBytes(path);
        allData = Files.readAllLines(path, StandardCharsets.UTF_8);

        for(int i=0;i < allData.size();i++){
            if(allData.get(i).trim().isEmpty()){
                allData.remove(i);
            }
        }

        this.PLCString = allData;
        return allData;
    }

    public List<String> readFileNew(String url) throws IOException, URISyntaxException {
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

        this.PLCString = allData;
        return allData;
    }

    public void updateFile(String file) throws IOException, URISyntaxException {
        byte[] bytes;
        String str;
        List<String> allData;

        URL url = getClass().getResource(file);
        System.out.println(url.toURI().toString());
        Path path = Paths.get(url.toURI());
        bytes = Files.readAllBytes(path);
        allData = Files.readAllLines(path, StandardCharsets.UTF_8);

        for(int i=0;i < allData.size();i++){
            if(allData.get(i).trim().isEmpty()){
                allData.remove(i);
            }
        }

        this.PLCString = allData;
    }

    /*
    Function -
        Checks if string is just letters
    Input -
        str: str that is being tested
    Output -
        bool: if the data is good, send true
     */
    public boolean verifyString(String str){
        char[] chars = str.toCharArray();

        for(char c : chars) {
            if(!Character.isLetter(c) && !Character.isDigit(c)){
                return false;
            }
        }

        return true;
    }

    /*
    Function -
        checks if the Token Queue is generally valid
    Input -
        s: this is the variable Queue
        q: this is the token Queue
        inputNames: names of inputs options
        inputs: inputs
    Output -
        bool: if the data is good and checks tests, true otherwise false
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
}
