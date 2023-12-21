package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.item.ItemBase;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import net.minecraft.item.EnumRarity;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsCoinsWidgets {
    public static void initCoins() {
        NOMICOIN_1 = createItem(new ItemBase(makeLabsName("nomicoin"), LabsCreativeTabs.TAB_NOMI_LABS));
        NOMICOIN_5 = createItem(new ItemBase(makeLabsName("nomicoin5"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        NOMICOIN_25 = createItem(new ItemBase(makeLabsName("nomicoin25"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        NOMICOIN_100 = createItem(new ItemBase(makeLabsName("nomicoin100"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
    }

    public static void initWidgets() {
        WOOD_WIDGET = createItem(new ItemBase(makeLabsName("woodenwidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_LEFT = createItem(new ItemBase(makeLabsName("woodwidgetleft"), LabsCreativeTabs.TAB_NOMI_LABS));
        WOOD_WIDGET_RIGHT = createItem(new ItemBase(makeLabsName("woodwidgetright"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET = createItem(new ItemBase(makeLabsName("stonewidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_UP = createItem(new ItemBase(makeLabsName("stonewidgetup"), LabsCreativeTabs.TAB_NOMI_LABS));
        STONE_WIDGET_DOWN = createItem(new ItemBase(makeLabsName("stonewidgetdown"), LabsCreativeTabs.TAB_NOMI_LABS));
        ALLOY_WIDGET = createItem(new ItemBase(makeLabsName("alloywidget"), LabsCreativeTabs.TAB_NOMI_LABS));
        ENDER_WIDGET = createItem(new ItemBase(makeLabsName("enderwidget"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
}
