package net.runelite.client.plugins.moleutil;

import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.moleutil.util.MouseUtil;

import javax.inject.Inject;


@PluginDescriptor(
        name = "Mole Script utilities",
        description = "My script utilities ",
        tags = {"util", "mole", "bot", "script"},
        enabledByDefault = false
)
public class MoleUtil extends Plugin {

    @Inject
    public MouseUtil mouse;

    @Provides
    public MoleUtilConfig getConfig(ConfigManager manager)
    {
        return manager.getConfig(MoleUtilConfig.class);
    }





}
