package com.chaosbuffalo.targeting_api.integration;

import com.chaosbuffalo.targeting_api.Faction;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;

import java.util.function.BiFunction;

/**
 * Created by Jacob on 4/18/2018.
 */
public class Integrations {

    public static boolean isLycanitesPresent() {
        return Loader.isModLoaded("lycanitesmobs");
    }

    private static void setupLycanites(){
        if (!isLycanitesPresent()){
            return;
        }
        Targeting.registerFriendlyEntity("com.lycanitesmobs.elementalmobs.entity.EntityNymph");
        Targeting.registerFriendlyEntity("com.lycanitesmobs.elementalmobs.entity.EntityWisp");
        BiFunction<Entity, Entity, Boolean> lycanitesWrapper = (caster, target) -> {
            return Targeting.isValidTarget(Targeting.TargetType.ENEMY, caster, target, true);
        };
        com.lycanitesmobs.api.Targeting.registerCallback(lycanitesWrapper);
    }

    public static void setupMinecraft(){

        Faction animals = new Faction("FarmAnimals");
        animals.addMember(EntityChicken.class);
        animals.addMember(EntitySheep.class);
        animals.addMember(EntityCow.class);
        animals.addMember(EntityDonkey.class);
        animals.addMember(EntityHorse.class);
        animals.addMember(EntityLlama.class);
        animals.addMember(EntityMooshroom.class);
        animals.addMember(EntityMule.class);
        animals.addMember(EntityPig.class);
        animals.addMember(EntityRabbit.class);
        Targeting.registerFaction(animals);
        animals.addFriendClass(EntityPlayer.class);
    }

    public static void setup(){
        setupLycanites();
    }
}
