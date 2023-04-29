package model.statePattern;

import controller.BoxSpeedController;

public class MediumSpeedState implements SpeedState {
    
    private BoxSpeedController controller;
    private int speed = 5;
    public MediumSpeedState(BoxSpeedController controller) {
        this.controller = controller;
    }

    @Override
    public void increaseSpeed() {
        controller.setHigh();
    }

    @Override
    public void decreaseSpeed() {
        controller.setLow();
    }

    @Override
    public int speed() {
        return speed;
    }
}
