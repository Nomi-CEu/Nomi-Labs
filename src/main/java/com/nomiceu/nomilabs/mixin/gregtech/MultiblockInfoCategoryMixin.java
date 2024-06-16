package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.IJEISpecialMultiblock;
import com.nomiceu.nomilabs.gregtech.mixinhelper.ConditionalJEIMultiblock;

import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.MultiblockInfoCategory;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.IModRegistry;

@Mixin(value = MultiblockInfoCategory.class, remap = false)
public class MultiblockInfoCategoryMixin {

    @Shadow
    @Final
    public static List<MultiblockControllerBase> REGISTER;

    @Inject(method = "registerRecipes", at = @At("HEAD"), cancellable = true)
    private static void registerConditionalAndSpecialRecipes(IModRegistry registry, CallbackInfo ci) {
        registry.addRecipes(REGISTER.stream()
                .filter((multi) -> !(multi instanceof ConditionalJEIMultiblock conditional) ||
                        conditional.shouldShowInJEI())
                .map((multi) -> multi instanceof IJEISpecialMultiblock special ? special.getWrapper() :
                        new MultiblockInfoRecipeWrapper(multi))
                .collect(Collectors.toList()),
                "gregtech:multiblock_info");
        ci.cancel();
    }
}
