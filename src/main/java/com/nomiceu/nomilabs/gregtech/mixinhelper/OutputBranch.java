package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.recipes.map.AbstractMapIngredient;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class OutputBranch {

    // Keys on this have *(should)* unique hashcodes, but perhaps not to specialNodes.
    private Map<AbstractMapIngredient, EitherOrBoth<Set<Recipe>, OutputBranch>> nodes;

    // Keys on this have *(should)* unique hashcodes, but perhaps not to nodes.
    private Map<AbstractMapIngredient, EitherOrBoth<Set<Recipe>, OutputBranch>> specialNodes;

    public boolean isEmpty() {
        return (nodes == null || nodes.isEmpty()) && (specialNodes == null || specialNodes.isEmpty());
    }

    @NotNull
    public Map<AbstractMapIngredient, EitherOrBoth<Set<Recipe>, OutputBranch>> getNodes(AbstractMapIngredient ingredient) {
        if (ingredient.isSpecialIngredient()) {
            if (specialNodes == null)
                specialNodes = new Object2ObjectOpenHashMap<>(1);
            return specialNodes;
        }
        if (nodes == null)
            nodes = new Object2ObjectOpenHashMap<>(1);
        return nodes;
    }

    @Nullable
    public Map<AbstractMapIngredient, EitherOrBoth<Set<Recipe>, OutputBranch>> getNodesIfExists(AbstractMapIngredient ingredient) {
        if (ingredient.isSpecialIngredient())
            return specialNodes;

        return nodes;
    }
}
