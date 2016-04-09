package gliath.sb.handler;

import gliath.sb.utilities.References;
import java.io.File;
import java.util.Locale;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ConfigurationHandler {
    public static Configuration configuration;
    public static boolean[] musicInBackgroundSettings;

    public static void init(File configFile) {
        if (musicInBackgroundSettings == null)
            musicInBackgroundSettings = new boolean[SoundCategory.values().length];

        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    public static void toggleSetting(SoundCategory sc) {
        musicInBackgroundSettings[sc.ordinal()] = !musicInBackgroundSettings[sc.ordinal()];
        configuration.save();
    }

    public static void resetSetting(SoundCategory sc) {
        musicInBackgroundSettings[sc.ordinal()] = false;
        configuration.save();
    }

    public static boolean getSetting(SoundCategory sc) {
        return musicInBackgroundSettings[sc.ordinal()];
    }

    private static void loadConfiguration() {
        Property prop;
        for (SoundCategory sc : SoundCategory.values()) {
            String key = "musicInBackground" + sc.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + sc.getName().substring(1);
            prop = configuration.get(Configuration.CATEGORY_GENERAL, key, false);
            prop.setLanguageKey("sb.configgui.musicOnBackground." + sc.getName())
                    .setRequiresMcRestart(false)
                    .setRequiresWorldRestart(false)
                    .setDefaultValue(false)
                    .setComment(I18n.format("sb.configgui.musicOnBackground." + sc.getName() + ".comment"));

            musicInBackgroundSettings[sc.ordinal()] = prop.getBoolean(false);
        }

        if (configuration.hasChanged())
            configuration.save();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(References.MOD_ID))
            loadConfiguration();
    }
}