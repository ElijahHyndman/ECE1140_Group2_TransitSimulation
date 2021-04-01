package WaysideGUI;
import WaysideController.WaysideController;
import WaysideController.WaysideSystem;
import WaysideController.GPIO;

import java.awt.CardLayout;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.Color;
import javax.swing.JTree;

import java.util.HashMap;
import javax.swing.JViewport;

/**
 * @author elijah
 */
public class WaysideUIJFrameWindow extends javax.swing.JFrame {

    /**
     * Creates new form WaysideUIJFrameWindow
     *
     * This class is a JFrame window class, a single object of this class allows the user to display
     *  all windows relevant to the Wayside GUI.
     *
     * This GUI will only reflect a static reading of the wayside controllers at any time.
     * To reflect new updates to the wayside controllers and their values, an object of this class will require
     * constant updating/refreshing with the most recent wayside controller objects. Refreshing of this GUI will
     * be handled by the WaysideUIClass, the same one intended to generate an instance of this class
     *
     *
     * WaysideUIJFrameWindow will create one entire window that encompasses everything the Wayside User Interface needs to accomplish.
     * This window will initially display the Wayside Controller main menu, and allow the user to navigate to
     * other jframe panels (Main menu, controller advanced menu, and so on)
     */

    // Data Members to populate GUI with information about
    private WaysideSystem system = new WaysideSystem();
    private Vector<WaysideController> controllers = new Vector<WaysideController>();
    private static WaysideController thisController = new WaysideController();

    // Status tracking about GUI state
    private boolean controllerSelected = false;
    private boolean hardwareView = false;

    // Helper varibles
    private static boolean protectedUpdate = false;


    public WaysideUIJFrameWindow() throws IOException {
        initComponents();
        updateControllerSelectText();
    }

    public WaysideUIJFrameWindow(Vector<WaysideController> defaultControllers) throws IOException {
        this.controllers = defaultControllers;
        initComponents();
        updateControllerSelectText();
    }

    public WaysideUIJFrameWindow(WaysideSystem existingSystem) throws IOException {
        this.controllers = existingSystem.getControllersVector();
        system = existingSystem;
        initComponents();
        updateControllerSelectText();
    }


    /*
     * Elijah: Functions useful for generating GUI data
     */


