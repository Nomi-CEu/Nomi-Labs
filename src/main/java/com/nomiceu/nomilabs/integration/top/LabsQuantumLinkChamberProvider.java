package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.items.IItemHandler;

import org.apache.commons.lang3.text.WordUtils;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.util.LabsTranslate;

import appeng.api.AEApi;
import appeng.api.features.ILocatable;
import appeng.me.cluster.implementations.QuantumCluster;
import appeng.tile.qnb.TileQuantumBridge;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

public class LabsQuantumLinkChamberProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":quantum_link_chamber_provider";
    }

    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState,
                             IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());

        if (!(te instanceof TileQuantumBridge bridge)) return;
        if (!AEApi.instance().definitions().blocks().quantumLink().maybeBlock()
                .map((link) -> bridge.getBlockType() == link).orElse(false))
            return;

        // Formed Info
        if (!bridge.isFormed()) {
            info.element(new ElementText(LabsTranslate.topTranslate("nomilabs.top.quantum_link_chamber.not_formed")));
            return;
        }
        info.element(new ElementText(LabsTranslate.topTranslate("nomilabs.top.quantum_link_chamber.formed")));

        // Powered Info
        if (!bridge.isPowered()) {
            info.element(new ElementText(LabsTranslate.topTranslate("nomilabs.top.quantum_link_chamber.not_powered")));
            return;
        }

        // Non QED Connection Info
        IItemHandler inv = bridge.getInternalInventory();
        long freq = bridge.getQEFrequency();
        if (freq == 0) {
            if (!inv.getStackInSlot(1).isEmpty()) {
                info.element(new ElementText(LabsTranslate.topTranslate("nomilabs.top.quantum_link_chamber.wireless")));
            } else {
                info.element(
                        new ElementText(LabsTranslate.topTranslate("nomilabs.top.quantum_link_chamber.no_operation")));
            }
            return;
        }

        // QED Connection Info
        var cluster = (QuantumCluster) bridge.getCluster();
        long serial = -cluster.getLocatableSerial(); // The other cluster has the negative serial of this one
        ILocatable locatable = AEApi.instance().registries().locatable().getLocatableBy(serial);

        if (!(locatable instanceof QuantumCluster other)) {
            info.element(
                    new ElementText(LabsTranslate.topTranslate("nomilabs.top.quantum_link_chamber.bad_connection")));
            return;
        }

        info.element(new ConnectionInfoElement(other.getCenter().getPos(),
                other.getCenter().getWorld().provider.getDimension()));
    }

    public static class ConnectionInfoElement implements IElement {

        private static final String KEY = "nomilabs.top.quantum_link_chamber.connected";
        private final BlockPos loc;
        private final int dim;
        private final String dimName;

        public ConnectionInfoElement(BlockPos loc, int dim) {
            this.loc = loc;
            this.dim = dim;
            this.dimName = "";
        }

        public ConnectionInfoElement(ByteBuf buf) {
            this.loc = NetworkTools.readPos(buf);
            this.dim = buf.readInt();
            this.dimName = WordUtils.capitalizeFully(
                    DimensionManager.getProviderType(dim).getName().replace("_", " "));
        }

        @Override
        public void render(int x, int y) {
            RenderHelper.renderText(Minecraft.getMinecraft(), x, y, getTranslated());
        }

        @Override
        public int getWidth() {
            return Minecraft.getMinecraft().fontRenderer.getStringWidth(getTranslated());
        }

        @Override
        public int getHeight() {
            return 10;
        }

        @Override
        public void toBytes(ByteBuf byteBuf) {
            NetworkTools.writePos(byteBuf, loc);
            byteBuf.writeInt(dim);
        }

        @Override
        public int getID() {
            return LabsTOPManager.QUANTUM_LINK_CONNECTION_ELEMENT;
        }

        public String getTranslated() {
            return LabsTranslate.translate(KEY, loc.getX(), loc.getY(), loc.getZ(), dimName);
        }
    }
}
