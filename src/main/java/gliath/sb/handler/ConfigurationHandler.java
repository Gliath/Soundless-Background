package gliath.sb.handler;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gliath.sb.References;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigurationHandler {
    public static Configuration configuration;
    public static boolean musicInBackground = false;

    public static void init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    public static void toggleSetting() {
        musicInBackground = !musicInBackground;
        configuration.save();
    }

    private static void loadConfiguration() {
        Property prop = configuration.get(Configuration.CATEGORY_GENERAL, "musicInBackground", false, "When this setting is false, no sounds will be heard, if Minecraft is in the background.");
        prop.setLanguageKey("sb.configgui.musicOnBackground")
                .setRequiresMcRestart(false)
                .setRequiresWorldRestart(false)
                .setDefaultValue(false);

        musicInBackground = prop.getBoolean(false);

        if (configuration.hasChanged())
            configuration.save();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(References.MOD_ID))
            loadConfiguration();
    }
}