package WaysideController;

import Track.Track;
import TrackConstruction.TrackElement;
import WaysideGUI.WaysideUIClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;


public class WaysideSystem {
    /***********************************************************************************************************************/
    /** Global Members
     */
    public static int NUMBER_WAYSIDE_SYSTEMS = 0;
    /** Default Members
     */
    public static String DEFAULT_GIVEN_NAME_FORMAT = "Wayside System #%d";
    public static String DEFAULT_LINE_NAME = "Unnamed Section";
    public static int DEFAULT_NUM_CONTROLLERS = 3;
    /** Information Members
     */
    private String trackSectionName;
    private int waysideIndex = ++NUMBER_WAYSIDE_SYSTEMS;
    private String givenName = String.format(DEFAULT_GIVEN_NAME_FORMAT,waysideIndex);
    private ArrayList<TrackElement> trackSection = new ArrayList<>();
    /** Operation Members
     */
    private Vector<WaysideController> controllers = new Vector<>();
    private HashMap<Integer, WaysideController> lut = new HashMap<Integer, WaysideController>();
    /***********************************************************************************************************************/
    /** TODO To Haleigh: WaysideSystem(String, ArrayList) is the preferred constructor when working with Wayside Systems!
     */
    public WaysideSystem() throws IOException{
        trackSectionName = DEFAULT_LINE_NAME;
        controllers = new Vector<>();
    }
    public WaysideSystem(String trackSectionName) throws IOException {
        this.trackSectionName = trackSectionName;
    }
    public WaysideSystem(String trackSectionName, ArrayList<TrackElement> trackSection, int numberControllers) throws IOException {
        this.trackSectionName = trackSectionName;
        this.trackSection = trackSection;
        generateControllers(numberControllers);
    }



    /*
        CTC Methods
     */



    /** finds the controller object which has jurisdiction over a specific block index.
     *
     * @param blockNumber
     * @return
     */
    public WaysideController getController(int blockNumber){
        // TODO
        return lut.get(blockNumber);
    }


