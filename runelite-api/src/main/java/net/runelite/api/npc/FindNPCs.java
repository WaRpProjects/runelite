package net.runelite.api.npc;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.NPCQuery;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("Unused")
@Singleton
public class FindNPCs {

    @Inject
    private Client client;

    /**
     * Get an NPC by ID that's closest to player
     * @param ids
     * @return
     */
    @Nullable
    public NPC findNearestNpc(int... ids) {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return null;
        }

        return new NPCQuery()
                .idEquals(ids)
                .result(client)
                .nearestTo(client.getLocalPlayer());
    }

    /**
     * Get an NPC by ID that's closest to player
     * @param names
     * @return
     */
    @Nullable
    public NPC findNearestNpc(String... names) {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return null;
        }

        return new NPCQuery()
                .nameContains(names)
                .result(client)
                .nearestTo(client.getLocalPlayer());
    }

    /**
     * Get's closest NPC withing range
     * @param worldPoint
     * @param dist
     * @param ids
     * @return
     */
    @Nullable
    public NPC findNearestNpcWithin(WorldPoint worldPoint, int dist, Collection<Integer> ids) {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return null;
        }

        return new NPCQuery()
                .isWithinDistance(worldPoint, dist)
                .idEquals(ids)
                .result(client)
                .nearestTo(client.getLocalPlayer());
    }

    /**
     * Get's closest attackable NPC withing a range
     * @param worldPoint
     * @param dist
     * @param name
     * @param exactnpcname
     * @return
     */
    @Nullable
    public NPC findNearestAttackableNpcWithin(WorldPoint worldPoint, int dist, String name, boolean exactnpcname) {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return null;
        }
        for (String npcName : name.split(",")) {
            NPC query;
            if (exactnpcname) {
                query = new NPCQuery()
                        .isWithinDistance(worldPoint, dist)
                        .filter(npc -> npc.getName() != null && npc.getName().toLowerCase().equals(npcName.toLowerCase()) && npc.getInteracting() == null && npc.getHealthRatio() != 0)
                        .result(client)
                        .nearestTo(client.getLocalPlayer());
            } else {
                query = new NPCQuery()
                        .isWithinDistance(worldPoint, dist)
                        .filter(npc -> npc.getName() != null && npc.getName().toLowerCase().contains(npcName.toLowerCase()) && npc.getInteracting() == null && npc.getHealthRatio() != 0)
                        .result(client)
                        .nearestTo(client.getLocalPlayer());
            }
            if (query != null) {
                return query;
            }
        }
        return null;
    }

    /**
     * Get's a list of NPCs from ID
     * @param ids
     * @return
     */
    public List<NPC> getNPCs(int... ids) {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return new ArrayList<>();
        }

        return new NPCQuery()
                .idEquals(ids)
                .result(client)
                .list;
    }

    /**
     * Get's a list of NPC's from Names
     * @param names
     * @return
     */
    public List<NPC> getNPCs(String... names) {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return new ArrayList<>();
        }

        return new NPCQuery()
                .nameContains(names)
                .result(client)
                .list;
    }

    /**
     * Get's the Interacting NPC with local Player
     * @return
     */
    public NPC getFirstNPCWithLocalTarget() {
        assert client.isClientThread();

        List<NPC> npcs = client.getNpcs();
        for (NPC npc : npcs) {
            if (npc.getInteracting() == client.getLocalPlayer()) {
                return npc;
            }
        }
        return null;
    }

}
