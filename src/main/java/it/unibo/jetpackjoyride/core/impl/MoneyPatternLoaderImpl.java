package it.unibo.jetpackjoyride.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import it.unibo.jetpackjoyride.common.Point2d;
import it.unibo.jetpackjoyride.common.Vector2d;
import it.unibo.jetpackjoyride.core.api.MoneyPatternLoader;
import it.unibo.jetpackjoyride.model.api.Hitbox;
import it.unibo.jetpackjoyride.model.impl.HitboxImpl;
import it.unibo.jetpackjoyride.model.impl.Money;

/**
 * A class to load money from file.
 * 
 * @author lorenzo.bacchini4@studio.unibo.it
 */

public class MoneyPatternLoaderImpl implements MoneyPatternLoader {

    /* Order to read data from nextLine(). */
    private final int X = 0;
    private final int Y = 1;
    private static final int LIMIT = 395;
    /*
     * Attribute that count the number of available pattern file.
     * N.B the number of available file must be the same of the number
     * of file money_.txt in the resources folder.
     */
    private int availableFile = 4;
    private int minAvailableFile = 1;
    private String fileNumber;
    private final String filename = "money";
    // Range to change the y coordinate of the money.
    private final int RANGE = 150;
    // Random to multiply the range.
    private final int MINRANDOM = -1;
    private final int MAXRANDOM = 1;

    /**
     * Constructor of the class MoneyPatternLoader.
     * Normal Constructor, the programmer set the minAvailableFile
     * and the availableFile.
     * 
     * @throws IllegalArgumentException if the number of available file
     *                                  is less than the minimum number of available
     *                                  file.
     */
    public MoneyPatternLoaderImpl() {
        if (this.availableFile < this.minAvailableFile) {
            throw new IllegalArgumentException(
                    "The number of available file is less than the minimum number of available file.");
        }
    }

    /**
     * Constructor of the class MoneyPatternLoader.
     * Specific Constructor to set the number of the
     * unique(1 file) available file.
     * 
     * @param num the number of the available file.
     */
    public MoneyPatternLoaderImpl(final int num) {
        this.minAvailableFile = this.availableFile = num;
    }

    public ArrayList<Money> getMoneyPattern() throws IOException {
        this.fileNumber = Integer.toString((int) Math.floor(Math.random()
                * (this.availableFile - this.minAvailableFile + 1)
                + this.minAvailableFile));

        final String name = filename + fileNumber + ".txt";
        ReadWriteFile<Integer,Integer> reader = new ReadWriteFile<>(name);


        final ArrayList<Money> moneyList = new ArrayList<>();
        final double multiplier = Math.floor(Math.random() * (MAXRANDOM - MINRANDOM) + MINRANDOM);
        while (filePattern.hasNextLine()) {
            final String line = filePattern.nextLine();
            final int x = Integer.parseInt(line.split(",")[X]);
            int y = Integer.parseInt(line.split(",")[Y]);
            y = y + (int) (multiplier * RANGE);
            final Point2d startPosition = new Point2d(x, y);
            final Point2d finishPosition = new Point2d(x - LIMIT, startPosition.y);
            final Vector2d vec = new Vector2d(finishPosition, startPosition);
            final Hitbox hitbox = new HitboxImpl(15, 15, startPosition);
            moneyList.add(new Money(startPosition, vec, hitbox));
        }
        filePattern.close();
        return moneyList;
    }
}
