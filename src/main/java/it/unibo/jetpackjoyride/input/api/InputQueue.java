package it.unibo.jetpackjoyride.input.api;

import java.util.LinkedList;

/**
 * Interface for the input queue.
 * @author mattia.burreli@studio.unibo.it
 */
public interface InputQueue {
    
    /**
     * Add an input to the queue.
     * @param input the input to add
     */
     public void addInput(final Input input);
    
    /**
     * Get a copy of imput queue.
     * @return the first input
     */
    public LinkedList<Input> getInputQueue();
    
    /**
     * Check if the queue is empty.
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty();

}
