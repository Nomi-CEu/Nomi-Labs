package com.nomiceu.nomilabs.gregtech.metatileentity.multiblock;

import net.minecraft.util.EnumFacing;

import org.jetbrains.annotations.NotNull;

import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;

public class ProperlySidedOverlayRenderer extends OrientedOverlayRenderer {

    public ProperlySidedOverlayRenderer(@NotNull String basePath) {
        super(basePath);
    }

    @Override
    public Rotation getRotation(Matrix4 transformation, EnumFacing renderSide, EnumFacing frontFacing) {
        if (renderSide.getAxis() == EnumFacing.Axis.Y)
            return super.getRotation(transformation, renderSide, frontFacing);
        if (renderSide.getAxis() == EnumFacing.Axis.X) {
            if (frontFacing == EnumFacing.DOWN) {
                transformation.translate(0, 1, 1);
                return new Rotation(Math.PI, 1, 0, 0);
            }
            if (frontFacing == EnumFacing.SOUTH) {
                transformation.translate(0, 1, 0);
                return new Rotation(Math.PI / 2, 1, 0, 0);
            }
            if (frontFacing == EnumFacing.NORTH) {
                transformation.translate(0, 0, 1);
                return new Rotation(-Math.PI / 2, 1, 0, 0);
            }
            return new Rotation(0, 1, 0, 0);
        }
        if (frontFacing == EnumFacing.DOWN) {
            transformation.translate(1, 1, 0);
            return new Rotation(Math.PI, 0, 0, 1);
        }
        if (frontFacing == EnumFacing.WEST) {
            transformation.translate(1, 0, 0);
            return new Rotation(Math.PI / 2, 0, 0, 1);
        }
        if (frontFacing == EnumFacing.EAST) {
            transformation.translate(0, 1, 0);
            return new Rotation(-Math.PI / 2, 0, 0, 1);
        }
        return new Rotation(0, 0, 0, 1);
    }
}
