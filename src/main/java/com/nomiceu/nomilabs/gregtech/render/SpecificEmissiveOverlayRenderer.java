package com.nomiceu.nomilabs.gregtech.render;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.nomiceu.nomilabs.NomiLabs;
import gregtech.client.renderer.cclop.LightMapOperation;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import gregtech.client.utils.BloomEffectUtil;
import gregtech.common.ConfigHolder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Just {@link gregtech.client.renderer.texture.cube.SimpleOverlayRenderer} but has a specified path for the emissive texture, and emissive is only applied when active.
 * <p>
 * Used by the Fusion MK III Overlay.
 */
public class SpecificEmissiveOverlayRenderer extends SimpleOverlayRenderer {
    private final String basePath;
    private final String emissivePath;

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite sprite;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite spriteEmissive;

    public SpecificEmissiveOverlayRenderer(String basePath, String emissivePath) {
        super(basePath);
        this.basePath = basePath;
        this.emissivePath = emissivePath;
    }

    /**
     * Overrided to correctly set spriteEmissive.
     * Otherwise, unchanged.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap textureMap) {
        String baseModID = "gregtech";
        String emissiveModID = "gregtech";
        String basePath = this.basePath;
        String emissivePath = this.emissivePath;
        String[] baseSplit = this.basePath.split(":");
        if (baseSplit.length == 2) {
            baseModID = baseSplit[0];
            basePath = baseSplit[1];
        }

        String[] emissiveSplit = this.emissivePath.split(":");
        if (emissiveSplit.length == 2) {
            emissiveModID = emissiveSplit[0];
            emissivePath = emissiveSplit[1];
        }

        NomiLabs.LOGGER.info("BASE PATH: {}, EMISSIVE PATH: {}", new ResourceLocation(baseModID, "blocks/" + basePath), new ResourceLocation(emissiveModID, "blocks/" + emissivePath));

        sprite = textureMap.registerSprite(new ResourceLocation(baseModID, "blocks/" + basePath));
        spriteEmissive = textureMap.registerSprite(new ResourceLocation(emissiveModID, "blocks/" + emissivePath));
    }

    /**
     * Overrided as we cannot set sprite and spriteEmissive correctly, as they are private.
     * Also makes it so that emissive is only applied if active.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void renderOrientedState(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 bounds, EnumFacing frontFacing, boolean isActive, boolean isWorkingEnabled) {
        Textures.renderFace(renderState, translation, pipeline, frontFacing, bounds, sprite, BlockRenderLayer.CUTOUT_MIPPED);
        if (isActive) {
            if (ConfigHolder.client.machinesEmissiveTextures) {
                IVertexOperation[] lightPipeline = ArrayUtils.add(pipeline, new LightMapOperation(240, 240));
                Textures.renderFace(renderState, translation, lightPipeline, frontFacing, bounds, spriteEmissive, BloomEffectUtil.getEffectiveBloomLayer());
            } else {
                Textures.renderFace(renderState, translation, pipeline, frontFacing, bounds, spriteEmissive, BlockRenderLayer.CUTOUT_MIPPED);
            }
        }
    }

    /**
     * Overrided as we cannot set sprite and spriteEmissive correctly, as they are private.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getParticleSprite() {
        return sprite;
    }
}
