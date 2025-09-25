package com.nomiceu.nomilabs.integration.top;

import net.minecraft.item.ItemStack;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.network.NetworkTools;

/**
 * Doesn't override ElementItemStack as the itemStack isn't protected.
 */
public class LabsItemOutputElement implements IElement {

    private static final IItemStyle style = new ItemStyle().width(16).height(16);

    private final ItemStack stack;
    private boolean expanded = false;

    public LabsItemOutputElement(ItemStack stack) {
        this.stack = stack;
    }

    public LabsItemOutputElement(ByteBuf buf) {
        if (buf.readBoolean()) {
            stack = NetworkTools.readItemStack(buf);
        } else {
            stack = ItemStack.EMPTY;
        }

        expanded = buf.readBoolean();
    }

    public void expand() {
        expanded = true;
    }

    @Override
    public void render(int x, int y) {
        ElementItemStackRender.render(stack, style, x, y);
        if (expanded)
            ElementTextRender.render(getTranslated(), x + 26, y + 4);
    }

    public String getTranslated() {
        return stack.getDisplayName();
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
    public void toBytes(ByteBuf buf) {
        if (!stack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, stack);
        } else {
            buf.writeBoolean(false);
        }

        buf.writeBoolean(expanded);
    }

    @Override
    public int getID() {
        return LabsTOPManager.ITEM_OUTPUT_ELEMENT;
    }
}