    public boolean updateGUI(Vector<WaysideController> controllerList) {
        /**
         * Updates the GUI with a fresh vector of Wayside Controller.
         *
         * @assert  tree takes time to expand; cannot update GUI faster than tree expands
         *
         * @before: GUI reflects the state of old Wayside Controller vector
         * @after:  GUI has updated all fields to reflect the state of the provided Controller List
         *
         * @param controllerList: a vector of up-to-date waysideControllerObjects
         */
        try {

            // Store Wayside Controllers locally
            controllers = controllerList;

            // Stash the state of Controller-Tree expansion so it can be restored, stash scroll height view
            String treeState = storeExpansionState(ControllerListTree);
            JViewport scrollLevel = TreeScrollPane.getViewport();

            // Refresh nodes on Controller-Tree
            repopulateControllerTree();

            // Restore the tree expansion state
            restoreExpansionState(ControllerListTree, treeState);
            TreeScrollPane.setViewport(scrollLevel);

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String storeExpansionState(JTree tree) {
        /**
         * Original code by G Cope, saves expansion state of tree in a String.
         *
         * @param   tree JTree object whose expansion state will be saved
         *
         * source: https://www.algosome.com/articles/save-jtree-expand-state.html
         */
        StringBuilder sb = new StringBuilder();

        // Generate string of indexes that are expanded on tree
        for ( int i = 0; i < tree.getRowCount(); i++ ){
            if ( tree.isExpanded(i) ){
                sb.append(i).append(",");
            }
        }

        return sb.toString();
    }


    public void restoreExpansionState(JTree tree, String s) {
        /**
         * Original code by G Cope, restores the expansion state of the tree according
         *  to string of commas and indexes.
         *
         * @param   tree JTree object who will be expanded
         * @param   s String, comma separated list of row indexes to expand
         *
         * source: https://www.algosome.com/articles/save-jtree-expand-state.html
         */
        String[] indexes = s.split(",");

        // expand the row indexes named in string s
        for ( String st : indexes ){

            int row = Integer.parseInt(st);

            tree.expandRow(row);

        }
    }


    public boolean repopulateControllerTree() {
        /**
         *  updates Controller-List Tree (Controller Quick Menu) according to current vector of controllers.
         *
         *  The structure will be:
         *      -"Controller List" is root note (String node)
         *      -Each controller is child of Controller List (Wayside Node)
         *      -Each controller will have children that list values and parameters (generated by treeNodeFromControllerObject() ) (String nodes)
         *
         *  @before:    tree list may or may not reflect state of controllers inside (controllers)
         *  @after:     tree list now reflects the state of all controllers inside (controllers)
         *
         *  @return     boolean about operation success. false will be returned if exception is thrown
         *  @param      none.
         */

        try {
            // --- Create hierarchy of nodes to place into tree
            DefaultMutableTreeNode top = new DefaultMutableTreeNode("Controller List");

            // Under the root, add each individual controller as a node.
            // Generate node per controller.
            String NodeName;
            for (WaysideController controller : controllers) {
                DefaultMutableTreeNode controllerNode = treeNodeFromControllerObject(controller);
                top.add(controllerNode);
            }

            // Place nodes into tree
            DefaultTreeModel tree = (DefaultTreeModel)ControllerListTree.getModel();
            tree.setRoot(top);

            // Operation successful if no errors
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public DefaultMutableTreeNode treeNodeFromControllerObject(WaysideController controller) {
        /**
         * utility function to generate one node to represent an entire wayside controller.
         * controller must implement generateDescriptionNode() function to get description nodes
         *
         * Structure - The top node is always the controller's name (node stores Wayside Controller Object)
         *  the subsequent nodes shall be information nodes generated by a function from the controller (nodes store Strings)
         *
         *  @param  controller Wayside Controller that will be represented by output node
         *  @return DefaultMutableTreeNode tree node named after controller, with children that contain info about controller
         *
         */

        // Top node is controller object (shows name)
        DefaultMutableTreeNode controllerNode = new DefaultMutableTreeNode(controller);

        // Hashmap of Key:List pairs. i.e. categoryNode:childNodes pairs
        HashMap<String, Vector<String>> info = controller.generateDescriptionNodes();

        // For every pair
        info.forEach( (categoryName,valueList) -> {

            // Parent node is category name
            DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(categoryName);

            // Child Nodes are strings from vector list
            for(String value : valueList) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(value);
                fieldNode.add(child);
            }

            // Append category node (+children) to controller node
            controllerNode.add(fieldNode);
        });

        return controllerNode;
    }

    public void updateControllerSelection() {
        /**
         * utility function that updates fields on main menu and controller advanced menu according to controller highlighted on controller tree.
         *
         * @assert Wayside Object "thisController" accurately points to selected controller node
         *
         * @before  fields on main menu and controller advanced menu refer to old controller seleciton
         * @after   fields on main menu and controller advanced menu refer to the controller highlighted on the tree
         */

        // Update selection text on main menu
        updateControllerSelectText();

        // populate the Testing Input and Output tables in Controller Advanced Menu
        updateTestingTables();

        // Generate and set header for Controller Advanced Menu
        String ControllerMenuHeaderText = "Controller Menu - %s".formatted(thisController.getName());
        ControllerMenuHeaderText += " - configured as %s".formatted((hardwareView) ? "Hardware" : "Software" );
        ControllerMenuHeader.setText(ControllerMenuHeaderText);
    }

    public void updateControllerSelectText() {
        /**
         * utility function that updates text field on main menu
         *
         * @before selection text on main menu possibly refers to wrong controller
         * @after selection text on main menu refers to name of "thisController" member
         */
        String Label = "Current Selected Controller: ";
        String cont = thisController.getName();

        // Change of implementation changes header of selection
        SelectedControllerText.setText(Label + cont);
    }

    public void updateTestingTables() {
        /**
         * Fills in the testing tables will the correct input and output names and values.
         *
         * @assert the correct, selected controller is stored in "thisController"
         */
        protectedUpdate = true;
        InputTable.setModel( buildInputTableModel() );
        OutputTable.setModel( buildOutputTableModel() );
        protectedUpdate = false;
    }

    public static DefaultTableModel buildInputTableModel() {
        /**
         * creates table model for the input table.
         * @before the table in the Advanced menu does not reflect the current wayside controller status
         */

        Vector<String> columnIdentifiers = new Vector<String>();
        Vector< Vector<Object>> dataVector = new Vector< Vector<Object>>();

        columnIdentifiers.add("Input Names");
        columnIdentifiers.add("Input Values");

        // Fill Data Vector
        List<String> InputNames = thisController.getInputNames();
        boolean[] InputValues = thisController.getGPIO().getAllInputValues();

        //List<String> InputNames = thisController.getAllNames();
        //List<Object> InputValues = thisController.getAllData();

        for (int i=0; i<InputNames.size(); i++) {
            Vector<Object> newrow = new Vector<Object>();
            //newrow.add("value name #%d".formatted(i));
            //newrow.add("value value #%d".formatted(i));
            newrow.add(InputNames.get(i));
            newrow.add(InputValues[i]);//InputValues.get(i).toString());
            dataVector.add(newrow);
        }

        return new DefaultTableModel(dataVector, columnIdentifiers);
    }

    public static DefaultTableModel buildOutputTableModel() {
        /**
         * creates table model for the output table.
         */

        Vector<String> columnIdentifiers = new Vector<String>();
        Vector< Vector<Object>> dataVector = new Vector< Vector<Object>>();

        columnIdentifiers.add("Output Names");
        columnIdentifiers.add("Output Values");

        // Fill Data Vector
        GPIO gpio = thisController.getGPIO();
        List<String> InputNames = gpio.getOutputNames();
        Boolean[] InputValues = gpio.getOutputValues();

        for (int i=0; i<InputValues.length; i++) {
            Vector<Object> newrow = new Vector<Object>();
            //newrow.add("value name #%d".formatted(i));
            //newrow.add("value value #%d".formatted(i));
            newrow.add(InputNames.get(i));
            newrow.add(InputValues[i]);
            dataVector.add(newrow);
        }


        return new DefaultTableModel(dataVector, columnIdentifiers);
    }
    public void handleHardwareTransition() {
        /**
         * handles (GUI) transition to Hardware Implementation for this controller;
         * TODO: invokes Hardware transition protocol from controller class
         *
         * @before advanced controller menu is in software format
         * @after advanced controller menu is in hardware format
         */
        hardwareView = true;

        // Change of implementation changes header of selection
        updateControllerSelection();
    }

    public void handleSoftwareTransition() {
        /**
         * handleHardwareTransition: handles every action that needs to be done for a software transition from hardware configuration; update GUI and call wayside controller
         *  hardware transition operation.
         *
         * @before advanced controller menu is in hardware format
         * @after advanced controller menu is in software format
         */
        hardwareView = false;
        updateControllerSelection();
    }

    /*
     * Elijah: The rest of this script is auto generated by the editor I am using in Apache Netbeans
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        ControllerAdvancedMenuButtons = new javax.swing.ButtonGroup();
        WaysideWindowFrame = new javax.swing.JPanel();
        MainMenuScreen = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ControllerMenuButton = new javax.swing.JButton();
        ControllerQuickMenu = new javax.swing.JPanel();
        TreeScrollPane = new javax.swing.JScrollPane();
        ControllerListTree = new javax.swing.JTree();
        ControllerGeographyLayout = new javax.swing.JPanel();
        WaysideConsoleFrame = new javax.swing.JPanel();
        ConsoleEditorScrollPanel = new javax.swing.JScrollPane();
        consoleEditor = new javax.swing.JEditorPane();
        executeConsole = new javax.swing.JButton();
        SelectedControllerText = new javax.swing.JLabel();
        ControllerScreen = new javax.swing.JPanel();
        ControllerMenuHeader = new javax.swing.JLabel();
        ReturnToMainMenuButton = new javax.swing.JButton();
        ControllerSpace = new javax.swing.JPanel();
        StatusArea = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        JurisdictionArea = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        TestingArea = new javax.swing.JPanel();
        TestMenuHeader = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        OutputTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        InputTable = new javax.swing.JTable();
        PLCArea = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        AreaSelectPanel = new javax.swing.JPanel();
        PLCRadioSelectButton = new javax.swing.JRadioButton();
        StatusRadioSelectButton = new javax.swing.JRadioButton();
        JurisdictionRadioSelectButton = new javax.swing.JRadioButton();
        TestingRadioSelectButton = new javax.swing.JRadioButton();
        HardwarePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        HardwareButton = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        WaysideWindowFrame.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Malayalam Sangam MN", 0, 18)); // NOI18N
        jLabel1.setText("Main Menu");

        ControllerMenuButton.setText("Open Controller Menu");
        ControllerMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ControllerMenuButtonActionPerformed(evt);
            }
        });

        ControllerQuickMenu.setBackground(new java.awt.Color(153, 153, 153));

        // generate top node called "Controller List"
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Controller List");

        // Under the root, add each individual controller as a node
        // Generate node for each controller
        String NodeName;
        for (WaysideController controller : controllers) {
            DefaultMutableTreeNode controllerNode = treeNodeFromControllerObject(controller);
            top.add(controllerNode);
        }

        // Place nodes into tree
        DefaultTreeModel tree = (DefaultTreeModel)ControllerListTree.getModel();
        tree.setRoot(top);
        ControllerListTree.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Controller Quick Menu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 8), new java.awt.Color(153, 153, 153))); // NOI18N
        ControllerListTree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
            }
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                ControllerListTreeTreeExpanded(evt);
            }
        });
        ControllerListTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                ControllerListTreeValueChanged(evt);
            }
        });
        TreeScrollPane.setViewportView(ControllerListTree);

        javax.swing.GroupLayout ControllerQuickMenuLayout = new javax.swing.GroupLayout(ControllerQuickMenu);
        ControllerQuickMenu.setLayout(ControllerQuickMenuLayout);
        ControllerQuickMenuLayout.setHorizontalGroup(
                ControllerQuickMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 263, Short.MAX_VALUE)
                        .addGroup(ControllerQuickMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(ControllerQuickMenuLayout.createSequentialGroup()
                                        .addComponent(TreeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        ControllerQuickMenuLayout.setVerticalGroup(
                ControllerQuickMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(ControllerQuickMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControllerQuickMenuLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(TreeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        ControllerGeographyLayout.setBackground(new java.awt.Color(231, 239, 232));
        ControllerGeographyLayout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout ControllerGeographyLayoutLayout = new javax.swing.GroupLayout(ControllerGeographyLayout);
        ControllerGeographyLayout.setLayout(ControllerGeographyLayoutLayout);
        ControllerGeographyLayoutLayout.setHorizontalGroup(
                ControllerGeographyLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 673, Short.MAX_VALUE)
        );
        ControllerGeographyLayoutLayout.setVerticalGroup(
                ControllerGeographyLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 358, Short.MAX_VALUE)
        );

        WaysideConsoleFrame.setBackground(new java.awt.Color(255, 255, 255));
        WaysideConsoleFrame.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Wayside Command Console", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 8), new java.awt.Color(153, 153, 153))); // NOI18N

        ConsoleEditorScrollPanel.setViewportView(consoleEditor);

        executeConsole.setText("Execute Command");
        executeConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeConsoleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout WaysideConsoleFrameLayout = new javax.swing.GroupLayout(WaysideConsoleFrame);
        WaysideConsoleFrame.setLayout(WaysideConsoleFrameLayout);
        WaysideConsoleFrameLayout.setHorizontalGroup(
                WaysideConsoleFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WaysideConsoleFrameLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ConsoleEditorScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(executeConsole)
                                .addContainerGap())
        );
        WaysideConsoleFrameLayout.setVerticalGroup(
                WaysideConsoleFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(WaysideConsoleFrameLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ConsoleEditorScrollPanel)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WaysideConsoleFrameLayout.createSequentialGroup()
                                .addContainerGap(150, Short.MAX_VALUE)
                                .addComponent(executeConsole)
                                .addGap(94, 94, 94))
        );

        SelectedControllerText.setText("Controller Value");

        javax.swing.GroupLayout MainMenuScreenLayout = new javax.swing.GroupLayout(MainMenuScreen);
        MainMenuScreen.setLayout(MainMenuScreenLayout);
        MainMenuScreenLayout.setHorizontalGroup(
                MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainMenuScreenLayout.createSequentialGroup()
                                .addGroup(MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                .addGroup(MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                                .addGroup(MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                                                .addContainerGap()
                                                                                .addComponent(SelectedControllerText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                                                .addComponent(ControllerMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 92, Short.MAX_VALUE)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addComponent(ControllerGeographyLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(WaysideConsoleFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(17, 17, 17))
                        .addGroup(MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(ControllerQuickMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(773, Short.MAX_VALUE)))
        );
        MainMenuScreenLayout.setVerticalGroup(
                MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                .addGroup(MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(ControllerGeographyLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(MainMenuScreenLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(SelectedControllerText)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ControllerMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(WaysideConsoleFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12))
                        .addGroup(MainMenuScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainMenuScreenLayout.createSequentialGroup()
                                        .addContainerGap(140, Short.MAX_VALUE)
                                        .addComponent(ControllerQuickMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap()))
        );

        WaysideWindowFrame.add(MainMenuScreen, "MainMenu");

        ControllerMenuHeader.setText("ControllerMenu");

        ReturnToMainMenuButton.setText("Return to Main Menu");
        ReturnToMainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReturnToMainMenuButtonActionPerformed(evt);
            }
        });

        ControllerSpace.setBackground(new java.awt.Color(153, 153, 153));
        ControllerSpace.setLayout(new java.awt.CardLayout());

        StatusArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Status Area");

        javax.swing.GroupLayout StatusAreaLayout = new javax.swing.GroupLayout(StatusArea);
        StatusArea.setLayout(StatusAreaLayout);
        StatusAreaLayout.setHorizontalGroup(
                StatusAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(StatusAreaLayout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(jLabel5)
                                .addContainerGap(590, Short.MAX_VALUE))
        );
        StatusAreaLayout.setVerticalGroup(
                StatusAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(StatusAreaLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(jLabel5)
                                .addContainerGap(521, Short.MAX_VALUE))
        );

        ControllerSpace.add(StatusArea, "StatusArea");

        JurisdictionArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("Jurisdiction Area");

        javax.swing.GroupLayout JurisdictionAreaLayout = new javax.swing.GroupLayout(JurisdictionArea);
        JurisdictionArea.setLayout(JurisdictionAreaLayout);
        JurisdictionAreaLayout.setHorizontalGroup(
                JurisdictionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(JurisdictionAreaLayout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(jLabel4)
                                .addContainerGap(543, Short.MAX_VALUE))
        );
        JurisdictionAreaLayout.setVerticalGroup(
                JurisdictionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(JurisdictionAreaLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel4)
                                .addContainerGap(510, Short.MAX_VALUE))
        );

        ControllerSpace.add(JurisdictionArea, "JurisdictionArea");

        TestingArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TestMenuHeader.setText("Testing Area");

        OutputTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                },
                new String [] {
                        "Output Name", "Output Value"
                }
        ));
        jScrollPane2.setViewportView(OutputTable);

        jLabel3.setText("Inputs");

        jLabel7.setText("Outputs");

        InputTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                },
                new String [] {
                        "Input Name", "Input Value"
                }
        ));
        InputTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                InputTablePropertyChange(evt);
            }
        });
        jScrollPane3.setViewportView(InputTable);

        javax.swing.GroupLayout TestingAreaLayout = new javax.swing.GroupLayout(TestingArea);
        TestingArea.setLayout(TestingAreaLayout);
        TestingAreaLayout.setHorizontalGroup(
                TestingAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(TestingAreaLayout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(TestingAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(TestingAreaLayout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(45, 45, 45)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(TestMenuHeader))
                                .addContainerGap(93, Short.MAX_VALUE))
                        .addGroup(TestingAreaLayout.createSequentialGroup()
                                .addGap(177, 177, 177)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(209, 209, 209))
        );
        TestingAreaLayout.setVerticalGroup(
                TestingAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(TestingAreaLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(TestMenuHeader)
                                .addGap(22, 22, 22)
                                .addGroup(TestingAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(TestingAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                                        .addComponent(jScrollPane3))
                                .addContainerGap(55, Short.MAX_VALUE))
        );

        ControllerSpace.add(TestingArea, "TestingArea");

        PLCArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setText("PLC area");

        javax.swing.GroupLayout PLCAreaLayout = new javax.swing.GroupLayout(PLCArea);
        PLCArea.setLayout(PLCAreaLayout);
        PLCAreaLayout.setHorizontalGroup(
                PLCAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PLCAreaLayout.createSequentialGroup()
                                .addGap(103, 103, 103)
                                .addComponent(jLabel6)
                                .addContainerGap(593, Short.MAX_VALUE))
        );
        PLCAreaLayout.setVerticalGroup(
                PLCAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PLCAreaLayout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel6)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ControllerSpace.add(PLCArea, "PLCArea");

        AreaSelectPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ControllerAdvancedMenuButtons.add(PLCRadioSelectButton);
        PLCRadioSelectButton.setText("PLC Script");
        PLCRadioSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PLCRadioSelectButtonActionPerformed(evt);
            }
        });

        ControllerAdvancedMenuButtons.add(StatusRadioSelectButton);
        StatusRadioSelectButton.setText("Full Status");
        StatusRadioSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusRadioSelectButtonActionPerformed(evt);
            }
        });

        ControllerAdvancedMenuButtons.add(JurisdictionRadioSelectButton);
        JurisdictionRadioSelectButton.setText("Track Jurisdiction");
        JurisdictionRadioSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JurisdictionRadioSelectButtonActionPerformed(evt);
            }
        });

        ControllerAdvancedMenuButtons.add(TestingRadioSelectButton);
        TestingRadioSelectButton.setText("Testing");
        TestingRadioSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TestingRadioSelectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AreaSelectPanelLayout = new javax.swing.GroupLayout(AreaSelectPanel);
        AreaSelectPanel.setLayout(AreaSelectPanelLayout);
        AreaSelectPanelLayout.setHorizontalGroup(
                AreaSelectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(AreaSelectPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(AreaSelectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(StatusRadioSelectButton)
                                        .addComponent(TestingRadioSelectButton)
                                        .addComponent(PLCRadioSelectButton)
                                        .addComponent(JurisdictionRadioSelectButton))
                                .addContainerGap(86, Short.MAX_VALUE))
        );
        AreaSelectPanelLayout.setVerticalGroup(
                AreaSelectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(AreaSelectPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(PLCRadioSelectButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(StatusRadioSelectButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JurisdictionRadioSelectButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TestingRadioSelectButton)
                                .addContainerGap(181, Short.MAX_VALUE))
        );

        HardwarePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Hardware"));

        jLabel2.setText("Configured as hardware");

        HardwareButton.setText("no");
        HardwareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HardwareButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HardwarePanelLayout = new javax.swing.GroupLayout(HardwarePanel);
        HardwarePanel.setLayout(HardwarePanelLayout);
        HardwarePanelLayout.setHorizontalGroup(
                HardwarePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(HardwarePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(HardwareButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HardwarePanelLayout.setVerticalGroup(
                HardwarePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(HardwarePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(HardwarePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(HardwareButton))
                                .addContainerGap(145, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ControllerScreenLayout = new javax.swing.GroupLayout(ControllerScreen);
        ControllerScreen.setLayout(ControllerScreenLayout);
        ControllerScreenLayout.setHorizontalGroup(
                ControllerScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ControllerScreenLayout.createSequentialGroup()
                                .addGroup(ControllerScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ControllerScreenLayout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addComponent(AreaSelectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(ControllerScreenLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(ControllerScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(HardwarePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ReturnToMainMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addComponent(ControllerSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(ControllerScreenLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(ControllerMenuHeader)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ControllerScreenLayout.setVerticalGroup(
                ControllerScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControllerScreenLayout.createSequentialGroup()
                                .addComponent(ControllerMenuHeader)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ControllerScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(ControllerScreenLayout.createSequentialGroup()
                                                .addComponent(ReturnToMainMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(HardwarePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(AreaSelectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(ControllerSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(86, 86, 86))
        );

        WaysideWindowFrame.add(ControllerScreen, "ControllerMenu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(WaysideWindowFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(WaysideWindowFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void ReturnToMainMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {
        /**
         * @before Advanced Controller Menu card is shown for window
         * @after Main Menu card is shown for window
         */
        // For the WaysideWindowFrame Card Layout, show the MainMenu Card
        CardLayout cards = (CardLayout)WaysideWindowFrame.getLayout();
        cards.show(WaysideWindowFrame,"MainMenu");
    }

    private void ControllerMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {
        /**
         * @before Main Menu card is shown for window
         * @after Advanced Controller Menu card is shown for window
         */

        // Only show if user has recently highlighted/selected a wayside controller
        if (controllerSelected) {
            CardLayout cards = (CardLayout)WaysideWindowFrame.getLayout();
            cards.show(WaysideWindowFrame,"ControllerMenu");

            // Select PLC on radio buttons as default to show
            ControllerAdvancedMenuButtons.setSelected(PLCRadioSelectButton.getModel(), true);
        } else {
            System.out.println("A controller is not selected");
        }
    }


    /* Radio Select Buttons on the Controller Advanced Options Window*/
    private void PLCRadioSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {

        CardLayout cards = (CardLayout)ControllerSpace.getLayout();
        cards.show(ControllerSpace, "PLCArea");
        System.out.println("Changing Controller Area to PLC");
    }

    private void StatusRadioSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CardLayout cards = (CardLayout)ControllerSpace.getLayout();
        cards.show(ControllerSpace, "StatusArea");
        System.out.println("Changing Controller Area to Status");
    }

    private void JurisdictionRadioSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        CardLayout cards = (CardLayout)ControllerSpace.getLayout();
        cards.show(ControllerSpace, "JurisdictionArea");
        System.out.println("Changing Controller Area to Jurisdiction");
    }

