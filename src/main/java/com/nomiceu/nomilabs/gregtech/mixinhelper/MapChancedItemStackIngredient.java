package com.nomiceu.nomilabs.gregtech.mixinhelper;

import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;

public class MapChancedItemStackIngredient extends MapOutputItemStackIngredient {
    protected int chance;
    protected int chanceBoost;

    public MapChancedItemStackIngredient(ChancedItemOutput chancedItemOutput) {
        super(chancedItemOutput.getIngredient(), chancedItemOutput.getIngredient().getMetadata(), chancedItemOutput.getIngredient().getTagCompound());
        chance = chancedItemOutput.getChance();
        chanceBoost = chancedItemOutput.getChanceBoost();
    }

    @Override
    protected int hash() {
        int hash = super.hash();
        hash += 31 * chance;
        hash += 31 * chanceBoost;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        MapChancedItemStackIngredient i = (MapChancedItemStackIngredient) o;
        return chance == i.chance && chanceBoost == i.chanceBoost;
    }

    @Override
    public String toString() {
        return "MapChancedItemStackIngredient{" +
                "chance=" + chance +
                ", chanceBoost=" + chanceBoost +
                ", item=" + stack.getItem().getRegistryName() +
                ", meta=" + meta +
                ", tag=" + tag +
                '}';
    }

    @Override
    public boolean isSpecialIngredient() {
        return true;
    }
}
