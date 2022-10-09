package net.runelite.client.plugins.nootwillow;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.utils.MouseUtils;
import net.runelite.api.random.Random;
import net.runelite.api.utils.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.Script;

import javax.inject.Inject;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@PluginDescriptor(
        name = "NootWillow",
        description = "Chops Willows just for testing",
        loadWhenOutdated = true
)
public class NootWillow extends Plugin {

    @Inject
    private NootWillowConfig config;
    @Inject
    private Client client;
    @Inject
    private InventoryUtils inventoryUtils;
    @Inject
    private ObjectUtils objectUtils;
    @Inject
    private MouseUtils mouse;
    @Inject
    private MenuUtils menu;
    @Inject
    private BankUtils bankUtils;
    @Inject
    private PlayerUtils playerUtils;
    @Inject
    private Sleep sleep;
    @Inject
    private Random random;

    LegacyMenuEntry targetMenu;
    private boolean started = false;

    //Script ugly info
    private int timeout = 0;
    private boolean isFull = false;
    private NootWillowState state;
    private GameObject bank;
    private GameObject trees;


    //private int[] tree = new int[]{10832, 10833, 10829, 10819, 10831};
    private int tree = 10822;
    @Override
    public void startUp() throws AWTException {
        log.info("[NootWillow] - Started script.");
    }

    @Provides
    NootWillowConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(NootWillowConfig.class);
    }


    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if(event.getMessage().contains("too full")) {
            isFull = true;
        }
    }
    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getKey().equals("Start")) {
            started = !started;
            log.info("[NootWillow] - Config check: " + started);
        }
    }

    public NootWillowState getState() {
        if( timeout > 0) {
            log.info("[NootWillow] Sleeping for:" + timeout);
            timeout--;
            return NootWillowState.SLEEP;
        }
        if(client.getLocalPlayer().getAnimation() != -1 || client.getLocalPlayer().getWalkAnimation() == 0) {
            log.info("[NootWillow] Animating");
            timeout += 12;
            return NootWillowState.ANIMATING;
        }
        if(isFull && bank != null) {
            log.info("[NootWillow] Banking");
            timeout += 8;
            return NootWillowState.BANKING;
        }
        if(!isFull && trees != null) {
            log.info("[NootWillow] Chopping");
            timeout += 6;
            return NootWillowState.CHOPPING;
        }
    return NootWillowState.SLEEP;

    }


    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event)
    {
        handleClick(event);
        log.debug(event.getMenuOption()+ ", "
                + event.getMenuTarget() + ", "
                + event.getId() + ", "
                + event.getMenuAction().name() + ", "
                + event.getParam0() + ", "
                + event.getParam1());
    }

    private void handleClick(MenuOptionClicked event) {

    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (client.getGameState() == GameState.LOGGED_IN) {
            bank = objectUtils.findNearestBank();
            trees = objectUtils.findNearestGameObject(tree);
            state = getState();

            switch (state) {
                case SLEEP:

                    break;
                case BANKING:
                    if(!bankUtils.isOpen()) {
                        log.info("[NootWillow] - Bank found going there now.");
                        Rectangle clickPoint = (trees.getConvexHull() != null) ? trees.getConvexHull().getBounds() :
                                new Rectangle(client.getCenterX() - 50, client.getCenterY() - 50, 100, 100);
                        log.info("[NootWillow] - Clicking Bank.");
                        mouse.moveClick(clickPoint);
                        timeout += 4;
                    }
                    if (bankUtils.isOpen()) {
                        log.info("[NootWillow] - Deposit all.");
                        bankUtils.depositAll();
                        isFull = false;
                        timeout += 5;
                    }
                    break;
                case CHOPPING:
                    log.info("[NootWillow] - Willow trees found.");
                    targetMenu = new LegacyMenuEntry("", "", trees.getId(), MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), trees.getSceneMinLocation().getX(), trees.getSceneMinLocation().getY(), false);
                    Rectangle clickPoint = (trees.getConvexHull() != null) ? trees.getConvexHull().getBounds() :
                            new Rectangle(client.getCenterX() - 50, client.getCenterY() - 50, 100, 100);
                    log.info("[NootWillow] - Clicking Trees.");
                    mouse.moveClick(clickPoint);
                    break;
                case ANIMATING:

                    break;
            }

        }
    }
}
