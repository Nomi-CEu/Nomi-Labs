package com.nomiceu.nomilabs.integration.top;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

/**
 * Almost identical to {@link mcjty.theoneprobe.apiimpl.elements.ElementItemStack}, but renders a custom count.
 */
public class LabsCustomCountItemStackElement implements IElement {

    private static final IItemStyle STYLE = new ItemStyle();

    private final ItemStack stack;
    private final String countText;

    /**
     * Inits the custom count element.
     * 
     * @param stack The ItemStack. Must not be empty, recommendation is for count to be 1.
     * @param text  Count text to render.
     */
    public LabsCustomCountItemStackElement(ItemStack stack, String text) {
        this.stack = stack;
        this.countText = text;
    }

    public LabsCustomCountItemStackElement(ByteBuf buf) {
        stack = NetworkTools.readItemStack(buf);
        countText = NetworkTools.readStringUTF8(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeItemStack(buf, stack);
        NetworkTools.writeStringUTF8(buf, countText);
    }

    /**
     * Simplified custom version of {@link mcjty.theoneprobe.apiimpl.client.ElementItemStackRender#render}
     */
    @Override
    public void render(int x, int y) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

        if (!RenderHelper.renderItemStack(Minecraft.getMinecraft(), itemRender, stack,
                x + (STYLE.getWidth() - 18) / 2, y + (STYLE.getHeight() - 18) / 2, countText)) {
            RenderHelper.renderText(Minecraft.getMinecraft(), x, y,
                    TextFormatting.RED + "ERROR: " + stack.getDisplayName());
        }
    }

    @Override
    public int getWidth() {
        return STYLE.getWidth();
    }

    @Override
    public int getHeight() {
        return STYLE.getHeight();
    }

    @Override
    public int getID() {
        return LabsTOPManager.LOCKED_ITEM_ELEMENT;
    }
}
