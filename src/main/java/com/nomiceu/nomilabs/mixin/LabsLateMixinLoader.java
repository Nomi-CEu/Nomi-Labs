package com.nomiceu.nomilabs.mixin;

import static com.nomiceu.nomilabs.NomiLabs.LOGGER;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraftforge.fml.common.Loader;

import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

import zone.rong.mixinbooter.ILateMixinLoader;

@SuppressWarnings("unused")
public class LabsLateMixinLoader implements ILateMixinLoader {

    public static final Map<String, Boolean> modMixinsConfig = new ImmutableMap.Builder<String, Boolean>()
            .put(LabsValues.DRACONIC_MODID,
                    LabsConfig.modIntegration.draconicEvolutionIntegration.enableDraconicEvolutionIntegration)
            .put(LabsValues.NUCLEARCRAFT_MODID,
                    LabsConfig.modIntegration.enableNuclearCraftIntegration)
            .put(LabsValues.XU2_MODID,
                    LabsConfig.modIntegration.enableExtraUtils2Integration)
            .put(LabsValues.GREGTECH_MODID, true)
            .put(LabsValues.JEI_MODID, true)
            .put(LabsValues.ROCKETRY_MODID,
                    LabsConfig.modIntegration.enableAdvancedRocketryIntegration)
            .put(LabsValues.ARCHITECTURE_MODID,
                    LabsConfig.modIntegration.enableArchitectureCraftIntegration)
            .put(LabsValues.EFFORTLESS_MODID,
                    LabsConfig.modIntegration.effortlessBuildingIntegration.enableEffortlessBuildingIntegration)
            .put(LabsValues.GROOVY_MODID, true)
            .put(LabsValues.CONTROLLING_MODID, true)
            .put(LabsValues.DEFAULT_WORLD_GEN_MODID,
                    LabsConfig.modIntegration.enableDefaultWorldGenIntegration)
            .put(LabsValues.FTB_UTILS_MODID,
                    LabsConfig.modIntegration.enableFTBUtilsIntegration)
            .put(LabsValues.TOP_ADDONS_MODID,
                    LabsConfig.modIntegration.enableTopAddonsIntegration)
            .put(LabsValues.TOP_MODID, true)
            .put(LabsValues.AE2_MODID, true)
            .put(LabsValues.ENDER_IO_MODID, true)
            .put(LabsValues.AA_MODID, true)
            .put(LabsValues.BOGOSORT_MODID, true)
            .put(LabsValues.THERMAL_FOUNDATION_MODID, true)
            .put(LabsValues.BQU_MODID, true)
            .put(LabsValues.BETTER_P2P_MODID, true)
            .put(LabsValues.STORAGE_DRAWERS_MODID, true)
            .build();

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
            LOGGER.fatal("Mixin Config: {}", mixinConfig);
            return true;
        }

        if (!Objects.equals(parts[1], LabsValues.LABS_MODID)) {
            LOGGER.error("Non Nomi-Labs Mixin Found in Mixin Queue. This is probably an error. Skipping...");
            LOGGER.error("Mixin Config: {}", mixinConfig);
            return true;
        }

        if (!Loader.isModLoaded(parts[2])) {
            LOGGER.error("Mod '{}' is not loaded. If this is a normal Nomi-CEu instance, this is probably an error.",
                    parts[2]);
            LOGGER.error("Not Loading Mixin Config {}", mixinConfig);
            return false;
        }

        if (!modMixinsConfig.containsKey(parts[2]) || !modMixinsConfig.get(parts[2])) {
            LOGGER.info("Integration for Mod '{}' is not enabled, or does not exist.", parts[2]);
            LOGGER.info("Not Loading Mixin Config {}", mixinConfig);
            return false;
        }

        return true;
    }
}
