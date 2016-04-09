package gliath.sb.gui;

import gliath.sb.handler.ConfigurationHandler;
import gliath.sb.utilities.References;
import java.io.IOException;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;


public class GuiScreenSoundSettings extends GuiScreen {
    private static final int BUTTON_START_ID = References.OPTION_BUTTON_IDSTART + 1;
    private static final int NUMBEROFSETTINGS = SoundCategory.values().length;
    private final GuiScreen previousScreen;
    private String guiTitle;

    public GuiScreenSoundSettings(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        guiTitle = I18n.format("gui.musicOnBackground.title");
        this.buttonList.add(new GuiButton(BUTTON_START_ID + SoundCategory.MASTER.ordinal(), this.width / 2 - 155, this.height / 6 - 12, 310, 20, getDisplayString(SoundCategory.MASTER)));

        int i = 2;
        for (SoundCategory sc : SoundCategory.values()) {
            if (sc != SoundCategory.MASTER) {
                this.buttonList.add(new GuiButton(BUTTON_START_ID + sc.ordinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), 150, 20, getDisplayString(sc)));
                ++i;
            }
        }

        if (i % 2 == 1) // If it's odd
            i++; // This ensures that the following 2 buttons are on a new line

        this.buttonList.add(new GuiButton(BUTTON_START_ID + NUMBEROFSETTINGS, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), 150, 20, getDisplayString("toggleAll")));
        i++;
        this.buttonList.add(new GuiButton(BUTTON_START_ID + NUMBEROFSETTINGS + 1, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), 150, 20, getDisplayString("reset")));
        checkSettingsButton();
        checkResetButton();

        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 200)
                this.mc.displayGuiScreen(this.previousScreen);
            else if (button.id >= BUTTON_START_ID && button.id <= BUTTON_START_ID + NUMBEROFSETTINGS + 1) {
                if (button.id >= BUTTON_START_ID && button.id < BUTTON_START_ID + NUMBEROFSETTINGS) {
                    SoundCategory sc = SoundCategory.values()[button.id - BUTTON_START_ID];
                    ConfigurationHandler.toggleSetting(sc);
                    button.displayString = getDisplayString(sc);
                    String key = "musicInBackground" + sc.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + sc.getName().substring(1);
                    ConfigurationHandler.configuration.get(Configuration.CATEGORY_GENERAL, key, false).set(ConfigurationHandler.getSetting(sc));
                } else if (button.id == BUTTON_START_ID + NUMBEROFSETTINGS) { // Toggle All
                    for (SoundCategory sc : SoundCategory.values()) {
                        GuiButton buttonToChange = this.buttonList.get(sc.ordinal());
                        ConfigurationHandler.toggleSetting(sc);
                        buttonToChange.displayString = getDisplayString(sc);

                        String key = "musicInBackground" + sc.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + sc.getName().substring(1);
                        ConfigurationHandler.configuration.get(Configuration.CATEGORY_GENERAL, key, false).set(ConfigurationHandler.getSetting(sc));
                    }
                } else if (button.id == BUTTON_START_ID + NUMBEROFSETTINGS + 1) { // Reset
                    for (SoundCategory sc : SoundCategory.values()) {
                        GuiButton buttonToChange = this.buttonList.get(sc.ordinal());
                        ConfigurationHandler.resetSetting(sc);
                        buttonToChange.displayString = getDisplayString(sc);

                        String key = "musicInBackground" + sc.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + sc.getName().substring(1);
                        ConfigurationHandler.configuration.get(Configuration.CATEGORY_GENERAL, key, false).set(ConfigurationHandler.getSetting(sc));
                    }
                }

                ConfigChangedEvent configEvent = new ConfigChangedEvent.OnConfigChangedEvent(References.MOD_ID, null, Minecraft.getMinecraft().theWorld != null, false);
                MinecraftForge.EVENT_BUS.post(configEvent);
                if (!configEvent.getResult().equals(Event.Result.DENY))
                    MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.PostConfigChangedEvent(References.MOD_ID, null, Minecraft.getMinecraft().theWorld != null, false));

                checkSettingsButton();
                checkResetButton();
            }
        }
    }

    private void checkSettingsButton() {
        boolean enableButtons = ConfigurationHandler.getSetting(SoundCategory.MASTER);

        for (GuiButton subButton : this.buttonList)
            if (subButton.id > BUTTON_START_ID + SoundCategory.MASTER.ordinal() && subButton.id < BUTTON_START_ID + NUMBEROFSETTINGS)
                subButton.enabled = enableButtons;
    }

    private void checkResetButton() {
        boolean allSettingsAreDefault = true;
        for (SoundCategory sc : SoundCategory.values())
            if (ConfigurationHandler.getSetting(sc)) {
                allSettingsAreDefault = false;
                break;
            }

        for (GuiButton resetButton : this.buttonList)
            if (resetButton.id == BUTTON_START_ID + NUMBEROFSETTINGS + 1) {
                resetButton.enabled = !allSettingsAreDefault;
                break;
            }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, guiTitle, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String getDisplayString(SoundCategory sc) {
        if (sc == null)
            return I18n.format("gui.musicOnBackground.toggleAll");
        else
            return I18n.format("soundCategory." + sc.getName()) + ": " + (ConfigurationHandler.getSetting(sc) ? I18n.format("options.on") : I18n.format("options.off"));
    }

    private String getDisplayString(String nameOfButton) {
        return I18n.format("gui.musicOnBackground." + nameOfButton);
    }
}
