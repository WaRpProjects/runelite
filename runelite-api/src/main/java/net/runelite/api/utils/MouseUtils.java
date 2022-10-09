package net.runelite.api.utils;

import net.runelite.api.ActionQueue;
import net.runelite.api.Client;
import net.runelite.api.mouse.MouseHandler;
import net.runelite.client.callback.ClientThread;

import javax.inject.Inject;
import java.awt.*;

public class MouseUtils {

    @Inject
    MouseHandler mouse;
    @Inject
    private MenuUtils menu;
    ActionQueue action;
    public void moveClick (Rectangle rectangle)
    {
        mouse.handleMouseClick(rectangle);
    }
    public void moveClick (Point point)
    {
        mouse.handleMouseClick(point);
    }

    public void mouseClick (Rectangle rectangle) { mouse.testMouseClick(rectangle);}

    public void doActionClientTick(LegacyMenuEntry entry, Rectangle rect, long ticksToDelay) {
        Point point = mouse.getClickPoint(rect);
        doActionClientTick(entry, point, ticksToDelay);
    }

    public void doActionClientTick(LegacyMenuEntry entry, Point point, long ticksToDelay) {
        Runnable runnable = () -> {
            menu.setEntry(entry);
            mouse.handleMouseClick(point);
        };

        action.delayClientTicks(ticksToDelay, runnable);
    }

    public void doActionMsTime(LegacyMenuEntry entry, Rectangle rect, long timeToDelay) {
        Point point = mouse.getClickPoint(rect);
        doActionMsTime(entry, point, timeToDelay);
    }

    public void doActionMsTime(LegacyMenuEntry entry, Point point, long timeToDelay) {
        Runnable runnable = () -> {
            menu.setEntry(entry);
            mouse.handleMouseClick(point);
        };
        action.delayTime(timeToDelay, runnable);
    }

}
