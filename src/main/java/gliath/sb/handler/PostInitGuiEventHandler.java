package gliath.sb.handler;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gliath.sb.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class PostInitGuiEventHandler {

    @SubscribeEvent
    public void onPostInitGuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiScreenOptionsSounds) {
            int x = event.gui.width / 2 - 75;
            int y = event.gui.height / 6 + 120;
            event.buttonList.add(new GuiButton(References.MOD_OPTIONSBUTTON_ID, x, y, 150, 20, getDisplayString()));
        }

        if (event.gui instanceof GuiMainMenu && !SoundEventHandler.isMcInitiated())
            SoundEventHandler.mcHasInitiated();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPreButtonPress(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.button.id == References.MOD_OPTIONSBUTTON_ID) {
            ConfigurationHandler.toggleSetting();
            event.button.displayString = getDisplayString();
            event.setCanceled(true);

            ConfigurationHandler.configuration.get(Configuration.CATEGORY_GENERAL, "musicInBackground", false).set(ConfigurationHandler.musicInBackground);

            ConfigChangedEvent configEvent = new ConfigChangedEvent.OnConfigChangedEvent(References.MOD_ID, null, Minecraft.getMinecraft().theWorld != null, false);
            MinecraftForge.EVENT_BUS.post(configEvent);
            if (!configEvent.getResult().equals(Event.Result.DENY))
                MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.PostConfigChangedEvent(References.MOD_ID, null, Minecraft.getMinecraft().theWorld != null, false));
        }
    }

    private String getDisplayString() {
        boolean musicONinBackground = ConfigurationHandler.musicInBackground;
        return "Music in Background: " + (musicONinBackground ? I18n.format("options.on") : I18n.format("options.off"));
    }
}