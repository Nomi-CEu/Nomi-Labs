package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.mixin.gregtech.AccessibleDecompositionRecipeHandler;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

@GroovyBlacklist
public class ReplaceDecomposition {
    public static void replaceDecomposition(Material material, List<MaterialStack> components) {
        LabsVirtualizedRegistries.REPLACE_DECOMP_MANAGER.changeMaterialDecomp(material, components);
    }

    public static void reloadDecompositionRecipes() {
        if (LabsVirtualizedRegistries.REPLACE_DECOMP_MANAGER.needReloading.isEmpty()) return;
        var time = System.currentTimeMillis();
        for (var modified : LabsVirtualizedRegistries.REPLACE_DECOMP_MANAGER.needReloading.entrySet()) {
            var material = modified.getKey();
            NomiLabs.LOGGER.debug("Replacing Decomp Recipes for {}...", material.getRegistryName());
            removeDecompRecipe(RecipeMaps.ELECTROLYZER_RECIPES, material);
            removeDecompRecipe(RecipeMaps.CENTRIFUGE_RECIPES, material);
            if (modified.getValue().isEmpty()) continue;
            OrePrefix prefix = material.hasProperty(PropertyKey.DUST) ? OrePrefix.dust : null;
            NomiLabs.LOGGER.debug("Adding Decomp Recipes for {}...", material.getRegistryName());
            AccessibleDecompositionRecipeHandler.processDecomposition(prefix,material);
        }
        NomiLabs.LOGGER.info("Reloading Decomp Recipes took {}ms", System.currentTimeMillis() - time);
    }

    private static void removeDecompRecipe(RecipeMap<?> map, Material input) {
        ItemStack itemInput = ItemStack.EMPTY;
        FluidStack fluidInput = null;
        if (input.hasProperty(PropertyKey.DUST))
            itemInput = OreDictUnifier.get(OrePrefix.dust, input);
        else
            fluidInput = input.getFluid(1);
        var recipe = map.find(itemInput.isEmpty() ? Collections.emptyList() : Collections.singletonList(itemInput),
                fluidInput == null ? Collections.emptyList() : Collections.singletonList(fluidInput),
                (recipe1) -> true);
        if (recipe == null) return;
        NomiLabs.LOGGER.debug("Removing Decomp Recipe for {} @ {} in recipe map {}.",
                itemInput.getItem().getRegistryName(), itemInput.getMetadata(),
                map.getUnlocalizedName());
        map.removeRecipe(recipe);
    }
}
