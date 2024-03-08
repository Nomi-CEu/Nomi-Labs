package com.nomiceu.nomilabs.gregtech.ingredient;

import gregtech.api.recipes.ingredients.GTRecipeInput;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Used for tool inputs. Other inputs with wild metadata can use the metadata value being 32767, but because tools
 * don't report all their different meta values, this needs to be done.
 */
@SuppressWarnings("unused")
public class GTRecipeItemWildMetaInput extends GTRecipeInput {
    // Can't use a map, there might be inputs with same item but different tag
    private final ItemStack[] inputStacks;

    public GTRecipeItemWildMetaInput(ItemStack stack) {
        this(new ItemStack[] { stack }, stack.getCount());
    }

    public GTRecipeItemWildMetaInput(ItemStack stack, int amount) {
        this(new ItemStack[] { stack }, amount);
    }

    public GTRecipeItemWildMetaInput(GTRecipeInput input) {
        this(input.getInputStacks());
    }

    public GTRecipeItemWildMetaInput(GTRecipeInput input, int amount) {
        this(input.getInputStacks(), amount);
    }

    public GTRecipeItemWildMetaInput(ItemStack... stacks) {
        this(stacks, stacks[0].getCount());
    }

    public GTRecipeItemWildMetaInput(ItemStack[] stacks, int amount) {
        this.amount = amount;

        List<ItemStack> result = new ObjectArrayList<>();
        for (var stack : stacks) {
            // Add all meta values of the amount
            for (int i = 0; i < OreDictionary.WILDCARD_VALUE; i++){
                result.add(new ItemStack(stack.getItem(), i, amount));
            }
        }

        inputStacks = result.toArray(new ItemStack[0]);
    }

    @Override
    protected GTRecipeInput copy() {
        return copyWithAmount(amount);
    }

    @Override
    public GTRecipeInput copyWithAmount(int amount) {
        GTRecipeItemWildMetaInput copy = new GTRecipeItemWildMetaInput(inputStacks, amount);
        copy.isConsumable = isConsumable;
        copy.nbtMatcher = nbtMatcher;
        copy.nbtCondition = nbtCondition;
        return copy;
    }

    @Override
    public ItemStack[] getInputStacks() {
        return inputStacks;
    }

    @Override
    public boolean acceptsStack(ItemStack input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        for (var stack : inputStacks) {
            if (stack.getItem() != input.getItem()) continue;

            if (nbtMatcher != null) {
                return nbtMatcher.evaluate(input, nbtCondition);
            }
            if (Objects.equals(stack.getTagCompound(), input.getTagCompound())) {
                return stack.areCapsCompatible(input);
            }
        }
        return false;
    }

    @Override
    protected int computeHash() {
        int hash = 1;
        for (var stack : inputStacks) {
            hash = 31 * hash + stack.getItem().hashCode();
            if (stack.getTagCompound() != null && this.nbtMatcher == null) {
                hash = 31 * hash + stack.getTagCompound().hashCode();
            }
        }
        hash = 31 * hash + amount;
        hash = 31 * hash + (isConsumable ? 1 : 0);
        hash = 31 * hash + (nbtMatcher != null ? nbtMatcher.hashCode() : 0);
        hash = 31 * hash + (nbtCondition != null ? nbtCondition.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GTRecipeItemWildMetaInput other)) return false;

        return equalIgnoreAmount(other) && amount != other.amount;
    }

    @Override
    public boolean equalIgnoreAmount(GTRecipeInput input) {
        if (this == input) return true;
        if (!(input instanceof GTRecipeItemWildMetaInput other)) return false;

        if (isConsumable != other.isConsumable) return false;
        if (!Objects.equals(nbtMatcher, other.nbtMatcher)) return false;
        if (!Objects.equals(nbtCondition, other.nbtCondition)) return false;

        if (inputStacks.length != other.inputStacks.length) return false;
        for (int i = 0; i < inputStacks.length; i++) {
            var thisStack = inputStacks[i];
            var otherStack = other.inputStacks[i];
            if (!ItemStack.areItemStacksEqual(thisStack, otherStack) ||
                    !Objects.equals(thisStack.getTagCompound(), otherStack.getTagCompound()))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return switch (inputStacks.length) {
            case 0 -> amount + "x[]";
            case 1 -> amount + "x" + toStringWithoutQuantity(inputStacks[0]);
            default -> amount + "x[" + Arrays.stream(inputStacks)
                    .map(GTRecipeItemWildMetaInput::toStringWithoutQuantity)
                    .collect(Collectors.joining("|")) + "]";
        };
    }

    private static String toStringWithoutQuantity(ItemStack stack) {
        return stack.getItem().getTranslationKey(stack) + "@" + stack.getItemDamage();
    }
}
