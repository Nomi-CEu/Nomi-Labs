package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumTank;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.custom.QuantumStorageRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;

/**
 * Continues to render counts or fluid if tank is empty but locked.
 */
@Mixin(value = MetaTileEntityQuantumTank.class, remap = false)
public abstract class MetaTileEntityQuantumTankClientMixin extends MetaTileEntity {

    @Shadow
    protected abstract boolean isLocked();

    @Shadow
    private @Nullable FluidStack lockedFluid;

    @Shadow
    protected FluidTank fluidTank;

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityQuantumTankClientMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    /**
     * Render a fluid color if empty but locked.
     */
    @WrapOperation(method = "renderMetaTileEntity(Lcodechicken/lib/render/CCRenderState;Lcodechicken/lib/vec/Matrix4;[Lcodechicken/lib/render/pipeline/IVertexOperation;)V",
                   at = @At(value = "INVOKE",
                            target = "Lgregtech/client/renderer/texture/custom/QuantumStorageRenderer;renderTankFluid(Lcodechicken/lib/render/CCRenderState;Lcodechicken/lib/vec/Matrix4;[Lcodechicken/lib/render/pipeline/IVertexOperation;Lnet/minecraftforge/fluids/FluidTank;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)V"))
    private void renderLocked(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline,
                              FluidTank tank,
                              IBlockAccess world, BlockPos pos, EnumFacing frontFacing, Operation<Void> original) {
        if (!ConfigHolder.client.enableFancyChestRender) return;

        FluidStack stack = tank.getFluid();
        // If we contain fluids, or we aren't locked, use default impl
        if (!(isLocked() && lockedFluid != null) || !(stack == null || stack.amount == 0)) {
            original.call(renderState, translation, pipeline, tank, world, pos, frontFacing);
            return;
        }

        Fluid fluid = lockedFluid.getFluid();
        if (fluid == null) return;

        // Mostly from the original render code, but simplified
        boolean gas = fluid.isGaseous(lockedFluid);
        Cuboid6 partialFluidBox = new Cuboid6(1.0625 / 16.0, 2.0625 / 16.0, 1.0625 / 16.0, 14.9375 / 16.0,
                14.9375 / 16.0, 14.9375 / 16.0);

        if (gas) {
            partialFluidBox.min.y = 13.9375 / 16.0;
        } else {
            partialFluidBox.max.y = 2.0625 / 16.0;
        }

        renderState.setFluidColour(lockedFluid);
        ResourceLocation fluidStill = fluid.getStill(lockedFluid);
        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite(fluidStill.toString());

        Textures.renderFace(renderState, translation, pipeline, frontFacing, partialFluidBox, fluidStillSprite,
                BlockRenderLayer.CUTOUT_MIPPED);

        Textures.renderFace(renderState, translation, pipeline, gas ? EnumFacing.DOWN : EnumFacing.UP, partialFluidBox,
                fluidStillSprite,
                BlockRenderLayer.CUTOUT_MIPPED);

        GlStateManager.resetColor();

        renderState.reset();
    }

    /**
     * Renders count 0 if empty but locked.
     */
    @WrapMethod(method = "renderMetaTileEntity(DDDF)V")
    private void lockedCountRender(double x, double y, double z, float partialTicks, Operation<Void> original) {
        if (!((AccessibleQuantumTank) this).labs$isLocked() ||
                !(fluidTank.getFluid() == null || fluidTank.getFluid().amount == 0)) {
            original.call(x, y, z, partialTicks);
            return;
        }

        QuantumStorageRenderer.renderTankAmount(x, y, z, getFrontFacing(), 0);
    }
}
