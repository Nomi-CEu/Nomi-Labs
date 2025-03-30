package com.nomiceu.nomilabs.integration.top;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.util.LabsTranslate;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;

public class LabsFluidNameElement implements IElement {

    private final String fluidName;
    private final int amount;
    private final boolean showLang;
    private final String translatedName;

    public LabsFluidNameElement(FluidStack fluid, boolean showLang) {
        this.fluidName = fluid.getFluid().getName();
        this.amount = fluid.amount;
        this.showLang = showLang;

        // Temp Translated Name, for usage if needed
        this.translatedName = fluid.getUnlocalizedName();
    }

    public LabsFluidNameElement(ByteBuf byteBuf) {
        this.fluidName = NetworkTools.readStringUTF8(byteBuf);
        this.amount = byteBuf.readInt();
        this.showLang = byteBuf.readBoolean();
        this.translatedName = translateFluid(fluidName, amount, "LabsFluidNameElement");
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(getTranslated());
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        NetworkTools.writeStringUTF8(byteBuf, fluidName);
        byteBuf.writeInt(amount);
        byteBuf.writeBoolean(showLang);
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(TextStyleClass.NAME + getTranslated(), x, y);
    }

    @Override
    public int getID() {
        return LabsTOPManager.FLUID_NAME_ELEMENT;
    }

    public String getTranslated() {
        if (showLang)
            return LabsTranslate.translate("nomilabs.gui.top_override.fluid", translatedName);
        else
            return translatedName;
    }

    public static String translateFluid(@Nullable String fluidName, int amount, String packet) {
        if (fluidName == null || fluidName.isEmpty()) return ""; // Empty Tank

        var fluid = FluidRegistry.getFluid(fluidName);

        // At least try and translate it if fluid is null
        if (fluid == null) {
            NomiLabs.LOGGER.error("Received Fluid Info Packet {} with Unknown Fluid {}!", packet, fluidName);
            return LabsTranslate.translate(fluidName);
        }

        return fluid.getLocalizedName(new FluidStack(fluid, amount));
    }
}
