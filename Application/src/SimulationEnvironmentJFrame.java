/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import CTCOffice.CTCOffice;
import SimulationEnvironment.SimulationEnvironment;
import SimulationEnvironment.TrainUnit;
import TrackConstruction.TrackElement;
import TrainControlUI.DriverUI;
import TrainModel.trainGUI;
import WaysideController.WaysideSystem;
import WorldClock.WorldClock;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author elijah
 */
public class SimulationEnvironmentJFrame extends javax.swing.JFrame {

    private SimulationEnvironment DisplaySE;
    /**
     * Creates new form SimulationEnvironmentJFrame
     */
    public SimulationEnvironmentJFrame(SimulationEnvironment mySE) {
        DisplaySE = mySE;
        initComponents();
        WorldClock clk = DisplaySE.getClock();
        //ClockRatioSlider.setMinimum((int) clk.MINIMUM_RATIO);
        //ClockRatioSlider.setMaximum((int) clk.MAXIMUM_RATIO);
        //ClockResolutionSlider.setMinimum((int) clk.MINIMUM_RESOLUTION);
        //ClockResolutionSlider.setMaximum((int) clk.MAXIMUM_RESOLUTION);
        UpdateSpawnTables();
        ClockRatioSlider.setValue((int) DisplaySE.getClock().getRatio());
        ClockResolutionSlider.setValue((int) DisplaySE.getClock().getResolution());
        this.setVisible(true);
    }
    public void latch(Object myObj) {
        DisplaySE = (SimulationEnvironment) myObj;
    }
    public void update() {
        TimeLabel.setText(DisplaySE.getClock().getTimeString());
        UpdateSpawnTables();
    }

