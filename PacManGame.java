import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;

public class PacManGame extends PApplet
{
    private PacMan pac;
    private Wall[] board;
    private ArrayList<Food> dots;
    private ArrayList<Ghost> ghosts;
    private Pellet powerPellet;
    private boolean pelletActive;
    private int startingSeconds;
    private PFont font;

    public static void main(String[] args)
    {
        PApplet.main("PacManGame");
    }

    public void settings()
    {
        size(850, 500);
    }

    public void setup()
    {
        pac = new PacMan(this);
        dots = new ArrayList<Food>();
        ghosts = new ArrayList<Ghost>();
        pelletActive = false;
        createBoard();

        startingSeconds = second();
        font = createFont("Century Gothic", 20);
    }

    public void draw()
    {
        background(0);

        // Draw food and pellet
        for (int i = 0; i < dots.size(); i++)
        {
            if (dots.get(i).exists())
            {
                dots.get(i).show();
            }
        }
        if (!pelletActive)
        {
            createPellet();
        }
        else
        {
            powerPellet.show();
        }

        for (int i = 0; i < board.length; i++)  // draw walls
        {
            board[i].show();
        }

        // Pacman display, key input, and movement/collisions
        pac.show();
        pac.move();
        if (keyPressed && key == CODED)
        {
            float newSpeed = (float) 2.5;
            if (pac.powerUp())
            {
                newSpeed = 3;           // faster with power-up
            }
            if (keyCode == UP)
            {
                pac.setDirection(270);
                pac.setXSpeed(0);
                pac.setYSpeed(-newSpeed);
            }
            else if (keyCode == DOWN)
            {
                pac.setDirection(90);
                pac.setXSpeed(0);
                pac.setYSpeed(newSpeed);
            }
            else if (keyCode == LEFT)
            {
                pac.setDirection(180);
                pac.setXSpeed(-newSpeed);
                pac.setYSpeed(0);
            }
            else if (keyCode == RIGHT)
            {
                pac.setDirection(0);
                pac.setXSpeed(newSpeed);
                pac.setYSpeed(0);
            }
        }
        wallCollision(pac);

        // Ghost display, movements, collisions
        for (Ghost g : ghosts)
        {
            g.show(pac.powerUp());
            g.move();
            wallCollision(g);
        }

        // Pacman and Food Hitbox
        for (int i = 0; i < dots.size(); i++)
        {
            if (dots.get(i).exists() && dist(dots.get(i).getX(), dots.get(i).getY(), pac.getX(), pac.getY()) < 15)
            {
                dots.get(i).eat();
                pac.incrementScore(10);     // 10 points per food
            }
        }
        if (Food.getNumEaten() >= Food.getMaxFood() - 10)        // replenish food when it's all eaten
        {
            for (Food f : dots)
            {
                f.reset();
            }
        }

        // Pacman and Pellet Hitbox
        if (pelletActive && dist(powerPellet.getX(), powerPellet.getY(), pac.getX(), pac.getY()) < 18)
        {
            pac.boost();
            pelletActive = false;
        }

        // Pacman and Ghost Hitbox
        for (int i = ghosts.size() - 1; i >= 0; i--)
        {
            if (dist(pac.getX(), pac.getY(), ghosts.get(i).getX(), ghosts.get(i).getY()) < 40)
            {
                if (pac.powerUp())
                {
                    pac.incrementScore(200);    // 200 points for eating a ghost
                    Ghost.ghostDied();
                    fill(255);
                    textSize(20);
                    text("200", ghosts.get(i).getX(), ghosts.get(i).getY());    // display points awarded
                    ghosts.remove(i);

                    if (ghosts.size() == 0)
                    {
                        addGhost();
                    }
                }
                else
                {
                    pac.die();
                    pac.reset();
                    if (pac.getLives() == 0)        // restart game
                    {
                        pac.resetGame();            // reset pacman location/score/lives
                        startingSeconds = second();     // reset timer
                        for (Food f : dots)
                        {
                            f.reset();              // reset food
                        }
                        pelletActive = false;

                        for (int j = ghosts.size() - 1; j >= 0; j--)    // reset all ghosts
                        {
                            ghosts.remove(j);
                        }
                        Ghost.resetNumOutside();
                        addGhost();
                    }
                    break;
                }
            }
        }

        // Cage and Ghost Timing
        // every 10 seconds, a new ghost leaves the cage
        // there can only be a max of 4 ghosts outside at once
        if (timeDifference(startingSeconds, second()) >= 10 && Ghost.getNumOutside() < 4)
        {
            startingSeconds = second();
            // "open" cage by removing one of the walls
            board[0].change(350, 445, 5, 5);
            Wall.setCage(true);
        }
        Ghost g = ghosts.get(ghosts.size() - 1);    // access the most recent ghost
        boolean ghostInCage = g.getY() < board[0].getY() && g.getY() > board[1].getY() && g.getX() > board[2].getX() && g.getX() < board[3].getX();
        if (Wall.cageOpen())
        {
            // once the ghost has left the cage, close the gate
            if (!ghostInCage)
            {
                Ghost.ghostLeft();
                board[0].change(350, 445, 150, 5);
                Wall.setCage(false);
            }
        }
        // shortly after the gate closes, create a new ghost in the cage
        if (timeDifference(startingSeconds, second()) >= 2 && !ghostInCage && Ghost.getNumOutside() < 4)
        {
            addGhost();
        }

        // displays lives, score, and game title
        showScore();
    }

