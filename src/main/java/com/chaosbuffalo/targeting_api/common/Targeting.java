package com.chaosbuffalo.targeting_api.common;
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

    private static Set<String> friendlyEntityTypes = Sets.newHashSet();

    private static Set<Association> associations = Sets.newHashSet();

    private static Set<BiFunction<Entity, Entity, Boolean>> friendlyCallbacks = Sets.newHashSet();

    private static Set<Faction> factions = Sets.newHashSet();

    private static Map<String, Faction> factionMap = Maps.newHashMap();

    static {
        Faction animals = new Faction("FarmAnimals");
        animals.addFriendClass(EntityPlayer.class);
        Targeting.registerFaction(animals);
    }

    public enum TargetType {
        ALL,
        ENEMY,
        FRIENDLY,
        PLAYERS,
        SELF,
    }

    private static class Association {
        public Class Source;
        public Class Target;
        public TargetType TargetType;
    }

    private static boolean areEntitiesEqual(Entity first, Entity second) {
        return first != null && second != null && first.getUniqueID().compareTo(second.getUniqueID()) == 0;
    }

    public static void registerFaction(Faction newFaction){
        factions.add(newFaction);
        factionMap.put(newFaction.name, newFaction);
    }

    public static Faction getFaction(String factionName){
        return factionMap.get(factionName);
    }

    public static void registerFriendlyEntity(String className) {
        friendlyEntityTypes.add(className);
    }

    public static void clearFriendlyEntities(){
        friendlyEntityTypes.clear();
    }

    public static boolean isFriendlyWithPlayers(Entity target){
        Faction farmAnimals = getFaction("FarmAnimals");
        if (target instanceof EntityLiving){
            return isRegisteredFriendly(target) || farmAnimals.isMember(target.getClass()) ||
                    checkFriendlyLiving((EntityLiving) target) || isPlayerControlled(target);
        } else {
            return isRegisteredFriendly(target) || farmAnimals.isMember(target.getClass())
                    || isPlayerControlled(target);
        }
    }

    public static void registerFriendlyCallback(BiFunction<Entity, Entity, Boolean> callback) {
        friendlyCallbacks.add(callback);
    }

    public static void registerClassAssociation(Class sourceClass, Class targetClass, TargetType targetType) {
        Association a = new Association();
        a.Source = sourceClass;
        a.Target = targetClass;
        a.TargetType = targetType;
        associations.add(a);
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
                return isValidFriendly(caster, target);
            case ENEMY:
                return isValidEnemy(caster, target);
        }
        return false;
    }

    private static boolean checkFriendlyLiving(EntityLiving target) {
        return target instanceof EntityVillager ||
                target instanceof EntityIronGolem;
    }


    private static boolean isRegisteredFriendly(Entity living) {
        return friendlyEntityTypes.contains(living.getClass().getName());
    }

    private static boolean isValidFriendlyCreature(Entity caster, EntityLivingBase target) {

        if (target instanceof EntityLiving && caster instanceof EntityPlayer) {
            EntityLiving otherMob = (EntityLiving) target;
            return checkFriendlyLiving(otherMob) || isRegisteredFriendly(target);
        } else if (target instanceof EntityLiving && caster instanceof EntityLiving) {
            EntityLiving otherMob = (EntityLiving) target;
            return isRegisteredFriendly(caster) &&
                    (checkFriendlyLiving(otherMob) || isRegisteredFriendly(target));
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
            return isValidFriendly(caster, controller);
        }

        if (target instanceof IEntityOwnable) {
            IEntityOwnable ownable = (IEntityOwnable) target;

            Entity owner = ownable.getOwner();
            if (owner != null) {
                // Owner is online, perform the normal checks
                return isValidFriendly(caster, owner);
            } else if (ownable.getOwnerId() != null) {
                // Entity is owned, but the owner is offline
                // If the owner if offline then there's not much we can do.
                // Returning false for right now due to Lycanites AI quirks
                return false;
            }
        }

        return false;
    }

    private static boolean casterIsFriendlyPlayerControlled(Entity caster, Entity target){
        Entity controller = caster.getControllingPassenger();
        if (controller instanceof EntityPlayer) {
            return isValidFriendly(controller, target);
        }

        if (caster instanceof IEntityOwnable) {
            IEntityOwnable ownable = (IEntityOwnable) caster;

            Entity owner = ownable.getOwner();
            if (owner != null) {
                // Owner is online, perform the normal checks
                return isValidFriendly(owner, target);
            } else if (ownable.getOwnerId() != null) {
                // Entity is owned, but the owner is offline
                // If the owner if offline then there's not much we can do.
                // return true so that we consider everyone our friend
                return true;
            }
        }

        return false;
    }

    private static boolean checkFactionFriends(Entity caster, Entity target){
        for (Faction f : factions){
            if (f.isMember(target.getClass()) && f.isFriend(caster.getClass())){
                return true;
            }
        }
        return false;
    }

    private static boolean checkFactionMembers(Entity caster, Entity target){
        for (Faction f : factions){
            if (f.isMember(target.getClass()) && f.isMember(caster.getClass())) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkAssociation(Entity caster, Entity target, TargetType type) {
        return associations.stream()
                .filter(a -> a.TargetType == type)
                .filter(a -> caster.getClass().isAssignableFrom(a.Source))
                .anyMatch(a -> target.getClass().isAssignableFrom(a.Target));
    }

    private static boolean isValidFriendly(Entity caster, Entity target) {

        // Always friendly with ourselves
        if (areEntitiesEqual(caster, target)) {
            return true;
        }

        // Always friendly with whitelisted entities
        if (caster instanceof EntityPlayer && isRegisteredFriendly(target)) {
            return true;
        }

        // Always friendly with entities on the same team
        if (isSameTeam(caster, target)) {
            return true;
        }

        if (isFriendlyPlayer(caster, target))
            return true;

        if (isFriendlyPlayerControlled(caster, target))
            return true;

        if (casterIsFriendlyPlayerControlled(caster, target))
            return true;

        if (target instanceof EntityLivingBase) {
            EntityLivingBase targetLiving = (EntityLivingBase) target;
            if (isValidFriendlyCreature(caster, targetLiving))
                return true;
        }

        if (checkAssociation(caster, target, TargetType.FRIENDLY))
            return true;

        if (checkFactionMembers(caster, target)){
            return true;
        }

        if (checkFactionFriends(caster, target)){
            return true;
        }

        for (BiFunction<Entity, Entity, Boolean> callback : friendlyCallbacks){
            if (callback.apply(caster, target)){
                return true;
            }
        }

        return false;
    }

    private static boolean isValidEnemy(Entity caster, Entity target) {
        return !isValidFriendly(caster, target);
    }

}
