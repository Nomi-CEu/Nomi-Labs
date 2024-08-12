package com.nomiceu.nomilabs.integration.top;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
    private final String translatedName;

    public LabsFluidNameElement(FluidStack fluid) {
        this.fluidName = fluid.getFluid().getName();
        this.amount = fluid.amount;

        // Temp Translated Name, for usage if needed
        this.translatedName = fluid.getUnlocalizedName();
    }

    public LabsFluidNameElement(ByteBuf byteBuf) {
        this.fluidName = NetworkTools.readStringUTF8(byteBuf);
        this.amount = byteBuf.readInt();
        this.translatedName = translateFluid(fluidName, amount, "LabsFluidNameElement");
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(translatedName);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        NetworkTools.writeStringUTF8(byteBuf, fluidName);
        byteBuf.writeInt(amount);
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(
                TextStyleClass.NAME + LabsTranslate.translate("nomilabs.gui.top_override.fluid", translatedName), x, y);
    }

    @Override
    public int getID() {
        return LabsTOPManager.FLUID_NAME_ELEMENT;
    }

    public static String translateFluid(String fluidName, int amount, String packet) {
        var fluid = FluidRegistry.getFluid(fluidName);

        // At least try and translate it if fluid is null
        if (fluid == null) {
            NomiLabs.LOGGER.error("Received Fluid Info Packet {} with Unknown Fluid {}!", packet, fluidName);
            return LabsTranslate.translate(fluidName);
        }

        return fluid.getLocalizedName(new FluidStack(fluid, amount));
    }
}
