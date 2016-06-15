package gliath.sb.handler;

import gliath.sb.gui.GuiScreenSoundSettings;
import gliath.sb.utilities.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OptionsMenuHandler {

    @SubscribeEvent
    public void onPostInitGuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiScreenOptionsSounds) {
            for (int i = 0; i < event.getButtonList().size(); i++) {
                if (event.getButtonList().get(i) instanceof GuiOptionButton) { // if it is the subtitle button, reposition it
                    GuiOptionButton subButton = (GuiOptionButton) event.getButtonList().get(i);
                    subButton.xPosition = event.getGui().width / 2 - 155;
                    break;
                }
            }

            int width = event.getGui().width / 2 + 5;
            int height = event.getGui().height / 6 - 12 + 144;
            event.getButtonList().add(new GuiButton(References.OPTION_BUTTON_IDSTART, width, height, 150, 20, I18n.format("gui.musicOnBackground")));
        }
    }

    @SubscribeEvent
    public void onPreButtonPress(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.getButton().id == References.OPTION_BUTTON_IDSTART)
            Minecraft.getMinecraft().displayGuiScreen(new GuiScreenSoundSettings(event.getGui()));
    }
}