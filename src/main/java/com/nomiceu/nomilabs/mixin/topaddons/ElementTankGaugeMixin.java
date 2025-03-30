package com.nomiceu.nomilabs.mixin.topaddons;

import static io.github.drmanganese.topaddons.elements.ElementRenderHelper.drawSmallText;

import java.util.Objects;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.top.LabsFluidNameElement;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.util.TextFormattingUtil;
import io.github.drmanganese.topaddons.elements.ElementTankGauge;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
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
    @Mutable
    @Final
    private int color1;

    @Shadow
    @Final
    private boolean sneaking;

    @Shadow
    @Final
    private int color2;

    @Unique
    private String labs$tankFluidTitle = null;

    @Unique
    private String labs$capacityInfo = null;

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void newRenderLogic(int x, int y, CallbackInfo ci) {
        boolean hasFluid = capacity > 0 && amount > 0;
        boolean expand = sneaking && hasFluid;

        int barHeight = expand ? 12 : 8;
        IProgressStyle style = new ProgressStyle()
                .borderColor(hasFluid ? color2 : 0xff969696)
                .filledColor(color1)
                .alternateFilledColor(color1)
                .backgroundColor(0x44969696)
                .showText(false);

        ElementProgressRender.render(style, amount, capacity, x, y, 100, barHeight);

        for (int i = 1; i < 10; i++) {
            RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? barHeight - 1 : barHeight / 2),
                    hasFluid ? color2 : 0xff767676);
        }

        if (expand) {
            ElementTextRender.render(labs$getCapacityInfo(), x + 3, y + 2);
            ElementTextRender.render(labs$getTankFluidTitle(), x + 1, y + 14);
        } else {
            drawSmallText(x + 2, y + 2, labs$getTranslatedTankName(), 0xffffffff);
        }

        ci.cancel();
    }

    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    private void newWidthLogic(CallbackInfoReturnable<Integer> cir) {
        if (sneaking && capacity > 0 && amount > 0)
            cir.setReturnValue(
                    Math.max(100, Minecraft.getMinecraft().fontRenderer.getStringWidth(labs$getTankFluidTitle())));
    }

    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    private void newHeightLogic(CallbackInfoReturnable<Integer> cir) {
        if (sneaking && !fluidName.isEmpty())
            cir.setReturnValue(25);
        else
            cir.setReturnValue(8);
    }

    @Unique
    private String labs$getTankFluidTitle() {
        if (labs$tankFluidTitle != null) return labs$tankFluidTitle;

        labs$tankFluidTitle = labs$getTranslatedTankName() + ": " +
                LabsFluidNameElement.translateFluid(fluidName, amount, "ElementTankGauge");
        return labs$tankFluidTitle;
    }

    @Unique
    private String labs$getTranslatedTankName() {
        return LabsTranslate.translate(tankName);
    }

    @Unique
    private String labs$getCapacityInfo() {
        if (labs$capacityInfo != null) return labs$capacityInfo;

        String suffixToUse = suffix;
        if (Objects.equals(suffix, "mB"))
            suffixToUse = "L";

        labs$capacityInfo = TextFormattingUtil.formatLongToCompactString(amount) + suffixToUse + " / " +
                TextFormattingUtil.formatLongToCompactString(capacity) + suffixToUse;
        return labs$capacityInfo;
    }
}
