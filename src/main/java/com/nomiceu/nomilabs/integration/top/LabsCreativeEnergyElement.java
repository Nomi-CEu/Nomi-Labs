package com.nomiceu.nomilabs.integration.top;

import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.GTValues;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;

public class LabsCreativeEnergyElement implements IElement {

    private final byte voltage;
    private final int amperage;

    public LabsCreativeEnergyElement(byte voltage, int amperage) {
        this.voltage = voltage;
        this.amperage = amperage;
    }

    public LabsCreativeEnergyElement(ByteBuf buf) {
        voltage = buf.readByte();
        amperage = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeByte(voltage);
        byteBuf.writeInt(amperage);
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(getTranslated(), x, y);
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(getTranslated());
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public int getID() {
        return LabsTOPManager.CREATIVE_ENERGY_ELEMENT;
    }

    public String getTranslated() {
        return LabsTranslate.translate("nomilabs.top.creative_energy.prov", GTValues.VNF[voltage], amperage);
    }
}
