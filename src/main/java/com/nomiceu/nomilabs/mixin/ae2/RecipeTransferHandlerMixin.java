package com.nomiceu.nomilabs.mixin.ae2;

import java.util.Map;
import java.util.Set;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleGTRecipeWrapper;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;
import com.nomiceu.nomilabs.integration.ae2.LabsAE2ImportHandler;
import com.nomiceu.nomilabs.mixin.jei.RecipeLayoutAccessor;

import appeng.container.implementations.ContainerPatternEncoder;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.recipeproperties.ResearchProperty;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;

/**
 * Allows skipping GT non-consumables. Allows prioritizing items.
 */
@Mixin(targets = "appeng.integration.modules.jei.RecipeTransferHandler", remap = false)
public class RecipeTransferHandlerMixin<T extends Container> {

    @WrapOperation(method = "transferRecipe",
                   at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"),
                   require = 1)
    private Set<Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>>> skipGTNonConsumables(Map<Integer, ? extends IGuiIngredient<ItemStack>> instance,
                                                                                              Operation<Set<Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>>>> original,
                                                                                              @Local(argsOnly = true) T container,
                                                                                              @Local(argsOnly = true) IRecipeLayout layout) {
        var ing = original.call(instance);

        if (!(container instanceof ContainerPatternEncoder encoder)) return ing;

        if (!(layout instanceof RecipeLayoutAccessor accessor)) return ing;
        if (!(accessor.labs$getRecipeWrapper() instanceof GTRecipeWrapper gt)) return ing;

        // Always skip first item (scanner item) in assembly line recipes
        boolean assemblySkip = ((AccessibleGTRecipeWrapper) gt).labs$getRecipeMap() ==
                RecipeMaps.ASSEMBLY_LINE_RECIPES &&
                gt.getRecipe().getProperty(ResearchProperty.getInstance(), null) != null;
        boolean skipNonConsume = !((InclNonConsumeSettable) encoder).labs$inclNonConsume();

        if (!assemblySkip && !skipNonConsume) return ing;

        // Use Object Array Set to preserve insertion order
        Set<Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>>> result = new ObjectArraySet<>();

        // Appropriate filtering
        for (var obj : ing) {
            if (obj.getValue().isInput()) {
                // Skip first item for assembly line; but may not be the first slot id
                if (assemblySkip && obj.getKey() == LabsAE2ImportHandler.ASSEMBLY_LINE_RESEARCH_SLOT)
                    continue;
                if (skipNonConsume && gt.isNotConsumedItem(obj.getKey()))
                    continue;
            }
            result.add(obj);
        }
        return result;
    }

    @WrapOperation(method = "transferRecipe",
                   at = @At(value = "INVOKE",
                            target = "Lappeng/util/Platform;isRecipePrioritized(Lnet/minecraft/item/ItemStack;)Z"),
                   require = 1)
    private boolean checkConfigPriority(ItemStack what, Operation<Boolean> original) {
        return original.call(what) || LabsAE2ImportHandler.isItemStackPrioritized(what);
    }
}
