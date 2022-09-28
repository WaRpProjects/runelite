package net.runelite.api.items;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.mouse.MouseUtil;
import net.runelite.api.utils.LegacyMenuEntry;
import net.runelite.api.utils.LegacyInventoryAssistant;
import net.runelite.api.widgets.WidgetItem;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings("Unused")
@Slf4j
@Singleton
public class FindItems {

    @Inject
    private Client client;

    @Inject
    LegacyInventoryAssistant legacyInventory;

    @Inject
    MouseUtil mouse;

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
}
