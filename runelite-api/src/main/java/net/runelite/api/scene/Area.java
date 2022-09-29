package net.runelite.api.scene;

import java.util.Arrays;
import net.runelite.api.coords.WorldPoint;

public interface Area {
    boolean contains(Position position);

//    boolean contains(WorldPoint worldPoint);

    static Area union(Area... areas) {
        return position -> Arrays.stream(areas).anyMatch(a -> a.contains(position));
    }

    static Area intersection(Area... areas) {
        return position -> Arrays.stream(areas).allMatch(a -> a.contains(position));
    }

    default Area minus(Area other) {
        return position -> Area.this.contains(position) && !other.contains(position);
    }
}