    private void createBoard()      // build wall design, draw food, add one ghost in the cage
    {
        board = new Wall[29];
        board[0] = new Wall(this, 350, 445, 150, 5);
        board[1] = new Wall(this, 350, 350, 150, 5);
        board[2] = new Wall(this, 350, 354, 5, 96);
        board[3] = new Wall(this, 495, 354, 5, 96);
        board[4] = new Wall(this, 50, 50, 100, 50);
        board[5] = new Wall(this, 0, 150, 125, 50);
        board[6] = new Wall(this, 100, 50, 50, 150);
        board[7] = new Wall(this, 200, 50, 150, 50);
        board[8] = new Wall(this, 400, 0, 50, 100);
        board[9] = new Wall(this, 500, 50, 150, 50);
        board[10] = new Wall(this, 700, 50, 100, 50);
        board[11] = new Wall(this, 725, 150, 125, 50);
        board[12] = new Wall(this, 700, 50, 50, 150);
        board[13] = new Wall(this, 200, 150, 50, 150);
        board[14] = new Wall(this, 300, 150, 250, 50);
        board[15] = new Wall(this, 600, 150, 50, 150);
        board[16] = new Wall(this, 75, 250, 75, 50);
        board[17] = new Wall(this, 200, 250, 150, 50);
        board[18] = new Wall(this, 400, 200, 50, 150);
        board[19] = new Wall(this, 500, 250, 125, 50);
        board[20] = new Wall(this, 700, 250, 75, 50);
        board[21] = new Wall(this, 50, 250, 50, 150);
        board[22] = new Wall(this, 150, 350, 125, 50);
        board[23] = new Wall(this, 250, 350, 50, 100);
        board[24] = new Wall(this, 550, 350, 150, 50);
        board[25] = new Wall(this, 550, 350, 50, 100);
        board[26] = new Wall(this, 750, 250, 50, 150);
        board[27] = new Wall(this, 0, 450, 200, 50);
        board[28] = new Wall(this, 650, 450, 200, 50);

        for (int y = 25; y < height; y += 50)
        {
            for (int x = 25; x < width; x += 50)
            {
                if (!(y < board[0].getY() && y > board[1].getY() && x > board[2].getX() && x < board[3].getX()))
                {
                    dots.add(new Food(this, x, y));
                }
            }
        }

        addGhost();
    }

