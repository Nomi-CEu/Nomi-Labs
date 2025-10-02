package com.nomiceu.nomilabs.integration.top;

import javax.annotation.Nullable;

import com.nomiceu.nomilabs.util.LabsTranslate;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;

public class LabsLangKeyWithArgsElement implements IElement {

    private final String key;
    private final String[] args;

    @Nullable
    private String localised;

    public LabsLangKeyWithArgsElement(String key, String... args) {
        this.key = key;
        this.args = args;
    }

    public LabsLangKeyWithArgsElement(ByteBuf buf) {
        key = NetworkTools.readStringUTF8(buf);
        args = new String[buf.readInt()];
        for (int i = 0; i < args.length; i++) {
            args[i] = NetworkTools.readStringUTF8(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        NetworkTools.writeStringUTF8(byteBuf, key);

        byteBuf.writeInt(args.length);
        for (String arg : args) {
            NetworkTools.writeStringUTF8(byteBuf, arg);
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
        return LabsTOPManager.TEXT_WITH_ARGS_ELEMENT;
    }

    public String getLocalised() {
        if (localised != null) return localised;

        localised = LabsTranslate.translate(key, (Object[]) args);
        return localised;
    }
}
