package com.chaosbuffalo.targeting_api.integration;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
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
        System.out.println("Hooking into lycanites.");
        Targeting.registerFriendlyEntity("com.lycanitesmobs.elementalmobs.entity.EntityNymph");
        BiFunction<Entity, Entity, Boolean> lycanitesWrapper = (caster, target) -> {
            return Targeting.isValidTarget(Targeting.TargetType.ENEMY, caster, target, true);
        };
        com.lycanitesmobs.api.Targeting.registerCallback(lycanitesWrapper);
    }

    public static void setup(){
        setupLycanites();
    }
}
