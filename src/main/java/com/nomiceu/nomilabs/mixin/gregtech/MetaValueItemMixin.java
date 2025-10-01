package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMetaValueItem;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.stats.IItemComponent;

/**
 * Allows Implementation of <a href="https://github.com/GregTechCEu/GregTech/pull/2660">GTCEu #2660</a>.
 */
@Mixin(value = MetaItem.MetaValueItem.class, remap = false)
public class MetaValueItemMixin implements AccessibleMetaValueItem {

    @Shadow
    @Final
    private List<IItemComponent> allStats;

    @Unique
    @Override
    public void labs$clearStats() {
        allStats.clear();
    }
}
