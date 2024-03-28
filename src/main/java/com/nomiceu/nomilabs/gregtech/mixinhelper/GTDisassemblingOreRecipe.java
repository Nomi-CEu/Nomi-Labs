package com.nomiceu.nomilabs.gregtech.mixinhelper;

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
        if (inv.getWidth() < 2 || inv.getWidth() > 3 || inv.getHeight() < 2 || inv.getHeight() > 3 || inv.getWidth() != inv.getHeight()) return false;
        if (inputLocation == -1) return false;
        var location = getLocationForDim(inv.getWidth());
        if (location == -1) return false;

        int locationX = location % inv.getWidth();
        int locationY = location / inv.getWidth();

        // Not using inv.stackList, as the stackList may not be correct for a given invCrafting.
        for (int x = 0; x < inv.getWidth(); x++) {
            for (int y = 0; y < inv.getHeight(); y++) {
                if (x == locationX && y == locationY) {
                    if (!input.get(inputLocation).apply(inv.getStackInRowAndColumn(x, y))) return false;
                    continue;
                }
                if (!inv.getStackInRowAndColumn(x, y).isEmpty()) return false;
            }
        }
        return true;
    }

    private int getLocationForDim(int dim) {
        switch (dim) {
            case 2 -> {
                if (inputLocation > 4 || inputLocation == 2) return -1; // Not in 2x2 square
                if (inputLocation > 2) return inputLocation - 1;
                return inputLocation;
            }
            case 3 -> { return inputLocation; }
            default -> { return -1; }
        }
    }
}
