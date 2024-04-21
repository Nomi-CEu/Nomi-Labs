package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.IResourceStack;

import gregtech.api.unification.stack.MaterialStack;

/**
 * Allows using the Groovy '*' Operator, and allows it to be treated as an IIngredient.
 */
@Mixin(value = MaterialStack.class, remap = false)
@SuppressWarnings("unused")
public abstract class MaterialStackMixin implements IIngredient {

    @Shadow
    @Final
    public long amount;

    @Shadow
    public abstract MaterialStack copy(long amount);

    @Shadow
    public abstract MaterialStack copy();

    @Unique
    @Override
    public IIngredient exactCopy() {
        return (IIngredient) copy();
    }

    @Unique
    @Override
    public int getAmount() {
        return (int) amount;
    }

    @Unique
    @Override
    public void setAmount(int amount) {
        throw new NotImplementedException("Material Stack cannot be modified!");
    }

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return new ItemStack[0];
    }

    @Override
    public Ingredient toMcIngredient() {
        return Ingredient.EMPTY;
    }

    @Unique
    @Override
    public IResourceStack multiply(Number num) {
        return (IResourceStack) copy(num.longValue());
    }
}
