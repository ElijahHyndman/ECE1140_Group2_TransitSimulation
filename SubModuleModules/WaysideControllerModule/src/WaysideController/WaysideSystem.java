package WaysideController;

import Track.Track;
import TrackConstruction.TrackBlock;
import TrackConstruction.TrackElement;
import WaysideGUI.WaysideUIClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class WaysideSystem {

    private String waysideName;
    private ArrayList<TrackElement> trackSection;


    private List<WaysideController> controllers;


    private int numberOfControllers;
    private int[] authorities;
    private double[] speeds;

    //each track element has a wayside controller, this is an easy way to find each one!
    private HashMap<Integer, WaysideController> lut;

    public WaysideSystem() throws IOException{
        waysideName = "Green";
        controllers = new LinkedList<>();
        numberOfControllers = 0;
    }

    //This construction is bad, doesn't use the lut at all...
//    public WaysideSystem(LinkedList<WaysideController> controllers) throws IOException {
//        currentLine = "Green";
//        this.controllers = controllers;
//        numberOfControllers = controllers.size();
//    }

    public WaysideSystem(String waysideName, ArrayList<TrackElement> trackSection) throws IOException {
        this.waysideName = waysideName;
        this.trackSection = trackSection;
        controllers = new LinkedList<WaysideController>();
        this.lut = new HashMap<>();
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
        //ArrayList<TrackBlock> blockArrayList = findAllBlocks(blockNumbers);
        String controllerName = "Controller" + Integer.toString(++numberOfControllers);

        WaysideController controller = new WaysideController(elementArrayList, controllerName);
        controllers.add(controller);

        TrackElement trackElement;
        for(int i=0;i < blockNumbers.length;i++){
            trackElement = getBlockElement(blockNumbers[i], trackSection);
            lut.put(trackElement.getBlockNum(), controller);
        }
    }

    /*
    add output w/plc within a wayside controller
     */
    public void addOutputWaysideController(int blockNumber, String PLCfile) throws IOException, URISyntaxException {
        // TODO
//        getController(blockNumber).addOutput(blockNumber, PLCfile);
//        outputBlocks.add(getBlockElement(blockNumber, blocks));
//        getController(blockNumber).generateOutputSignal(blockNumber, false);
    }

    /*
add output w/plc within a wayside controller
 */
    public void updateOutputWaysideController(int blockNumber, String PLCfile) throws IOException, URISyntaxException {
        // TODO
//        getController(blockNumber).updateOutput(blockNumber, PLCfile);
//        outputBlocks.add(getBlockElement(blockNumber, blocks));
//        getController(blockNumber).generateOutputSignal(blockNumber, false);
    }

    /*
    update output within a wayside controller
     */
    public void updateOutputWaysideController(int blockNumber) throws IOException {
        // TODO
        //getController(blockNumber).generateOutputSignal(blockNumber, false);
    }

    /*
    update output within a wayside controller
     */
    public void updateAllOutputsWaysideController() throws IOException {
            //        for(int i=0;i < outputBlocks.size();i++){
            // TODO
            //getController(outputBlocks.get(i).getBlockNum()).generateOutputSignal(outputBlocks.get(i).getBlockNum(), false);
        //}
    }

    //helper function that finds all the track ELEMENTS with the corresponding blocks numbers and returns the array list (needs improvement)
    public ArrayList<TrackElement> findAllElements(int[] blockNumbers) throws IOException {
        ArrayList<TrackElement> newElementSet = new ArrayList<>();

        //needs check to see if the blocks numbers are even part of the system, if not, throw exception!

        for(int i=0;i < blockNumbers.length;i++){
            for(int j = 0; j < trackSection.size(); j++){
                if((trackSection.get(j).getBlockNum() == blockNumbers[i]) && (!newElementSet.contains(trackSection.get(j)))){
                    newElementSet.add(trackSection.get(j));
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
            for(int j = 0; j < trackSection.size(); j++){
                if((trackSection.get(j).getBlockNum() == blockNumbers[i]) && (!newBlockSet.contains(trackSection.get(j))) &&
                        (trackSection.get(i).getType().equalsIgnoreCase("block"))){
                    newBlockSet.add((TrackBlock) trackSection.get(j)); //casting should not change the reference.
                }
            }
        }

        return newBlockSet;
    }

    public TrackElement findBlock(int blockNumber) throws IOException {
        return getController(blockNumber).getBlockElement(blockNumber);
    }

    /*
    finds the corresponding controller for each block!
     */
    public WaysideController getController(int blockNumber){
        return lut.get(blockNumber);
    }

    /*
    somes lines are pre-defined, here is a quick way to generate the lines for the assignment!
     */
    public void generateLine() throws IOException, URISyntaxException {
//        if(currentLine.equalsIgnoreCase("green line") && (trackSection.size() == 151)) {
//            //NEEDS TO BE REPLACED WITH SOME METHOD THE CALLS GET THE TRACK FROM THE SIM ENVIRO OR IN PARAM
//            System.out.println("Generating Green Line Wayside Controllers!");
//            int[] controller1Blocks = new int[20];
//            int[] controller2Blocks = new int[19];
//            int[] controller10Blocks = new int[12];
//            int[] controller11Blocks = new int[7];
//            int[] controller3Blocks = new int[13];
//            int[] controller4Blocks = new int[14];
//            int[] controller5Blocks = new int[8];
//            int[] controller6Blocks = new int[20];
//            //int[] controller7Blocks = new int[1];
//            int[] controller8Blocks = new int[19];
//            int[] controller9Blocks = new int[19];
//            int j;
//
//            //controller1
//            j = 1;
//            for(int i=0;i <= 19;i++){
//                controller1Blocks[i] = j;
//                j++;
//            }
//
//            //controller2
//            j = 21;
//            for(int i=0;i <= 14;i++){
//                controller2Blocks[i] = j;
//                j++;
//            }
//            j = 147;
//            for(int i=15;i <= 18;i++){
//                controller2Blocks[i] = j;
//                j++;
//            }
//
//            //controller11
//            j=140;
//            for(int i=0;i <= 6;i++){
//                controller11Blocks[i] = j;
//                j++;
//            }
//
//            //controller10
//            j = 36;
//            for(int i=0;i <= 11;i++){
//                controller10Blocks[i] = j;
//                j++;
//            }
//
//            //controller3
//            j=48;
//            for(int i=0;i <= 12;i++){
//                controller3Blocks[i] = j;
//                j++;
//            }
//
//            //controller4
//            j=61;
//            for(int i=0;i <= 12;i++){
//                controller4Blocks[i] = j;
//                j++;
//            }
//            controller4Blocks[13] = 0;
//
//            //controller5
//            j=74;
//            for(int i=0;i <= 6;i++){
//                controller5Blocks[i] = j;
//                j++;
//            }
//            controller5Blocks[7] = 101;
//
//            //controller6
//            j=81;
//            for(int i=0;i <= 19;i++){
//                controller6Blocks[i] = j;
//                j++;
//            }
//
////        //controller7
////        controller7Blocks[0] = 0;
//
//            //controller8
//            j=102;
//            for(int i=0;i <= 18;i++){
//                controller8Blocks[i] = j;
//                j++;
//            }
//
//            //controller8
//            j=121;
//            for(int i=0;i <= 18;i++){
//                controller9Blocks[i] = j;
//                j++;
//            }
//
//            // Create controllers from jurisdictions
//            addWaysideController(controller1Blocks);
//            addWaysideController(controller2Blocks);
//            addWaysideController(controller3Blocks);
//            addWaysideController(controller4Blocks);
//            addWaysideController(controller5Blocks);
//            addWaysideController(controller6Blocks);
//            //addWaysideController(controller7Blocks);
//            addWaysideController(controller8Blocks);
//            addWaysideController(controller9Blocks);
//            addWaysideController(controller10Blocks);
//            addWaysideController(controller11Blocks);
//
//            addOutputWaysideController(12, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\SwitchBlock12PLC");
//            addOutputWaysideController(29, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\SwitchBlock29PLC");
//            addOutputWaysideController(58, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\SwitchBlock58PLC");
//            addOutputWaysideController(62, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\SwitchBlock62PLC");
//            addOutputWaysideController(76, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\SwitchBlock76PLC");
//            addOutputWaysideController(86, "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\SwitchBlock85PLC");
//            updateAllOutputsWaysideController();
//        }else{
//            throw new IOException("Generation Error: Invalid line generation");
//        }
    }

    /*
    broadcast to all the controllers!
     */
    public boolean broadcastToControllers(double[] newSpeeds, int[] newAuthorities) throws IOException {
        WaysideController temp;

        //RUN CHECKS TO MAKE SURE THE INPUTS ARE VALID HERE!

        //collision safety check
        for(int i=0;i < authorities.length;i++){
            if(authorities[i] > 0 && newAuthorities[i] > 0){
                newAuthorities[i] = 0;
            }
        }

        authorities = newAuthorities;
        speeds = newSpeeds;

        for(int i=0;i < speeds.length;i++){
            temp = lut.get(i);
            temp.setSpeed(i, speeds[i]);
        }

        temp = null;
        for(int i=0;i < authorities.length;i++){
            temp = lut.get(i);
            temp.setAuthority(i, authorities[i]);
        }

        return false;
    }

    /*
    gets the occupancy from the proper controller
     */
    public boolean getOccupancy(int blockNumber) throws IOException {
        // TODO
        return false;
    }

    /*

     */
    public boolean getSwitchStatus(int blockNumber) throws IOException {
        return getController(blockNumber).getSwitchStatus(blockNumber);
    }

    /*

     */
    public void setSwitchStatus(int blockNumber, boolean status) throws IOException {
        getController(blockNumber).setSwitchStatus(blockNumber ,status);
    }

    /*
    sets if a track should be CLOSED! failure status is currently used.
     */
    public void setClose(int blockNumber) throws IOException {
        int blockIsClosed = 4;
        getController(blockNumber).getBlockElement(blockNumber).setFailureStatus(blockIsClosed);
    }
    public void setOpen(int blockNumber) throws IOException {
        int blockIsOpen = 0;
        getController(blockNumber).getBlockElement(blockNumber).setFailureStatus(blockIsOpen);
    }

    /*
    sets if a track should be OPEN! failure status is currently used.
     */
    public void openClose(int blockNumber) throws IOException {
        getController(blockNumber).getBlockElement(blockNumber).setFailureStatus(0);
    }

    /*
    helper function - finds the specific block element from the block number
    */
    public TrackElement getBlockElement(int blockNumber, ArrayList<TrackElement> blocks) throws IOException {
        int num;
        for(int i = 0; i < blocks.size(); i++){
            num = blocks.get(i).getBlockNum();
            if(blockNumber == num){
                return blocks.get(i);
            }
        }

        throw new IOException("Controller Error: No block with that number in the wayside...");
    }

    /*
    Test Function that gets all the blocks
     */
    public ArrayList<TrackElement> getTrackSection(){
        return trackSection;
    }
    public String getLine() {return waysideName;}
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
                currName = controllers.get(i).getControllerAlias();
                if(currName.equals(controllerName)){
                    nameIndex = i;
                }
            }

            if(nameIndex == -1){
                throw new IOException("Command Error: Bad Name...");
            }

            // TODO
            //controllers.get(nameIndex).updateOutput(Integer.parseInt(blockNumber),PLCfile);
            updateAllOutputsWaysideController();
            return "You have successfully uploaded a PLC to " + controllerName;
        }else if(commands[0].equals("update")){ //COMPILE COMMAND
//            controllerName = commands[1];
//            PLCfile = commands[2];
//            blockNumber = commands[3];
//            int nameIndex = -1;
//            String currName;
//
//            for(int i=0;i < controllers.size();i++){
//                currName = controllers.get(i).getName();
//                if(currName.equals(controllerName)){
//                    nameIndex = i;
//                }
//            }
//
//            if(nameIndex == -1){
//                throw new IOException("Command Error: Bad Name...");
//            }
//
//            controllers.get(nameIndex).updateOutput(Integer.parseInt(blockNumber),PLCfile);
//            return "You have successfully updated a PLC on " + controllerName;
        }if(commands[0].equals("broadcastToControllersTest")){ //COMPILE COMMAND
            double[] speed = new double[151];
            int[] authority = new int[151];

            for(int i=0;i < speed.length;i++){
                speed[i] = i;
                authority[i] = i;
            }

            broadcastToControllers(speed, authority);

            //generateTestController();
            return "Error in broadcasting - something failed...";
        }else{
            return "Command Error: The current command was invalid...";
        }
    }

    //TEST GETTERS AND SETTER ******************************************************************************************
    public HashMap<Integer, WaysideController> getLut(){
        return lut;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String filepath = "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\TrackModelModule\\src\\Track\\Test.csv";
        Track instance = new Track();
        instance.importTrack(filepath);

        //WaysideSystem testSystem = new WaysideSystem(instance.getGreenLine(), "Green line");
        WaysideSystem testSystem = null;
        testSystem.generateLine();
        WaysideUIClass guiUpdater = new WaysideUIClass(testSystem);

        try {
            System.out.println("starting GUI");
            guiUpdater.start();
            System.out.println("working!");
        } catch (Exception e) {
            System.out.println("failed when running");
        }
    }

}