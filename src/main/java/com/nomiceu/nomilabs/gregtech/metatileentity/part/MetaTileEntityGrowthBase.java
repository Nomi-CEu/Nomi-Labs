package com.nomiceu.nomilabs.gregtech.metatileentity.part;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import com.nomiceu.nomilabs.gregtech.metatileentity.multiblock.MetaTileEntityBaseChamber;
import gregtech.api.GTValues;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
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

    public MetaTileEntityGrowthBase(ResourceLocation metaTileEntityId, int tier, ICubeRenderer renderer) {
        super(metaTileEntityId, tier);
        this.tier = tier;
        this.renderer = renderer;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityGrowthBase(metaTileEntityId, tier, renderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        renderer.render(renderState, translation, pipeline);
    }

    @Override
    public void update() {
        if (getWorld() != null && getWorld().isRemote && getController() instanceof MetaTileEntityBaseChamber controller &&
                controller.isActive() && GTValues.RNG.nextFloat() > 0.8) {
            spawnEffect(controller.getTopAbsolute(), EnumParticleTypes.VILLAGER_HAPPY);
        }
    }

    @SideOnly(Side.CLIENT)
    public void spawnEffect(EnumFacing facing, @NotNull EnumParticleTypes particle) {
        if (getPos() == null) return;

        BlockPos pos = getPos();
        float xPos = facing.getXOffset() * 0.76F + pos.getX() + 0.25F;
        float yPos = facing.getYOffset() * 0.76F + pos.getY() + 0.25F;
        float zPos = facing.getZOffset() * 0.76F + pos.getZ() + 0.25F;

        float ySpd = facing.getYOffset() * 0.1F + 0.2F + 0.1F * GTValues.RNG.nextFloat();
        float xSpd;
        float zSpd;

        if (facing.getYOffset() == -1) {
            float temp = GTValues.RNG.nextFloat() * 2 * (float) Math.PI;
            xSpd = (float) Math.sin(temp) * 0.1F;
            zSpd = (float) Math.cos(temp) * 0.1F;
        } else {
            xSpd = facing.getXOffset() * (0.1F + 0.2F * GTValues.RNG.nextFloat());
            zSpd = facing.getZOffset() * (0.1F + 0.2F * GTValues.RNG.nextFloat());
        }

        xPos += GTValues.RNG.nextFloat() * 0.5F;
        yPos += GTValues.RNG.nextFloat() * 0.5F;
        zPos += GTValues.RNG.nextFloat() * 0.5F;

        getWorld().spawnParticle(particle, xPos, yPos, zPos, xSpd, ySpd, zSpd);
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
