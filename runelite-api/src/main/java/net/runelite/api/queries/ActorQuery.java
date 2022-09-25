package net.runelite.api.queries;

import java.util.function.Predicate;
import net.runelite.api.Actor;
import net.runelite.api.Locatable;
import net.runelite.api.coords.WorldPoint;

public abstract class ActorQuery<EntityType extends Actor & Locatable, QueryType> extends LocatableQuery<EntityType, QueryType>
{
    @SuppressWarnings("unchecked")
    public QueryType nameEquals(String... names)
    {
        predicate = and(actor ->
        {
            for (String name : names)
            {
                String actorName = actor.getName();
                if (actorName != null && actorName.equals(name))
                {
                    return true;
                }
            }
            return false;
        });
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType nameContains(String... names)
    {
        predicate = and(actor ->
        {
            for (String name : names)
            {
                String actorName = actor.getName();
                if (actorName != null && actorName.contains(name))
                {
                    return true;
                }
            }
            return false;
        });
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType isLevel(int level)
    {
        predicate = and(actor -> actor.getCombatLevel() == level);
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType animationEquals(int animation)
    {
        predicate = and(actor -> actor.getAnimation() == animation);
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType isInteractingWith(Actor actor)
    {
        predicate = and(a -> a.getInteracting().equals(actor));
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType isWithinArea(WorldPoint from, int area)
    {
        predicate = and(a -> a.getWorldArea().distanceTo(from) <= area);
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType hasNoHealthBar()
    {
        predicate = and(a -> a.getHealthRatio() == -1);
        return (QueryType) this;
    }

    @SuppressWarnings("unchecked")
    public QueryType filter(Predicate<EntityType> other)
    {
        predicate = and(other);
        return (QueryType) this;
    }
}
