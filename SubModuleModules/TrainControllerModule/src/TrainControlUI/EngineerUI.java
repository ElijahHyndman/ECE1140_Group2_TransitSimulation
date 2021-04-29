package TrainControlUI;

import implementation.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** EngineerUI is an interface for allowing the ENGINEER to
 * manually enter the train's Kp and Ki values
 *
 * Author: Reagan Dowling
 */
public class EngineerUI extends javax.swing.JFrame implements ActionListener {

    private JFrame mainEng;
    private TrainMotor theMotor;
    private JTextField Kp;
    private JTextField Ki;

    public EngineerUI(TrainMotor motor){

        theMotor = motor;

        mainEng = new JFrame("Train Engineer");
        mainEng.setSize(600, 700);
        mainEng.setLayout(null);
        mainEng.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setUpEngWindow();

        mainEng.setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setUpEngWindow(){

        JLabel welcome = new JLabel("Welcome Train Engineer");
        welcome.setBounds(200, 50, 200, 25);
        mainEng.add(welcome);

        JLabel setPID = new JLabel("Please input the values for Kp and Ki below.");
        setPID.setBounds(162, 150, 275, 25);
        mainEng.add(setPID);

        JLabel setKp = new JLabel(" KP : ");
        setKp.setBounds(200, 250, 100, 25);
        mainEng.add(setKp);

        JLabel setKi = new JLabel(" KI : ");
        setKi.setBounds(200, 300, 100, 25);
        mainEng.add(setKi);

        Kp = new JTextField();
        Kp.setBounds(300, 250, 75, 25);
        Kp.setVisible(true);
        mainEng.add(Kp);

        Ki = new JTextField();
        Ki.setBounds(300, 300, 75, 25 );
        Ki.setVisible(true);
        mainEng.add(Ki);


        JButton setFields = new JButton("Set Kp and Ki");
        setFields.setBounds(200, 450, 200, 150 );
        setFields.addActionListener(this);
        setFields.setActionCommand("Set");
        mainEng.add(setFields);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        //Setting Kp and Ki, creates PID controller for the motor
        if (action.equals("Set")){
            theMotor.setKpKi(Double.parseDouble(Kp.getText()),Double.parseDouble(Ki.getText()) );
            Kp.setText("");
            Ki.setText("");
        }
    }
}
