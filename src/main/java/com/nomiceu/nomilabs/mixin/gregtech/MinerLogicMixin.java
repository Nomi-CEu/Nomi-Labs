package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import gregtech.api.capability.impl.miner.MinerLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;

/**
 * Fixes Multiblock Miners not being able to use ME Output Buses.
 */
@Mixin(value = MinerLogic.class, remap = false)
public class MinerLogicMixin {

    @Shadow
    @Final
    protected MetaTileEntity metaTileEntity;

    @WrapOperation(method = "mineAndInsertItems",
                   at = @At(value = "INVOKE",
                            target = "Lgregtech/api/util/GTTransferUtils;addItemsToItemHandler(Lnet/minecraftforge/items/IItemHandler;ZLjava/util/List;)Z",
                            ordinal = 0),
                   require = 1)
    private boolean checkForInfiniteOutput(IItemHandler amountToInsert, boolean amount, List<ItemStack> entry,
                                           Operation<Boolean> original) {
        // Whilst Miner does not support Voiding, ME Output Hatch Presence also returns true
        if (metaTileEntity instanceof MultiblockWithDisplayBase multi && multi.canVoidRecipeItemOutputs()) {
            return true;
        }
        return original.call(amountToInsert, amount, entry);
    }
}
