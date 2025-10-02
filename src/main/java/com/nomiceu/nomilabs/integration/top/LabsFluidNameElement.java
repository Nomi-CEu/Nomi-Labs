package com.nomiceu.nomilabs.integration.top;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.util.LabsTranslate;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;

public class LabsFluidNameElement implements IElement {

    private final String fluidName;
    private final int amount;
    @Nullable
    private final String lang;
    private final String translatedName;

    public LabsFluidNameElement(FluidStack fluid, @Nullable String lang) {
        this.fluidName = fluid.getFluid().getName();
        this.amount = fluid.amount;
        this.lang = lang;

        // Temp Translated Name, for usage if needed
        this.translatedName = fluid.getUnlocalizedName();
    }

    public LabsFluidNameElement(ByteBuf byteBuf) {
        this.fluidName = NetworkTools.readStringUTF8(byteBuf);
        this.amount = byteBuf.readInt();
        if (byteBuf.readBoolean())
            this.lang = NetworkTools.readStringUTF8(byteBuf);
        else
            this.lang = null;
        this.translatedName = LabsTOPUtils.translateFluid(
                fluidName, LabsTOPUtils.getFluid(fluidName, "LabsFluidNameElement"), amount);
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
        byteBuf.writeBoolean(lang != null);
        if (lang != null)
            NetworkTools.writeStringUTF8(byteBuf, lang);
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
        if (lang != null)
            return LabsTranslate.translate(lang, translatedName);
        else
            return translatedName;
    }
}