    private void wallCollision(PacMan p)       // method used by both pacman and ghosts
    {
        float r = p.radius();
        for (Wall w : board)
        {
            if (p.xSpeed() != 0 && (p.getX() < w.getX() || p.getX() > w.getX() + w.width()))      // moving horizontally
            {
                if (p.getY() + r > w.getY() + 2 && p.getY() - r < w.getY() + w.height() - 2)                // pacman is within the vertical bounds of the wall
                {
                    if (p.xSpeed() > 0 && p.getX() + r > w.getX() && p.getX() + r < w.getX() + w.width())        // pacman is touching the left of the wall
                    {
                        p.setXSpeed(0);
                    }
                    else if (p.xSpeed() < 0 && p.getX() - r <= w.getX() + w.width() && p.getX() - r > w.getX())    // touching the right
                    {
                        p.setXSpeed(0);
                    }
                }
            }
            else if (p.ySpeed() != 0 && (p.getY() < w.getY() || p.getY() > w.getY() + w.height()))        // moving vertically
            {
                if (p.getX() + r > w.getX() + 2 && p.getX() - r < w.getX() + w.width() - 2)                         // pacman is within the horizontal bounds of the wall
                {
                    if (p.ySpeed() > 0 && p.getY() + r > w.getY() && p.getY() + r < w.getY() + w.height())       // pacman is touching the top of the wall
                    {
                        p.setYSpeed(0);
                    }
                    else if (p.ySpeed() < 0 && p.getY() - r <= w.getY() + w.height() && p.getY() - r > w.getY())      // touching the bottom
                    {
                        p.setYSpeed(0);
                    }
                }
            }
        }
    }

    private void addGhost()
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        int rand = (int) (Math.random() * 4);
        boolean valid = false;
        while (!valid)              // keep generating a random color until it is unique to the other ghosts
        {
            if (rand == 0)
            {   // red
                red = 230;
                green = 15;
                blue = 15;
            }
            else if (rand == 1)
            {   // pink
                red = 240;
                green = 170;
                blue = 170;
            }
            else if (rand == 2)
            {   // orange
                red = 250;
                green = 110;
                blue = 20;
            }
            else
            {   // light blue
                red = 30;
                green = 230;
                blue = 210;
            }
            valid = true;
            for (Ghost g : ghosts)      // check if color is a repeat
            {
                if (red == g.getRed())
                {
                    rand = (int) (Math.random() * 4);
                    valid = false;
                    break;
                }
            }
        }
        // starting position is always (400, 400) in the cage
        ghosts.add(new Ghost(this, 400, 400, red, green, blue));
        ghosts.get(ghosts.size() - 1).setYSpeed(-2);
    }

    private void createPellet()
    {
        if ((int) (Math.random() * 900) == 0)      // once every ~15 seconds
        {
            int x = (int) (Math.random() * 17) * 50 + 25;
            int y = (int) (Math.random() * 10) * 50 + 25;
            boolean inWall = true;
            while (inWall)          // makes sure that the pellet does not appear in the wall or in the cage
            {
                inWall = false;
                for (Wall w : board)
                {
                    if ((x > w.getX() && x < w.getX() + w.width() && y > w.getY() && y < w.getY() + w.height()) ||
                            (y < board[0].getY() && y > board[1].getY() && x > board[2].getX() && x < board[3].getX()))
                    {
                        inWall = true;
                        x = ((int) (Math.random() * 17)) * 50 + 25;
                        y = ((int) (Math.random() * 10)) * 50 + 25;
                    }
                }
            }
            powerPellet = new Pellet(this, x, y);
            pelletActive = true;
        }
    }

    private void showScore()
    {
        textFont(font);
        fill(255);

        // print score
        textAlign(CENTER, CENTER);
        text("SCORE: " + pac.getScore(), 575, 75);

        // print lives
        textAlign(LEFT, CENTER);
        text("LIVES: ", 210, 75);
        fill(255, 255, 0);
        noStroke();
        if (pac.getLives() >= 1)
        {
            arc(280, 75, 20, 20, PI / 4, 7 * PI / 4, PIE);
            if (pac.getLives() >= 2)
            {
                arc(305, 75, 20, 20, PI / 4, 7 * PI / 4, PIE);
                if (pac.getLives() >= 3)
                {
                    arc(330, 75, 20, 20, PI / 4, 7 * PI / 4, PIE);
                }
            }
        }

        fill(255, 255, 0);
        stroke(255, 255, 0);
        textAlign(CENTER, CENTER);
        textSize(40);
        text("PAC MAN", 425, 170);
    }

    public static int timeDifference(int start, int end)        // returns amount of time between start and end
    {
        if (end >= start)
        {
            return end - start;
        }
        else
        {
            return (60 - start) + end;
        }
    }
}