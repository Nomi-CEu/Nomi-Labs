package com.nomiceu.nomilabs.recipe;

import com.nomiceu.nomilabs.item.ItemHandFramingTool;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class HandFramingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private final ItemStack output;

    public HandFramingRecipe(ItemStack output) {
        this.output = output;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
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
        ItemStack topLeft = inv.getStackInRowAndColumn(offsetX, offsetY);
        ItemStack bottomRight = inv.getStackInRowAndColumn(offsetX + 1, offsetY + 1);

        return !bottomRight.isEmpty() && bottomRight.getItem() == output.getItem() && !topLeft.isEmpty();
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
    public ItemStack getCraftingResult(InventoryCrafting inv) {
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

    private ItemStack tryCraftingAt(InventoryCrafting inv, int i, int j) {
        ItemStack topLeft = inv.getStackInRowAndColumn(i, j); // Side
        ItemStack topRight = inv.getStackInRowAndColumn(i + 1, j); // Trim
        ItemStack bottomLeft = inv.getStackInRowAndColumn(i, j + 1); // Front
        ItemStack bottomRight = inv.getStackInRowAndColumn(i + 1, j + 1);  // Tool

        // Validate the tool is in the bottomRight slot and the side material is provided
        if (isValidCraftingSet(topLeft, bottomRight)) {
            NBTTagCompound tag = new NBTTagCompound();
            ItemStack resultStack = output.copy();

            tag.setTag(ItemHandFramingTool.MAT_SIDE_TAG, topLeft.serializeNBT());

            addTagIfValidBlock(ItemHandFramingTool.MAT_FRONT_TAG, bottomLeft, tag); // Add front material if provided
            addTagIfValidBlock(ItemHandFramingTool.MAT_TRIM_TAG, topRight, tag); // Add trim material if provided

            resultStack.setTagCompound(tag);
            return resultStack;
        }

        return ItemStack.EMPTY;
    }

    private boolean isValidCraftingSet(ItemStack topLeft, ItemStack bottomRight) {
        return !bottomRight.isEmpty() && bottomRight.getItem() == output.getItem() &&
                !topLeft.isEmpty() && topLeft.getItem() instanceof ItemBlock &&
                ((ItemBlock) topLeft.getItem()).getBlock() != Blocks.AIR;
    }

    private void addTagIfValidBlock(String tagName, ItemStack stack, NBTTagCompound tag) {
        if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() != Blocks.AIR) {
            tag.setTag(tagName, stack.serializeNBT());
        }
    }

    @Override
    public boolean canFit(int width, int height) {
        return (width == 2 && height == 2) || (width == 3 && height == 3);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }
}
