package WaysideController;

import WaysideGUI.WaysideUIClass;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import TrackConstruction.*;

public class WaysideSystem {

    String currentLine;
    List<WaysideController> controllers;
    ArrayList<TrackElement> blocks;
    int numberOfControllers;

    //each track element has a wayside controller, this is an easy way to find each one!
    HashMap<TrackElement, WaysideController> lut;

    public WaysideSystem() throws IOException{
        currentLine = "Green";
        controllers = new LinkedList<>();
        numberOfControllers = 0;
        generateLine();
    }

    //This construction is bad, doesn't use the lut at all...
    public WaysideSystem(LinkedList<WaysideController> controllers) throws IOException {
        currentLine = "Green";
        this.controllers = controllers;
        numberOfControllers = controllers.size();
        generateLine();
    }

    public WaysideSystem(ArrayList<TrackElement> blocks, String line) throws IOException {
        currentLine = line;
        controllers = new LinkedList<WaysideController>();
        this.blocks = blocks;
        numberOfControllers = 0;
    }

    /*
    Helper function that gets all the wayside controllers within the system in a vector
     */
    public Vector<WaysideController> getControllersVector() {
        Vector<WaysideController> out = new Vector<WaysideController>();
        for(WaysideController controller : controllers) {
            out.add(controller);
        }
        return out;
    }

    /*
    add a wayside controller
     */
    public void addWaysideController(int[] blockNumbers) throws IOException {
        ArrayList<TrackElement> elementArrayList = findAllElements(blockNumbers);
        ArrayList<TrackBlock> blockArrayList = findAllBlocks(blockNumbers);
        String controllerName = "Controller " + Integer.toString(++numberOfControllers);

        WaysideController controller = new WaysideController(elementArrayList, blockArrayList, controllerName);
        controllers.add(controller);

        TrackElement trackElement;
        for(int i=0;i < blockNumbers.length;i++){
            trackElement = getBlockElement(blockNumbers[i], blocks);
            lut.put(trackElement, controller);
        }
    }

    //helper function that finds all the track ELEMENTS with the corresponding blocks numbers and returns the array list (needs improvement)
    public ArrayList<TrackElement> findAllElements(int[] blockNumbers) throws IOException {
        ArrayList<TrackElement> newElementSet = new ArrayList<>();

        //needs check to see if the blocks numbers are even part of the system, if not, throw exception!

        for(int i=0;i < blockNumbers.length;i++){
            for(int j=0;j < blocks.size();j++){
                if((blocks.get(j).getBlockNum() == blockNumbers[i]) && (!newElementSet.contains(blocks.get(j)))){
                    newElementSet.add(blocks.get(j));
                }
            }
        }

        return newElementSet;
    }

    //helper function that finds all the track BLOCKS with the corresponding blocks numbers and returns the array list (needs improvement)
    public ArrayList<TrackBlock> findAllBlocks(int[] blockNumbers) throws IOException {
        ArrayList<TrackBlock> newBlockSet = new ArrayList<>();

        //needs check to see if the blocks numbers are even part of the system, if not, throw exception!

        for(int i=0;i < blockNumbers.length;i++){
            for(int j=0;j < blocks.size();j++){
                if((blocks.get(j).getBlockNum() == blockNumbers[i]) && (!newBlockSet.contains(blocks.get(j))) &&
                        (blocks.get(i).getType().equalsIgnoreCase("block"))){
                    newBlockSet.add((TrackBlock) blocks.get(j)); //casting should not change the reference.
                }
            }
        }

        return newBlockSet;
    }

    /*
    finds the corresponding controller for each block!
     */
    public WaysideController findController(int blockNumber){
        return lut.get(blockNumber);
    }

    /*
    somes lines are pre-defined, here is a quick way to generate the lines for the assignment!
     */
    public void generateLine() throws IOException {
        if(currentLine.equalsIgnoreCase("green") && (blocks.size() == 150)) {
            //NEEDS TO BE REPLACED WITH SOME METHOD THE CALLS GET THE TRACK FROM THE SIM ENVIRO OR IN PARAM
            String currentLine = "Green";
        }else{
            throw new IOException("Generation Error: Invalid line generation");
        }
    }

