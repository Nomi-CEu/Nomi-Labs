package com.nomiceu.nomilabs.groovy;

import gregicality.multiblocks.api.fluids.GCYMFluidStorageKeys;
import gregicality.multiblocks.api.recipes.alloyblast.AlloyBlastRecipeProducer;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.BlastProperty;
import net.minecraftforge.fluids.Fluid;
import org.jetbrains.annotations.NotNull;

public class ABSRecipeReplacer extends AlloyBlastRecipeProducer {
    public static final AlloyBlastRecipeProducer REPLACE_PRODUCER = new ABSRecipeReplacer();

    /**
     * Same as super, but doesn't make a vac freezer recipe.
     */
    @Override
    public void produce(@NotNull Material material, @NotNull BlastProperty blastProperty) {
        int compAmount = material.getMaterialComponents().size();

        // ignore non-alloys
        if (compAmount < 2) return;

        // get the output fluid
        Fluid molten = material.getFluid(GCYMFluidStorageKeys.MOLTEN);


        RecipeBuilder<BlastRecipeBuilder> builder = createBuilder(blastProperty, material);

        int outputAmount = addInputs(material, builder);
        if (outputAmount <= 0) return;

        buildRecipes(blastProperty, molten, outputAmount, compAmount, builder);
    }
}
