package com.nomiceu.nomilabs.integration.ae2;

import javax.vecmath.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.IModelState;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;

import appeng.items.misc.ItemEncodedPattern;
import codechicken.lib.render.item.CCRenderItem;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;

public class LabsEncodedPatternRenderer implements IItemRenderer {

    private final IBakedModel baseModel;

    public LabsEncodedPatternRenderer(IBakedModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        ItemStack stackToRender = ItemStack.EMPTY;
        IBakedModel model;

        if (LabsTooltipHelper.isShiftDown()) {
            ItemEncodedPattern iep = (ItemEncodedPattern) stack.getItem();
            stackToRender = iep.getOutput(stack);
        }

        if (!stackToRender.isEmpty())
            model = renderItem.getItemModelWithOverrides(stackToRender, null, null);
        else {
            stackToRender = stack;
            model = baseModel;
        }

        GlStateManager.pushMatrix();

        // Counteract Render Item Translation
        GlStateManager.translate(0.5F, 0.5F, 0.5F);

        // Manual Handling of isGui3d
        if (model.isGui3d() && transformType == ItemCameraTransforms.TransformType.GUI)
            GlStateManager.enableLighting();

        renderItem.renderItemModel(stackToRender, model, transformType, false);
        GlStateManager.popMatrix();
    }

    @Override
    @NotNull
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        CCRenderItem.notifyTransform(cameraTransformType);
        return Pair.of(this, null);
    }

    @Override
    public IModelState getTransforms() {
        return TransformUtils.DEFAULT_BLOCK;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }
}
