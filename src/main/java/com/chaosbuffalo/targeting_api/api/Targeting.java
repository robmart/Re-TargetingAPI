package com.chaosbuffalo.targeting_api.api;

import com.chaosbuffalo.targeting_api.api.faction.IFaction;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;

import java.util.Map;

public class Targeting {

    private static Map<String, IFaction> factionMap = Maps.newHashMap();

    /**
     * Register a new faction
     * @param newFaction The faction that should be registered
     */
    public static void registerFaction(IFaction newFaction){
        factionMap.put(newFaction.getName(), newFaction);
    }

    /**
     * Gets faction from name
     * @param factionName The name of the requested faction
     * @return The requested faction
     */
    public static IFaction getFaction(String factionName){
        return factionMap.get(factionName);
    }

    /**
     * Checks if target is valid
     * @param type The target type used
     * @param caster The caster
     * @param target The target
     * @param excludeCaster Whether to exclude the caster. Can target caster if false
     * @return Whether the target is valid
     */
    public static boolean isValidTarget(TargetType type, Entity caster, Entity target, boolean excludeCaster) {
        if (caster == null || target == null) {
            return false;
        }
        if (!(target instanceof EntityLivingBase)){
            return false;
        }
        if (excludeCaster && caster.equals(target)) {
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
                return caster.equals(target);
            case PLAYERS:
                return target instanceof EntityPlayer;
            case FRIENDLY:
                return isFriendly(caster, target);
            case ENEMY:
                return isValidEnemy(caster, target);
        }
        return false;
    }

    /**
     * Check if two entities are on the same scoreboard team
     * @param caster Entity one
     * @param target Entity two
     * @return Whether the two entities are on the same scoreboard team
     */
    public static boolean isSameTeam(Entity caster, Entity target) {
        Team myTeam = caster.getTeam();
        Team otherTeam = target.getTeam();
        return myTeam != null && otherTeam != null && myTeam.isSameTeam(otherTeam);
    }

    /**
     * Check if two players are friendly
     * @param caster Player one
     * @param target Player two
     * @return If the two players are friendly
     */
    public static boolean isFriendlyPlayer(Entity caster, Entity target) {
        if (caster.equals(target)) return true;
        if (isSameTeam(caster, target)) return true;
        return checkIfSameFaction(caster, target);
    }

    /**
     * Checks if the rideable/ownable entity is owned
     * @param target The rideable/ownable entity
     * @return Whether the rideable/ownable entity is owned
     */
    public static boolean isPlayerControlled(Entity target) {
        Entity controller = target.getControllingPassenger();
        if (controller instanceof EntityPlayer) {
            return true;
        }
        if (target instanceof IEntityOwnable) {
            IEntityOwnable ownable = (IEntityOwnable) target;
            // Entity is owned, but the owner is offline
            // If the owner if offline then there's not much we can do.
            // Returning false for right now due to Lycanites AI quirks
            return ownable.getOwnerId() != null;
        }
        return false;
    }

    /**
     * Checks if rideable/ownable entity is friendly with rider
     * @param caster The rider/owner
     * @param target The rideable/ownable entity
     * @return Whether rideable/ownable entity is friendly with rider
     */
    public static boolean isFriendlyPlayerControlled(Entity caster, Entity target) {
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

    /**
     * Check whether caster's faction is friends with target
     * @param caster The member of the faction
     * @param target The target being evaluated
     * @return Whether caster's faction is friends with target
     */
    public static boolean checkFactionFriends(Entity caster, Entity target){
        for (IFaction faction : factionMap.values()){
            if (faction.isMember(caster) && faction.isFriend(target)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if two entities are in the same faction
     * @param caster Entity one
     * @param target Entity two
     * @return Whether they are in the same faction or not
     */
    public static boolean checkIfSameFaction(Entity caster, Entity target){
        for (IFaction faction : factionMap.values()){
            if (faction.isMember(target.getClass()) && faction.isMember(caster.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if two caster is friends with target
     * @param caster Caster
     * @param target Target
     * @return Whether they are friendly or not
     */
    public static boolean isFriendly(Entity caster, Entity target) {
        if (checkIfSameFaction(caster, target)) return true;
        return checkFactionFriends(caster, target);
    }

    /**
     * Check if the target is enemy of caster
     * @param caster Caster
     * @param target Target
     * @return Whether they are enemies or not
     */
    public static boolean isValidEnemy(Entity caster, Entity target) { //TODO Proper enemies
        return !isFriendly(caster, target);
    }

}
