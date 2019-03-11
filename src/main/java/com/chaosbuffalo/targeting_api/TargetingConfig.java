package com.chaosbuffalo.targeting_api;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;

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
            "net.minecraft.entity.passive.EntityChicken",
            "net.minecraft.entity.passive.EntitySheep",
            "net.minecraft.entity.passive.EntityCow",
            "net.minecraft.entity.passive.EntityDonkey",
            "net.minecraft.entity.passive.EntityHorse",
            "net.minecraft.entity.passive.EntityLlama",
            "net.minecraft.entity.passive.EntityMooshroom",
            "net.minecraft.entity.passive.EntityMule",
            "net.minecraft.entity.passive.EntityPig",
            "net.minecraft.entity.passive.EntityRabbit",
            "net.minecraft.entity.passive.EntityParrot",
            "net.minecraft.entity.passive.EntityOcelot",
            "net.minecraft.entity.passive.EntityWolf",
            "net.minecraft.entity.passive.EntitySquid"
    };

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        try {
            config.load();
        } catch (Exception e) {
            Log.info("Error loading config, returning to default variables.");
        } finally {
            if (config.hasChanged())
                config.save();
        }
    }

    public static void registerFarmAnimals(){
        Faction farmAnimals = Targeting.getFaction("FarmAnimals");
        farmAnimals.clearMembers();
        for (String farmAnimal : FARM_ANIMALS){
            try {
                Class mobClass = Class.forName(farmAnimal);
                if (Entity.class.isAssignableFrom(mobClass)){
                    farmAnimals.addMember(mobClass);
                    Log.info("%s registered to farm animal faction.", mobClass.getName());
                } else {
                    Log.info("Entity not assignable from %s, skipping", farmAnimal);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.info("Failed to load class for %s", farmAnimal);
            }
        }
    }

    public static void registerFriendlyEntities(){
        for (String friendlyClass : FRIENDLY_ENTITIES){
            Log.info("%s registered as friendly entity.", friendlyClass);
            Targeting.registerFriendlyEntity(friendlyClass);
        }
    }
}
