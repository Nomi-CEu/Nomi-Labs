package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.pattern.PatternError;

@Mixin(value = MultiblockWithDisplayBase.class, remap = false)
public abstract class MultiblockWithDisplayBaseMixin extends MultiblockControllerBase {

    /**
     * Mandatory Ignored Constructor
     */
    public MultiblockWithDisplayBaseMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Redirect(method = "addErrorText",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/metatileentity/multiblock/MultiblockDisplayText;builder(Ljava/util/List;Z)Lgregtech/api/metatileentity/multiblock/MultiblockDisplayText$Builder;"),
              require = 1)
    private MultiblockDisplayText.Builder addInfoAboutStructure(List<ITextComponent> textList,
                                                                boolean isStructureFormed) {
        var result = MultiblockDisplayText.builder(textList, isStructureFormed);
        if (!isStructureFormed && structurePattern != null) {
            PatternError error = structurePattern.getError();
            if (error != null) textList.add(new TextComponentString(error.getErrorInfo()));
        }
        return result;
    }
}
