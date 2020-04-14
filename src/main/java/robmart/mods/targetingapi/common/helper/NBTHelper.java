package robmart.mods.targetingapi.common.helper;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import robmart.mods.targetingapi.common.TargetingAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class NBTHelper {

    public static CompoundNBT getNBTFromFile(File file) {
        CompoundNBT nbt = null;
        try {
            FileInputStream fileinputstream = new FileInputStream(file);
            nbt = CompressedStreamTools.readCompressed(fileinputstream);
        } catch (IOException ioexception) {
            TargetingAPI.logger.error("Could not load data", ioexception);
        }

        return nbt;
    }
}
