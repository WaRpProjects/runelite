package net.runelite.api.queries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;

public class WallObjectQuery extends TileObjectQuery<WallObject, WallObjectQuery>
{
    @Override
    public LocatableQueryResults<WallObject> result(Client client)
    {
        return new LocatableQueryResults<>(getWallObjects(client).stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .distinct()
                .collect(Collectors.toList()));
    }

    private Collection<WallObject> getWallObjects(Client client)
    {
        Collection<WallObject> objects = new ArrayList<>();
        for (Tile tile : getTiles(client))
        {
            objects.add(tile.getWallObject());
        }
        return objects;
    }
}
