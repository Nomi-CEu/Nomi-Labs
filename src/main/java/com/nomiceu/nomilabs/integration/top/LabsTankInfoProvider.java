package com.nomiceu.nomilabs.integration.top;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;

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

        // Name Logic
        Function<Integer, String> name = null;

        // GT Machine Compat
        if (tile instanceof IGregTechTileEntity gt &&
                gt.getMetaTileEntity() instanceof SimpleMachineMetaTileEntity simple) {
            RecipeMap<?> recipeMap = simple.getRecipeMap();
            if (recipeMap != null) {
                name = (i) -> (i > recipeMap.getMaxFluidInputs() - 1) ?
                        "nomilabs.gui.top.tank.output" : "nomilabs.gui.top.tank.input";
            }
        } else if (Loader.isModLoaded(LabsValues.TOP_ADDONS_MODID) && getNameMap().containsKey(tile.getClass())) {
            String[] names = getNameMap().get(tile.getClass());
            name = (i) -> names[i];
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

        // Calculate Display Preferences
        boolean expanded = mode == ProbeMode.EXTENDED || tanks.length <= LabsConfig.topSettings.expandViewTankThreshold;

        for (int i = 0; i < tanks.length; i++) {
            IFluidTankProperties tank = tanks[i];

            String tankName = (name != null) ? name.apply(i) : "nomilabs.gui.top.tank.default";
            info.element(new LabsTankGaugeElement(tank.getContents(), tankName, tank.getCapacity(), expanded));
        }
    }

    private Map<Class<? extends TileEntity>, String[]> getNameMap() {
        return Names.tankNamesMap;
    }
}
