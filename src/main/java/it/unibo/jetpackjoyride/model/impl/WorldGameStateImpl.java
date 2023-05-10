package it.unibo.jetpackjoyride.model.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.w3c.dom.Entity;

import it.unibo.jetpackjoyride.common.Pair;
import it.unibo.jetpackjoyride.common.Point2d;
import it.unibo.jetpackjoyride.common.Vector2d;
import it.unibo.jetpackjoyride.model.api.EntitiesGenerator;
import it.unibo.jetpackjoyride.model.api.Statistics;
import it.unibo.jetpackjoyride.model.api.WorldGameState;
import it.unibo.jetpackjoyride.model.api.Player.PlayerDirection;
import it.unibo.jetpackjoyride.core.api.MoneyPatternLoader;
import it.unibo.jetpackjoyride.core.impl.MoneyPatternLoaderImpl;

public class WorldGameStateImpl implements WorldGameState {

    private static final int FRAME_HEIGHT = 1800;
    private static final int ENTITIES_NUMBER = 3;
    private static final int SCIENTIST_NUMBER = 2;
    private static final int START_NUMBER_DECIDER = 0;
    private static final int SPEED_MOLTIPLICATOR = 2;
    private static final int TIME_TO_WAIT = 5000;
    private static final int TIME_TO_WAIT_POWER_UP = 4000;
    private static final int TIME_TO_WAIT_LASER = 6000;
    private static final int SPEED_POWERUP_DISTANCE = 1000;
    private Statistics runStatistics;
    private EntitiesGenerator entitiesGenerator;
    private PlayerImpl player;
    private List<Money> money;
    private Set<Pair<String, GameObject>> entities;
    private long previousCycleStartTime;
    private MoneyPatternLoader moneyPatternLoader;
    private Random random;
    private int deciderEntitiesGenerator; // 0-2 = entities, 3 = money, 4 = laser

    public WorldGameStateImpl() {
        this.inizializeWorldGameState();
    }

    @Override
    public void updateState(final long elapsedTime) {
        this.checkBoardPlayerCollision();
        this.updateTimeLaser();
        this.updateEntities(elapsedTime);
        this.entitiesGarbage();
        this.checkPlayerCollision();
        if (this.player.getStatusPlayer()) {
            this.newEntities();
            this.runStatistics.increment("score");
        } else {
            this.notifyEndGame();
        }

    }

