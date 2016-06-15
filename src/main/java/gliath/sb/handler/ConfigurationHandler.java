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
    public static double[] musicInBackgroundSettings;

    public static void init(File configFile) {
        if (musicInBackgroundSettings == null)
            musicInBackgroundSettings = new double[SoundCategory.values().length];

        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    public static void setSetting(SoundCategory sc, float soundLevel) {
        musicInBackgroundSettings[sc.ordinal()] = soundLevel;
        configuration.save();
    }

    public static void resetSetting(SoundCategory sc) {
        musicInBackgroundSettings[sc.ordinal()] = getDefaultValue(sc);
        configuration.save();
    }

    public static double getSetting(SoundCategory sc) {
        return musicInBackgroundSettings[sc.ordinal()];
    }

    private static void loadConfiguration() {
        Property prop;
        for (SoundCategory sc : SoundCategory.values()) {
            String properName = sc.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + sc.getName().substring(1);
            String key = "musicInBackground" + properName;
            prop = configuration.get(Configuration.CATEGORY_GENERAL, key, getDefaultValue(sc));
            prop.setLanguageKey("sb.configgui.musicOnBackground." + sc.getName())
                    .setRequiresMcRestart(false)
                    .setRequiresWorldRestart(false)
                    .setDefaultValue(getDefaultValue(sc))
                    .setComment(I18n.format("sb.configgui.musicOnBackground.comment", properName));

            musicInBackgroundSettings[sc.ordinal()] = prop.getDouble(getDefaultValue(sc));
        }

        if (configuration.hasChanged())
            configuration.save();
    }

    private static double getDefaultValue(SoundCategory sc) {
        return sc.equals(SoundCategory.MASTER) ? 0.0d : 100.0d;
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(References.MOD_ID))
            loadConfiguration();
    }
}