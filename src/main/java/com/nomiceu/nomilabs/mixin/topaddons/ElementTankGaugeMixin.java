package com.nomiceu.nomilabs.mixin.topaddons;

import static io.github.drmanganese.topaddons.elements.ElementRenderHelper.drawSmallText;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.top.LabsFluidNameElement;
import com.nomiceu.nomilabs.integration.top.LabsFluidStackElement;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.util.TextFormattingUtil;
import gregtech.client.utils.RenderUtil;
import io.github.drmanganese.topaddons.elements.ElementTankGauge;
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

    @Unique
    private TextureAtlasSprite labs$sprite = null;

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void newRenderLogic(int x, int y, CallbackInfo ci) {
        boolean hasFluid = capacity > 0 && amount > 0 && fluidName != null && !fluidName.isEmpty();
        boolean expand = sneaking && hasFluid;
        int barHeight = expand ? 12 : 8;

        ci.cancel();

        // Update sprite if needed
        if (hasFluid && labs$sprite == null) {
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            if (fluid == null)
                NomiLabs.LOGGER.error("Received Fluid Info Packet ElementTankGauge with Unknown Fluid {}!", fluidName);
            else
                labs$sprite = LabsFluidStackElement.getFluidAtlasSprite(fluid.getStill().toString());
        }

        // Box
        int borderColor = hasFluid ? color2 : 0xff969696;
        RenderHelper.drawThickBeveledBox(x, y, x + 100, y + barHeight, 1, borderColor, borderColor, 0x44969696);

        // Render fluid (Adaptation of RenderUtil#drawFluidForGui)
        if (hasFluid && labs$sprite != null) {
            labs$renderFluidTexture(x, y, barHeight);
        }

        for (int i = 1; i < 10; i++) {
            RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? barHeight - 1 : barHeight / 2),
                    borderColor);
        }

        if (expand) {
            ElementTextRender.render(labs$getCapacityInfo(), x + 3, y + 2);
            ElementTextRender.render(labs$getTankFluidTitle(), x + 1, y + 14);
        } else {
            drawSmallText(x + 2, y + 2, labs$getTranslatedTankName(), 0xffffffff);
        }
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
    private void labs$renderFluidTexture(int x, int y, int barHeight) {
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        RenderUtil.setGlColorFromInt(color1, 0xFF);

        int scaledAmount = (int) ((long) amount * 98 / capacity);

        int xTileCount = scaledAmount / 16;
        int xRemainder = scaledAmount - xTileCount * 16;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            int width = xTile == xTileCount ? xRemainder : 16;
            int fluidX = x + 1 + (xTile + 1) * 16 - 16;
            if (width > 0) {
                int maskTop = 16 - barHeight + 2;
                int maskRight = 16 - width;

                RenderUtil.drawFluidTexture(fluidX, y - 16 + barHeight - 1, labs$sprite, maskTop, maskRight,
                        0.0);
            }
        }

        GlStateManager.disableBlend();
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
