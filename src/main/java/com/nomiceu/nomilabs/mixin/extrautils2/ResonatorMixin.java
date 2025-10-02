package com.nomiceu.nomilabs.mixin.extrautils2;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rwtema.extrautils2.api.resonator.IResonatorRecipe;
import com.rwtema.extrautils2.tile.TileResonator;

@Mixin(value = TileResonator.class, remap = false)
public class ResonatorMixin {

    @Shadow
    IResonatorRecipe currentRecipe;

    @Shadow
    private ItemStackHandler INPUT;

    // Remove frequency from ALL NEW XU2 Ingredients
    @Inject(method = "getOutputStack", at = @At("HEAD"), cancellable = true)
    private void getOutputStackWithoutFrequency(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = currentRecipe.getOutput(INPUT.getStackInSlot(0)).copy();

        cir.setReturnValue(stack);
    }
}
