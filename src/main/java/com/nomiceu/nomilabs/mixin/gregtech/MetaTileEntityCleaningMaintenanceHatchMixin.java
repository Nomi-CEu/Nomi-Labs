package com.nomiceu.nomilabs.mixin.gregtech;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.gregtech.render.SpecificEmissiveOverlayRenderer;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityCleaningMaintenanceHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Client only mixin to give info about whether the multiblock is working/active to the Cleaning Maintenance Hatch, as it completely overrides the original super function, without calling super.
 * <p>
 * Only does this with {@link com.nomiceu.nomilabs.gregtech.render.SpecificEmissiveOverlayRenderer}, allows us to make it emissive.
 * <p>
 * Currently only used for the Fusion MK III Overlay.
 * <p>
 * This may need to be updated if GT changes that class.
 */
@Mixin(value = MetaTileEntityCleaningMaintenanceHatch.class, remap = false)
public abstract class MetaTileEntityCleaningMaintenanceHatchMixin extends MetaTileEntityMultiblockPart {
    /**
     * Mandatory Ignored Constructor
     */
    public MetaTileEntityCleaningMaintenanceHatchMixin(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
    }

    @Inject(method = "renderMetaTileEntity", at = @At("HEAD"), cancellable = true)
    public void renderMetaTileEntityEmissive(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, CallbackInfo ci) {
        ICubeRenderer baseTexture = getBaseTexture();
        NomiLabs.LOGGER.info("hi");
        if (baseTexture instanceof SpecificEmissiveOverlayRenderer && getController() instanceof RecipeMapMultiblockController recipeMapController) {
            NomiLabs.LOGGER.info("hi (2)");
            pipeline = ArrayUtils.add(pipeline,
                    new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering())));
            for (var facing : EnumFacing.values())
                baseTexture.renderOrientedState(renderState, translation, pipeline, facing, recipeMapController.getRecipeMapWorkable().isActive(), recipeMapController.getRecipeMapWorkable().isWorkingEnabled());

            if (shouldRenderOverlay())
                Textures.MAINTENANCE_OVERLAY_CLEANING.renderSided(getFrontFacing(), renderState, translation, pipeline);
            ci.cancel();
        }
    }
}
