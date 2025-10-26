package com.nomiceu.nomilabs.mixin.ae2fc;

import static com.nomiceu.nomilabs.integration.ae2fc.RecipeTransferBuilderState.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.glodblock.github.integration.jei.RecipeTransferBuilder;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleGTRecipeWrapper;
import com.nomiceu.nomilabs.integration.ae2.LabsAE2ImportHandler;
import com.nomiceu.nomilabs.mixin.jei.RecipeLayoutAccessor;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.recipeproperties.ResearchProperty;
import gregtech.integration.jei.recipe.GTRecipeWrapper;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Allows toggling of inclusion of non-consumed items, allow excluding non-consumed fluids. Also, allows prioritizing
 * items.
 */
@Mixin(value = RecipeTransferBuilder.class, remap = false)
public class RecipeTransferBuilderMixin {

    @Shadow
    @Final
    private IRecipeLayout recipe;

    @Redirect(method = "split",
              at = @At(value = "INVOKE",
                       target = "Lcom/glodblock/github/integration/gregtech/GregUtil;isNotConsume(Lmezz/jei/api/recipe/IRecipeWrapper;I)Z"),
              require = 1)
    private boolean toggleNonConsume(IRecipeWrapper wrapper, int e) {
        if (!(wrapper instanceof GTRecipeWrapper gt)) return false;

        // Always skip first item (scanner item) in assembly line recipes
        boolean assemblySkip = ((AccessibleGTRecipeWrapper) gt).labs$getRecipeMap() ==
                RecipeMaps.ASSEMBLY_LINE_RECIPES &&
                gt.getRecipe().getProperty(ResearchProperty.getInstance(), null) != null;

        if (assemblySkip && e == LabsAE2ImportHandler.ASSEMBLY_LINE_RESEARCH_SLOT) return true;
        return (container == null || !container.labs$inclNonConsume()) && gt.isNotConsumedItem(e);
    }

    @Redirect(method = "split",
              at = @At(value = "INVOKE", target = "Ljava/util/List;toArray([Ljava/lang/Object;)[Ljava/lang/Object;"),
              require = 1)
    private <T> T[] prioitizeItems(List<ItemStack> instance, T[] ts) {
        List<ItemStack> results = new ArrayList<>(instance.size());

        for (var item : instance) {
            if (LabsAE2ImportHandler.isItemStackPrioritized(item))
                results.add(0, item);
            else
                results.add(item);
        }

        // noinspection unchecked
        return (T[]) results.toArray(new ItemStack[0]);
    }

    @Redirect(method = "split",
              at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;", ordinal = 0),
              require = 1)
    private Collection<? extends IGuiIngredient<FluidStack>> handleNonConsumeFluids(Map<Integer, ? extends IGuiIngredient<FluidStack>> instance) {
        var ing = instance.values();

        if (container == null || container.labs$inclNonConsume()) return ing;
        if (!(recipe instanceof RecipeLayoutAccessor accessor)) return ing;
        if (!(accessor.labs$getRecipeWrapper() instanceof GTRecipeWrapper gt)) return ing;

        List<IGuiIngredient<FluidStack>> result = new ArrayList<>();
        for (var entry : instance.entrySet()) {
            if (entry.getValue().isInput() && gt.isNotConsumedFluid(entry.getKey())) continue;
            result.add(entry.getValue());
        }
        return result;
    }
}
