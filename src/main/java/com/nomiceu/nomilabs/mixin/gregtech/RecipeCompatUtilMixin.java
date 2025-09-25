package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.GroovyHandFixHelper;

import gregtech.api.GTValues;
import gregtech.api.block.machines.MachineItemBlock;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.pipenet.block.material.BlockMaterialPipe;
import gregtech.api.unification.material.Material;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockCompressed;
import gregtech.common.blocks.BlockFrame;
import gregtech.integration.RecipeCompatUtil;

/**
 * Applies <a href="https://github.com/GregTechCEu/GregTech/pull/2785">#2785</a>.
 */
@Mixin(value = RecipeCompatUtil.class, remap = false)
public final class RecipeCompatUtilMixin {

    /**
     * Full replacement of the method; too complex & widespread to be done with redirects.
     */
    @Inject(method = "getMetaItemId", at = @At("HEAD"), cancellable = true)
    private static void newMetaItemIdLogic(ItemStack item, CallbackInfoReturnable<String> cir) {
        if (item.getItem() instanceof MetaItem<?>metaItem) {
            MetaItem<?>.MetaValueItem metaValueItem = metaItem.getItem(item);
            if (metaValueItem != null) {
                String nameSpace = Objects.requireNonNull(metaValueItem.getMetaItem().getRegistryName()).getNamespace();
                String name = metaValueItem.unlocalizedName;
                cir.setReturnValue(nameSpace.equals(GTValues.MODID) ? name : (nameSpace + ":" + name));
                return;
            }
        }

        if (!(item.getItem() instanceof ItemBlock itemBlock)) {
            cir.setReturnValue(null);
            return;
        }

        Block block = itemBlock.getBlock();
        if (item.getItem() instanceof MachineItemBlock) {
            MetaTileEntity mte = GTUtility.getMetaTileEntity(item);
            if (mte != null) {
                cir.setReturnValue(mte.metaTileEntityId.getNamespace().equals(GTValues.MODID) ?
                        mte.metaTileEntityId.getPath() : mte.metaTileEntityId.toString());
                return;
            }
        }
        if (block instanceof BlockCompressed blockCompressed) {
            Material material = blockCompressed.getGtMaterial(item);
            cir.setReturnValue(GroovyHandFixHelper.getRlPrefix(material) + "block" + material.toCamelCaseString());
            return;
        }
        if (block instanceof BlockFrame blockFrame) {
            Material material = blockFrame.getGtMaterial(item);
            cir.setReturnValue(GroovyHandFixHelper.getRlPrefix(material) + "frame" + material.toCamelCaseString());
            return;
        }
        if (block instanceof BlockMaterialPipe<?, ?, ?>blockMaterialPipe) {
            Material material = blockMaterialPipe.getItemMaterial(item);
            cir.setReturnValue(
                    GroovyHandFixHelper.getRlPrefix(material) + blockMaterialPipe.getPrefix().name +
                            material.toCamelCaseString());
            return;
        }

        cir.setReturnValue(null);
    }
}
