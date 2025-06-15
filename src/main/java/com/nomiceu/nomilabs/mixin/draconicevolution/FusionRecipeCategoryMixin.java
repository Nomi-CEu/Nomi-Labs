package com.nomiceu.nomilabs.mixin.draconicevolution;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.brandon3055.brandonscore.items.ItemEnergyBase;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.integration.jei.FusionRecipeCategory;
import com.brandon3055.draconicevolution.items.ToolUpgrade;
import com.brandon3055.draconicevolution.items.armor.WyvernArmor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

/**
 * Fixes energy bars exceeding maximum size in JEI, for RF Upgrade recipes.
 */
@Mixin(value = FusionRecipeCategory.class, remap = false)
public class FusionRecipeCategoryMixin {

    @WrapOperation(method = "setRecipe",
                   at = @At(value = "INVOKE",
                            target = "Lcom/brandon3055/draconicevolution/api/itemupgrade/UpgradeHelper;setUpgradeLevel(Lnet/minecraft/item/ItemStack;Ljava/lang/String;I)V",
                            ordinal = 0),
                   require = 1)
    private void checkEnergyAmtInput(ItemStack stack, String upgrade, int level, Operation<Void> original,
                                     @Share("energyCap") LocalIntRef energyCap) {
        original.call(stack, upgrade, level);
        if (!upgrade.equals(ToolUpgrade.RF_CAPACITY))
            return;

        if (stack.getItem() instanceof ItemEnergyBase base) {
            energyCap.set(base.getCapacity(stack));
            base.setEnergy(stack, energyCap.get());
        } else if (stack.getItem() instanceof WyvernArmor armor) {
            energyCap.set(armor.getMaxEnergyStored(stack));
            ItemNBTHelper.setInteger(stack, "Energy", energyCap.get());
        }
    }

    @WrapOperation(method = "setRecipe",
                   at = @At(value = "INVOKE",
                            target = "Lcom/brandon3055/draconicevolution/api/itemupgrade/UpgradeHelper;setUpgradeLevel(Lnet/minecraft/item/ItemStack;Ljava/lang/String;I)V",
                            ordinal = 1),
                   require = 1)
    private void checkEnergyAmtOutput(ItemStack stack, String upgrade, int level, Operation<Void> original,
                                      @Share("energyCap") LocalIntRef energyCap) {
        original.call(stack, upgrade, level);
        if (!upgrade.equals(ToolUpgrade.RF_CAPACITY))
            return;

        if (stack.getItem() instanceof ItemEnergyBase base) {
            base.setEnergy(stack, energyCap.get());
        } else if (stack.getItem() instanceof WyvernArmor) {
            ItemNBTHelper.setInteger(stack, "Energy", energyCap.get());
        }
    }
}
