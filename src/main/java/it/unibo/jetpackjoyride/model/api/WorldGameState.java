package it.unibo.jetpackjoyride.model.api;

import java.util.List;
import java.util.Set;

import it.unibo.jetpackjoyride.common.Pair;
import it.unibo.jetpackjoyride.model.impl.GameObject;
import it.unibo.jetpackjoyride.model.impl.Money;
import it.unibo.jetpackjoyride.model.impl.PlayerImpl;

/**
 * Interface for the game state and the world.
 * contains the entities and the world, the main statistics of the run
 * and status updates of the entities and the world.
 * 
 * @author mattia.burreli@studio.unibo.it
 */
public interface WorldGameState {

    /**
     * Get the player.
     * 
     * @return the player entity;
     */
    public PlayerImpl getPlayer();

    /**
     * Get money etities.
     * 
     * @return money entities;
     */
    public List<Money> getMoney();

    /**
     * Get all entities.
     * 
     * @return all entities;
     */
    public Set<Pair<String, GameObject>> getWorldEntities();

    /**
     * Get the statistics of the current run.
     * 
     * @return statistics of run
     */
    public Statistics getWorldStatistics();

    /**
     * Update the status of the world and its entities.
     * 
     * @param elapsedTime
     */
    public void updateState(final long elapsedTime);
}