package com.nomiceu.nomilabs.mixin.ae2;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.api.features.InscriberProcessType;
import appeng.core.features.registries.inscriber.InscriberRecipe;

/**
 * Fixes Inscriber Recipes, In GroovyScript, for AE2 v0.56.5.
 */
@Mixin(value = InscriberRecipe.class, remap = false)
public class InscriberRecipeMixin {

    @Mutable
    @Shadow
    @Final
    @Nonnull
    private List<ItemStack> maybeTop;

    @Mutable
    @Shadow
    @Final
    @Nonnull
    private List<ItemStack> maybeBot;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void setMaybeTopBottomProperly(Collection<ItemStack> inputs, ItemStack output, @Nullable List<ItemStack> top,
                                          @Nullable List<ItemStack> bot, InscriberProcessType type, CallbackInfo ci) {
        maybeTop = top == null || top.isEmpty() ? Collections.emptyList() : top;
        maybeBot = bot == null || bot.isEmpty() ? Collections.emptyList() : bot;
    }
}
