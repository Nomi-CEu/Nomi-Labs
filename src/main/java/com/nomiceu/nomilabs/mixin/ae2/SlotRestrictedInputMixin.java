package com.nomiceu.nomilabs.mixin.ae2;

import java.util.Optional;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.util.ItemMeta;

import appeng.api.AEApi;
import appeng.container.slot.SlotRestrictedInput;

/**
 * Fixes validation for Quantum Card Locked Slots not accounting for meta.
 */
@Mixin(value = SlotRestrictedInput.class, remap = false)
public class SlotRestrictedInputMixin {

    @Shadow
    @Final
    private SlotRestrictedInput.PlacableItemType which;

    @Inject(method = "isItemValid",
            at = @At(value = "INVOKE",
                     target = "Lappeng/api/IAppEngApi;definitions()Lappeng/api/definitions/IDefinitions;",
                     ordinal = 0,
                     remap = false),
            cancellable = true,
            remap = true,
            require = 1)
    private void properlyCheckQuantumLink(ItemStack i, CallbackInfoReturnable<Boolean> cir) {
        if (which == SlotRestrictedInput.PlacableItemType.CARD_QUANTUM) {
            Optional<ItemStack> quant = AEApi.instance().definitions().materials().cardQuantumLink().maybeStack(1);
            if (!quant.isPresent()) {
                cir.setReturnValue(false);
                return;
            }
            cir.setReturnValue(ItemMeta.compare(quant.get(), i));
        }
    }
}
