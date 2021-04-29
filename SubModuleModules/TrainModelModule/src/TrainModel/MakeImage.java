package TrainModel;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MakeImage {

    double dist = 0;
    double a;
	public static void main(String[] args) throws IOException {

    }
        public void make(int distance, int auth) throws IOException {

            int width = 500;
            int height = 250;
            int circleWidth = height;
            if(auth != a){
                a = auth;
            }


            double covered = (distance/a)*451;
            System.out.print(dist);
            System.out.print(covered);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.blue);
            g2d.fillRect(0,125, 500, 2);
            g2d.setColor(Color.black);
            g2d.fillRect((int)covered, 120, 32, 12);



            File file = new File("image.png");
            ImageIO.write(bufferedImage, "png", file);
        }
}