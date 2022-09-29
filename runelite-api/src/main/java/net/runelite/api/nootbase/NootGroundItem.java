package net.runelite.api.nootbase;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.scene.Position;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NootGroundItem implements Locatable, Interactable {
    private final Game game;
    private final TileItem tileItem;
    private final ItemComposition definition;

    public NootGroundItem(Game game, TileItem tileItem, ItemComposition definition) {
        this.game = game;
        this.tileItem = tileItem;
        this.definition = definition;
    }

    public Game game() {
        return game;
    }

    public Client client() {
        return game.client;
    }

    public Position position() {
        Tile tile = tileItem.getTile();
        return tile != null ? new Position(tileItem.getTile().getWorldLocation()) : null;
    }

    public int id() {
        return tileItem.getId();
    }

    public int quantity() {
        return tileItem.getQuantity();
    }

    public String name() {
        return definition.getName();
    }

    public List<String> actions() {
        return Arrays.stream(definition.getInventoryActions())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void interact(String action) {
        tileItem.interact(action);
        game.sleepDelay();
//        for (int i = 0; i < actions().size(); i++) {
//            if (action.equalsIgnoreCase(actions().get(i))) {
//                interact(i);
//                return;
//            }
//        }
//        throw new IllegalArgumentException("no action \"" + action + "\" on ground item " + id());
    }

    private int getActionId(int action) {
        switch (action) {
            case 0:
                return MenuAction.GROUND_ITEM_FIRST_OPTION.getId();
            case 1:
                return MenuAction.GROUND_ITEM_SECOND_OPTION.getId();
            case 2:
                return MenuAction.GROUND_ITEM_THIRD_OPTION.getId();
            case 3:
                return MenuAction.GROUND_ITEM_FOURTH_OPTION.getId();
            case 4:
                return MenuAction.GROUND_ITEM_FIFTH_OPTION.getId();
            default:
                throw new IllegalArgumentException("action = " + action);
        }
    }

    @Override
    public Point getClickPoint() {
        return null;
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }

    @Override
    public int getActionOpcode(int action) {
        return 0;
    }

    @Override
    public MenuAutomated getMenu(int actionIndex) {
        return null;
    }

    @Override
    public MenuAutomated getMenu(int actionIndex, int opcode) {
        return null;
    }
    //ToDo Check method as it's casting
    public void interact(int action) {
        tileItem.interact(String.valueOf(action));
        game.sleepDelay();
//        game.interactionManager().interact(
//                id(),
//                MenuAction.GROUND_ITEM_THIRD_OPTION.getId(), //TODO configure for other menu actions for ground items
//                tileItem.getTile().getSceneLocation().getX(),
//                tileItem.getTile().getSceneLocation().getY()
//        );
    }

    @Override
    public void interact(int index, int opcode) {

    }

    @Override
    public void interact(int identifier, int opcode, int param0, int param1) {

    }

    public String toString() {
        return name() + " (" + id() + ")" + (quantity() == 1 ? "" : " x" + quantity());
    }

    @Override
    public WorldPoint getWorldLocation() {
        return null;
    }

    @Override
    public LocalPoint getLocalLocation() {
        return null;
    }
}
