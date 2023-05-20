package it.unibo.jetpackjoyride.model.impl;

import java.util.HashMap;
import java.util.Map;
import it.unibo.jetpackjoyride.model.api.Statistics;

/**
 * Class to modelize the statisstics of the game.
 * 
 * @author emanuele.sanchi@studio.unibo.it
 */
public class StatisticsImpl implements Statistics {

    private Map<String, Integer> statistics = new HashMap<>();

    @Override
    public int getValue(String name) {
        return this.statistics.get(name);
    }

    @Override
    public void setValue(String name, int value) {
        this.statistics.replace(name, value);
    }

    @Override
    public void increment(String name, int value) {
        this.statistics.replace(name, this.statistics.get(name) + value);
    }

    @Override
    public void increment(String name) {
        this.statistics.replace(name, this.statistics.get(name) + 1);
    }

    @Override
    public Map<String, Integer> getAll() {
        return this.statistics;
    }

    @Override
    public void addStatistic(String name, int value) {
        this.statistics.put(name, value);
    }

    @Override
    public void setAll(Map<String, Integer> stats) {
        this.statistics.putAll(stats);
    }

    @Override
    public void updateGeneralStats(Statistics stats) {
        Map<String, Integer> runStats = stats.getAll();
        for (String key : runStats.keySet()) {
            this.increment(key, runStats.get(key));
        }
        if(stats.getAll().get("Money") > statistics.get("MaxMoney")) {
            this.setValue("MaxMoney", stats.getAll().get("Money"));
        }
        if(stats.getAll().get("Meters") > statistics.get("MaxMeters")) {
            this.setValue("MaxMeters", stats.getAll().get("Meters"));
        }
    }

}
