package com.nomiceu.nomilabs.integration.top;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.util.TextFormattingUtil;
import gregtech.client.utils.RenderUtil;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;

/**
 * Modified From <a href=
 * "https://github.com/Supernoobv/GregicProbeCEu/blob/master/src/main/java/vfyjxf/gregicprobe/element/FluidStackElement.java">GregicProbe</a>.
 */
public class LabsFluidOutputElement implements IElement {

    private final String fluidName;
    private final int color;
    private final int amount;
    private boolean expanded = false;

    @Nullable
    private Fluid fluid;

    @Nullable
    private TextureAtlasSprite sprite = null;

    public LabsFluidOutputElement(@NotNull FluidStack stack) {
        // Save the fluidName, not the location, as that may need to be calculated on client
        this.fluidName = FluidRegistry.getFluidName(stack.getFluid());
        this.color = stack.getFluid().getColor(stack);
        this.amount = stack.amount;
    }

    public LabsFluidOutputElement(@NotNull ByteBuf buf) {
        fluidName = NetworkTools.readStringUTF8(buf);
        color = buf.readInt();
        amount = buf.readInt();
        expanded = buf.readBoolean();
        fluid = LabsTOPUtils.getFluid(fluidName, "LabsFluidStackElement");
        sprite = LabsTOPUtils.getFluidAtlasSprite(fluid);
    }

    @Override
    public void render(int x, int y) {
        if (fluid == null || sprite == null) return;

        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        RenderUtil.setGlColorFromInt(color, 0xFF);
        RenderUtil.drawFluidTexture(x + 1, y - 1, sprite, 2, 2, 0);

        if (amount > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 1);
            Minecraft minecraft = Minecraft.getMinecraft();
            String format = TextFormattingUtil.formatLongToCompactString(amount) + "L";
            minecraft.fontRenderer.drawStringWithShadow(
                    format,
                    (x + 17) * 2 - 1 - minecraft.fontRenderer.getStringWidth(format),
                    (y + 17) * 2 - 1 - minecraft.fontRenderer.FONT_HEIGHT,
                    0xFFFFFF);
            GlStateManager.popMatrix();
        }

        GlStateManager.disableBlend();

        if (expanded)
            ElementTextRender.render(TextStyleClass.NAME + getTranslated(), x + 26, y + 4);
    }

    public String getTranslated() {
        return LabsTOPUtils.translateFluid(fluidName, fluid, amount);
    }

    public void expand() {
        expanded = true;
    }

    @Override
    public int getWidth() {
        return 16 + (expanded ? 10 + ElementTextRender.getWidth(getTranslated()) : 0);
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void toBytes(@NotNull ByteBuf buf) {
        NetworkTools.writeStringUTF8(buf, fluidName);
        buf.writeInt(color);
        buf.writeInt(amount);
        buf.writeBoolean(expanded);
    }

    @Override
    public int getID() {
        return LabsTOPManager.FLUID_OUTPUT_ELEMENT;
    }
}
