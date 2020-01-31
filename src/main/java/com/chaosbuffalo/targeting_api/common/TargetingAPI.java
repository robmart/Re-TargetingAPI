package com.chaosbuffalo.targeting_api.common;

import com.chaosbuffalo.targeting_api.common.config.TargetingConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.chaosbuffalo.targeting_api.api.reference.Reference.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION)
public class TargetingAPI {

    @Mod.Instance
    public static TargetingAPI instance = new TargetingAPI();

    @SidedProxy(clientSide = COMMON_PROXY, serverSide = CLIENT_PROXY)
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MOD_ID.toUpperCase());

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
