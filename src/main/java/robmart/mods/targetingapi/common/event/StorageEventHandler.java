package robmart.mods.targetingapi.common.event;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import robmart.mods.targetingapi.api.Targeting;
import robmart.mods.targetingapi.api.faction.IFaction;

import java.io.File;

@Mod.EventBusSubscriber
public class StorageEventHandler {

    //TODO: Probably not the intended way to do this. Find a better way.

    @SubscribeEvent
    public static void onServerStarting(final FMLServerStartingEvent event){
        File saveDir = new File(event.getServer().getDataDirectory().getAbsolutePath() +  "\\saves\\" +
                event.getServer().getFolderName());
        File factionDir = new File(saveDir.getAbsolutePath() + "\\factions");
        if (factionDir.exists()) {

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
