package robmart.mods.targetingapi.common;

import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import robmart.mods.targetingapi.api.Targeting;
import robmart.mods.targetingapi.api.faction.Faction;
import robmart.mods.targetingapi.api.faction.IFaction;
import robmart.mods.targetingapi.common.commands.CommandFactions;

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
public class CommonProxy {

    public void commonSetup(final FMLCommonSetupEvent event) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
        CommandFactions.register(event.getCommandDispatcher());
    }

    public void serverStarted(FMLServerStartedEvent event) {
        Faction animals = new Faction(event.getServer().getWorlds().iterator().next(), "FarmAnimals");
        animals.addFriendClass(PlayerEntity.class);
        animals.addMemberClass(CowEntity.class);
        animals.addMemberClass(SheepEntity.class);
        animals.addMemberClass(ChickenEntity.class);
        animals.addMemberClass(HorseEntity.class);
        animals.addMemberClass(LlamaEntity.class);
        animals.addMemberClass(DonkeyEntity.class);
        animals.addMemberClass(MuleEntity.class);
        animals.addMemberClass(PigEntity.class);
        animals.addMemberClass(ParrotEntity.class);
        animals.addMemberClass(RabbitEntity.class);
        animals.addMemberClass(OcelotEntity.class);
        animals.addMemberClass(WolfEntity.class);
        animals.addMemberClass(SquidEntity.class);
        animals.addMemberClass(MooshroomEntity.class);
        Targeting.registerFaction(animals);
    }
}
