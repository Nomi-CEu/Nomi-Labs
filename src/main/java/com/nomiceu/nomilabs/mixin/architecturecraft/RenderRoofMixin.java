package com.nomiceu.nomilabs.mixin.architecturecraft;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.elytradev.architecture.client.render.shape.RenderRoof;
import com.elytradev.architecture.client.render.shape.RenderShape;
import com.elytradev.architecture.client.render.target.RenderTargetBase;
import com.elytradev.architecture.client.render.texture.ITexture;
import com.elytradev.architecture.common.helpers.Trans3;
import com.elytradev.architecture.common.tile.TileShape;
import com.nomiceu.nomilabs.integration.architecturecraft.LabsShapes;

@Mixin(value = RenderRoof.class, remap = false)
public abstract class RenderRoofMixin extends RenderShape {

    @Unique
    private Map<Integer, Runnable> ID_TO_RENDER_MAP = null;

    @Shadow
    protected abstract void bottomQuad();

    @Shadow
    protected abstract void backQuad();

    @Shadow
    protected abstract void beginNegZSlope();

    @Shadow
    protected abstract void beginQuad();

    @Shadow
    protected abstract void vertex(double x, double y, double z, double u, double v);

    @Shadow
    protected abstract void endFace();

    @Shadow
    protected abstract void beginPosXFace();

    @Shadow
    protected abstract void beginTriangle();

    @Shadow
    protected abstract void beginNegZFace();

    @Shadow
    protected abstract void beginPosZFace();

    @Shadow
    protected abstract void beginNegXFace();

    /**
     * Default Ignored Constructor
     */
    private RenderRoofMixin(TileShape te, ITexture[] textures, Trans3 t, RenderTargetBase target) {
        super(te, textures, t, target);
    }

