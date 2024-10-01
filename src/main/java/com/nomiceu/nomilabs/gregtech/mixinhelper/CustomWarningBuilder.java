package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.List;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.util.TextComponentUtil;

@SuppressWarnings("UnusedReturnValue")
public class CustomWarningBuilder {

    private final AccessibleRecipeLogic al;
    private final List<ITextComponent> textList;
    private final boolean structureFormed;

    public CustomWarningBuilder(List<ITextComponent> textList, boolean structureFormed, AbstractRecipeLogic al) {
        this.al = asCustom(al);
        this.textList = textList;
        this.structureFormed = structureFormed;
    }

    public CustomWarningBuilder addOutputFull() {
        if (structureFormed && al.labs$outputFull())
            textList.add(TextComponentUtil.translationWithColor(TextFormatting.YELLOW,
                    "nomilabs.multiblock.output_full"));
        return this;
    }

    public CustomWarningBuilder addRecipeVoltageHigh() {
        if (structureFormed && al.labs$recipeVoltageTooHigh())
            textList.add(TextComponentUtil.translationWithColor(TextFormatting.YELLOW,
                    "nomilabs.multiblock.recipe_voltage"));
        return this;
    }

    public CustomWarningBuilder addCleanroomWarnings() {
        if (!structureFormed) return this;

        if (al.labs$requiresCleanroom()) {
            textList.add(TextComponentUtil.translationWithColor(TextFormatting.YELLOW,
                    "nomilabs.multiblock.cleanroom_needed"));
            return this;
        }

        if (al.labs$cleanroomDirty()) {
            textList.add(TextComponentUtil.translationWithColor(TextFormatting.YELLOW,
                    "nomilabs.multiblock.cleanroom_dirty"));
            return this;
        }

        if (al.labs$wrongCleanroom())
            textList.add(TextComponentUtil.translationWithColor(TextFormatting.YELLOW,
                    "nomilabs.multiblock.cleanroom_wrong"));
        return this;
    }

    private static AccessibleRecipeLogic asCustom(AbstractRecipeLogic al) {
        return (AccessibleRecipeLogic) al;
    }
}
