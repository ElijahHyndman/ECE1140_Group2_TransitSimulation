package TrainControlUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;

import GUIInterface.AppGUIModule;
import implementation.*;
import java.util.Vector;


/** DriverUI is the central user interface allowing the DRIVER to
 * manually enter inputs, and view train model vital and non-vital
 * attributes.
 *
 * Author: Reagan Dowling
 * Date: 02/24/2021
 */
public class DriverUI implements ActionListener, AppGUIModule {

    private TrainControl control;
    //private TestingUI testing;

    public JFrame main;
    public JLabel speedVal;
    public JLabel comSpeedVal;
    public JButton setSpeed;
    public JTextField inputSpeed;
    public JButton setNewSpeed;
    public JButton cancelSpeed;
    public JLabel accelerationValue;
    public JLabel powerVal;
    public JLabel authVal;
    public JLabel limitVal;
    public JPopupMenu alertError;
    public JLabel beaconMessage;
    public JButton eBrake;
    public JButton sBrake;
    public JLabel time;
    public JButton switchMode;
    public JLabel currentMode;
    public JLabel message;
    public JLabel brake;
    public JButton reEnable;
    public JButton stopBraking;
    public Gauge gauge;
    public JPanel center;

    public DecimalFormat format;
    public Timer timer;
    public Graphics graphics;

    public DriverUI() {
        //The train control associated for this train/driver
        control = new TrainControl();

        main = new JFrame("Train X");
        main.setSize(1600, 900);
        main.setLayout(null);
        main.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        format = new DecimalFormat("#.##");

        setUpWindow();
        setFrames();
    }

    public void setFrames(){
        //The train control associated for this train/driver
        //control = trainController

        format = new DecimalFormat("#.##");

        //==Background panels for layout===
        JPanel panel = new JPanel();
        panel.setBounds(1157, 75, 400, 800);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        panel.setVisible(true);
        main.add(panel);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 1157, 75);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        main.add(topPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(0, 75, 300, 500);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        main.add(rightPanel);

        JPanel speedPanel = new JPanel();
        speedPanel.setBounds(385, 510, 325, 250);
        speedPanel.setBackground(Color.WHITE);
        speedPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        main.add(speedPanel);

        JPanel authPanel = new JPanel();
        authPanel.setBounds(735, 510, 300, 250);
        authPanel.setBackground(Color.WHITE);
        authPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        main.add(authPanel);

        center = new JPanel();
        center.setLayout(new GridLayout(1,2));

        gauge = new Gauge(0.0, 100.0, Gauge.SEMI_CIRCLE);
        gauge.setBackground(Color.WHITE);
        gauge.setForeground(Color.BLACK);
        gauge.setHighlight(Color.DARK_GRAY);
        gauge.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        center.add(gauge);
        center.setBounds(450, 100, 400, 300);

        main.add(center);

        main.setVisible(true);
    }

    public void latch(Object myObject){

        control = (TrainControl) myObject;
        //Create new testing UI
        //testing = new TestingUI(control);

        //Create an engineer UI for the train
        new EngineerUI(control.getTrainMotor());

    }

