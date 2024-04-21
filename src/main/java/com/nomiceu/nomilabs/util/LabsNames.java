package com.nomiceu.nomilabs.util;

import net.minecraft.util.ResourceLocation;

import com.cleanroommc.groovyscript.helper.GroovyHelper;
import com.nomiceu.nomilabs.LabsValues;

public class LabsNames {

    public static ResourceLocation makeLabsName(String name) {
        return new ResourceLocation(LabsValues.LABS_MODID, name);
    }

    public static ResourceLocation makeGroovyName(String name) {
        return new ResourceLocation(GroovyHelper.getPackId(), name);
    }
}
