package net.runelite.client.plugins.nootplugins;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.utils.*;
import net.runelite.api.objects.FindObjects;
import net.runelite.api.utils.MouseUtils;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.awt.*;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Script;

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
    private MouseUtils mouse;
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
