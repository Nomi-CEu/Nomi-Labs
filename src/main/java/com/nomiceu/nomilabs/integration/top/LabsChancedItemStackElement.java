package com.nomiceu.nomilabs.integration.top;

import net.minecraft.item.ItemStack;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.elements.ElementItemStack;

public class LabsChancedItemStackElement extends ElementItemStack {

    private final int chance;

    public LabsChancedItemStackElement(ItemStack itemStack, int chance, IItemStyle style) {
        super(itemStack, style);
        this.chance = chance;
    }

    public LabsChancedItemStackElement(ByteBuf buf) {
        super(buf);
        chance = buf.readInt();
    }

    @Override
    public void render(int x, int y) {
        super.render(x, y);
        LabsTOPUtils.renderChance(chance, x, y);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(chance);
    }

    @Override
    public int getID() {
        return LabsTOPManager.CHANCED_ITEM_STACK_ELEMENT;
    }
}
