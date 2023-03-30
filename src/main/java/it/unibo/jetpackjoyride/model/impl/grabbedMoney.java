package it.unibo.jetpackjoyride.model.impl;

/**
 * Class to count number of grabbedMoney.
 * 
 * @author emanuele.sanchi@studio.unibo.it
 * 
 */
public class GrabbedMoney extends StatisticsImpl {

    public GrabbedMoney(int value, String name) {
        super(value, name);
    }

    @Override
    public void increment() {
    }

    @Override
    public void setValue(int amount) {
        super.value+=amount;
    }

}
