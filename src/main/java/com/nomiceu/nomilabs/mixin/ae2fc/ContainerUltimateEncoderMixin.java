package com.nomiceu.nomilabs.mixin.ae2fc;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.glodblock.github.client.container.ContainerUltimateEncoder;
import com.glodblock.github.common.tile.TileUltimateEncoder;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;
import com.nomiceu.nomilabs.util.LabsSide;

import appeng.container.guisync.GuiSync;

/**
 * Allows saving import/not import non-consume items into patterns.
 */
@Mixin(value = ContainerUltimateEncoder.class, remap = false)
public abstract class ContainerUltimateEncoderMixin implements InclNonConsumeSettable {

    @Shadow
    @Final
    private TileUltimateEncoder encoder;

    @GuiSync(320001)
    @Unique
    public boolean labs$inclNonConsume = true;

    @Inject(method = "detectAndSendChanges", at = @At("RETURN"), remap = true)
    private void syncNonConsume(CallbackInfo ci) {
        if (!LabsSide.isServer()) return;

        labs$inclNonConsume = ((InclNonConsumeSettable) encoder).labs$inclNonConsume();
    }

    @Override
    public boolean labs$inclNonConsume() {
        return labs$inclNonConsume;
    }

    @Override
    public void labs$setInclNonConsume(boolean inclNonConsume) {
        labs$inclNonConsume = inclNonConsume;

        ((InclNonConsumeSettable) encoder).labs$setInclNonConsume(inclNonConsume);
    }
}
