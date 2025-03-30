package com.nomiceu.nomilabs.mixin.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.util.TextFormattingUtil;
import gregtech.common.metatileentities.multi.MetaTileEntityLargeBoiler;

/**
 * Updates Tooltips for the 10x Fuel Efficiency Increase in {@link BoilerRecipeLogicMixin}.
 */
@Mixin(value = MetaTileEntityLargeBoiler.class, remap = false)
public class MetaTileEntityLargeBoilerClientMixin {

    @Redirect(method = "addInformation",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/resources/I18n;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                       ordinal = 0,
                       remap = true))
    private String formatNew(String s, Object[] objects) {
        return LabsTranslate.translate(s, TextFormattingUtil.formatNumbers((int) objects[0] * 10));
    }
}
