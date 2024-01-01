package com.nomiceu.nomilabs.remap;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Function;

public class Remapper {
    private final Function<ResourceLocation, Boolean> shouldRemap;
    private final Function<ResourceLocation, ResourceLocation> remapRl;

    public Remapper(Function<ResourceLocation, Boolean> shouldRemap, Function<ResourceLocation, ResourceLocation> remapRl) {
        this.shouldRemap = shouldRemap;
        this.remapRl = remapRl;
    }

    public boolean shouldRemap(ResourceLocation rl) {
        return shouldRemap.apply(rl);
    }

    protected ResourceLocation remapRl(ResourceLocation rl) {
        return remapRl.apply(rl);
    }

    public <T extends IForgeRegistryEntry<T>> ResourceLocation remapEntry(RegistryEvent.MissingMappings.Mapping<T> entry) {
        var rl = remapRl(entry.key);
        entry.remap(entry.registry.getValue(rl));
        return rl;
    }

    public enum RemapTypes {
        ITEM,
        BLOCK,
        ENTITY,
        BIOME
    }
}
