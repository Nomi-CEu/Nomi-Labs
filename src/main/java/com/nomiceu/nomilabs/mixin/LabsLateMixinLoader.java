package com.nomiceu.nomilabs.mixin;

import static com.nomiceu.nomilabs.LabsValues.*;
import static com.nomiceu.nomilabs.NomiLabs.LOGGER;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraftforge.fml.common.Loader;

import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.config.LabsConfig;

import zone.rong.mixinbooter.ILateMixinLoader;

@SuppressWarnings("unused")
public class LabsLateMixinLoader implements ILateMixinLoader {

    public static final Map<String, Boolean> modMixinsConfig = new ImmutableMap.Builder<String, Boolean>()
            .put(DRACONIC_MODID,
                    LabsConfig.modIntegration.draconicEvolutionIntegration.enableDraconicEvolutionIntegration)
            .put(NUCLEARCRAFT_MODID,
                    LabsConfig.modIntegration.enableNuclearCraftIntegration)
            .put(XU2_MODID,
                    LabsConfig.modIntegration.enableExtraUtils2Integration)
            .put(GREGTECH_MODID, true)
            .put(JEI_MODID, true)
            .put(ROCKETRY_MODID,
                    LabsConfig.modIntegration.enableAdvancedRocketryIntegration)
            .put(ARCHITECTURE_MODID,
                    LabsConfig.modIntegration.enableArchitectureCraftIntegration)
            .put(EFFORTLESS_MODID,
                    LabsConfig.modIntegration.effortlessBuildingIntegration.enableEffortlessBuildingIntegration)
            .put(GROOVY_MODID, true)
            .put(CONTROLLING_MODID, true)
            .put(DEFAULT_WORLD_GEN_MODID,
                    LabsConfig.modIntegration.enableDefaultWorldGenIntegration)
            .put(DME_MODID, true)
            .put(FTB_UTILS_MODID,
                    LabsConfig.modIntegration.enableFTBUtilsIntegration)
            .put(TOP_ADDONS_MODID,
                    LabsConfig.modIntegration.enableTopAddonsIntegration)
            .put(TOP_MODID, true)
            .put(AE2_MODID, true)
            .put(ENDER_IO_MODID, true)
            .put(AA_MODID, true)
            .put(THERMAL_FOUNDATION_MODID, true)
            .put(BQU_MODID, true)
            .put(BETTER_P2P_MODID, true)
            .put(STORAGE_DRAWERS_MODID, true)
            .put(SOLAR_FLUX_MODID,
                    LabsConfig.modIntegration.solarFluxPerformanceOptions.enableSolarFluxPerformance)
            .put(ARMOR_PLUS_MODID, true)
            .put(GCYM_MODID, true)
            .build();

    @Override
    public List<String> getMixinConfigs() {
        return modMixinsConfig.keySet().stream().map(mod -> "mixins." + LABS_MODID + "." + mod + ".json")
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

        if (!Objects.equals(parts[1], LABS_MODID)) {
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
