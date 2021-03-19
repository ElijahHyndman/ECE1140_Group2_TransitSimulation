package WaysideController;

import WaysideGUI.WaysideUIClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class WaysideSystem {

    List<WaysideController> controllers;

    public WaysideSystem() {
        controllers = new LinkedList<>();
    }
    public WaysideSystem(LinkedList<WaysideController> controllers) {
        this.controllers = controllers;
    }


    public Vector<WaysideController> getControllersVector() {
        Vector<WaysideController> out = new Vector<WaysideController>();
        for(WaysideController controller : controllers) {
            out.add(controller);
        }
        return out;
    }


    public static LinkedList<WaysideController> generateTestControllers(int nControllers) {
        int[] blocks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        String currentLine = "Blue";
        List<String> currentInputNames = Arrays.asList("A", "C", "D");
        boolean[] inputs = new boolean[]{false, true, true};

        LinkedList<WaysideController> out = new LinkedList<WaysideController>();
        for (int i=0; i<nControllers; i++) {
            out.add( new WaysideController() );
        }
        return out;
    }


    public void generateTestController() {
        int[] blocks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        String currentLine = "Blue";
        List<String> currentInputNames = Arrays.asList("A", "C", "D");
        boolean[] inputs = new boolean[]{false, true, true};

        WaysideController waysideController = new WaysideController(blocks, currentLine, currentInputNames, inputs, "Controller" + (controllers.size()+1));
        controllers.add(waysideController);
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
        String outputName;

        //test if valid input was inputed
        if(commands.length <= 0 || commands.length > 4) {
            return "Command Error: The current command has bad param/or is invalid...";
        }

        //upload
        if(commands[0].equals("upload")) { //UPLOAD COMMAND
            controllerName = commands[1];
            PLCfile = commands[2];
            outputName = commands[3];
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

            controllers.get(nameIndex).addOutputSignal(PLCfile, outputName);
            return "You have successfully added a PLCfile and Output To a Controller!";
        }else if(commands[0].equals("compile")){ //COMPILE COMMAND
            controllerName = commands[1];
            outputName = commands[2];
            int nameIndex = -1;

            for(int i=0;i < controllers.size();i++){
                if(controllers.get(i).getName().equals(controllerName)){
                    nameIndex = i;
                }
            }

            if(nameIndex == -1){
                throw new IOException("Command Error: Bad Name...");
            }

            controllers.get(nameIndex).generateOutputSignal(outputName);
            return "You have successfully compiled the PLC!";
        }if(commands[0].equals("add")){ //COMPILE COMMAND
            controllerName = commands[1];

            int nameIndex = -1;

            if(commands[1].equals("default")){
                nameIndex = 1;
            }

            if(nameIndex == -1){
                throw new IOException("Command Error: Bad Name...");
            }

            generateTestController();
            return "Nice new controller!";
        }else{
            return "Command Error: The current command was invalid...";
        }
    }


    public static void main(String[] args) {
        WaysideSystem testSystem = new WaysideSystem();
        WaysideUIClass guiUpdater;

        //WaysideController testControllers = generateTestController();
        //testSystem = new WaysideSystem(testControllers);
        testSystem.generateTestController();
        guiUpdater = new WaysideUIClass(testSystem);

        System.out.println("starting GUI");
        try {
            guiUpdater.start();
        } catch (Exception e) {
            System.out.println("failed when running");
        }
    }
}