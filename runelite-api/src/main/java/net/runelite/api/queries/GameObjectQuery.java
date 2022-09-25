package net.runelite.api.queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.Tile;

public class GameObjectQuery extends TileObjectQuery<GameObject, GameObjectQuery>
{
    @Override
    public LocatableQueryResults<GameObject> result(Client client)
    {
        return new LocatableQueryResults<>(getGameObjects(client).stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .distinct()
                .collect(Collectors.toList()));
    }

    private Collection<GameObject> getGameObjects(Client client)
    {
        Collection<GameObject> objects = new ArrayList<>();
        for (Tile tile : getTiles(client))
        {
            GameObject[] gameObjects = tile.getGameObjects();
            if (gameObjects != null)
            {
                objects.addAll(Arrays.asList(gameObjects));
            }
        }
        return objects;
    }
}
