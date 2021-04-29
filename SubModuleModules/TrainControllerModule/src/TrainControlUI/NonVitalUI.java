package TrainControlUI;

import implementation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** NonVitalUI is created when the "View Non-Vital Components"
 * button is clicked. The created window displays the status of
 * all of the current train's non vital components
 *
 * Author: Reagan Dowling
 */

public class NonVitalUI extends javax.swing.JFrame implements ActionListener {
    private JFrame nonVital;
    private NonVitalComponents nonVitalComp;

    private JLabel stationNumber;
    private JLabel doorsRight;
    private JLabel doorsLeft;
    private JLabel temp;
    private JLabel inLights;
    private JLabel outLights;
    private JLabel announcementProgress;
    private JLabel advertisementProgress;
    private JButton returnHome;
    private JLabel headLights;

    public NonVitalUI(NonVitalComponents trainComponents){

        nonVitalComp = trainComponents;

        nonVital = new JFrame("Non-Vital Components");
        nonVital.setSize(850, 650);
        nonVital.setLayout(null);
        nonVital.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setUpWindow();

        JPanel first = new JPanel();
        first.setBounds(150, 40, 560, 50);
        first.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        nonVital.add(first);

        JPanel second = new JPanel();
        second.setBounds(150, 90, 560, 50);
        second.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        second.setBackground(Color.white);
        nonVital.add(second);

        JPanel third = new JPanel();
        third.setBounds(150, 140, 560, 50);
        third.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        nonVital.add(third);

        JPanel fourth = new JPanel();
        fourth.setBounds(150, 190, 560, 50);
        fourth.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        fourth.setBackground(Color.white);
        nonVital.add(fourth);

        JPanel fifth = new JPanel();
        fifth.setBounds(150, 240, 560, 50);
        fifth.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        nonVital.add(fifth);

        JPanel sixth = new JPanel();
        sixth.setBounds(150, 290, 560, 50);
        sixth.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        sixth.setBackground(Color.white);
        nonVital.add(sixth);

        JPanel seventh = new JPanel();
        seventh.setBounds(150, 340, 560, 50);
        seventh.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        nonVital.add(seventh);

        JPanel eighth = new JPanel();
        eighth.setBounds(150, 390, 560, 50);
        eighth.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        eighth.setBackground(Color.white);
        nonVital.add(eighth);

        JPanel ninth = new JPanel();
        ninth.setBounds(150, 440, 560, 50);
        ninth.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
        nonVital.add(ninth);


        nonVital.setVisible(true);

      /*  while (nonVital.isVisible()){

            stationNumber.setText(nonVitalComp.getNextStation());
            doorsRight.setText(String.valueOf(nonVitalComp.getRightDoors()));
            doorsLeft.setText(String.valueOf(nonVitalComp.getLeftDoors()));
            temp.setText(String.valueOf(nonVitalComp.getTemperature()));
            inLights.setText(String.valueOf(nonVitalComp.getCabinLights()));
            outLights.setText(String.valueOf(nonVitalComp.getExternalLights()));
            announcementProgress.setText(String.valueOf(nonVitalComp.announcementInProgress()));
            advertisementProgress.setText(String.valueOf(nonVitalComp.getCurrentAdvertisement()));

        }

       */

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setUpWindow(){
        JLabel nextStation = new JLabel("Next Station : ");
        nextStation.setBounds(225, 50, 200, 25);
        nextStation.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(nextStation);

        JLabel rightDoors = new JLabel("Right Doors : ");
        rightDoors.setBounds(225,100,200,25);
        rightDoors.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(rightDoors);

        JLabel leftDoors = new JLabel("Left Doors : ");
        leftDoors.setBounds(225, 150, 200, 25);
        leftDoors.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(leftDoors);

        JLabel cabinTemp = new JLabel("Cabin Temperature (F) : ");
        cabinTemp.setBounds(225, 200, 200, 25);
        cabinTemp.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(cabinTemp);

        JLabel cabinLights = new JLabel("Cabin Lights: ");
        cabinLights.setBounds(225, 250, 200, 25);
        cabinLights.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(cabinLights);

        JLabel externalLights = new JLabel("External Lights : ");
        externalLights.setBounds(225, 300, 200, 25);
        externalLights.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(externalLights);

        JLabel head = new JLabel("Head Lights : ");
        head.setBounds(225, 350, 200, 25);
        head.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(head);

        JLabel announcement = new JLabel("Announcement : ");
        announcement.setBounds(225, 400, 200, 25);
        announcement.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(announcement);

        JLabel advertisement = new JLabel("Advertisement: ");
        advertisement.setBounds(225, 450, 200, 25);
        advertisement.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        nonVital.add(advertisement);

        stationNumber = new JLabel(nonVitalComp.getNextStation());
        stationNumber.setBounds(500, 50, 200, 25);
        stationNumber.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(stationNumber);

        doorsRight = new JLabel();
        if (nonVitalComp.getRightDoors()){
            doorsRight.setText("Open");
        }else{
            doorsRight.setText("Closed");
        }
        doorsRight.setBounds(500, 100, 200, 25);
        doorsRight.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(doorsRight);

        doorsLeft = new JLabel();
        if (nonVitalComp.getLeftDoors()){
            doorsLeft.setText("Open");
        }else{
            doorsLeft.setText("Closed");
        }
        doorsLeft.setBounds(500, 150, 200, 25);
        doorsLeft.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(doorsLeft);

        temp = new JLabel(String.valueOf(nonVitalComp.getTemperature()));
        temp.setBounds(500, 200, 200, 25);
        temp.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(temp);

        inLights = new JLabel();
        if (nonVitalComp.getCabinLights()){
            inLights.setText("ON");
        }else{
            inLights.setText("OFF");
        }
        inLights.setBounds(500, 250, 200, 25);
        inLights.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(inLights);

        outLights = new JLabel();
        if (nonVitalComp.getExternalLights()){
            outLights.setText("ON");
        }else{
            outLights.setText("OFF");
        }
        outLights.setBounds(500, 300, 200, 25);
        outLights.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(outLights);

        headLights = new JLabel();
        if (nonVitalComp.getHeadLights()){
            headLights.setText("ON");
        }else{
            headLights.setText("OFF");
        }
        headLights.setBounds(500, 350, 200, 25);
        headLights.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(headLights);

        String announce = null;
        announcementProgress = new JLabel();
        if (!nonVitalComp.announcementInProgress()){
            announce = "Not in progress";
        }else{
            announce = "In progress";
        }
        announcementProgress.setText(announce);
        announcementProgress.setBounds(500, 400, 200, 25);
        announcementProgress.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(announcementProgress);

        String ad;
        int theAd = nonVitalComp.getCurrentAdvertisement();
        advertisementProgress = new JLabel();
        if (theAd == 0){
            ad = "No advertisement in progress";
        }else {
            ad = ("Advertisement #" + theAd + " in progress");
        }
        advertisementProgress.setText(ad);
        advertisementProgress.setBounds(500, 450, 250, 25);
        advertisementProgress.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        nonVital.add(advertisementProgress);

        returnHome = new JButton("Return to Main");
        returnHome.setBounds(315, 500, 200, 100);
        returnHome.addActionListener(this);
        returnHome.setActionCommand("return");
        returnHome.setBackground(Color.darkGray);
        returnHome.setOpaque(true);
        returnHome.setFont(new Font("Sans Serif", Font.BOLD, 18));
        nonVital.add(returnHome);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("return")){
            nonVital.dispose();
        }

    }
}
