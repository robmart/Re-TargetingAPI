package com.chaosbuffalo.targeting_api.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.chaosbuffalo.targeting_api.api.reference.Reference.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION)
public class TargetingAPI {

    public static final Logger logger = LogManager.getLogger(MOD_ID.toUpperCase());
    private static File suggested;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        suggested = event.getSuggestedConfigurationFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        TargetingConfig.init(suggested);
    }
}
