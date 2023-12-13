package com.nomiceu.nomilabs.remap.remapper;

import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.LabsValues;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

public class DeprecatedItemRemapper extends ItemRemapper {
    private static final Map<ResourceLocation, Supplier<Item>> DEPRECATION_REMAP = ImmutableMap.of(
            new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID,"blazepowder"), () -> Items.BLAZE_POWDER,
            new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "dark_red_coal"), () -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2:ingredients"))
    );

    public DeprecatedItemRemapper() {
        super(DEPRECATION_REMAP::containsKey, rl -> rl);
    }

    @Override
    public void remapEntry(RegistryEvent.MissingMappings.Mapping<Item> entry) {
        entry.remap(DEPRECATION_REMAP.get(entry.key).get());
    }
}
