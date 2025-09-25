package com.nomiceu.nomilabs.mixin.ae2;

import java.util.function.Function;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.ae2.LabsEncodedPatternRenderer;

/**
 * Use our custom CCL encoded pattern renderer to fix issues with AE2's, notably chests not rendering and
 * draconic evolution parts not rendering correctly.
 */
@Mixin(targets = "appeng.client.render.crafting.ItemEncodedPatternModel", remap = false)
public class ItemEncodedPatternModelMixin {

    @Inject(method = "bake",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraftforge/client/model/PerspectiveMapWrapper;getTransforms(Lnet/minecraftforge/common/model/IModelState;)Lcom/google/common/collect/ImmutableMap;",
                     shift = At.Shift.BEFORE),
            require = 1,
            cancellable = true)
    private void returnCustomModel(IModelState state, VertexFormat format,
                                   Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter,
                                   CallbackInfoReturnable<IBakedModel> cir, @Local IBakedModel baseModel) {
        cir.setReturnValue(new LabsEncodedPatternRenderer(baseModel));
    }
}
