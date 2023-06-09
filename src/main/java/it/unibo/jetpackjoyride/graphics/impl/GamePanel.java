package it.unibo.jetpackjoyride.graphics.impl;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.parser.ParseException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import it.unibo.jetpackjoyride.common.Pair;
import it.unibo.jetpackjoyride.core.impl.SliderImpl;
import it.unibo.jetpackjoyride.model.api.Direction;
import it.unibo.jetpackjoyride.model.api.Orientation;
import it.unibo.jetpackjoyride.model.api.Scientist;
import it.unibo.jetpackjoyride.model.api.SkinInfo;
import it.unibo.jetpackjoyride.model.api.SkinInfoPositions;
import it.unibo.jetpackjoyride.model.api.GameObject;
import it.unibo.jetpackjoyride.model.impl.PlayerImpl;
import it.unibo.jetpackjoyride.model.impl.SkinInfoImpl;
import it.unibo.jetpackjoyride.model.impl.StatisticsImpl;
import it.unibo.jetpackjoyride.model.impl.Money;
import it.unibo.jetpackjoyride.model.impl.Electrode;
import it.unibo.jetpackjoyride.model.impl.LaserRay;
import it.unibo.jetpackjoyride.model.impl.Rocket;

/**
 * Class of the panel's game. Used to visualize map of game and sprites.
 * 
 * @author emanuele.sanchi@studio.unibo.it
 */
public final class GamePanel extends JPanel {

    private transient Set<Pair<String, GameObject>> entities;
    private transient PlayerImpl player;
    private transient List<Money> money;
    private final int posImage1;
    private final int posImage2;
    private final transient Image backgruondImage1;
    private final transient Image backgruondImage2;
    private final transient Image rocket;
    private final transient Image warning;
    private final transient Image vertElectrode;
    private final transient Image horElectrode;
    private final transient Image shield;
    private final transient Image speedup;
    private final transient Image rightScientist;
    private final transient Image leftScientist;
    private final transient Image laser;
    private final transient Image barry;
    private final transient Image barryWoman;
    private final transient Image barryUp;
    private final transient Image barryWomanUp;
    private transient Image playerImage;
    private transient Image playerUpImage;
    private final transient Image moneyImage;
    private final transient SliderImpl slider;
    private int score;
    private int monies;
    private static final String FILENAME = "/config/sprites.json";
    private final JLabel scoreLabel;
    private final JLabel moneyLabel;
    private static final float FONT_SIZE = 15f;
    private static final String BACKGROUND = "background1";
    private static final long serialVersionUID = 1L;
    /**
     * Constant for the label's width.
     */
    private static final int LABEL_WIDTH = 100;
    /**
     * Constant for the label's height.
     */
    private static final int LABEL_HEIGHT = 20;
    /**
     * Constant for the shield's stroke.
     */
    private static final int SHIELD_STROKE = 5;
    /**
     * Constant for the shield's radius.
     */
    private static final int SHIELD_RADIUS = 70;
    /**
     * Constant for the shield's extremes.
     */
    private static final Pair<Integer, Integer> LASER_EXTREMES = new Pair<>(-10, 1150);

    /**
     * Constructor of the class.
     * 
     * @param font the font for labels and buttons
     * @throws ParseException
     * @throws IOException
     */
    public GamePanel(final Font font) throws ParseException, IOException {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        final SpriteLoader spriteLoader = new SpriteLoader();
        spriteLoader.loadSprites(FILENAME);
        final Map<String, Sprite> sprites = spriteLoader.getSpritesScaled();
        // loading background image
        backgruondImage1 = sprites.get(BACKGROUND).getScaled();
        backgruondImage2 = sprites.get(BACKGROUND).getScaled();
        final int width = sprites.get(BACKGROUND).getScaledlDim().getX();
        final int height = sprites.get(BACKGROUND).getScaledlDim().getY();
        slider = new SliderImpl(width);
        // loading sprite images and adjust sizes
        rocket = sprites.get("rocket").getScaled();
        warning = sprites.get("warning").getScaled();
        vertElectrode = sprites.get("vElectrode").getScaled();
        horElectrode = sprites.get("hElectrode").getScaled();
        shield = sprites.get("shield").getScaled();
        speedup = sprites.get("speedup").getScaled();
        rightScientist = sprites.get("rightScientist").getScaled();
        leftScientist = sprites.get("leftScientist").getScaled();
        laser = sprites.get("laser").getScaled();
        barry = sprites.get("barry").getScaled();
        barryUp = sprites.get("barryUp").getScaled();
        barryWoman = sprites.get("barryWoman").getScaled();
        barryWomanUp = sprites.get("barryWomanUp").getScaled();
        moneyImage = sprites.get("money").getScaled();
        // Stats labels
        this.scoreLabel = new JLabel("Score: " + score);
        this.moneyLabel = new JLabel("Monies: " + monies);
        this.scoreLabel.setFont(font.deriveFont(FONT_SIZE));
        this.moneyLabel.setFont(font.deriveFont(FONT_SIZE));
        final Icon moneyIcon = new ImageIcon(moneyImage);
        this.moneyLabel.setIcon(moneyIcon);
        this.scoreLabel.setSize(LABEL_WIDTH, LABEL_HEIGHT);
        this.moneyLabel.setSize(LABEL_WIDTH, LABEL_HEIGHT);
        moneyLabel.setAlignmentX(LEFT_ALIGNMENT);
        moneyLabel.setAlignmentY(LEFT_ALIGNMENT);
        scoreLabel.setAlignmentX(LEFT_ALIGNMENT);
        scoreLabel.setAlignmentY(LEFT_ALIGNMENT);
        // Add components to panel and set size
        this.add(moneyLabel);
        this.add(scoreLabel);
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(this.getPreferredSize());
        this.setVisible(false);
        this.posImage1 = 0;
        this.posImage2 = width;
        this.slider.start();
    }

