package com.chaosbuffalo.targeting_api.common;
import com.chaosbuffalo.targeting_api.api.TargetType;
import com.chaosbuffalo.targeting_api.api.faction.IFaction;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class Targeting {

    private static Map<String, IFaction> factionMap = Maps.newHashMap();

    private static class Association {
        public Class Source;
        public Class Target;
        public com.chaosbuffalo.targeting_api.api.TargetType TargetType;
    }

    private static boolean areEntitiesEqual(Entity first, Entity second) {
        return first != null && second != null && first.getUniqueID().compareTo(second.getUniqueID()) == 0;
    }

    public static void registerFaction(IFaction newFaction){
        factionMap.put(newFaction.getName(), newFaction);
    }

    public static IFaction getFaction(String factionName){
        return factionMap.get(factionName);
    }

    public static boolean isFriendlyWithPlayers(Entity target){
        for (IFaction faction: factionMap.values()) {
            if (faction.isMember(target.getClass()) && faction.isFriend(EntityPlayer.class))
                return true;
        }

        return false;
    }

    public static boolean isValidTarget(TargetType type, Entity caster, Entity target, boolean excludeCaster) {
        if (caster == null || target == null) {
            return false;
        }
        if (!(target instanceof EntityLivingBase)){
            return false;
        }
        if (excludeCaster && areEntitiesEqual(caster, target)) {
            return false;
        }
        // Targets should be alive
        if (!target.isEntityAlive())
            return false;

        // Ignore spectators
        if (target instanceof EntityPlayer && ((EntityPlayer) target).isSpectator())
            return false;

        // Ignore Creative Mode players
        if (target instanceof EntityPlayer && ((EntityPlayer) target).isCreative())
            return false;

        switch (type) {
            case ALL:
                return true;
            case SELF:
                return areEntitiesEqual(caster, target);
            case PLAYERS:
                return target instanceof EntityPlayer;
            case FRIENDLY:
                return isFriendly(caster, target);
            case ENEMY:
                return isValidEnemy(caster, target);
        }
        return false;
    }

    private static boolean isSameTeam(Entity caster, Entity target) {
        Team myTeam = caster.getTeam();
        Team otherTeam = target.getTeam();
        return myTeam != null && otherTeam != null && myTeam.isSameTeam(otherTeam);
    }

    private static boolean isFriendlyPlayer(Entity caster, Entity target) {

        if (areEntitiesEqual(caster, target))
            return true;

        // Currently the only friendly player check is if the caster and target are on the same team
        if (isSameTeam(caster, target))
            return true;

        return false;
    }

    private static boolean isPlayerControlled(Entity target) {
        Entity controller = target.getControllingPassenger();
        if (controller instanceof EntityPlayer) {
            return true;
        }
        if (target instanceof IEntityOwnable) {
            IEntityOwnable ownable = (IEntityOwnable) target;
            if (ownable.getOwnerId() != null) {
                // Entity is owned, but the owner is offline
                // If the owner if offline then there's not much we can do.
                // Returning false for right now due to Lycanites AI quirks
                return true;
            }
        }
        return false;
    }

    private static boolean isFriendlyPlayerControlled(Entity caster, Entity target) {
        Entity controller = target.getControllingPassenger();
        if (controller instanceof EntityPlayer) {
            return isFriendly(caster, controller);
        }

        if (target instanceof IEntityOwnable) {
            IEntityOwnable ownable = (IEntityOwnable) target;

            Entity owner = ownable.getOwner();
            if (owner != null) {
                // Owner is online, perform the normal checks
                return isFriendly(caster, owner);
            } else if (ownable.getOwnerId() != null) {
                // Entity is owned, but the owner is offline
                // If the owner if offline then there's not much we can do.
                // Returning false for right now due to Lycanites AI quirks
                return false;
            }
        }

        return false;
    }

    private static boolean checkFactionFriends(Entity caster, Entity target){
        for (IFaction f : factionMap.values()){
            if (f.isMember(target.getClass()) && f.isFriend(caster.getClass())){
                return true;
            }
        }
        return false;
    }

    private static boolean checkFactionMembers(Entity caster, Entity target){
        for (IFaction f : factionMap.values()){
            if (f.isMember(target.getClass()) && f.isMember(caster.getClass())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFriendly(Entity caster, Entity target) { //TODO Individual
        for (IFaction faction: factionMap.values()) {
            if (faction.isMember(caster.getClass()) && faction.isFriend(target.getClass()))
                return true;
        }

        return false;
    }

    private static boolean isValidEnemy(Entity caster, Entity target) { //TODO Proper enemies
        return !isFriendly(caster, target);
    }

}
