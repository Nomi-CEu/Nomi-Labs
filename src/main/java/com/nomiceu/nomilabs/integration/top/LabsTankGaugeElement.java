package com.nomiceu.nomilabs.integration.top;

import static io.github.drmanganese.topaddons.elements.ElementRenderHelper.drawSmallText;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.util.TextFormattingUtil;
import gregtech.client.utils.RenderUtil;
import io.github.drmanganese.topaddons.reference.Colors;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

/**
 * Our improved version of {@link io.github.drmanganese.topaddons.elements.ElementTankGauge}.
 */
public class LabsTankGaugeElement implements IElement {

    public static int BAR_WIDTH = 100;

    /* Input Values */
    private final String fluidName;
    private final String tankName;

    private final int fillColor;
    private final int outlineColor;

    private final int amount;
    private final int capacity;

    private final boolean expandedView;

    /* Cached Values */
    @Nullable
    private TextureAtlasSprite sprite;

    @Nullable
    private String formattedTitle;
    @Nullable
    private String formattedAmount;

    public LabsTankGaugeElement(@Nullable FluidStack fluid, String tankName, int capacity, boolean expandedView) {
        this.tankName = tankName;
        this.capacity = capacity;
        this.expandedView = expandedView;

        if (fluid != null) {
            fluidName = fluid.getFluid().getName();
            amount = fluid.amount;
            fillColor = Colors.getHashFromFluid(fluid);
            outlineColor = new Color(fillColor).darker().getRGB();
        } else {
            fluidName = null;
            amount = 0;
            fillColor = 0xff777777;
            outlineColor = 0xff535353;
        }
    }

    public LabsTankGaugeElement(ByteBuf byteBuf) {
        fluidName = NetworkTools.readStringUTF8(byteBuf);
        amount = byteBuf.readInt();

        capacity = byteBuf.readInt();

        tankName = NetworkTools.readStringUTF8(byteBuf);
        expandedView = byteBuf.readBoolean();

        fillColor = byteBuf.readInt();
        outlineColor = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        // Use auto handling of null strings
        NetworkTools.writeStringUTF8(byteBuf, fluidName);
        byteBuf.writeInt(amount);

        byteBuf.writeInt(capacity);

        NetworkTools.writeStringUTF8(byteBuf, tankName);
        byteBuf.writeBoolean(expandedView);

        byteBuf.writeInt(fillColor);
        byteBuf.writeInt(outlineColor);
    }

    @Override
    public void render(int x, int y) {
        boolean hasFluid = hasFluid();
        boolean expand = shouldExpand();
        int barHeight = expand ? 12 : 8;

        // Box
        int borderColor = hasFluid ? outlineColor : 0xff969696;
        RenderHelper.drawThickBeveledBox(x, y, x + BAR_WIDTH, y + barHeight, 1, borderColor, borderColor, 0x44969696);

        // Render fluid (Adaptation of RenderUtil#drawFluidForGui)
        if (hasFluid) {
            renderFluidTexture(x, y, barHeight);
        }

        for (int i = 1; i < 10; i++) {
            RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? barHeight - 1 : barHeight / 2),
                    borderColor);
        }

        if (expand) {
            ElementTextRender.render(formattedAmount(), x + 3, y + 2);
            ElementTextRender.render(formattedTitle(), x + 1, y + 14);
        } else {
            drawSmallText(x + 2, y + 2, tankName(), 0xffffffff);
        }
    }

    @Override
    public int getWidth() {
        return shouldExpand() ?
                Math.max(BAR_WIDTH, Minecraft.getMinecraft().fontRenderer.getStringWidth(formattedTitle())) : BAR_WIDTH;
    }

    @Override
    public int getHeight() {
        return shouldExpand() ? 25 : 8;
    }

    @Override
    public int getID() {
        return LabsTOPManager.TANK_GAUGE_ELEMENT;
    }

    private String tankName() {
        return LabsTranslate.translate(tankName);
    }

    private String formattedTitle() {
        if (formattedTitle != null) return formattedTitle;

        formattedTitle = tankName() + ": " + LabsFluidNameElement.translateFluid(fluidName, amount, "ElementTankGauge");
        return formattedTitle;
    }

    private String formattedAmount() {
        if (formattedAmount != null) return formattedAmount;

        formattedAmount = TextFormattingUtil.formatLongToCompactString(amount) + "L" + " / " +
                TextFormattingUtil.formatLongToCompactString(capacity) + "L";
        return formattedAmount;
    }

    private boolean shouldExpand() {
        return expandedView && hasFluid();
    }

    private boolean hasFluid() {
        return capacity > 0 && fluidName != null && !fluidName.isEmpty();
    }

    private void renderFluidTexture(int x, int y, int barHeight) {
        // Update sprite if required
        if (sprite == null) {
            sprite = LabsFluidStackElement.getFluidAtlasSprite("ElementTankGuage", fluidName);

            if (sprite == null) return;
        }

        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        RenderUtil.setGlColorFromInt(fillColor, 0xFF);

        int scaledAmount = (int) ((long) amount * 98 / capacity);

        int xTileCount = scaledAmount / 16;
        int xRemainder = scaledAmount - xTileCount * 16;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            int width = xTile == xTileCount ? xRemainder : 16;
            int fluidX = x + 1 + (xTile + 1) * 16 - 16;
            if (width > 0) {
                int maskTop = 16 - barHeight + 2;
                int maskRight = 16 - width;

                RenderUtil.drawFluidTexture(fluidX, y - 16 + barHeight - 1, sprite, maskTop, maskRight,
                        0.0);
            }
        }

        GlStateManager.disableBlend();
    }
}
