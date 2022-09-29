package net.runelite.client.plugins.nootwillow;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("NootWillow")
public interface NootWillowConfig extends Config {
    @ConfigItem(
            keyName = "Start",
            name = "Start of the Bot",
            description = "Just for WaRp to Test"
    )
    default boolean Start()
    {
        return false;
    }
}
