package com.nomiceu.nomilabs.integration.top;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.util.LabsTranslate;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;

/**
 * Almost identical to {@link mcjty.theoneprobe.apiimpl.elements.ElementItemLabel}, but allows a custom lang key.
 */
public class LabsItemNameElement implements IElement {

    private final ItemStack stack;

    @Nullable
    private final String lang;

    @Nullable
    private String localised;

    public LabsItemNameElement(ItemStack stack, @Nullable String lang) {
        this.stack = stack;
        this.lang = lang;
    }

    public LabsItemNameElement(ByteBuf buf) {
        if (buf.readBoolean())
            stack = NetworkTools.readItemStack(buf);
        else
            stack = ItemStack.EMPTY;

        if (buf.readBoolean())
            lang = NetworkTools.readString(buf);
        else
            lang = null;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!stack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, stack);
        } else {
            buf.writeBoolean(false);
        }

        if (lang != null) {
            buf.writeBoolean(true);
            NetworkTools.writeString(buf, lang);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(getLocalised(), x, y);
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(getLocalised());
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getID() {
        return LabsTOPManager.ITEM_NAME_ELEMENT;
    }

    public String getLocalised() {
        if (localised != null) return localised;

        if (lang == null)
            localised = stack.getDisplayName();
        else
            localised = LabsTranslate.translate(lang, stack.getDisplayName());
        return localised;
    }
}