    @Unique
    private Map<Integer, Runnable> labs$getRenderMap() {
        if (ID_TO_RENDER_MAP != null) return ID_TO_RENDER_MAP;

        ID_TO_RENDER_MAP = new HashMap<>();
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_A1.id, this::labs$renderSlopeA1);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_A2.id, this::labs$renderSlopeA2);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_B1.id, this::labs$renderSlopeB1);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_B2.id, this::labs$renderSlopeB2);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_B3.id, this::labs$renderSlopeB3);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_C1.id, this::labs$renderSlopeC1);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_C2.id, this::labs$renderSlopeC2);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_C3.id, this::labs$renderSlopeC3);
        ID_TO_RENDER_MAP.put(LabsShapes.SLOPE_TILE_C4.id, this::labs$renderSlopeC4);
        return ID_TO_RENDER_MAP;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderCustomSlopes(CallbackInfo ci) {
        var map = labs$getRenderMap();
        var id = te.getShape().id;
        if (!map.containsKey(id)) return;
        map.get(id).run();
        ci.cancel();
    }

    // -------------------------------------------------------------------------------------
    @Unique
    protected void labs$renderSlopeA1() {
        labs$renderVariableSlope(1.0, 0.5);

        labs$renderVariableFaceLeft(0, 0.5);
        labs$renderVariableTriangleLeft(0.5, 0.5);

        labs$renderVariableFaceRight(0, 0.5);
        labs$renderVariableTriangleRight(0.5, 0.5);

        labs$renderVariableFrontFace(0.5);

        bottomQuad();
        backQuad();
    }

    @Unique
    protected void labs$renderSlopeA2() {
        labs$renderVariableSlope(0.5, 0);

        labs$renderVariableTriangleLeft(0, 0.5);

        labs$renderVariableTriangleRight(0, 0.5);

        bottomQuad();
        labs$renderVariableBackFace(0.5);
    }

    @Unique
    protected void labs$renderSlopeB1() {
        labs$renderVariableSlope(1.0, 0.66666);

        labs$renderVariableFaceLeft(0, 0.66666);
        labs$renderVariableTriangleLeft(0.66666, 0.33333);

        labs$renderVariableFaceRight(0, 0.66666);
        labs$renderVariableTriangleRight(0.66666, 0.33333);

        labs$renderVariableFrontFace(0.66666);

        bottomQuad();
        backQuad();
    }

    @Unique
    protected void labs$renderSlopeB2() {
        labs$renderVariableSlope(0.66666, 0.33333);

        labs$renderVariableFaceLeft(0, 0.33333);
        labs$renderVariableTriangleLeft(0.33333, 0.33333);

        labs$renderVariableFaceRight(0, 0.33333);
        labs$renderVariableTriangleRight(0.33333, 0.33333);

        labs$renderVariableFrontFace(0.33333);

        bottomQuad();
        labs$renderVariableBackFace(0.66666);
    }

    @Unique
    protected void labs$renderSlopeB3() {
        labs$renderVariableSlope(0.33333, 0);

        labs$renderVariableTriangleLeft(0, 0.33333);

        labs$renderVariableTriangleRight(0, 0.33333);

        bottomQuad();
        labs$renderVariableBackFace(0.33333);
    }

    @Unique
    protected void labs$renderSlopeC1() {
        labs$renderVariableSlope(1, 0.75);

        labs$renderVariableFaceLeft(0, 0.75);
        labs$renderVariableTriangleLeft(0.75, 0.25);

        labs$renderVariableFaceRight(0, 0.75);
        labs$renderVariableTriangleRight(0.75, 0.25);

        labs$renderVariableFrontFace(0.75);

        bottomQuad();
        backQuad();
    }

    @Unique
    protected void labs$renderSlopeC2() {
        labs$renderVariableSlope(0.75, 0.50);

        labs$renderVariableFaceLeft(0, 0.50);
        labs$renderVariableTriangleLeft(0.50, 0.25);

        labs$renderVariableFaceRight(0, 0.50);
        labs$renderVariableTriangleRight(0.50, 0.25);

        labs$renderVariableFrontFace(0.50);

        bottomQuad();
        labs$renderVariableBackFace(0.75);
    }

    @Unique
    protected void labs$renderSlopeC3() {
        labs$renderVariableSlope(0.50, 0.25);

        labs$renderVariableFaceLeft(0, 0.25);
        labs$renderVariableTriangleLeft(0.25, 0.25);

        labs$renderVariableFaceRight(0, 0.25);
        labs$renderVariableTriangleRight(0.25, 0.25);

        labs$renderVariableFrontFace(0.25);

        bottomQuad();
        labs$renderVariableBackFace(0.50);
    }

    @Unique
    protected void labs$renderSlopeC4() {
        labs$renderVariableSlope(0.25, 0);

        labs$renderVariableTriangleLeft(0, 0.25);

        labs$renderVariableTriangleRight(0, 0.25);

        bottomQuad();
        labs$renderVariableBackFace(0.25);
    }

    // -------------------------------------------------------------------------------------

    @Unique
    protected void labs$renderVariableSlope(double start, double end) {
        beginNegZSlope();
        // Front slope
        beginQuad();
        vertex(1, start, 1, 0, 0);
        vertex(1, end, 0, 0, 1);
        vertex(0, end, 0, 1, 1);
        vertex(0, start, 1, 1, 0);
        endFace();
    }

    @Unique
    protected void labs$renderVariableTriangleLeft(double offset, double height) {
        beginPosXFace();
        beginTriangle();
        vertex(1, offset + height, 1, 0, 0);
        vertex(1, offset, 1, 0, 1 - height);
        vertex(1, offset, 0, 1, 1 - height);
        endFace();
    }

    @Unique
    protected void labs$renderVariableTriangleRight(double offset, double height) {
        beginNegXFace();
        beginTriangle();
        vertex(0, offset + height, 1, 1, 0);
        vertex(0, offset, 0, 0, 1 - height);
        vertex(0, offset, 1, 1, 1 - height);
        endFace();
    }

    @SuppressWarnings("SameParameterValue")
    @Unique
    protected void labs$renderVariableFaceLeft(double offset, double height) {
        beginNegXFace();
        beginQuad();
        vertex(0, offset + height, 0, 0, 1 - height);
        vertex(0, offset, 0, 0, 1);
        vertex(0, offset, 1, 1, 1);
        vertex(0, offset + height, 1, 1, 1 - height);
        endFace();
    }

    @SuppressWarnings("SameParameterValue")
    @Unique
    protected void labs$renderVariableFaceRight(double offset, double height) {
        beginPosXFace();
        beginQuad();
        vertex(1, offset + height, 1, 0, 1 - height);
        vertex(1, offset, 1, 0, 1);
        vertex(1, offset, 0, 1, 1);
        vertex(1, offset + height, 0, 1, 1 - height);
        endFace();
    }

    @Unique
    protected void labs$renderVariableFrontFace(double height) {
        beginNegZFace();
        beginQuad();
        vertex(1, height, 0, 0, 1 - height);
        vertex(1, 0, 0, 0, 1);
        vertex(0, 0, 0, 1, 1);
        vertex(0, height, 0, 1, 1 - height);
        endFace();
    }

    @Unique
    protected void labs$renderVariableBackFace(double height) {
        beginPosZFace();
        beginQuad();
        vertex(0, height, 1, 0, 1 - height);
        vertex(0, 0, 1, 0, 1);
        vertex(1, 0, 1, 1, 1);
        vertex(1, height, 1, 1, 1 - height);
        endFace();
    }
}
