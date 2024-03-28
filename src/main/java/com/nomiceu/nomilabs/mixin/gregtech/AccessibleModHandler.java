package com.nomiceu.nomilabs.mixin.gregtech;

import gregtech.api.recipes.ModHandler;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ModHandler.class, remap = false)
public interface AccessibleModHandler {
    @Invoker(value = "validateRecipeWithOutput")
    static boolean validateRecipeWithOutput(@NotNull String regName, @NotNull ItemStack result,
                                                   @NotNull Object... recipe) {
        throw new NotImplementedException("AccessibleModHandler Failed to Apply!");
    }
}
