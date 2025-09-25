package com.nomiceu.nomilabs.integration.top;

import net.minecraft.item.ItemStack;

import io.netty.buffer.ByteBuf;

public class LabsChancedItemOutputElement extends LabsItemOutputElement {

    private final int chance;

    public LabsChancedItemOutputElement(ItemStack itemStack, int chance) {
        super(itemStack);
        this.chance = chance;
    }

    public LabsChancedItemOutputElement(ByteBuf buf) {
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
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(chance);
    }

    @Override
    public int getID() {
        return LabsTOPManager.CHANCED_ITEM_OUTPUT_ELEMENT;
    }
}
