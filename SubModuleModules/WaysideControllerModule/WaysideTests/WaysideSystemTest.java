import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import WaysideController.*;
import WaysideGUI.*;

import java.io.IOException;
import java.net.URISyntaxException;

class WaysideSystemTest {
    @Test
    @DisplayName("Dummy Test!")
    public void testDummy() throws IOException, URISyntaxException {
        String commandLine = "upload gaming potato";
        String[] commands = commandLine.split(" ");

        //test if valid input was inputed
//        if(commands[0].length() == 0 || commands[0].length() >= 4) {
//            throw new IOException("Command Error: The current command was invalid...");
//        }

        for(int i=0;i < commands.length;i++){
            System.out.println(commands[i]);
        }
    }

    @Test
    @DisplayName("CommandLineTest!")
    public void testCommandLine() throws IOException, URISyntaxException {
        WaysideSystem testSystem = new WaysideSystem();
        WaysideUIClass guiUpdater;
        guiUpdater = new WaysideUIClass(testSystem);
        testSystem.generateTestController();

        System.out.println(testSystem.readConsole("upload Controller1 C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\WaysideControllerModule\\Resources\\testtoken3 Switch1"));
        System.out.println(testSystem.readConsole("compile Controller1 Switch1"));
        System.out.println("starting GUI");
        try {
            guiUpdater.start();
        } catch (Exception e) {
            System.out.println("failed when running");
        }
    }

    @Test
    @DisplayName("Block generation and assignment")
    public void testBlockCreationAndAssignment() throws IOException, URISyntaxException {
        WaysideSystem testSystem = new WaysideSystem();

    }
}