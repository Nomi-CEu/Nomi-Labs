package com.nomiceu.nomilabs.integration.top;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.ByteBuf;

public class LabsChancedFluidNameElement extends LabsFluidNameElement {

    private final int chance;

    public LabsChancedFluidNameElement(FluidStack fluid, int chance, @Nullable String lang) {
        super(fluid, lang);
        this.chance = chance;
    }

    public LabsChancedFluidNameElement(ByteBuf byteBuf) {
        super(byteBuf);
        chance = byteBuf.readInt();
    }

    @Override
    public String getTranslated() {
        return super.getTranslated() + " (" + RecipeOutputsProvider.formatChance(chance) + ")";
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        super.toBytes(byteBuf);
        byteBuf.writeInt(chance);
    }

    @Override
    public int getID() {
        return LabsTOPManager.CHANCED_FLUID_NAME_ELEMENT;
    }
}
