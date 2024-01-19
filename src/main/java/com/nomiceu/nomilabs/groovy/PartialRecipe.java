package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

/**
 * Used for recipes where the input is unknown (Replacing Output)
 */
@GroovyBlacklist
public class PartialRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IShapedRecipe {
    private final IShapedRecipe originalRecipe;
    private final ItemStack output;

    public PartialRecipe(IShapedRecipe originalRecipe, ItemStack output) {
        this.originalRecipe = originalRecipe;
        this.output = output;
    }
    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
        return this.originalRecipe.matches(inv, worldIn);
    }

    @Override
    public @NotNull ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return this.originalRecipe.canFit(width, height);
    }

    @Override
    public @NotNull ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull InventoryCrafting inv) {
        return this.originalRecipe.getRemainingItems(inv);
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.originalRecipe.getIngredients();
    }

    @Override
    public boolean isDynamic() {
        return this.originalRecipe.isDynamic();
    }

    @Override
    public @NotNull String getGroup() {
        return this.originalRecipe.getGroup();
    }

    @Override
    public int getRecipeWidth() {
        return originalRecipe.getRecipeWidth();
    }

    @Override
    public int getRecipeHeight() {
        return originalRecipe.getRecipeHeight();
    }
}
