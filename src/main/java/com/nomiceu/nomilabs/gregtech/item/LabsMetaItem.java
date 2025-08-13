package com.nomiceu.nomilabs.gregtech.item;

import static com.nomiceu.nomilabs.gregtech.item.LabsMetaItems.*;
import static gregtech.api.GTValues.*;

import java.util.Locale;

import net.minecraft.util.ResourceLocation;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.gregtech.mixinhelper.BucketItemFluidContainer;
import com.nomiceu.nomilabs.util.LabsNames;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.items.metaitem.FilteredFluidStats;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.stack.ItemMaterialInfo;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.common.items.behaviors.TooltipBehavior;

public class LabsMetaItem extends StandardMetaItem {

    public LabsMetaItem() {
        super();
        setCreativeTab(LabsCreativeTabs.TAB_NOMI_LABS);
    }

    @Override
    public ResourceLocation createItemModelPath(MetaItem<?>.MetaValueItem metaValueItem, String postfix) {
        return LabsNames.makeLabsName(formatModelPath(metaValueItem) + postfix);
    }

    @Override
    public void registerSubItems() {
        BRONZE_CELL = addItem(0, "bronze_cell")
                .addComponents(
                        new FilteredFluidStats(8000,
                                Materials.Bronze.getProperty(PropertyKey.FLUID_PIPE).getMaxFluidTemperature(),
                                true, false, false, false, true),
                        new BucketItemFluidContainer())
                .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Bronze, M * 4)));
        GOLD_CELL = addItem(1, "gold_cell")
                .addComponents(
                        new FilteredFluidStats(8000,
                                Materials.Gold.getProperty(PropertyKey.FLUID_PIPE).getMaxFluidTemperature(),
                                true, true, false, false, true),
                        new BucketItemFluidContainer())
                .setMaterialInfo(new ItemMaterialInfo(new MaterialStack(Materials.Gold, M * 4)));

        UNIVERSAL_CIRCUITS = new MetaItem.MetaValueItem[UV + 1]; // +1, due to ULV
        for (int voltage = 0; voltage <= UV; voltage++) {
            UNIVERSAL_CIRCUITS[voltage] = addItem(2 + voltage,
                    "universal_circuit." + VN[voltage].toLowerCase(Locale.ROOT))
                            .addComponents(new TooltipBehavior(lines -> {
                                lines.add(LabsTranslate.translate("tooltip.nomilabs.metaitem.universal_circuit.1"));
                                lines.add(LabsTranslate.translate("tooltip.nomilabs.metaitem.universal_circuit.2"));
                            }));
        }
    }
}
