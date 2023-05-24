package it.unibo.jetpackjoyride.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import it.unibo.jetpackjoyride.common.Point2d;
import it.unibo.jetpackjoyride.common.Vector2d;
import it.unibo.jetpackjoyride.model.impl.GameObject;
import it.unibo.jetpackjoyride.model.impl.HitboxImpl;

public class HitboxImplTest {
    Point2d positionPlayer = new Point2d(20, 350);
    HitboxImpl hitboxPlayer = new HitboxImpl(30, 15, positionPlayer);
    private static final int X_PLAYER=30;
    private static final int Y_PLAYER=15;

    /**
     * Test for the checkCollision method.
     * This test check if the entity is in collision with the player in every frontal position.
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testHitboxCollision() throws IOException, ParseException {
        Point2d positionEntity = new Point2d(20, 380);
        long dt = 2;
        int y=0;
        HitboxImpl hitbox = new HitboxImpl(X_PLAYER, Y_PLAYER, positionEntity);
        GameObject entity = new GameObject(positionEntity, new Vector2d(this.positionPlayer, positionEntity), hitbox);

        while (y>=-X_PLAYER) {
            entity.updateState(dt);
            entity.getHitbox().updateHitbox(entity.getCurrentPos());
            if (entity.getHitbox().checkCollision(hitboxPlayer)) {
                y--;
                entity.setPos(new Point2d(positionEntity.x, positionEntity.y + y));
                break;
            } else if (entity.getCurrentPos().x < 0) {
                assertTrue(false);
                break;
            }
        }
        assertTrue(true);
    }

}
