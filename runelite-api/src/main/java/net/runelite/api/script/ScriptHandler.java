package net.runelite.api.script;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.random.Random;
import net.runelite.api.utils.Sleep;

import javax.inject.Inject;

@Slf4j
public class ScriptHandler implements Runnable{

    private final Script script;
    private static final int FAILURE_RESET = 75000;
    private static final int MAX_FAILURES = 10;
    @Inject
    private Sleep sleep;
    @Inject
    private Random random;

    public ScriptHandler(Script script) {
        this.script = script;
    }

    @Override
    public void run() {
            int failures = 0;
            long lastFailure = System.currentTimeMillis();

            script.onStart();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (script.started()) {
                        script.loop();
                    } else {
                        script.stop();
                    }
                } catch (IllegalStateException | AssertionError | NullPointerException e) {

                    if (System.currentTimeMillis() - lastFailure > FAILURE_RESET) {
                        failures = 0;
                    }

                    lastFailure = System.currentTimeMillis();

                    if (failures <= MAX_FAILURES) {
                        failures++;
                        log.info("Caught failure #{}, restarting in 3 seconds", failures);
                        e.printStackTrace();
                        log.info("{} - {} - caused by: {}", e.getLocalizedMessage(), e.getMessage(), e.getCause());
                        sleep.sleep(3000);
                    } else {
                        log.info("Caught > 10 failures, stopping plugin");
                        script.stop();
                        return;
                    }
                } catch (UnsupportedOperationException e) {
                    log.info("Caught unsupported terminal failure, stopping instantly");
                    //script.game.sendGameMessage("Caught unsupported terminal failure, stopping instantly");
                    e.printStackTrace();
                    log.info("{} - caused by: {}", e.getMessage(), e.getCause());
                    //script.game.sendGameMessage(e.getMessage() + " - caused by: " + e.getCause());
                    script.stop();
                    return;
                }
            }
        }

}
