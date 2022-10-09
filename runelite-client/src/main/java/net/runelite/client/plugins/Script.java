package net.runelite.client.plugins;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.nootbase.Game;
import net.runelite.api.script.PluginStoppedException;

import javax.swing.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class Script extends Plugin implements Runnable{

    private static final ScheduledExecutorService EXECUTOR = new LoggableExecutor(1);
    private final AtomicInteger ticks = new AtomicInteger(0);

    private volatile int nextSleep = 1000;
    private int currentSleep = 1000;
    private int sleepUntil = 0;

    protected abstract int loop();

    private Future<?> task;


    @Override
    public void run()
    {
        task = EXECUTOR.schedule(loopTask(), nextSleep, TimeUnit.MILLISECONDS);

        while (task != null && !task.isCancelled())
        {
            log.info("[RunTask] While loop");
            if (task.isCancelled())
            {
                task = null;
                return;
            }

            if (task == null || task.isDone())
            {
                log.info("[RunTask] Null");
                task = EXECUTOR.schedule(loopTask(), nextSleep, TimeUnit.MILLISECONDS);
                continue;
            }
            log.info("[RunTask] Sleep");
            Time.sleep(10);
        }
    }

    private Runnable loopTask()
    {
        return () ->
        {
            try
            {
                int currentTick = ticks.get();
                if (sleepUntil > 0 && sleepUntil > currentTick && Game.isLoggedIn())
                {
                    return;
                }

                sleepUntil = 0;
                log.debug("[LoopTask] Loop running " + this.getName());
                currentSleep = this instanceof ScriptLoop ? ((ScriptLoop) this).outerLoop() : loop();
            }
            catch (PluginStoppedException e)
            {

                SwingUtilities.invokeLater(() -> Plugins.stopPlugin(this));
                log.info("[LoopTask Exception: " + e.getMessage());
            }
            finally
            {
                log.debug("[LoopTask] Finally");
                if (sleepUntil == 0)
                {
                    if (currentSleep < 0 && Game.isLoggedIn())
                    {
                        int currentTick = ticks.get();
                        sleepUntil = currentTick + Math.abs(currentSleep);
                        nextSleep = 0;
                    }
                    else
                    {
                        nextSleep = currentSleep < 0 ? 1000 : currentSleep;
                    }
                }
            }
        };
    }

    public boolean isRunning()
    {
        return task != null && !task.isCancelled();
    }

    public void stop()
    {
        task.cancel(true);
    }

    private static class LoggableExecutor extends ScheduledThreadPoolExecutor
    {
        public LoggableExecutor(int corePoolSize)
        {
            super(corePoolSize);
        }

        protected void afterExecute(Runnable r, Throwable t)
        {
            super.afterExecute(r, t);

            if (t == null && r instanceof Future<?>)
            {
                try
                {
                    Future<?> future = (Future<?>) r;
                    if (future.isDone())
                    {
                        future.get();
                    }
                }
                catch (CancellationException ignored)
                {

                }
                catch (ExecutionException ee)
                {
                    t = ee.getCause();
                }
                catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                }
            }

            if (t != null)
            {
                log.error("Error in loop", t);
            }
        }
    }
}
