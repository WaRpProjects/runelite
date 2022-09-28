package net.runelite.api.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.mouse.MouseUtil;
import net.runelite.api.queries.InventoryItemQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import static net.runelite.api.NootConfig.iterating;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class InventoryUtils {
    @Inject
    private Client client;

    @Inject
    private MouseUtil mouse;

    @Inject
    private MenuUtils menu;

    @Inject
    private BankUtils bank;

    @Inject
    private Sleep sleep;


    @Inject
    LegacyInventoryAssistant legacyInventory;


    public void interactWithItem(int itemID, long delay, String... option) {
        interactWithItem(itemID, false, delay, option);
    }

    public void interactWithItem(int itemID, boolean forceLeftClick, long delay, String... option) {
        interactWithItem(new int[]{itemID}, forceLeftClick, delay, option);
    }

    public void interactWithItem(int[] itemID, long delay, String... option) {
        interactWithItem(itemID, false, delay, option);
    }

    public void interactWithItem(int[] itemID, boolean forceLeftClick, long delay, String... option) {
        List<Integer> boxedIds = Arrays.stream(itemID).boxed().collect(Collectors.toList());
        LegacyMenuEntry entry = legacyInventory.getLegacyMenuEntry(boxedIds, Arrays.asList(option), forceLeftClick);
        if (entry != null) {
            WidgetItem wi = legacyInventory.getWidgetItem(boxedIds);
            if (wi != null)
                mouse.doActionMsTime(entry, wi.getCanvasBounds(), delay);
        }
    }

    public void openInventory() {
        if (client == null || client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        client.runScript(915, 3); //open inventory
    }

    public boolean isFull() {
        return getEmptySlots() <= 0;
    }

    public boolean isEmpty() {
        return getEmptySlots() >= 28;
    }

    public boolean isOpen() {
        if (client.getWidget(WidgetInfo.INVENTORY) == null) {
            return false;
        }
        return !client.getWidget(WidgetInfo.INVENTORY).isHidden();
    }

    public int getEmptySlots() {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            return 28 - legacyInventory.getWidgetItems().size();
        } else {
            return -1;
        }
    }

    public List<WidgetItem> getItems(Collection<Integer> ids) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        List<WidgetItem> matchedItems = new ArrayList<>();

        if (inventoryWidget != null) {
            Collection<WidgetItem> items = legacyInventory.getWidgetItems();
            for (WidgetItem item : items) {
                if (ids.contains(item.getId())) {
                    matchedItems.add(item);
                }
            }
            return matchedItems;
        }
        return null;
    }

    //Requires Inventory visible or returns empty
    public List<WidgetItem> getItems(String itemName) {
        /*return new InventoryWidgetItemQuery()
                .filter(i -> client.getItemDefinition(i.getId())
                        .getName()
                        .toLowerCase()
                        .contains(itemName))
                .result(client)
                .list;*/
        return legacyInventory.getWidgetItems().stream().filter(wi -> wi.getWidget().getName().toLowerCase().contains(itemName.toLowerCase())).collect(Collectors.toList());
    }

    public Collection<WidgetItem> getAllItems() {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            return legacyInventory.getWidgetItems();
        }
        return null;
    }

    public Collection<Integer> getAllItemIDs() {
        Collection<WidgetItem> inventoryItems = getAllItems();
        if (inventoryItems != null) {
            Set<Integer> inventoryIDs = new HashSet<>();
            for (WidgetItem item : inventoryItems) {
                if (inventoryIDs.contains(item.getId())) {
                    continue;
                }
                inventoryIDs.add(item.getId());
            }
            return inventoryIDs;
        }
        return null;
    }

    public List<Item> getAllItemsExcept(List<Integer> exceptIDs) {
        exceptIDs.add(-1); //empty inventory slot
        ItemContainer inventoryContainer = client.getItemContainer(InventoryID.INVENTORY);
        if (inventoryContainer != null) {
            Item[] items = inventoryContainer.getItems();
            List<Item> itemList = new ArrayList<>(Arrays.asList(items));
            itemList.removeIf(item -> exceptIDs.contains(item.getId()));
            return itemList.isEmpty() ? null : itemList;
        }
        return null;
    }

    public WidgetItem getWidgetItem(int id) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<WidgetItem> items = legacyInventory.getWidgetItems();
            for (WidgetItem item : items) {
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    public WidgetItem getWidgetItem(Collection<Integer> ids) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<WidgetItem> items = legacyInventory.getWidgetItems();
            for (WidgetItem item : items) {
                if (ids.contains(item.getId())) {
                    return item;
                }
            }
        }
        return null;
    }

    public Item getItemExcept(List<Integer> exceptIDs) {
        exceptIDs.add(-1); //empty inventory slot
        ItemContainer inventoryContainer = client.getItemContainer(InventoryID.INVENTORY);
        if (inventoryContainer != null) {
            Item[] items = inventoryContainer.getItems();
            List<Item> itemList = new ArrayList<>(Arrays.asList(items));
            itemList.removeIf(item -> exceptIDs.contains(item.getId()));
            return itemList.isEmpty() ? null : itemList.get(0);
        }
        return null;
    }

    public int getItemCount(int id, boolean stackable) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        int total = 0;
        if (inventoryWidget != null) {
            Collection<WidgetItem> items = legacyInventory.getWidgetItems();
            for (WidgetItem item : items) {
                if (item.getId() == id) {
                    if (stackable) {
                        return item.getQuantity();
                    }
                    total++;
                }
            }
        }
        return total;
    }

    public boolean containsItem(int itemID) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }

        return new InventoryItemQuery(InventoryID.INVENTORY)
                .idEquals(itemID)
                .result(client)
                .size() >= 1;
    }

    public boolean containsItem(String itemName) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }

        /*WidgetItem inventoryItem = new InventoryWidgetItemQuery()
                .filter(i -> client.getItemDefinition(i.getId())
                        .getName()
                        .toLowerCase()
                        .contains(itemName))
                .result(client)
                .first();*/

        return legacyInventory.getWidgetItems().stream().anyMatch(wi -> wi.getWidget().getName().contains(itemName));
        //return inventoryItem != null;
    }

    public boolean containsStackAmount(int itemID, int minStackAmount) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        Item item = new InventoryItemQuery(InventoryID.INVENTORY)
                .idEquals(itemID)
                .result(client)
                .first();

        return item != null && item.getQuantity() >= minStackAmount;
    }

    public boolean containsItemAmount(int id, int amount, boolean stackable, boolean exactAmount) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        int total = 0;
        if (inventoryWidget != null) {
            Collection<WidgetItem> items = legacyInventory.getWidgetItems();
            for (WidgetItem item : items) {
                if (item.getId() == id) {
                    if (stackable) {
                        total = item.getQuantity();
                        break;
                    }
                    total++;
                }
            }
        }
        return (!exactAmount || total == amount) && (total >= amount);
    }

    public boolean containsItemAmount(Collection<Integer> ids, int amount, boolean stackable, boolean exactAmount) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        int total = 0;
        if (inventoryWidget != null) {
            Collection<WidgetItem> items = legacyInventory.getWidgetItems();
            for (WidgetItem item : items) {
                if (ids.contains(item.getId())) {
                    if (stackable) {
                        total = item.getQuantity();
                        break;
                    }
                    total++;
                }
            }
        }
        return (!exactAmount || total == amount) && (total >= amount);
    }

    public boolean containsItem(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        return getItems(itemIds).size() > 0;
    }

    public boolean containsAllOf(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        for (int item : itemIds) {
            if (!containsItem(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsExcept(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        Collection<WidgetItem> inventoryItems = getAllItems();
        List<Integer> depositedItems = new ArrayList<>();

        for (WidgetItem item : inventoryItems) {
            if (!itemIds.contains(item.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean onlyContains(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }

        Collection<WidgetItem> inventoryItems = getAllItems();

        for (WidgetItem item : inventoryItems) {
            if (!itemIds.contains(item.getId())) {
                return false;
            }
        }

        return true;
    }

    public void dropItem(WidgetItem item) {

        //menu.setEntry(new LegacyMenuEntry("", "", item.getId(), MenuAction.ITEM_FIFTH_OPTION.getId(), item.getIndex(), 9764864, false));
        menu.setEntry(legacyInventory.getLegacyMenuEntry(item.getId(), "drop"));

        mouse.moveClick(item.getCanvasBounds());
    }
    /*

    public void dropAllExcept(Collection<Integer> ids, boolean dropAll, int minDelayBetween, int maxDelayBetween) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            log.info("can't drop item, bank is open");
            return;
        }
        Collection<WidgetItem> inventoryItems = getAllItems();
        executorService.submit(() ->
        {
            try {
                iterating = true;
                for (WidgetItem item : inventoryItems) {
                    if (ids.contains(item.getId())) {
                        log.debug("not dropping item: " + item.getId());
                        continue;
                    }
                    log.info("Dropping item: {}, at slot: {}", item.getId(), item.getIndex());
                    utils.game.inventory().withSlot(item.getIndex()).withId(item.getId()).first().interact("Drop");
                    utils.game.sleepExact(CalculationUtils.random(minDelayBetween, maxDelayBetween));
                    if (!dropAll) {
                        break;
                    }
                }
                iterating = false;
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
        });
    }

    public void dropInventory(boolean dropAll, int minDelayBetween, int maxDelayBetween) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            log.info("can't drop item, bank is open");
            return;
        }
        Collection<Integer> inventoryItems = getAllItemIDs();
        dropItems(inventoryItems, dropAll, minDelayBetween, maxDelayBetween);
    }



     */
    public void itemsInteract(Collection<Integer> ids, int id, boolean exceptItems, boolean interactAll, int minDelayBetween, int maxDelayBetween) {
        Collection<WidgetItem> inventoryItems = getAllItems();
            try {
                iterating = true;
                for (WidgetItem item : inventoryItems) {
                    if ((!exceptItems && ids.contains(item.getId()) || (exceptItems && !ids.contains(item.getId())))) {
                        log.info("interacting inventory item: {}", item.getId());
                        sleep.sleep(minDelayBetween, maxDelayBetween);
                        menu.setEntry(new LegacyMenuEntry("", "", id, legacyInventory.idToMenuAction(id), item.getIndex(), WidgetInfo.INVENTORY.getId(),
                                true));
                        mouse.moveClick(item.getCanvasBounds());
                        if (!interactAll) {
                            break;
                        }
                    }
                }
                iterating = false;
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
    }

    public void itemsInteract(Collection<Integer> ids, String optionText, boolean exceptItems, boolean interactAll, int minDelayBetween, int maxDelayBetween) {
        Collection<WidgetItem> inventoryItems = getAllItems();
            try {
                iterating = true;
                for (WidgetItem item : inventoryItems) {
                    if ((!exceptItems && ids.contains(item.getId()) || (exceptItems && !ids.contains(item.getId())))) {
                        log.info("interacting inventory item: {}", item.getId());
                        sleep.sleep(minDelayBetween, maxDelayBetween);
                        menu.setEntry(legacyInventory.getLegacyMenuEntry(item.getId(), true, optionText));
                        mouse.moveClick(item.getCanvasBounds());
                        if (!interactAll) {
                            break;
                        }
                    }
                }
                iterating = false;
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
    }


    public void combineItems(Collection<Integer> ids, int item1ID, int opcode, boolean exceptItems, boolean interactAll, int minDelayBetween, int maxDelayBetween) {
        WidgetItem item1 = getWidgetItem(item1ID);
        if (item1 == null) {
            log.info("combine item1 item not found in inventory");
            return;
        }
        Collection<WidgetItem> inventoryItems = getAllItems();
            try {
                iterating = true;
                for (WidgetItem item : inventoryItems) {
                    if ((!exceptItems && ids.contains(item.getId()) || (exceptItems && !ids.contains(item.getId())))) {
                        log.info("interacting inventory item: {}", item.getId());
                        sleep.sleep(minDelayBetween, maxDelayBetween);

                        //menu.setModifiedEntry(new LegacyMenuEntry("", "", item1.getId(), opcode, item1.getIndex(), WidgetInfo.INVENTORY.getId(),
                        //        false), item.getId(), item.getIndex(), MenuAction.ITEM_USE_ON_ITEM.getId());
                        menu.setModifiedEntry(
                                new LegacyMenuEntry("", "", 0, opcode, item1.getIndex(), WidgetInfo.INVENTORY.getId(), false),
                                item.getId(), item.getIndex(), MenuAction.WIDGET_TARGET_ON_WIDGET.getId()
                        );

                        mouse.moveClick(item1.getCanvasBounds());
                        if (!interactAll) {
                            break;
                        }
                    }
                }
                iterating = false;
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
    }
}
