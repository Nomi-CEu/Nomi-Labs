package com.nomiceu.nomilabs.mixin.ae2;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;
import com.nomiceu.nomilabs.mixin.jei.RecipeLayoutAccessor;

import appeng.container.implementations.ContainerPatternEncoder;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;

/**
 * Allows skipping GT non-consumables.
 */
@Mixin(targets = "appeng.integration.modules.jei.RecipeTransferHandler", remap = false)
public class RecipeTransferHandlerMixin {

    @WrapOperation(method = "transferRecipe",
                   at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"),
                   require = 1)
    private <
            T extends Container> Set<Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>>> skipGTNonConsumables(Map<Integer, ? extends IGuiIngredient<ItemStack>> instance,
                                                                                                                   Operation<Set<Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>>>> original,
                                                                                                                   @Local(argsOnly = true) T container,
                                                                                                                   @Local(argsOnly = true) IRecipeLayout layout) {
        var ing = original.call(instance);

        if (!(container instanceof ContainerPatternEncoder encoder) || encoder.isCraftingMode()) return ing;
        if (((InclNonConsumeSettable) encoder).labs$inclNonConsume()) return ing;

        if (!(layout instanceof RecipeLayoutAccessor accessor)) return ing;
        if (!(accessor.labs$getRecipeWrapper() instanceof GTRecipeWrapper gt)) return ing;

        // Use Object Array Set to preserve insertion order
        return ing.stream().filter(e -> !e.getValue().isInput() || !gt.isNotConsumedItem(e.getKey()))
                .collect(Collectors.toCollection(ObjectArraySet::new));
    }
}
