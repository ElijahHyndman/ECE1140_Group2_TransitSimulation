/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import GUIInterface.AppGUIModule;
import SimulationEnvironment.*;
import WaysideController.WaysideSystem;
import WorldClock.WorldClock;

import java.awt.CardLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author elijah
 */
public class SimulationEnvironmentUI extends javax.swing.JFrame implements AppGUIModule {
    /** Members
     */
    private SimulationEnvironment SE;

    public SimulationEnvironmentUI() {
        initComponents();
        WorldClock clk = SE.getClock();
        //ClockRatioSlider.setMinimum((int) clk.MINIMUM_RATIO);
        //ClockRatioSlider.setMaximum((int) clk.MAXIMUM_RATIO);
        //ClockResolutionSlider.setMinimum((int) clk.MINIMUM_RESOLUTION);
        //ClockResolutionSlider.setMaximum((int) clk.MAXIMUM_RESOLUTION);
        UpdateSpawnTables();
        //ClockRatioSlider.setValue((int) SE.getClock().getRatio());
        //ClockResolutionSlider.setValue((int) SE.getClock().getResolution());
        this.setVisible(true);
        initComponents();
    }

    public SimulationEnvironmentUI(SimulationEnvironment existingSimulationEnvironment) {
        latch(existingSimulationEnvironment);
        initComponents();
        WorldClock clk = SE.getClock();
        //ClockRatioSlider.setMinimum((int) clk.MINIMUM_RATIO);
        //ClockRatioSlider.setMaximum((int) clk.MAXIMUM_RATIO);
        //ClockResolutionSlider.setMinimum((int) clk.MINIMUM_RESOLUTION);
        //ClockResolutionSlider.setMaximum((int) clk.MAXIMUM_RESOLUTION);
        UpdateSpawnTables();
        this.setVisible(true);
        initComponents();
    }



    /*
        Inheritance Methods
     */
    //@Override
    public void latch(Object myObj) {
        SE = (SimulationEnvironment) myObj;
    }
    //@Override
    public void update() {
        // Show newest Clock Time
        TimeLabel.setText(SE.getClock().getTimeString());
        // Redraw all tables
        UpdateSpawnTables();
    }

    //@Override
    public Object getJFrame() {
        return this;
    }

    //@Override
    public void setVis(boolean visible) {
        setVisible(visible);
    }

    public void setGreenLine() {
        //CardLayout card = (CardLayout) JFrame.getLayout();
        //card.show(mainPanel,"MainMenuCard");
    }




