package com.nomiceu.nomilabs.mixin.ae2;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.AccessibleContainerInterface;
import com.nomiceu.nomilabs.util.LabsSide;

import appeng.api.config.LockCraftingMode;
import appeng.container.guisync.GuiSync;
import appeng.container.implementations.ContainerInterface;
import appeng.helpers.DualityInterface;

/**
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/507">AE2 #507</a> to v0.56.5.
 */
@Mixin(value = ContainerInterface.class, remap = false)
public class ContainerInterfaceMixin implements AccessibleContainerInterface {

    @Shadow
    @Final
    private DualityInterface myDuality;

    @Unique
    private static final int LABS$LOCK_SYNC_ID = 9;

    @Unique
    @GuiSync(LABS$LOCK_SYNC_ID)
    public LockCraftingMode labs$lockReason = LockCraftingMode.NONE;

    @Inject(method = "detectAndSendChanges", at = @At("HEAD"), remap = true)
    private void syncLockReason(CallbackInfo ci) {
        if (LabsSide.isServer()) {
            labs$lockReason = myDuality.getCraftingLockedReason();
        }
    }

    @Unique
    @Override
    public LockCraftingMode labs$getLockingMode() {
        return labs$lockReason;
    }
}
