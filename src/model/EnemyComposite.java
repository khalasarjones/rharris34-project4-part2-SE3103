package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import view.GameBoard;

public class EnemyComposite extends GameElement {

    public static final int NROW = 2;
    public static final int NCOLS = 10;
    public static final int ENEMY_SIZE = 20;
    public int UNIT_MOVE = 5;                     // remove static from unit move

    private ArrayList<ArrayList<GameElement>> rows; 
    private ArrayList<GameElement> bombs;
    private boolean movingToRight = true;
    private Random random = new Random();
    private boolean lostGame = false;
    private boolean win = false;

    private int score = 0; 

    public EnemyComposite() {
        rows = new ArrayList<>();
        bombs = new ArrayList<>();

        for (int r = 0; r < NROW; r++)
        {
            var oneRow = new ArrayList<GameElement>();
            rows.add(oneRow);
            for (int c = 0; c < NCOLS; c++)
            {
                oneRow.add(new Enemy(
                c * ENEMY_SIZE * 2, r * ENEMY_SIZE * 2, ENEMY_SIZE, Color.yellow, true
                ));
            }
        }

    }


    public boolean areAllEnemiesDestroyed() {
        for (var row : rows) {
            if (row.size() > 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void render(Graphics2D g2) {
        // render enemy array
        for (var r: rows) {
            for (var c: r) {
                c.render(g2);
            }
        }

        // render bombs
        for (var b: bombs) {
            b.render(g2);
        }

        // Draw "You Lose" text
        if (isEnemyAtBottom() || lostGame) {
            rows = new ArrayList();
            lostGame = true;
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            String message = "You Lose";
            int messageWidth = g2.getFontMetrics().stringWidth(message);
            int x = (GameBoard.WIDTH - messageWidth) / 2;
            int y = GameBoard.HEIGHT / 2;
            g2.drawString(message, x, y);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            String scoreText = "Score: " + score;
            g2.drawString(scoreText, x+30, y+50);

        // Draw "You Win" text
        } else if (win && ! lostGame) {
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            String message = "You Win";
            int messageWidth = g2.getFontMetrics().stringWidth(message);
            int x = (GameBoard.WIDTH - messageWidth) / 2;
            int y = GameBoard.HEIGHT / 2;
            g2.drawString(message, x, y);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            String scoreText = "Score: " + score;
            g2.drawString(scoreText, x+30, y+50);

        } else{ 
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            String scoreText = "Score: " + score;
            g2.drawString(scoreText, 20, 30);
        }
    }

    @Override
    public void animate() {
        int dx = UNIT_MOVE;
        int dy = 0; // Add a new variable for vertical movement
        if (movingToRight) {
            if (rightEnd() >= GameBoard.WIDTH) {
                dx = -dx;
                movingToRight = false;
                dy = ENEMY_SIZE; // Move down 20 units when switching sides
            }
        } else {
            dx = -dx;
            if (leftEnd() <= 0) {
                dx = -dx;
                movingToRight = true;
                dy = ENEMY_SIZE; // Move down 20 units when switching sides
            }
        }

        for (var row : rows) {
            for (var e : row) {
                e.x += dx;
                e.y += dy; // Update the y-coordinate for vertical movement
            }
        }

        // animate bombs
        for (var b : bombs)
            b.animate();
    }


    private int rightEnd() {
        int xEnd = -100;
        for (var row: rows) {
            if (row.size() == 0) continue;
            int x = row.get(row.size() - 1).x + ENEMY_SIZE;
            if (x > xEnd) xEnd = x;
        }

        return xEnd;
    }

    public boolean isEnemyAtBottom() {  // did enemy units reach bottom of screen
        for (var row : rows) {
            for (var enemy : row) {
                if (enemy.y + enemy.height >= GameBoard.HEIGHT) {
                    return true;
                }
            }
        }
        return false;
    }

    private int leftEnd() {
        int xEnd = 9000;
        for (var row: rows) {
            if (row.size() == 0) continue;
            int x = row.get(0).x;
            if (x < xEnd) xEnd = x;
        }
        return xEnd;
    }

    public void dropBombs() {
        for (var row: rows) {
            for (var e: row) {
                if (random.nextFloat() < 0.1F) {
                    bombs.add(new Bomb(e.x, e.y));
                }
            }
        }
    }

    public void removeBombsOutOfBounds() {
        var remove = new ArrayList<GameElement>();
        for (var b: bombs) {
            if (b.y >= GameBoard.HEIGHT) {
                remove.add(b);
            }
        }
        bombs.removeAll(remove);
    }

    public void processCollision(Shooter shooter) {

        var removeBullets = new ArrayList<GameElement>();

        // bullets vs enemies 
        GameElement elem = new Bullet(-1,-1,Color.red);

        for (var row: rows)
        {
            var removeEnemies = new ArrayList<GameElement>();
            for (var enemy: row) {
                for (var bullet: shooter.getWeapons())
                {

                    if (enemy.collideWith(bullet))                            // enemy collision with blue bullet
                    {
                        System.out.println(enemy.x +","+ enemy.y);           // location of destroyed enemy (x,y)

                        if (bullet.color == Color.blue)
                        {
                            for (var ro: rows)
                            {

                                for (var enem : ro)
                                {
                                    if(enem.x == enemy.x && enem != enemy)         // blue bullet can destroy two enemies
                                    {
                                        removeEnemies.add(enem);
                                        score = shooter.getScore();
                                        shooter.setScore(score += 10);            // blue bullet hit adds additional 10 points
                                    }
                                }
                            }
                                for (var component: shooter.getComponents())
                                {
                                    if(component.ispresent == false)
                                    {
                                        component.ispresent = true;            // restore a component of shooter
                                        break;
                                    }
                                }

                        }

                        score = shooter.getScore();
                        shooter.setScore(score+=10);
                        removeBullets.add(bullet);
                        removeEnemies.add(enemy);
                    }

                }
            }

            rows.get(0).removeAll(removeEnemies);
            rows.get(1).removeAll(removeEnemies);

        }


        shooter.getWeapons().removeAll(removeBullets);


        // point loss for bomb hits on shooter
        var removeBombs = new ArrayList<GameElement>();
        for (var b: bombs) {
            for (var component: shooter.getComponents()) {
                if (component.collideWith(b))
                {
                   component.ispresent = false;

                     if (score >= 20)
                        shooter.setScore(score-=20);

                     else if(score>=10)
                         shooter.setScore(score-=10);

                    removeBombs.add(b);
                     //break;
                }
            
            }

        }

        if (areAllEnemiesDestroyed()) {
            win = true;
        }


        boolean found = false;
        for (var component: shooter.getComponents())
        {
            if (component.ispresent)
                found = true;
        }
        if(!found)
            lostGame = true;

        // bullets vs bombs

        removeBombs = new ArrayList<GameElement>();
        removeBullets.clear();

        for (var b: bombs) {
            for (var bullet: shooter.getWeapons()) {
                if (b.collideWith(bullet)) {
                    removeBombs.add(b);
                    removeBullets.add(bullet);
                }
            }
        }

        shooter.getWeapons().removeAll(removeBullets);
        bombs.removeAll(removeBombs);

    }

    public int getLength() {
        int length = 0;
        for (ArrayList<GameElement> lis : rows) {
            length += lis.size();
        }
        return length;
    }

    public void setSpeed(int speed) {
        UNIT_MOVE = speed;

    }
}