    public void update(){
        time.setText(java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
        speedVal.setText(String.valueOf(format.format(control.getActualSpeed()*2.23694)));
        comSpeedVal.setText(String.valueOf(format.format(control.getCommandedSpeed()*2.23694)));
        limitVal.setText(String.valueOf(format.format(control.getSpeedLimit()*2.23694)));
        beaconMessage.setText(control.getBeacon());
        if (control.getStoppingDistance() == -1){
            authVal.setText("");
        }else{
            authVal.setText(String.valueOf(format.format((control.getStoppingDistance()))));
        }
        //authVal.setText(String.valueOf(format.format((control.getStoppingDistance()))));
        brake.setText(String.valueOf((control.getSafeBreakingDistance())*3.281));
        message.setText(control.getSystemMessage());
        accelerationValue.setText(String.valueOf(format.format(control.getAcceleration()*.621371)));
        powerVal.setText(String.valueOf(format.format(control.getPower())));
    }

    @Override
    public Object getJFrame() {
        return this;
    }

    @Override
    public void setVis(boolean visible) {
        return;
    }

    public void setUpWindow() {

        //=====STATIC LABELS======

        JLabel mode = new JLabel("Control Mode : ");
        mode.setBounds(25, 10, 100, 25);
        main.add(mode);

        currentMode = new JLabel(control.getControlMode());
        currentMode.setBounds(125, 10, 100, 25);
        main.add(currentMode);

        message = new JLabel();
        message.setBounds(300, 20, 650, 25);
        message.setFont(new Font("Verdana", Font.BOLD, 16));
        message.setForeground(Color.RED);
        main.add(message);

        time = new JLabel(java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
        time.setBounds(1265, 20, 200, 25);
        time.setFont(new Font("Sans Serif", Font.BOLD, 20));
        main.add(time);

        JLabel accel = new JLabel("POWER");
        accel.setBounds(100, 85, 100, 100);
        accel.setFont(new Font("Sans Serif", Font.BOLD, 18));
        main.add(accel);

        JLabel accelUnits = new JLabel("kW");
        accelUnits.setBounds(175, 200, 50, 50);
        accelUnits.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        main.add(accelUnits);

        JLabel pow = new JLabel("ACCELERATION");
        pow.setBounds(65, 310, 150, 100);
        pow.setFont(new Font("Sans Serif", Font.BOLD, 18));
        main.add(pow);

        JLabel powerUnits = new JLabel("mph/s");
        powerUnits.setBounds(150, 400, 100, 100);
        powerUnits.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        main.add(powerUnits);

        JLabel beacon = new JLabel("Beacon");
        beacon.setBounds(1275, 100, 100, 100);
        beacon.setFont(new Font("Sans Serif", Font.BOLD, 16));
        main.add(beacon);

        JLabel nonVital = new JLabel("Non-Vital Components");
        nonVital.setBounds(1215, 375, 200, 100);
        nonVital.setFont(new Font("Sans Serif", Font.BOLD, 16));
        main.add(nonVital);

        JLabel speedList = new JLabel("SPEED");
        speedList.setBounds(410, 485, 100, 25);
        speedList.setFont(new Font("Sans Serif", Font.BOLD, 18));
        main.add(speedList);

        JLabel actSpeed = new JLabel("Actual Speed : ");
        actSpeed.setBounds(410, 535, 125, 25);
        actSpeed.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(actSpeed);

        JLabel speedUnits = new JLabel("mph");
        speedUnits.setBounds(635, 535, 50, 25);
        speedUnits.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(speedUnits);

        JLabel comSpeed = new JLabel("Commanded Speed : ");
        comSpeed.setBounds(410, 610, 150, 25);
        comSpeed.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(comSpeed);

        JLabel speedUnitsCopy = new JLabel("mph");
        speedUnitsCopy.setBounds(635, 610, 50, 25);
        speedUnitsCopy.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(speedUnitsCopy);

        JLabel limitSpeed = new JLabel("Speed Limit : ");
        limitSpeed.setBounds(410, 685, 100, 25);
        limitSpeed.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(limitSpeed);

        JLabel speedUnitCopy2 = new JLabel("mph");
        speedUnitCopy2.setBounds(635, 685, 50, 25);
        speedUnitCopy2.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(speedUnitCopy2);

        JLabel authList = new JLabel("AUTHORITY");
        authList.setBounds(760, 485, 125, 25);
        authList.setFont(new Font("Sans Serif", Font.BOLD, 18));
        main.add(authList);

        JLabel distance = new JLabel("Distance : ");
        distance.setBounds(760, 535, 100, 25);
        distance.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(distance);

        JLabel distUnits = new JLabel("ft");
        distUnits.setBounds(955, 535, 50, 25);
        distUnits.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(distUnits);


        //======BUTTONS=======

        sBrake = new JButton("Service Brake");
        sBrake.setBounds(945, 100, 175, 150);
        sBrake.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        sBrake.addActionListener(this);
        sBrake.setBackground(Color.LIGHT_GRAY);
        sBrake.setOpaque(true);
        sBrake.setActionCommand("service");
        main.add(sBrake);

        stopBraking = new JButton("Stop Braking");
        stopBraking.setBounds(945, 100, 175, 150);
        stopBraking.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        stopBraking.addActionListener(this);
        stopBraking.setBackground(Color.RED);
        stopBraking.setOpaque(true);
        stopBraking.setActionCommand("stop brake");
        stopBraking.setVisible(false);
        stopBraking.setEnabled(false);
        main.add(stopBraking);

        eBrake = new JButton("Emergency Brake");
        eBrake.setBounds(20, 625, 275, 175);
        eBrake.addActionListener(this);
        eBrake.setActionCommand("emergency");
        eBrake.setBackground(Color.RED);
        eBrake.setFont(new Font("Sans Serif", Font.BOLD, 22));
        eBrake.setForeground(Color.WHITE);
        eBrake.setContentAreaFilled(true);
        eBrake.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        eBrake.setOpaque(true);
        main.add(eBrake);

        reEnable = new JButton("Re-Enable");
        reEnable.setBounds(300, 775, 100, 25);
        reEnable.addActionListener(this);
        reEnable.setActionCommand("ReEnable");
        reEnable.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        reEnable.setEnabled(false);
        main.add(reEnable);

        setSpeed = new JButton("Set Speed");
        setSpeed.setBounds(945, 300, 175, 150);
        setSpeed.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        setSpeed.addActionListener(this);
        setSpeed.setActionCommand("Set Speed");
        setSpeed.setBackground(Color.LIGHT_GRAY);
        setSpeed.setOpaque(true);
        main.add(setSpeed);

        setNewSpeed = new JButton("Set");
        setNewSpeed.setBounds(950, 350, 50, 25);
        setNewSpeed.addActionListener(this);
        setNewSpeed.setActionCommand("New Speed");
        setNewSpeed.setVisible(false);
        main.add(setNewSpeed);

        cancelSpeed = new JButton("Cancel");
        cancelSpeed.setBounds(1030, 350, 100, 25);
        cancelSpeed.addActionListener(this);
        cancelSpeed.setActionCommand("Cancel Speed");
        cancelSpeed.setVisible(false);
        main.add(cancelSpeed);

        JButton nonVitalView = new JButton("View Non-Vital Components");
        nonVitalView.setBounds(1200, 500, 225, 75);
        nonVitalView.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVitalView.addActionListener(this);
        nonVitalView.setActionCommand("Non-Vital");
        nonVitalView.setBackground(Color.LIGHT_GRAY);
        nonVitalView.setOpaque(true);
        main.add(nonVitalView);

        switchMode = new JButton();
        switchMode.setBounds(15, 35, 200, 35);
        switchMode.addActionListener(this);
        switchMode.setActionCommand("mode");
        switchMode.setText("Switch to Manual Mode");
        main.add(switchMode);


        //======DYNAMIC VALUES======

        //ACCEL
        accelerationValue = new JLabel();
        accelerationValue.setText(String.valueOf(control.getAcceleration()));
        accelerationValue.setBounds(90, 400, 100, 100);
        accelerationValue.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        main.add(accelerationValue);

        //POWER
        powerVal = new JLabel();
        powerVal.setBounds(90, 200, 100, 50);
        powerVal.setText(String.valueOf(control.getPower()));
        powerVal.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        main.add(powerVal);

        //BEACON
        beaconMessage = new JLabel();
        beaconMessage.setText(control.getBeacon());
        beaconMessage.setBounds(1250, 200, 350, 100);
        beaconMessage.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        main.add(beaconMessage);

        //ACTUAL SPEED
        speedVal = new JLabel();
        speedVal.setText(String.valueOf(control.getActualSpeed()));
        speedVal.setBounds(585, 535, 50, 25);
        speedVal.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(speedVal);

        //COMMANDED SPEED
        comSpeedVal = new JLabel();
        comSpeedVal.setText(String.valueOf(control.getCommandedSpeed()));
        comSpeedVal.setBounds(585, 610, 50, 25);
        comSpeedVal.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(comSpeedVal);

        //SPEED LIMIT
        limitVal = new JLabel();
        limitVal.setText(String.valueOf(control.getSpeedLimit()));
        limitVal.setBounds(585, 685, 50, 25);
        limitVal.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(limitVal);

        //AUTHORITY DISTANCE
        authVal = new JLabel();
        authVal.setText(String.valueOf(format.format((control.getAuthority())*3.281)));
        authVal.setBounds(890, 535, 100, 25);
        authVal.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        main.add(authVal);

        brake = new JLabel();
        brake.setText(String.valueOf(format.format((control.getSafeBreakingDistance()*3.281))));
        brake.setBounds(890, 635, 100, 25);
        brake.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        //main.add(brake);

        //Input SPEED
        inputSpeed = new JTextField();
        inputSpeed.setBounds(950, 300, 175, 50);
        inputSpeed.setVisible(false);
        main.add(inputSpeed);

        //===POPUPS====
        alertError = new JPopupMenu();
        alertError.setLabel("ERROR");
        alertError.setBounds(400, 200, 600, 600);
        alertError.setToolTipText("TESTING");
        alertError.setVisible(false);
        main.add(alertError);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch (action) {
            case "service" -> {
                control.useServiceBrake(true);
                sBrake.setEnabled(false);
                sBrake.setVisible(false);
                stopBraking.setEnabled(true);
                stopBraking.setVisible(true);
            }
            case "stop brake" -> {
                control.useServiceBrake(false);
                stopBraking.setEnabled(false);
                stopBraking.setVisible(false);
                sBrake.setEnabled(true);
                sBrake.setVisible(true);
            }
            case "Set Speed" -> {
                setSpeed.setVisible(false);
                inputSpeed.setVisible(true);
                setNewSpeed.setVisible(true);
                cancelSpeed.setVisible(true);
            }
            case "emergency" -> {
                control.useEmergencyBrake(true);
                eBrake.setEnabled(false);
                reEnable.setEnabled(true);
            }
            case "New Speed" -> {
                String result;
                try {
                    result = control.inputSpeed(Double.parseDouble(inputSpeed.getText()));
                } catch (NumberFormatException notInt) {
                    JOptionPane.showMessageDialog(main, "ERROR\r\nPlease Enter A Number");
                    break;
                }
                if (result.equals("Success")) {
                    JOptionPane.showMessageDialog(main, "Speed Input Successful");
                    inputSpeed.setVisible(false);
                    setNewSpeed.setVisible(false);
                    cancelSpeed.setVisible(false);
                    setSpeed.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(main, "ERROR \r\nSpeed Input Unsuccessful\r\n" + result + ", Try Again");
                }
            }
            case "Cancel Speed" -> {
                inputSpeed.setVisible(false);
                setNewSpeed.setVisible(false);
                cancelSpeed.setVisible(false);
                setSpeed.setVisible(true);
            }
            case "Non-Vital" ->
                    //Create new non vital comp. Ui using the trains non vital components
                    new NonVitalUI(control.getNonVitalComponents());

            case "mode" -> {
                control.switchMode();
                if (control.getControlMode().equals("Automatic")){
                    switchMode.setText("Switch to Manual Mode");
                    currentMode.setText("Automatic");
                }else {
                    switchMode.setText("Switch to Automatic Mode");
                    currentMode.setText("Manual");
                }
            }
            case "ReEnable" -> {
                eBrake.setEnabled(true);
                reEnable.setEnabled(false);
                control.useEmergencyBrake(false);
            }
        }
    }

    
    public static void main(String args[]){
       // new DriverUI();
    }

}

