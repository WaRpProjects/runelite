package net.runelite.api.random;

import net.runelite.api.Client;
import net.runelite.api.Point;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    @Inject
    private Client client;

    protected static final java.util.Random random = new java.util.Random();



    public int getRandomIntBetweenRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public int getRandomSleepBetweenRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
