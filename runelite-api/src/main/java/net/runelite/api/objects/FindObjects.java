package net.runelite.api.objects;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Locatable;
import net.runelite.api.queries.GameObjectQuery;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import static net.runelite.api.Banks.ALL_BANKS;

@SuppressWarnings("Unused")
@Singleton
public class FindObjects {

    @Inject
    private Client client;

    @Nullable
    public GameObject findNearestGameObject(int... ids) {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return null;
        }

        return new GameObjectQuery()
                .idEquals(ids)
                .result(client)
                .first();
    }

    @Nullable
    public GameObject findNearestBank() {
        assert client.isClientThread();

        if (client.getLocalPlayer() == null) {
            return null;
        }

        return new GameObjectQuery()
                .idEquals(ALL_BANKS)
                .result(client)
                .nearestTo(client.getLocalPlayer());
    }
}
