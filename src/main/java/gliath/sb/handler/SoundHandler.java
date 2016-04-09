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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.Display;

public class SoundHandler {
    private float[] soundLevels;
    private boolean mcHasInitiated;

    public SoundHandler() {
        soundLevels = new float[SoundCategory.values().length];
        mcHasInitiated = false;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPostInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiMainMenu && !mcHasInitiated) {
            mcHasInitiated = true;
        }
    }

    @SubscribeEvent
    public void handleRenderTickEvent(RenderTickEvent event) {
        try {
            boolean hasDisplayedConsoleMessage = false;
            if (mcHasInitiated && !Display.isActive()) {
                GameSettings gs = Minecraft.getMinecraft().gameSettings;
                for (SoundCategory sc : SoundCategory.values())
                    if (!ConfigurationHandler.getSetting(sc) && soundLevels[sc.ordinal()] == 0.0f) {
                        if (!hasDisplayedConsoleMessage) {
                            ModLogger.info("Window inactive, silencing sound");
                            hasDisplayedConsoleMessage = true;
                        }

                        soundLevels[sc.ordinal()] = gs.getSoundLevel(sc);
                        gs.setSoundLevel(sc, 0.0f);
                    }
            } else if (Display.isActive()) {
                for (SoundCategory sc : SoundCategory.values())
                    if (soundLevels[sc.ordinal()] != 0.0f) {
                        if (!hasDisplayedConsoleMessage) {
                            ModLogger.info("Window active, restoring the sound");
                            hasDisplayedConsoleMessage = true;
                        }

                        Minecraft.getMinecraft().gameSettings.setSoundLevel(sc, soundLevels[sc.ordinal()]);
                        soundLevels[sc.ordinal()] = 0.0f;
                    }
            }
        } catch (Exception e) {
            ModLogger.error(e.getMessage());
        }
    }
}