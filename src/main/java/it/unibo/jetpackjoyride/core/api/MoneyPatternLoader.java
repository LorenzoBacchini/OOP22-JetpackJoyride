package it.unibo.jetpackjoyride.core.api;

import java.io.IOException;
import java.util.ArrayList;
import it.unibo.jetpackjoyride.model.impl.Money;

/**
 * Interface to load money pattern from file.
 * 
 * @author lorenzo.bacchini4@studio.unibo.it
 */
public interface MoneyPatternLoader {

    /**
     * Method to get the money pattern from file.
     * 
     * @return an array list of Money
     */
    ArrayList<Money> getMoneyPattern() throws IOException;
}
