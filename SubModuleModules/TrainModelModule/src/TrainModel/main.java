package TrainModel;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
/**
 *
 * @author Devon
 */
public class main {
    
    Thread t;
    
    
    public static void main(String[] args) {
        trainGUI gui = new trainGUI(0);
        gui.setVisible(true);
        gui.newTrain();

        gui.trains.get(0).setAuthority(10);
        while(true){
            //recalc every sleep(1000) and update display
            gui.trains.get(0).updatePhysicalState("00",1);
            gui.updateDisplay();

            //sleep
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

        }

    }
}
