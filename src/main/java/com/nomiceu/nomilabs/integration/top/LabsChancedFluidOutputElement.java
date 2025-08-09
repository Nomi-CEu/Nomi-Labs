package com.nomiceu.nomilabs.integration.top;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import io.netty.buffer.ByteBuf;

public class LabsChancedFluidOutputElement extends LabsFluidOutputElement {

    private final int chance;

    public LabsChancedFluidOutputElement(@NotNull FluidStack stack, int chance) {
        super(stack);
        this.chance = chance;
    }

    public LabsChancedFluidOutputElement(@NotNull ByteBuf buf) {
        super(buf);
        chance = buf.readInt();
    }

    @Override
    public void render(int x, int y) {
        super.render(x, y);
        LabsTOPUtils.renderChance(chance, x, y);
    }

    @Override
    public String getTranslated() {
        return super.getTranslated() + " (" + LabsTOPUtils.formatChance(chance) + ")";
    }

    @Override
    public void toBytes(@NotNull ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(chance);
    }

    @Override
    public int getID() {
        return LabsTOPManager.CHANCED_FLUID_OUTPUT_ELEMENT;
    }
}
