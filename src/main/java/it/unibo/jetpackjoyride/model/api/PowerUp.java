package it.unibo.jetpackjoyride.model.api;

public interface PowerUp {
    /**
     * Generic PowerUp interface
     * @author: lorenzo.annibalini@studio.unibo.it
     */

    public static enum PowerUpType { SHIELD, SPEED, JETPACK, COIN, NONE }

    /**
     * active the powerup
     */
    public void active();

    /**
     * disable the powerup
     */
    public void disable();

    /**
     * @return true if the powerup is active
     */
    public boolean isActive();

    /**
     * @return the type of the powerup
     */
    public PowerUpType getType();

    /**
     * set the cost in coin of the powerup
     */
    public void setCost(int cost);

    /**
     * @return the cost in coin of the powerup
     */
    public int getCost();

    /**
     * Set the duration of the powerup
     */
    public void setDuration(int duration);

    /**
     * @return the duration of the powerup
     */
    public int getDuration();
}
