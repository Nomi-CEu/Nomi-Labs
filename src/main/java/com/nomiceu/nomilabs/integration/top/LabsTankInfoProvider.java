package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMetaTileEntityQuantumTank;
import com.nomiceu.nomilabs.util.LabsTranslate;

import cofh.thermalexpansion.block.storage.TileTank;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMap;
import io.github.drmanganese.topaddons.reference.Names;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;

/**
 * Our improved version of {@link io.github.drmanganese.topaddons.addons.AddonForge}.
 */
public class LabsTankInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":tank_info_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state,
                             IProbeHitData data) {
        /* Disable for enderio, endertanks (not enderstorage), as they already have their own fluid display */
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (rl == null || rl.getNamespace().equals("enderio") || rl.getNamespace().equals("endertanks"))
            return;

        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile == null) return;

        IFluidHandler cap = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        if (cap == null) return;

        // Locked Tank Display
        if (isTankLocked(tile)) {
            info.text(LabsTranslate.topTranslate("nomilabs.gui.top.tank.locked"));
        }

        // GT Machine Compat
        int inputAmt = 0;
        if (tile instanceof IGregTechTileEntity gt &&
                gt.getMetaTileEntity() instanceof SimpleMachineMetaTileEntity simple) {
            RecipeMap<?> recipeMap = simple.getRecipeMap();
            if (recipeMap != null)
                inputAmt = recipeMap.getMaxFluidInputs();
        }

        // Handle Custom Tank Properties
        IFluidTankProperties[] tanks = null;
        if (tile instanceof CustomFluidTankProvider provider) {
            tanks = provider.labs$getOverrideTanks();
        } else if (tile instanceof IGregTechTileEntity gt) {
            MetaTileEntity mte = gt.getMetaTileEntity();
            if (mte instanceof CustomFluidTankProvider provider)
                tanks = provider.labs$getOverrideTanks();
        }

        // Default Tank Properties
        if (tanks == null) tanks = cap.getTankProperties();

        boolean expandedView = mode == ProbeMode.EXTENDED ||
                tanks.length <= LabsConfig.topSettings.expandViewTankThreshold;

        for (int i = 0; i < tanks.length; i++) {
            IFluidTankProperties tank = tanks[i];

            // Name Handling
            String tankName = "nomilabs.gui.top.tank.default";
            if (Names.tankNamesMap.containsKey(tile.getClass()))
                tankName = Names.tankNamesMap.get(tile.getClass())[i];
            else if (inputAmt != 0) {
                if (i > inputAmt - 1)
                    tankName = "nomilabs.gui.top.tank.output";
                else
                    tankName = "nomilabs.gui.top.tank.input";
            }

            info.element(new LabsTankGaugeElement(tank.getContents(), tankName, tank.getCapacity(), expandedView));
        }
    }

    public boolean isTankLocked(TileEntity tile) {
        // Thermal Portable
        if (tile instanceof TileTank tank) {
            return tank.isLocked();
        }

        // GT Super/Quantum
        if (tile instanceof IGregTechTileEntity gt) {
            MetaTileEntity mte = gt.getMetaTileEntity();
            if (mte instanceof AccessibleMetaTileEntityQuantumTank tank) {
                return tank.labs$isLocked();
            }
        }

        return false;
    }
}
