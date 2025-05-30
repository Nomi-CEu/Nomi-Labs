package com.nomiceu.nomilabs.groovy;

import net.minecraft.item.crafting.Ingredient;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.groovyscript.api.IIngredient;

/**
 * A template for a simple implementation of IIngredient, with stack size 1, no marks, and no copying.
 * <p>
 * Also sets `toMcIngredient` to return an ingredient of the matching stacks.
 */
public abstract class SimpleIIngredient implements IIngredient {

    @Override
    public Ingredient toMcIngredient() {
        return Ingredient.fromStacks(getMatchingStacks());
    }

    @Override
    public int getAmount() {
        return 1;
    }

    @Override
    public void setAmount(int amount) {}

    @Override
    public IIngredient exactCopy() {
        return this;
    }

    @Nullable
    @Override
    public String getMark() {
        return null;
    }

    @Override
    public void setMark(String mark) {}
}
