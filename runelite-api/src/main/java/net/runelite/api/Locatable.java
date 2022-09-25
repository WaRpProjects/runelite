package net.runelite.api;

import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public interface Locatable extends Positionable
{
    /**
     * Gets the server-side location of the actor.
     * <p>
     * This value is typically ahead of where the client renders and is not
     * affected by things such as animations.
     *
     * @return the server location
     */
    WorldPoint getWorldLocation();

    /**
     * Gets the client-side location of the actor.
     *
     * @return the client location
     */
    LocalPoint getLocalLocation();

    default int distanceTo(Locatable locatable)
    {
        return locatable.getWorldLocation().distanceTo(getWorldLocation());
    }

    default int distanceTo(WorldPoint point)
    {
        return point.distanceTo(getWorldLocation());
    }

    default int getWorldX()
    {
        return getWorldLocation().getX();
    }

    default int getWorldY()
    {
        return getWorldLocation().getY();
    }

    default int getPlane()
    {
        return getWorldLocation().getPlane();
    }
}