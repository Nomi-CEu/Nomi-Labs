package com.nomiceu.nomilabs.mixin.topaddons;

import static io.github.drmanganese.topaddons.elements.ElementRenderHelper.drawSmallText;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.top.LabsFluidNameElement;

import io.github.drmanganese.topaddons.elements.ElementTankGauge;
import io.github.drmanganese.topaddons.styles.ProgressStyleTank;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.rendering.RenderHelper;

/**
 * Fixes Localization of Fluid Names. (Client Only)
 */
@Mixin(value = ElementTankGauge.class, remap = false)
public class ElementTankGaugeMixin {

    @Shadow
    @Final
    @Mutable
    private String fluidName;

    @Shadow
    @Final
    private int amount;

    @Shadow
    @Final
    private int capacity;

    @Shadow
    @Final
    private String tankName;

    @Shadow
    @Final
    private String suffix;

    @Shadow
    @Final
    private int color1;

    @Shadow
    @Final
    private boolean sneaking;

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void newRenderLogic(int x, int y, CallbackInfo ci) {
        boolean shouldRender = sneaking && capacity > 0;

        var tankHeight = shouldRender ? 12 : 8;

        if (capacity > 0) {
            ElementProgressRender.render(new ProgressStyleTank().filledColor(color1).alternateFilledColor(color1),
                    amount, capacity, x, y, 100, tankHeight);
        } else {
            ElementProgressRender.render(new ProgressStyleTank(), amount, capacity, x, y, 100, tankHeight);
        }

        for (int i = 1; i < 10; i++) {
            RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? tankHeight - 1 : tankHeight / 2),
                    0xff767676);
        }

        if (shouldRender) {
            ElementTextRender.render(amount + "/" + capacity + " " + suffix, x + 3, y + 2);

            var tank = tankName + ": ";
            var translatedName = LabsFluidNameElement.translateFluid(fluidName, amount, "ElementTankGauge");
            var mc = Minecraft.getMinecraft();
            labs$drawMediumText(x + 1 + mc.fontRenderer.getStringWidth(tank) * 3 / 4, y + 14, translatedName);
            labs$drawMediumText(x + 1, y + 14, tank);
        } else {
            drawSmallText(x + 2, y + 2, tankName, 0xffffffff);
        }

        ci.cancel();
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    private void newHeightLogic(CallbackInfoReturnable<Integer> cir) {
        if (sneaking && !fluidName.isEmpty())
            cir.setReturnValue(23);
        else
            cir.setReturnValue(8);
    }

    @Unique
    private static void labs$drawMediumText(int x, int y, String text) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75f, 0.75f, 1);
        mc.fontRenderer.drawStringWithShadow(text, x * 4f / 3f, y * 4f / 3f, 0xffffffff);
        GlStateManager.popMatrix();
    }
}
