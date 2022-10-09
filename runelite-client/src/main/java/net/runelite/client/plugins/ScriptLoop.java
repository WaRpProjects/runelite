package net.runelite.client.plugins;


import lombok.Getter;
import net.runelite.api.GameState;
import net.runelite.api.nootbase.Game;
import net.runelite.client.Static;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.awt.*;

public abstract class ScriptLoop extends Script{

    protected final Logger logger;

    private boolean restart;
    private boolean paused;
    private boolean onLogin;

    public ScriptLoop()
    {
        logger = (Logger) LoggerFactory.getLogger(getClass());
    }

    @Inject
    @Getter
    private Paint paint;



    public ScriptLoop(Logger logger) {
        this.logger = logger;
    }

    protected abstract int loop();

    public abstract void onStart(String... args);

    public void onStop()
    {

    }

    public void onLogin()
    {

    }

    public int outerLoop()
    {
        int loopSleep;
        if (paused)
        {
            return 1000;
        }

        if (restart)
        {
            restart = false;
            return 1000;
        }

        if (Game.getState() == GameState.LOGGED_IN && !onLogin)
        {
            onLogin = true;
            onLogin();
            return 100;
        }



        loopSleep = loop();
        return loopSleep != 0 ? loopSleep : 1000;
    }

    public void pauseScript()
    {

    }

    public boolean isRestart()
    {
        return restart;
    }

    public void setRestart(boolean restart)
    {
        this.restart = restart;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused)
    {
        this.paused = paused;
    }
}


