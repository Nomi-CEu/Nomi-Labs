package com.nomiceu.nomilabs.creativetab.registry;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.creativetab.BaseCreativeTab;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import net.minecraft.creativetab.CreativeTabs;

public class LabsCreativeTabs {
    public static CreativeTabs TAB_NOMI_LABS;

    public static void preInit() {
        TAB_NOMI_LABS = new BaseCreativeTab(LabsValues.LABS_MODID + ".main", () -> LabsItems.HEART_OF_THE_UNIVERSE, true);
    }
}
