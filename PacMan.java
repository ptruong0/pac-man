import processing.core.PApplet;

public class PacMan
{
    // used by Ghost class as well
    protected PApplet applet;
    protected float radius;
    protected float x, y;
    protected float xSpeed, ySpeed;

    private int direction;  // in degrees
    private float mouthAngle;   // in radians
    private boolean mouthClosing;
    private boolean powerUp;
    private int boostStartTime; // records time when boost started
    private int score;
    private int lives;

    public PacMan(PApplet app)
    {
        applet = app;
        radius = 20;
        x = radius;
        y = radius;
        xSpeed = 0;
        ySpeed = 0;
        direction = 0;
        mouthAngle = applet.PI / 4;
        mouthClosing = true;
        powerUp = false;
        score = 0;
        lives = 3;
    }

    public void show()
    {
        // translate/rotate
        applet.translate(x, y);
        float radians = applet.radians(direction);
        applet.rotate(radians);

        applet.stroke(255, 255, 0);
        applet.fill(255, 255, 0);
        applet.arc(0, 0, radius * 2, radius * 2, mouthAngle, 2 * applet.PI - mouthAngle, applet.PIE);       // pie shape with changing angle

        // untranslate/unrotate
        applet.rotate(-radians);
        applet.translate(-x, -y);

        // mouth continuously opening and closing
        float mouthSpd = applet.PI / 32;
        if (powerUp)
        {
            mouthSpd = applet.PI / 16;
        }
        if (mouthClosing)
        {
            mouthAngle -= mouthSpd;
        }
        else
        {
            mouthAngle += mouthSpd;
        }
        if (mouthAngle <= 0)
        {
            mouthClosing = false;
        }
        else if (mouthAngle >= applet.PI / 4)
        {
            mouthClosing = true;
        }

        // turn off power-up after 10 seconds
        if (PacManGame.timeDifference(boostStartTime, applet.second()) >= 10)
        {
            powerUp = false;
            boostStartTime = 0;
        }
    }

    public void move()
    {
        if ((x < radius && xSpeed < 0) || (x > applet.width - radius && xSpeed > 0))        // check left and right borders of the window
        {
            xSpeed = 0;
        }
        x += xSpeed;
        if ((y < radius && ySpeed < 0) || (y > applet.height - radius && ySpeed > 0))       // check top and bottom borders of the window
        {
            ySpeed = 0;
        }
        y += ySpeed;
    }

    public void boost()
    {
        powerUp = true;
        boostStartTime = applet.second();
    }

    public void reset()
    {
        x = radius;
        y = radius;
    }

    public void resetGame()
    {
        score = 0;
        lives = 3;
        setXSpeed(0);
        setYSpeed(0);
        reset();
    }

    public void setXSpeed(float spd) { xSpeed = spd; }

    public void setYSpeed(float spd) { ySpeed = spd; }

    public void setDirection(int newDirection) { direction = newDirection; }

    public void incrementScore(int points) { score += points; }

    public void die()
    {
        lives--;
    }



    public float getX() { return x; }

    public float getY() { return y; }

    public float radius() { return radius; }

    public float xSpeed() { return xSpeed; }

    public float ySpeed() { return ySpeed; }

    public boolean powerUp() { return powerUp; }

    public int getScore() { return score; }

    public int getLives() { return lives; }
}