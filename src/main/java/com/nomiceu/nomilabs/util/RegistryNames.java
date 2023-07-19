package com.nomiceu.nomilabs.util;

import com.nomiceu.nomilabs.LabsValues;
import net.minecraft.util.ResourceLocation;

public class RegistryNames {

    public static ResourceLocation makeLabsName(String name) {
        return new ResourceLocation(LabsValues.LABS_MODID, name);
    }

    public static ResourceLocation makeCTName(String name) {
        return new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, name);
    }
}
