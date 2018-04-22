package com.chaosbuffalo.targeting_api;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Jacob on 4/19/2018.
 */
public class Faction {

    public String name;

    public Faction(String name) {
        this.name = name;
    }

    private Set<Class> members = Sets.newHashSet();

    private Set<Class> friendClasses = Sets.newHashSet();

    public void addFriendClass(Class classToAdd) {
        friendClasses.add(classToAdd);
    }

    public void addMember(Class classToAdd){
        members.add(classToAdd);
    }

    public boolean isMember(Class potentialMember){
        return members.contains(potentialMember);
    }

    public boolean isFriend(Class potentialFriend){
        return friendClasses.contains(potentialFriend);
    }
}
