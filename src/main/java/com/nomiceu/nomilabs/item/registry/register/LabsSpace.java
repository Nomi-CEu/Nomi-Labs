package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.item.ItemBase;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;

import static com.nomiceu.nomilabs.util.LabsNames.makeCTName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsSpace {
    public static void initSpace() {
        RADIATION_LAYER = createItem(new ItemBase(makeCTName("radiationlayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        PRESSURE_LAYER = createItem(new ItemBase(makeCTName("pressurelayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        CLOTH = createItem(new ItemBase(makeCTName("cloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        THERMAL_CLOTH = createItem(new ItemBase(makeCTName("thermalcloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_HELMET = createItem(new ItemBase(makeCTName("unpreparedspacehelmet"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_CHESTPIECE = createItem(new ItemBase(makeCTName("unpreparedspacechestpiece"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_LEGGINGS = createItem(new ItemBase(makeCTName("unpreparedspaceleggings"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_BOOTS = createItem(new ItemBase(makeCTName("unpreparedspaceboots"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
}
