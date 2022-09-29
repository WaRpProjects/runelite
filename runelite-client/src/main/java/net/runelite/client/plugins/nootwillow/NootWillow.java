package net.runelite.client.plugins.nootwillow;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.mouse.MouseUtil;
import net.runelite.api.script.Script;
import net.runelite.api.utils.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
@PluginDescriptor(
        name = "NootWillow",
        description = "Chops Willows just for testing",
        loadWhenOutdated = true
)
public class NootWillow extends Script {

    @Inject
    private NootWillowConfig config;

    @Inject
    private Client client;
    @Inject
    private InventoryUtils inventoryUtils;
    @Inject
    private ObjectUtils objectUtils;
    @Inject
    private MouseUtil mouse;
    @Inject
    private MenuUtils menu;
    @Inject
    private BankUtils bankUtils;
    @Inject
    private PlayerUtils playerUtils;
    @Inject
    private Sleep sleep;

    private boolean started = false;
    private int[] tree = new int[] {10833, 10829, 10819, 10831};

    @Override
    public void startUp() throws AWTException {
        log.info("[NootWillow] - Started script.");
        execute();
    }
    @Override
    public void onStop() {
        log.info("[NootWillow] - Stopped script.");
    }

    NootWillowConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(NootWillowConfig.class);
    }


    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getKey().equals("Start")) {
            started = !started;
            log.info("[NootWillow] - Config check: " + started);
        }
    }
    @Override
    public void loop() {
        if (client.getGameState() != GameState.LOGGED_IN || !started) {
            log.info("[NootWillow] - Not logged in or started.");
            return;
        }



        if (inventoryUtils.isFull() && !bankUtils.isOpen()) {
            var bank = objectUtils.findNearestBank();
            if (bank != null) {
                log.info("[NootWillow] - Bank found going there now.");
               var targetMenu = new LegacyMenuEntry("", "", bank.getId(),
                        MenuAction.GAME_OBJECT_SECOND_OPTION.getId(), bank.getSceneMinLocation().getX(),
                        bank.getSceneMinLocation().getY(), true);
                menu.setEntry(targetMenu);
                Rectangle r = bank.getConvexHull().getBounds();
                log.info("[NootWillow] - Clicking Bank.");
                mouse.moveClick(r);
                sleep.sleep(234, 934);
            }
            sleep.sleep(234, 934);
        }

        if(inventoryUtils.isFull() && bankUtils.isOpen()) {
            log.info("[NootWillow] - Bank is open emptying logs.");
            bankUtils.depositAll();
            sleep.sleep(1234, 2934);
        }

        if(!inventoryUtils.isFull() && !playerUtils.isAnimating()) {
            var trees = objectUtils.findNearestGameObject(tree);
            if (trees != null) {
                log.info("[NootWillow] - Willow trees found.");
                var targetMenu = new LegacyMenuEntry("", "", trees.getId(),
                        MenuAction.GAME_OBJECT_SECOND_OPTION.getId(), trees.getSceneMinLocation().getX(),
                        trees.getSceneMinLocation().getY(), true);
                menu.setEntry(targetMenu);
                Rectangle r = trees.getConvexHull().getBounds();
                log.info("[NootWillow] - Clicking Trees.");
                mouse.moveClick(r);
                sleep.sleep(1234, 1934);
            }
            log.info("[NootWillow] - Sleeping state.");
            sleep.sleep(432, 2134);
        }
    }

}
