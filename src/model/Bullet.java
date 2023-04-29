package model;

import java.awt.Graphics2D;
import java.awt.Color;

public class Bullet extends GameElement
{

    public static final int WIDTH = 5;
    public static final int UNIT_MOVE = 10;
    public Bullet(int x, int y, Color c) {           // add color parameter to bullet constructor
        super(x, y, c, true, WIDTH, WIDTH*3);
        color = c;
    }

    @Override
    public void render(Graphics2D g2)
    {
        g2.setColor(color);
        if (filled)
            g2.fillRect(x, y, width, height);
        else {
            g2.drawRect(x, y, width, height);
        }
    }

    @Override
    public void animate() {
        super.y -= UNIT_MOVE;
    }
    
}