    /*

     */
    public boolean broadcastToControllers(int[] speeds, int[] authority) throws IOException {
        WaysideController temp;

        //RUN CHECKS TO MAKE SURE THE INPUTS ARE VALID HERE!

        for(int i=0;i < speeds.length;i++){
            temp = lut.get(i);
            temp.setSpeed(i, speeds[i]);
        }

        temp = null;
        for(int i=0;i < authority.length;i++){
            temp = lut.get(i);
            temp.setAuthority(i, authority[i]);
        }

        return false;
    }

    /*
    gets the occupancy from the proper controller
     */
    public boolean getOccupancy(int blockNumber) throws IOException {
        return findController(blockNumber).getGPIO().getOccupancy(blockNumber);
    }

    /*

     */
    public boolean getSwitchStatus(int blockNumber) throws IOException {
        return findController(blockNumber).getSwitchStatus(blockNumber);
    }

    /*

     */
    public void setSwitchStatus(int blockNumber, boolean status) throws IOException {
        findController(blockNumber).setSwitchStatus(blockNumber ,status);
    }

    /*

     */
    public void setClose(int blockNumber){

    }

    /*

     */
    public void openClose(int blockNumber) {

    }

    /*
    helper function - finds the specific block element from the block number
    */
    public TrackElement getBlockElement(int blockNumber, ArrayList<TrackElement> blocks) throws IOException {
        for(int i = 0; i < blocks.size(); i++){
            if(blockNumber == blocks.get(i).getBlockNum()){
                return blocks.get(i);
            }
        }

        throw new IOException("Controller Error: No block with that number in the wayside...");
    }

    /*
    Function -
        Reads the console and uses that data to perform actions on the entire system
    Input -
        commandLine: the name of the input
            current commands
            --upload controller PLCfile outputName
                add a given output to the controller (will be able to be used to connect aswell)
            --compile controller outputName
                add
    Output -
        String: This will describe what is happening with the command
     */
    public String readConsole(String commandLine) throws IOException, URISyntaxException {
        String[] commands = commandLine.split(" ");

        String controllerName;
        String PLCfile;
        String blockNumber;

        //test if valid input was inputed
        if(commands.length <= 0 || commands.length > 4) {
            return "Command Error: The current command has bad param/or is invalid...";
        }

        //upload
        if(commands[0].equals("upload")) { //UPLOAD COMMAND
            controllerName = commands[1];
            PLCfile = commands[2];
            blockNumber = commands[3];
            int nameIndex = -1;
            String currName;

            for(int i=0;i < controllers.size();i++){
                currName = controllers.get(i).getName();
                if(currName.equals(controllerName)){
                    nameIndex = i;
                }
            }

            if(nameIndex == -1){
                throw new IOException("Command Error: Bad Name...");
            }

            controllers.get(nameIndex).addOutput(Integer.parseInt(blockNumber),PLCfile);
            return "You have successfully added an output on " + controllerName;
        }else if(commands[0].equals("update")){ //COMPILE COMMAND
            controllerName = commands[1];
            PLCfile = commands[2];
            blockNumber = commands[3];
            int nameIndex = -1;
            String currName;

            for(int i=0;i < controllers.size();i++){
                currName = controllers.get(i).getName();
                if(currName.equals(controllerName)){
                    nameIndex = i;
                }
            }

            if(nameIndex == -1){
                throw new IOException("Command Error: Bad Name...");
            }

            controllers.get(nameIndex).updateOutput(Integer.parseInt(blockNumber),PLCfile);
            return "You have successfully updated a PLC on " + controllerName;
        }if(commands[0].equals("add")){ //COMPILE COMMAND
            controllerName = commands[1];

            int nameIndex = -1;

            if(commands[1].equals("default")){
                nameIndex = 1;
            }

            if(nameIndex == -1){
                throw new IOException("Command Error: Bad Name...");
            }

            //generateTestController();
            return "Nice new controller!";
        }else{
            return "Command Error: The current command was invalid...";
        }
    }

    //TEST GETTERS AND SETTER ******************************************************************************************
    public HashMap<TrackElement, WaysideController> getLut(){
        return lut;
    }

    public static void main(String[] args) throws IOException {
        WaysideSystem testSystem = new WaysideSystem();
        WaysideUIClass guiUpdater;

        //WaysideController testControllers = generateTestController();
        //testSystem = new WaysideSystem(testControllers);
        //testSystem.generateTestController();
        guiUpdater = new WaysideUIClass(testSystem);

        System.out.println("starting GUI");
        try {
            guiUpdater.start();
        } catch (Exception e) {
            System.out.println("failed when running");
        }
    }
}