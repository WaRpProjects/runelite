package net.runelite.api.nootbase;

import net.runelite.api.*;
import net.runelite.api.random.Random;
import net.runelite.api.scene.Position;
import net.runelite.api.utils.LegacyInventoryAssistant;
import net.runelite.api.vars.AccountType;
import net.runelite.client.callback.ClientThread;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Game {

    @Inject
    public Client client;

    @Inject
    private ActionQueue action;

    @Inject
    public ClientThread clientThread;

    @Inject
    public Random random;

    @Inject
    public LegacyInventoryAssistant inventoryAssistant;


    public static boolean sleeping = false;
    public static long millisDelay = 0;

    public Client client() {
        return client;
    }

    public ClientThread clientThread() {
        return clientThread;
    }

    public <T> T getFromClientThread(Supplier<T> supplier) {
        if (!client.isClientThread()) {
            CompletableFuture<T> future = new CompletableFuture<>();

            clientThread().invoke(() -> {
                future.complete(supplier.get());
            });
            return future.join();
        } else {
            return supplier.get();
        }
    }

    public long sleepDelay() {
        final long delay = random.getRandomSleepBetweenRange(232, 654);


        return delay;
    }

    public void sleepExact(long time) {
        sleeping = true;
        millisDelay = System.currentTimeMillis() + time;

        if (client.isClientThread()) {
            //log.info("Current time: {}, waiting until: {}", System.currentTimeMillis(), millisDelay);
            return;
        }

        long endTime = System.currentTimeMillis() + time;

        time = endTime - System.currentTimeMillis();

        if (time > 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                sleeping = false;
                throw new RuntimeException(e);
            }
        }
        sleeping = false;
    }

    /*
    *ToDo Make ExecutorService accessible around API
    public <T> T getFromExecutorThread(Supplier<T> supplier) {
        if (client.isClientThread()) {
            CompletableFuture<T> future = new CompletableFuture<>();

            executorService.submit(() -> {
                future.complete(supplier.get());
            });
            return future.join();
        } else {
            return supplier.get();
        }
    }
     */

    public AccountType accountType() {
        return getFromClientThread(() -> client.getAccountType());
    }

    public Position base() {
        return new Position(client.getBaseX(), client.getBaseY(), client.getPlane());
    }

    public boolean inInstance() {
        return client().isInInstancedRegion();
    }


    public int varb(int id) {
        return getFromClientThread(() -> client.getVarbitValue(id));
    }

    public int varp(int id) {
        return getFromClientThread(() -> client.getVarpValue(id));
    }

    public int energy() {
        return client.getEnergy();
    }

    public int experience(Skill skill) {
        return client.getSkillExperience(skill);
    }

    public int modifiedLevel(Skill skill) {
        return client.getBoostedSkillLevel(skill);
    }

    public int baseLevel(Skill skill) {
        return client.getRealSkillLevel(skill);
    }

    public GrandExchangeOffer grandExchangeOffer(int slot) {
        return client.getGrandExchangeOffers()[slot];
    }

    public boolean membersWorld() {
        return client().getWorldType().contains(WorldType.MEMBERS);
    }
}