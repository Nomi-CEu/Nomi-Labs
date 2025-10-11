package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumChest;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumStorage;
import com.nomiceu.nomilabs.util.LabsTranslate;

import cofh.thermalexpansion.block.storage.TileTank;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;

/**
 * Provides display properties for quantum storage and thermal tanks.
 * Also adds text specifying storage capacity and displays locked items if empty for quantum chests.
 * Storage Drawers are handled by {@link LabsDrawersProvider}.
 */
public class LabsPropertiesProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":properties_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state,
                             IProbeHitData data) {
        if (!state.getBlock().hasTileEntity(state)) return;

        TileEntity te = world.getTileEntity(data.getPos());

        if (checkQuantum(te, mode, info)) return;

        if (Loader.isModLoaded(LabsValues.THERMAL_EXPANSION_MODID)) {
            checkThermal(te, mode, info);
        }
    }

    private boolean checkQuantum(TileEntity te, ProbeMode mode, IProbeInfo info) {
        if (!(te instanceof IGregTechTileEntity igtte)) return false;

        MetaTileEntity mte = igtte.getMetaTileEntity();
        if (!(mte instanceof AccessibleQuantumStorage quantum))
            // Skip checking other TEs, as the others don't handle MTEs
            return true;

        if ((mte instanceof AccessibleQuantumChest chest)) {
            if (((MetaTileEntityQuantumChest) mte).getCombinedInventory().getStackInSlot(0).isEmpty()) {
                if (chest.labs$isLocked()) displayLockedItem(chest.labs$getLockedStack(), mode, info);
            }

            long stored = chest.labs$getMaxStored();

            var format = mode == ProbeMode.EXTENDED || stored < 10_000 ? NumberFormat.COMMAS : NumberFormat.COMPACT;
            String numText = ElementProgress.format(stored, format, "");

            info.text(TextStyleClass.LABEL + LabsTranslate.topTranslate("nomilabs.top.quantum_chest.cap") +
                    " " + TextStyleClass.INFO + numText);
        }

        LabsTOPUtils.addProperties(mode, info, quantum.labs$isLocked(), quantum.labs$isVoiding());
        return true;
    }

    private void displayLockedItem(ItemStack stack, ProbeMode mode, IProbeInfo info) {
        IProbeInfo vertical = info.vertical(
                new LayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(0));

        if (Tools.show(mode, Config.getRealConfig().getShowChestContentsDetailed()) &&
                1 <= Config.showItemDetailThresshold) { // Only 1 item stored in quantum
            vertical.horizontal(new LayoutStyle().spacing(8).alignment(ElementAlignment.ALIGN_CENTER))
                    .element(new LabsCustomCountItemStackElement(stack, "0"))
                    .itemLabel(stack);

            return;
        }

        vertical.element(new LabsCustomCountItemStackElement(stack, "0"));
    }

    private void checkThermal(TileEntity te, ProbeMode mode, IProbeInfo info) {
        if (!(te instanceof TileTank tank)) return;

        LabsTOPUtils.addProperties(mode, info, tank.isLocked(), false); // Thermal tanks cannot void
    }
}
