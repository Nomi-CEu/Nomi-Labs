package com.nomiceu.nomilabs.remap.remapper;

import com.nomiceu.nomilabs.NomiLabs;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.function.Function;

public class BlockRemapper extends Remapper<Block> {
    public BlockRemapper(Function<ResourceLocation, Boolean> shouldRemap, Function<ResourceLocation, ResourceLocation> remapRl) {
        super(shouldRemap, remapRl);
    }

    @Override
    public void remapEntry(RegistryEvent.MissingMappings.Mapping<Block> entry) {
        var newRL = remapRl(entry.key);
        NomiLabs.LOGGER.info("Remapping Block Resource Location: '{}' to '{}'", entry.key.toString(), newRL.toString());
        entry.remap(ForgeRegistries.BLOCKS.getValue(newRL));
    }
}
