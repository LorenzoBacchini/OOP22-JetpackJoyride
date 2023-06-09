package it.unibo.jetpackjoyride.graphics.impl;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jetpackjoyride.model.api.Gadget;
import it.unibo.jetpackjoyride.model.api.GadgetInfoPositions;
import it.unibo.jetpackjoyride.model.impl.GadgetImpl;
import it.unibo.jetpackjoyride.model.api.SkinInfo;
import it.unibo.jetpackjoyride.model.api.SkinInfoPositions;
import it.unibo.jetpackjoyride.model.api.Statistics;
import it.unibo.jetpackjoyride.model.impl.SkinInfoImpl;
import it.unibo.jetpackjoyride.model.impl.StatisticsImpl;
import it.unibo.jetpackjoyride.input.api.InputQueue;
import it.unibo.jetpackjoyride.input.api.Input.TypeInput;
import it.unibo.jetpackjoyride.input.impl.InputImpl;

/**
 * Class that represents the panel of the shop.
 * 
 * @author lorenzo.bacchini4@studio.unibo.it
 */
public class ShopPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient InputQueue inputQueue;
    private final transient Gadget gadget;
    private final transient SkinInfo skinInfo;
    private final JLabel moneyLabel;
    private final Map<String, ArrayList<JButton>> buttonMapGadget;
    private final Map<String, ArrayList<JButton>> buttonMapSkin;
    private final transient SpriteLoader spriteLoader;
    private final transient Statistics generalStatistics;
    private static final float FONTSIZE = 20;
    private static final String ENABLE = "Enable";
    private static final String DISABLE = "Disable";
    private static final String PURCHASED = "Purchased";
    private final Font font;

    /**
     * Constructor for the ShopPanel.
     * 
     * @param inputQueue
     * @param generalStatistics
     * @param font
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "generalStatistics and inputQueue must be the same object")
    public ShopPanel(final InputQueue inputQueue, final Statistics generalStatistics, final Font font) {
        super();
        this.inputQueue = inputQueue;
        this.spriteLoader = new SpriteLoader();
        this.generalStatistics = generalStatistics;
        this.font = font;
        this.buttonMapGadget = new HashMap<>();
        this.buttonMapSkin = new HashMap<>();
        this.setLayout(new BorderLayout());
        final JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        this.moneyLabel = new JLabel("YourMoney: " + this.getActualMoney());
        this.moneyLabel.setFont(font);
        this.add(moneyLabel, BorderLayout.LINE_START);
        final JLabel gadgetLabel = new JLabel("Gadget");
        gadgetLabel.setFont(font);
        boxPanel.add(gadgetLabel);
        this.add(boxPanel, BorderLayout.NORTH);

        JButton menu;
        menu = new JButton("Menu");
        menu.setFont(font);
        menu.addActionListener(e -> {
            this.inputQueue.addInput(new InputImpl(TypeInput.MENU, null));
        });
        this.add(menu, BorderLayout.SOUTH);

        gadget = new GadgetImpl();
        for (final String name : gadget.getAll().keySet()) {
            final String state = gadget.getValue(name).get(GadgetInfoPositions.STATE.ordinal());
            final String purchased = gadget.getValue(name).get(GadgetInfoPositions.PURCHASED.ordinal());
            final String price = gadget.getValue(name).get(GadgetInfoPositions.PRICE.ordinal());
            final String description = gadget.getValue(name).get(GadgetInfoPositions.DESCRIPTION.ordinal());

            final JPanel flowPanel = new JPanel(new FlowLayout());
            final JLabel nameLabel = new JLabel(name);
            final JLabel priceLabel = new JLabel(price);
            final JLabel descriptionLabel = new JLabel(description);
            nameLabel.setFont(font.deriveFont(FONTSIZE));
            priceLabel.setFont(font.deriveFont(FONTSIZE));
            descriptionLabel.setFont(font.deriveFont(FONTSIZE));
            flowPanel.add(nameLabel);
            flowPanel.add(priceLabel);
            flowPanel.add(descriptionLabel);
            final JButton enableButton = createGadgetButton(Boolean.parseBoolean(state) ? DISABLE : ENABLE,
                    true, name);
            final JButton purchasedButton = createGadgetButton(PURCHASED, !Boolean.parseBoolean(purchased), name);
            buttonMapGadget.put(name, new ArrayList<>(List.of(enableButton, purchasedButton)));
            flowPanel.add(purchasedButton);
            flowPanel.add(enableButton);
            boxPanel.add(flowPanel);
        }

        final JLabel skinLabel = new JLabel("Skin");
        skinLabel.setFont(font);
        boxPanel.add(skinLabel);
        skinInfo = new SkinInfoImpl();
        for (final String name : skinInfo.getAll().keySet()) {
            final String state = skinInfo.getValue(name).get(SkinInfoPositions.STATE.ordinal());
            final String purchased = skinInfo.getValue(name).get(SkinInfoPositions.PURCHASED.ordinal());
            final String price = skinInfo.getValue(name).get(SkinInfoPositions.PRICE.ordinal());

            final JPanel flowPanel = new JPanel(new FlowLayout());
            final JLabel nameLabel = new JLabel(name);
            final JLabel priceLabel = new JLabel(price);
            nameLabel.setFont(font.deriveFont(FONTSIZE));
            priceLabel.setFont(font.deriveFont(FONTSIZE));
            flowPanel.add(nameLabel);
            flowPanel.add(priceLabel);
            final JButton enableButton = createSkinButton(ENABLE, !Boolean.parseBoolean(state), name);
            final JButton purchasedButton = createSkinButton(PURCHASED, !Boolean.parseBoolean(purchased), name);
            buttonMapSkin.put(name, new ArrayList<>(List.of(enableButton, purchasedButton)));
            flowPanel.add(loadSpriteImage(name));
            flowPanel.add(purchasedButton);
            flowPanel.add(enableButton);
            boxPanel.add(flowPanel);
        }
    }

    /**
     * method to return only the statistic
     * that represent the actual money of the player.
     * 
     * @return the actual money of the player
     */
    private int getActualMoney() {
        return this.generalStatistics.getValue(StatisticsImpl.ACTUAL_MONEY);
    }

    /**
     * This method load the sprite image from the spriteLoader.
     * 
     * @param name of the sprite to load
     * @return the JLabel with the sprite image
     */
    private JLabel loadSpriteImage(final String name) {
        final Sprite skinSprite = spriteLoader.getSprites().get(name);
        skinSprite.scale();
        final Image skinImage = skinSprite.getScaled();
        return new JLabel(new ImageIcon(skinImage));
    }

    /**
     * This method create a button for the gadget.
     * 
     * @param text    of the button
     * @param enabled state of the gadget
     * @param name    of the gadget
     * @return the Jbutton created
     */
    private JButton createGadgetButton(final String text, final boolean enabled, final String name) {
        final JButton button = new JButton(text);
        button.setFont(font.deriveFont(FONTSIZE));
        button.setEnabled(enabled);
        button.addActionListener(e -> {
            switch (button.getText()) {
                case ENABLE:
                    this.inputQueue.addInput(new InputImpl(TypeInput.ENABLE, name));
                    break;
                case DISABLE:
                    this.inputQueue.addInput(new InputImpl(TypeInput.DISABLE, name));
                    break;
                case PURCHASED:
                    this.inputQueue.addInput(new InputImpl(TypeInput.BUY, name));
                    break;
                default:
                    break;
            }
        });
        return button;
    }

    /**
     * This method create a button for the gadget.
     * 
     * @param text    of the button
     * @param enabled state of the gadget
     * @param name    of the gadget
     * @return the JButton created
     */
    private JButton createSkinButton(final String text, final boolean enabled, final String name) {
        final JButton button = new JButton(text);
        button.setFont(font.deriveFont(FONTSIZE));
        button.setEnabled(enabled);
        button.addActionListener(e -> {
            switch (button.getText()) {
                case ENABLE:
                    this.inputQueue.addInput(new InputImpl(TypeInput.SELECT_SKIN, name));
                    break;
                case PURCHASED:
                    this.inputQueue.addInput(new InputImpl(TypeInput.BUY_SKIN, name));
                    break;
                default:
                    break;
            }
        });
        return button;
    }

    /**
     * This method update the panel recalulating the button state
     * by reading new states from the gadget values and skin values.
     */
    public void update() {
        for (final Map.Entry<String, ArrayList<JButton>> entry : buttonMapGadget.entrySet()) {
            final ArrayList<JButton> buttonList = buttonMapGadget.get(entry.getKey());
            final String purchased = gadget.getValue(entry.getKey()).get(GadgetInfoPositions.PURCHASED.ordinal());
            final String state = Boolean
                    .parseBoolean(gadget.getValue(entry.getKey()).get(GadgetInfoPositions.STATE.ordinal())) ? DISABLE
                            : ENABLE;
            buttonList.get(GadgetInfoPositions.PURCHASED.ordinal()).setEnabled(!Boolean.parseBoolean(purchased));
            buttonList.get(GadgetInfoPositions.STATE.ordinal()).setText(state);
        }
        for (final Map.Entry<String, ArrayList<JButton>> entry : buttonMapSkin.entrySet()) {
            final ArrayList<JButton> buttonList = buttonMapSkin.get(entry.getKey());
            final String purchased = skinInfo.getValue(entry.getKey()).get(SkinInfoPositions.PURCHASED.ordinal());
            final String state = skinInfo.getValue(entry.getKey()).get(SkinInfoPositions.STATE.ordinal());
            buttonList.get(SkinInfoPositions.PURCHASED.ordinal()).setEnabled(!Boolean.parseBoolean(purchased));
            buttonList.get(SkinInfoPositions.STATE.ordinal()).setEnabled(!Boolean.parseBoolean(state));
        }
        this.moneyLabel.setText("YourMoney: " + this.getActualMoney());
    }

}
