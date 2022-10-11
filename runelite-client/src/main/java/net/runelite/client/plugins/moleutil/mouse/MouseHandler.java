package net.runelite.client.plugins.moleutil.mouse;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.plugins.moleutil.util.RandomUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SuppressWarnings("unused")
@Singleton
public class MouseHandler
{
    //Running mouse functions on a different Thread so we don't get a desync
    ExecutorService executorService;

    public MouseHandler ()
    {
        executorService = Executors.newFixedThreadPool (1);
    }

    @Inject
    private Client client;
    @Inject
    private RandomUtil random;



    /* Handles all Mouse Features
     * ToDo Clean up code. *Done 11-10-2022
     * ToDo Get it working with Settings.
     * ToDo Add more features with mouse.
     * ToDo Make it Spoofable.
     */


    /**
     * mouseEvent void
     * @param id
     * @param point
     */
    private void mouseEvent (int id, Point point)
    {
        MouseEvent e = new MouseEvent(
                client.getCanvas(), id,
                System.currentTimeMillis(),
                0, point.getX(), point.getY(),
                1, false, 1
        );

        client.getCanvas().dispatchEvent(e);
    }

    /**
     * Get's a random clicking point from Rectangle.
     * @param rect
     * @return
     */
    private Point getClickPoint (Rectangle rect)
    {
        final int x = (int) (rect.getX() + random.getRandomIntBetweenRange((int) rect.getWidth() / 6 * -1, (int) rect.getWidth() / 6) + rect.getWidth() / 2);
        final int y = (int) (rect.getY() + random.getRandomIntBetweenRange((int) rect.getHeight() / 6 * -1, (int) rect.getHeight() / 6) + rect.getHeight() / 2);
        final int testX = (int) rect.getX();
        final int testY = (int) rect.getY();
        return new Point(testX, testY);
    }

    private void clickMouse (Point point)
    {
        assert !client.isClientThread();

        if (client.isStretchedEnabled())
        {
            final Dimension stretched = client.getStretchedDimensions();
            final Dimension real = client.getRealDimensions();
            final double width = (stretched.width / real.getWidth());
            final double height = (stretched.height / real.getHeight());
            point = new Point((int) (point.getX() * width), (int) (point.getY() * height));
        }

        mouseEvent(MouseEvent.MOUSE_PRESSED, point);
        mouseEvent(MouseEvent.MOUSE_RELEASED, point);
        mouseEvent(MouseEvent.MOUSE_CLICKED, point);
    }

    /**
     * Moves mouse to Point
     * @param point
     */
    private void moveMouse (Point point)
    {
        assert !client.isClientThread ();

        if (client.isStretchedEnabled ())
        {
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
     * Move and Clicks on Point
     * @param point
     */
    private void moveClick (Point point)
    {
        assert !client.isClientThread();

        if (client.isStretchedEnabled())
        {
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

    public void handleMouseClick (Rectangle rectangle)
    {
        Point point = getClickPoint(rectangle);
        handleMouseClick(point);
    }

    public void handleMouseClick (Point point)
    {
        final int viewportHeight = client.getViewportHeight();
        final int viewportWidth = client.getViewportWidth();

        if (point.getX() > viewportWidth || point.getY() > viewportHeight || point.getX() < 0 || point.getY() < 0) {
            point = new Point(client.getCenterX() + random.getRandomIntBetweenRange(-100, 100),
                    client.getCenterY() + random.getRandomIntBetweenRange(-100, 100));
        }

        log.info("[MouseUtil] - Clicking on: Y" + point.getY() + " - X" + point.getX());
        Point finalClickPoint = point;
        executorService.submit(() ->
                clickMouse(finalClickPoint)
        );
        log.info("[MouseUtil] - Done clicking mouse.");
    }

    /**
     * Moves the mouse on canvas
     * @param rectangle
     */
    public void handleMouseMove (Rectangle rectangle)
    {
        Point point =  getClickPoint(rectangle);
        handleMouseMove(point);
    }

    /**
     * Moves the mouse on canvas
     * @param point
     */
    public void handleMouseMove (Point point)
    {
        final int viewportHeight = client.getViewportHeight();
        final int viewportWidth = client.getViewportWidth();

        if (point.getX() > viewportWidth || point.getY() > viewportHeight || point.getX() < 0 || point.getY() < 0) {
            point = new Point(client.getCenterX() + random.getRandomIntBetweenRange(-100, 100),
                    client.getCenterY() + random.getRandomIntBetweenRange(-100, 100));
        }

        log.info("[MouseUtil] - Moving mouse to: Y" + point.getY() + " - X" + point.getX());
        Point finalClickPoint = point;
        executorService.submit(() ->
                moveMouse(finalClickPoint)
        );
        log.info("[MouseUtil] - Done moving mouse.");
    }


    /**
     * Handles mouse feature and Thread
     * @param rectangle
     */
    public void handleMoveClick (Rectangle rectangle)
    {
        Point point = getClickPoint(rectangle);
        handleMoveClick(point);
    }

    /**
     * Handles mouse feature and Thread
     * @param point
     */
    public void handleMoveClick (Point point)
    {

        final int viewportHeight = client.getViewportHeight();
        final int viewportWidth = client.getViewportWidth();

        if (point.getX() > viewportWidth || point.getY() > viewportHeight || point.getX() < 0 || point.getY() < 0) {
            point = new Point(client.getCenterX() + random.getRandomIntBetweenRange(-100, 100),
                    client.getCenterY() + random.getRandomIntBetweenRange(-100, 100));
        }

        log.info("[MouseUtil] - Moving mouse to: Y" + point.getY() + " - X" + point.getX() + ". To click");
        Point finalClickPoint = point;
        executorService.submit(() ->
                {
                    moveMouse(finalClickPoint);
                    try
                    {
                        Thread.sleep(random.getRandomIntBetweenRange(859, 2321));
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    moveClick(finalClickPoint);
                }
        );
        log.info("[MouseUtil] - Done moving and clicking on point.");
    }

}