    private void TestingRadioSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Code for changing which window area is shown on the Controller Advanced Menu
        CardLayout cards = (CardLayout)ControllerSpace.getLayout();
        cards.show(ControllerSpace, "TestingArea");
        System.out.println("Changing Controller Area to Testing");
    }


    private void ControllerListTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        /**
         * code to update GUI when user selects a new node on the Controller Quick Menu Tree.
         *
         * @before GUI refers to last selected controller node (or "no controller selected" if in default phase)
         * @after GUI refers to newly selected node if-and-only-if node is a controller node
         */

        // Retrieve selected node from tree object
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) ControllerListTree.getLastSelectedPathComponent();

        // No Node is Selected
        if (selectedNode == null) {
            return;
        }

        /* Ignore: Code from when I stored controllers as strings in nodes
        // Get Name of Node
        String selectedNodeName = (String) selectedNode.getUserObject();
        System.out.printf("Tree node: "+selectedNodeName+" is selected. ");

        // Verifying that selected Node is a Controller
        for (EmptyWaysideController ControllerObject: controllers) {
            // If node is a controller
            if (ControllerObject.getName() == selectedNodeName) {
                /*
                    Assert: from this point on, a valid controller will always be selected.

                controllerSelected =true;
                thisController = ControllerObject;
                System.out.println( " (Selected controller is now: "+ControllerObject.getName()+")" );
                updateControllerSelection();
                return;
            }
        }
        */

        // Get object from selected node
        Object selectedNodeObject = selectedNode.getUserObject();

        // See if object is wayside controller
        if (selectedNodeObject instanceof WaysideController) {
            // is wayside controller
            thisController =(WaysideController) selectedNodeObject;
            controllerSelected =true;
            System.out.println( " (Selected controller is now: "+thisController.getName()+")" );
            updateControllerSelection();
            return;
        } else {
            // is not wayside controller
            System.out.println(" (ignoring selection)");
            return;
        }



    }

    private void HardwareButtonActionPerformed(java.awt.event.ActionEvent evt) {
        /**
         * handles the graphical change when user specifies wayside controller as hardware implementation; invokes transition handler.
         */
        // Assert: hardware button selected means user intends for this controller to be a hardware controller
        boolean isOn = HardwareButton.isSelected();
        if (isOn) {
            HardwareButton.setText("yes");
            HardwareButton.setBackground(Color.GREEN);
            handleHardwareTransition();
        } else {
            HardwareButton.setText("no");
            HardwareButton.setBackground(Color.GRAY);
            handleSoftwareTransition();
        }
    }

    private void ControllerListTreeTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {
        // TODO Figure out if node was expanded by click, only then can we use that as current controller
        /*
        // get the node from the path of the tree expansion event
        DefaultMutableTreeNode expanded = (DefaultMutableTreeNode) evt.getPath().getLastPathComponent();
        Object nodeObject = expanded.getUserObject();

        // See if object is wayside controller
        if (nodeObject instanceof EmptyWaysideController) {
            // is wayside controller
            thisController =(EmptyWaysideController) nodeObject;
            controllerSelected =true;
            System.out.println( " (Selected controller is now: ("+thisController.getName()+")" );
            updateControllerSelection();
            return;
        } else {
            // is not wayside controller
            System.out.println(" (ignoring selection)");
            return;
        }*/
    }

    private void executeConsoleActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String result = "";
        String rawConsoleString = consoleEditor.getText();
        String consoleLines[] = rawConsoleString.split("\\r?\\n");

        System.out.println(rawConsoleString);
        try {
            //result = system.readConsole(rawConsoleString);
            for (String commandLine : consoleLines) {
                System.out.println("Executing command line: " + commandLine);
                result = system.readConsole(commandLine);
            }


        } catch (Exception e) {
            System.out.println("Failure to read console in wayside system");
            e.printStackTrace();
        }

        System.out.println("Result from Controller: " + result);
        consoleEditor.setText(result);
    }

    private void InputTablePropertyChange(java.beans.PropertyChangeEvent evt) {
        /**
         * if the user changes values of the inputs, all the input values to change
         */

        // Avoid allowing a Table-Redraw to invoke this function
        if (protectedUpdate) {
            //System.out.println("Ignoring property change b.c. protected");
            return;
        }

        String newInputValue;
        int row = InputTable.getSelectedRow();
        int col = InputTable.getSelectedColumn();
        int inputValueColumn = 1;
        boolean successful;

        // Ignore if any column has changed other than the input value column
        if(col != inputValueColumn) {return;}

        // Allows us to ignore the "double click causes event"
        if(evt.getOldValue() == null) {return;}

        // If row/col is a valid index
        if (row > -1 && col > -1) {
            // Get String from cell
            Object value = InputTable.getValueAt(row,col);
            //System.out.println("New value: " + value);
            newInputValue = value.toString().toLowerCase();

            // Update controller inputs with string
            try {
                thisController.updateTestInputs(newInputValue, row);
                updateControllerSelection();

            } catch (Exception e) {
                System.out.println("Failure with changing input values to controller");
                e.printStackTrace();
            }
        }

    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WaysideUIJFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WaysideUIJFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WaysideUIJFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WaysideUIJFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new WaysideUIJFrameWindow().setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel AreaSelectPanel;
    private javax.swing.JScrollPane ConsoleEditorScrollPanel;
    private javax.swing.ButtonGroup ControllerAdvancedMenuButtons;
    private javax.swing.JPanel ControllerGeographyLayout;
    private javax.swing.JTree ControllerListTree;
    private javax.swing.JButton ControllerMenuButton;
    private javax.swing.JLabel ControllerMenuHeader;
    private javax.swing.JPanel ControllerQuickMenu;
    private javax.swing.JPanel ControllerScreen;
    private javax.swing.JPanel ControllerSpace;
    private javax.swing.JToggleButton HardwareButton;
    private javax.swing.JPanel HardwarePanel;
    private javax.swing.JTable InputTable;
    private javax.swing.JPanel JurisdictionArea;
    private javax.swing.JRadioButton JurisdictionRadioSelectButton;
    private javax.swing.JPanel MainMenuScreen;
    private javax.swing.JTable OutputTable;
    private javax.swing.JPanel PLCArea;
    private javax.swing.JRadioButton PLCRadioSelectButton;
    private javax.swing.JButton ReturnToMainMenuButton;
    private javax.swing.JLabel SelectedControllerText;
    private javax.swing.JPanel StatusArea;
    private javax.swing.JRadioButton StatusRadioSelectButton;
    private javax.swing.JLabel TestMenuHeader;
    private javax.swing.JPanel TestingArea;
    private javax.swing.JRadioButton TestingRadioSelectButton;
    private javax.swing.JScrollPane TreeScrollPane;
    private javax.swing.JPanel WaysideConsoleFrame;
    private javax.swing.JPanel WaysideWindowFrame;
    private javax.swing.JEditorPane consoleEditor;
    private javax.swing.JButton executeConsole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration
}