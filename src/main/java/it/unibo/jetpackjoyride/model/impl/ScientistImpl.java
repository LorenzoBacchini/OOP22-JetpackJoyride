package it.unibo.jetpackjoyride.model.impl;

import it.unibo.jetpackjoyride.model.api.Direction;
import it.unibo.jetpackjoyride.model.api.Scientist;

/**
 * TODO: Vedi commento su texture nell'interfaccia ?
 * Standard scientist implementation
 * @author lorenzo.annibalini@studio.unibo.it
 */

public class ScientistImpl implements Scientist{

    final static double MAXSPEED = 10;

    private final Direction direction;
    private boolean life;
    private double speed;
    private String texture;

    public ScientistImpl(Direction direction, String texture) {
        this.direction = direction;
        this.life = true;
        this.texture = texture;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public Boolean isAlive() {
        return this.life;
    }

    @Override
    public void killScientist() {
        this.life = false;    
    }

    @Override
    public void setSpeed(double speed) {
        if(speed < 0 && speed > MAXSPEED) {
            throw new IllegalArgumentException("Speed can't be negative");
        }
        this.speed = speed;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    @Override
    public String getTexture() {
        return this.texture;
    }

    @Override
    public void setTexture(String texture) {
        this.texture = texture;
    }
    
}
