package net.runelite.client.plugins.moleutil.mouse;

public enum Mouse
{
        ZERO_MOUSE("0x,0y mouse"),
        NO_MOVE("No move data"),
        MOVE("Move mouse"),
        RECTANGLE("Rectangle mouse");

        public final String name;

        Mouse(String name)
        {
            this.name = name;
        }

}
