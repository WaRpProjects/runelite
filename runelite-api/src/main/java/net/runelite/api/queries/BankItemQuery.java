package net.runelite.api.queries;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.QueryResults;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

public class BankItemQuery extends WidgetItemQuery
{
    private static final int ITEM_EMPTY = 6512;

    @Override
    public QueryResults<WidgetItem> result(Client client)
    {
        Collection<WidgetItem> widgetItems = getBankItems(client);
        return new QueryResults<>(widgetItems.stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    private Collection<WidgetItem> getBankItems(Client client)
    {
        Collection<WidgetItem> widgetItems = new ArrayList<>();
        Widget bank = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        if (bank != null && !bank.isHidden())
        {
            Widget[] children = bank.getDynamicChildren();
            for (int i = 0; i < children.length; i++)
            {
                Widget child = children[i];
                if (child.getItemId() == ITEM_EMPTY || child.isSelfHidden())
                {
                    continue;
                }
                // set bounds to same size as default inventory
                Rectangle bounds = child.getBounds();
                bounds.setBounds(bounds.x - 1, bounds.y - 1, 32, 32);
                // Index is set to 0 because the widget's index does not correlate to the order in the bank
                widgetItems.add(new WidgetItem(child.getItemId(), child.getItemQuantity(), 0, bounds, child, null));
            }
        }
        return widgetItems;
    }
}