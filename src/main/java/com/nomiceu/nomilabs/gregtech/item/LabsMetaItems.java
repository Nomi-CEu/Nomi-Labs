package com.nomiceu.nomilabs.gregtech.item;

import com.nomiceu.nomilabs.util.LabsNames;

import gregtech.api.items.metaitem.MetaItem;

public class LabsMetaItems {

    public static MetaItem<?>.MetaValueItem BRONZE_CELL;

    public static void preInit() {
        LabsMetaItem metaItem = new LabsMetaItem();
        metaItem.setRegistryName(LabsNames.makeLabsName("meta_item"));
    }
}
