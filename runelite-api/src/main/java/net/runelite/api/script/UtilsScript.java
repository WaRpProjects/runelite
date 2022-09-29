package net.runelite.api.script;

import com.google.inject.Injector;
import net.runelite.api.nootbase.Game;
import net.runelite.client.plugins.Plugin;

import javax.inject.Inject;

public abstract class UtilsScript extends Plugin {

    @Inject
    protected Game game;
    @Inject
    protected Injector injector;

}
