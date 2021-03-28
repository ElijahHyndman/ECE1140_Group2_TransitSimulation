package CTCUI;

import java.awt.*;       // Using AWT's Graphics and Color
import javax.swing.*;    // Using Swing's components and containers
public class TransitDisplay extends JFrame {
   // Define constants
   public static final int CANVAS_WIDTH  = 640;
   public static final int CANVAS_HEIGHT = 480;

   private DrawCanvas canvas;
 
   // Constructor to set up the GUI components and event handlers
   public TransitDisplay() {
      canvas = new DrawCanvas();    // Construct the drawing canvas
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
 
      // Set the Drawing JPanel as the JFrame's content-pane
      Container cp = getContentPane();
      cp.add(canvas);
      // or "setContentPane(canvas);"
 
      setDefaultCloseOperation(EXIT_ON_CLOSE);   // Handle the CLOSE button
      pack();              // Either pack() the components; or setSize()
      setTitle("Blue Line Track");  // "super" JFrame sets the title
      setVisible(true);    // "super" JFrame show
   }
   private class DrawCanvas extends JPanel {
      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);     // paint parent's background
         setBackground(Color.BLACK);  // set background color for this JPanel
 
         // Your custom painting codes. For example,
         // Drawing primitive shapes
         g.setColor(Color.CYAN);    // set the drawing color
         g.drawLine(20, 300, 70, 300);
         g.drawLine(75,300,125,300);
         g.drawLine(130,300,180,300);
         g.drawLine(185,300,235,300);
         g.drawLine(240,300,290,300);
         g.drawLine(295,300,345,300);
         g.drawLine(350,300,400,300);
         g.drawLine(405,300,455,300);
         g.drawLine(460,300,510,300);
         g.drawLine(515,300,565,300);
         g.drawLine(325,315,375,315);
         g.drawLine(380,315,430,315);
         g.drawLine(435,315,485,315);
         g.drawLine(490,315,540,315);
         g.drawLine(545,315,595,315);
         
         g.setColor(Color.RED);       // change the drawing color
         g.drawOval(10,298,5,5);
         g.fillOval(10,298,5,5);
         g.drawOval(570,298,5,5);
         g.fillOval(570,298,5,5);
         g.drawOval(600,313,5,5);
         g.fillOval(600,313,5,5);

         g.setColor(Color.lightGray);
         //g.drawLine(290,300,325,285);
         g.drawLine(290,300,325,315);
         //g.fillRect(400, 350, 60, 50);
         // Printing texts
         g.setColor(Color.WHITE);
         g.setFont(new Font("Monospaced", Font.PLAIN, 10));
         g.drawString("Yard", 3, 295);
         g.drawString("Station C",590,310);
         g.drawString("Station B",560,295);
         g.setFont(new Font("Monospaced", Font.PLAIN, 9));
         g.drawString("Switch 1",262,315);

      }
   }
 
   // The entry main method
   public static void main(String[] args) {
      // Run the GUI codes on the Event-Dispatching thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new TransitDisplay(); // Let the constructor do the job
         }
      });
   }
}