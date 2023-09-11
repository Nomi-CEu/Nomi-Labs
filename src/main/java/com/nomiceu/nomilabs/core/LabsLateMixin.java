package com.nomiceu.nomilabs.core;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.LabsValues;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.nomiceu.nomilabs.NomiLabs.LOGGER;

public class LabsLateMixin implements ILateMixinLoader {
    public static final List<String> modMixins = ImmutableList.of(
            "draconicevolution",
            "nuclearcraft"
    );

    @Override
    public List<String> getMixinConfigs() {
       return modMixins.stream().map(mod -> "mixins." + LabsValues.LABS_MODID + "." + mod + ".json")
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
            LOGGER.error("Mod " + parts[2] + "is not loaded. If this is a normal Nomi-CEu instance, this is probably an error.");
            LOGGER.error("Not Loading Mixin Config " + mixinConfig);
            return false;
        }

        return Loader.isModLoaded(parts[2]);
    }
}
