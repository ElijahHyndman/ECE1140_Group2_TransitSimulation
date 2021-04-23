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
      setTitle("Green Line Track");  // "super" JFrame sets the title
      setVisible(true);    // "super" JFrame show
   }
   private class DrawCanvas extends JPanel {
      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);     // paint parent's background
         setBackground(Color.BLACK);  // set background color for this JPanel

         // Your custom painting codes. For example,
         // Drawing primitive shapes
         g.setColor(Color.green);    // set the drawing color
         g.drawLine(20, 100, 30, 100); //J
         g.drawLine(35,100,115,100);//K
         g.drawLine(120,100,170,100);//L
         g.drawLine(180,100,210,100);//M
         g.drawLine(220,100,320,100);//N
         g.drawLine(325,100,355,100);//O
         g.drawLine(220,100,235,70);//R
         g.drawLine(235,70,250,70);//R
         g.drawLine(255,70,285,70);//S
         g.drawLine(290,70,340,70);//T
         g.drawLine(325,100,335,130);//Q
         g.drawLine(335,130,355,130);//Q
         g.drawLine(360,130,395,130);//P
         g.drawLine(395,130,410,100);//P
         g.drawLine(365,100,410,100);//P
         g.drawLine(345,70,415,70);//U

         g.drawLine(15,170,45,170);//U
         g.drawLine(50,170,75,170);//V
         g.drawLine(80,170,230,170);//W
         g.drawLine(235,170,280,170);//X
         g.drawLine(285,170,330,170);//Y
         g.drawLine(335,170,350,170);//Z
         g.drawLine(350,170,370,200);//Z
         g.drawLine(375,200,405,200);//F
         g.drawLine(340,200,370,200);//G
         g.drawLine(320,200,335,200);//H
         g.drawLine(180,200,315,200);//I
         g.drawLine(140,200,160,200);//J

         g.drawLine(15,260,55,260);//F
         g.drawLine(60,260,90,260);//E
         g.drawLine(95,260,125,260);//D
         g.drawLine(130,260,150,290);//A
         g.drawLine(150,290,180,290);//A
         g.drawLine(185,290,215,290);//B
         g.drawLine(130,260,200,260);//C
         g.drawLine(200,260,220,290);//C


         g.setColor(Color.RED);       // change the drawing color
         g.drawOval(10,98,5,5);
         g.fillOval(10,98,5,5);
         g.drawOval(75,98,5,5);
         g.fillOval(75,98,5,5);
         g.drawOval(170,98,5,5);
         g.fillOval(170,98,5,5);
         g.drawOval(215,98,5,5);
         g.fillOval(215,98,5,5);
         g.drawOval(355,98,5,5);
         g.fillOval(355,98,5,5);
         g.drawOval(410,98,5,5);
         g.fillOval(410,98,5,5);
         g.drawOval(415,68,5,5);
         g.fillOval(415,68,5,5);

         g.drawOval(10,168,5,5);
         g.fillOval(10,168,5,5);
         g.drawOval(90,168,5,5);
         g.fillOval(90,168,5,5);
         g.drawOval(150,168,5,5);
         g.fillOval(150,168,5,5);
         g.drawOval(210,168,5,5);
         g.fillOval(210,168,5,5);
         g.drawOval(405,198,5,5);
         g.fillOval(405,198,5,5);
         g.drawOval(355,198,5,5);
         g.fillOval(355,198,5,5);
         g.drawOval(305,198,5,5);
         g.fillOval(305,198,5,5);
         g.drawOval(250,198,5,5);
         g.fillOval(250,198,5,5);
         g.drawOval(190,198,5,5);
         g.fillOval(190,198,5,5);
         g.drawOval(130,198,5,5);
         g.fillOval(130,198,5,5);

         g.drawOval(10,258,5,5);
         g.fillOval(10,258,5,5);
         g.drawOval(100,258,5,5);
         g.fillOval(100,258,5,5);
         g.drawOval(160,288,5,5);
         g.fillOval(160,288,5,5);
         g.drawOval(200,258,5,5);
         g.fillOval(200,258,5,5);

         g.setColor(Color.LIGHT_GRAY);
         g.drawLine(160,200,180,200);

         // Printing texts
         g.setColor(Color.WHITE);
         g.setFont(new Font("Monospaced", Font.PLAIN, 10));
         g.drawString("Yard", 3, 115);
         g.drawString("Glenbury",75,115);
         g.drawString("Dormont",170,115);
         g.drawString("Mt.Lebanon",215,115);
         g.drawString("Poplar",355,115);
         g.drawString("Castle Shannon",420,105);
         g.drawString("Glenbury",425,75);

         g.drawString("Glenbury",10,185);
         g.drawString("Overbrook",90,185);
         g.drawString("Inglewood",150,185);
         g.drawString("Central",210,185);
         g.drawString("Whited",415,205);
         g.drawString("Southbank",355,215);
         g.drawString("Central",305,215);
         g.drawString("Inglewood",247,215);
         g.drawString("Overbrook",190,215);
         g.drawString("Yard",125,215);

         g.drawString("Whited",10,275);
         g.drawString("Station",95,275);
         g.drawString("Pioneer",160,305);
         g.drawString("Edgebrook",210,265);

         g.setFont(new Font("Monospaced", Font.PLAIN, 13));
         g.drawString("J", 20, 90);
         g.drawString("K", 75, 90);
         g.drawString("L",143,90);
         g.drawString("M",190,90);
         g.drawString("N",260,90);
         g.drawString("O",340,90);
         g.drawString("R",235,60);
         g.drawString("S",270,60);
         g.drawString("T",315,60);
         g.drawString("Q",340,120);
         g.drawString("P",380,90);
         g.drawString("U",370,60);

         g.drawString("U",30,160);
         g.drawString("V",60,160);
         g.drawString("W",150,160);
         g.drawString("X",258,160);
         g.drawString("Y",300,160);
         g.drawString("Z",345,160);
         g.drawString("F",390,190);
         g.drawString("G",350,190);
         g.drawString("H",325,190);
         g.drawString("I",260,190);

         g.drawString("F",30,250);
         g.drawString("E", 75, 250);
         g.drawString("D", 110,250);
         g.drawString("A", 160,280);
         g.drawString("B", 190,280);
         g.drawString("C",170,250);

         g.setFont(new Font("Monospaced", Font.PLAIN, 7));
         g.drawString("65", 75, 97);
         g.drawString("73",170, 97);
         g.drawString("77",215,97);
         g.drawString("88",355,97);
         g.drawString("96",410,97);
         g.drawString("114",415,67);

         g.drawString("114",10,167);
         g.drawString("123",90,167);
         g.drawString("132",150,167);
         g.drawString("141",210,167);
         g.drawString("22",405,197);
         g.drawString("31",355,197);
         g.drawString("39",305,197);
         g.drawString("48",250,197);
         g.drawString("57",190,197);

         g.drawString("22",10,257);
         g.drawString("16",100,257);
         g.drawString("2",160,287);
         g.drawString("9",200,257);

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