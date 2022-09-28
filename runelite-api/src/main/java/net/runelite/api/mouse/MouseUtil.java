package net.runelite.api.mouse;

import net.runelite.api.ActionQueue;
import net.runelite.api.MenuEntry;
import net.runelite.api.utils.LegacyMenuEntry;
import net.runelite.api.utils.MenuUtils;

import javax.inject.Inject;
import java.awt.*;

public class MouseUtil {

    @Inject
    MouseClick mouse;
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

    public void moveClick (Rectangle rectangle, LegacyMenuEntry menu) { mouse.handleMouseClick(rectangle, menu); }
    public void moveClick (Point point, LegacyMenuEntry menu) { mouse.handleMouseClick(point, menu); }
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
