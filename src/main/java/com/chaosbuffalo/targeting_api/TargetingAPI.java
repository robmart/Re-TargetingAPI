package com.chaosbuffalo.targeting_api;

import com.chaosbuffalo.targeting_api.integration.Integrations;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = TargetingAPI.MODID, name = TargetingAPI.NAME, version = TargetingAPI.VERSION)
public class TargetingAPI
{
    public static final String MODID = "targeting_api";
    public static final String NAME = "Targeting API";
    public static final String VERSION = "0.1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Integrations.setup();
    }
}
