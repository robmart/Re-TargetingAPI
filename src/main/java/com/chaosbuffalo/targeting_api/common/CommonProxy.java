package com.chaosbuffalo.targeting_api.common;

import com.chaosbuffalo.targeting_api.api.Targeting;
import com.chaosbuffalo.targeting_api.api.faction.IFaction;
import com.chaosbuffalo.targeting_api.common.config.TargetingConfig;
import com.chaosbuffalo.targeting_api.api.faction.Faction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

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
    private static File suggested;

    public void preInit(FMLPreInitializationEvent event) {
        suggested = event.getSuggestedConfigurationFile();

        IFaction animals = new Faction("FarmAnimals");
        animals.addFriendClass(EntityPlayer.class);
        Targeting.registerFaction(animals);
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
        TargetingConfig.init(suggested);
    }
}
