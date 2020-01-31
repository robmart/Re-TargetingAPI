package com.chaosbuffalo.targeting_api.api.faction;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;

import java.util.Set;

/**
 * Created by Jacob on 4/19/2018.
 */
public class Faction implements IFaction {
    //TODO: Enemies, Remove friends/members/enemies, Individual friends/members/enemies

    private String name;

    private Set<Class<? extends Entity>> memberClasses = Sets.newHashSet();
    private Set<Class<? extends Entity>> friendClasses = Sets.newHashSet();

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
    public void addMemberClass(Class<? extends Entity> classToAdd){
        if (!isMember(classToAdd))
            memberClasses.add(classToAdd);
    }

    @Override
    public void clearMembers(){
        memberClasses.clear();
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
        return isMember(potentialMember.getClass());
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
        return isFriend(potentialFriend.getClass());
    }
}
