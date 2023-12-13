package com.nomiceu.nomilabs.remap.remapper;

import com.nomiceu.nomilabs.NomiLabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.function.Function;

public class ItemRemapper extends Remapper<Item> {

    public ItemRemapper(Function<ResourceLocation, Boolean> shouldRemap, Function<ResourceLocation, ResourceLocation> remapRl) {
        super(shouldRemap, remapRl);
    }

    @Override
    public void remapEntry(RegistryEvent.MissingMappings.Mapping<Item> entry) {
        var newRL = remapRl(entry.key);
        NomiLabs.LOGGER.info("Remapping Item Resource Location: '{}' to '{}'", entry.key.toString(), newRL.toString());
        entry.remap(ForgeRegistries.ITEMS.getValue(newRL));
    }
}
