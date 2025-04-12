package com.nomiceu.nomilabs.integration.top;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import io.netty.buffer.ByteBuf;

public class LabsChancedFluidStackElement extends LabsFluidStackElement {

    private final int chance;

    public LabsChancedFluidStackElement(@NotNull FluidStack stack, int chance) {
        super(stack);
        this.chance = chance;
    }

    public LabsChancedFluidStackElement(@NotNull ByteBuf buf) {
        super(buf);
        chance = buf.readInt();
    }

    @Override
    public void render(int x, int y) {
        super.render(x, y);
        RecipeOutputsProvider.renderChance(chance, x, y);
    }

    @Override
    public void toBytes(@NotNull ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(chance);
    }

    @Override
    public int getID() {
        return LabsTOPManager.CHANCED_FLUID_STACK_ELEMENT;
    }
}
