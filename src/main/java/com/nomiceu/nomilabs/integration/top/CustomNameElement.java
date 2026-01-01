package com.nomiceu.nomilabs.integration.top;

import static mcjty.theoneprobe.apiimpl.elements.ElementVertical.SPACING;
import static net.minecraft.util.text.TextFormatting.ITALIC;
import static net.minecraft.util.text.TextFormatting.RESET;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;

/**
 * An element that displays the normal ItemStack display name,
 * and a custom name only IF it is not the same as the ItemStack display name.
 */
public class CustomNameElement implements IElement {

    public static final int TEXT_HEIGHT = 10;

    private final ItemStack pickStack;
    private final String customName;

    public CustomNameElement(ItemStack pickStack, String customName) {
        this.pickStack = pickStack;
        this.customName = customName;
    }

    public CustomNameElement(ByteBuf buf) {
        if (buf.readBoolean()) {
            pickStack = NetworkTools.readItemStack(buf);
        } else {
            pickStack = ItemStack.EMPTY;
        }

        customName = NetworkTools.readStringUTF8(buf);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(int x, int y) {
        if (!pickStack.isEmpty())
            ElementTextRender.render(pickStack.getDisplayName(), x, y);
        if (shouldRender())
            ElementTextRender.render(getFormattedString(), x, y + TEXT_HEIGHT + SPACING);
    }

    @Override
    public int getWidth() {
        var pickStackWidth = pickStack.isEmpty() ? TEXT_HEIGHT : ElementTextRender.getWidth(pickStack.getDisplayName());

        return shouldRender() ? Math.max(pickStackWidth, ElementTextRender.getWidth(getFormattedString())) :
                pickStackWidth;
    }

    @Override
    public int getHeight() {
        return shouldRender() ? TEXT_HEIGHT * 2 + SPACING : TEXT_HEIGHT;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!pickStack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, pickStack);
        } else {
            buf.writeBoolean(false);
        }

        NetworkTools.writeStringUTF8(buf, customName);
    }

    @Override
    public int getID() {
        return LabsTOPManager.CUSTOM_NAME_ELEMENT;
    }

    private String getFormattedString() {
        return ITALIC + "(" + customName + ")" + RESET;
    }

    private boolean shouldRender() {
        if (pickStack.isEmpty()) return true;

        return !pickStack.getDisplayName().trim().equals(customName);
    }
}
