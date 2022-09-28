package net.runelite.api.utils;

import net.runelite.api.random.Random;

import javax.inject.Inject;

public class Sleep {

    @Inject
    private Random random;

    /**
     * Pauses execution for a random amount of time between two values.
     *
     * @param minSleep The minimum time to sleep.
     * @param maxSleep The maximum time to sleep.
     * @see #sleep(int)
     */
    public void sleep(int minSleep, int maxSleep) {
        sleep(random.getRandomSleepBetweenRange(minSleep, maxSleep));
    }

    /**
     * Pauses execution for a given number of milliseconds
     * @param toSleep The time to sleep in milliseconds.
     */
    public void sleep(int toSleep) {
        try {
            long start = System.currentTimeMillis();
            Thread.sleep(toSleep);

            // Guarantee minimum sleep
            long now;
            while (start + toSleep > (now = System.currentTimeMillis())) {
                Thread.sleep(start + toSleep - now);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pauses execution for a given number of milliseconds
     * @param toSleep The time to sleep in milliseconds.
     */
    public void sleep(long toSleep) {
        try {
            long start = System.currentTimeMillis();
            Thread.sleep(toSleep);

            // Guarantee minimum sleep
            long now;
            while (start + toSleep > (now = System.currentTimeMillis())) {
                Thread.sleep(start + toSleep - now);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
