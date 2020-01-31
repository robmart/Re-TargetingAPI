package robmart.mods.targetingapi.api.faction;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;

import java.util.Set;

/**
 * Created by Jacob on 4/19/2018.
 */
public class Faction implements IFaction {
    //TODO: Enemies, Remove friends/members/enemies, Individual enemies

    private String name;

    private Set<Class<? extends Entity>> memberClasses = Sets.newHashSet();
    private Set<Class<? extends Entity>> friendClasses = Sets.newHashSet();
    private Set<Entity> memberEntities = Sets.newHashSet();
    private Set<Entity> friendEntities = Sets.newHashSet();

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
        if (isFriend(potentialFriend.getClass())) return true;
        for (Entity entity : friendEntities) {
            if (entity.equals(potentialFriend)) return true;
        }

        return false;
    }
}
