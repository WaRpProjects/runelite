package net.runelite.client.plugins.moleutil.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    /**
     * Returns random int in range
     * @param min
     * @param max
     * @return
     */
    public int getRandomIntBetweenRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
