package net.runelite.api.script;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
@Slf4j
public class Script extends UtilsScript{

    private Future<?> future;
    private ScriptHandler scriptHandler;

    private ExecutorService executorService;

    private volatile boolean started;

    public void start() {
        executorService = Executors.newFixedThreadPool(1);
        if (scriptHandler != null) {
            log.info("Script already running, stopping it first!");
            stop();
        }
        scriptHandler = new ScriptHandler(this);

        if (future == null || future.isCancelled() || future.isDone()) {
            future = executorService.submit(scriptHandler);
            started = true;
        } else {
            stop();
        }
    }

    public void execute() {
        if (scriptHandler != null) {
            log.info("Script already running, stopping it first!");
            stop();
            return;
        }
        scriptHandler = new ScriptHandler(this);

        if (future == null || future.isCancelled() || future.isDone()) {
            future = executorService.submit(scriptHandler);
            started = true;
        } else {
            stop();
        }
    }

    public void stop() {
        scriptHandler = null;
        started = false;
        if (future != null) {
            onStop();
            future.cancel(true);
        } else {
            log.info("Couldn't find future to stop");
        }
    }

    @Override
    protected void shutDown() {
        log.info("Shutting down");
        stop();
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (!started()) {
            return;
        }
    }

    protected void loop() {

    }

    protected void onStart() {

    }

    protected void onStop() {

    }

    public boolean started() {
        return started;
    }
}
