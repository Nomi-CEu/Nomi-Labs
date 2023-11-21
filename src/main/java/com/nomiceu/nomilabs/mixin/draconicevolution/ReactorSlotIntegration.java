package com.nomiceu.nomilabs.mixin.draconicevolution;

import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.brandon3055.draconicevolution.inventory.ContainerReactor;
import com.nomiceu.nomilabs.integration.draconicevolution.ReactorLogic;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContainerReactor.SlotReactor.class, remap = false)
public abstract class ReactorSlotIntegration {
    @Final
    @Shadow
    private TileReactorCore tile;

    /**
     * Overrides Normal Draconic Reactor to allow usage of GT Awakened Draconium.
     */
    @Inject(method = "getStack", at = @At("HEAD"), cancellable = true)
    public void getGTStack(CallbackInfoReturnable<ItemStack> cir) {
        int index = ((ContainerReactor.SlotReactor) (Object) this).getSlotIndex();
        if (index < 3)
            cir.setReturnValue(ReactorLogic.getStack(index, tile));
    }
}
