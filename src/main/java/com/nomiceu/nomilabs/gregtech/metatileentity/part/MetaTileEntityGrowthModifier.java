package com.nomiceu.nomilabs.gregtech.metatileentity.part;

import com.nomiceu.nomilabs.gregtech.metatileentity.multiblock.MetaTileEntityBaseChamber;
import com.nomiceu.nomilabs.util.LabsParticle;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.client.particle.VanillaParticleEffects;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;

import java.util.function.Function;

public class MetaTileEntityGrowthModifier extends MetaTileEntityMultiblockPart {

    private final int tier;
    private final OrientedOverlayRenderer renderer;
    private final boolean spawnParticle;
    private final float particleBuffer;
    private final Function<MetaTileEntityGrowthModifier, EnumFacing> particleDir;
    private final EnumParticleTypes particleType;

    public MetaTileEntityGrowthModifier(ResourceLocation metaTileEntityId, int tier, OrientedOverlayRenderer renderer, boolean spawnParticle, float particleBuffer, Function<MetaTileEntityGrowthModifier, EnumFacing> particleDir, EnumParticleTypes particleType) {
        super(metaTileEntityId, tier);
        this.tier = tier;
        this.renderer = renderer;
        this.spawnParticle = spawnParticle;
        this.particleBuffer = particleBuffer;
        this.particleDir = particleDir;
        this.particleType = particleType;
        frontFacing = EnumFacing.UP;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityGrowthModifier(metaTileEntityId, tier, renderer, spawnParticle, particleBuffer, particleDir, particleType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        var active = getController() != null && getController().isActive();
        renderer.renderOrientedState(renderState, translation, pipeline, getFrontFacing(), active,
                true);
    }

    @Override
    public void update() {
        if (spawnParticle && getWorld() != null && getWorld().isRemote && getController() instanceof MetaTileEntityBaseChamber controller &&
                controller.isActive() && GTValues.RNG.nextFloat() > particleBuffer) {
            LabsParticle.spawnEffect(this, particleDir.apply(this), particleType);
        }
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    protected boolean openGUIOnRightClick() {
        return false;
    }

    @Override
    public boolean canPlaceCoverOnSide(@NotNull EnumFacing side) {
        return false;
    }
}
