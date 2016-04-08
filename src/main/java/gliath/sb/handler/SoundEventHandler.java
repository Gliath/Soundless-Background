package gliath.sb.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gliath.sb.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.Display;

public class SoundEventHandler {
    private float soundLevel;
    private static boolean mcHasInitiated;

    public SoundEventHandler() {
        soundLevel = 0.0f;
        mcHasInitiated = false;
    }

    public static boolean isMcInitiated() {
        return mcHasInitiated;
    }

    public static void mcHasInitiated() {
        mcHasInitiated = true;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void handleRenderTickEvent(RenderTickEvent event) {
        try {
            if (mcHasInitiated && !Display.isActive() && !ConfigurationHandler.musicInBackground && soundLevel == 0.0f) {
                ModLogger.info("Window not active, silencing the sound");
                GameSettings gs = Minecraft.getMinecraft().gameSettings;
                soundLevel = gs.getSoundLevel(SoundCategory.MASTER);
                gs.setSoundLevel(SoundCategory.MASTER, 0.0f);
            } else if (Display.isActive() && soundLevel != 0.0f) {
                ModLogger.info("Window active, restoring the sound");
                Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MASTER, soundLevel);
                soundLevel = 0.0f;
            }
        } catch (Exception e) {
            ModLogger.error(e.getMessage());
        }
    }
}