package net.runelite.api.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.mouse.MouseUtil;
import net.runelite.api.random.CalculationUtils;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.random.Calculations;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Slf4j
@Singleton
public class PlayerUtils {
    @Inject
    private Client client;

    @Inject
    private MouseUtil mouse;

    @Inject
    private MenuUtils menu;

    @Inject
    private InventoryUtils inventory;

    @Inject
    private BankUtils bank;

    @Inject
    private CalculationUtils calc;

    private int nextRunEnergy;

    public boolean isMoving() {
        Player player = client.getLocalPlayer();
        if (player == null) {
            return false;
        }
        return player.getIdlePoseAnimation() != player.getPoseAnimation();
    }

    public boolean isMoving(LocalPoint lastTickLocalPoint) {
        return !client.getLocalPlayer().getLocalLocation().equals(lastTickLocalPoint);
    }

    public boolean isInteracting() {
        return isMoving() || client.getLocalPlayer().getAnimation() != -1;
    }

    public boolean isAnimating() {
        return client.getLocalPlayer().getAnimation() != -1;
    }

    public boolean isRunEnabled() {
        return client.getVarpValue(173) == 1;
    }

    //enables run if below given minimum energy with random positive variation
    public void handleRun(int minEnergy, int randMax) {
        assert client.isClientThread();
        if (nextRunEnergy < minEnergy || nextRunEnergy > minEnergy + randMax) {
            nextRunEnergy = calc.getRandomIntBetweenRange(minEnergy, minEnergy + calc.getRandomIntBetweenRange(0, randMax));
        }
        if (client.getEnergy() > nextRunEnergy ||
                client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0) {
            if (drinkStamPot(15 + calc.getRandomIntBetweenRange(0, 30))) {
                return;
            }
            if (!isRunEnabled()) {
                nextRunEnergy = 0;
                Widget runOrb = client.getWidget(WidgetInfo.MINIMAP_RUN_ORB);
                if (runOrb != null) {
                    enableRun(runOrb.getBounds());
                }
            }
        }
    }

    public void handleRun(int minEnergy, int randMax, int potEnergy) {
        assert client.isClientThread();
        if (nextRunEnergy < minEnergy || nextRunEnergy > minEnergy + randMax) {
            nextRunEnergy = calc.getRandomIntBetweenRange(minEnergy, minEnergy + calc.getRandomIntBetweenRange(0, randMax));
        }
        if (client.getEnergy() > (minEnergy + calc.getRandomIntBetweenRange(0, randMax)) ||
                client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0) {
            if (drinkStamPot(potEnergy)) {
                return;
            }
            if (!isRunEnabled()) {
                nextRunEnergy = 0;
                Widget runOrb = client.getWidget(WidgetInfo.MINIMAP_RUN_ORB);
                if (runOrb != null) {
                    enableRun(runOrb.getBounds());
                }
            }
        }
    }

    public void enableRun(Rectangle runOrbBounds) {
        log.info("enabling run");
            menu.setEntry(new LegacyMenuEntry("Toggle Run", "", 1, 57, -1,
                    10485783, false));
            mouse.moveClick(runOrbBounds);
    }

    //Checks if Stamina enhancement is active and if stamina potion is in inventory
    public WidgetItem shouldStamPot(int energy) {

            return null;

    }

    public boolean drinkStamPot(int energy) {
        WidgetItem staminaPotion = shouldStamPot(energy);
        if (staminaPotion != null) {
            log.info("using stamina potion");
            //menu.setEntry(new LegacyMenuEntry("", "", staminaPotion.getId(), MenuAction.ITEM_FIRST_OPTION.getId(),
            //        staminaPotion.getIndex(), 9764864, false));
            //mouse.delayMouseClick(staminaPotion.getCanvasBounds(), calc.getRandomIntBetweenRange(5, 200));
            inventory.interactWithItem(
                    staminaPotion.getId(),
                    calc.getRandomIntBetweenRange(5, 200),
                    "drink"
            );
            return true;
        }
        return false;
    }

    public List<Item> getEquippedItems() {
        assert client.isClientThread();

        List<Item> equipped = new ArrayList<>();
        Item[] items = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
        for (Item item : items) {
            if (item.getId() == -1 || item.getId() == 0) {
                continue;
            }
            equipped.add(item);
        }
        return equipped;
    }

    /*
     *
     * Returns if a specific item is equipped
     *
     * */
    public boolean isItemEquipped(Collection<Integer> itemIds) {
        assert client.isClientThread();

        ItemContainer equipmentContainer = client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipmentContainer != null) {
            Item[] items = equipmentContainer.getItems();
            for (Item item : items) {
                if (itemIds.contains(item.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}