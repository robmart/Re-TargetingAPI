package com.chaosbuffalo.targeting_api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.File;

@Config(modid = TargetingAPI.MODID, category = "gameplay")
public class TargetingConfig {

    @Config.Comment("Class Names for Friendly Entities")
    public static String[] FRIENDLY_ENTITIES = {
            "com.lycanitesmobs.elementalmobs.entity.EntityNymph",
            "com.lycanitesmobs.elementalmobs.entity.EntityWisp"
    };

    @Config.Comment("Class Names for Friendly Entities")
    public static String[] FARM_ANIMALS = {
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
            Log.info("Error loading config, returning to default variables.");
        } finally {
            TargetingConfig.registerFriendlyEntities();
            TargetingConfig.registerFarmAnimals();
            if (config.hasChanged())
                config.save();
        }
    }

    public static void registerFarmAnimals(){
        Faction farmAnimals = Targeting.getFaction("FarmAnimals");
        farmAnimals.clearMembers();
        for (String farmAnimal : FARM_ANIMALS){
            ResourceLocation loc = new ResourceLocation(farmAnimal);
            Class<? extends Entity> entityclass = EntityList.getClass(loc);
            if (entityclass != null){
                farmAnimals.addMember(entityclass);
            } else {
                Log.info("Entity %s not registered, skipping", farmAnimal);
            }
        }
    }

    public static void registerFriendlyEntities(){
        Targeting.clearFriendlyEntities();
        for (String friendlyClass : FRIENDLY_ENTITIES){
            Log.info("%s registered as friendly entity.", friendlyClass);
            Targeting.registerFriendlyEntity(friendlyClass);
        }
    }
}
