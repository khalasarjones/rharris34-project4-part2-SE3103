package model.statePattern;

import controller.BoxSpeedController;

public class HighSpeedState implements SpeedState {
    private BoxSpeedController controller;
    private int speed = 7;
    public HighSpeedState(BoxSpeedController controller) {
        this.controller = controller;
    }

    @Override
    public void increaseSpeed() {
        controller.setHigh();
    }

    @Override
    public void decreaseSpeed() {
        controller.setMedium();
    }

    @Override
    public int speed() {
        return speed;
    }
}
