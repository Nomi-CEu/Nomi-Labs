package com.nomiceu.nomilabs.recipe;

import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.IFrameable;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityFramingTable;
import com.nomiceu.nomilabs.NomiLabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

/**
 * This is the actual hand framing recipe, whilst the one in GroovyScript is the example one.
 */
public class HandFramingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe, IShapedRecipe {

    public HandFramingRecipe() {
    }

    @Override
    public boolean matches(InventoryCrafting inv, @NotNull World worldIn) {
        // Validate 2x2 grid
        if (inv.getWidth() == 2 && inv.getHeight() == 2) {
            return check2x2Grid(inv, 0, 0);
        }

        // Validate 3x3 grid
        if (inv.getWidth() == 3 && inv.getHeight() == 3) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    // Check every possible 2x2 grid
                    if (check2x2Grid(inv, i, j)) {
                        if (checkRemainingEmpty(inv, i, j)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean check2x2Grid(InventoryCrafting inv, int offsetX, int offsetY) {
        ItemStack topLeft = inv.getStackInRowAndColumn(offsetX, offsetY); // Side
        ItemStack bottomRight = inv.getStackInRowAndColumn(offsetX + 1, offsetY + 1); // Frameable

        return isValidCraftingSet(topLeft, bottomRight);
    }

    private boolean checkRemainingEmpty(InventoryCrafting inv, int offsetX, int offsetY) {
        for (int i = 0; i < inv.getWidth(); i++) {
            for (int j = 0; j < inv.getHeight(); j++) {
                if (i >= offsetX && i < offsetX + 2 && j >= offsetY && j < offsetY + 2) {
                    continue;
                }

                if (!inv.getStackInRowAndColumn(i, j).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                ItemStack resultStack = tryCraftingAt(inv, i, j);
                if (!resultStack.isEmpty()) {
                    return resultStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 2 && height >= 2;
    }

    private ItemStack tryCraftingAt(InventoryCrafting inv, int i, int j) {
        ItemStack topLeft = inv.getStackInRowAndColumn(i, j).copy(); // Side
        ItemStack topRight = inv.getStackInRowAndColumn(i + 1, j).copy(); // Trim
        ItemStack bottomLeft = inv.getStackInRowAndColumn(i, j + 1).copy(); // Front
        ItemStack bottomRight = inv.getStackInRowAndColumn(i + 1, j + 1).copy();  // Tool

        // Validate the tool is in the bottomRight slot and the side material is provided
        if (isValidCraftingSet(topLeft, bottomRight)) {
            return ((IFrameable) bottomRight.getItem()).decorate(bottomRight, topLeft, getValidStackOrEmpty(topRight), getValidStackOrEmpty(bottomLeft));
        }

        return ItemStack.EMPTY;
    }

    private boolean isValidCraftingSet(ItemStack side, ItemStack frameable) {
        return TileEntityFramingTable.isItemValidDrawer(frameable) &&
                TileEntityFramingTable.isItemValidMaterial(side);
    }

    private ItemStack getValidStackOrEmpty(ItemStack stack) {
        if (TileEntityFramingTable.isItemValidMaterial(stack))
            return stack;
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getRecipeOutput() {
        // Dynamic Recipe
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public int getRecipeWidth() {
        return 2;
    }

    @Override
    public int getRecipeHeight() {
        return 2;
    }
}
