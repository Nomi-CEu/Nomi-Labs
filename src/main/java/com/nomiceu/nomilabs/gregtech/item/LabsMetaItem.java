package com.nomiceu.nomilabs.gregtech.item;

import static com.nomiceu.nomilabs.gregtech.item.LabsMetaItems.*;
import static gregtech.api.GTValues.*;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.gregtech.mixinhelper.BucketItemFluidContainer;

import gregtech.api.items.metaitem.FilteredFluidStats;
import gregtech.api.items.metaitem.StandardMetaItem;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.stack.ItemMaterialInfo;
import gregtech.api.unification.stack.MaterialStack;

public class LabsMetaItem extends StandardMetaItem {

    @Override
    public void registerSubItems() {
        BRONZE_CELL = addItem(0, "bronze_cell")
                .addComponents(
                        new FilteredFluidStats(8000,
                                Materials.Bronze.getProperty(PropertyKey.FLUID_PIPE).getMaxFluidTemperature(),
                                true, false, false, false, true),
                        new BucketItemFluidContainer())
                .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Bronze, M * 4)))
                .setCreativeTabs(LabsCreativeTabs.TAB_NOMI_LABS);
        GOLD_CELL = addItem(1, "gold_cell")
                .addComponents(
                        new FilteredFluidStats(8000,
                                Materials.Gold.getProperty(PropertyKey.FLUID_PIPE).getMaxFluidTemperature(),
                                true, true, false, false, true),
                        new BucketItemFluidContainer())
                .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Gold, M * 4)))
                .setCreativeTabs(LabsCreativeTabs.TAB_NOMI_LABS);
    }
}
