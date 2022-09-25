package net.runelite.api.queries;

import static java.lang.Math.abs;
import java.util.function.Predicate;
import net.runelite.api.Locatable;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.Query;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public abstract class LocatableQuery<EntityType extends Locatable, QueryType> extends Query<EntityType, QueryType, LocatableQueryResults<EntityType>>
{
    @SuppressWarnings("unchecked")
    public QueryType atWorldLocation(WorldPoint location)
    {
        predicate = and(object -> object.getWorldLocation().equals(location));
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType atLocalLocation(LocalPoint location)
    {
        predicate = and(object -> object.getLocalLocation().equals(location));
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType isWithinDistance(LocalPoint to, int distance)
    {
        predicate = and(a -> a.getLocalLocation().distanceTo(to) <= distance);
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType isWithinDistance(WorldPoint to, int distance)
    {
        predicate = and(a -> a.getWorldLocation().distanceTo(to) <= distance);
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType isWithinArea(LocalPoint from, int area)
    {
        predicate = and(a ->
        {
            LocalPoint localLocation = a.getLocalLocation();
            return abs(localLocation.getX() - from.getX()) < area
                    && abs(localLocation.getY() - from.getY()) < area;
        });
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType filter(Predicate<EntityType> other)
    {
        predicate = and(other);
        return (QueryType) this;
    }
}
