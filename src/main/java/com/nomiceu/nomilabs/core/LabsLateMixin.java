package com.nomiceu.nomilabs.core;

import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.nomiceu.nomilabs.NomiLabs.LOGGER;

@SuppressWarnings("unused")
public class LabsLateMixin implements ILateMixinLoader {
    public static final Map<String, Boolean> modMixinsConfig = ImmutableMap.of(
            LabsValues.DRACONIC_MODID, LabsConfig.modIntegration.draconicEvolutionIntegration.enableDraconicEvolutionIntegration,
            LabsValues.NUCLEARCRAFT_MODID, LabsConfig.modIntegration.enableNuclearCraftIntegration,
            LabsValues.XU2_MODID, LabsConfig.modIntegration.enableExtraUtils2Integration
    );

    @Override
    public List<String> getMixinConfigs() {
       return modMixinsConfig.keySet().stream().map(mod -> "mixins." + LabsValues.LABS_MODID + "." + mod + ".json")
               .collect(Collectors.toList());
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        String[] parts = mixinConfig.split("\\.");

        if (parts.length != 4) {
            LOGGER.fatal("Mixin Config Check Failed! Invalid Length.");
            LOGGER.fatal("Mixin Config: " + mixinConfig);
            return true;
        }

        if (!Objects.equals(parts[1], LabsValues.LABS_MODID)) {
            LOGGER.error("Non Nomi-Labs Mixin Found in Mixin Queue. This is probably an error. Skipping...");
            LOGGER.error("Mixin Config: " + mixinConfig);
            return true;
        }

        if (!Loader.isModLoaded(parts[2])){
            LOGGER.error("Mod '" + parts[2] + "' is not loaded. If this is a normal Nomi-CEu instance, this is probably an error.");
            LOGGER.error("Not Loading Mixin Config " + mixinConfig);
            return false;
        }

        if (!modMixinsConfig.containsKey(parts[2]) || !modMixinsConfig.get(parts[2])) {
            LOGGER.info("Integration for Mod '" + parts[2] + "' is not enabled, or does not exist.");
            LOGGER.info("Not Loading Mixin Config " + mixinConfig);
            return false;
        }

        return true;
    }
}
