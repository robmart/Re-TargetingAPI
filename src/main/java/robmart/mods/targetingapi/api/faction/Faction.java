package robmart.mods.targetingapi.api.faction;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;

import java.util.Set;

/**
 * Created by Jacob on 4/19/2018.
 * Remade by Robmart on 1/30/2020
 */
public class Faction implements IFaction {
    private String name;

    private Set<Class<? extends Entity>> memberClasses = Sets.newHashSet();
    private Set<Class<? extends Entity>> friendClasses = Sets.newHashSet();
    private Set<Class<? extends Entity>> enemyClasses = Sets.newHashSet();
    private Set<Entity> memberEntities = Sets.newHashSet();
    private Set<Entity> friendEntities = Sets.newHashSet();
    private Set<Entity> enemyEntities = Sets.newHashSet();

    public Faction(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addFriendClass(Class<? extends Entity> classToAdd) {
        if (!isFriend(classToAdd))
            friendClasses.add(classToAdd);
    }

    @Override
    public void addFriendEntity(Entity entityToAdd) {
        if (!isFriend(entityToAdd))
            friendEntities.add(entityToAdd);
    }

    @Override
    public void removeFriendClass(Class<? extends Entity> classToRemove) {
        if (isFriend(classToRemove))
            friendClasses.remove(classToRemove);
    }

    @Override
    public void removeFriendEntity(Entity entityToRemove) {
        if (isFriend(entityToRemove))
            friendEntities.remove(entityToRemove);
    }

    @Override
    public void addMemberClass(Class<? extends Entity> classToAdd){
        if (!isMember(classToAdd))
            memberClasses.add(classToAdd);
    }

    @Override
    public void addMemberEntity(Entity entityToAdd){
        if (!isMember(entityToAdd))
            memberEntities.add(entityToAdd);
    }

    @Override
    public void removeMemberClass(Class<? extends Entity> classToRemove) {
        if (isMember(classToRemove))
            memberClasses.remove(classToRemove);
    }

    @Override
    public void removeMemberEntity(Entity entityToRemove) {
        if (isMember(entityToRemove))
            memberEntities.remove(entityToRemove);
    }

    @Override
    public void addEnemyClass(Class<? extends Entity> classToAdd) {
        if (!isEnemy(classToAdd))
            enemyClasses.add(classToAdd);
    }

    @Override
    public void addEnemyEntity(Entity entityToAdd) {
        if (!isEnemy(entityToAdd))
            enemyEntities.add(entityToAdd);
    }

    @Override
    public void removeEnemyClass(Class<? extends Entity> classToRemove) {
        if (isEnemy(classToRemove))
            enemyClasses.remove(classToRemove);
    }

    @Override
    public void removeEnemyEntity(Entity entityToRemove) {
        if (isEnemy(entityToRemove))
            enemyEntities.remove(entityToRemove);
    }

    @Override
    public void clearMembers(){
        memberClasses.clear();
        memberEntities.clear();
    }

    @Override
    public void clearFriends() {
        friendClasses.clear();
        friendEntities.clear();
    }

    @Override
    public void clearEnemies() {
        enemyClasses.clear();
        enemyEntities.clear();
    }

    @Override
    public boolean isMember(Class<? extends Entity> potentialMember){
        if (potentialMember == null) return false;
        for (Class<? extends Entity> member : memberClasses){
            if (member.isAssignableFrom(potentialMember))
                return true;
        }

        return false;
    }

    @Override
    public boolean isMember(Entity potentialMember){
        if (potentialMember == null) return false;
        if (isMember(potentialMember.getClass())) return true;
        for (Entity entity : memberEntities) {
            if (entity.equals(potentialMember)) return true;
        }

        return false;
    }

    @Override
    public boolean isFriend(Class<? extends Entity> potentialFriend){
        if (potentialFriend == null) return false;
        for (Class<? extends Entity> member : friendClasses){
            if (member.isAssignableFrom(potentialFriend))
                return true;
        }

        return false;
    }

    @Override
    public boolean isFriend(Entity potentialFriend){
        if (potentialFriend == null) return false;
        if (isFriend(potentialFriend.getClass())) return true;
        for (Entity entity : friendEntities) {
            if (entity.equals(potentialFriend)) return true;
        }

        return false;
    }

    @Override
    public boolean isEnemy(Class<? extends Entity> potentialEnemy){
        if (potentialEnemy == null) return false;
        for (Class<? extends Entity> member : enemyClasses){
            if (member.isAssignableFrom(potentialEnemy))
                return true;
        }

        return false;
    }

    @Override
    public boolean isEnemy(Entity potentialEnemy){
        if (potentialEnemy == null) return false;
        if (isFriend(potentialEnemy.getClass())) return true;
        for (Entity entity : enemyEntities) {
            if (entity.equals(potentialEnemy)) return true;
        }

        return false;
    }
}
