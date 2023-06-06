package it.unibo.jetpackjoyride.graphics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import it.unibo.jetpackjoyride.graphics.impl.SpriteLoader;

/**
 * JUnit test for {@link SpriteLoader}.
 * 
 * @author emanuele.sanchi@studio.unibo.it
 */
class SpriteLoaderTest {
    private final SpriteLoader sl;

    SpriteLoaderTest() {
        sl = new SpriteLoader();
    }

    @Test
    void testLoadSprites() throws ParseException, IOException {
        sl.loadSprites("sprites.json");
        assertEquals(7, sl.getSprites().size());

    }
}
