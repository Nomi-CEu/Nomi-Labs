package com.nomiceu.nomilabs.groovy;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.ShapedCraftingRecipe;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;

import groovy.lang.Closure;

/**
 * A shaped conversion recipe where there must be only one input.
 */
@SuppressWarnings("unused")
public class ShapedConversionRecipe extends ShapedCraftingRecipe {

    private int recipeInputLocation;
    private int trueInputLocation;

    private Integer[] cache;

    public ShapedConversionRecipe(ItemStack output, List<IIngredient> input, int width, int height) {
        this(output, input, width, height, false, null, null);
    }

    public ShapedConversionRecipe(ItemStack output, List<IIngredient> input, int width, int height, boolean mirrored,
                                  @Nullable Closure<ItemStack> recipeFunction, @Nullable Closure<Void> recipeAction) {
        super(output, input, width, height, mirrored, recipeFunction, recipeAction);
        recipeInputLocation = -1;
        if (width != height) {
            GroovyLog.get()
                    .exception(new IllegalArgumentException(
                            "Shaped Conversion Recipes must have the same width and height!"));
            return;
        }
        for (int i = 0; i < input.size(); i++) {
            var ing = input.get(i);
            if (IngredientHelper.isEmpty(ing)) continue;
            if (recipeInputLocation != -1) {
                GroovyLog.get()
                        .exception(new IllegalArgumentException(
                                "Shaped Conversion Recipes can only have one non-empty input!"));
                return;
            }
            recipeInputLocation = i;
        }
        trueInputLocation = recipeInputLocation;
        if (width == 1) {
            // Default to center
            recipeInputLocation = 4;
        } else if (width == 2) {
            if (recipeInputLocation > 1) {
                recipeInputLocation++;
            }
        }
    }

    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
        return matchesShaped(inv, recipeInputLocation, (stack) -> input.get(trueInputLocation).test(stack), cache,
                (cache1) -> cache = cache1);
    }

    public static boolean matchesShaped(@NotNull InventoryCrafting inv, int inputLocation, Predicate<ItemStack> accepts,
                                        Integer[] cache, Consumer<Integer[]> setCache) {
        if (inv.getWidth() < 2 || inv.getWidth() > 3 || inv.getHeight() < 2 || inv.getHeight() > 3 ||
                inv.getWidth() != inv.getHeight())
            return false;
        if (inputLocation == -1) return false;
        var location = getLocationForDim(inv.getWidth(), inputLocation, cache, setCache);
        if (location == -1) return false;

        int locationX = location % inv.getWidth();
        int locationY = location / inv.getWidth();

        // Not using inv.stackList, as the stackList may not be correct for a given invCrafting.
        for (int x = 0; x < inv.getWidth(); x++) {
            for (int y = 0; y < inv.getHeight(); y++) {
                if (x == locationX && y == locationY) {
                    if (!accepts.test(inv.getStackInRowAndColumn(x, y))) return false;
                    continue;
                }
                if (!inv.getStackInRowAndColumn(x, y).isEmpty()) return false;
            }
        }
        return true;
    }

    private static int getLocationForDim(int dim, int inputLocation, Integer[] cache, Consumer<Integer[]> setCache) {
        if (cache == null) {
            cache = new Integer[2];

            cache[0] = inputLocation;
            if (inputLocation > 4 || inputLocation == 2) cache[0] = -1; // Not in 2x2 square
            else if (inputLocation > 2) cache[0] = inputLocation - 1;

            cache[1] = inputLocation;
            setCache.accept(cache);
        }
        // 0 when cache is 2, 1 when cache is 3
        return cache[dim - 2];
    }
}
