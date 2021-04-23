package TrainModel;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import TrainModel.*;

import java.lang.*;
import java.io.IOException;

/**
 *
 * @author Devon
 */
public class main {
    
    Thread t;
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        // TODO Elijah: had to change this to get it to work
        //original: double distance = 0;
        int distance = 0;
        trainGUI gui = new trainGUI(0);
        gui.setVisible(true);
        gui.newTrain();
        
        //MakeImage image = new MakeImage();
        //try{
            // TODO Elijah: had to change this to get it to work
            // original: image.make(distance, 0);
            //image.make(distance);
        //} catch(IOException e) {
          //  System.out.println("error");
        //}


        System.out.println("begining");
        while(true){
            
            
            
            //recalc every sleep(1000) and update display
            for(int i=0; i<gui.trains.size(); i++){
                //gui.trains.get(i).updatePhysicalState("",1);
            }
            gui.updateDisplay();

            //Elijah: Had to cast to into to be acceptable
            distance = (int) gui.trains.get(gui.mainTrainIndex).getActualSpeed();
            
            //image stuff
            //try{
                // TODO Elijah: I had to change this to get it to work
                //image.make(distance, 0);
                //image.make(distance);
            //} catch(IOException e) {

            //}
            
            //sleep
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Loop Cycle");
        }

    }
}
