package com.nomiceu.nomilabs.mixin.gregtech;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.gregtech.mixinhelper.LockableQuantumStorage;
import com.nomiceu.nomilabs.integration.top.CustomFluidTankProvider;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.custom.QuantumStorageRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;

/**
 * Provides locked fluid info to TOP. Properly syncs locked status to client; so we can display it in rendering.
 * Also, continues to render counts or fluid if tank is empty but locked.
 */
@Mixin(value = MetaTileEntityQuantumTank.class, remap = false)
public abstract class MetaTileEntityQuantumTankMixin extends MetaTileEntity
                                                     implements CustomFluidTankProvider, LockableQuantumStorage {

    @Shadow
    protected abstract boolean isLocked();

    @Shadow
    protected FluidTank fluidTank;

    @Shadow
    private @Nullable FluidStack lockedFluid;

    @Shadow
    @Final
    private int maxFluidCapacity;

    @Shadow
    protected abstract void setLocked(boolean locked);

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityQuantumTankMixin(ResourceLocation metaTileEntityId) {
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
        if (!labs$isLocked() || !(fluidTank.getFluid() == null || fluidTank.getFluid().amount == 0)) {
            original.call(x, y, z, partialTicks);
            return;
        }

        QuantumStorageRenderer.renderTankAmount(x, y, z, getFrontFacing(), 0);
    }

    /**
     * Sync locked stack in initial sync data.
     */
    @Inject(method = "writeInitialSyncData", at = @At("TAIL"))
    private void writeLockedInitialSync(PacketBuffer buf, CallbackInfo ci) {
        if (isLocked()) {
            if (lockedFluid == null)
                buf.writeBoolean(false);
            else {
                buf.writeBoolean(true);
                buf.writeCompoundTag(lockedFluid.writeToNBT(new NBTTagCompound()));
            }
        }
    }

    @Inject(method = "receiveInitialSyncData", at = @At("TAIL"))
    private void readLockedInitialSync(PacketBuffer buf, CallbackInfo ci) {
        try {
            if (isLocked() && buf.readBoolean())
                lockedFluid = FluidStack.loadFluidStackFromNBT(buf.readCompoundTag());
        } catch (IOException e) {
            NomiLabs.LOGGER.error(
                    "[QuantumTankMixin] Failed to load info from tile at {} from buffer in initial sync!",
                    getPos());
        }
    }

    @Inject(method = "receiveCustomData", at = @At("TAIL"))
    private void receiveLockedUpdate(int dataId, PacketBuffer buf, CallbackInfo ci) {
        if (dataId == GregtechDataCodes.UPDATE_LOCKED_STATE) {
            setLocked(buf.readBoolean());
            scheduleRenderUpdate();
        }
    }

    @Inject(method = "setLocked",
            at = @At(value = "INVOKE",
                     target = "Lgregtech/common/metatileentities/storage/MetaTileEntityQuantumTank;markDirty()V"))
    private void sendCustomData(boolean locked, CallbackInfo ci) {
        writeCustomData(GregtechDataCodes.UPDATE_LOCKED_STATE, buf -> buf.writeBoolean(locked));
    }

    @Unique
    @Override
    @Nullable
    public IFluidTankProperties[] labs$getOverrideTanks() {
        // Default logic: if not locked, no fluid tank, or there is fluid in tank
        if (!isLocked() || lockedFluid == null || fluidTank == null || fluidTank.getFluidAmount() != 0)
            return null;

        var fluidToUse = lockedFluid.copy();
        fluidToUse.amount = 0;

        return new FluidTankProperties[] { new FluidTankProperties(fluidToUse, maxFluidCapacity) };
    }

    @Unique
    @Override
    public boolean labs$isLocked() {
        return isLocked() && lockedFluid != null;
    }
}
