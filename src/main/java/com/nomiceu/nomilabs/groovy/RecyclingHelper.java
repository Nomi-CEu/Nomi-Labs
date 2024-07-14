package com.nomiceu.nomilabs.groovy;

import static com.cleanroommc.groovyscript.compat.vanilla.VanillaModule.crafting;
import static com.nomiceu.nomilabs.util.LabsGroovyHelper.throwOrGroovyLog;

import java.util.*;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.util.ItemTagMeta;
import com.nomiceu.nomilabs.util.LabsNames;

import gregtech.api.recipes.*;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.category.RecipeCategories;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.GTRecipeOreInput;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.stack.ItemMaterialInfo;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.loaders.recipe.RecyclingRecipes;

@GroovyBlacklist
public class RecyclingHelper {

    private static boolean reloadingRecycling = false;

    public static final Map<RecipeMap<?>, GTRecipeCategory> recyclingMaps = ImmutableMap.of(
            RecipeMaps.ARC_FURNACE_RECIPES, RecipeCategories.ARC_FURNACE_RECYCLING,
            RecipeMaps.MACERATOR_RECIPES, RecipeCategories.MACERATOR_RECYCLING,
            RecipeMaps.EXTRACTOR_RECIPES, RecipeCategories.EXTRACTOR_RECYCLING);

    public static void reloadRecyclingRecipes() {
        if (LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.needReloading.isEmpty()) return;
        reloadingRecycling = true;
        var time = System.currentTimeMillis();
        for (var modified : LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.needReloading.entrySet()) {
            var itemMeta = modified.getKey();
            var stack = itemMeta.toStack();
            NomiLabs.LOGGER.debug("Removing Recycling Recipes for {} @ {}...", itemMeta.getItem().getRegistryName(),
                    itemMeta.getMeta());
            removeRecyclingRecipe(RecipeMaps.ARC_FURNACE_RECIPES, RecipeCategories.ARC_FURNACE_RECYCLING, stack,
                    Materials.Oxygen.getFluid());
            removeRecyclingRecipe(RecipeMaps.MACERATOR_RECIPES, RecipeCategories.MACERATOR_RECYCLING, stack, null);
            removeRecyclingRecipe(RecipeMaps.EXTRACTOR_RECIPES, RecipeCategories.EXTRACTOR_RECYCLING, stack, null);
            if (modified.getValue() == null) continue;
            NomiLabs.LOGGER.debug("Adding Recycling Recipes for {} @ {}...", itemMeta.getItem().getRegistryName(),
                    itemMeta.getMeta());
            RecyclingRecipes.registerRecyclingRecipes(stack, modified.getValue().getMaterials(), false, null);
        }
        NomiLabs.LOGGER.info("Reloading Recycling Recipes took {}ms", System.currentTimeMillis() - time);
        reloadingRecycling = false;
    }

    private static void removeRecyclingRecipe(RecipeMap<?> map, GTRecipeCategory category, ItemStack itemInput,
                                              Fluid fluidInput) {
        var recipe = map.find(Collections.singletonList(itemInput),
                fluidInput == null ? Collections.emptyList() : Collections.singletonList(new FluidStack(fluidInput, 1)),
                (recipe1) -> recipe1.getRecipeCategory().equals(category));
        if (recipe == null) return;
        NomiLabs.LOGGER.debug("Removing Recycling Recipe for {} @ {} in recipe map {} and recipe category {}.",
                itemInput.getItem().getRegistryName(), itemInput.getMetadata(),
                map.getUnlocalizedName(), category.getName());
        map.removeRecipe(recipe);
    }

    public static void replaceRecipeShaped(ResourceLocation name, ItemStack output, List<List<IIngredient>> inputs) {
        validate(name, output, true, false);
        crafting.remove(name);
        crafting.addShaped(LabsNames.makeGroovyName(name.getPath()), output, inputs);
        registerRecycling(output, inputs);
    }

    public static void replaceRecipeShaped(ItemStack oldOutput, ItemStack newOutput, List<List<IIngredient>> inputs) {
        replaceRecipeShaped(getRecipeName(oldOutput), newOutput, inputs);
    }

