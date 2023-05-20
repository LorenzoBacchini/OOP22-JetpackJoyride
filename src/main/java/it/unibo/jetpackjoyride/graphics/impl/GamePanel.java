package it.unibo.jetpackjoyride.graphics.impl;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.parser.ParseException;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unibo.jetpackjoyride.common.Pair;
import it.unibo.jetpackjoyride.core.api.SkinInfoPositions;
import it.unibo.jetpackjoyride.core.api.Slider;
import it.unibo.jetpackjoyride.core.impl.SliderImpl;
import it.unibo.jetpackjoyride.model.api.Direction;
import it.unibo.jetpackjoyride.model.api.Orientation;
import it.unibo.jetpackjoyride.model.api.Scientist;
import it.unibo.jetpackjoyride.model.api.SkinInfo;
import it.unibo.jetpackjoyride.model.impl.GameObject;
import it.unibo.jetpackjoyride.model.impl.PlayerImpl;
import it.unibo.jetpackjoyride.model.impl.SkinInfoImpl;
import it.unibo.jetpackjoyride.model.impl.StatisticsImpl;
import it.unibo.jetpackjoyride.model.impl.Money;
import it.unibo.jetpackjoyride.model.impl.Electrode;
import it.unibo.jetpackjoyride.model.impl.LaserRay;

/**
 * Class of the panel's game. Used to visualize map of game and sprites.
 * 
 * @author emanuele.sanchi@studio.unibo.it
 */
public class GamePanel extends JPanel {

    private Set<Pair<String, GameObject>> entities;
    private PlayerImpl player;
    private List<Money> money = new ArrayList<>();
    private int posImage1;
    private int posImage2;
    private Image backgruondImage1;
    private Image backgruondImage2;
    private Image rocket;
    private Image vertElectrode;
    private Image horElectrode;
    private Image shield;
    private Image speedup;
    private Image rightScientist;
    private Image leftScientist;
    private Image laser;
    private Image barry;
    private Image barryWoman;
    private Image playerImage;
    private Image moneyImage;
    private SliderImpl slider;
    private int width;
    private int height;
    private int score = 0;
    private int monies = 0;
    private static final String filename = "/config/sprites.json";
    private JLabel scoreLabel;
    private JLabel moneyLabel;

