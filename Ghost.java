import processing.core.PApplet;

public class Ghost extends PacMan
{
    private int r, g, b;
    private static int numOutside = 0;

    public Ghost(PApplet app, float xLoc, float yLoc, int red, int green, int blue)
    {
        super(app);
        x = xLoc;
        y = yLoc;
        radius = 24;
        r = red;
        g = green;
        b = blue;
    }

    public void show(boolean disable)
    {
        applet.stroke(r, g, b);
        applet.fill(r, g, b);
        if (disable)
        {
            applet.stroke(10, 20, 110);         // dark blue when vulnerable to pacman
            applet.fill(10, 20, 110);
        }

        // ghost shape
        applet.beginShape();
        applet.vertex(x - radius, y + radius);
        applet.bezierVertex(x - 45, y - 50, x + 45, y - 50, x + radius, y + radius);
        applet.vertex(x + radius / 2, y + radius / 2);
        applet.vertex(x, y + radius);
        applet.vertex(x - radius / 2, y + radius / 2);
        applet.vertex(x - radius, y + radius);
        applet.endShape();

        // eyes
        applet.fill(255);
        applet.ellipse(x - radius / 3, y - radius / 2, radius / 2, radius / 2);
        applet.ellipse(x + radius / 3, y - radius / 2, radius / 2, radius / 2);
        applet.fill(0, 0, 255);
        if (disable)
        {
            applet.fill(255, 255, 0);
        }
        applet.ellipse(x - radius / 3, y - radius / 2, radius / 4, radius / 4);
        applet.ellipse(x + radius / 3, y - radius / 2, radius / 4, radius / 4);
    }

    @Override
    public void move()
    {
        super.move();
        while (xSpeed == 0 && ySpeed == 0)
        {
            int r = (int) (Math.random() * 4);
            if (r == 0)
            {
                xSpeed = (float) 3;
            }
            else if (r == 1)
            {
                xSpeed = (float) -3;
            }
            else if (r == 2)
            {
                ySpeed = (float) 3;
            }
            else
            {
                ySpeed = (float) -3;
            }
        }
    }

    public int getRed() { return r; }

    public static void ghostLeft() { numOutside++; }

    public static void ghostDied() { numOutside--; }

    public static void resetNumOutside()
    {
        numOutside = 0;
    }

    public static int getNumOutside() { return numOutside; }






}
