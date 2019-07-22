package com.chaosbuffalo.targeting_api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

@Config(modid = TargetingAPI.MODID, category = "gameplay")
public class TargetingConfig {

    @Config.Comment("Registry Names for Friendly Entities")
    public static String[] FRIENDLY_ENTITIES = {
            "elementalmobs:nymph",
            "elementalmobs:wisp",
            "mowziesmobs:lantern",
            "lootablebodies:corpse"
    };

    @Config.Comment("Registry Names for Farm Animals")
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
                Log.info("Entity %s registered as farm animal.", farmAnimal);
            } else {
                Log.info("Entity %s not registered, skipping", farmAnimal);
            }
        }
    }

    public static void registerFriendlyEntities(){
        Targeting.clearFriendlyEntities();
        for (String friendlyClass : FRIENDLY_ENTITIES){
            ResourceLocation loc = new ResourceLocation(friendlyClass);
            Class<? extends Entity> entityclass = EntityList.getClass(loc);
            if (entityclass != null){
                Targeting.registerFriendlyEntity(entityclass.getName());
                Log.info("%s registered as friendly entity.", friendlyClass);
            } else {
                Log.info("Entity %s not registered, skipping", friendlyClass);
            }
        }
    }
}
