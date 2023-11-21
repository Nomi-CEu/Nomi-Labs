package com.nomiceu.nomilabs.mixin.draconicevolution;

import com.brandon3055.draconicevolution.inventory.ContainerReactor;
import com.nomiceu.nomilabs.integration.draconicevolution.ReactorLogic;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContainerReactor.class, remap = false)
public class ReactorIntegration {
    /**
     * Overrides Normal Draconic Reactor to allow usage of GT Awakened Draconium.
     */
    @Inject(method = "getFuelValue", at = @At("HEAD"), cancellable = true)
    public void getGTFuelValue(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ReactorLogic.getFuelValue(stack));
    }
}