    public void UpdateSpawnTables() {

        /*
                CTC Table
         */
        // Set the CTC table
        CTCSpawnTable.setFillsViewportHeight(true);
        DefaultTableModel ctcTableModel = (DefaultTableModel) CTCSpawnTable.getModel();
        ctcTableModel.setValueAt("CTC",0,0);
        ctcTableModel.setValueAt("Spawn GUI",0,1);


        /*
                Wayside System Table
         */
        // Set the WaysideSystem table
        DefaultTableModel waysideTableModel = (DefaultTableModel) WaysideSystemSpawnTable.getModel();
        //TODO Vector<WaysideSystem> WSystems = DisplaySE.getCTC().getWaysideSystems();
        //TODO waysideTableModel.setRowCount(WSystems.size());// Set the WaysideSystem table
        //TODO this is hard coded
        ArrayList<WaysideSystem> WSystems = SE.getCTC().getWaysideSystem();
        // If values exist
        if(WSystems.size() != 0) {
            waysideTableModel.setRowCount(WSystems.size());
            waysideTableModel.setValueAt(WSystems.get(0),0,0);
            waysideTableModel.setValueAt("Spawn GUI", 0,1);
        }

        waysideTableModel.setRowCount(WSystems.size());
        for(int index =0;index<WSystems.size(); index++) {
            WaysideSystem ws = WSystems.get(index);
            waysideTableModel.setValueAt(ws,index,0);
            waysideTableModel.setValueAt("Spawn GUI", index,1);
        }


        /*
                Train Model Table
         */
        // Set the TrainUnit table
        DefaultTableModel trainTableModel = (DefaultTableModel) TrainUnitSpawnTable.getModel();
        Vector<TrainUnit> trains = SE.getTrains();

        trainTableModel.setRowCount(trains.size());

        try {TimeUnit.MILLISECONDS.sleep(20);} catch (Exception e) {}
        for(int index =0;index<trains.size(); index++) {
            TrainUnit train = trains.get(index);
            trainTableModel.setValueAt(train.getName(),index,0);
            trainTableModel.setValueAt("Spawn Controller GUI",index,1);
            trainTableModel.setValueAt("Spawn Model GUI",index,2);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        label1 = new java.awt.Label();
        MainWindow = new javax.swing.JPanel();
        ImportMenu = new javax.swing.JPanel();
        TrackPathScrollBar = new javax.swing.JScrollPane();
        TrackPathTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        ImportTrackButton = new javax.swing.JButton();
        ControlMenu = new javax.swing.JPanel();
        UIMenus = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CTCSpawnTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        TrackSpawnTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        WaysideSystemSpawnTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        TrainUnitSpawnTable = new javax.swing.JTable();
        WorldClockMenu = new javax.swing.JPanel();
        ClockRatioTextField = new javax.swing.JTextField();
        ClockResolutionTextField = new javax.swing.JTextField();
        ClockPauseButton = new javax.swing.JButton();
        TimeLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        label1.setText("label1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MainWindow");
        getContentPane().setLayout(new java.awt.CardLayout());

        MainWindow.setLayout(new java.awt.CardLayout());

        ImportMenu.setBackground(new java.awt.Color(102, 102, 102));

        TrackPathScrollBar.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TrackPathTextField.setText("Enter track csv path...");
        TrackPathScrollBar.setViewportView(TrackPathTextField);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Enter Absolute Path To .csv File For Track System");

        ImportTrackButton.setText("Import Track");
        ImportTrackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ImportTrackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ImportMenuLayout = new javax.swing.GroupLayout(ImportMenu);
        ImportMenu.setLayout(ImportMenuLayout);
        ImportMenuLayout.setHorizontalGroup(
                ImportMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ImportMenuLayout.createSequentialGroup()
                                .addGroup(ImportMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(ImportMenuLayout.createSequentialGroup()
                                                .addGap(254, 254, 254)
                                                .addComponent(TrackPathScrollBar, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(ImportMenuLayout.createSequentialGroup()
                                                .addGap(464, 464, 464)
                                                .addComponent(ImportTrackButton))
                                        .addGroup(ImportMenuLayout.createSequentialGroup()
                                                .addGap(362, 362, 362)
                                                .addComponent(jLabel2)))
                                .addContainerGap(274, Short.MAX_VALUE))
        );
        ImportMenuLayout.setVerticalGroup(
                ImportMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImportMenuLayout.createSequentialGroup()
                                .addContainerGap(255, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(39, 39, 39)
                                .addComponent(TrackPathScrollBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ImportTrackButton)
                                .addGap(180, 180, 180))
        );

        MainWindow.add(ImportMenu, "ImportMenu");

        ControlMenu.setBackground(new java.awt.Color(51, 51, 51));
        ControlMenu.setForeground(new java.awt.Color(51, 51, 51));

        UIMenus.setBackground(new java.awt.Color(102, 102, 102));

        CTCSpawnTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null}
                },
                new String [] {
                        "Module", "User Interface"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CTCSpawnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CTCSpawnTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(CTCSpawnTable);

        TrackSpawnTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null}
                },
                new String [] {
                        "Module", "User Interface"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TrackSpawnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TrackSpawnTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TrackSpawnTable);

        WaysideSystemSpawnTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                },
                new String [] {
                        "Module", "User Interface"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        WaysideSystemSpawnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WaysideSystemSpawnTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(WaysideSystemSpawnTable);

        TrainUnitSpawnTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "Module", "Control UI", "Model UI"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TrainUnitSpawnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TrainUnitSpawnTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TrainUnitSpawnTable);

        javax.swing.GroupLayout UIMenusLayout = new javax.swing.GroupLayout(UIMenus);
        UIMenus.setLayout(UIMenusLayout);
        UIMenusLayout.setHorizontalGroup(
                UIMenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(UIMenusLayout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addGroup(UIMenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2)
                                        .addComponent(jScrollPane3)
                                        .addComponent(jScrollPane4))
                                .addContainerGap(106, Short.MAX_VALUE))
        );
        UIMenusLayout.setVerticalGroup(
                UIMenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(UIMenusLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23))
        );

        WorldClockMenu.setBackground(new java.awt.Color(102, 102, 102));

        ClockRatioTextField.setText("ratio");
        ClockRatioTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ClockRatioTextFieldFocusLost(evt);
            }
        });

        ClockResolutionTextField.setText("Resolution");
        ClockResolutionTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ClockResolutionTextFieldFocusLost(evt);
            }
        });
        ClockResolutionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClockResolutionTextFieldActionPerformed(evt);
            }
        });

        ClockPauseButton.setText("Start");
        ClockPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClockPauseButtonActionPerformed(evt);
            }
        });

        TimeLabel.setForeground(new java.awt.Color(255, 255, 255));
        TimeLabel.setText("00:00:00");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("World Clock");

        javax.swing.GroupLayout WorldClockMenuLayout = new javax.swing.GroupLayout(WorldClockMenu);
        WorldClockMenu.setLayout(WorldClockMenuLayout);
        WorldClockMenuLayout.setHorizontalGroup(
                WorldClockMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(WorldClockMenuLayout.createSequentialGroup()
                                .addGroup(WorldClockMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(WorldClockMenuLayout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addGroup(WorldClockMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(WorldClockMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(ClockPauseButton)
                                                                .addComponent(ClockResolutionTextField)
                                                                .addComponent(ClockRatioTextField))
                                                        .addGroup(WorldClockMenuLayout.createSequentialGroup()
                                                                .addGap(3, 3, 3)
                                                                .addComponent(jLabel1))))
                                        .addGroup(WorldClockMenuLayout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(TimeLabel)))
                                .addContainerGap(18, Short.MAX_VALUE))
        );
        WorldClockMenuLayout.setVerticalGroup(
                WorldClockMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WorldClockMenuLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(TimeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ClockPauseButton)
                                .addGap(200, 200, 200)
                                .addComponent(ClockRatioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(ClockResolutionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(96, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ControlMenuLayout = new javax.swing.GroupLayout(ControlMenu);
        ControlMenu.setLayout(ControlMenuLayout);
        ControlMenuLayout.setHorizontalGroup(
                ControlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlMenuLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(WorldClockMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(UIMenus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(44, Short.MAX_VALUE))
        );
        ControlMenuLayout.setVerticalGroup(
                ControlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlMenuLayout.createSequentialGroup()
                                .addContainerGap(17, Short.MAX_VALUE)
                                .addGroup(ControlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(UIMenus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(WorldClockMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15))
        );

        MainWindow.add(ControlMenu, "ControlMenu");

        getContentPane().add(MainWindow, "card4");

        pack();
    }// </editor-fold>

    private void ClockResolutionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void TrainUnitSpawnTableMouseClicked(java.awt.event.MouseEvent evt) {

        // TODO add your handling code here:
        int spawnModelColumn = 2;
        int spawnControllerColumn = 1;

        Point point = evt.getPoint();
        int column = TrainUnitSpawnTable.columnAtPoint(point);
        int row = TrainUnitSpawnTable.rowAtPoint(point);


        System.out.println(String.format("Clicked on (%d,%d)",row,column));


        Vector<TrainUnit> trains = SE.getTrains();
        TrainUnit thisTrain = null;
        try {
            thisTrain = trains.get(row);
        } catch(Exception e) {
            System.out.printf("Error when accessing selected train row from TrainList in SE UI (trainslength=%d selectedTrainRow=%d)\n",trains.size(),row);
            e.printStackTrace();
        }

        if (column == spawnModelColumn) {
            //System.out.println("Spawning CTC Gui");
            //DisplaySE.spawnTrainModelGUI(trains.get(row));
            // TODO
//            trainGUI modelUI = new trainGUI(0);
//            modelUI.latch(thisTrain.getHull());
//            modelUI.setVisible(true);
//            try{GUIWindowLauncher.launchWindow(modelUI);} catch (Exception e) {}
        }
        else if (column == spawnControllerColumn) {
            //DisplaySE.spawnTrainControllerUI(trains.get(row));
            // TODO
//            DriverUI controllerUI = new DriverUI();
//            controllerUI.latch(thisTrain.getController());
//            try{GUIWindowLauncher.launchWindow(controllerUI);} catch (Exception e) {}
        }
    }

    private void WaysideSystemSpawnTableMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void TrackSpawnTableMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void CTCSpawnTableMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void ClockRatioTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void ClockResolutionTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
    }

    private void ClockPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {

        String currentMode = ClockPauseButton.getText();
        String textToStart = "Start";
        String textToPause = "Pause";
        if (currentMode.equals(textToStart)) {
            SE.getClock().start();
            ClockPauseButton.setText(textToPause);
        } else if (currentMode.equals(textToPause)){
            SE.getClock().halt();
            ClockPauseButton.setText(textToStart);
        } else {
            ClockPauseButton.setText("error");
        }
    }

    public void ImportTrackButtonActionPerformed(java.awt.event.MouseEvent evt){
        String pathString = getPathString();
        if (!pathString.equals("Enter track csv path...")){

        }

    }




    public String getPathString() {
        String path = TrackPathTextField.getText();
        System.out.println("Path given is: " + path);
        return path;
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
            java.util.logging.Logger.getLogger(SimulationEnvironmentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimulationEnvironmentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimulationEnvironmentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimulationEnvironmentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SimulationEnvironment SE = new SimulationEnvironment();
                new SimulationEnvironmentUI(SE).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JTable CTCSpawnTable;
    private javax.swing.JButton ClockPauseButton;
    private javax.swing.JTextField ClockRatioTextField;
    private javax.swing.JTextField ClockResolutionTextField;
    private javax.swing.JPanel ControlMenu;
    private javax.swing.JPanel ImportMenu;
    private javax.swing.JButton ImportTrackButton;
    private javax.swing.JPanel MainWindow;
    private javax.swing.JLabel TimeLabel;
    private javax.swing.JScrollPane TrackPathScrollBar;
    private javax.swing.JTextField TrackPathTextField;
    private javax.swing.JTable TrackSpawnTable;
    private javax.swing.JTable TrainUnitSpawnTable;
    private javax.swing.JPanel UIMenus;
    private javax.swing.JTable WaysideSystemSpawnTable;
    private javax.swing.JPanel WorldClockMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private java.awt.Label label1;
    // End of variables declaration
}
