package com.nomiceu.nomilabs.mixin.actuallyadditions;

import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.collect.ImmutableSet;
import com.nomiceu.nomilabs.config.LabsConfig;

import de.ellpeck.actuallyadditions.mod.blocks.BlockLaserRelay;
import de.ellpeck.actuallyadditions.mod.config.ConfigValues;

@Mixin(value = BlockLaserRelay.class, remap = false)
public class BlockLaserRelayClientMixin {

    // Yes, we are comparing the item with the two screwdrivers' registry names.
    // For now, this will do. It works, and is simple.
    @Unique
    private static final Set<ResourceLocation> labs$screwdriverRls = ImmutableSet
            .of(new ResourceLocation("gregtech", "screwdriver"), new ResourceLocation("gregtech", "screwdriver_lv"));

    /**
     * At the first stack.getItem, return the expected 'configurator item' if it is a screwdriver.
     * Also probably not the best way to do this.
     */
    @Redirect(method = "displayHud",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
                       ordinal = 0,
                       remap = true),
              require = 1,
              remap = false)
    private Item checkForScrewdriver(ItemStack instance) {
        Item origitem = instance.getItem();

        if (!LabsConfig.modIntegration.gtScrewdriveAARelays) return origitem;

        if (labs$screwdriverRls.contains(origitem.getRegistryName()))
            return ConfigValues.itemCompassConfigurator;

        return origitem;
    }
}
