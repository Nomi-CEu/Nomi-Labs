package com.nomiceu.nomilabs.groovy;

import java.util.stream.Stream;

import gregtech.api.recipes.RecipeBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ChangeRecipeBuilderCollection<T extends RecipeBuilder<T>> extends ObjectArrayList<ChangeRecipeBuilder<T>> {

    /**
     * Copies the collection and each builder's original version.
     */
    public ChangeRecipeBuilderCollection<T> copy() {
        ChangeRecipeBuilderCollection<T> copy = new ChangeRecipeBuilderCollection<>();

        for (ChangeRecipeBuilder<T> change : this) {
            copy.add(change.copyOriginal());
        }

        return copy;
    }

    public static <
            T extends RecipeBuilder<T>> ChangeRecipeBuilderCollection<T> fromStream(Stream<ChangeRecipeBuilder<T>> stream) {
        ChangeRecipeBuilderCollection<T> collection = new ChangeRecipeBuilderCollection<>();
        stream.forEach(collection::add);
        return collection;
    }
}
