package com.chaosbuffalo.targeting_api;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = TargetingAPI.MODID, name = TargetingAPI.NAME, version = TargetingAPI.VERSION)
public class TargetingAPI
{
    public static final String MODID = "targeting_api";
    public static final String NAME = "Targeting API";
    public static final String VERSION = "0.13";

    public static Logger logger;
    private static File suggested;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        suggested = event.getSuggestedConfigurationFile();

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        TargetingConfig.init(suggested);
    }
}
