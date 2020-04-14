package robmart.mods.targetingapi.api.faction;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import robmart.mods.targetingapi.api.reference.Reference;

import java.util.Set;

/**
 * Created by Jacob on 4/19/2018.
 * Remade by Robmart on 1/30/2020
 */
public class Faction extends WorldSavedData implements IFaction {
    private final World world;
    private final String name;
    private final boolean isPermanent;

    private final Set<Class<? extends Entity>> memberClasses = Sets.newHashSet();
    private final Set<Class<? extends Entity>> friendClasses = Sets.newHashSet();
    private final Set<Class<? extends Entity>> enemyClasses = Sets.newHashSet();
    private final Set<Entity> memberEntities = Sets.newHashSet();
    private final Set<Entity> friendEntities = Sets.newHashSet();
    private final Set<Entity> enemyEntities = Sets.newHashSet();

    private final CompoundNBT unprocessedData = new CompoundNBT();

    public Faction(World world, String name) {
        this(world, name, false);
    }

    public Faction(World world, String name, boolean permanent) {
        super(Reference.MOD_ID +  "_Faction_" + name);
        this.world = world;
        this.name = name;
        this.isPermanent = permanent;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean getIsPermanent() {
        return this.isPermanent;
    }

    @Override
    public void addFriendClass(Class<? extends Entity> classToAdd) {
        if (!isFriend(classToAdd))
            this.friendClasses.add(classToAdd);

        markDirty();
    }

    @Override
    public void addFriendEntity(Entity entityToAdd) {
        if (!isFriend(entityToAdd))
            this.friendEntities.add(entityToAdd);

        markDirty();
    }

    @Override
    public void removeFriendClass(Class<? extends Entity> classToRemove) {
        if (isFriend(classToRemove))
            this.friendClasses.remove(classToRemove);

        markDirty();
    }

    @Override
    public void removeFriendEntity(Entity entityToRemove) {
        if (isFriend(entityToRemove))
            this.friendEntities.remove(entityToRemove);

        markDirty();
    }

    @Override
    public void addMemberClass(Class<? extends Entity> classToAdd){
        if (!isMember(classToAdd))
            this.memberClasses.add(classToAdd);

        markDirty();
    }

    @Override
    public void addMemberEntity(Entity entityToAdd){
        if (!isMember(entityToAdd))
            this.memberEntities.add(entityToAdd);

        markDirty();
    }

    @Override
    public void removeMemberClass(Class<? extends Entity> classToRemove) {
        if (isMember(classToRemove))
            this.memberClasses.remove(classToRemove);

        markDirty();
    }

    @Override
    public void removeMemberEntity(Entity entityToRemove) {
        if (isMember(entityToRemove))
            this.memberEntities.remove(entityToRemove);

        markDirty();
    }

    @Override
    public void addEnemyClass(Class<? extends Entity> classToAdd) {
        if (!isEnemy(classToAdd))
            this.enemyClasses.add(classToAdd);

        markDirty();
    }

    @Override
    public void addEnemyEntity(Entity entityToAdd) {
        if (!isEnemy(entityToAdd))
            this.enemyEntities.add(entityToAdd);

        markDirty();
    }

    @Override
    public void removeEnemyClass(Class<? extends Entity> classToRemove) {
        if (isEnemy(classToRemove))
            this.enemyClasses.remove(classToRemove);

        markDirty();
    }

    @Override
    public void removeEnemyEntity(Entity entityToRemove) {
        if (isEnemy(entityToRemove))
            this.enemyEntities.remove(entityToRemove);

        markDirty();
    }

    @Override
    public Set<Object> getAllMembers() {
        Set<Object> memberSet = Sets.newHashSet();
        memberSet.addAll(this.memberClasses);
        memberSet.addAll(this.memberEntities);
        return memberSet;
    }

    @Override
    public Set<Object> getAllFriends() {
        Set<Object> friendSet = Sets.newHashSet();
        friendSet.addAll(this.friendClasses);
        friendSet.addAll(this.friendEntities);
        return friendSet;
    }

    @Override
    public Set<Object> getAllEnemies() {
        Set<Object> enemySet = Sets.newHashSet();
        enemySet.addAll(this.enemyClasses);
        enemySet.addAll(this.enemyEntities);
        return enemySet;
    }

    @Override
    public void clearMembers(){
        this.memberClasses.clear();
        this.memberEntities.clear();

        markDirty();
    }

    @Override
    public void clearFriends() {
        this.friendClasses.clear();
        this.friendEntities.clear();

        markDirty();
    }

    @Override
    public void clearEnemies() {
        this.enemyClasses.clear();
        this.enemyEntities.clear();

        markDirty();
    }

    @Override
    public boolean isMember(Class<? extends Entity> potentialMember){
        if (potentialMember == null) return false;
        for (Class<? extends Entity> member : this.memberClasses){
            if (member.isAssignableFrom(potentialMember))
                return true;
        }

        return false;
    }

    @Override
    public boolean isMember(Entity potentialMember){
        if (potentialMember == null) return false;
        if (isMember(potentialMember.getClass())) return true;
        for (Entity entity : this.memberEntities) {
            if (entity.equals(potentialMember)) return true;
        }

        return false;
    }

    @Override
    public boolean isFriend(Class<? extends Entity> potentialFriend){
        if (potentialFriend == null) return false;
        for (Class<? extends Entity> member : this.friendClasses){
            if (member.isAssignableFrom(potentialFriend))
                return true;
        }

        return false;
    }

    @Override
    public boolean isFriend(Entity potentialFriend){
        if (potentialFriend == null) return false;
        if (isFriend(potentialFriend.getClass())) return true;
        for (Entity entity : this.friendEntities) {
            if (entity.equals(potentialFriend)) return true;
        }

        return false;
    }

    @Override
    public boolean isEnemy(Class<? extends Entity> potentialEnemy){
        if (potentialEnemy == null) return false;
        for (Class<? extends Entity> member : this.enemyClasses){
            if (member.isAssignableFrom(potentialEnemy))
                return true;
        }

        return false;
    }

    @Override
    public boolean isEnemy(Entity potentialEnemy){
        if (potentialEnemy == null) return false;
        if (isFriend(potentialEnemy.getClass())) return true;
        for (Entity entity : this.enemyEntities) {
            if (entity.equals(potentialEnemy)) return true;
        }

        return false;
    }

    //Turn back before it's too late
    //Trust me don't go further

    public void read(CompoundNBT nbt) {
        nbt = nbt.getCompound("data");

        int i = 0;
        while (nbt.contains("MemberClass" + i)) { //For each saved class
            try {
                //Gets the class from string and adds it to the faction
                addMemberClass((Class<? extends Entity>) Class.forName(nbt.getString("MemberClass" + i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }
        i = 0;
        int unprocessedCounter = 0;
        while (nbt.contains("MemberEntity" + i)) { //For each saved entity
            CompoundNBT entityNBT = (CompoundNBT) nbt.get("MemberEntity" + i++);
            try {
                Class<?> entityClass = Class.forName(entityNBT.getString("EntityType"));
                if (PlayerEntity.class.isAssignableFrom(entityClass)) { //Checks if entity is player
                    PlayerEntity player = this.world.getServer().getPlayerList().getPlayerByUUID(entityNBT.getUniqueId("UUID"));
                    if (player != null) { //If player has loaded, joined the server
                        addMemberEntity(player);
                    } else { //If the player hasn't joined the server
                        this.unprocessedData.put("UnprocessedMemberPlayer" + unprocessedCounter++, entityNBT);
                    }
                } else if (Entity.class.isAssignableFrom(entityClass)) { //Checks if it actually is an entity
                      Entity entity = this.world.getServer().getWorld(DimensionType.getById(entityNBT.getInt("Dimension")))
                              .getEntityByUuid(entityNBT.getUniqueId("UUID"));
                      if (entity != null) {
                          addMemberEntity(entity);
                      }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        i = 0;
        while (nbt.contains("FriendClass" + i)) {//For each saved class
            try {
                //Gets the class from string and adds it to the faction
                addFriendClass((Class<? extends Entity>) Class.forName(nbt.getString("FriendClass" + i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }
        i = 0;
        unprocessedCounter = 0;
        while (nbt.contains("FriendEntity" + i)) {//For each saved entity
            CompoundNBT entityNBT = (CompoundNBT) nbt.get("FriendEntity" + i++);
            try {
                Class<?> entityClass = Class.forName(entityNBT.getString("EntityType"));
                if (PlayerEntity.class.isAssignableFrom(entityClass)) {//Checks if entity is player
                    PlayerEntity player = this.world.getServer().getPlayerList().getPlayerByUUID(entityNBT.getUniqueId("UUID"));
                    if (player != null) {//If player has loaded, joined the server
                        addFriendEntity(player);
                    } else {//If the player hasn't joined the server
                        this.unprocessedData.put("UnprocessedFriendPlayer" + unprocessedCounter++, entityNBT);
                    }
                } else if (Entity.class.isAssignableFrom(entityClass)) { //Checks if it actually is an entity
                    Entity entity = this.world.getServer().getWorld(DimensionType.getById(entityNBT.getInt("Dimension")))
                            .getEntityByUuid(entityNBT.getUniqueId("UUID"));
                    if (entity != null) {
                        addFriendEntity(entity);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        i = 0;
        while (nbt.contains("EnemyClass" + i)) {//For each saved class
            try {
                //Gets the class from string and adds it to the faction
                addEnemyClass((Class<? extends Entity>) Class.forName(nbt.getString("EnemyClass" + i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }
        i = 0;
        unprocessedCounter = 0;
        while (nbt.contains("EnemyEntity" + i)) {//For each saved entity
            CompoundNBT entityNBT = (CompoundNBT) nbt.get("EnemyEntity" + i++);
            try {
                Class<?> entityClass = Class.forName(entityNBT.getString("EntityType"));
                if (PlayerEntity.class.isAssignableFrom(entityClass)) {//Checks if entity is player
                    PlayerEntity player = this.world.getServer().getPlayerList().getPlayerByUUID(entityNBT.getUniqueId("UUID"));
                    if (player != null) {//If player has loaded, joined the server
                        addEnemyEntity(player);
                    } else {//If the player hasn't joined the server
                        this.unprocessedData.put("UnprocessedEnemyPlayer" + unprocessedCounter++, entityNBT);
                    }
                } else if (Entity.class.isAssignableFrom(entityClass)) { //Checks if it actually is an entity
                    Entity entity = this.world.getServer().getWorld(DimensionType.getById(entityNBT.getInt("Dimension")))
                            .getEntityByUuid(entityNBT.getUniqueId("UUID"));
                    if (entity != null) {
                        addEnemyEntity(entity);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (!getIsPermanent())
            return compound;

        compound.putString("Name", getName());
        for (int i = 0; i < this.memberClasses.size(); i++) {
            compound.putString("MemberClass" + i, this.memberClasses.toArray()[i].toString().replaceAll("(.*)\\s", ""));
        }

        int imember;
        for (imember = 0; imember < this.memberEntities.size(); imember++) {
            CompoundNBT entityNBT = new CompoundNBT();
            Entity entity = (Entity) this.memberEntities.toArray()[imember];
            entityNBT.putString("EntityType", entity.getClass().getName());
            if (!(entity instanceof PlayerEntity)) {
                entityNBT.putInt("Dimension", entity.dimension.getId());
            }
            entityNBT.putUniqueId("UUID", entity.getUniqueID());
            compound.put("MemberEntity" + imember, entityNBT);
        }

        for (int i = 0; this.unprocessedData.contains("UnprocessedMemberPlayer" + i); i++) {
            compound.put("MemberEntity" + imember++, this.unprocessedData.get("UnprocessedMemberPlayer" + i));
            this.unprocessedData.remove("UnprocessedMemberPlayer" + imember);
        }

        for (int i = 0; i < this.friendClasses.size(); i++) {
            compound.putString("FriendClass" + i, this.friendClasses.toArray()[i].toString().replaceAll("(.*)\\s", ""));
        }

        for (imember = 0; imember < this.friendEntities.size(); imember++) {
            CompoundNBT entityNBT = new CompoundNBT();
            Entity entity = (Entity) this.friendEntities.toArray()[imember];
            entityNBT.putString("EntityType", entity.getClass().getName());
            if (!(entity instanceof PlayerEntity)) {
                entityNBT.putInt("Dimension", entity.dimension.getId());
            }
            entityNBT.putUniqueId("UUID", entity.getUniqueID());
            compound.put("FriendEntity" + imember, entityNBT);
        }

        for (int i = 0; this.unprocessedData.contains("UnprocessedFriendPlayer" + i); i++) {
            compound.put("FriendEntity" + imember++, this.unprocessedData.get("UnprocessedFriendPlayer" + i));
            this.unprocessedData.remove("UnprocessedFriendPlayer" + i);
        }

        for (int i = 0; i < this.enemyClasses.size(); i++) {
            compound.putString("EnemyClass" + i, this.enemyClasses.toArray()[i].toString().replaceAll("(.*)\\s", ""));
        }

        for (imember = 0; imember < this.enemyEntities.size(); imember++) {
            CompoundNBT entityNBT = new CompoundNBT();
            Entity entity = (Entity) this.enemyEntities.toArray()[imember];
            entityNBT.putString("EntityType", entity.getClass().getName());
            if (!(entity instanceof PlayerEntity)) {
                entityNBT.putInt("Dimension", entity.dimension.getId());
            }
            entityNBT.putUniqueId("UUID", entity.getUniqueID());
            compound.put("EnemyEntity" + imember, entityNBT);
        }

        for (int i = 0; this.unprocessedData.contains("UnprocessedEnemyPlayer" + i); i++) {
            compound.put("EnemyEntity" + imember++, this.unprocessedData.get("UnprocessedEnemyPlayer" + i));
            this.unprocessedData.remove("UnprocessedEnemyPlayer" + i);
        }
        return compound;
    }
}
