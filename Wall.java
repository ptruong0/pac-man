import processing.core.PApplet;

public class Wall
{
    private PApplet applet;
    private float x, y;
    private float w, h;
    private static boolean cageOpen = false;

    public Wall(PApplet app, float xPos, float yPos, float width, float height)
    {
        applet = app;
        x = xPos;
        y = yPos;
        w = width;
        h = height;
    }

    public void show()
    {
        applet.stroke(20, 20, 225);
        applet.fill(20, 20, 225);
        applet.rect(x, y, w, h, 7);     // rounded corners
    }

    public void change(float xPos, float yPos, float width, float height)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
    }

    public static void setCage(boolean open)
    {
        cageOpen = open;
    }

    public static boolean cageOpen() { return cageOpen; }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float width()
    {
        return w;
    }

    public float height()
    {
        return h;
    }
}