    public static void replaceRecipeOutput(ResourceLocation name, ItemStack newOutput) {
        IShapedRecipe originalRecipe = validate(name, newOutput, true, true);
        var originalCount = originalRecipe.getRecipeOutput().getCount();
        var newCount = newOutput.getCount();

        crafting.remove(name);
        ReloadableRegistryManager.addRegistryEntry(ForgeRegistries.RECIPES, LabsNames.makeGroovyName(name.getPath()),
                new PartialRecipe(originalRecipe, newOutput.copy()));

        ImmutableList<MaterialStack> originalMaterials = Objects
                .requireNonNull(OreDictUnifier.getMaterialInfo(newOutput)).getMaterials();
        List<MaterialStack> newMaterials = new ArrayList<>();

        // Multiplies by original then Divides by new as
        // https://github.com/GregTechCEu/GregTech/blob/master/src/main/java/gregtech/api/recipes/RecyclingHandler.java#L82
        // divides
        originalMaterials.forEach((materialStack -> newMaterials
                .add(new MaterialStack(materialStack.material, materialStack.amount * originalCount / newCount))));

        LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerOre(newOutput, new ItemMaterialInfo(newMaterials));
    }

    public static void replaceRecipeOutput(ItemStack oldOutput, ItemStack newOutput) {
        replaceRecipeOutput(getRecipeName(oldOutput), newOutput);
    }

    public static void replaceRecipeInput(ResourceLocation name, List<List<IIngredient>> newInputs) {
        IRecipe originalRecipe = validate(name, ItemStack.EMPTY, false, false);
        var originalOutput = originalRecipe.getRecipeOutput();
        crafting.remove(name);
        crafting.addShaped(LabsNames.makeGroovyName(name.getPath()), originalOutput, newInputs);
        registerRecycling(originalOutput, newInputs);
    }

    public static void replaceRecipeInput(ItemStack oldOutput, List<List<IIngredient>> newInputs) {
        replaceRecipeInput(getRecipeName(oldOutput), newInputs);
    }

    public static void createRecipe(String name, ItemStack output, List<List<IIngredient>> input) {
        crafting.addShaped(LabsNames.makeGroovyName(name), output, input);
        registerRecycling(output, input);
    }

    public static void createRecipe(ItemStack output, List<List<IIngredient>> input) {
        crafting.addShaped(output, input);
        registerRecycling(output, input);
    }

    public static void changeStackRecycling(ItemStack output, List<IIngredient> ingredients) {
        registerRecycling(output, Collections.singletonList(ingredients));
    }

    /**
     * Helper Method for Recipe/Recipe Builder Method to Use.<br>
     * Returns false if recipe is invalid, and returns true if successful.
     */
    public static boolean changeStackRecycling(List<ItemStack> outputs, List<GTRecipeInput> inputs) {
        if (outputs.size() != 1 || inputs.isEmpty()) {
            throwOrGroovyLog(new IllegalArgumentException(
                    "Cannot change recycling from recipe when there is not one item output, or there are no item inputs!"));
            return false;
        }
        LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerOre(outputs.get(0),
                RecyclingHandler.getRecyclingIngredients(inputs, outputs.get(0).getCount()));
        return true;
    }

    /**
     * Helper method for find recipe + change stack recycling.<br>
     * Returns false if recipe is not found, more than one recipe is found, or the found recipe is invalid.
     * Returns true otherwise.
     */
    public static boolean changeStackRecycling(ItemStack output, RecipeMap<?> map, Predicate<Recipe> acceptedRecipe) {
        var foundRecipes = ((AccessibleRecipeMap) map).findByOutput(Collections.singletonList(output),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), acceptedRecipe);

        if (foundRecipes == null || foundRecipes.isEmpty()) { // Is Empty probably won't happen, but no harm in checking
            throwOrGroovyLog(new IllegalStateException("Could not find recipe in Recipe Map " +
                    map.getUnlocalizedName() + " with output " + output.toString() + "!"));
            return false;
        }

        if (foundRecipes.size() > 1) {
            // Check if the found recipes have the same item input, as that means they are the same for the purposes of
            // recycling.
            // This may take a while with a lot of recipes. Hopefully not required much if the users specify recipes
            // correctly.
            // Use Multiset: an efficient way to compare inputs where inputs can be duplicated.
            Multiset<GTRecipeInput> inputs = HashMultiset.create();
            inputs.addAll(foundRecipes.get(0).getInputs());
            boolean inputsAllEqual = true;

            // Skip Recipe with index 0, that is already the inputs from above
            for (int i = 1; i < foundRecipes.size(); i++) {
                Multiset<GTRecipeInput> compare = HashMultiset.create();
                compare.addAll(foundRecipes.get(i).getInputs());

                if (!inputs.equals(compare)) {
                    inputsAllEqual = false;
                    break;
                }
            }

            if (!inputsAllEqual) {
                throwOrGroovyLog(new IllegalStateException("Found more than one recipe in Recipe Map " +
                        map.getUnlocalizedName() + " with output " + output.toString() + "!"));
                return false;
            }
        }

