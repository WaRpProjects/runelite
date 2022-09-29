package net.runelite.api.scene;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Position implements Area {
    @Inject
    private Client client;
    public final int x;
    public final int y;
    public final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(WorldPoint worldPoint) {
        this.x = worldPoint.getX();
        this.y = worldPoint.getY();
        this.z = worldPoint.getPlane();
    }

    public static Position unpack(int packed) {
        return new Position(packed >> 14 & 0x3fff, packed & 0x3fff, packed >> 28);
    }

    public Position north() {
        return new Position(x, y + 1, z);
    }

    public Position south() {
        return new Position(x, y - 1, z);
    }

    public Position east() {
        return new Position(x + 1, y, z);
    }

    public Position west() {
        return new Position(x - 1, y, z);
    }

    public Position add(int x, int y, int z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public WorldPoint toWorldPoint() {
        return new WorldPoint(x, y, z);
    }

    public double distanceTo(Position other) {
        if (z != other.z) {
            return Double.MAX_VALUE;
        }

        return Math.hypot((other.x - x), (other.y - y));
    }

    public double distanceTo(WorldPoint other) {
        if (z != other.getPlane()) {
            return Double.MAX_VALUE;
        }

        return Math.hypot((other.getX() - x), (other.getY() - y));
    }

    public boolean inside(Area area) {
        return area.contains(this);
    }

    public int regionID() {
        return x >> 6 << 8 | y >> 6;
    }

    public Position groundLevel() {
        return new Position(x, y, 0);
    }

    public int packed() {
        return (z << 28) + (x << 14) + y;
    }

    @Override
    public boolean contains(Position position) {
        return Objects.equals(position, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
