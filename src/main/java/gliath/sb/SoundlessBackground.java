package gliath.sb;

import gliath.sb.handler.ConfigurationHandler;
import gliath.sb.handler.PostInitGuiEventHandler;
import gliath.sb.handler.SoundEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = References.MOD_ID, name=References.MOD_NAME, version=References.MOD_VERSION, guiFactory = References.MOD_GUIFACTORY, clientSideOnly = true)
public class SoundlessBackground {

    @Mod.Instance(References.MOD_ID)
    public static SoundlessBackground instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        ModLogger.info("Registering handlers");
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
        MinecraftForge.EVENT_BUS.register(new PostInitGuiEventHandler());

        SoundEventHandler seHandler = new SoundEventHandler();
        MinecraftForge.EVENT_BUS.register(seHandler);
        FMLCommonHandler.instance().bus().register(seHandler);
    }
}