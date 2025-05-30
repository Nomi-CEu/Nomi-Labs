package com.nomiceu.nomilabs.integration.top;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.NomiLabs;

import gregtech.api.util.TextFormattingUtil;
import gregtech.client.utils.RenderUtil;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;

/**
 * From <a href=
 * "https://github.com/Supernoobv/GregicProbeCEu/blob/master/src/main/java/vfyjxf/gregicprobe/element/FluidStackElement.java">GregicProbe</a>.
 */
public class LabsFluidStackElement implements IElement {

    private final String fluidName;
    private final int color;
    private final int amount;

    @Nullable
    private TextureAtlasSprite sprite = null;

    public LabsFluidStackElement(@NotNull FluidStack stack) {
        // Save the fluidName, not the location, as that may need to be calculated on client
        this.fluidName = FluidRegistry.getFluidName(stack.getFluid());
        this.color = stack.getFluid().getColor(stack);
        this.amount = stack.amount;
    }

    public LabsFluidStackElement(@NotNull ByteBuf buf) {
        this.fluidName = NetworkTools.readStringUTF8(buf);
        this.color = buf.readInt();
        this.amount = buf.readInt();
    }

    @Override
    public void render(int x, int y) {
        if (sprite == null)
            sprite = getFluidAtlasSprite("LabsFluidStackElement", fluidName);

        GlStateManager.enableBlend();
        if (sprite != null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            RenderUtil.setGlColorFromInt(color, 0xFF);
            RenderUtil.drawFluidTexture(x + 1, y - 1, sprite, 2, 2, 0);
        }

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
    }

    @Override
    public int getWidth() {
        return 16;
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
    }

    @Override
    public int getID() {
        return LabsTOPManager.FLUID_STACK_ELEMENT;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFluidAtlasSprite(String packet, String fluidName) {
        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null) {
            NomiLabs.LOGGER.error("Received Fluid Info Packet {} with Unknown Fluid {}!", packet, fluidName);
            return null;
        }

        String actualLocation = fluid.getStill().toString();

        // Gregtech fluids added by GRS do this for some reason
        // As a consequence the fluid texture from /dull will not show up on anything from GRS.
        if (actualLocation.contains("material_sets/fluid/") &&
                (actualLocation.contains("/gas") || actualLocation.contains("/plasma"))) {
            actualLocation = actualLocation.replace("material_sets/fluid/", "material_sets/dull/");
        }

        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(actualLocation);
    }
}