    /**
     * Constructor of the class.
     * 
     * @param entities the set of entities to show
     * @param player   the object of the player
     * @param money    the list of money that has to be shown
     * @throws ParseException
     */
    public GamePanel() throws ParseException {
        SpriteLoader spriteLoader = new SpriteLoader();
        spriteLoader.loadSprites(filename);
        Map<String, Sprite> sprites = spriteLoader.getSpritesScaled();
        // loading background image
        backgruondImage1 = sprites.get("background1").getScaled();
        backgruondImage2 = sprites.get("background1").getScaled();
        this.width = sprites.get("background1").getScaledlDim().getX();
        this.height = sprites.get("background1").getScaledlDim().getY();
        slider = new SliderImpl(this.width);
        // loading sprite images and adjust sizes
        rocket = sprites.get("rocket").getScaled();
        vertElectrode = sprites.get("vElectrode").getScaled();
        horElectrode = sprites.get("hElectrode").getScaled();
        shield = sprites.get("shield").getScaled();
        speedup = sprites.get("speedup").getScaled();
        rightScientist = sprites.get("rightScientist").getScaled();
        leftScientist = sprites.get("leftScientist").getScaled();
        laser = sprites.get("laser").getScaled();
        barry = sprites.get("barry").getScaled();
        barryWoman = sprites.get("barryWoman").getScaled();
        moneyImage = sprites.get("money").getScaled();
        // Stats labels
        this.scoreLabel = new JLabel("Score: " + score);
        this.moneyLabel = new JLabel("Monies: " + monies);
        Icon moneyIcon = new ImageIcon(moneyImage);
        this.moneyLabel.setIcon(moneyIcon);
        this.scoreLabel.setSize(100, 20);
        this.moneyLabel.setSize(100, 20);
        this.add(moneyLabel);
        this.add(scoreLabel);
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setSize(this.getPreferredSize());
        this.setVisible(false);
        this.posImage1 = 0;
        this.posImage2 = this.width;
        this.slider.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g = (Graphics2D) g;
        super.paintComponent(g);
        // Update stats
        StatisticsImpl currentStats = this.player.getStatistics();
        this.monies = currentStats.getValue("GrabbedMoney");
        this.score = currentStats.getValue("TotalMeters");
        // Update labels
        this.moneyLabel.setText("Monies: " + this.monies);
        this.scoreLabel.setText("Score: " + this.score);
        // Draw background image
        g.drawImage(backgruondImage1, this.posImage1 - slider.getPos(), 0, this);
        g.drawImage(backgruondImage2, this.posImage2 - slider.getPos(), 0, this);
        // Draw entities
        for (Pair<String, GameObject> el : entities) {
            String entityName = el.getX();
            GameObject entity = el.getY();
            switch (entityName) {
                case "Rocket":
                    this.drawSprite(g, rocket, entity);
                    break;
                case "Electrode":
                    if (((Electrode) entity).getOrientation() == Orientation.HORIZONTAL) {
                        this.drawSprite(g, vertElectrode, entity);
                    } else {
                        this.drawSprite(g, horElectrode, entity);
                    }
                    break;
                case "Scientist":
                    if (((Scientist) entity).getDirection() == Direction.RIGHT) {
                        this.drawSprite(g, rightScientist, entity);
                    } else {
                        this.drawSprite(g, leftScientist, entity);
                    }
                    break;
                case "ShieldPowerUp":
                    this.drawSprite(g, shield, entity);
                    break;
                case "SpeedUpPowerup":
                    this.drawSprite(g, speedup, entity);
                    break;
                case "Laser":
                    System.out.println("Laser");
                    if (!((LaserRay) entity).isActive()) {
                        this.drawSprite(g, laser, entity);
                    } else {
                        this.drawSprite(g, laser, entity);
                        ((Graphics2D) g).setStroke(new BasicStroke(25));
                        g.setColor(Color.RED);
                        ((Graphics2D) g).setStroke(new BasicStroke(4f));
                        g.drawLine(0, (int) entity.getCurrentPos().y + 15, this.getWidth(), (int) entity.getCurrentPos().y + 15);
                    }
                    break;
                case "Nothing":
                    break;
                default:
                    break;
            }

        }

        // Draw player
        this.drawSprite(g, playerImage, player);
        if (player.getHearts() == 2) {
            ((Graphics2D) g).setStroke(new BasicStroke(10));
            g.setColor(Color.GREEN);
            g.drawOval((int)player.getCurrentPos().x - 10, (int)player.getCurrentPos().y - 10, 70, 70);
        }

        // Draw monies if present
        if (!money.isEmpty()) {
            for (Money m : money) {
                this.drawSprite(g, moneyImage, m);
            }
        }
    }

    /**
     * Metohd to draw a sprite.
     * 
     * @param g      graphics object
     * @param sprite image to draw
     * @param entity entity object with values to draw
     */
    private void drawSprite(Graphics g, Image image, GameObject entity) {
        if (entity.getClass().getName() == "it.unibo.jetpackjoyride.model.impl.Money") {
            g.drawImage(image, ((int) entity.getCurrentPos().x), (int) entity.getCurrentPos().y,
                    this);
        } else {
            if (entity.getClass().getName() == "it.unibo.jetpackjoyride.model.impl.LaserRay") {
                g.drawImage(image, 0, (int) entity.getCurrentPos().y, this);
                g.drawImage(image, 1150, (int) entity.getCurrentPos().y, this);
            } else {
                g.drawImage(image, (int) entity.getCurrentPos().x, (int) entity.getCurrentPos().y, this);

            }

        }
    }

    /**
     * Method to set new values for entities.
     * 
     * @param entities entities to draw
     */
    public void setEntities(final Set<Pair<String, GameObject>> entities) {
        this.entities = entities;
    }

    /**
     * Method to set new value for the player.
     * 
     * @param player player to draw
     */
    public void setPlayer(final PlayerImpl player) {
        this.player = player;
        // Load the right image for the player based on the skin
        SkinInfo skinInfo = new SkinInfoImpl();
        String skin = skinInfo.getAll().entrySet().stream()
                .filter(x -> "true".equals(x.getValue().get(SkinInfoPositions.STATE.ordinal()))).findAny().get()
                .getKey();
        this.playerImage = "barry".equals(skin) ? this.barry : this.barryWoman;
    }

    /**
     * Method to set new value for money.
     * 
     * @param money money to draw
     */
    public void setMoney(final List<Money> money) {
        // this.money.clear();
        this.money = money;
    }

}
