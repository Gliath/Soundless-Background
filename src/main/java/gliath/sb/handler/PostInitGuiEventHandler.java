package gliath.sb.handler;

import gliath.sb.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PostInitGuiEventHandler {

    @SubscribeEvent
    public void onPostInitGuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiScreenOptionsSounds) {
            for (int i = 0; i < event.buttonList.size(); i++) {
                if (event.buttonList.get(i) instanceof GuiOptionButton) { // is the show subtitle button
                    GuiOptionButton subButton = (GuiOptionButton) event.buttonList.get(i);
                    subButton.xPosition = event.gui.width / 2 - 155;
                    break;
                }
            }

            int x = event.gui.width / 2 + 5;
            int y = event.gui.height / 6 - 12 + 144;
            event.buttonList.add(new GuiButton(References.MOD_OPTIONSBUTTON_ID, x, y, 150, 20, getDisplayString()));
        }
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
        boolean musicONinBackground = ConfigurationHandler.musicInBackground; // get the real answer from the config
        return I18n.format("gui.musicOnBackground") + ": " + (musicONinBackground ? I18n.format("options.on") : I18n.format("options.off"));
    }
}