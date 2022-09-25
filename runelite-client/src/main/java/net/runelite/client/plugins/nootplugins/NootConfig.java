package net.runelite.client.plugins.nootplugins;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("NootPlugin")
public interface NootConfig extends Config {
    @ConfigItem(
            keyName = "Test",
            name = "Testing of Bot Features",
            description = "Just for WaRp to Test"
    )
    default boolean Test()
    {
        return false;
    }
}
