package it.unibo.jetpackjoyride.model.impl;

import it.unibo.jetpackjoyride.common.*;
import it.unibo.jetpackjoyride.model.api.Hitbox;

/*
 * Class that represents a shield power up
 * @author lorenzo.annibalini@studio.unibo.it
 */

public class ShieldPowerUpImpl extends GameObject {

    private boolean active;
    private final long duration;
    private long startTime;

    public ShieldPowerUpImpl(final long duration, final Point2d pos, final Vector2d vel, final Hitbox hitbox) {
        super(pos, vel, hitbox);
        this.duration = duration;
        this.active = false;
        this.startTime = 0;
    }

    public boolean isActive() {
        if (this.active) {
            if (System.currentTimeMillis() - this.startTime > this.duration) {
                this.active = false;
            }
        }
        return this.active;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setIsActive(final boolean active) {
        this.active = active;
        this.startTime = System.currentTimeMillis();
    }
     
}