import processing.core.PApplet;

public class Pellet extends Food
{

    public Pellet(PApplet app, float xLoc, float yLoc)
    {
        super(app, xLoc, yLoc);
        radius = 10;     // slightly larger than normal food
    }

    @Override
    public void show()
    {
        applet.stroke(220, 120, 120);
        applet.fill(220, 120, 120);     // pink-ish
        applet.ellipse(x, y, radius * 2, radius * 2);
    }

}
