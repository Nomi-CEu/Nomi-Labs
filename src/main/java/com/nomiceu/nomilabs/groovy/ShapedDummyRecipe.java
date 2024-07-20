package com.nomiceu.nomilabs.groovy;

import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.ShapedCraftingRecipe;

/**
 * A shaped recipe where it shows up in JEI, but can never be crafted.
 * <p>
 * Inputs, Outputs, Width, etc. are still needed for the JEI preview.
 */
@SuppressWarnings("unused")
public class ShapedDummyRecipe extends ShapedCraftingRecipe {

    public ShapedDummyRecipe(ItemStack output, List<IIngredient> input, int width, int height, boolean mirrored) {
        super(output, input, width, height, mirrored, null, null);
    }

    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
        return false;
    }
}
