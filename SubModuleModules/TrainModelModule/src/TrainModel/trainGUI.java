/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainModel;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
/**
 *
 * @author Devon
 */
public class trainGUI extends javax.swing.JFrame {
       
    //set of Trains across the systemz
    public ArrayList<Train> trains = new ArrayList<Train>();
    int namesIndex = 2;
    int trainIndex = 0;
    public int mainTrainIndex;
    
    /**
     * Creates new form trainGUI
     */
    
    public ArrayList<Train> getTrains(){
        return trains;
    }
    
    public trainGUI(int index) {
        initComponents();
        this.mainTrainIndex = index;
        numTrains.setText(Integer.toString(namesIndex-1));
        
    }
    public void newTrain() {
        Train t1 = new Train(5, 2);
        trains.add(t1);
    }
    public void updateDisplay(){
        
        table1.setValueAt(trains.get(mainTrainIndex).displayAcceleration, 0, 1);
        table1.setValueAt(trains.get(mainTrainIndex).displayActualSpeed, 1, 1);
        table1.setValueAt(trains.get(mainTrainIndex).displayCommandedSpeed, 2, 1);
        table1.setValueAt(trains.get(mainTrainIndex).power, 3, 1);
        table1.setValueAt(trains.get(mainTrainIndex).authority, 4, 1);
        table1.setValueAt(trains.get(mainTrainIndex).mass, 5, 1);
        
        table2.setValueAt(trains.get(mainTrainIndex).serviceBrake, 0, 1);
        table2.setValueAt(trains.get(mainTrainIndex).emergencyBrake, 1, 1);
        table2.setValueAt(trains.get(mainTrainIndex).passengerBrake, 2, 1);
        
        table4.setValueAt(trains.get(mainTrainIndex).signalPickupFail, 0, 1);
        table4.setValueAt(trains.get(mainTrainIndex).brakeFail, 1, 1);
        table4.setValueAt(trains.get(mainTrainIndex).engineFail, 2, 1);
        
        additionalTable.setValueAt(trains.get(mainTrainIndex).advertisements, 0, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).announcements, 1, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).beacon, 2, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).crewCount, 3, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).leftDoors, 4, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).rightDoors, 5, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).headlights, 6, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).cabinLights, 7, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).outerLights, 8, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).nextStop, 9, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).passengerCount, 10, 1);
        additionalTable.setValueAt(trains.get(mainTrainIndex).cabinTemp, 11, 1);
        
        updateTestDisplay();
           
    }
    
    public void updateTestDisplay(){
        
        testTable.setValueAt(trains.get(trainIndex).displayAcceleration, 0, 1);
        testTable.setValueAt(trains.get(trainIndex).displayActualSpeed, 1, 1);
        testTable.setValueAt(trains.get(trainIndex).authority, 2, 1);
        testTable.setValueAt(trains.get(trainIndex).beacon, 3, 1);
        testTable.setValueAt(trains.get(trainIndex).displayCommandedSpeed, 4, 1);
        testTable.setValueAt(trains.get(trainIndex).leftDoors, 5, 1);
        testTable.setValueAt(trains.get(trainIndex).rightDoors, 6, 1);
        testTable.setValueAt(trains.get(trainIndex).emergencyBrake, 7, 1);
        testTable.setValueAt(trains.get(trainIndex).headlights, 8, 1);
        testTable.setValueAt(trains.get(trainIndex).cabinLights, 9, 1);
        testTable.setValueAt(trains.get(trainIndex).outerLights, 10, 1);
        testTable.setValueAt(trains.get(trainIndex).nextStop, 11, 1);
        testTable.setValueAt(trains.get(trainIndex).serviceBrake, 12, 1);
        testTable.setValueAt(trains.get(trainIndex).passengerCount, 13, 1);
        testTable.setValueAt(trains.get(trainIndex).power, 14, 1);
        testTable.setValueAt(trains.get(trainIndex).cabinTemp, 15, 1);
        testTable.setValueAt(trains.get(trainIndex).passengerBrake, 16, 1);
        testTable.setValueAt(trains.get(trainIndex).mass, 17, 1);
        
        testFailure.setValueAt(trains.get(trainIndex).signalPickupFail, 0, 1);
        testFailure.setValueAt(trains.get(trainIndex).brakeFail, 1, 1);
        testFailure.setValueAt(trains.get(trainIndex).engineFail, 2, 1);
        
        try {
            BufferedImage bufImg=ImageIO.read(new File("./src/images/image.png"));
            imageLabel.setIcon(new ImageIcon(bufImg));
            //jlabel.repaint();
            //works even without repaint
        }
        catch (IOException ex) {
            System.out.println("Unable to read image file");
        }   
    }
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        mainView = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        table4 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        additionalView = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        additionalTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        trainTable = new javax.swing.JTable();
        testView = new javax.swing.JPanel();
        tablePane = new javax.swing.JScrollPane();
        testTable = new javax.swing.JTable();
        selectTrain = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        addTrain = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        numTrains = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        testFailure = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        menu = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        mainButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        additionalButton = new javax.swing.JButton();
        testButton = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));

        mainPanel.setLayout(new java.awt.CardLayout());

        table1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Acceleration", null, "ft/s^2", "Train Model"},
                {"Actual Speed", null, "ft/s", "Train Model"},
                {"Commanded Speed", null, "ft/s", "Track Model"},
                {"Power", null, "HP", "Train Controller"},
                {"Authority", null, "blocks", "Track Model"},
                {"Mass", null, "kg", "Train Model"}
            },
            new String [] {
                "Information Type", "Data", "Units", "Source"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1.setRowHeight(25);
        jScrollPane2.setViewportView(table1);

        table2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Service", null, "Train Controller"},
                {"Emergency", null, "Train Controller"},
                {"Passenger Emergency", null, "Passenger"}
            },
            new String [] {
                "Type", "Data", "Source"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table2.setRowHeight(25);
        jScrollPane3.setViewportView(table2);

        table4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Signal Pickup", null},
                {"Brake", null},
                {"Engine ", null}
            },
            new String [] {
                "Failure Type", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table4.setRowHeight(30);
        jScrollPane5.setViewportView(table4);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Movement Information");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Brakes");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Failures");

        javax.swing.GroupLayout mainViewLayout = new javax.swing.GroupLayout(mainView);
        mainView.setLayout(mainViewLayout);
        mainViewLayout.setHorizontalGroup(
            mainViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainViewLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(mainViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        mainViewLayout.setVerticalGroup(
            mainViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainViewLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        mainPanel.add(mainView, "TrainModel.main");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Additional Information");

        additionalTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        additionalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Advertisements", null, "Train Controller"},
                {"Announcements", null, "Train Controller"},
                {"Beacon", null, "Train Controller"},
                {"Crew Count", null, "Train Model"},
                {"Doors (Left)", null, "Train Controller"},
                {"Doors (Right)", null, "Train Controller"},
                {"Headlights", null, "Train Controller"},
                {"Lights (Cabin)", null, "Train Controller"},
                {"Lights (Outer)", null, "Train Controller"},
                {"Next Stop", null, "Train Controller"},
                {"Passenger Count", null, "Train Model"},
                {"Cabin Temperature (°F)", null, "Train Controller"}
            },
            new String [] {
                "Information", "Data", "Source"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        additionalTable.setRowHeight(25);
        jScrollPane6.setViewportView(additionalTable);
        if (additionalTable.getColumnModel().getColumnCount() > 0) {
            additionalTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Train Model Information");

        trainTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        trainTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Length", "105.6 ft", "Train Model"},
                {"Width", "8.71 ft", "Train Model"},
                {"Height", "11.22 ft", "Train Model"}
            },
            new String [] {
                "Information", "Data", "Source"
            }
        ));
        trainTable.setRowHeight(25);
        jScrollPane7.setViewportView(trainTable);

        javax.swing.GroupLayout additionalViewLayout = new javax.swing.GroupLayout(additionalView);
        additionalView.setLayout(additionalViewLayout);
        additionalViewLayout.setHorizontalGroup(
            additionalViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, additionalViewLayout.createSequentialGroup()
                .addContainerGap(196, Short.MAX_VALUE)
                .addGroup(additionalViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(95, 95, 95))
        );
        additionalViewLayout.setVerticalGroup(
            additionalViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(additionalViewLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        mainPanel.add(additionalView, "additional");

        testTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        testTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Acceleration", null, "ft/s^2", null},
                {"Actual Speed", null, "ft/s", null},
                {"Authority", null, "blocks", null},
                {"Beacon", null, "-", null},
                {"Commanded Speed", null, "ft/s", null},
                {"Doors (Left)", null, "-", null},
                {"Doors (Right)", null, "-", null},
                {"Emergency Brake", null, "-", null},
                {"Headlights", null, "-", null},
                {"Lights (Cabin)", null, "-", null},
                {"Lights (Outer)", null, "-", null},
                {"Next Stop", null, "-", null},
                {"Service Brake", null, "-", null},
                {"Passenger Count", null, "-", null},
                {"Power", null, "kW", null},
                {"Temp (Cabin)", null, "°F", null},
                {"Passenger Brake", null, "-", null},
                {"Mass", null, "kg", null}
            },
            new String [] {
                "Information", "Data", "Units", "New Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        testTable.setRowHeight(22);
        tablePane.setViewportView(testTable);
        if (testTable.getColumnModel().getColumnCount() > 0) {
            testTable.getColumnModel().getColumn(0).setMinWidth(135);
            testTable.getColumnModel().getColumn(0).setMaxWidth(135);
            testTable.getColumnModel().getColumn(1).setResizable(false);
        }

        selectTrain.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Train 1" }));
        selectTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectTrainActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Select Train");

        addTrain.setText("Add New Train");
        addTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTrainActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Trains in Operation = ");

        jButton1.setText("Enter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        numTrains.setBackground(new java.awt.Color(250, 250, 250));
        numTrains.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        testFailure.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        testFailure.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Signal Pickup", null, null},
                {"Brake", null, null},
                {"Engine", null, null}
            },
            new String [] {
                "Failure Type", "Data", "New Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        testFailure.setRowHeight(20);
        jScrollPane8.setViewportView(testFailure);

        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("image.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout testViewLayout = new javax.swing.GroupLayout(testView);
        testView.setLayout(testViewLayout);
        testViewLayout.setHorizontalGroup(
            testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(testViewLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jLabel8)
                .addGap(26, 26, 26)
                .addComponent(selectTrain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(testViewLayout.createSequentialGroup()
                .addGroup(testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(testViewLayout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(testViewLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tablePane, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)))
                .addGroup(testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(testViewLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(numTrains, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(addTrain))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        testViewLayout.setVerticalGroup(
            testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(testViewLayout.createSequentialGroup()
                .addGroup(testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, testViewLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, testViewLayout.createSequentialGroup()
                        .addContainerGap(24, Short.MAX_VALUE)
                        .addGroup(testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(selectTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9)
                                .addComponent(jLabel8))
                            .addComponent(numTrains, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(testViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(testViewLayout.createSequentialGroup()
                        .addComponent(tablePane, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainPanel.add(testView, "card4");

        menu.setBackground(new java.awt.Color(204, 204, 204));

        jPanel2.setBackground(new java.awt.Color(220, 220, 220));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Main Information");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainButton.setBackground(new java.awt.Color(220, 220, 220));
        mainButton.setText("Display");
        mainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainButtonActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(220, 220, 220));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Additional Info View");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        additionalButton.setBackground(new java.awt.Color(220, 220, 220));
        additionalButton.setText("Display");
        additionalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                additionalButtonActionPerformed(evt);
            }
        });

        testButton.setBackground(new java.awt.Color(220, 220, 220));
        testButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        testButton.setText("Test");
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(testButton))
                    .addGroup(menuLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(menuLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(additionalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mainButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainButton)
                .addGap(47, 47, 47)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(additionalButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 320, Short.MAX_VALUE)
                .addComponent(testButton)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed
        mainPanel.removeAll();
        mainPanel.add(testView);
        mainPanel.repaint();
        mainPanel.revalidate();
        mainButton.setText("Display");
        additionalButton.setText("Display");
        testButton.setText("(Displaying)");
    }//GEN-LAST:event_testButtonActionPerformed

    private void additionalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_additionalButtonActionPerformed
        mainPanel.removeAll();
        mainPanel.add(additionalView);
        mainPanel.repaint();
        mainPanel.revalidate();
        mainButton.setText("Display");
        testButton.setText("Test");
        additionalButton.setText("(Displaying)");
    }//GEN-LAST:event_additionalButtonActionPerformed

    private void mainButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainButtonActionPerformed
        mainPanel.removeAll();
        mainPanel.add(mainView);
        mainPanel.repaint();
        mainPanel.revalidate();
        additionalButton.setText("Display");
        testButton.setText("Test");
        mainButton.setText("(Displaying)");
        
    }//GEN-LAST:event_mainButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(testTable.getValueAt(0,3) != null && testTable.getValueAt(0,3) != ""){
            trains.get(trainIndex).setDisplayAccel(Double.parseDouble(testTable.getValueAt(0,3).toString()));
        }
        if(testTable.getValueAt(1,3) != null && testTable.getValueAt(1,3) != ""){
            trains.get(trainIndex).setDisplaySpeed(Double.parseDouble(testTable.getValueAt(1,3).toString()));
        }
        if(testTable.getValueAt(2,3) != null && testTable.getValueAt(2,3) != ""){
            trains.get(trainIndex).setAuthority(Integer.parseInt(testTable.getValueAt(2,3).toString()));
        }
        if(testTable.getValueAt(3,3) != null && testTable.getValueAt(3,3) != ""){
            trains.get(trainIndex).setBeacon(Integer.parseInt(testTable.getValueAt(3,3).toString()));
        }
        if(testTable.getValueAt(4,3) != null && testTable.getValueAt(4,3) != ""){
            trains.get(trainIndex).setDisplayCommandedSpeed(Double.parseDouble(testTable.getValueAt(4,3).toString()));
        }
        if(testTable.getValueAt(5,3) != null && testTable.getValueAt(5,3) != ""){
            trains.get(trainIndex).setLeftDoors(Boolean.valueOf((testTable.getValueAt(5,3).toString())));
        }
        if(testTable.getValueAt(6,3) != null && testTable.getValueAt(6,3) != ""){
            trains.get(trainIndex).setRightDoors(Boolean.valueOf((testTable.getValueAt(6,3).toString())));
        }
        if(testTable.getValueAt(7,3) != null && testTable.getValueAt(7,3) != ""){
            trains.get(trainIndex).setEmergencyBrake(Boolean.valueOf((testTable.getValueAt(7,3).toString())));
        }
        if(testTable.getValueAt(8,3) != null && testTable.getValueAt(8,3) != ""){
            trains.get(trainIndex).setHeadlights(Boolean.valueOf((testTable.getValueAt(8,3).toString())));
        }
        if(testTable.getValueAt(9,3) != null && testTable.getValueAt(9,3) != ""){
            trains.get(trainIndex).setCabinLights(Boolean.valueOf((testTable.getValueAt(9,3).toString())));
        }
        if(testTable.getValueAt(10,3) != null && testTable.getValueAt(10,3) != ""){
            trains.get(trainIndex).setOuterLights(Boolean.valueOf((testTable.getValueAt(10,3).toString())));
        }
        if(testTable.getValueAt(11,3) != null && testTable.getValueAt(11,3) != ""){
            trains.get(trainIndex).setNextStop(((testTable.getValueAt(11,3).toString())));
        }
        if(testTable.getValueAt(12,3) != null && testTable.getValueAt(12,3) != ""){
            trains.get(trainIndex).setServiceBrake(Boolean.valueOf((testTable.getValueAt(12,3).toString())));
        }
        if(testTable.getValueAt(13,3) != null && testTable.getValueAt(13,3) != ""){
            trains.get(trainIndex).setPassengerCount(Integer.parseInt(testTable.getValueAt(13,3).toString()));
        }
        if(testTable.getValueAt(14,3) != null && testTable.getValueAt(14,3) != ""){
            trains.get(trainIndex).setPower(Double.parseDouble(testTable.getValueAt(14,3).toString()));
        }
        if(testTable.getValueAt(15,3) != null && testTable.getValueAt(15,3) != ""){
            trains.get(trainIndex).setCabinTemp(Integer.parseInt(testTable.getValueAt(15,3).toString()));
        }
        if(testTable.getValueAt(16,3) != null && testTable.getValueAt(16,3) != ""){
            trains.get(trainIndex).setPassengerBrake(Boolean.valueOf((testTable.getValueAt(16,3).toString())));
        }
        if(testTable.getValueAt(17,3) != null && testTable.getValueAt(17,3) != ""){
            trains.get(trainIndex).setMass(Double.parseDouble(testTable.getValueAt(17,3).toString()));
        }

        //failure types
        if(testFailure.getValueAt(0,2) != null){
            trains.get(trainIndex).setSignalFail(Boolean.valueOf((testFailure.getValueAt(0,2).toString())));
        }if(testFailure.getValueAt(1,2) != null){
            trains.get(trainIndex).setBrakeFail(Boolean.valueOf((testFailure.getValueAt(1,2).toString())));
        }if(testFailure.getValueAt(2,2) != null){
            trains.get(trainIndex).setEngineFail(Boolean.valueOf((testFailure.getValueAt(2,2).toString())));
        }

        for(int i = 0; i<18; i++){
            testTable.setValueAt(null, i, 3);
        }
        for(int i = 0; i<3; i++){
            testFailure.setValueAt(null, i, 2);
        }
        updateTestDisplay();
        updateDisplay();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTrainActionPerformed
        Train t2 = new Train(5, 2);
        trains.add(t2);
        selectTrain.addItem("Train "+Integer.toString(namesIndex));
        namesIndex++;
        numTrains.setText(Integer.toString(namesIndex-1));
    }//GEN-LAST:event_addTrainActionPerformed

    private void selectTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectTrainActionPerformed
        trainIndex = selectTrain.getSelectedIndex();
        System.out.println(trainIndex);

        updateTestDisplay();
    }//GEN-LAST:event_selectTrainActionPerformed

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
            java.util.logging.Logger.getLogger(trainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(trainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(trainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(trainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new trainGUI(0).setVisible(true);
            }
        });
        /*      */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTrain;
    private javax.swing.JButton additionalButton;
    private javax.swing.JTable additionalTable;
    private javax.swing.JPanel additionalView;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton mainButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mainView;
    private javax.swing.JPanel menu;
    private javax.swing.JLabel numTrains;
    private javax.swing.JComboBox<String> selectTrain;
    private javax.swing.JTable table1;
    private javax.swing.JTable table2;
    private javax.swing.JTable table4;
    private javax.swing.JScrollPane tablePane;
    private javax.swing.JButton testButton;
    private javax.swing.JTable testFailure;
    private javax.swing.JTable testTable;
    private javax.swing.JPanel testView;
    private javax.swing.JTable trainTable;
    // End of variables declaration//GEN-END:variables
}
