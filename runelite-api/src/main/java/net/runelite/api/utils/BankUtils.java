package net.runelite.api.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.queries.BankItemQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.mouse.MouseUtil;
import net.runelite.api.random.Random;

import static net.runelite.api.NootConfig.iterating;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Singleton
public class BankUtils {
    @Inject
    private Client client;
    @Inject
    private Random random;

    @Inject
    private Sleep sleep;

    @Inject
    private MouseUtil mouse;

    @Inject
    private InventoryUtils inventory;

    @Inject
    private MenuUtils menu;

    private GameEngine clientThread;

    public boolean isDepositBoxOpen() {
        return client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER) != null;
    }

    public boolean isOpen() {
        Widget bankWidget = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        // When you close the bank manually with a hot-key, the widget is still active but hidden.
        return client.getItemContainer(InventoryID.BANK) != null /*&& !bankWidget.isHidden()*/; //TODO handle client thread for isHidden
    }

    public void close() {
        if (!isOpen()) {
            return;
        }
        menu.setEntry(new LegacyMenuEntry("", "", 1, MenuAction.CC_OP.getId(), 11, 786434, false)); //close bank
        Widget bankCloseWidget = client.getWidget(WidgetInfo.BANK_PIN_EXIT_BUTTON);
        if (bankCloseWidget != null) {
            mouse.moveClick(bankCloseWidget.getBounds());
            return;
        }
    }

    public int getBankMenuOpcode(int bankID) {
        return Banks.BANK_CHECK_BOX.contains(bankID) ? MenuAction.GAME_OBJECT_FIRST_OPTION.getId() :
                MenuAction.GAME_OBJECT_SECOND_OPTION.getId();
    }


    //doesn't NPE
    public boolean containsAnyOf(int... ids) {
        if (isOpen()) {
            ItemContainer bankItemContainer = client.getItemContainer(InventoryID.BANK);

            return new BankItemQuery().idEquals(ids).result(client).size() > 0;
        }
        return false;
    }

    public boolean containsAnyOf(Collection<Integer> ids) {
        if (isOpen()) {
            ItemContainer bankItemContainer = client.getItemContainer(InventoryID.BANK);
            for (int id : ids) {
                if (new BankItemQuery().idEquals(ids).result(client).size() > 0) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    public boolean contains(int itemID, int minStackAmount) {
        if (isOpen()) {
            ItemContainer bankItemContainer = client.getItemContainer(InventoryID.BANK);
            final WidgetItem bankItem;
            if (bankItemContainer != null) {
                for (Item item : bankItemContainer.getItems()) {
                    if (item.getId() == itemID) {
                        return item.getQuantity() >= minStackAmount;
                    }
                }
            }
        }
        return false;
    }

    public Widget getBankItemWidget(int id) {
        if (!isOpen()) {
            return null;
        }

        WidgetItem bankItem = new BankItemQuery().idEquals(id).result(client).first();
        if (bankItem != null) {
            return bankItem.getWidget();
        } else {
            return null;
        }
    }

    //doesn't NPE
    public Widget getBankItemWidgetAnyOf(int... ids) {
        if (!isOpen()) {
            return null;
        }

        WidgetItem bankItem = new BankItemQuery().idEquals(ids).result(client).first();
        if (bankItem != null) {
            return bankItem.getWidget();
        } else {
            return null;
        }
    }

    public Widget getBankItemWidgetAnyOf(Collection<Integer> ids) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return null;
        }

        WidgetItem bankItem = new BankItemQuery().idEquals(ids).result(client).first();
        if (bankItem != null) {
            return bankItem.getWidget();
        } else {
            return null;
        }
    }

    public void depositAll() {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
            Widget depositInventoryWidget = client.getWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY);
            if (isDepositBoxOpen()) {
                menu.setEntry(new LegacyMenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1, 12582916, false)); //deposit all in bank interface
            } else {
                menu.setEntry(new LegacyMenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1, 786474, false)); //deposit all in bank interface
            }
            if ((depositInventoryWidget != null)) {
                mouse.moveClick(depositInventoryWidget.getBounds());
            }
    }

    public void depositAllExcept(Collection<Integer> ids) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        Collection<WidgetItem> inventoryItems = inventory.getAllItems();
        List<Integer> depositedItems = new ArrayList<>();

            try {
                iterating = true;
                for (WidgetItem item : inventoryItems) {
                    if (!ids.contains(item.getId()) && item.getId() != 6512 && !depositedItems.contains(item.getId())) //6512 is empty widget slot
                    {
                        log.info("depositing item: " + item.getId());
                        depositAllOfItem(item);
                        sleep.sleep(80, 200);
                        depositedItems.add(item.getId());
                    }
                }
                iterating = false;
                depositedItems.clear();
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
    }

    public void depositAllOfItem(WidgetItem item) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        boolean depositBox = isDepositBoxOpen();
        menu.setEntry(new LegacyMenuEntry("", "", (depositBox) ? 1 : 8, MenuAction.CC_OP.getId(), item.getIndex(),
                (depositBox) ? 12582914 : 983043, false));
        mouse.moveClick(item.getCanvasBounds());
    }

    public void depositAllOfItem(int itemID) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        depositAllOfItem(inventory.getWidgetItem(itemID));
    }

    public void depositAllOfItems(Collection<Integer> itemIDs) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        Collection<WidgetItem> inventoryItems = inventory.getAllItems();
        List<Integer> depositedItems = new ArrayList<>();
            try {
                iterating = true;
                for (WidgetItem item : inventoryItems) {
                    if (itemIDs.contains(item.getId()) && !depositedItems.contains(item.getId())) //6512 is empty widget slot
                    {
                        log.info("depositing item: " + item.getId());
                        depositAllOfItem(item);
                        sleep.sleep(80, 170);
                        depositedItems.add(item.getId());
                    }
                }
                iterating = false;
                depositedItems.clear();
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
    }

    public void depositOneOfItem(WidgetItem item) {
        if (!isOpen() && !isDepositBoxOpen() || item == null) {
            return;
        }
        boolean depositBox = isDepositBoxOpen();

        menu.setEntry(new LegacyMenuEntry("", "", (client.getVarbitValue(6590) == 0) ? 2 : 3, MenuAction.CC_OP.getId(), item.getIndex(),
                (depositBox) ? 12582914 : 983043, false));
        mouse.moveClick(item.getCanvasBounds());
    }

    public void depositOneOfItem(int itemID) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        depositOneOfItem(inventory.getWidgetItem(itemID));
    }

    public void withdrawAllItem(Widget bankItemWidget) {
            LegacyMenuEntry m = new LegacyMenuEntry("Withdraw-All", "", 7, MenuAction.CC_OP.getId(), bankItemWidget.getIndex(), WidgetInfo.BANK_ITEM_CONTAINER.getId(), false);
            mouse.moveClick(new Rectangle(-200, 200));

    }

    public void withdrawAllItem(int bankItemID) {
        Widget item = getBankItemWidget(bankItemID);
        if (item != null) {
            withdrawAllItem(item);
        } else {
            log.debug("Withdraw all item not found.");
        }
    }

    public void withdrawItem(Widget bankItemWidget) {
        LegacyMenuEntry entry = new LegacyMenuEntry("", "", (client.getVarbitValue(6590) == 0) ? 1 : 2, MenuAction.CC_OP.getId(),
                bankItemWidget.getIndex(), WidgetInfo.BANK_ITEM_CONTAINER.getId(), false);
        mouse.doActionClientTick(entry, bankItemWidget.getBounds(), 0);
    }

    public void withdrawItem(int bankItemID) {
        Widget item = getBankItemWidget(bankItemID);
        if (item != null) {
            withdrawItem(item);
        }
    }
}
