package com.chaosbuffalo.targeting_api;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;

import java.util.Set;

/**
 * Created by Jacob on 4/19/2018.
 */
public class Faction {

    public String name;

    public Faction(String name) {
        this.name = name;
    }

    private Set<Class<? extends Entity>> members = Sets.newHashSet();

    private Set<Class<? extends Entity>> friendClasses = Sets.newHashSet();

    public void addFriendClass(Class<? extends Entity> classToAdd) {
        friendClasses.add(classToAdd);
    }

    public void addMember(Class<? extends Entity> classToAdd){
        members.add(classToAdd);
    }

    public void clearMembers(){
        members.clear();
    }

    public boolean isMember(Class<? extends Entity> potentialMember){
        if (potentialMember != null){
            for (Class<? extends Entity> member : members){
                if (member.isAssignableFrom(potentialMember)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFriend(Class<? extends Entity> potentialFriend){

        if (potentialFriend != null){
            for (Class<? extends Entity> member : friendClasses){
                if (member.isAssignableFrom(potentialFriend)){
                    return true;
                }
            }
        }
        return false;
    }
}
