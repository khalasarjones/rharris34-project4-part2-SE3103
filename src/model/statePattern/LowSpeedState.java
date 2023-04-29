package model.statePattern;

import controller.BoxSpeedController;

public class LowSpeedState implements SpeedState {

    private BoxSpeedController controller;
    private int speed = 3;
    public LowSpeedState(BoxSpeedController controller) {
        this.controller = controller;
    }

    @Override
    public void increaseSpeed() {
        controller.setMedium();
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
