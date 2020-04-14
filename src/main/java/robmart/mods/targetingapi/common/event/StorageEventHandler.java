package robmart.mods.targetingapi.common.event;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import robmart.mods.targetingapi.api.Targeting;
import robmart.mods.targetingapi.api.faction.Faction;
import robmart.mods.targetingapi.api.faction.IFaction;
import robmart.mods.targetingapi.common.TargetingAPI;
import robmart.mods.targetingapi.common.helper.NBTHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Mod.EventBusSubscriber
public class StorageEventHandler {

//    @SubscribeEvent
//    public static void debugDamage(final LivingHurtEvent event) {
//        IFaction faction = Targeting.getFaction("FarmAnimals1");
//        Entity entity = event.getEntity();
//        faction.addMemberEntity(entity);
//    }

    //TODO: Probably not the intended way to do this. Find a better way.

    private static void loadFactions(final Event event) {
        MinecraftServer server = event instanceof FMLServerStartingEvent ? ((FMLServerStartingEvent) event).getServer() : ((PlayerEvent.PlayerLoggedInEvent) event).getEntity().getServer();
        File saveDir = new File(server.getDataDirectory().getAbsolutePath() +  "\\saves\\" +
                server.getFolderName());
        File factionDir = new File(saveDir.getAbsolutePath() + "\\factions");
        if (factionDir.exists()) {
            for (String fileName : factionDir.list()) {
                File file = new File(factionDir + "\\" + fileName);
                if (file.isFile() && file.exists() && fileName.contains(".dat")) {
                    CompoundNBT nbt = NBTHelper.getNBTFromFile(file);
                    if (nbt != null) {
                        IFaction faction = new Faction(server.getWorld(DimensionType.OVERWORLD), nbt.getCompound("data").getString("Name"), true);
                        ((WorldSavedData) faction).read(nbt);
                        Targeting.registerFaction(faction);
                    }
                }
            }
        } else {
            factionDir.mkdir();
        }
    }

    @SubscribeEvent
    public static void onServerStarting(final FMLServerStartingEvent event){
        loadFactions(event);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        loadFactions(event);
    }

    @SubscribeEvent
    public static void onWorldSave(final WorldEvent.Save event) {
        File saveDir = new File(event.getWorld().getWorld().getServer().getDataDirectory().getAbsolutePath() +
                "\\saves\\" + event.getWorld().getWorld().getServer().getFolderName());
        File factionDir = new File(saveDir.getAbsolutePath() + "\\factions");
        ImmutableMap<String, IFaction> factionMap = Targeting.getFactionMap();
        if (factionDir.exists()) {
            if (factionDir.isFile()) {
                factionDir.delete();
                factionDir.mkdir();
            }

            for (IFaction faction : factionMap.values()) {
                if (faction.getIsPermanent()) {
                    ((WorldSavedData) faction).save(new File(factionDir.getAbsolutePath() + "\\" + faction.getName() + ".dat"));
                }
            }
        } else {
            factionDir.mkdir();
        }
    }
}