    public void setGreenLine() {
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel,"MainMenuCard");
    }

    public void UpdateSpawnTables() {
        // Set the CTC table
        CTCSpawnTable.setFillsViewportHeight(true);
        DefaultTableModel ctcTableModel = (DefaultTableModel) CTCSpawnTable.getModel();
        ctcTableModel.setValueAt("CTC",0,0);
        ctcTableModel.setValueAt("Spawn GUI",0,1);

        // Set the WaysideSystem table
        DefaultTableModel waysideTableModel = (DefaultTableModel) WaysideSystemSpawnTable.getModel();
        //TODO Vector<WaysideSystem> WSystems = DisplaySE.getCTC().getWaysideSystems();
        //TODO waysideTableModel.setRowCount(WSystems.size());// Set the WaysideSystem table
        //TODO this is hard coded
        /*if(WSystems.size() != 0) {
            waysideTableModel.setRowCount(WSystems.size());
            waysideTableModel.setValueAt(WSystems.get(0),0,0);
            waysideTableModel.setValueAt("Spawn GUI", 0,1);
        }
        */
        //waysideTableModel.setRowCount(WSystems.size());
        // TODO
//        for(int index =0;index<WSystems.size(); index++) {
//            WaysideSystem ws = WSystems.get(index);
//            waysideTableModel.setValueAt(ws,index,0);
//            waysideTableModel.setValueAt("Spawn GUI", index,1);
//        }

        // Set the TrainUnit table
        DefaultTableModel trainTableModel = (DefaultTableModel) TrainUnitSpawnTable.getModel();
        Vector<TrainUnit> trains = DisplaySE.getTrains();

        trainTableModel.setRowCount(trains.size());

        try {TimeUnit.MILLISECONDS.sleep(20);} catch (Exception e) {}
        for(int index =0;index<trains.size(); index++) {
            TrainUnit train = trains.get(index);
            trainTableModel.setValueAt(train.getName(),index,0);
            trainTableModel.setValueAt("Spawn Controller GUI",index,1);
            trainTableModel.setValueAt("Spawn Model GUI",index,2);
        }
    }


    public String getPathString() {
        String path = AbsolutePathToCSVField.getText();
        System.out.println("Path given is: " + path);
        return path;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        ImportTrackPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        AbsolutePathToCSVField = new javax.swing.JTextField();
        MainMenu = new javax.swing.JPanel();
        ClockControlPane = new javax.swing.JPanel();
        ClockResolutionSlider = new javax.swing.JSlider();
        WorldClock clk = DisplaySE.getClock();
        ClockRatioSlider = new javax.swing.JSlider();
        ClockPauseButton = new javax.swing.JButton();
        WorldClockSpeedLabel = new javax.swing.JLabel();
        PhysicsResolutionLabel = new javax.swing.JLabel();
        TimeLabel = new javax.swing.JLabel();
        GUISpawnPane = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CTCSpawnTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        WaysideSystemSpawnTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        TrainUnitSpawnTable = new javax.swing.JTable();
        SpawnTrainButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MainWindow");

        mainPanel.setLayout(new java.awt.CardLayout());

        jLabel1.setText("Import Track");

        jButton1.setText("Import Track .csv");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        AbsolutePathToCSVField.setText("Absolute path to .csv");
        AbsolutePathToCSVField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbsolutePathToCSVFieldActionPerformed(evt);
            }
        });
        jScrollPane1.setViewportView(AbsolutePathToCSVField);

        javax.swing.GroupLayout ImportTrackPanelLayout = new javax.swing.GroupLayout(ImportTrackPanel);
        ImportTrackPanel.setLayout(ImportTrackPanelLayout);
        ImportTrackPanelLayout.setHorizontalGroup(
                ImportTrackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ImportTrackPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(307, 307, 307))
                        .addGroup(ImportTrackPanelLayout.createSequentialGroup()
                                .addGap(281, 281, 281)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(ImportTrackPanelLayout.createSequentialGroup()
                                .addGap(210, 210, 210)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(446, Short.MAX_VALUE))
        );
        ImportTrackPanelLayout.setVerticalGroup(
                ImportTrackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ImportTrackPanelLayout.createSequentialGroup()
                                .addContainerGap(332, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60))
        );

        mainPanel.add(ImportTrackPanel, "ImportTrackCard");

        MainMenu.setBackground(new java.awt.Color(51, 51, 51));

        ClockControlPane.setBackground(new java.awt.Color(102, 102, 102));
        ClockControlPane.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        ClockResolutionSlider.setMinimum(1);
        ClockResolutionSlider.setMaximum((int)clk.MAX_ALLOWABLE_RESOLUTION);
        ClockResolutionSlider.setMajorTickSpacing(2);
        ClockResolutionSlider.setPaintTicks(true);
        ClockResolutionSlider.setPaintLabels(true);
        ClockResolutionSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ClockResolutionSliderMouseReleased(evt);
            }
        });

        ClockRatioSlider.setMajorTickSpacing(1);
        ClockRatioSlider.setMinimum(1);
        ClockRatioSlider.setMaximum((int)clk.MAX_ALLOWABLE_RATIO);
        ClockRatioSlider.setMajorTickSpacing(2);
        ClockRatioSlider.setPaintTicks(true);
        ClockRatioSlider.setPaintLabels(true);
        ClockRatioSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ClockRatioSliderMouseReleased(evt);
            }
        });

        ClockPauseButton.setText("Start");
        ClockPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClockPauseButtonActionPerformed(evt);
            }
        });

        WorldClockSpeedLabel.setText("World Clock Speed");

        PhysicsResolutionLabel.setText("Physics Resolution");

        TimeLabel.setText("HH:mm:ss");

        javax.swing.GroupLayout ClockControlPaneLayout = new javax.swing.GroupLayout(ClockControlPane);
        ClockControlPane.setLayout(ClockControlPaneLayout);
        ClockControlPaneLayout.setHorizontalGroup(
                ClockControlPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ClockControlPaneLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(ClockPauseButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                                .addGroup(ClockControlPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(ClockControlPaneLayout.createSequentialGroup()
                                                .addComponent(TimeLabel)
                                                .addGap(39, 39, 39)
                                                .addComponent(ClockRatioSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ClockResolutionSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(155, 155, 155))
                                        .addGroup(ClockControlPaneLayout.createSequentialGroup()
                                                .addComponent(WorldClockSpeedLabel)
                                                .addGap(82, 82, 82)
                                                .addComponent(PhysicsResolutionLabel)
                                                .addGap(189, 189, 189))))
        );
        ClockControlPaneLayout.setVerticalGroup(
                ClockControlPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ClockControlPaneLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(ClockControlPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(ClockResolutionSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ClockRatioSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(ClockControlPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(ClockControlPaneLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                                .addComponent(PhysicsResolutionLabel)
                                                .addContainerGap())
                                        .addGroup(ClockControlPaneLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(WorldClockSpeedLabel)
                                                .addGap(0, 0, Short.MAX_VALUE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ClockControlPaneLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(ClockControlPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ClockPauseButton)
                                        .addComponent(TimeLabel))
                                .addGap(20, 20, 20))
        );

        CTCSpawnTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null}
                },
                new String [] {
                        "Object", "Spawn"
                }
        ));
        CTCSpawnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CTCSpawnTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(CTCSpawnTable);

        WaysideSystemSpawnTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null},
                        {null, null}
                },
                new String [] {
                        "Object", "Spawn"
                }
        ));
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
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "Object", "Spawn Model", "Spawn Controller"
                }
        ));
        TrainUnitSpawnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TrainUnitSpawnTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TrainUnitSpawnTable);

        SpawnTrainButton.setText("Spawn Train");
        SpawnTrainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpawnTrainButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout GUISpawnPaneLayout = new javax.swing.GroupLayout(GUISpawnPane);
        GUISpawnPane.setLayout(GUISpawnPaneLayout);
        GUISpawnPaneLayout.setHorizontalGroup(
                GUISpawnPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GUISpawnPaneLayout.createSequentialGroup()
                                .addGroup(GUISpawnPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(GUISpawnPaneLayout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(27, 27, 27)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GUISpawnPaneLayout.createSequentialGroup()
                                                .addGap(68, 68, 68)
                                                .addComponent(SpawnTrainButton)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        GUISpawnPaneLayout.setVerticalGroup(
                GUISpawnPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GUISpawnPaneLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(GUISpawnPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(GUISpawnPaneLayout.createSequentialGroup()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addComponent(SpawnTrainButton))
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(85, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MainMenuLayout = new javax.swing.GroupLayout(MainMenu);
        MainMenu.setLayout(MainMenuLayout);
        MainMenuLayout.setHorizontalGroup(
                MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(MainMenuLayout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addGroup(MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(GUISpawnPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ClockControlPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MainMenuLayout.setVerticalGroup(
                MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(MainMenuLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(ClockControlPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(GUISpawnPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(MainMenu, "MainMenuCard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void AbsolutePathToCSVFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        boolean success = DisplaySE.importTrack(getPathString());
        if (success) {
            CardLayout card = (CardLayout)mainPanel.getLayout();
            card.show(mainPanel, "MainMenuCard");
        }
    }

    private void ClockPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String currentMode = ClockPauseButton.getText();
        String textToStart = "Start";
        String textToPause = "Pause";
        if (currentMode.equals(textToStart)) {
            DisplaySE.getClock().start();
            ClockPauseButton.setText(textToPause);
        } else if (currentMode.equals(textToPause)){
            DisplaySE.getClock().halt();
            ClockPauseButton.setText(textToStart);
        } else {
            ClockPauseButton.setText("error");
        }
    }

    private void SpawnTrainButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        DisplaySE.spawnRunningTrain(new TrackElement(), new TrackElement());
        UpdateSpawnTables();
    }

    private void ClockRatioSliderMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        // TODO add your handling code here:
        double clockratiomax = DisplaySE.getClock().MAX_ALLOWABLE_RATIO;
        double clockratiomin = DisplaySE.getClock().MIN_ALLOWABLE_RATIO;
        //double dist = (clockratiomax -clockratiomin) / 100.0;
        //System.out.println("new ratio: " + dist*ClockRatioSlider.getValue());
        System.out.println(ClockRatioSlider.getValue());
        DisplaySE.getClock().setRatio(ClockRatioSlider.getValue());
    }

    private void ClockResolutionSliderMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        DisplaySE.getClock().setResolution(ClockResolutionSlider.getValue());
    }

    private void CTCSpawnTableMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int spawnGUIColumn = 1;

        Point point = evt.getPoint();
        int column = CTCSpawnTable.columnAtPoint(point);
        int row = CTCSpawnTable.rowAtPoint(point);
        System.out.println(String.format("Clicked on (%d,%d)",row,column));
        if (column == spawnGUIColumn) {
            //System.out.println("Spawning CTC Gui");
            //DisplaySE.spawnCTCGUI(DisplaySE.getCTC());
        }
    }

    private void WaysideSystemSpawnTableMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int spawnWaysideColumn = 1;

        Point point = evt.getPoint();
        int column = WaysideSystemSpawnTable.columnAtPoint(point);
        int row = WaysideSystemSpawnTable.rowAtPoint(point);
        System.out.println(String.format("Clicked on (%d,%d)",row,column));
        if (column == spawnWaysideColumn) {
            //System.out.println("Spawning CTC Gui");
            // TODO Vector<WaysideSystem> WS = DisplaySE.getCTC().getWaysideSystems();
            // TODO DisplaySE.spawnWaysideGUI(WS.get(row));
        }
    }

    private void TrainUnitSpawnTableMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int spawnModelColumn = 2;
        int spawnControllerColumn = 1;

        Point point = evt.getPoint();
        int column = TrainUnitSpawnTable.columnAtPoint(point);
        int row = TrainUnitSpawnTable.rowAtPoint(point);


        System.out.println(String.format("Clicked on (%d,%d)",row,column));


        Vector<TrainUnit> trains = DisplaySE.getTrains();
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
            trainGUI modelUI = new trainGUI(0);
            modelUI.latch(thisTrain.getHull());
            modelUI.setVisible(true);
            try{GUIWindowLauncher.launchWindow(modelUI);} catch (Exception e) {}
        }
        else if (column == spawnControllerColumn) {
            //DisplaySE.spawnTrainControllerUI(trains.get(row));
            DriverUI controllerUI = new DriverUI();
            controllerUI.latch(thisTrain.getController());
            try{GUIWindowLauncher.launchWindow(controllerUI);} catch (Exception e) {}
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
            java.util.logging.Logger.getLogger(SimulationEnvironmentJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimulationEnvironmentJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimulationEnvironmentJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimulationEnvironmentJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SimulationEnvironmentJFrame se = new SimulationEnvironmentJFrame( new SimulationEnvironment());
//                while(true) {
//                    se.update();
//                }
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JTextField AbsolutePathToCSVField;
    private javax.swing.JTable CTCSpawnTable;
    private javax.swing.JPanel ClockControlPane;
    private javax.swing.JButton ClockPauseButton;
    private javax.swing.JSlider ClockRatioSlider;
    private javax.swing.JSlider ClockResolutionSlider;
    private javax.swing.JPanel GUISpawnPane;
    private javax.swing.JPanel ImportTrackPanel;
    private javax.swing.JPanel MainMenu;
    private javax.swing.JLabel PhysicsResolutionLabel;
    private javax.swing.JButton SpawnTrainButton;
    private javax.swing.JLabel TimeLabel;
    private javax.swing.JTable TrainUnitSpawnTable;
    private javax.swing.JTable WaysideSystemSpawnTable;
    private javax.swing.JLabel WorldClockSpeedLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration
}