    /**
     * Create new entities in the world.
     */
    private void newEntities() {
        long currentCycleStartTime = System.currentTimeMillis();
        if (this.entities.size() - this.entities.stream()
                .filter(entity -> entity.getX().matches("Scientist") || entity.getX().matches("Nothing"))
                .count() <= ENTITIES_NUMBER && currentCycleStartTime - this.previousCycleStartTime >= TIME_TO_WAIT
                && this.deciderEntitiesGenerator >= 0 && this.deciderEntitiesGenerator <= 2) {
            this.entitiesGenerator.generateEntity(this.entities, random.nextInt(2) + 2);
            this.entities = this.entitiesGenerator.getEntities();
            this.previousCycleStartTime = currentCycleStartTime;
            this.deciderEntitiesGenerator = random.nextInt(5);
        } else if (currentCycleStartTime - this.previousCycleStartTime >= TIME_TO_WAIT
                && this.deciderEntitiesGenerator == 3) {
            try {
                this.money = moneyPatternLoader.getMoneyPattern();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.previousCycleStartTime = currentCycleStartTime;
            this.deciderEntitiesGenerator = random.nextInt(5);
        } else if (currentCycleStartTime - this.previousCycleStartTime >= TIME_TO_WAIT
                && this.deciderEntitiesGenerator == 4 && this.entities.size() == 0) {
            this.entitiesGenerator.generateLaser(this.entities, random.nextInt(4));
            this.entities = this.entitiesGenerator.getEntities();
            this.previousCycleStartTime = currentCycleStartTime;
        }

        if (this.entities.stream()
                .filter(entity -> entity.getX().matches("Scientist"))
                .count() == 0 && this.deciderEntitiesGenerator != 4) {
            this.entitiesGenerator.generateScientists(entities, SCIENTIST_NUMBER);
            this.entities = this.entitiesGenerator.getEntities();
        }
    }

    /**
     * Check if the player is colliding with an entity or a money.
     * If collide with an entity check the type of the entity and do the right
     * action. If collide with a money add the money to the player's money.
     * For all the entities that are not colliding with the player they are
     * eliminated.
     */
    private void checkPlayerCollision() {
        Iterator<Pair<String, GameObject>> entityIterator = this.entities.iterator();
        Iterator<Money> moneyIterator = this.money.iterator();
        while (entityIterator.hasNext()) {
            Pair<String, GameObject> entity = entityIterator.next();
            if (this.player.getHitbox().checkCollision(entity.getY().getHitbox())) {
                switch (entity.getX()) {
                    case "Rocket":
                        this.player.removeHeart();
                        this.entities.remove(entity);
                        break;
                    case "Electrode":
                        this.player.removeHeart();
                        this.entities.remove(entity);
                        break;
                    case "SpeedPowerUp":
                        this.runStatistics.increment("score", SPEED_POWERUP_DISTANCE);
                        this.entities.remove(entity);
                        break;
                    case "ShieldPowerUp":
                        this.player.addHeart();
                        this.entities.remove(entity);
                        break;
                    case "Laser":
                        this.player.removeHeart();
                        this.entities.remove(entity);
                        break;
                    case "Scientist":
                        this.entities.remove(entity);
                        break;
                    case "Nothing":
                        break;
                    default:
                        throw new IllegalArgumentException("The type of Entity is NULL or is incorrect.");
                }

            }
        }
        while (moneyIterator.hasNext()) {
            Money moneyElem = moneyIterator.next();
            if (this.player.getHitbox().checkCollision(moneyElem.getHitbox())) {
                this.runStatistics.increment("money");
                this.money.remove(moneyElem);
            }
        }

    }

    /**
     * Check if the player is colliding with the upper board or with the lower
     * board.
     */
    private void checkBoardPlayerCollision() {
        if (Math.abs((this.player.getHitbox().getHeigthHitbox() / 2) - this.player.getHitbox().getPointUpLeft().y) <= 0
                && this.player.getDirection() == PlayerDirection.UP) {
            this.player.setDirectionSTATIC();
        } else if (Math.abs((this.player.getHitbox().getHeigthHitbox() / 2)
                + this.player.getHitbox().getPointUpLeft().y) >= FRAME_HEIGHT
                && this.player.getDirection() == PlayerDirection.DOWN) {
            this.player.setDirectionSTATIC();
        }

    }

    /**
     * Method to check if an entity is out of visible range and so has to be
     * deleted.
     */
    private void entitiesGarbage() {
        Iterator<Pair<String, GameObject>> entityIterator = this.entities.iterator();
        Iterator<Money> moneyIterator = this.money.iterator();
        while (entityIterator.hasNext()) {
            Pair<String, GameObject> entity = entityIterator.next();
            if (entity.getY().getCurrentPos().x < 0) {
                this.entities.remove(entity);
            }
        }

        while (moneyIterator.hasNext()) {
            Money moneyElem = moneyIterator.next();
            if (moneyElem.getCurrentPos().x < 0) {
                this.money.remove(moneyElem);
            }
        }

    }

    /**
     * Initialize the world state, setting the player, the statistics and the
     * entities.
     */
    private void inizializeWorldGameState() {
        this.runStatistics = new StatisticsImpl();
        this.entitiesGenerator = new EntitiesGeneratorImpl();
        this.entities = new HashSet<>();
        this.money = new ArrayList<>();
        this.moneyPatternLoader = new MoneyPatternLoaderImpl();
        this.random = new Random();
        this.runStatistics.addStatistic("money", 0);
        this.runStatistics.addStatistic("score", 0);
        this.player = new PlayerImpl(new Point2d(50, 350), new Vector2d(50, 350),
                new HitboxImpl(15, 10, new Point2d(50, 350)));
        this.entitiesGenerator.generateEntity(this.entities, ENTITIES_NUMBER);
        this.entitiesGenerator.generateScientists(this.entities, SCIENTIST_NUMBER);
        this.entities = this.entitiesGenerator.getEntities();
        this.deciderEntitiesGenerator = START_NUMBER_DECIDER;
    }

    /**
     * Update the state of all the entities in the world.
     * 
     * @param elapsedTime
     */
    private void updateEntities(final long elapsedTime) {
        this.entities.stream().forEach(entity -> entity.getY().updateState(elapsedTime));
        this.player.updateState(elapsedTime);
        this.money.stream().forEach(moneyElem -> moneyElem.updateState(elapsedTime));
    }

    /**
     * Notify that the game is ended at the game engine.
     */
    private void notifyEndGame() {

    }

    /**
     * Update the time entities. If the time of the entity is ended the entity will
     * change its state or doing something related to the entity.
     */
    private void updateTimeLaser() {
        Iterator<Pair<String, GameObject>> entityIterator = this.entities.iterator();
        if (this.deciderEntitiesGenerator == 4
                && this.entities.stream().filter(entity -> entity.getX().equals("Laser")).count() != 0) {
            while (entityIterator.hasNext()) {
                LaserRay laserRay = (LaserRay) entityIterator.next().getY();
                laserRay.updateState(ENTITIES_NUMBER);
                if (laserRay.isEnded()) {
                    this.entities.remove(laserRay);
                    this.deciderEntitiesGenerator = random.nextInt(5);
                }

            }
        }

    }

    @Override
    public PlayerImpl getPlayer() {
        return this.player;
    }

    @Override
    public List<Money> getMoney() {
        return this.money;
    }

    @Override
    public Set<Pair<String, GameObject>> getWorldEntities() {
        return this.entities;
    }

    @Override
    public Statistics getWorldStatistics() {
        return this.runStatistics;
    }

    @Override
    public void newGame() {
        this.inizializeWorldGameState();
    }

}
