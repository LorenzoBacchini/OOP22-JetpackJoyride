package it.unibo.jetpackjoyride.graphics.impl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;

import it.unibo.jetpackjoyride.input.api.InputQueue;
import it.unibo.jetpackjoyride.input.api.Input.typeInput;
import it.unibo.jetpackjoyride.input.impl.InputImpl;
import it.unibo.jetpackjoyride.model.api.Statistics;

/*
 * Panel for the end of the game
 * @author lorenzo.annibalini@studio.unibo.it
 */

public class EndGamePanel extends JPanel{
    //End Game panels
    private JPanel statisticsPanel = new JPanel(new FlowLayout());
    private JPanel mainPageComands = new JPanel();

    //End Games buttons
    private JButton exit = new JButton("Exit");
    private JButton menu = new JButton("Menu");

     //Title of the main page
     JTextArea title = new JTextArea();
     JPanel titlePanel = new JPanel();

    public EndGamePanel(final InputQueue inputHandler, Statistics statistics) {

        //Main Page layout
        this.setLayout(new BorderLayout());

        //Font of the title
        title.setEditable(false);
        title.setBackground(null);
        title.setFont( new Font("Arial", Font.BOLD, 30));
        titlePanel.add(title, Alignment.CENTER);
        final String welcomText = "End Game !!!";
        title.setText(welcomText);


        //Position of the panels in the mainPage
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(new JPanel(), BorderLayout.EAST);
        this.add(new JPanel(), BorderLayout.WEST);
        this.add(statisticsPanel, BorderLayout.CENTER);
        this.add(mainPageComands, BorderLayout.SOUTH);

        //Statistics panel
        Map<String,Integer> statsMap = statistics.getAll();
        String statsText = "<html>";
        for (String statName : statsMap.keySet()) {
            int value = statsMap.get(statName);
            statsText = statsText + statName + ": " +  value + "<br>";
        }
        statsText = statsText + "</html>";
        JLabel label = new JLabel(statsText);
        statisticsPanel.add(label, BorderLayout.CENTER);

        //settingsPageComand panel
       // mainPageComands.add(settings);
        mainPageComands.add(exit);
        mainPageComands.add(menu);

        //set visible to false
        this.setVisible(false);

        /* ------------------------ ACTION LISTENER -------------------------*/
        exit.addActionListener(e -> inputHandler.addInput(new InputImpl(typeInput.EXIT, "Exit")));
        menu.addActionListener(e -> inputHandler.addInput(new InputImpl(typeInput.MENU, "Menu")));

    }
}