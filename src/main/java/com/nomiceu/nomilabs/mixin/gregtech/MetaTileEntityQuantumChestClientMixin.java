package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumChest;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.client.renderer.texture.custom.QuantumStorageRenderer;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest;

/**
 * Makes quantum chest rendering take into account 'export slot' items.
 */
@Mixin(value = MetaTileEntityQuantumChest.class, remap = false)
public abstract class MetaTileEntityQuantumChestClientMixin extends MetaTileEntity {

    @Shadow
    protected long itemsStoredInside;

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityQuantumChestClientMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Inject(method = "renderMetaTileEntity(DDDF)V", at = @At("HEAD"), cancellable = true)
    private void accountExportItemsOrLocked(double x, double y, double z, float partialTicks, CallbackInfo ci) {
        ci.cancel();

        ItemStack stack = getExportItems().getStackInSlot(0);
        long amount = 0;
        if (stack.isEmpty()) {
            if (((AccessibleQuantumChest) this).labs$isLocked()) {
                // Use locked stack, set count to -1 to bypass initial checks; we can normalise it to zero later
                stack = ((AccessibleQuantumChest) this).labs$getLockedStack();
                amount = -1;
            }
        } else {
            amount = itemsStoredInside + stack.getCount();
        }

        QuantumStorageRenderer.renderChestStack(x, y, z, (MetaTileEntityQuantumChest) (Object) this, stack, amount,
                partialTicks);
    }
}
