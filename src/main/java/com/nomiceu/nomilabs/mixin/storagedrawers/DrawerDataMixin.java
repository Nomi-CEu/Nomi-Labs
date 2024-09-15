package com.nomiceu.nomilabs.mixin.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StandardDrawerGroup.DrawerData.class, remap = false)
public abstract class DrawerDataMixin {

    @Shadow
    private int count;

    @Shadow
    IDrawerAttributes attrs;

    @Shadow
    protected abstract void reset(boolean notify);

    @Inject(method = "syncAttributes", at = @At("HEAD"))
    private void labs$updateEmptyStatus(CallbackInfo ci) {
        if (count == 0 && !attrs.isItemLocked(LockAttribute.LOCK_POPULATED))
            reset(true);
    }
}
