package TrainControlUI;

import implementation.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** TestingUI is a user interface meant to simulate the Train Control
 * and Train Model interaction. It used for testing the Driver UI.
 * It displays the output results of changing driver inputs, and
 * provides forced necessary train model inputs.
 *
 * Author: Reagan Dowling
 * Date: 02/24/2021
 */

public class TestingUI implements ActionListener{

    private TrainControl currentControl;
    private String beaconTest;
    private double comSpeedTest;
    private double speedLimitTest;
    private double actualSpeedTest;
    private double authorityTest;
    private JFrame mainTest;
    private JButton setActualSpeed;
    private JButton setCommandedSpeed;
    private JButton setSpeedLimit;
    private JButton setBeacon;
    private JButton setAuthority;
    private JTextField inputAuthority;
    private JTextField inputActualSpeed;
    private JTextField inputCommandedSpeed;
    private JTextField inputSpeedLimit;
    private JTextField inputBeacon;


    public TestingUI(TrainControl control){
        currentControl = control;

        actualSpeedTest=0;
        beaconTest=null;
        comSpeedTest=0;
        speedLimitTest=0;
        authorityTest = 0;

        mainTest = new JFrame("Testing UI");
        mainTest.setSize(600, 400);
        mainTest.setLayout(null);
        mainTest.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUpTestWindow();

        mainTest.setVisible(true);


    }

    public void setTrainInputs(){
        currentControl.newTrainInput(new TrainModelInput(actualSpeedTest, comSpeedTest, speedLimitTest, authorityTest, beaconTest));
    }

    public void setActualVelocity(double inputVelocity){
        actualSpeedTest = inputVelocity;
    }
    public void newBeacon(String beacon){
        beaconTest = beacon;
    }
    public void newCommandedSpeed(double comSpeed){
        comSpeedTest = comSpeed;
    }
    public void newSpeedLimit(double speedLimit){
        speedLimitTest = speedLimit;
    }
    public void newAuthority(double authority){ authorityTest = 0;}

    public void setUpTestWindow(){

        JLabel actualSpeed = new JLabel("Input Actual Speed (km/h) : ");
        actualSpeed.setBounds(25, 25, 200, 25);
        mainTest.add(actualSpeed);

        JLabel commandedSpeed = new JLabel("Input Commanded Speed (km/h) : ");
        commandedSpeed.setBounds(25,75,250,25);
        mainTest.add(commandedSpeed);

        JLabel speedLimit = new JLabel("Input Speed Limit (km/h) : ");
        speedLimit.setBounds(25, 125, 200, 25);
        mainTest.add(speedLimit);

        JLabel authority = new JLabel("Input Authority (Blocks) : ");
        authority.setBounds(25, 175, 200, 25);
        mainTest.add(authority);

        JLabel Beacon = new JLabel("Set new beacon : ");
        Beacon.setBounds(25, 225, 200, 25);
        mainTest.add(Beacon);

        inputActualSpeed = new JTextField();
        inputActualSpeed.setBounds(250, 25, 50, 25);
        inputActualSpeed.setVisible(true);
        mainTest.add(inputActualSpeed);

        inputCommandedSpeed = new JTextField();
        inputCommandedSpeed.setBounds(250, 75, 50, 25);
        inputCommandedSpeed.setVisible(true);
        mainTest.add(inputCommandedSpeed);

        inputSpeedLimit = new JTextField();
        inputSpeedLimit.setBounds(250, 125, 50, 25);
        inputSpeedLimit.setVisible(true);
        mainTest.add(inputSpeedLimit);

        inputAuthority = new JTextField();
        inputAuthority.setBounds(250, 175, 50, 25);
        inputAuthority.setVisible(true);
        mainTest.add(inputAuthority);

        inputBeacon = new JTextField();
        inputBeacon.setBounds(250, 225, 150, 25);
        inputBeacon.setVisible(true);
        mainTest.add(inputBeacon);

        setActualSpeed = new JButton("Set");
        setActualSpeed.setBounds(400, 25, 50, 25);
        setActualSpeed.addActionListener(this);
        setActualSpeed.setActionCommand("Actual");
        mainTest.add(setActualSpeed);

        setCommandedSpeed = new JButton("Set");
        setCommandedSpeed.setBounds(400, 75, 50, 25);
        setCommandedSpeed.addActionListener(this);
        setCommandedSpeed.setActionCommand("Commanded");
        mainTest.add(setCommandedSpeed);

        setSpeedLimit = new JButton("Set");
        setSpeedLimit.setBounds(400, 125, 50, 25);
        setSpeedLimit.addActionListener(this);
        setSpeedLimit.setActionCommand("Limit");
        mainTest.add(setSpeedLimit);

        setAuthority = new JButton("Set");
        setAuthority.setBounds(400,175, 50, 25);
        setAuthority.addActionListener(this);
        setAuthority.setActionCommand("authority");
        mainTest.add(setAuthority);

        setBeacon = new JButton("Set");
        setBeacon.setBounds(400, 225, 50, 25);
        setBeacon.addActionListener(this);
        setBeacon.setActionCommand("Beacon");
        mainTest.add(setBeacon);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch(action){
            case "Actual" -> {
                setActualVelocity(Double.parseDouble(inputActualSpeed.getText()));
                inputActualSpeed.setText("");
            }
            case "Commanded" -> {
                newCommandedSpeed(Double.parseDouble(inputCommandedSpeed.getText()));
                inputCommandedSpeed.setText("");
            }
            case "Limit" -> {
                newSpeedLimit(Double.parseDouble(inputSpeedLimit.getText()));
                inputSpeedLimit.setText("");
            }
            case "Beacon" -> {
                newBeacon(inputBeacon.getText());
                inputBeacon.setText("");
            }
            case "authority" -> {
                newAuthority(Double.parseDouble(inputAuthority.getText()));
                currentControl.setAuthority(Integer.parseInt(inputAuthority.getText()));
                inputAuthority.setText("");
            }
        }

        }
}
