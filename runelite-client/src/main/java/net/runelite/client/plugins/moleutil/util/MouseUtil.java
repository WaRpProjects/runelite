package net.runelite.client.plugins.moleutil.util;


import net.runelite.api.Point;
import net.runelite.client.plugins.moleutil.mouse.MouseHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
@SuppressWarnings("unused")
@Singleton
public class MouseUtil
{
    @Inject
    MouseHandler mouse;

    /**
     * Moves mouse and click on Rectangle
     */
    public void moveClick (Rectangle rectangle)
    {
        mouse.handleMoveClick(rectangle);
    }

    public void moveClick (Point point)
    {
        mouse.handleMoveClick(point);
    }

    public void move(Rectangle rectangle)
    {
        mouse.handleMouseMove(rectangle);
    }

    public void move(Point point)
    {
        mouse.handleMouseMove(point);
    }

    public void click (Rectangle rectangle)
    {
        mouse.handleMouseClick(rectangle);
    }

    public void click (Point point)
    {
        mouse.handleMouseClick(point);
    }
}
