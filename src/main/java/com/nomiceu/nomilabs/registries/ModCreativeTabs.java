package com.nomiceu.nomilabs.registries;

import net.minecraft.creativetab.CreativeTabs;

public class ModCreativeTabs {
    public static CreativeTabs NOMI_CORE;

    public static void preInit() {
        NOMI_CORE = new BaseCreativeTab("nomi_core", ModItems.HAND_FRAMING_TOOL, true);
    }
}