    /** retrieves the occupancy of a block from the track system.
     * uses look-up table to call upon the correct wayside controller. Failure to update the lut when controller jurisdictions change will cause failure
     *
     * @param targetBlockNumber, the unique index of the intended TrackElement block whom we are acquiring the occupancy from
     * @return boolean, the occupancy of
     */
    public boolean getOccupancy(int targetBlockNumber) throws Exception {
            try {
                // TODO the return should occur here, we need lut first
                //ctrl.getOccupancyOfBlock(targetBlockNumber);
            } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
                failureToRetrieveOccupancyFromTrackElement.printStackTrace();
                throw new Exception(String.format("Failure occurred when retrieving occupancy status from Block (index %d)"));
            }
            return false;
    }


    /** allows CTC to update the speed and authorities for every block within the TrackLine
     *  length of (1) lineSpeeds (2) lineAuthorities and (3) the ArrayList<TrackElement> which this WaysideSystem was constructed with must match
     *  the element indexes in (1) and (2) must match one-to-one to the sequence of blocks within the Track Line, for example (with an offset of one, which is how ours will work):
     *  element 0 in (1),(2) will correspond to Track block #1 in Green Line
     *  element 1 in (1),(2) will correspond to Track block #2 in Green Line
     *  element 2 in (1),(2) will correspond to Track block #3 in Green Line
     *  ...
     *  element n in (1),(2) will correspond to Track block #(n+1) in Green Line
     *
     *  an example outcome for the Green WaysideSystem would be:
     *  Index   Arrays (S,A):           Gives outcome:
     *  0       [25.0][3]               Track Index #1 gets speed 25 and authority 3
     *  1       [25.0][1]               Track Index #2 gets speed 25 and authority 1
     *  2       [00.0][0]               Track Index #3 gets speed 0 and authority 0
     *  ...
     *
     * @param lineSpeeds, the array of intended speeds for all of the track blocks as doubles (one to one correspondance)
     * @param lineAuthorities, the array of intended authorities for all of the track blocks as ints (one to one correspondance)
     */
    public void broadcastToControllers(double[] lineSpeeds, int[] lineAuthorities) throws Exception {
        setAuthorities(lineAuthorities);
        setCommandedSpeeds(lineSpeeds);
    }

    public void setAuthorities(int[] lineAuthorities) throws Exception {
        if(lineAuthorities.length != trackSection.size())
            throw new Exception (String.format("Length of commanded Authorities Array given to Wayside System (%s) does not match the size of tracks which %s controls.\nLength of line under Wayside jurisdiction is (%d) and length of given Authority vector is (%d).\n",
                    this.trackSectionName,this.trackSectionName,trackSection.size(),lineAuthorities.length));
        int arrayIndex = 0;
        // For each Block in line... For each controller...
        for (int blockNum = 1; arrayIndex<trackSection.size(); blockNum++,arrayIndex++) {
            for (WaysideController ctrl : controllers) {
                ctrl.setBlockAuthority(blockNum, lineAuthorities[arrayIndex]);
            }
        }
    }

    public void setCommandedSpeeds(double[] lineSpeeds) throws Exception {
        if(lineSpeeds.length != trackSection.size())
            throw new Exception (String.format("Length of commanded Speeds Array given to Wayside System (%s) does not match the size of tracks which %s controls.\nLength of line under Wayside jurisdiction is (%d) and length of given speed vector is (%d).\n",
                    this.trackSectionName,this.trackSectionName,trackSection.size(),lineSpeeds.length));
        int arrayIndex = 0;
        // For each Block in line... For each controller...
        for (int blockNum = 1; arrayIndex<trackSection.size(); blockNum++,arrayIndex++) {
            for (WaysideController ctrl : controllers) {
                ctrl.setBlockSpeed(blockNum, lineSpeeds[arrayIndex]);
            }
        }
    }

    public void setClose(int targetBlockIndex) throws Exception {
        // TODO
    }

    public void setOpen(int targetBlockIndex) throws Exception {
        // TODO
    }

    public boolean getSwitchStatus(int targetBlockIndex) throws Exception {
        // TODO
        return false;
    }

    public void setSwitchStatus(int targetBlockIndex, boolean orientation) throws Exception {
        // TODO
    }



    /*
            Management Methods
     */

    /** accepts a new track for management under this Wayside System, generates all controller and controller information necessary
     *
     */
    public void registerNewTrack(ArrayList<TrackElement> trackLine) throws IOException, URISyntaxException {
        this.trackSection = trackLine;
        generateLine();
    }

    /** performs any necessary checks, steps, or filters before accepting a controller into the system
     *
     * @param controller
     */
    public void addController(WaysideController controller) {
        controllers.add(controller);
        this.trackSection.addAll(controller.getJurisdiction());
    }


    /** creates all WaysideController objects whenever the Track Line has been established (track array given.)
     *
     * @param nControllers
     */
    public void generateControllers(int nControllers) {
        // Generate subsets of current Line Section
        ArrayList< ArrayList<TrackElement> > trackPartitions = partitionArrayList(trackSection,nControllers);

    }

    /** partitions an arbitrarily sized array list into a specified number of sections (as evenly as possible.)
     *
     * assert: the element distribution inside of the partitions will NOT be sequential
     */
    public static <T> ArrayList<ArrayList<T>> partitionArrayList(ArrayList<T> list, int sections) {
        if (sections <= 0)
            return null;
        ArrayList<ArrayList<T>> subsets= new ArrayList<>();
        // initialize subset arrays
        for(int i=0; i<sections; i++) {
            ArrayList<T> partition = new ArrayList<>();
            subsets.add(partition);
        }
        int size = list.size();
        int assignedPartition;
        // For all items...
        for (int i=0; i<size; i++) {
            // Calculate which partition gets this item
            assignedPartition = i % sections;
            System.out.printf("%d gets %d\n",assignedPartition,i);
            // Give item to assigned partition
            subsets.get(assignedPartition).add(list.get(i));
        }
        return subsets;
    }


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


    /** accepts string from WaysideSystemUI console to perform action
     *
     * Commandline actions:
     * upload PLC file to controller:
     * @param commandLine
     * @return
     * @throws IOException
     * @throws URISyntaxException
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
            //updateAllOutputsWaysideController();
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

            //broadcastToControllers(speed, authority);

            //generateTestController();
            return "Error in broadcasting - something failed...";
        }else{
            return "Command Error: The current command was invalid...";
        }
    }



    /*
            Getters and Setters
     */


    /** The expectation is that users will set WaysideAlias to a string name for the track line it overlooks*/
    public String getLineName() {return trackSectionName;}
    public String getIdentificationName() {return givenName;}
    public Vector<WaysideController> getControllers() { return controllers; }
    public ArrayList<TrackElement> getTrackSection(){
        return trackSection;
    }



    /*
            Cheeky Solution
     */


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WaysideSystem))
            return false;
        WaysideSystem other = (WaysideSystem) o;
        // Priority 1: Wayside Alias Name matching Wayside Alias Name takes precedence
        // ignore case to make it easier to use for ctc
        if (this.trackSectionName.equalsIgnoreCase(other.trackSectionName)) {
            return true;
        }
        // Priority 2: Wayside Given Name matching Wayside Given Name
        if (this.givenName.equals(other.givenName)) {
            return true;
        }
        // Return false otherwise
        return false;
    }
    /*
            Main for running just a wayside system on its own
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        String filepath = "C:\\Users\\Harsh\\IdeaProjects\\ECE1140_Group2_TransitSimulation\\SubModuleModules\\TrackModelModule\\src\\Track\\Test.csv";
        Track instance = new Track();
        //instance.importTrack(filepath);

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