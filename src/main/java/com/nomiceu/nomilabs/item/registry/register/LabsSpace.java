package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.item.ItemBase;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsSpace {
    public static void initSpace() {
        RADIATION_LAYER = createItem(new ItemBase(makeLabsName("radiationlayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        PRESSURE_LAYER = createItem(new ItemBase(makeLabsName("pressurelayer"), LabsCreativeTabs.TAB_NOMI_LABS));
        CLOTH = createItem(new ItemBase(makeLabsName("cloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        THERMAL_CLOTH = createItem(new ItemBase(makeLabsName("thermalcloth"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_HELMET = createItem(new ItemBase(makeLabsName("unpreparedspacehelmet"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_CHESTPIECE = createItem(new ItemBase(makeLabsName("unpreparedspacechestpiece"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_LEGGINGS = createItem(new ItemBase(makeLabsName("unpreparedspaceleggings"), LabsCreativeTabs.TAB_NOMI_LABS));
        UNPREPARED_SPACE_BOOTS = createItem(new ItemBase(makeLabsName("unpreparedspaceboots"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
}
