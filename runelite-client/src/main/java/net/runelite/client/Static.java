package net.runelite.client;

import lombok.Getter;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Static {

    @Inject
    @Getter
    private static EventBus eventBus;

    @Inject
    @Getter
    private static ClientThread clientThread;


}
