package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin makes it so that the repair cost returned from a given itemstack is always 0.
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getRepairCost()I", at = @At("HEAD"), cancellable = true)
    public void getRepairCost(CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.disableXpScaling)
            cir.setReturnValue(0);
    }
}
