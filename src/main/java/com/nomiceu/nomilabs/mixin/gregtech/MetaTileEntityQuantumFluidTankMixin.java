package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraftforge.fluids.FluidStack;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumTank;

import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;

/**
 * Notify locked updates to client appropriately.
 */
@Mixin(targets = "gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank$QuantumFluidTank", remap = false)
public class MetaTileEntityQuantumFluidTankMixin {

    @Shadow
    @Final
    MetaTileEntityQuantumTank this$0;

    @Inject(method = "fillInternal",
            at = @At(value = "FIELD",
                     target = "Lnet/minecraftforge/fluids/FluidStack;amount:I",
                     opcode = Opcodes.PUTFIELD,
                     shift = At.Shift.AFTER),
            require = 1)
    private void updateClientState(FluidStack resource, boolean doFill, CallbackInfoReturnable<Integer> cir) {
        // At this point, we are sure that the tank is locked
        ((AccessibleQuantumTank) this$0).labs$lockedFluidUpdateNotify();
        this$0.markDirty();
    }
}
