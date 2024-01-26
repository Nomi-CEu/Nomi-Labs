package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false)
public abstract class GuiIngameForgeMixin {
    @Shadow
    public static int left_height;

    @Shadow
    public abstract void renderHealth(int width, int height);

    @Shadow
    private ScaledResolution res;

    @Inject(method = "renderGameOverlay(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/GuiIngameForge;renderHealth(II)V", shift = At.Shift.AFTER, remap = false), remap = true)
    public void renderHealthAgain(float partialTicks, CallbackInfo ci) {
        if (LabsConfig.advanced.doubleHealthMode == LabsConfig.Advanced.DoubleHealthMode.NONE) return;
        if (LabsConfig.advanced.doubleHealthMode == LabsConfig.Advanced.DoubleHealthMode.SPACE) left_height += 10;
        renderHealth(res.getScaledWidth(), res.getScaledHeight());
    }
}
