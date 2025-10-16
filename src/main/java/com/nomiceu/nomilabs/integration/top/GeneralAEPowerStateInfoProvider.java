package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.nomiceu.nomilabs.LabsValues;

import appeng.api.implementations.IPowerChannelState;
import appeng.helpers.AEMultiTile;
import appeng.integration.modules.theoneprobe.TheOneProbeText;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;

/**
 * Allows showing the power state info to tiles that implement IGridProxyable.
 */
public class GeneralAEPowerStateInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":general_ae_power_state_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state,
                             IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof IPowerChannelState) return; // Handled by AE Default

        if (te instanceof AEMultiTile) return; // AE Cables

        if (!(te instanceof IGridProxyable proxyable)) return;
        AENetworkProxy proxy = proxyable.getProxy();

        if (proxy == null) return;

        boolean isActive = proxy.isActive();
        boolean isPowered = proxy.isPowered();
        if (isActive && isPowered) {
            info.text(TheOneProbeText.DEVICE_ONLINE.getLocal());
        } else if (isPowered) {
            info.text(TheOneProbeText.DEVICE_MISSING_CHANNEL.getLocal());
        } else {
            info.text(TheOneProbeText.DEVICE_OFFLINE.getLocal());
        }
    }
}
