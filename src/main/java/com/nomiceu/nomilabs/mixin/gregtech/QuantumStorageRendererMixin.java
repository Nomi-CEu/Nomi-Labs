package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.gregtech.mixinhelper.LockableQuantumStorage;
import com.nomiceu.nomilabs.util.LabsNames;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.custom.QuantumStorageRenderer;

/**
 * Renders a red glass 'screen' if the chest/tank is locked.
 */
@Mixin(value = QuantumStorageRenderer.class, remap = false)
public class QuantumStorageRendererMixin {

    @SideOnly(Side.CLIENT)
    @Unique
    private TextureAtlasSprite labs$lockedTexture;

    @Inject(method = "registerIcons", at = @At("TAIL"))
    private void registerLockedIcon(TextureMap textureMap, CallbackInfo ci) {
        labs$lockedTexture = textureMap.registerSprite(LabsNames.makeLabsName("blocks/overlay/overlay_screen_locked"));
    }

    @WrapOperation(method = "renderMachine",
                   at = @At(value = "INVOKE",
                            target = "Lgregtech/client/renderer/texture/Textures;renderFace(Lcodechicken/lib/render/CCRenderState;Lcodechicken/lib/vec/Matrix4;[Lcodechicken/lib/render/pipeline/IVertexOperation;Lnet/minecraft/util/EnumFacing;Lcodechicken/lib/vec/Cuboid6;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/util/BlockRenderLayer;)V",
                            ordinal = 0))
    private <T extends MetaTileEntity & ITieredMetaTileEntity> void renderLocked(CCRenderState renderState,
                                                                                 Matrix4 translation,
                                                                                 IVertexOperation[] ops,
                                                                                 EnumFacing face,
                                                                                 Cuboid6 bounds,
                                                                                 TextureAtlasSprite sprite,
                                                                                 BlockRenderLayer layer,
                                                                                 Operation<Void> original,
                                                                                 @Local(argsOnly = true) T mte) {
        if (mte instanceof LockableQuantumStorage lock) {
            if (lock.labs$isLocked()) {
                Textures.renderFace(renderState, translation, ops, face, bounds, labs$lockedTexture, layer);
                return;
            }
        }

        original.call(renderState, translation, ops, face, bounds, sprite, layer);
    }
}
