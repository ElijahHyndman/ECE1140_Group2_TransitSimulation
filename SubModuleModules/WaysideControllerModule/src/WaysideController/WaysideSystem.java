package WaysideController;

import PLCInput.*;
import PLCOutput.SwitchPLCOutput;
import Track.Track;
import TrackConstruction.Station;
import TrackConstruction.Switch;
import TrackConstruction.TrackElement;
import WaysideGUI.WaysideUIClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


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
    public static HasAuthorityPLCInput.AuthRule DEFAULT_AUTHORITY_RULE = HasAuthorityPLCInput.AuthRule.TrueWhenGreaterOrEqual;
    public static int DEFAULT_AUTHORITY_CRITERIA = 1;
    public static OccupationPLCInput.OccRule DEFAULT_OCCUPATION_RULE = OccupationPLCInput.OccRule.TrueWhenOccupied;
    final static public String AUTHORITY_INPUT_PLC_VARIABLE_FORMAT = "HASAUTH%d";
    final static public String OCCUPATION_INPUT_PLC_VARIABLE_FORMAT = "OCC%d";
    /** Information Members
     * @member trackLine, the arraylist of the entire Track System Line Color which this wayside system will overlook (e.x. the full arraylist of the green line)
     */
    private String trackSectionName;
    private int waysideIndex = ++NUMBER_WAYSIDE_SYSTEMS;
    private String givenName = String.format(DEFAULT_GIVEN_NAME_FORMAT,waysideIndex);
    private ArrayList<TrackElement> trackLine = new ArrayList<>();
    /** Operation Members
     * @member lut, lookup table that maps block number indexes (integer) to the associated controller that has jursidiction over that block (WaysideController)
     */
    private Vector<WaysideController> controllers = new Vector<>();
    private HashMap<Integer, WaysideController> lut = new HashMap<Integer, WaysideController>();
    private ArrayList<PLCInput> trackLineInputPool = new ArrayList<PLCInput>();
    private int numberControllers = DEFAULT_NUM_CONTROLLERS;
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
    public WaysideSystem( ArrayList<TrackElement> trackLine, String trackSectionName, int numberControllers) throws Exception {
        this.trackSectionName = trackSectionName;
        this.trackLine = trackLine;
        this.numberControllers = numberControllers;
        // Splits track into jurisdictions
        registerNewTrack(trackLine);
    }
    public WaysideSystem( ArrayList<TrackElement> trackLine, String trackSectionName) throws Exception {
        this.trackSectionName = trackSectionName;
        this.trackLine = trackLine;
        // Splits track into jurisdictions
        registerNewTrack(trackLine);
    }



    /*
        CTC Methods
     */


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

    /**
     *
     * @param targetSwitchIndex
     * @param script
     */
    public void uploadSwitchPLCToController(int targetSwitchIndex, PLCEngine script) throws Exception {
        WaysideController ctrl = getControllerOfBlock(targetSwitchIndex);
        // A controller must oversee every block

        if (ctrl == null)
            throw new Exception(String.format("Wayside System error: error uploading PLC to switch %d, controller not assigned to switch\nMake sure that block number specified is within this wayside system's assigned line\n",targetSwitchIndex));
        // Attempt to get occupancy from controller
        try {
            ctrl.uploadSafetyPLCScript(script);
        } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
            failureToRetrieveOccupancyFromTrackElement.printStackTrace();
            throw new Exception(String.format("Failure occurred when retrieving occupancy status from Block (index %d)",targetSwitchIndex));
        }
    }

    /** sets the authorities for the entire track line under this WaysideSystem's jurisdiction using an array of integers
     *
     * @param lineAuthorities, MUST match the same length as the track line arraylist used to create this wayside system. one-to-one match
     * @throws Exception size of array does not match jurisdiction; failure to set authority to controller
     */
    public void setAuthorities(int[] lineAuthorities) throws Exception {
        if(lineAuthorities.length != trackLine.size())
            throw new Exception (String.format("Length of commanded Authorities Array given to Wayside System (%s) does not match the size of tracks which %s controls.\nLength of line under Wayside jurisdiction is (%d) and length of given Authority vector is (%d).\n",
                    this.trackSectionName,this.trackSectionName, trackLine.size(),lineAuthorities.length));
        int arrayIndex = 0;
        // For each Block in line... For each controller...
        for (int blockNum = 1; arrayIndex< trackLine.size(); blockNum++,arrayIndex++) {
            for (WaysideController ctrl : controllers) {
                ctrl.setBlockAuthority(blockNum, lineAuthorities[arrayIndex]);
            }
        }
    }


    /** sets the commanded speeds for the entire track line under this WaysideSystem's jurisdiction using an array of doubles
     *
     * @param lineSpeeds, MUST match the same length as the track line arraylist used to create this wayside system. one-to-one match
     * @throws Exception
     */
    public void setCommandedSpeeds(double[] lineSpeeds) throws Exception {
        if(lineSpeeds.length != trackLine.size())
            throw new Exception (String.format("Length of commanded Speeds Array given to Wayside System (%s) does not match the size of tracks which %s controls.\nLength of line under Wayside jurisdiction is (%d) and length of given speed vector is (%d).\n",
                    this.trackSectionName,this.trackSectionName, trackLine.size(),lineSpeeds.length));
        int arrayIndex = 0;
        // For each Block in line... For each controller...
        for (int blockNum = 1; arrayIndex< trackLine.size(); blockNum++,arrayIndex++) {
            for (WaysideController ctrl : controllers) {
                ctrl.setBlockSpeed(blockNum, lineSpeeds[arrayIndex]);
            }
        }
    }


    /** retrieves the occupancy of a block from the track system.
     * uses look-up table to call upon the correct wayside controller. Failure to update the lut when controller jurisdictions change will cause failure
     *
     * @param targetBlockNumber, the unique index of the intended TrackElement block whom we are acquiring the occupancy from
     * @return boolean, the occupancy of
     */
    public boolean getOccupancy(int targetBlockNumber) throws Exception {
        WaysideController ctrl = getControllerOfBlock(targetBlockNumber);
        // A controller must oversee every block
        if (ctrl == null)
            throw new Exception(String.format("Wayside System error: look up table for block index %d returned a null controller object when searching for occupancy\nMake sure that block number specified is within this wayside system's assigned line\n",targetBlockNumber));
        // Attempt to get occupancy from controller
        try {
            return ctrl.getOccupancy(targetBlockNumber);
        } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
            failureToRetrieveOccupancyFromTrackElement.printStackTrace();
            throw new Exception(String.format("Failure occurred when retrieving occupancy status from Block (index %d)",targetBlockNumber));
        }
    }


    /** sets a block within the track system as designated "closed"
     *
     * @param targetBlockNumber
     * @throws Exception
     */
    public void setClose(int targetBlockNumber) throws Exception {
        WaysideController ctrl = getControllerOfBlock(targetBlockNumber);

        // A controller must oversee every block
        if (ctrl == null)
            throw new Exception(String.format("Wayside System error: look up table for track block (index %d) found no controllers.\nMake sure that block number specified is within this wayside system's assigned line\n",targetBlockNumber));

        // Attempt to set block closed using controller
        try {
            ctrl.setClose(targetBlockNumber);
        } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
            failureToRetrieveOccupancyFromTrackElement.printStackTrace();
            throw new Exception(String.format("Failure occurred when attempting to set Block (index %d) as closed within %s",targetBlockNumber,ctrl.getControllerName()));
        }
    }


    /** sets a block within the track system as designated "open"
     *
     * @param targetBlockNumber
     * @throws Exception
     */
    public void setOpen(int targetBlockNumber) throws Exception {
        WaysideController ctrl = getControllerOfBlock(targetBlockNumber);

        // A controller must oversee every block
        if (ctrl == null)
            throw new Exception(String.format("Wayside System error: look up table for track block (index %d) found no controllers.\nMake sure that block number specified is within this wayside system's assigned line\n",targetBlockNumber));

        // Attempt to set block closed using controller
        try {
            ctrl.setOpen(targetBlockNumber);
        } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
            failureToRetrieveOccupancyFromTrackElement.printStackTrace();
            throw new Exception(String.format("Failure occurred when attempting to set Block (index %d) as closed within %s",targetBlockNumber,ctrl.getControllerName()));
        }
    }


    /** gets the closure status of a block from within the track system under this WaysideSystem's jurisdiction
     *
     * @param targetBlockNumber
     * @return
     * @throws Exception
     */
    public boolean getIsClosed(int targetBlockNumber) throws Exception {
        WaysideController ctrl = getControllerOfBlock(targetBlockNumber);

        // A controller must oversee every block
        if (ctrl == null)
            throw new Exception(String.format("Wayside System error: look up table for track block (index %d) found no controllers.\nMake sure that block number specified is within this wayside system's assigned line\n",targetBlockNumber));

        // Attempt to set block closed using controller
        try {
            return ctrl.getIsClosed(targetBlockNumber);
        } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
            failureToRetrieveOccupancyFromTrackElement.printStackTrace();
            throw new Exception(String.format("Failure occurred when attempting to set Block (index %d) as closed within %s",targetBlockNumber,ctrl.getControllerName()));
        }
    }


    /** gets the current status of a switch (arbitrary choice of boolean is that false=DEFAULT true=SECONDARY orientations)
     *
     * @param targetBlockNumber
     * @return
     * @throws Exception
     */
    public boolean getSwitchStatus(int targetBlockNumber) throws Exception {
        WaysideController ctrl = getControllerOfBlock(targetBlockNumber);

        // A controller must oversee every block
        if (ctrl == null)
            throw new Exception(String.format("Wayside System error: look up table for track block (index %d) found no controllers.\nMake sure that block number specified is within this wayside system's assigned line\n",targetBlockNumber));

        // Attempt to set block closed using controller
        try {
            return ctrl.getSwitchStatus(targetBlockNumber);
        } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
            failureToRetrieveOccupancyFromTrackElement.printStackTrace();
            throw new Exception(String.format("Failure occurred when attempting to set Block (index %d) as closed within %s",targetBlockNumber,ctrl.getControllerName()));
        }
    }


    /** manually sets the switch status for a switch. Switch must be "released" before the plc can take over again
     *
     * @param targetBlockNumber
     * @param orientation
     * @throws Exception
     */
    public void setSwitchStatus(int targetBlockNumber, boolean orientation) throws Exception {
        WaysideController ctrl = getControllerOfBlock(targetBlockNumber);

        // A controller must oversee every block
        if (ctrl == null)
            throw new Exception(String.format("Wayside System error: look up table for track block (index %d) found no controllers.\nMake sure that block number specified is within this wayside system's assigned line\n",targetBlockNumber));

        // Attempt to set block closed using controller
        try {
            ctrl.setSwitchStatus(targetBlockNumber,orientation);
        } catch (Exception failureToRetrieveOccupancyFromTrackElement) {
            failureToRetrieveOccupancyFromTrackElement.printStackTrace();
            throw new Exception(String.format("Failure occurred when attempting to set Block (index %d) as closed within %s",targetBlockNumber,ctrl.getControllerName()));
        }
    }



    /*
            Management Methods
     */



    /** accepts a new track for management under this Wayside System and handles all operations with auto-generation, generates all controller and controller information necessary
     *
     * @before current track may be out of date
     * @after given track has been stored
     * @after all of the PLC input variables have been created and may be referenced
     * @after given trackLine has been partitioned into jurisdictions (number jurisdictions = @member numberControllers)
     * @after wayside controllers have been created for all jurisdictions
     */
    public void registerNewTrack(ArrayList<TrackElement> trackLine) throws Exception {
        /*
                New Track Operations
         */
        this.trackLine = trackLine;
        this.trackLineInputPool = generateInputPool(trackLine);
        ArrayList<ArrayList<TrackElement>> jurisdictions = partitionArrayList(trackLine, numberControllers);

        /*
                Create controllers
         */
        for (ArrayList<TrackElement> jurisdiction : jurisdictions) {
            generateController(jurisdiction);
        }
    }


    /** performs any steps and filters when registering a new controller under the jurisdiction of this wayside system
     *
     */
    public void generateController(ArrayList<TrackElement> assignedControllerJurisdiction) throws Exception {
        // Create new controller and assign it jurisdiction
        WaysideController newController = new WaysideController(trackLine, assignedControllerJurisdiction);
        controllers.add(newController);
        // Give controller access to full input pool
        newController.assignInputPool(trackLineInputPool);
        // Register controller under LUT for block purposes
        for (TrackElement block : assignedControllerJurisdiction) {
            // controller is associated by integer blocknumber key
            lut.put(block.getBlockNum(), newController);
        }
        // launches controller onto a new thread
        newController.start();
    }


    /** generates pool of possible inputs a user would want to refer to in their PLC scripts for this whole system
     *
     * handles the formatting of variable input names for the PLCScript based on defined String constants at top of this file
     *
     * @before current input pool may reflect an old track  or may not exist
     * @return an input pool which matches the
     */
    public ArrayList<PLCInput> generateInputPool(ArrayList<TrackElement> track) {
        ArrayList<PLCInput> inputs = new ArrayList<PLCInput>();
        PLCInput occupationInput;
        PLCInput hasAuthorityInput;
        for (TrackElement element : track) {
            int blockIndex = element.getBlockNum();
            /*
                    General input forms for all blocks
             */
            occupationInput = new OccupationPLCInput(String.format(OCCUPATION_INPUT_PLC_VARIABLE_FORMAT,blockIndex), element, DEFAULT_OCCUPATION_RULE);
            hasAuthorityInput = new HasAuthorityPLCInput(String.format(AUTHORITY_INPUT_PLC_VARIABLE_FORMAT,blockIndex), element, DEFAULT_AUTHORITY_RULE, DEFAULT_AUTHORITY_CRITERIA);
            /*
                    Switch input forms
             */
            if (element instanceof Switch) {
                Switch sw = (Switch) element;
                // account for any switch inputs
            }
            /*
                    Station input forms
             */
            if (element instanceof Station) {
                Station st = (Station) element;
                // account for any station inputs
            }

            // store to list
            inputs.add(occupationInput);
            inputs.add(hasAuthorityInput);
        }
        return inputs;
    }


    /** partitions an arbitrarily sized array list into a specified number of sections (as evenly as possible.)
     *
     * assert: element distribution is now sequential when mapped to partitions
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

        // perParition is rounded up of even distribution
        int size = list.size();
        int perPartition = (int) Math.ceil((double)size / (double)sections);
        int assignedPartition;
        // i iterates over the entire list, assignedPartition breaks i into partition chunks
        int i = 0;
        for (T item : list) {
            // integer division
            assignedPartition = i / perPartition;
            subsets.get(assignedPartition).add( list.get(i) );
            i++;
        }
        return subsets;
    }


    /** finds the controller object which has jurisdiction over a specific block index.
     *
     * @param blockNumber
     * @return
     */
    public WaysideController getControllerOfBlock(int blockNumber){
        //System.out.printf("Controller assigned to block (%d) is ctrl (%s)\n",blockNumber,lut.get(blockNumber).getControllerName());
        return lut.get(blockNumber);
    }



    /*
            GUI Methods
     */



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
    public ArrayList<TrackElement> getTrackLine(){
        return trackLine;
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
}