package com.nomiceu.nomilabs.gregtech.mixinhelper;

import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.map.MapFluidIngredient;

public class MapChancedFluidIngredient extends MapFluidIngredient {

    protected int chance;
    protected int chanceBoost;

    public MapChancedFluidIngredient(ChancedFluidOutput chancedFluidOutput) {
        super(chancedFluidOutput.getIngredient());
        chance = chancedFluidOutput.getChance();
        chanceBoost = chancedFluidOutput.getChanceBoost();
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
        MapChancedFluidIngredient f = (MapChancedFluidIngredient) o;
        return chance == f.chance && chanceBoost == f.chanceBoost;
    }

    @Override
    public String toString() {
        return "MapChancedFluidIngredient{" +
                "chance=" + chance +
                ", chanceBoost=" + chanceBoost +
                ", fluid=" + fluid.getName() +
                ", tag=" + tag +
                '}';
    }

    @Override
    public boolean isSpecialIngredient() {
        return true;
    }
}
