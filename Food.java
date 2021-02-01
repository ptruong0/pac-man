import processing.core.PApplet;

public class Food
{
    // used by Pellet class as well
    protected PApplet applet;
    protected float x, y;
    protected float radius;

    private boolean eaten;
    private static int numEaten = 0;    // different than score, resets when there are no more foods on screen
    private static int maxFoods = 102;  // maximum number of foods on screen at once

    public Food(PApplet app, float xLoc, float yLoc)
    {
        applet = app;
        radius = 2;
        x = xLoc;
        y = yLoc;
        eaten = false;
    }

    public void show()
    {
        applet.stroke(255);
        applet.fill(255);
        applet.ellipse(x, y, radius * 2, radius * 2);
    }

    public void eat()
    {
        eaten = true;
        numEaten++;
    }

    public static int getNumEaten()
    {
        return numEaten;
    }

    public static int getMaxFood() { return maxFoods; }

    public boolean exists()
    {
        return !eaten;
    }

    public void reset()
    {
        eaten = false;
        numEaten = 0;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }
}
