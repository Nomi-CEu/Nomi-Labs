package com.nomiceu.nomilabs.mixin.advancedrocketry.prerework;

import static org.lwjgl.opengl.GL11.GL_GREATER;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import zmaster587.advancedRocketry.api.dimension.solar.StellarBody;
import zmaster587.advancedRocketry.client.render.planet.RenderPlanetarySky;
import zmaster587.advancedRocketry.dimension.DimensionProperties;
import zmaster587.advancedRocketry.util.AstronomicalBodyHelper;

/**
 * Fixes Moons' and Parent Planets' Sizes in Skyboxes. Uses the new algorithms from newer AdvRocketry.
 */
@Mixin(value = RenderPlanetarySky.class, remap = false)
public abstract class RenderPlanetarySkyMixin {

    @Shadow
    protected abstract void renderPlanet2(BufferBuilder buffer, DimensionProperties properties, float size,
                                          float alphaMultiplier, double shadowAngle, boolean hasRing);

    @Redirect(method = "render",
              at = @At(value = "INVOKE",
                       target = "Lzmaster587/advancedRocketry/client/render/planet/RenderPlanetarySky;renderPlanet2(Lnet/minecraft/client/renderer/BufferBuilder;Lzmaster587/advancedRocketry/dimension/DimensionProperties;FFDZ)V"),
              require = 1)
    private void useProperParent(RenderPlanetarySky instance, BufferBuilder buffer, DimensionProperties properties,
                                 float size, float alphaMultiplier, double shadowAngle, boolean hasRing) {
        // The 'size' here is the body size multiplier from orbiting distance
        renderPlanet2(buffer, properties, 20f * size * (float) Math.pow(properties.getGravitationalMultiplier(), 0.4),
                alphaMultiplier, shadowAngle, hasRing);
    }

    @Redirect(method = "render",
              at = @At(value = "INVOKE",
                       target = "Lzmaster587/advancedRocketry/client/render/planet/RenderPlanetarySky;renderPlanet(Lnet/minecraft/client/renderer/BufferBuilder;Lzmaster587/advancedRocketry/dimension/DimensionProperties;FFDZZ)V"),
              require = 1)
    private void useProperMoon(RenderPlanetarySky instance, BufferBuilder buffer, DimensionProperties properties,
                               float planetOrbitalDistance, float alphaMultiplier, double shadowAngle,
                               boolean hasAtmosphere, boolean hasRing) {
        // The 'planetOrbitalDistance' is irrelevant here
        renderPlanet2(buffer, properties,
                20f * AstronomicalBodyHelper.getBodySizeMultiplier(properties.getParentOrbitalDistance()) *
                        (float) Math.pow(properties.getGravitationalMultiplier(), 0.4),
                alphaMultiplier, shadowAngle, hasRing);
    }

    /**
     * Fixes mipmaps of culled blocks not being rendered properly.<p>
     * Alphafunc is modified in the logic for drawing the black hole.<p>
     * Values for alphaFunc retrieved from
     * {@link net.minecraft.client.renderer.EntityRenderer#renderWorld(float, long)},
     * which is contains the call of alphaFunc right before sky is rendered.
     */
    @Inject(method = "drawStar",
            at = @At(value = "RETURN"))
    private void resetAlphaFunc(BufferBuilder buffer, StellarBody sun, DimensionProperties properties,
                                int solarOrbitalDistance,
                                float sunSize, Vec3d sunColor, float multiplier, CallbackInfo ci) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, 0.5F);
    }
}
