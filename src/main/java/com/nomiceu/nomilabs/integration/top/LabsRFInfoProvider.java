package com.nomiceu.nomilabs.integration.top;

import static mcjty.theoneprobe.api.TextStyleClass.PROGRESS;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

import mcjty.lib.api.power.IBigPower;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.compat.RedstoneFluxTools;
import mcjty.theoneprobe.config.Config;

/**
 * A duplicate of TOP RF Info Provider Logic, but separate
 * Allows for rearranging the RF bar.
 */
public class LabsRFInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":rf_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state,
                             IProbeHitData data) {
        if (LabsConfig.topSettings.rfProviderMode == 0) return;

        TileEntity te = world.getTileEntity(data.getPos());
        if (te == null) return;

        if (te instanceof IBigPower) {
            long energy = ((IBigPower) te).getStoredPower();
            long maxEnergy = ((IBigPower) te).getCapacity();
            addRFInfo(info, LabsConfig.topSettings.rfProviderMode, energy, maxEnergy);
            return;
        }

        if (Loader.isModLoaded(LabsValues.REDSTONE_FLUX_MODID) && RedstoneFluxTools.isEnergyHandler(te)) {
            int energy = RedstoneFluxTools.getEnergy(te);
            int maxEnergy = RedstoneFluxTools.getMaxEnergy(te);
            addRFInfo(info, LabsConfig.topSettings.rfProviderMode, energy, maxEnergy);
            return;
        }

        if (te.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage handler = te.getCapability(CapabilityEnergy.ENERGY, null);
            if (handler != null) {
                addRFInfo(info, LabsConfig.topSettings.rfProviderMode, handler.getEnergyStored(),
                        handler.getMaxEnergyStored());
            }
        }
    }

    /**
     * Adds information about Redstone Flux energy based on the given configuration.
     * If the RF mode in the configuration is set to 1, a progress bar is displayed with specific styling.
     * Otherwise, a text representation of the RF energy is shown.
     *
     * @author McJty
     *
     * @param probeInfo The {@link IProbeInfo} object to which the RF information will be added.
     * @param rfMode    Mode to display RF in.
     * @param energy    The current amount of RF energy.
     * @param maxEnergy The maximum capacity of RF energy.
     */
    private void addRFInfo(IProbeInfo probeInfo, int rfMode, long energy, long maxEnergy) {
        if (rfMode == 1) {
            probeInfo.progress(energy, maxEnergy,
                    probeInfo.defaultProgressStyle()
                            .suffix("RF")
                            .filledColor(Config.rfbarFilledColor)
                            .alternateFilledColor(Config.rfbarAlternateFilledColor)
                            .borderColor(Config.rfbarBorderColor)
                            .numberFormat(Config.rfFormat));
        } else {
            probeInfo.text(PROGRESS + "RF: " + ElementProgress.format(energy, Config.rfFormat, "RF"));
        }
    }
}
