package net.runelite.api.mouse;

import javax.inject.Inject;
import java.awt.*;

public class MouseUtil {

    @Inject
    MouseClick mouse;

    public void moveClick (Rectangle rectangle)
    {
        mouse.handleMouseClick(rectangle);
    }

}
