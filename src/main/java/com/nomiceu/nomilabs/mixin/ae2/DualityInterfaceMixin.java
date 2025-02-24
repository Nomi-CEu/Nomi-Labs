package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RangedWrapper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.integration.ae2.AccessibleAppEngNetworkInventory;

import appeng.helpers.DualityInterface;

/**
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/454">AE2 #454</a> to v0.56.6.
 */
@Mixin(value = DualityInterface.class, remap = false)
public class DualityInterfaceMixin {

    @WrapOperation(method = "getAdaptor",
                   at = @At(value = "NEW",
                            target = "(Lnet/minecraftforge/items/IItemHandlerModifiable;II)Lnet/minecraftforge/items/wrapper/RangedWrapper;"),
                   require = 1)
    private RangedWrapper returnNewRangedWrapper(IItemHandlerModifiable compose, int minSlot, int maxSlotExclusive,
                                                 Operation<RangedWrapper> original) {
        return ((AccessibleAppEngNetworkInventory) compose).labs$getBufferWrapper(minSlot);
    }
}
