package com.nomiceu.nomilabs.mixin.ae2fc;

import static com.nomiceu.nomilabs.integration.ae2fc.RecipeTransferBuilderState.container;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.glodblock.github.integration.jei.RecipeTransferBuilder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.mixin.jei.RecipeLayoutAccessor;

import gregtech.integration.jei.recipe.GTRecipeWrapper;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Allows toggling of inclusion of non-consumed items, allow excluding non-consumed fluids.
 */
@Mixin(value = RecipeTransferBuilder.class, remap = false)
public class RecipeTransferBuilderMixin {

    @Shadow
    @Final
    private IRecipeLayout recipe;

    @WrapOperation(method = "split",
                   at = @At(value = "INVOKE",
                            target = "Lcom/glodblock/github/integration/gregtech/GregUtil;isNotConsume(Lmezz/jei/api/recipe/IRecipeWrapper;I)Z"),
                   require = 1)
    private boolean toggleNonConsume(IRecipeWrapper instance, int e, Operation<Boolean> original) {
        return (container == null || !container.labs$inclNonConsume()) && original.call(instance, e);
    }

    @Redirect(method = "split",
              at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;", ordinal = 0),
              require = 1)
    private Collection<? extends IGuiIngredient<FluidStack>> handleNonConsumeFluids(Map<Integer, ? extends IGuiIngredient<FluidStack>> instance) {
        var ing = instance.values();

        if (container == null || container.labs$inclNonConsume()) return ing;
        if (!(recipe instanceof RecipeLayoutAccessor accessor)) return ing;
        if (!(accessor.labs$getRecipeWrapper() instanceof GTRecipeWrapper gt)) return ing;
        return instance.entrySet().stream()
                .filter(e -> !(e.getValue().isInput() && gt.isNotConsumedFluid(e.getKey())))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
