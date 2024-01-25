package com.nomiceu.nomilabs.util;

import com.cleanroommc.groovyscript.helper.GroovyHelper;
import com.nomiceu.nomilabs.LabsValues;
import net.minecraft.util.ResourceLocation;

public class LabsNames {
    public static ResourceLocation makeLabsName(String name) {
        return new ResourceLocation(LabsValues.LABS_MODID, name);
    }

    public static ResourceLocation makeGroovyName(String name) {
        return new ResourceLocation(GroovyHelper.getPackId(), name);
    }
}
