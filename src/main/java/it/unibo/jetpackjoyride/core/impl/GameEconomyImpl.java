package it.unibo.jetpackjoyride.core.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.unibo.jetpackjoyride.core.api.GadgetInfoPositions;
import it.unibo.jetpackjoyride.core.api.GameEconomy;
import it.unibo.jetpackjoyride.core.api.Saves;
import it.unibo.jetpackjoyride.core.api.SkinInfoPositions;
import it.unibo.jetpackjoyride.model.api.Gadget;
import it.unibo.jetpackjoyride.model.api.SkinInfo;
import it.unibo.jetpackjoyride.model.api.Statistics;
import it.unibo.jetpackjoyride.model.impl.GadgetImpl;
import it.unibo.jetpackjoyride.model.impl.SkinInfoImpl;
import it.unibo.jetpackjoyride.model.impl.StatisticsImpl;

public class GameEconomyImpl implements GameEconomy {

    /**
     * gadget is the class that contains the map 
     * with the name of the gadget and the gadget information.
     * 
     * gadgetInfo will contain the information of the gadget.
     */
    private Gadget gadget;
    private List<String> gadgetInfo;

    /**
     * skin is the class that contains the map 
     * with the name of the skin and the gadget information.
     * 
     * skinInfo will contain the information of the skin.
     */
    private SkinInfo skin;
    private List<String> skinInfo;

    /**
     * saves is the class to load game general statistic.
     * statics is the class that contains the map 
     * with the name of the statistic and the statistic value.
     */
    private Saves saves;
    private Statistics statistics;

    public GameEconomyImpl() {
        gadget = new GadgetImpl();
        skin = new SkinInfoImpl();
        saves = new SavesImpl();
        statistics = new StatisticsImpl();
    }

    @Override
    public void buyGadget(String name) {
        this.gadgetInfo = gadget.getValue(name);
        try {
            this.statistics.setAll(saves.downloadSaves());
        }catch(IOException e) {
            e.printStackTrace();
        }
        int gadgetPrice = Integer.parseInt(
                                gadgetInfo.get(GadgetInfoPositions.PRICE.ordinal()));
        int ActualMoney = statistics.getValue("ActualMoney"); 
        if (ActualMoney >= gadgetPrice){
            gadgetInfo.set(GadgetInfoPositions.PURCHASED.ordinal(), "true");
            gadget.setValue(name, gadgetInfo);
            statistics.increment("ActualMoney", -gadgetPrice);
            statistics.increment("MoneySpent", gadgetPrice);
            try {
                this.saves.uploadSaves(this.statistics.getAll());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void enableGadget(String name) {
        this.gadgetInfo = gadget.getValue(name);
        if (gadgetInfo.get(GadgetInfoPositions.PURCHASED.ordinal()).equals("true")) {
            gadgetInfo.set(GadgetInfoPositions.STATE.ordinal(), "true");
            gadget.setValue(name, gadgetInfo);
        }
    }

    @Override
    public void disableGadget(String name) {
        this.gadgetInfo = gadget.getValue(name);
        gadgetInfo.set(GadgetInfoPositions.STATE.ordinal(), "false");
        gadget.setValue(name, gadgetInfo);
    }

    @Override
    public void buySkin(String name) {
        this.skinInfo = skin.getValue(name);
        try {
            this.statistics.setAll(saves.downloadSaves());
        }catch(IOException e) {
            e.printStackTrace();
        }
        int skinPrice = Integer.parseInt(
                                skinInfo.get(SkinInfoPositions.PRICE.ordinal()));
        int ActualMoney = statistics.getValue("ActualMoney"); 
        if (ActualMoney >= skinPrice){
            skinInfo.set(SkinInfoPositions.PURCHASED.ordinal(), "true");
            skin.setValue(name, skinInfo);
            statistics.increment("ActualMoney", -skinPrice);
            statistics.increment("MoneySpent", skinPrice);
            try {
                this.saves.uploadSaves(this.statistics.getAll());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void selectSkin(String name) {
        this.skinInfo = skin.getValue(name);
        if (skinInfo.get(SkinInfoPositions.PURCHASED.ordinal()).equals("true")) {
            Map<String, List<String>> allSkin = skin.getAll();

            allSkin.entrySet().stream()
                .filter(e -> 
                    e.getValue().get(SkinInfoPositions.STATE.ordinal()).equals("true"))
                .forEach(e -> {
                    e.getValue().set(SkinInfoPositions.STATE.ordinal(), "false");
                    skin.setValue(e.getKey(), e.getValue());
                });
            skinInfo.set(GadgetInfoPositions.STATE.ordinal(), "true");
            skin.setValue(name, skinInfo);
        }
    }

}