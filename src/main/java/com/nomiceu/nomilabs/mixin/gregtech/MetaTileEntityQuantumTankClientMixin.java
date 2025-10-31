package com.nomiceu.nomilabs.mixin.gregtech;

import static net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack.FLUID_NBT_KEY;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
 * Changes some formatting and fixes an edge case in tooltip.
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

        if (renderContextStack != null) {
            // Rendering for stack; custom logic
            NBTTagCompound tag = renderContextStack.getTagCompound();
            if (tag == null) return;

            var locked = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("LockedFluid"));

            var handler = FluidUtil.getFluidHandler(renderContextStack);
            if (handler != null) {
                var prop = handler.getTankProperties()[0];
                FluidStack stack = prop.getContents();

                if (stack != null && stack.amount != 0) {
                    tank = new FluidTank(stack, prop.getCapacity());
                    original.call(renderState, translation, pipeline, tank, world, pos, frontFacing);
                    return;
                }
            }

            if (locked != null)
                labs$renderLockedFluid(renderState, translation, pipeline, world, pos, locked);

            return;
        }

        FluidStack stack = tank.getFluid();

        // We aren't locked, or contain fluids, use default impl
        if (!(isLocked() && lockedFluid != null) || !(stack == null || stack.amount == 0)) {
            original.call(renderState, translation, pipeline, tank, world, pos, frontFacing);
            return;
        }
        labs$renderLockedFluid(renderState, translation, pipeline, world, pos, lockedFluid);
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

    @Unique
    private void labs$renderLockedFluid(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline,
                                        IBlockAccess world, BlockPos pos, FluidStack locked) {
        Fluid fluid = locked.getFluid();
        if (fluid == null) return;

        if (world != null) {
            renderState.setBrightness(world, pos);
        }

        // Mostly from the original render code, but simplified
        boolean gas = fluid.isGaseous(locked);
        Cuboid6 partialFluidBox = new Cuboid6(1.0625 / 16.0, 2.0625 / 16.0, 1.0625 / 16.0, 14.9375 / 16.0,
                14.9375 / 16.0, 14.9375 / 16.0);

        if (gas) {
            partialFluidBox.min.y = 13.9375 / 16.0;
        } else {
            partialFluidBox.max.y = 2.0625 / 16.0;
        }

        renderState.setFluidColour(locked);
        ResourceLocation fluidStill = fluid.getStill(locked);
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

    @Inject(method = "addInformation",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;",
                     remap = true),
            require = 1,
            remap = false,
            cancellable = true)
    private void addProperInfo(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced,
                               CallbackInfo ci) {
        ci.cancel();

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return;

        boolean locked = tag.hasKey("LockedFluid", Constants.NBT.TAG_COMPOUND);

        FluidStack fluidStack = null;
        if (tag.hasKey(FLUID_NBT_KEY, Constants.NBT.TAG_COMPOUND)) {
            fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(FLUID_NBT_KEY));
        }

        // Fallback to locked
        if (fluidStack == null && locked) {
            fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("LockedFluid"));
            if (fluidStack != null)
                fluidStack.amount = 0;
        }

        if (fluidStack != null) {
            tooltip.add(I18n.format("gregtech.universal.tooltip.fluid_stored", fluidStack.getLocalizedName(),
                    fluidStack.amount));
        }

        if (locked) {
            tooltip.add(I18n.format("nomilabs.top.properties.locked"));
        }

        if (tag.getBoolean("IsVoiding") || tag.getBoolean("IsPartialVoiding")) { // legacy save support
            tooltip.add(I18n.format("nomilabs.top.properties.voiding"));
        }
    }
}