    @SuppressFBWarnings(value = "BC_UNCONFIRMED_CAST",
    justification = "The cast is safe because is always in a switch case and it's sure that the object is what i'm casting to")
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        // Update stats
        final StatisticsImpl currentStats = this.player.getStatistics();
        this.monies = currentStats.getValue(StatisticsImpl.GRABBED_MONEY);
        this.score = currentStats.getValue(StatisticsImpl.TOTAL_METERS);
        // Update labels
        this.moneyLabel.setText("Monies: " + this.monies);
        this.scoreLabel.setText("Score: " + this.score);
        // Draw background image
        g2d.drawImage(backgruondImage1, this.posImage1 - slider.getPos(), 0, this);
        g2d.drawImage(backgruondImage2, this.posImage2 - slider.getPos(), 0, this);
        // Draw entities
        final Iterator<Pair<String, GameObject>> it = this.entities.iterator();
        // for (final Pair<String, GameObject> el : entities) {
        while (it.hasNext()) {
            final Pair<String, GameObject> el = it.next();
            final String entityName = el.getX();
            final GameObject entity = el.getY();
            switch (entityName) {
                case "Rocket":
                    if (!((Rocket) entity).isActive()) {
                        this.drawSprite(g2d, warning, entity);
                    } else {
                        this.drawSprite(g2d, rocket, entity);
                    }
                    break;
                case "Electrode":
                    if (((Electrode) entity).getOrientation() == Orientation.HORIZONTAL) {
                        this.drawSprite(g2d, horElectrode, entity);
                    } else {
                        this.drawSprite(g2d, vertElectrode, entity);
                    }
                    break;
                case "Scientist":
                    if (((Scientist) entity).getDirection() == Direction.RIGHT) {
                        this.drawSprite(g2d, rightScientist, entity);
                    } else {
                        this.drawSprite(g2d, leftScientist, entity);
                    }
                    break;
                case "ShieldPowerUp":
                    this.drawSprite(g2d, shield, entity);
                    break;
                case "SpeedUpPowerup":
                    this.drawSprite(g2d, speedup, entity);
                    break;
                case "Laser":
                    if (!((LaserRay) entity).isActive()) {
                        this.drawSprite(g2d, laser, entity);
                    } else {
                        this.drawSprite(g2d, laser, entity);
                        g2d.setStroke(new BasicStroke(4f));
                        g2d.setColor(Color.RED);
                        g2d.drawLine(0, (int) entity.getCurrentPos().getY(), this.getWidth(),
                                (int) entity.getCurrentPos().getY());
                    }
                    break;
                case "Nothing":
                    break;
                default:
                    break;
            }

        }

        // Draw player
        if (player.getCurrentVel().getY() > 0) {
            this.drawSprite(g2d, playerImage, player);
        } else {
            this.drawSprite(g2d, playerUpImage, player);
        }
        // Draw shield if present
        if (player.getHearts() == 2) {
            g2d.setStroke(new BasicStroke(SHIELD_STROKE));
            g2d.setColor(Color.GREEN);
            g2d.drawOval((int) player.getHitbox().getPointUpLeft().getX() - 10,
                    (int) player.getHitbox().getPointUpLeft().getY() - 10, SHIELD_RADIUS, SHIELD_RADIUS);
        }

        // Draw monies if present
        if (!money.isEmpty()) {
            for (final Money m : money) {
                this.drawSprite(g2d, moneyImage, m);
            }
        }
    }

    /**
     * Metohd to draw a sprite.
     * 
     * @param g      graphics object
     * @param image  image to draw
     * @param entity entity object with values to draw
     */
    private void drawSprite(final Graphics2D g, final Image image, final GameObject entity) {
        // x and y are the coordinates of the upper left corner of the image based on
        // hitbox
        final int x = (int) entity.getHitbox().getPointUpLeft().getX();
        final int y = (int) entity.getHitbox().getPointUpLeft().getY();
        if ("it.unibo.jetpackjoyride.model.impl.Money".equals(entity.getClass().getName())) {
            g.drawImage(image, x, y, this);
        } else {
            if ("it.unibo.jetpackjoyride.model.impl.LaserRay".equals(entity.getClass().getName())) {
                g.drawImage(image, LASER_EXTREMES.getX(), y, this);
                g.drawImage(image, LASER_EXTREMES.getY(), y, this);
            } else {
                g.drawImage(image, x, y, this);

            }

        }
    }

    /**
     * Method to set new values for entities.
     * 
     * @param entities entities to draw
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", 
                        justification = "This method is used to set the entities on start and the object is always the same")
    public void setEntities(final Set<Pair<String, GameObject>> entities) {
        this.entities = entities;
    }

    /**
     * Method to set new value for the player.
     * 
     * @param player player to draw
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
                        justification = "This method is used to set the player object on start and the object is always the same")
    public void setPlayer(final PlayerImpl player) {
        this.player = player;
        // Load the right image for the player based on the skin
        final SkinInfo skinInfo = new SkinInfoImpl();
        final String skin = skinInfo.getAll().entrySet().stream()
                .filter(x -> "true".equals(x.getValue().get(SkinInfoPositions.STATE.ordinal()))).findAny().get()
                .getKey();
        this.playerImage = "barry".equals(skin) ? this.barry : this.barryWoman;
        this.playerUpImage = "barry".equals(skin) ? this.barryUp : this.barryWomanUp;
    }

    /**
     * Method to set new value for money.
     * 
     * @param money money to draw
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
                        justification = "This method is used to set the monies on start and the object is always the same")
    public void setMoney(final List<Money> money) {
        // this.money.clear();
        this.money = money;
    }

}
