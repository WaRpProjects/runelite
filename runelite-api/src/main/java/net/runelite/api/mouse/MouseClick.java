package net.runelite.api.mouse;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.utils.LegacyMenuEntry;
import net.runelite.api.utils.MenuUtils;
import net.runelite.api.utils.Sleep;
import net.runelite.api.random.Random;
import net.runelite.api.widgets.Widget;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.MouseEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SuppressWarnings("unused")
@Singleton
public class MouseClick {

    //Executor thread for the mouse.
    public MouseClick() {
        executorService = Executors.newFixedThreadPool(1);
    }
    @Inject
    private Client client;
    @Inject
    private Random random;
    @Inject
    private Sleep sleep;
    @Inject
    private MenuUtils menu;

    ExecutorService executorService;

    /* Handles all Mouse Features
     * ToDo Clean up code.
     * ToDo Get it working with Settings.
     * ToDo Add more features with mouse.
     * ToDo Make it Spoofable.
     */

    /**
     * Handles the mouseclick with moving.
     * @param rectangle
     */
    public void handleMouseClick(Rectangle rectangle) {
        Point point = getClickPoint(rectangle);
        handleMouseClick(point);
    }
    /**
     * Handles the mouseclick with moving.
     * @param point
     */
    public void handleMouseClick(Point point)
    {

        final int viewportHeight = client.getViewportHeight();
        final int viewportWidth = client.getViewportWidth();

        Widget minimapWidget = client.getWidget(164, 20);

        if (point.getX() > viewportWidth || point.getY() > viewportHeight || point.getX() < 0 || point.getY() < 0) {
            point = new Point(client.getCenterX() + random.getRandomIntBetweenRange(-100, 100),
                    client.getCenterY() + random.getRandomIntBetweenRange(-100, 100));
        }

        log.debug("[MouseUtil] - Starting Mouse handler on a different thread.");
        Point finalClickPoint = point;
        executorService.submit(() ->
                {
                moveMouseEvent(finalClickPoint);
                        try {
                            Thread.sleep(random.getRandomIntBetweenRange(394, 1203));
                        }
                        catch (InterruptedException e)
                        {
                                throw new RuntimeException(e);
                    }
            moveClick(finalClickPoint);
                }
        );

        log.debug("[MouseUtil] - Done moving and clicking on point.");
    }

    public void handleMouseClick(Rectangle rectangle, LegacyMenuEntry m) {
        Point point = getClickPoint(rectangle);
        handleMouseClick(point, m);
    }

    public void handleMouseClick(Point point, LegacyMenuEntry m)
    {

        final int viewportHeight = client.getViewportHeight();
        final int viewportWidth = client.getViewportWidth();

        Widget minimapWidget = client.getWidget(164, 20);

        if (point.getX() > viewportWidth || point.getY() > viewportHeight || point.getX() < 0 || point.getY() < 0) {
            point = new Point(client.getCenterX() + random.getRandomIntBetweenRange(-100, 100),
                    client.getCenterY() + random.getRandomIntBetweenRange(-100, 100));
        }

        log.debug("[MouseUtil] - Starting Mouse handler on a different thread.");
        Point finalClickPoint = point;
        executorService.submit(() ->
                {
                    moveMouseEvent(finalClickPoint);
                    try {
                        Thread.sleep(random.getRandomIntBetweenRange(394, 1203));
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    menu.setEntry(m);
                    moveClick(finalClickPoint);
                }
        );

        log.debug("[MouseUtil] - Done moving and clicking on point.");
    }


    private void mouseEvent(int id, Point point) {
        MouseEvent e = new MouseEvent(
                (Component) client.getCanvas(), id,
                System.currentTimeMillis(),
                0, (int) point.getX(), (int) point.getY(),
                1, false, 1
        );

        client.getCanvas().dispatchEvent(e);
    }

    /**
     * Move the mouse to a Point on screen.
     * @param point
     */
    public void moveClick(Point point) {
        assert !client.isClientThread();

        if (client.isStretchedEnabled()) {
            final Dimension stretched = client.getStretchedDimensions();
            final Dimension real = client.getRealDimensions();
            final double width = (stretched.width / real.getWidth());
            final double height = (stretched.height / real.getHeight());
            point = new Point((int) (point.getX() * width), (int) (point.getY() * height));
        }
        mouseEvent(MouseEvent.MOUSE_ENTERED, point);
        mouseEvent(MouseEvent.MOUSE_EXITED, point);
        mouseEvent(MouseEvent.MOUSE_MOVED, point);
        mouseEvent(MouseEvent.MOUSE_PRESSED, point);
        mouseEvent(MouseEvent.MOUSE_RELEASED, point);
        mouseEvent(MouseEvent.MOUSE_CLICKED, point);
    }

    /**
     * Get's a random point to click on screen
     * @param rect
     * @return
     */
    public Point getClickPoint(Rectangle rect) {
        final int x = (int) (rect.getX() + random.getRandomIntBetweenRange((int) rect.getWidth() / 6 * -1, (int) rect.getWidth() / 6) + rect.getWidth() / 2);
        final int y = (int) (rect.getY() + random.getRandomIntBetweenRange((int) rect.getHeight() / 6 * -1, (int) rect.getHeight() / 6) + rect.getHeight() / 2);

        return new Point(x, y);
    }

    /**
     * Moves the mouse to a Point on client.
     * @param point
     */
    public void moveMouseEvent(Point point) {
        assert !client.isClientThread();

        if (client.isStretchedEnabled()) {
            final Dimension stretched = client.getStretchedDimensions();
            final Dimension real = client.getRealDimensions();
            final double width = (stretched.width / real.getWidth());
            final double height = (stretched.height / real.getHeight());
            point = new Point((int) (point.getX() * width), (int) (point.getY() * height));
        }
        mouseEvent(MouseEvent.MOUSE_ENTERED, point);
        mouseEvent(MouseEvent.MOUSE_EXITED, point);
        mouseEvent(MouseEvent.MOUSE_MOVED, point);
    }

    /**
     * Not Implemted yet. WaRp
     * @param min
     * @param max
     */
    public void clickRandomPointCenter(int min, int max) {
        assert !client.isClientThread();

        Point point = new Point(client.getCenterX() + random.getRandomIntBetweenRange(min, max), client.getCenterY() + random.getRandomIntBetweenRange(min, max));
        handleMouseClick(point);
    }
}
