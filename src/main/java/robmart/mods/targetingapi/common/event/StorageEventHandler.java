package robmart.mods.targetingapi.common.event;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import robmart.mods.targetingapi.api.Targeting;
import robmart.mods.targetingapi.api.faction.Faction;
import robmart.mods.targetingapi.api.faction.IFaction;
import robmart.mods.targetingapi.common.helper.NBTHelper;

import java.io.File;

@Mod.EventBusSubscriber
public class StorageEventHandler {

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
                        Faction faction = new Faction(server.getWorlds().iterator().next(), nbt.getCompound("data").getString("Name"), true);
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
        ImmutableMap<String, Faction> factionMap = Targeting.getFactionMap();
        if (factionDir.exists()) {
            if (factionDir.isFile()) {
                factionDir.delete();
                factionDir.mkdir();
            }

            for (Faction faction : factionMap.values()) {
                if (faction.getIsPermanent()) {
                    faction.save(new File(factionDir.getAbsolutePath() + "\\" + faction.getName() + ".dat"));
                }
            }
        } else {
            factionDir.mkdir();
        }
    }
}
