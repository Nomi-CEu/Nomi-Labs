package com.nomiceu.nomilabs.mixin.architecturecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.elytradev.architecture.common.shape.Shape;
import com.elytradev.architecture.common.shape.ShapePage;
import com.elytradev.architecture.common.tile.TileSawbench;
import com.nomiceu.nomilabs.integration.architecturecraft.LabsShapes;

@Mixin(value = TileSawbench.class, remap = false)
public class TileSawbenchMixin {

    /**
     * Adds the new Shapes to the pages.
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void changePages(CallbackInfo ci) {
        TileSawbench.pages = new ShapePage[] {
                new ShapePage("Roofing",
                        Shape.ROOF_TILE,
                        Shape.ROOF_OUTER_CORNER,
                        Shape.ROOF_INNER_CORNER,
                        Shape.ROOF_RIDGE,
                        Shape.ROOF_SMART_RIDGE,
                        Shape.ROOF_VALLEY,
                        Shape.ROOF_SMART_VALLEY,
                        Shape.ROOF_OVERHANG,
                        Shape.ROOF_OVERHANG_OUTER_CORNER,
                        Shape.ROOF_OVERHANG_INNER_CORNER,
                        Shape.ROOF_OVERHANG_GABLE_LH,
                        Shape.ROOF_OVERHANG_GABLE_RH,
                        Shape.ROOF_OVERHANG_GABLE_END_LH,
                        Shape.ROOF_OVERHANG_GABLE_END_RH,
                        Shape.ROOF_OVERHANG_RIDGE,
                        Shape.ROOF_OVERHANG_VALLEY,
                        Shape.BEVELLED_OUTER_CORNER,
                        Shape.BEVELLED_INNER_CORNER),
                new ShapePage("Rounded",
                        Shape.CYLINDER,
                        Shape.CYLINDER_HALF,
                        Shape.CYLINDER_QUARTER,
                        Shape.CYLINDER_LARGE_QUARTER,
                        Shape.ANTICYLINDER_LARGE_QUARTER,
                        Shape.PILLAR,
                        Shape.POST,
                        Shape.POLE,
                        Shape.SPHERE_FULL,
                        Shape.SPHERE_HALF,
                        Shape.SPHERE_QUARTER,
                        Shape.SPHERE_EIGHTH,
                        Shape.SPHERE_EIGHTH_LARGE,
                        Shape.SPHERE_EIGHTH_LARGE_REV),
                new ShapePage("Classical",
                        Shape.PILLAR_BASE,
                        Shape.PILLAR,
                        Shape.DORIC_CAPITAL,
                        Shape.DORIC_TRIGLYPH,
                        Shape.DORIC_TRIGLYPH_CORNER,
                        Shape.DORIC_METOPE,
                        Shape.IONIC_CAPITAL,
                        Shape.CORINTHIAN_CAPITAL,
                        Shape.ARCHITRAVE,
                        Shape.ARCHITRAVE_CORNER,
                        Shape.CORNICE_LH,
                        Shape.CORNICE_RH,
                        Shape.CORNICE_END_LH,
                        Shape.CORNICE_END_RH,
                        Shape.CORNICE_RIDGE,
                        Shape.CORNICE_VALLEY,
                        Shape.CORNICE_BOTTOM),
                new ShapePage("Window",
                        Shape.WINDOW_FRAME,
                        Shape.WINDOW_CORNER,
                        Shape.WINDOW_MULLION),
                new ShapePage("Arches",
                        Shape.ARCH_D_1,
                        Shape.ARCH_D_2,
                        Shape.ARCH_D_3_A,
                        Shape.ARCH_D_3_B,
                        Shape.ARCH_D_3_C,
                        Shape.ARCH_D_4_A,
                        Shape.ARCH_D_4_B,
                        Shape.ARCH_D_4_C),
                new ShapePage("Railings",
                        Shape.BALUSTRADE_PLAIN,
                        Shape.BALUSTRADE_PLAIN_OUTER_CORNER,
                        Shape.BALUSTRADE_PLAIN_INNER_CORNER,
                        Shape.BALUSTRADE_PLAIN_WITH_NEWEL,
                        Shape.BALUSTRADE_PLAIN_END,
                        Shape.BANISTER_PLAIN_TOP,
                        Shape.BANISTER_PLAIN,
                        Shape.BANISTER_PLAIN_BOTTOM,
                        Shape.BANISTER_PLAIN_END,
                        Shape.BANISTER_PLAIN_INNER_CORNER,
                        Shape.BALUSTRADE_FANCY,
                        Shape.BALUSTRADE_FANCY_CORNER,
                        Shape.BALUSTRADE_FANCY_WITH_NEWEL,
                        Shape.BALUSTRADE_FANCY_NEWEL,
                        Shape.BANISTER_FANCY_TOP,
                        Shape.BANISTER_FANCY,
                        Shape.BANISTER_FANCY_BOTTOM,
                        Shape.BANISTER_FANCY_END,
                        Shape.BANISTER_FANCY_NEWEL_TALL),
                new ShapePage("Other",
                        Shape.CLADDING_SHEET,
                        Shape.SLAB,
                        Shape.STAIRS,
                        Shape.STAIRS_OUTER_CORNER,
                        Shape.STAIRS_INNER_CORNER,
                        LabsShapes.SLOPE_TILE_A1,
                        LabsShapes.SLOPE_TILE_A2,
                        LabsShapes.SLOPE_TILE_B1,
                        LabsShapes.SLOPE_TILE_B2,
                        LabsShapes.SLOPE_TILE_B3,
                        LabsShapes.SLOPE_TILE_C1,
                        LabsShapes.SLOPE_TILE_C2,
                        LabsShapes.SLOPE_TILE_C3,
                        LabsShapes.SLOPE_TILE_C4) };
    }
}
