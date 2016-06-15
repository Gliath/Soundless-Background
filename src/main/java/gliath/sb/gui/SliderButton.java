package gliath.sb.gui;

import gliath.sb.handler.ConfigurationHandler;
import gliath.sb.utilities.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Locale;

public class SliderButton extends GuiButton {
    private final SoundCategory soundCategory;
    private GuiScreenSoundSettings guiScreen;
    private final String buttonName;
    private boolean isUsed;

    public SliderButton(int buttonId, int x, int y, SoundCategory soundCategory, boolean isMasterCategory, GuiScreenSoundSettings guiScreen) {
        super(buttonId, x, y, isMasterCategory ? 310 : 150, 20, "");
        this.soundCategory = soundCategory;
        this.buttonName = I18n.format("soundCategory." + soundCategory.getName());
        this.guiScreen = guiScreen;
        updateButtonDisplayString();
    }

    public SoundCategory getSoundCategory() {
        return this.soundCategory;
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (!super.mousePressed(mc, mouseX, mouseY))
            return false;

        updateButton(mouseX);
        this.isUsed = true;
        return true;
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.isUsed)
                updateButton(mouseX);

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            float f = ((float) ConfigurationHandler.getSetting(soundCategory)) / 100.0f;
            int x = this.xPosition + (int) (f * (float) (this.width - 8));
            this.drawTexturedModalRect(x, this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(x + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        if (!this.isUsed)
            return;

        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0f));
        this.isUsed = false;

        updateButton(mouseX);
    }

    private void updateButton(int mouseX) {
        float newSoundLevel = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
        newSoundLevel = MathHelper.clamp_float(newSoundLevel, 0.0f, 1.0f);
        updateSoundLevel(mouseX, newSoundLevel * 100.0f);

        updateButtonDisplayString();
        guiScreen.checkSettingsAndResetButton();
    }

    public void updateButtonDisplayString() {
        float f = (float) ConfigurationHandler.getSetting(soundCategory);
        this.displayString = this.buttonName + ": " + (f == 0.0f ? I18n.format("options.off") : (int) f + "%");
    }

    private void updateSoundLevel(int mouseX, float soundLevel) {
        ConfigurationHandler.setSetting(soundCategory, soundLevel);
        String key = "musicInBackground" + soundCategory.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + soundCategory.getName().substring(1);
        ConfigurationHandler.configuration.get(Configuration.CATEGORY_GENERAL, key, soundCategory.equals(SoundCategory.MASTER) ? 0.0d : 100.0d)
                .set(ConfigurationHandler.getSetting(soundCategory));

        ConfigChangedEvent configEvent = new ConfigChangedEvent.OnConfigChangedEvent(References.MOD_ID, null, Minecraft.getMinecraft().theWorld != null, false);
        MinecraftForge.EVENT_BUS.post(configEvent);
        if (!configEvent.getResult().equals(Event.Result.DENY))
            MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.PostConfigChangedEvent(References.MOD_ID, null, Minecraft.getMinecraft().theWorld != null, false));
    }

    // Override so it doesn't play a sound
    public void playPressSound(SoundHandler soundHandlerIn) { }
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }
}