package com.nomiceu.nomilabs.gregtech.mixinhelper;

import com.nomiceu.nomilabs.groovy.ShapedConversionRecipe;
import gregtech.common.crafting.GTShapedOreRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * A disassembling recipe that assumes that there is only one input.
 */
public class GTDisassemblingOreRecipe extends GTShapedOreRecipe {
    private final int inputLocation;
    private Integer[] cache;

    public GTDisassemblingOreRecipe(boolean isClearing, ResourceLocation group, @NotNull ItemStack result, Object... recipe) {
        super(isClearing, group, result, recipe);
        for (int i = 0; i < input.size(); i++) {
            var ing = input.get(i);
            if (ing.apply(ItemStack.EMPTY)) continue; // Empty Ingredients Return True when ItemStack.EMPTY applied
            inputLocation = i;
            return;
        }
        inputLocation = -1;
    }

    /**
     * A matches where the input must perfectly match the ingredient list.
     */
    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World world) {
        return ShapedConversionRecipe.matchesShaped(inv, inputLocation, (stack) -> input.get(inputLocation).apply(stack), cache, (cache1) -> cache = cache1);
    }
}
