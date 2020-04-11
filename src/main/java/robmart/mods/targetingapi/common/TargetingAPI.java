package robmart.mods.targetingapi.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import robmart.mods.targetingapi.client.ClientProxy;
import robmart.mods.targetingapi.common.config.ConfigHandler;

import static robmart.mods.targetingapi.api.reference.Reference.*;

@Mod(MOD_ID)
public class TargetingAPI {

    public static TargetingAPI instance;

    public TargetingAPI() {
        instance = this;
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
    }

    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MOD_ID.toUpperCase());

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        proxy.commonSetup(event);
    }
}
