package com.chaosbuffalo.targeting_api.api.faction;

import net.minecraft.entity.Entity;

/**
 * Created by Robmart.
 * <p>
 * This software is a modification for the game Minecraft.
 * Copyright (C) 2020 Robmart
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public interface IFaction {

    String getName();
    void addFriendClass(Class<? extends Entity> classToAdd);
    void addFriendEntity(Entity entityToAdd);
    void addMemberClass(Class<? extends Entity> classToAdd);
    void addMemberEntity(Entity entityToAdd);
    void clearMembers();
    boolean isMember(Class<? extends Entity> potentialMember);
    boolean isMember(Entity potentialMember);
    boolean isFriend(Class<? extends Entity> potentialFriend);
    boolean isFriend(Entity potentialFriend);
}
