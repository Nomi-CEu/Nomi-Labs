package com.nomiceu.nomilabs.integration.top;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import com.nomiceu.nomilabs.LabsTextures;
import com.nomiceu.nomilabs.util.LabsTranslate;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;

public class LabsPropertyElement implements IElement {

    public static final String[] TEXT_BY_IDX = new String[] {
            "nomilabs.top.properties.locked",
            "nomilabs.top.properties.voiding"
    };

    public static final int ICON_SIZE = 12;
    public static final int ICON_BUFFER = 4;

    private final int iconIdx;
    private final boolean expand;

    public LabsPropertyElement(int iconIdx, boolean expand) {
        this.iconIdx = iconIdx;
        this.expand = expand;
    }

    public LabsPropertyElement(ByteBuf buf) {
        this.iconIdx = buf.readInt();
        this.expand = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(iconIdx);
        buf.writeBoolean(expand);
    }

    @Override
    public void render(int x, int y) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        LabsTextures.TOP_ICONS[iconIdx].draw(x, y, ICON_SIZE, ICON_SIZE);

        if (expand) {
            ElementTextRender.render(getText(), x + ICON_SIZE + ICON_BUFFER, y + 2); // To centre it vertically
        }
    }

    @Override
    public int getWidth() {
        if (expand) {
            return ICON_SIZE + ICON_BUFFER + Minecraft.getMinecraft().fontRenderer.getStringWidth(getText());
        }

        return ICON_SIZE;
    }

    @Override
    public int getHeight() {
        return ICON_SIZE;
    }

    @Override
    public int getID() {
        return LabsTOPManager.PROPERTIES_ELEMENT;
    }

    public String getText() {
        return LabsTranslate.translate(TEXT_BY_IDX[iconIdx]);
    }
}