        return changeStackRecycling(Collections.singletonList(output), foundRecipes.get(0).getInputs());
    }

    private static IShapedRecipe validate(ResourceLocation name, ItemStack output, boolean validateOutput,
                                          boolean requireOutputOriginalInfo) {
        IRecipe originalRecipe = ForgeRegistries.RECIPES.getValue(name);
        if (originalRecipe == null)
            throw new IllegalArgumentException("Could not find recipe with name " + name + "!");

        if (!(originalRecipe instanceof IShapedRecipe shapedRecipe))
            throw new IllegalArgumentException("Recipe with name " + name + " is not shaped!");

        if (originalRecipe.isDynamic())
            throw new IllegalArgumentException("Cannot replace Dynamic Recipe " + name + "!");

        ItemStack originalOutput = checkAndGetOutput(output, validateOutput, originalRecipe);

        if (requireOutputOriginalInfo && OreDictUnifier.getMaterialInfo(originalOutput) == null)
            throw new IllegalArgumentException("Could not find existing Material Info for item " + originalOutput);

        ReloadableRegistryManager.removeRegistryEntry(ForgeRegistries.RECIPES, name);

        return shapedRecipe;
    }

    private static boolean isRecipeValid(ResourceLocation name) {
        IRecipe originalRecipe = ForgeRegistries.RECIPES.getValue(name);
        if (originalRecipe == null)
            return false;

        if (!(originalRecipe instanceof IShapedRecipe))
            return false;

        return !originalRecipe.isDynamic();
    }

    private static void registerRecycling(ItemStack output, List<List<IIngredient>> inputs) {
        if (inputs.isEmpty() || inputs.stream().allMatch(List::isEmpty)) {
            LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerOre(output, null);
            return;
        }
        List<GTRecipeInput> gtInputs = new ArrayList<>();
        for (var inputList : inputs) {
            for (var input : inputList) {
                var gtInput = ofGroovyIngredient(input);
                if (gtInput != null)
                    gtInputs.add(gtInput);
            }
        }
        LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerOre(output,
                RecyclingHandler.getRecyclingIngredients(gtInputs, output.getCount()));
    }

    @NotNull
    private static ItemStack checkAndGetOutput(ItemStack output, boolean validateOutput, IRecipe originalRecipe) {
        ItemStack originalOutput = originalRecipe.getRecipeOutput();
        if (validateOutput) {
            if (!Objects.equals(originalOutput.getItem().getRegistryName(), output.getItem().getRegistryName()))
                throw new IllegalArgumentException("New Recipe Output " + output.getItem().getRegistryName() +
                        " must have same Registry Name as Original Input " +
                        originalOutput.getItem().getRegistryName() + "!");
            if (originalOutput.getMetadata() != output.getMetadata())
                throw new IllegalArgumentException("New Recipe Output's Metadata " + output.getMetadata() +
                        " must be the same as Original Input's Metadata " + originalOutput.getMetadata() + "!");
        }
        return originalOutput;
    }

    @SuppressWarnings("ConstantValue")
    @Nullable
    private static GTRecipeInput ofGroovyIngredient(IIngredient ingredient) {
        if (ingredient instanceof OreDictIngredient oreDictIngredient) {
            return new GTRecipeOreInput(oreDictIngredient.getOreDict(), ingredient.getAmount());
        }
        if ((Object) ingredient instanceof ItemStack stack) {
            return new GTRecipeItemInput(stack);
        }
        return null;
    }

    private static ResourceLocation getRecipeName(ItemStack oldOutput) {
        ResourceLocation name = null;
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            if (recipe.getRegistryName() != null &&
                    isRecipeValid(recipe.getRegistryName()) &&
                    ItemTagMeta.compare(recipe.getRecipeOutput(), oldOutput) &&
                    recipe.getRecipeOutput().getCount() == oldOutput.getCount()) {
                if (name == null) {
                    name = recipe.getRegistryName();
                    continue;
                }
                throw new IllegalArgumentException("Error Finding Recipes for Output " + oldOutput + ":" +
                        "Too Many Recipes for Output. Max: 1.");
            }
        }
        if (name == null)
            throw new IllegalArgumentException("Error Finding Recipes for Output " + oldOutput + ":" +
                    "No recipes for Output. Requires: 1. Recipes must not be dynamic, and have the exact same stack, including count, metadata and tag.");
        return name;
    }

    public static boolean isReloadingRecycling() {
        return reloadingRecycling;
    }
}
