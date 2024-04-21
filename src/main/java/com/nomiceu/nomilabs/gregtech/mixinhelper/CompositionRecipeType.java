package com.nomiceu.nomilabs.gregtech.mixinhelper;

import com.nomiceu.nomilabs.groovy.ChangeComposition;

import gregicality.multiblocks.api.recipes.GCYMRecipeMaps;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Material;

public enum CompositionRecipeType {

    ELECTROLYZER(RecipeMaps.ELECTROLYZER_RECIPES) {

        @Override
        public void remove(Material input) {
            // Have to check both, the decomp type could have changed!
            ChangeComposition.removeDecompRecipe(this, input);
            ChangeComposition.removeDecompRecipe(CENTRIFUGE, input);
        }
    },
    CENTRIFUGE(RecipeMaps.CENTRIFUGE_RECIPES) {

        @Override
        public void remove(Material input) {
            // Have to check both, the decomp type could have changed!
            ChangeComposition.removeDecompRecipe(ELECTROLYZER, input);
            ChangeComposition.removeDecompRecipe(this, input);
        }
    },
    ALLOY_BLAST(GCYMRecipeMaps.ALLOY_BLAST_RECIPES) {

        @Override
        public void remove(Material input) {
            ChangeComposition.removeABSRecipe(input);
        }
    },
    MIXER(RecipeMaps.MIXER_RECIPES) {

        @Override
        public void remove(Material input) {
            ChangeComposition.removeMixerRecipe(input);
        }
    };

    public final RecipeMap<?> map;

    CompositionRecipeType(RecipeMap<?> map) {
        this.map = map;
    }

    public abstract void remove(Material input);
}
