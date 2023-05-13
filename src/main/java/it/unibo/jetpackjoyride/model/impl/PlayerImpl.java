package it.unibo.jetpackjoyride.model.impl;

import java.util.List;
import java.util.Map;

import it.unibo.jetpackjoyride.common.Point2d;
import it.unibo.jetpackjoyride.common.Vector2d;
import it.unibo.jetpackjoyride.model.api.Gadget;
import it.unibo.jetpackjoyride.model.api.Hitbox;
import it.unibo.jetpackjoyride.model.api.Player;
import it.unibo.jetpackjoyride.core.api.GadgetInfoPositions;

/**
 * This is a class to model a generic player.
 * 
 * @author mattia.burreli@studio.unibo.it
 */
public class PlayerImpl extends GameObject implements Player {

    private boolean statusPlayer;
    private int hearts;
    private PlayerDirection direction;
    private final int UP_VELOCITY = -4;
    private final int DOWN_VELOCITY = 4;

    /**
     * constructor to create a player.
     * 
     * @param pos
     * @param vel
     */
    public PlayerImpl(Point2d pos, Vector2d vel, Hitbox hitbox) {
        super(pos, vel, hitbox);
        this.hearts = 1;
        this.setPlayerAlive();
        this.direction = PlayerDirection.STATIC;
    }

    @Override
    public boolean getStatusPlayer() {
        return statusPlayer;
    }

    /**
     * Function for set the status of player to false.
     */
    private void setPlayerDeath() {
        this.statusPlayer = false;
    }

    /**
     * Function for set the status of player to true.
     */
    private void setPlayerAlive() {
        this.statusPlayer = true;
    }

    @Override
    public void addHeart() {
        this.hearts++;
    }

    @Override
    public void removeHeart() {
        if (this.hearts > 0) {
            this.hearts--;
        } else {
            this.setPlayerDeath();
        }

    }

    @Override
    public void setDirectionUP() {
        this.direction = PlayerDirection.UP;
        double multiplier = this.applyGadget(direction);
        this.setVel(new Vector2d(this.getCurrentPos().x, this.getCurrentPos().y+UP_VELOCITY*multiplier));
    }

    @Override
    public void setDirectionDOWN() {
        this.direction = PlayerDirection.DOWN;
        double multiplier = this.applyGadget(direction);
        this.setVel(new Vector2d(this.getCurrentPos().x, this.getCurrentPos().y+DOWN_VELOCITY*multiplier));
    }

    @Override
    public void setDirectionSTATIC() {
        this.direction = PlayerDirection.STATIC;
        this.setVel(new Vector2d(this.getCurrentPos().x, this.getCurrentPos().y));
    }

    @Override
    public PlayerDirection getDirection() {
        return this.direction;
    }

    private double applyGadget(PlayerDirection direction){
        Gadget gadget = new GadgetImpl();
        Map<String, List<String>> gadgets = gadget.getAll();
        for (String name : gadgets.keySet()) {
            if ((gadgets.get(name).get(GadgetInfoPositions.STATE.ordinal())).equals("true")){
                switch(name){
                    case "Air Barry":
                        if(direction == PlayerDirection.UP){
                            return 1.3;
                        }
                    break;
                    case "Gravity Belt":
                        if(direction == PlayerDirection.DOWN){
                            return 1.3;
                        }
                    break;
                    default:
                    break;
                }
            }
        }
        return 1;
    }
}
