package it.unibo.jetpackjoyride.graphics.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import it.unibo.jetpackjoyride.graphics.api.View;
import it.unibo.jetpackjoyride.model.impl.WorldGameStateImpl;


/*
 * Implements the View interface of the game.
 * 
 * @author lorenzo.annibalini@studio.unibo.it
 */

public class ViewImpl implements View {

    //TODO: Add Shop, Game and Statistics panels
    
    private final JFrame frame;
    private final GamePanel game;
    private final MenuPanel menuPanel;
    //private final EndGamePanel endGame;
    //private final ShopPanel shop;
    private final StatisticsPanel statistics;

    public ViewImpl(final WorldGameStateImpl worldGameState) {
        this.frame = new JFrame("Jetpack Joyride");
        this.game = new GamePanel(worldGameState.getGameObjects(), worldGameState.getPlayer(), worldGameState.getMoney());
        this.menuPanel = new MenuPanel();
        //this.shop = new ShopPanel();
        this.statistics = new StatisticsPanel(worldGameState.getStatistics());
       
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(this.menuPanel.getPreferredSize());
        this.frame.setMinimumSize(this.menuPanel.getPreferredSize());
        this.frame.pack();
        this.frame.getContentPane().add(this.menuPanel);
        this.frame.setVisible(true);
    }

    @Override
    public void renderGame() {
        this.game.repaint();
    }

    @Override
    public void renderMenu() {
        this.menuPanel.repaint();
    }

    @Override
    public void renderShop() {
       // this.shop.repaint();
    }

    @Override
    public void renderEndGame() {
        //TODO: cosa devo fare qui? Chiamo statistics ?
    }

    @Override
    public void renderStatistics() {
        this.statistics.repaint();
    }
    
}
