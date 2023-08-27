package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import net.minecraft.item.EnumRarity;

import static com.nomiceu.nomilabs.util.RegistryNames.makeCTName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsEndgame {
    public static void initEndgame() {
        HEART_OF_THE_UNIVERSE = createItem(new ItemBase(makeCTName("heartofauniverse"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        CREATIVE_TANK_MOLD = createItem(new ItemBase(makeCTName("creativeportabletankmold"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        EXOTIC_MATERIALS_CATALYST = createItem(new ItemBase(makeCTName("exoticmaterialscatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        ETERNAL_CATALYST = createItem(new ItemBase(makeCTName("eternalcatalyst"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        ULTIMATE_GEM = createItem(new ItemBase(makeCTName("ultimate_gem"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
    }
}
