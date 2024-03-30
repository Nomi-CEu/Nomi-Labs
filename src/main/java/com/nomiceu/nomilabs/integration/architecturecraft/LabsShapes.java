package com.nomiceu.nomilabs.integration.architecturecraft;

import com.elytradev.architecture.common.shape.EnumShape;
import com.elytradev.architecture.common.shape.ShapeKind;
import com.elytradev.architecture.common.shape.EnumShapeSymmetry;
import com.nomiceu.nomilabs.mixin.architecturecraft.ShapeAccessor;
import net.minecraftforge.common.util.EnumHelper;

public class LabsShapes {
    public static final EnumShape SLOPE_TILE_A1 = addShape("SLOPE_TILE_A1", 94, "Slope A Start", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 1, 0xcf);
    public static final EnumShape SLOPE_TILE_A2 = addShape("SLOPE_TILE_A2", 95, "Slope A End", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 3, 0x0f);
    public static final EnumShape SLOPE_TILE_B1 = addShape("SLOPE_TILE_B1", 96, "Slope B Start", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 1, 0xff);
    public static final EnumShape SLOPE_TILE_B2 = addShape("SLOPE_TILE_B2", 97, "Slope B Middle", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 2, 0xcf);
    public static final EnumShape SLOPE_TILE_B3 = addShape("SLOPE_TILE_B3", 98, "Slope B End", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 3, 0x0f);
    public static final EnumShape SLOPE_TILE_C1 = addShape("SLOPE_TILE_C1", 99, "Slope C 1", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 1, 0xff);
    public static final EnumShape SLOPE_TILE_C2 = addShape("SLOPE_TILE_C2", 100, "Slope C 2", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 2, 0xcf);
    public static final EnumShape SLOPE_TILE_C3 = addShape("SLOPE_TILE_C3", 101, "Slope C 3", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 3, 0x0f);
    public static final EnumShape SLOPE_TILE_C4 = addShape("SLOPE_TILE_C4", 102, "Slope C 4", ShapeKind.Roof, EnumShapeSymmetry.BILATERAL, 1, 4, 0x0f);

    /**
     * Essentially, this loads the class, allowing the above values to be added.
     * <p>
     * If for some reason, the values are needed before this, they will still be loaded, and calling init will have no affect on that.
     */
    public static void preInit() {

    }

    @SuppressWarnings("SameParameterValue")
    private static EnumShape addShape(String name, int id, String title, ShapeKind kind, EnumShapeSymmetry sym, int used, int made, int occ) {
        var shape = EnumHelper.addEnum(EnumShape.class, name,
                new Class<?>[]{int.class, String.class, ShapeKind.class, EnumShapeSymmetry.class, int.class, int.class, int.class},
                id, title, kind, sym, used, made, occ);
        ShapeAccessor.getIDMap().put(id, shape) ;
        return shape;
    }
}
