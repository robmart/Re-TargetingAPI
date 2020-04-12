package robmart.mods.targetingapi.common.event;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import robmart.mods.targetingapi.api.Targeting;
import robmart.mods.targetingapi.api.faction.Faction;
import robmart.mods.targetingapi.api.faction.IFaction;
import robmart.mods.targetingapi.common.TargetingAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Mod.EventBusSubscriber
public class StorageEventHandler {

    //TODO: Probably not the intended way to do this. Find a better way.

    @SubscribeEvent
    public static void onServerStarting(final FMLServerStartingEvent event){
        File saveDir = new File(event.getServer().getDataDirectory().getAbsolutePath() +  "\\saves\\" +
                event.getServer().getFolderName());
        File factionDir = new File(saveDir.getAbsolutePath() + "\\factions");
        if (factionDir.exists()) {
            for (String fileName : factionDir.list()) {
                File file = new File(factionDir + "\\" + fileName);
                CompoundNBT nbt = null;
                if (file.isFile() && file.exists() && fileName.contains(".dat")) {
                    try {
                        FileInputStream fileinputstream = new FileInputStream(file);
                        nbt = CompressedStreamTools.readCompressed(fileinputstream);
                    } catch (IOException ioexception) {
                        TargetingAPI.logger.error("Could not load data", ioexception);
                    }

                    if (nbt != null) {
                        IFaction faction = new Faction(nbt.getCompound("data").getString("Name"), true);
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
