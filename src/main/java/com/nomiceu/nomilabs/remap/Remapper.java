package com.nomiceu.nomilabs.remap;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
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

    public ResourceLocation remapRl(ResourceLocation rl) {
        return remapRl.apply(rl);
    }

    @Nullable
    public <T extends IForgeRegistryEntry<T>> ResourceLocation remapEntry(RegistryEvent.MissingMappings.Mapping<T> entry, RemapTypes type) {
        var rl = remapRl(entry.key);
        var remap = new RemapTypes.RegistryHelper<T>().getRegistryForType(type).getValue(rl);
        if (remap == null) return null;
        entry.remap(remap);
        return rl;
    }

    public enum RemapTypes {
        ITEM,
        BLOCK,
        ENTITY,
        BIOME;

        @SuppressWarnings("unchecked")
        public static class RegistryHelper<T extends IForgeRegistryEntry<T>> {
            public IForgeRegistry<T> getRegistryForType(RemapTypes type) {
                switch (type) {
                    case ITEM -> {
                        return (IForgeRegistry<T>) ForgeRegistries.ITEMS;
                    }
                    case BLOCK -> {
                        return (IForgeRegistry<T>) ForgeRegistries.BLOCKS;
                    }
                    case ENTITY -> {
                        return (IForgeRegistry<T>) ForgeRegistries.ENTITIES;
                    }
                    case BIOME -> {
                        return (IForgeRegistry<T>) ForgeRegistries.BIOMES;
                    }
                    default -> throw new RuntimeException("No Registry Set for Type " + type.name() + "!");
                }
            }
        }
    }
}
