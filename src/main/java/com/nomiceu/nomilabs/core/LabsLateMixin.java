package com.nomiceu.nomilabs.core;

import static com.nomiceu.nomilabs.NomiLabs.LOGGER;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraftforge.fml.common.Loader;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

import zone.rong.mixinbooter.ILateMixinLoader;

@SuppressWarnings("unused")
public class LabsLateMixin implements ILateMixinLoader {

    public static final Map<String, Boolean> modMixinsConfig = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.DRACONIC_MODID,
                    LabsConfig.modIntegration.draconicEvolutionIntegration.enableDraconicEvolutionIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.NUCLEARCRAFT_MODID,
                    LabsConfig.modIntegration.enableNuclearCraftIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.XU2_MODID,
                    LabsConfig.modIntegration.enableExtraUtils2Integration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.GREGTECH_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.JEI_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.ROCKETRY_MODID,
                    LabsConfig.modIntegration.enableAdvancedRocketryIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.ARCHITECTURE_MODID,
                    LabsConfig.modIntegration.enableArchitectureCraftIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.EFFORTLESS_MODID,
                    LabsConfig.modIntegration.effortlessBuildingIntegration.enableEffortlessBuildingIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.GROOVY_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.CONTROLLING_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.DEFAULT_WORLD_GEN_MODID,
                    LabsConfig.modIntegration.enableDefaultWorldGenIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.DME_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.FTB_UTILS_MODID,
                    LabsConfig.modIntegration.enableFTBUtilsIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.TOP_ADDONS_MODID,
                    LabsConfig.modIntegration.enableTopAddonsIntegration),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.TOP_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.AE2_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.ENDER_IO_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.AA_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.BOGOSORT_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.THERMAL_FOUNDATION_MODID, true),
            new AbstractMap.SimpleImmutableEntry<>(LabsValues.BQU_MODID, true))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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
