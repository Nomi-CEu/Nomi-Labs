package com.nomiceu.nomilabs.mixin.groovyscript;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cleanroommc.groovyscript.sandbox.Preprocessor;

/**
 * Makes side only preprocessor take into account effective side, not the side type
 * (client instance or dedicated server)
 */
@Mixin(value = Preprocessor.class, remap = false)
public class PreprocessorMixin {

    @Redirect(method = "checkSide",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;getSide()Lnet/minecraftforge/fml/relauncher/Side;"))
    private static Side returnEffectiveSide(FMLCommonHandler instance) {
        return instance.getEffectiveSide();
    }
}
