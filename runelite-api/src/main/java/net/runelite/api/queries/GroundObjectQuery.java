package net.runelite.api.queries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.Tile;

public class GroundObjectQuery extends TileObjectQuery<GroundObject, GroundObjectQuery>
{
    @Override
    public LocatableQueryResults<GroundObject> result(Client client)
    {
        return new LocatableQueryResults<>(getGroundObjects(client).stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .distinct()
                .collect(Collectors.toList()));
    }

    private Collection<GroundObject> getGroundObjects(Client client)
    {
        Collection<GroundObject> objects = new ArrayList<>();
        for (Tile tile : getTiles(client))
        {
            objects.add(tile.getGroundObject());
        }
        return objects;
    }
}
