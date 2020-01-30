package com.chaosbuffalo.targeting_api.common.faction;

import com.chaosbuffalo.targeting_api.api.faction.IFaction;
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

    public String getName() {
        return this.name;
    }

    public void addFriendClass(Class<? extends Entity> classToAdd) {
        if (!isFriend(classToAdd))
            friendClasses.add(classToAdd);
    }

    public void addMemberClass(Class<? extends Entity> classToAdd){
        if (!isMember(classToAdd))
            memberClasses.add(classToAdd);
    }

    public void clearMembers(){
        memberClasses.clear();
    }

    public boolean isMember(Class<? extends Entity> potentialMember){
        if (potentialMember == null) return false;
        for (Class<? extends Entity> member : memberClasses){
            return member.isAssignableFrom(potentialMember);
        }

        return false;
    }

    public boolean isFriend(Class<? extends Entity> potentialFriend){
        if (potentialFriend == null) return false;
        for (Class<? extends Entity> member : friendClasses){
            return member.isAssignableFrom(potentialFriend);
        }

        return false;
    }
}
