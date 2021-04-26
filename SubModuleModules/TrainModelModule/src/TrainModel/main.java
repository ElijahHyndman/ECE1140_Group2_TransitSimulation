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
        int distance = 0;
        trainGUI gui = new trainGUI(0);
        gui.setVisible(true);
        gui.newTrain();
        
        MakeImage image = new MakeImage();
        try{
            image.make(distance);
        } catch(IOException e) {
           System.out.println("error");
        }


        System.out.println("begining");
        while(true){
            
            
            
            //recalc every sleep(1000) and update display
            gui.trains.get(0).updatePhysicalState("00",1);
            gui.updateDisplay();
            gui.mainTrain = gui.trains.get(0);
            //Elijah: Had to cast to into to be acceptable
            distance = (int) gui.trains.get(gui.mainTrainIndex).getActualSpeed();
            
            //image stuff
            try{
                image.make(distance);
            } catch(IOException e) {

            }
            
            //sleep
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

        }

    }
}
