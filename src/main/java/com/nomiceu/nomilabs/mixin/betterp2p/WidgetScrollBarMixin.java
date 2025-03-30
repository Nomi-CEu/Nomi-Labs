package com.nomiceu.nomilabs.mixin.betterp2p;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.projecturanus.betterp2p.client.gui.widget.WidgetScrollBar;

/**
 * Fixes clicking scrollbar.
 */
@Mixin(value = WidgetScrollBar.class, remap = false)
public abstract class WidgetScrollBarMixin {

    @Shadow
    protected abstract int getRange();

    @Shadow
    private int displayX;

    @Shadow
    private int width;

    @Shadow
    private int displayY;

    @Shadow
    private int height;

    @Shadow
    private int currentScroll;

    @Shadow
    private int minScroll;

    @Shadow
    private boolean moving;

    @Shadow
    protected abstract void applyRange();

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private void properlyHandleClick(int x, int y, CallbackInfo ci) {
        ci.cancel();
        if (getRange() == 0) {
            return;
        }

        if (y > displayY && y <= displayY + height) {
            if (x > displayX && x <= displayX + width) {
                currentScroll = y - displayY;
                currentScroll = minScroll + currentScroll * 2 * getRange() / height;
                currentScroll = (currentScroll + 1) >> 1;
                applyRange();
                moving = true;
            }
        }
    }
}
