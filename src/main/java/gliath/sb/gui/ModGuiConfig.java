package gliath.sb.gui;

import cpw.mods.fml.client.config.GuiConfig;
import gliath.sb.References;
import gliath.sb.handler.ConfigurationHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class ModGuiConfig extends GuiConfig {
    public ModGuiConfig (GuiScreen guiScreen) {
        super(guiScreen,
                new ConfigElement(ConfigurationHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                References.MOD_ID, false, false, "Soundless Background Configuration");
    }
}