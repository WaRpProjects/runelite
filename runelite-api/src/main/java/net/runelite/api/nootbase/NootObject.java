package net.runelite.api.nootbase;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.scene.Position;
import net.runelite.api.mouse.MouseHandler;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NootObject implements Locatable, Interactable {

    private final Game game;
    public final TileObject tileObject;
    private final ObjectComposition definition;

    @Inject
    private MouseHandler mouse;

    public NootObject(Game game, TileObject tileObject, ObjectComposition definition) {
        this.game = game;
        this.tileObject = tileObject;
        this.definition = definition;
    }


    //	@Override
    public Game game() {
        return game;
    }

    public Client client() {
        return game.client;
    }

    public Position position() {
        return new Position(tileObject.getWorldLocation());
    }

    public LocalPoint localPoint() {
        return tileObject.getLocalLocation();
    }

    public int id() {
        return tileObject.getId();
    }

    public String name() {
        return definition().getName();
    }

    public int orientation() { //TODO untested decorative object orientation removal impacts
        if (tileObject instanceof WallObject) {
            int orientation = ((WallObject) tileObject).getOrientationA();
            if (orientation == 1) return 0;
            if (orientation == 2) return 1;
            if (orientation == 4) return 2;
            if (orientation == 8) return 3;
            throw new AssertionError();
        }

//        if (tileObject instanceof DecorativeObject)
//            return ((DecorativeObject) tileObject).getOrientation();

        return -1;
    }

    public List<String> actions() {
        return Arrays.stream(definition().getActions())
//                .filter(Objects::nonNull) //TODO may need to readd this for non-imposter objects
                .collect(Collectors.toList());
    }

    public ObjectComposition definition() {
        int[] imposter = definition.getImpostorIds();
        if (imposter != null && imposter.length > 0 && definition.getImpostor() != null) {
            return definition.getImpostor();
        }
        return definition;
    }

    public Point menuPoint() {
        if (tileObject instanceof GameObject) {
            GameObject temp = (GameObject) tileObject;
            return temp.getSceneMinLocation();
        }
        return new Point(localPoint().getSceneX(), localPoint().getSceneY());
    }

    @Override
    public void interact(String action) {
        tileObject.interact(action);
        game.sleepDelay();
    }

    private int getActionId(int action) {
        switch (action) {
            case 0:
                return MenuAction.GAME_OBJECT_FIRST_OPTION.getId();
            case 1:
                return MenuAction.GAME_OBJECT_SECOND_OPTION.getId();
            case 2:
                return MenuAction.GAME_OBJECT_THIRD_OPTION.getId();
            case 3:
                return MenuAction.GAME_OBJECT_FOURTH_OPTION.getId();
            case 4:
                return MenuAction.GAME_OBJECT_FIFTH_OPTION.getId();
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
        return definition().getActions();
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

    public void interact(int action) {
        tileObject.interact(action);
        game.sleepDelay();
//        game.interactionManager().interact(id(),
//                getActionId(action),
//                menuPoint().getX(),
//                menuPoint().getY()
//        );
    }

    @Override
    public void interact(int index, int opcode) {

    }

    @Override
    public void interact(int identifier, int opcode, int param0, int param1) {

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
