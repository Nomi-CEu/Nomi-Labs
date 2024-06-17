package com.nomiceu.nomilabs.gregtech.metatileentity.part;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import com.nomiceu.nomilabs.gregtech.metatileentity.multiblock.MetaTileEntityBaseChamber;
import com.nomiceu.nomilabs.util.LabsParticle;
import gregtech.api.GTValues;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.cube.SimpleOrientedCubeRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import gregtech.client.renderer.texture.custom.FireboxActiveRenderer;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class MetaTileEntityGrowthBase extends MetaTileEntityMultiblockPart {

    private final int tier;
    private final ICubeRenderer renderer;
    private final float particleBuffer;

    public MetaTileEntityGrowthBase(ResourceLocation metaTileEntityId, int tier, ICubeRenderer renderer, float particleBuffer) {
        super(metaTileEntityId, tier);
        this.tier = tier;
        this.renderer = renderer;
        this.particleBuffer = particleBuffer;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityGrowthBase(metaTileEntityId, tier, renderer, particleBuffer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        var direction = (getController() instanceof MetaTileEntityBaseChamber chamber) ? chamber.getTopAbsolute().getOpposite() : null;
        for (var facing : EnumFacing.VALUES) {
            if (facing != direction) renderer.renderSided(facing, renderState, translation, pipeline);
        }
    }

    @Override
    public void update() {
        if (getWorld() != null && getWorld().isRemote && getController() instanceof MetaTileEntityBaseChamber controller &&
                controller.isActive() && GTValues.RNG.nextFloat() > particleBuffer) {
            LabsParticle.spawnEffect(this, controller.getTopAbsolute(), EnumParticleTypes.VILLAGER_HAPPY);
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

    @Override
    public boolean hasFrontFacing() {
        return false;
    }
}
