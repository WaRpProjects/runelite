package net.runelite.api.random;

import net.runelite.api.Client;
import net.runelite.api.Point;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    @Inject
    private Client client;

    protected static final java.util.Random random = new java.util.Random();


    /**
     * Returns random int in range
     * @param min
     * @param max
     * @return
     */
    public int getRandomIntBetweenRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Returns random sleep time
     * @param min
     * @param max
     * @return
     */
    public int getRandomSleepBetweenRange(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max + 1); }

    /**
     * Returns a random number
     * @param min
     * @param max
     * @return
     */
    public int getRandomNumber(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max + 1); }

}
