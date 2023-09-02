package com.nomiceu.nomilabs.fluid.registry;

import com.nomiceu.nomilabs.LabsValues;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * (Excerpted from Tinkers' Construct with permission, thanks guys!)
 */
@SideOnly(Side.CLIENT)
public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

    public final Fluid fluid;
    public final ModelResourceLocation location;

    public FluidStateMapper(Fluid fluid) {
        this.fluid = fluid;

        this.location = new ModelResourceLocation(new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "fluids"), fluid.getName());
    }

    @Override
    protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
        return this.location;
    }

    @Override
    public @NotNull ModelResourceLocation getModelLocation(@NotNull ItemStack stack) {
        return this.location;
    }
}