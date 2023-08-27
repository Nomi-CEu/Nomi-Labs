package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.item.ItemBase;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import net.minecraft.item.EnumRarity;

import static com.nomiceu.nomilabs.util.RegistryNames.makeCTName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsCoinsWidgets {
    public static void initCoins() {
        NOMICOIN_1 = createItem(new ItemBase(makeCTName("omnicoin"), LabsCreativeTabs.TAB_NOMI_LABS));
        NOMICOIN_5 = createItem(new ItemBase(makeCTName("omnicoin5"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        NOMICOIN_25 = createItem(new ItemBase(makeCTName("omnicoin25"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        NOMICOIN_100 = createItem(new ItemBase(makeCTName("omnicoin100"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
    }

    public static void initWidgets() {
        WOOD_WIDGET = createItem(new ItemBase(makeCTName("woodenwidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_LEFT = createItem(new ItemBase(makeCTName("woodwidgetleft"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_RIGHT = createItem(new ItemBase(makeCTName("woodwidgetright"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET = createItem(new ItemBase(makeCTName("stonewidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_UP = createItem(new ItemBase(makeCTName("stonewidgetup"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_DOWN = createItem(new ItemBase(makeCTName("stonewidgetdown"), LabsCreativeTabs.TAB_NOMI_LABS));
        ALLOY_WIDGET = createItem(new ItemBase(makeCTName("alloywidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        ENDER_WIDGET = createItem(new ItemBase(makeCTName("enderwidget"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
}
