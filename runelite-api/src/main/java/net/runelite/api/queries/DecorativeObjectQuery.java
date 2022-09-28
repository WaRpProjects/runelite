package net.runelite.api.queries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.Tile;

public class DecorativeObjectQuery extends TileObjectQuery<DecorativeObject, DecorativeObjectQuery>
{
    @Override
    public LocatableQueryResults<DecorativeObject> result(Client client)
    {
        return new LocatableQueryResults<>(getDecorativeObjects(client).stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .distinct()
                .collect(Collectors.toList()));
    }

    private Collection<DecorativeObject> getDecorativeObjects(Client client)
    {
        Collection<DecorativeObject> objects = new ArrayList<>();
        for (Tile tile : getTiles(client))
        {
            objects.add(tile.getDecorativeObject());
        }
        return objects;
    }
}
