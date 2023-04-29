package controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import model.Bullet;
import model.Shooter;
import view.GameBoard;

public class TimerListener implements ActionListener{

    public enum EventType {
        KEY_RIGHT, KEY_LEFT, KEY_SPACE
    }
    static int n = 0;
    private GameBoard gameBoard;
    private LinkedList<EventType> eventQueue;
    private final int BOMB_DROP_FREQ = 20;
    private int frameCounter = 0;

    private BoxSpeedController speed;


    public TimerListener(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        speed = new BoxSpeedController(20);             // create new box speed object
        eventQueue = new LinkedList<>();
    }

    public void setSpeedEnemy(){
        gameBoard.getEnemyComposite().setSpeed(speed.getCurrentState().speed());    // speed state of enemies method
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ++frameCounter;
        update();
        processEventQueue();
        processCollision();
        gameBoard.getCanvas().repaint();
    }

    private void processEventQueue()
    {

        while (!eventQueue.isEmpty()) {
            var e = eventQueue.getFirst();
            eventQueue.removeFirst();
            Shooter shooter = gameBoard.getShooter();
            if (shooter == null) return;

            switch (e) {
                case KEY_LEFT:
                    shooter.moveLeft();
                    break;
                case KEY_RIGHT:
                    shooter.moveRight();
                    break;
                case KEY_SPACE:
                    if (shooter.canFireMoreBullet())
                    {
                        n++;
                        if(shooter.weapons.size() == 2) {
                            shooter.getWeapons().add(new Bullet(shooter.x, shooter.y, Color.blue));   // blue bullet for third shot
                        }
                        else
                        shooter.getWeapons().add(new Bullet(shooter.x, shooter.y, Color.red));
                    }
                    break;
            }
        }

        if (frameCounter == BOMB_DROP_FREQ) {
            gameBoard.getEnemyComposite().dropBombs();
            frameCounter = 0;
        }
    }

    private void processCollision() {
        var shooter = gameBoard.getShooter();
        var enemyComposite = gameBoard.getEnemyComposite();

        speed.setNumberOfBoxes(enemyComposite.getLength());                        // establish speed state in collision
        gameBoard.getEnemyComposite().setSpeed(speed.getCurrentState().speed());

        shooter.removeBulletsOutofBound();
        enemyComposite.removeBombsOutOfBounds();
        enemyComposite.processCollision(shooter);
    }

    private void update() {

        for (var e: gameBoard.getCanvas().getGameElements())
            e.animate();
    }

    public LinkedList<EventType> getEventQueue() {
        return eventQueue;
    }
    
}
