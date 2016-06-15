package gliath.sb.handler;

import gliath.sb.utilities.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.lwjgl.opengl.Display;

public class SoundHandler {
    private float[] originalSoundLevels;
    private boolean mcHasInitiated;
    private boolean mcHasBeenSilenced;

    public SoundHandler() {
        originalSoundLevels = new float[SoundCategory.values().length];
        for (int i = 0; i < originalSoundLevels.length; i++)
            originalSoundLevels[i] = -1.0f;

        mcHasInitiated = false;
        mcHasBeenSilenced = false;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPostInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiMainMenu && !mcHasInitiated)
            mcHasInitiated = true;
    }

    @SubscribeEvent
    public void handleRenderTickEvent(RenderTickEvent event) {
        if (!mcHasInitiated)
            return;

        try {
            boolean hasDisplayedConsoleMessage = false;
            if (!Display.isActive() && !mcHasBeenSilenced) {
                for (SoundCategory sc : SoundCategory.values())
                    if (((float) ConfigurationHandler.getSetting(sc)) < 100.0f) { // 100.0f means it doesn't silence the sound
                        if (!hasDisplayedConsoleMessage) {
                            ModLogger.info("Window inactive, silencing sound");
                            hasDisplayedConsoleMessage = true;
                            mcHasBeenSilenced = true; // More like: "is going to be silenced"
                        }

                        originalSoundLevels[sc.ordinal()] = Minecraft.getMinecraft().gameSettings.getSoundLevel(sc);
                        float newSoundLevel = originalSoundLevels[sc.ordinal()] * (((float) ConfigurationHandler.getSetting(sc)) / 100.0f);

                        Minecraft.getMinecraft().gameSettings.setSoundLevel(sc, newSoundLevel);
                    }
            } else if (Display.isActive() && mcHasBeenSilenced) {
                mcHasBeenSilenced = false; // Like above, it's going to be unsilenced

                for (SoundCategory sc : SoundCategory.values()) {
                    if (originalSoundLevels[sc.ordinal()] >= 0.0f) {
                        if (!hasDisplayedConsoleMessage) {
                            ModLogger.info("Window active, restoring the sound");
                            hasDisplayedConsoleMessage = true;
                        }

                        Minecraft.getMinecraft().gameSettings.setSoundLevel(sc, originalSoundLevels[sc.ordinal()]);
                        originalSoundLevels[sc.ordinal()] = -1.0f;
                    }
                }
            }
        } catch (Exception e) {
            ModLogger.error(e.getMessage());
        }
    }
}