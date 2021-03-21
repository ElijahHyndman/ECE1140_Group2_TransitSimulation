/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainModel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MakeImage {

	public static void main(String[] args) throws IOException {
            
    
  

  }
        public void make(int distance) throws IOException {
            int width = 500;
            int height = 250;
            int circleWidth = height;


            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.blue);
            g2d.fillRect(0,125, 500, 2);
            g2d.setColor(Color.black);
            g2d.fillRect(distance, 120, 32, 12);



            File file = new File("image.png");
            ImageIO.write(bufferedImage, "png", file);
        }
}