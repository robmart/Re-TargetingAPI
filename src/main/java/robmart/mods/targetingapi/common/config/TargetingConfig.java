package robmart.mods.targetingapi.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import robmart.mods.targetingapi.api.reference.Reference;
import robmart.mods.targetingapi.common.TargetingAPI;

import java.io.File;

@Config(modid = Reference.MOD_ID, category = "gameplay")
public class TargetingConfig {

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        try {
            config.load();
        } catch (Exception e) {
            TargetingAPI.logger.info("Error loading config, returning to default variables.");
        } finally {
            if (config.hasChanged())
                config.save();
        }
    }
}
