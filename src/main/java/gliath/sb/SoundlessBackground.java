package gliath.sb;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gliath.sb.handler.ConfigurationHandler;
import gliath.sb.handler.PostInitGuiEventHandler;
import gliath.sb.handler.SoundEventHandler;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = References.MOD_ID, name=References.MOD_NAME, version=References.MOD_VERSION, guiFactory = References.MOD_GUIFACTORY)
public class SoundlessBackground {

    @Mod.Instance(References.MOD_ID)
    public static SoundlessBackground instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        ModLogger.info("Registering handlers");
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
        MinecraftForge.EVENT_BUS.register(new PostInitGuiEventHandler());
        FMLCommonHandler.instance().bus().register(new SoundEventHandler());
    }
}