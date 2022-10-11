package net.runelite.client.plugins.moleutil;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("moleutil")
public interface MoleUtilConfig extends Config
{

    @ConfigItem(
            keyName = "Run",
            name = "Testing now",
            description = "Yes",
            position = 0
    )
    default boolean test()
    {
        return true;
    }
}
