package com.nomiceu.nomilabs.item.registry.register;

import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.text.TextFormatting;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsEndgame {
    public static void initEndgame() {
        HEART_OF_THE_UNIVERSE = createItem(new ItemBase(makeLabsName("heartofauniverse"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        CREATIVE_TANK_MOLD = createItem(new ItemBase(makeLabsName("creativeportabletankmold"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        EXOTIC_MATERIALS_CATALYST = createItem(new ItemBase(makeLabsName("exoticmaterialscatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        ETERNAL_CATALYST = createItem(new ItemBase(makeLabsName("eternalcatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        ULTIMATE_GEM = createItem(new ItemBase(makeLabsName("ultimate_gem"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1,
                ImmutableMap.of("item.nomilabs.ultimate_gem.tooltip", TextFormatting.GRAY.toString())));
    }
}
