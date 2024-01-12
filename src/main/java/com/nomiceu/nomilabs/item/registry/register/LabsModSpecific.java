package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemCapacitor;
import com.nomiceu.nomilabs.item.ItemHandFramingTool;
import net.minecraftforge.fml.common.Loader;

import static com.nomiceu.nomilabs.item.registry.LabsItems.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;

/**
 * Initializer for mod specific items.
 * <br>
 * Split into methods to not load unneeded classes.
 */
public class LabsModSpecific {
    public static void initModSpecific() {
        if (Loader.isModLoaded(LabsValues.ENDER_IO_MODID))
            initEnderIO();
        if (Loader.isModLoaded(LabsValues.STORAGE_DRAWERS_MODID))
            initStorageDrawers();
    }

    private static void initEnderIO() {
        COMPRESSED_OCTADIC_CAPACITOR = createItem(new ItemCapacitor(makeLabsName("compressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS, ItemCapacitor.LabsCapacitorData.COMPRESSED));
        DOUBLE_COMPRESSED_OCTADIC_CAPACITOR = createItem(new ItemCapacitor(makeLabsName("doublecompressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS, ItemCapacitor.LabsCapacitorData.DOUBLE_COMPRESSED));
    }

    private static void initStorageDrawers() {
        HAND_FRAMING_TOOL = createItem(new ItemHandFramingTool(makeLabsName("hand_framing_tool"), LabsCreativeTabs.TAB_NOMI_LABS));
    }
}
