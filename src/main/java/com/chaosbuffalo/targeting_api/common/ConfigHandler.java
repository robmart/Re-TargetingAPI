package com.chaosbuffalo.targeting_api;

import com.chaosbuffalo.targeting_api.api.reference.Reference;
import com.chaosbuffalo.targeting_api.common.TargetingAPI;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ConfigHandler {

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Reference.MOD_ID)) {
            ConfigManager.sync(TargetingAPI.MODID, Config.Type.INSTANCE);
            TargetingConfig.registerFriendlyEntities();
            TargetingConfig.registerFarmAnimals();
        }
    }
}
