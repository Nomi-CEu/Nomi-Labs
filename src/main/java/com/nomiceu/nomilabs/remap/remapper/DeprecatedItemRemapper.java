package com.nomiceu.nomilabs.remap.remapper;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DeprecatedItemRemapper extends ItemRemapper {
    private static final Map<ResourceLocation, Supplier<Item>> DEPRECATION_REMAP = new HashMap<>();

    public DeprecatedItemRemapper() {
        /* This is a method reference, so it is fine to add the keys afterwards */
        super(DEPRECATION_REMAP::containsKey, rl -> rl);

        /* It is too big to use ImmutableMap.of(). Add each manually. */
        DEPRECATION_REMAP.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin"), () -> LabsItems.NOMICOIN_1
        );
        DEPRECATION_REMAP.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin5"), () -> LabsItems.NOMICOIN_5
        );
        DEPRECATION_REMAP.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin25"), () -> LabsItems.NOMICOIN_25
        );
        DEPRECATION_REMAP.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin100"), () -> LabsItems.NOMICOIN_100
        );
        DEPRECATION_REMAP.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID,"blazepowder"), () -> Items.BLAZE_POWDER
        );
        DEPRECATION_REMAP.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "dark_red_coal"),
                () -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2:ingredients"))
        );
    }

    @Override
    public void remapEntry(RegistryEvent.MissingMappings.Mapping<Item> entry) {
        entry.remap(DEPRECATION_REMAP.get(entry.key).get());
    }
}
