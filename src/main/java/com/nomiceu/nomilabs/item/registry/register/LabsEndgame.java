package com.nomiceu.nomilabs.item.registry.register;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.text.TextFormatting;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;
import static com.nomiceu.nomilabs.tooltip.LabsTooltipHelper.Tooltip;

public class LabsEndgame {
    public static void initEndgame() {
        HEART_OF_THE_UNIVERSE = createItem(new ItemBase(makeLabsName("heartofauniverse"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        CREATIVE_TANK_MOLD = createItem(new ItemBase(makeLabsName("creativeportabletankmold"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        EXOTIC_MATERIALS_CATALYST = createItem(new ItemBase(makeLabsName("exoticmaterialscatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        ETERNAL_CATALYST = createItem(new ItemBase(makeLabsName("eternalcatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 64,
                ImmutableList.of(Tooltip.of("tooltip.nomilabs.eternalcatalyst.description").withFormat(TextFormatting.DARK_GRAY))));
        ULTIMATE_GEM = createItem(new ItemBase(makeLabsName("ultimate_gem"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1,
                ImmutableList.of(Tooltip.of("tooltip.nomilabs.ultimate_items.description").withFormat(TextFormatting.GRAY))));
    }
}
