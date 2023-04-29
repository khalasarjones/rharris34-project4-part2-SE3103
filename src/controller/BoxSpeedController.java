package controller;

import model.statePattern.LowSpeedState;
import model.statePattern.MediumSpeedState;
import model.statePattern.HighSpeedState;
import model.statePattern.SpeedState;

public class BoxSpeedController {
    private SpeedState lowSpeedState;
    private SpeedState mediumSpeedState;
    private SpeedState highSpeedState;

    private SpeedState currentState;
    private int numberOfBoxes;
    
    public BoxSpeedController(int numberOfBoxes) {
        this.lowSpeedState = new LowSpeedState(this);
        this.mediumSpeedState = new MediumSpeedState(this);
        this.highSpeedState = new HighSpeedState(this);
        
        this.numberOfBoxes = numberOfBoxes;
        if (numberOfBoxes <= 10) {
            this.currentState = lowSpeedState;
        } else if (numberOfBoxes > 10 && numberOfBoxes <= 50) {
            this.currentState = mediumSpeedState;
        } else {
            this.currentState = highSpeedState;
        }
    }
    public int getNumberOfBoxes() {
        return numberOfBoxes;
    }
    
    public void setNumberOfBoxes(int numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
        
        if (numberOfBoxes >= 15) {
            this.currentState = lowSpeedState;
        } else if (numberOfBoxes > 8) {
            this.currentState = mediumSpeedState;
        } else {
            this.currentState = highSpeedState;
        }
    }
    
    public SpeedState getCurrentState() {
        return currentState;
    }
    
    public void increaseSpeed() {
        currentState.increaseSpeed();
    }
    
    public void decreaseSpeed() {
        currentState.decreaseSpeed();
    }


    public void setMedium() {
        currentState = this.mediumSpeedState;
    }

    public void setHigh() {
        currentState = this.highSpeedState;
    }

    public void setLow() {
        currentState = this.lowSpeedState;
    }
}
