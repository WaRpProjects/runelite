package net.runelite.client.plugins.nootplugins;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.utils.*;
import net.runelite.api.objects.FindObjects;
import net.runelite.api.mouse.MouseUtil;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.awt.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PluginDescriptor(
        name = "NootPlugin",
        description = "Settings for my private Client",
        loadWhenOutdated = true
)
public class NootPlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private FindObjects findObject;
    @Inject
    private Sleep sleep;
    @Inject BankUtils bankUtils;
    @Inject InventoryUtils inventoryUtils;
    @Inject
    private MenuUtils menu;
    private LegacyMenuEntry targetMenu;
    @Inject
    private NootConfig config;
    @Inject
    private MouseUtil mouse;
    private boolean isRunning = false;
    Robot robot;
    private GameObject test;
    @Provides
    NootConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(NootConfig.class);
    }

    @Override
    public void startUp() throws AWTException {
    robot = new Robot();
    }

    //1515 946
    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (!inventoryUtils.isOpen()) {
            inventoryUtils.openInventory();
        }
        if (inventoryUtils.isOpen()) {
            inventoryUtils.interactWithItem(946, false, 154);
            inventoryUtils.interactWithItem(1515, false, 154);
        }


    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (event.getKey().equals("Test"))
        {
            isRunning = !isRunning;
        }


        if (event.getGroup().equals("NootPlugin"))
        {
            if (test != null)
            {
                log.debug("Found Bank");

                targetMenu = new LegacyMenuEntry("", "", test.getId(),
                        MenuAction.GAME_OBJECT_SECOND_OPTION.getId(), test.getSceneMinLocation().getX(),
                        test.getSceneMinLocation().getY(), true);
                menu.setEntry(targetMenu);
                Rectangle r = test.getConvexHull().getBounds();
                mouse.moveClick(r);

            }
            else
            {
                log.debug("Not found");
            }
        }
    }
}
