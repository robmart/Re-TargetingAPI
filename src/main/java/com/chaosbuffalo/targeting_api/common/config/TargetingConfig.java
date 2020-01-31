package com.chaosbuffalo.targeting_api.common.config;

import com.chaosbuffalo.targeting_api.api.faction.IFaction;
import com.chaosbuffalo.targeting_api.api.reference.Reference;
import com.chaosbuffalo.targeting_api.api.Targeting;
import com.chaosbuffalo.targeting_api.common.TargetingAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

@Config(modid = Reference.MOD_ID, category = "gameplay")
public class TargetingConfig {

    @Config.Comment("Registry Names for Farm Animals")
    public static String[] FARM_ANIMALS = { //TODO: No
            "minecraft:cow",
            "minecraft:sheep",
            "minecraft:chicken",
            "minecraft:horse",
            "minecraft:llama",
            "minecraft:donkey",
            "minecraft:mule",
            "minecraft:pig",
            "minecraft:parrot",
            "minecraft:rabbit",
            "minecraft:ocelot",
            "minecraft:wolf",
            "minecraft:squid",
            "minecraft:mooshroom"
    };

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        try {
            config.load();
        } catch (Exception e) {
            TargetingAPI.logger.info("Error loading config, returning to default variables.");
        } finally {
            TargetingConfig.registerFarmAnimals();
            if (config.hasChanged())
                config.save();
        }
    }

    public static void registerFarmAnimals(){
        IFaction farmAnimals = Targeting.getFaction("FarmAnimals");
        farmAnimals.clearMembers();
        for (String farmAnimal : FARM_ANIMALS){
            ResourceLocation loc = new ResourceLocation(farmAnimal);
            Class<? extends Entity> entityclass = EntityList.getClass(loc);
            if (entityclass != null){
                farmAnimals.addMemberClass(entityclass);
                TargetingAPI.logger.info("Entity {} registered as farm animal.", farmAnimal);
            } else {
                TargetingAPI.logger.info("Entity {} not registered, skipping", farmAnimal);
            }
        }
    }
}
