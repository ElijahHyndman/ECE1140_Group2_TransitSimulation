package TrainControlUI;

import java.awt.*;
        import java.awt.font.*;
        import java.awt.geom.*;
        import javax.swing.*;

/**
 * A Guage is a component that displays a bounded range and a numeric value
 * using a circular or semi-circular representation (e.g., an analog
 * pressure gauge or speedometer).
 *
 * Note: This class could implement java.awt.Adjustable if necessary. However,
 * there would be a loss of precision since the minimum, maximum and value
 * of Adjustable objects are int values rather than double values.
 *
 * @author  Prof. David Bernstein, James Madison University
 * @version 1.0
 */
public class Gauge extends JComponent
{
    private Color    highlight;
    private double   max, min, value;
    private int      ticks, shape;

    private static final int     PIVOT_RADIUS   =  4;
    private static final int     TICK_LENGTH    = 10;
    private static final Stroke  OUTLINE_STROKE = new BasicStroke(2.0f);
    private static final Stroke  TICK_STROKE    = new BasicStroke(1.0f);


    public  static final int     CIRCLE         = 0;
    public  static final int     SEMI_CIRCLE    = 1;


    /**
     * Default Constructor
     *
     * Constructs a semi-circular Gauge with a range of [0,100]
     */
    public Gauge()
    {
        this(0.0, 100.0, SEMI_CIRCLE);
    }

    /**
     * Explicit Value Constructor
     *
     * Constructs a semi-circular Gauge with a range of [min,max]
     *
     * @param min   The minimum possible value
     * @param max   The maximum possible value
     */
    public Gauge(double min, double max)
    {
        this(min, max, SEMI_CIRCLE);
    }

    /**
     * Explicit Value Constructor
     *
     * @param min   The minimum possible value
     * @param max   The maximum possible value
     * @param shape Either SEMI_CIRCLE or CIRCLE
     */
    public Gauge(double min, double max, int shape)
    {
        if ((shape >= CIRCLE) && (shape <= SEMI_CIRCLE)) this.shape = shape;
        else                                             this.shape = CIRCLE;

        this.min   = min;
        this.max   = max;

        this.ticks = 10;

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }

    /**
     * Render this Gauge
     *
     * @param g   The graphics object to use
     */
    public void paint(Graphics g)
    {
        AffineTransform    at;
        Color[]            gradientColors;
        Dimension          d;
        double             a, degrees, degreesPerTick;
        double             height, increasePerTick, label, radius;
        double             width, xMid, yMid;
        Ellipse2D.Double   middle, pivot;
        float[]            gradientFractions;
        Font               font;
        FontRenderContext  frc;
        Graphics2D         g2;
        Insets             insets;
        int                labels;
        Line2D.Double      axis, pointer;
        Paint              gradient;
        Point2D.Double     p, q;
        Rectangle2D        r;
        Shape              outline;
        String             s;

        super.paint(g);

        // Setup the Graphics2D object
        g2      = (Graphics2D)g;
        font    = getFont();
        frc     = g2.getFontRenderContext();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Initialize
        gradientColors       = new Color[2];
        gradientFractions    = new float[2];
        gradientColors[0]    = highlight;
        gradientColors[1]    = getBackground();
        gradientFractions[0] = 0.00f;
        gradientFractions[1] = 0.80f;

        d       = getSize();
        insets  = getInsets();

        width   = d.getWidth()  - insets.left - insets.right;
        height  = d.getHeight() - insets.top  - insets.bottom;

        // Determine the geometry based on the type/shape of the Gauge
        if (shape == CIRCLE)
        {
            radius  = Math.min(width/2.0, height/2.0);
            xMid    = insets.left + width/2.0;
            yMid    = insets.top  + height/2.0;
            degrees = 360.0;

            degreesPerTick = degrees / ticks;
            increasePerTick  = (this.max - this.min) / ticks;

            labels = ticks;

            outline = new Ellipse2D.Double(xMid-radius, yMid-radius,
                    radius*2.0, radius*2.0);
        }
        else
        {
            radius  = Math.min(width/2.0, height);
            xMid    = insets.left + width/2.0;
            yMid    = insets.top  + height;
            degrees = 180.0;

            degreesPerTick = degrees / ticks;
            increasePerTick  = (this.max - this.min) / ticks;

            labels = ticks + 1;

            outline = new Arc2D.Double(xMid-radius, yMid-radius,
                    radius*2.0, radius*2.0,
                    0.0, degrees, Arc2D.OPEN);
        }

        // Draw the lines that will become the tick marks
        axis = new Line2D.Double(xMid-radius, yMid, xMid, yMid);
        g2.setColor(getForeground());
        g2.setStroke(TICK_STROKE);
        a = 0.0;
        for (int i=0; i<=ticks; i++)
        {
            at = AffineTransform.getRotateInstance(Math.toRadians(a),
                    xMid, yMid);
            g2.draw(at.createTransformedShape(axis));
            a += degreesPerTick;
        }


        // Fill the middle in the background color (thereby creating the ticks)
        middle = new Ellipse2D.Double(xMid-radius+TICK_LENGTH,
                yMid-radius+TICK_LENGTH,
                (radius-TICK_LENGTH)*2.0,
                (radius-TICK_LENGTH)*2.0);
        g2.setColor(getBackground());
        g2.fill(middle);



        // Draw the outline
        g2.setColor(getForeground());
        g2.setStroke(OUTLINE_STROKE);
        g2.draw(outline);


        // Fill the highlight
        if (highlight != null)
        {
            gradient = new RadialGradientPaint((float)xMid,(float)yMid,
                    (float)radius,
                    gradientFractions,
                    gradientColors);
            g2.setPaint(gradient);
            g2.fill(middle);
        }


        // Draw the text
        g2.setColor(getForeground());
        g2.setFont(getFont());
        p     = new Point2D.Double(xMid-radius+2*TICK_LENGTH, yMid);
        q     = new Point2D.Double();
        a     = 0.0;
        label = 0.0;

        for (int i=0; i<labels; i++)
        {
            at  = AffineTransform.getRotateInstance(Math.toRadians(a),
                    xMid, yMid);
            at.transform(p, q);

            s   = String.format("%3.0f", label);
            r   = font.getStringBounds(s, frc);

            g2.drawString(s,
                    (float)q.x-(float)r.getWidth(),
                    (float)q.y+(float)r.getHeight()/2.0f);

            a     += degreesPerTick;
            label += increasePerTick;

        }


        // Draw the pointer
        pivot = new Ellipse2D.Double(xMid-PIVOT_RADIUS,
                yMid-PIVOT_RADIUS,
                PIVOT_RADIUS*2.0,
                PIVOT_RADIUS*2.0);
        pointer = new Line2D.Double(xMid-radius+2*TICK_LENGTH,yMid,xMid,yMid);
        at=AffineTransform.getRotateInstance(Math.toRadians(value/max*degrees),
                xMid, yMid);
        g2.setColor(Color.BLACK);
        g2.draw(at.createTransformedShape(pointer));
        g2.fill(pivot);
    }

    /**
     * Set the highlight color to use in the center of the Gauge
     * (or null to use the background color)
     *
     * @param highlightColor  The highlight color to use
     */
    public void setHighlight(Color highlightColor)
    {
        this.highlight = highlightColor;
    }

    /**
     * Set the current value to display
     *
     * @param value  The current value
     */
    public void setValue(double value)
    {
        this.value = value;
    }
}