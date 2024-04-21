package com.nomiceu.nomilabs.mixin.draconicevolution;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.brandon3055.draconicevolution.inventory.ContainerReactor;
import com.nomiceu.nomilabs.integration.draconicevolution.ReactorLogic;

@Mixin(value = ContainerReactor.SlotReactor.class, remap = false)
public abstract class ReactorSlotIntegration {

    @Final
    @Shadow
    private TileReactorCore tile;

    /**
     * Overrides Normal Draconic Reactor to allow usage of GT Awakened Draconium.
     */
    @Inject(method = "getStack()Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true, remap = true)
    public void getGTStack(CallbackInfoReturnable<ItemStack> cir) {
        int index = ((ContainerReactor.SlotReactor) (Object) this).getSlotIndex();
        if (index < 3)
            cir.setReturnValue(ReactorLogic.getStack(index, tile));
    }
}
