package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import net.minecraft.item.EnumRarity;

import static com.nomiceu.nomilabs.util.RegistryNames.makeCTName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsData {
    public static void initData() {
        DRAGON_DATA = createItem(new ItemBase(makeCTName("dragonlairdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WITHER_DATA = createItem(new ItemBase(makeCTName("witherrealmdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        IMPOSSIBLE_DATA = createItem(new ItemBase(makeCTName("impossiblerealmdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        UNIVERSE_DATA = createItem(new ItemBase(makeCTName("universecreationdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
        STELLAR_DATA = createItem(new ItemBase(makeCTName("stellarcreationdata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        CHAOS_DRAGON_DATA = createItem(new ItemBase(makeCTName("lairofthechaosguardiandata"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 1));
    }
}
