package net.runelite.api.queries;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Query;
import net.runelite.api.QueryResults;

@RequiredArgsConstructor
public class InventoryItemQuery extends Query<Item, InventoryItemQuery, QueryResults<Item>>
{
    private final InventoryID inventory;

    @Override
    public QueryResults<Item> result(Client client)
    {
        ItemContainer container = client.getItemContainer(inventory);
        if (container == null)
        {
            return new QueryResults<>(null);
        }
        return new QueryResults<>(Arrays.stream(container.getItems())
                .filter(Objects::nonNull)
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    public InventoryItemQuery idEquals(int... ids)
    {
        predicate = and(item ->
        {
            for (int id : ids)
            {
                if (item.getId() == id)
                {
                    return true;
                }
            }
            return false;
        });
        return this;
    }
}