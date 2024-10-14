package com.nomiceu.nomilabs.groovy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import com.brandon3055.draconicevolution.lib.RecipeManager;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.cleanroommc.groovyscript.helper.recipe.RecipeName;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.cleanroommc.groovyscript.sandbox.ClosureHelper;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.integration.jei.JEIPlugin;
import com.nomiceu.nomilabs.integration.storagedrawers.CustomUpgradeHandler;
import com.nomiceu.nomilabs.mixin.gregtech.RecipeBuilderAccessor;
import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.util.GTUtility;
import gregtech.client.utils.TooltipHelper;
import gregtech.integration.groovy.VirtualizedRecipeMap;
import groovy.lang.Closure;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * The interface for groovy to interact with.
 */
@SuppressWarnings("unused")
public class GroovyHelpers {

    public static class TranslationHelpers {

        public static String translate(String key, Object... params) {
            return LabsTranslate.translate(key, params);
        }

        public static String translateFormat(String key, TooltipHelper.GTFormatCode format, Object... params) {
            return LabsTranslate.translateFormat(key, format, params);
        }

        public static String format(String str, TextFormatting... formats) {
            return LabsTranslate.format(str, formats);
        }

        public static String format(String str, TooltipHelper.GTFormatCode... formats) {
            return LabsTranslate.format(str, formats);
        }

        public static String format(String str, LabsTranslate.Format... formats) {
            return LabsTranslate.format(str, formats);
        }

        public static LabsTranslate.Translatable translatable(String key, Object... params) {
            return LabsTranslate.translatable(key, params);
        }

        public static LabsTranslate.Translatable translatableLiteral(String text) {
            return LabsTranslate.translatableLiteral(text);
        }

        public static LabsTranslate.Translatable translatableEmpty() {
            return LabsTranslate.translatableEmpty();
        }
    }

    public static class TooltipHelpers {

        public static void addTooltip(ItemStack item, List<LabsTranslate.Translatable> tr) {
            LabsTooltipHelper.addTooltip(new ItemMeta(item), tr);
        }

        public static void addTooltip(ItemStack item, LabsTranslate.Translatable tr) {
            // Don't use Collections.singletonList, as other elements may need to be added
            List<LabsTranslate.Translatable> list = new ArrayList<>();
            list.add(tr);
            LabsTooltipHelper.addTooltip(new ItemMeta(item), list);
        }
    }

    public static class SafeMethodHelpers {

        /**
         * Calls a declared instance method of a caller safely. Searches for the method in that class and its
         * subclasses.
         */
        @Nullable
        public static Object callInstanceMethod(Object caller, @NotNull String methodName,
                                                @Nullable List<Object> params) {
            return callMethod(caller.getClass(), caller, methodName, params, false);
        }

        /**
         * Calls a declared instance method of a specific class safely. Only searches for that method in that class.
         */
        @Nullable
        public static Object callInstanceMethodOfClass(Class<?> clazz, Object caller, @NotNull String methodName,
                                                       @Nullable List<Object> params) {
            return callMethod(clazz, caller, methodName, params, true);
        }

        /**
         * Calls a declared static method of a class safely. Only searches for that method in that class.
         */
        @Nullable
        public static Object callStaticMethod(Class<?> clazz, @NotNull String methodName,
                                              @Nullable List<Object> params) {
            return callMethod(clazz, null, methodName, params, true);
        }

        /**
         * Call a method of a class safely.
         * 
         * @param clazz      Class to call.
         * @param caller     Caller. Null if static.
         * @param methodName Name of method
         * @param params     Params. Null or Empty List if none
         * @param strict     Whether to be strict. Strict means that checking for the method is only done in the class
         *                   provided.
         */
        @Nullable
        public static Object callMethod(Class<?> clazz, @Nullable Object caller, @NotNull String methodName,
                                        @Nullable List<Object> params, boolean strict) {
            try {
                var paramArray = params != null ?
                        params.toArray() :
                        new Object[0];
                var paramClasses = params != null ?
                        params.stream().map(Object::getClass).toArray(Class<?>[]::new) :
                        new Class<?>[0];
                Method method = strict ? clazz.getDeclaredMethod(methodName, paramClasses) :
                        clazz.getMethod(methodName, paramClasses);
                return method.invoke(caller, paramArray);
            } catch (NoSuchMethodException | ClassCastException | NoClassDefFoundError | InvocationTargetException |
                     IllegalAccessException | IllegalArgumentException e) {
                // Doesn't throw it, but lets us know about the issue.
                GroovyLog.get().exception(e);
                return null;
            }
        }
    }

    public static class JEIHelpers {

        /* Description + Tooltip */
        public static void addDescription(ItemStack stack, LabsTranslate.Translatable... description) {
            JEIPlugin.addGroovyDescription(stack, description);
        }

        public static void addRecipeOutputTooltip(ItemStack stack, LabsTranslate.Translatable... tooltip) {
            JEIPlugin.addGroovyRecipeOutputTooltip(stack, tooltip);
        }

        public static void addRecipeOutputTooltip(ItemStack stack, ResourceLocation recipeName,
                                                  LabsTranslate.Translatable... tooltip) {
            JEIPlugin.addGroovyRecipeOutputTooltip(stack, recipeName, tooltip);
        }

        /* Hiding Ignore NBT */
        public static void hideItemIgnoreNBT(ItemStack stack) {
            JEIPlugin.hideItemNBTMatch(stack, (tag) -> true);
        }

        public static void removeAndHideItemIgnoreNBT(ItemStack stack) {
            JEIPlugin.removeAndHideItemNBTMatch(stack, (tag) -> true);
        }

        public static void yeetItemIgnoreNBT(ItemStack stack) {
            removeAndHideItemIgnoreNBT(stack);
        }

        /* Hiding NBT Match */
        public static void hideItemNBTMatch(ItemStack stack, Function<NBTTagCompound, Boolean> condition) {
            JEIPlugin.hideItemNBTMatch(stack, condition);
        }

        public static void removeAndHideItemNBTMatch(ItemStack stack, Function<NBTTagCompound, Boolean> condition) {
            JEIPlugin.removeAndHideItemNBTMatch(stack, condition);
        }

        public static void yeetItemNBTMatch(ItemStack stack, Function<NBTTagCompound, Boolean> condition) {
            JEIPlugin.removeAndHideItemNBTMatch(stack, condition);
        }
    }

    public static class MaterialHelpers {

        public static void hideMaterial(Material material) {
            MaterialHelper.forMaterialItem(material,
                    (stack) -> ModSupport.JEI.get().ingredient.hide(IngredientHelper.toIIngredient(stack)));
            MaterialHelper.forMaterialFluid(material, (fluid) -> ModSupport.JEI.get().ingredient
                    .hide(IngredientHelper.toIIngredient(toFluidStack(fluid))));
        }

        public static void removeAndHideMaterial(Material material) {
            MaterialHelper.forMaterialItem(material,
                    (stack) -> ModSupport.JEI.get().ingredient.removeAndHide(IngredientHelper.toIIngredient(stack)));
            // Normal Hiding for Fluids, they don't have recipes
            MaterialHelper.forMaterialFluid(material, (fluid) -> ModSupport.JEI.get().ingredient
                    .hide(IngredientHelper.toIIngredient(toFluidStack(fluid))));
        }

        public static void yeetMaterial(Material material) {
            removeAndHideMaterial(material);
        }

        public static void forMaterial(Material material, Closure<ItemStack> itemAction, Closure<Fluid> fluidAction) {
            forMaterialItem(material, itemAction);
            forMaterialFluid(material, fluidAction);
        }

        public static void forMaterialItem(Material material, Closure<ItemStack> action) {
            MaterialHelper.forMaterialItem(material, (stack) -> ClosureHelper.call(action, stack));
        }

        public static void forMaterialFluid(Material material, Closure<Fluid> action) {
            MaterialHelper.forMaterialFluid(material, (fluid) -> ClosureHelper.call(action, fluid));
        }

        private static FluidStack toFluidStack(Fluid fluid) {
            return new FluidStack(fluid, 1);
        }
    }

    public static class RecyclingHelpers {

        public static void replaceRecipeShaped(String name, ItemStack output, List<List<IIngredient>> inputs) {
            if (name.contains(":"))
                replaceRecipeShaped(new ResourceLocation(name), output, inputs);
            else
                replaceRecipeShaped(GTUtility.gregtechId(name), output, inputs);
        }

        public static void replaceRecipeShaped(ResourceLocation name, ItemStack output,
                                               List<List<IIngredient>> inputs) {
            RecyclingHelper.replaceRecipeShaped(name, output, inputs);
        }

        public static void replaceRecipeShaped(ItemStack oldOutput, ItemStack newOutput,
                                               List<List<IIngredient>> inputs) {
            RecyclingHelper.replaceRecipeShaped(oldOutput, newOutput, inputs);
        }

        public static void replaceRecipeOutput(String name, ItemStack output) {
            if (name.contains(":"))
                replaceRecipeOutput(new ResourceLocation(name), output);
            else
                replaceRecipeOutput(GTUtility.gregtechId(name), output);
        }

        public static void replaceRecipeOutput(ResourceLocation name, ItemStack newOutput) {
            RecyclingHelper.replaceRecipeOutput(name, newOutput);
        }

        public static void replaceRecipeOutput(ItemStack oldOutput, ItemStack newOutput) {
            RecyclingHelper.replaceRecipeOutput(oldOutput, newOutput);
        }

        public static void replaceRecipeInput(String name, List<List<IIngredient>> inputs) {
            if (name.contains(":"))
                replaceRecipeInput(new ResourceLocation(name), inputs);
            else
                replaceRecipeInput(GTUtility.gregtechId(name), inputs);
        }

        public static void replaceRecipeInput(ResourceLocation name, List<List<IIngredient>> newInputs) {
            RecyclingHelper.replaceRecipeInput(name, newInputs);
        }

        public static void replaceRecipeInput(ItemStack oldOutput, List<List<IIngredient>> newInputs) {
            RecyclingHelper.replaceRecipeInput(oldOutput, newInputs);
        }

        public static void createRecipe(String name, ItemStack output, List<List<IIngredient>> input) {
            RecyclingHelper.createRecipe(name, output, input);
        }

        public static void createRecipe(ItemStack output, List<List<IIngredient>> input) {
            RecyclingHelper.createRecipe(output, input);
        }

        public static void changeStackRecycling(ItemStack output, List<IIngredient> ingredients) {
            RecyclingHelper.changeStackRecycling(output, ingredients);
        }

        public static void changeStackRecyclingNBT(ItemStack output, List<IIngredient> ingredients, NBTMatcher matcher,
                                                   NBTCondition condition) {
            RecyclingHelper.changeStackRecycling(output, ingredients);

            LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerNBTHandling(output, matcher, condition);
        }

        public static void changeStackRecycling(Recipe recipe) {
            RecyclingHelper.changeStackRecycling(recipe.getOutputs(), recipe.getInputs());
        }

        public static void changeStackRecyclingNBT(Recipe recipe, NBTMatcher matcher, NBTCondition condition) {
            if (RecyclingHelper.changeStackRecycling(recipe.getOutputs(), recipe.getInputs())) {
                LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerNBTHandling(recipe.getOutputs().get(0),
                        matcher, condition);
            }
        }

        /*
         * Recipe Search + Recycling Helpers
         * These Helpers have an output, map, (and a predicate) input, use them to find a recipe, then use that recipe
         * to change the output's recycling.
         */
        public static void changeStackRecycling(ItemStack output, VirtualizedRecipeMap map) {
            changeStackRecycling(output, map.getRecipeMap(), (r) -> true);
        }

        public static void changeStackRecyclingNBT(ItemStack output, VirtualizedRecipeMap map, NBTMatcher matcher,
                                                   NBTCondition condition) {
            changeStackRecyclingNBT(output, map.getRecipeMap(), (r) -> true, matcher, condition);
        }

        public static void changeStackRecycling(ItemStack output, VirtualizedRecipeMap map,
                                                Predicate<Recipe> acceptedRecipe) {
            RecyclingHelper.changeStackRecycling(output, map.getRecipeMap(), acceptedRecipe);
        }

        public static void changeStackRecyclingNBT(ItemStack output, VirtualizedRecipeMap map,
                                                   Predicate<Recipe> acceptedRecipe, NBTMatcher matcher,
                                                   NBTCondition condition) {
            if (RecyclingHelper.changeStackRecycling(output, map.getRecipeMap(), acceptedRecipe)) {
                LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerNBTHandling(output, matcher, condition);
            }
        }

        public static void changeStackRecycling(ItemStack output, RecipeMap<?> map) {
            changeStackRecycling(output, map, (r) -> true);
        }

        public static void changeStackRecyclingNBT(ItemStack output, RecipeMap<?> map, NBTMatcher matcher,
                                                   NBTCondition condition) {
            changeStackRecyclingNBT(output, map, (r) -> true, matcher, condition);
        }

        public static void changeStackRecycling(ItemStack output, RecipeMap<?> map, Predicate<Recipe> acceptedRecipe) {
            RecyclingHelper.changeStackRecycling(output, map, acceptedRecipe);
        }

        public static void changeStackRecyclingNBT(ItemStack output, RecipeMap<?> map, Predicate<Recipe> acceptedRecipe,
                                                   NBTMatcher matcher, NBTCondition condition) {
            if (RecyclingHelper.changeStackRecycling(output, map, acceptedRecipe)) {
                LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER.registerNBTHandling(output, matcher, condition);
            }
        }

        public static void removeStackRecycling(ItemStack output) {
            RecyclingHelper.changeStackRecycling(output, Collections.emptyList());
        }
    }

    public static class ChangeCompositionHelpers {

        public static CompositionBuilder changeComposition(Material material) {
            return new CompositionBuilder(material);
        }

        public static CompositionBuilder replaceComposition(MaterialStack material) {
            return new CompositionBuilder(material.material);
        }

        public static CompositionBuilder changeComposition(ItemStack stack) {
            MaterialStack mat = CompositionBuilder.getMatFromStack(stack);
            if (mat == null) return new CompositionBuilder(
                    GregTechAPI.materialManager.getRegistry(GTValues.MODID).getFallbackMaterial());

            return new CompositionBuilder(mat.material);
        }

        public static CompositionBuilder changeComposition(FluidStack fluid) {
            MaterialStack mat = CompositionBuilder.getMatFromFluid(fluid);
            if (mat == null) return new CompositionBuilder(
                    GregTechAPI.materialManager.getRegistry(GTValues.MODID).getFallbackMaterial());

            return new CompositionBuilder(mat.material);
        }
    }

    public static class GTRecipeHelpers {

        public static ChancedItemOutput chanced(ItemStack stack, int chance, int chanceBoost) {
            return new ChancedItemOutput(stack, chance, chanceBoost);
        }

        public static ChancedFluidOutput chanced(FluidStack fluid, int chance, int chanceBoost) {
            return new ChancedFluidOutput(fluid, chance, chanceBoost);
        }

        @Contract("_ -> new")
        public static GTRecipeInput toGtInput(IIngredient ingredient) {
            // noinspection Contract
            return RecipeBuilderAccessor.ofGroovyIngredient(ingredient);
        }
    }

    public static class KeyBindingHelpers {

        public static void addOverride(String id, int keyCode) {
            addOverride(id, KeyModifier.NONE, keyCode);
        }

        public static void addOverride(String id, KeyModifier modifier, int keyCode) {
            LabsVirtualizedRegistries.KEYBIND_OVERRIDES_MANAGER.addOverride(id, modifier, keyCode);
        }
    }

    public static class MiscHelpers {

        public static void removeDraconicFusionRecipe(ItemStack catalyst, ItemStack result) {
            if (!Loader.isModLoaded(LabsValues.DRACONIC_MODID)) return;

            // noinspection SimplifyStreamApiCallChains
            for (IFusionRecipe recipe : RecipeManager.FUSION_REGISTRY.getRecipes().stream()
                    .filter(x -> x.getRecipeCatalyst().isItemEqual(catalyst) &&
                            x.getRecipeOutput(catalyst).isItemEqual(result))
                    .collect(Collectors.toList())) {
                ModSupport.DRACONIC_EVOLUTION.get().fusion.remove(recipe);
            }
        }
    }

    public static class NBTClearingRecipeHelpers {

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack item) {
            return nbtClearingRecipe(item, item, null);
        }

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack item, @Nullable Consumer<ItemStack> clearer) {
            return nbtClearingRecipe(item, item, clearer);
        }

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack input, ItemStack output) {
            return nbtClearingRecipe(input, output, null);
        }

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack input, ItemStack exampleOutput,
                                                          @Nullable Consumer<ItemStack> clearer) {
            return nbtClearingRecipe(input, exampleOutput, clearer, NBTClearingRecipe.CAN_CLEAR_TOOLTIP,
                    NBTClearingRecipe.WARNING_TOOLTIP);
        }

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack item, LabsTranslate.Translatable tooltip) {
            return nbtClearingRecipe(item, item, null, tooltip);
        }

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack item, @Nullable Consumer<ItemStack> clearer,
                                                          LabsTranslate.Translatable canClearTooltip,
                                                          LabsTranslate.Translatable warningTooltip) {
            return nbtClearingRecipe(item, item, clearer, canClearTooltip, warningTooltip);
        }

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack input, ItemStack output,
                                                          LabsTranslate.Translatable canClearTooltip,
                                                          LabsTranslate.Translatable warningTooltip) {
            return nbtClearingRecipe(input, output, null, canClearTooltip, warningTooltip);
        }

        public static NBTClearingRecipe nbtClearingRecipe(ItemStack input, ItemStack exampleOutput,
                                                          @Nullable Consumer<ItemStack> clearer,
                                                          LabsTranslate.Translatable canClearTooltip,
                                                          LabsTranslate.Translatable warningTooltip) {
            ResourceLocation name = RecipeName.generateRl("nomilabs_nbt_clearing");

            GroovyLog.Msg msg = GroovyLog.msg("Error adding Minecraft Shaped Crafting recipe '{}'", name).error()
                    .add(IngredientHelper.isEmpty(input), () -> "Input must not be empty")
                    .add(IngredientHelper.isEmpty(exampleOutput), () -> "Output must not be empty")
                    .add(ReloadableRegistryManager.hasNonDummyRecipe(name),
                            () -> "a recipe with that name already exists! Remove the recipe first");
            if (msg.postIfNotEmpty()) return null;

            input = input.copy();
            input.setCount(1);
            input.setTagCompound(null);
            exampleOutput = exampleOutput.copy();
            exampleOutput.setCount(1);
            exampleOutput.setTagCompound(null);

            var recipe = new NBTClearingRecipe(input, exampleOutput, clearer);
            NBTClearingRecipe.NBT_CLEARERS
                    .computeIfAbsent(new ItemMeta(exampleOutput), (key) -> new Object2ObjectOpenHashMap<>())
                    .put(new ItemMeta(input), warningTooltip);
            ReloadableRegistryManager.addRegistryEntry(ForgeRegistries.RECIPES, name, recipe);
            TooltipHelpers.addTooltip(input, canClearTooltip);
            return recipe;
        }

        /**
         * The original ItemStack will be modified. No new ItemSTack will be created.
         * <p>
         * Only tags provided will be transferred. All other tags will be deleted.
         */
        public static NBTTagCompound transferSubTags(ItemStack orig, String... keys) {
            return transferSubTags(orig, null, keys);
        }

        /**
         * The original ItemStack will be modified. No new ItemSTack will be created.
         * <p>
         * Only tags provided will be transferred. All other tags will be deleted.
         */
        public static NBTTagCompound transferSubTags(ItemStack orig, @Nullable NBTTagCompound existing,
                                                     String... keys) {
            var origCompound = orig.getTagCompound();
            if (origCompound == null) return existing;

            var tagCompound = existing == null ? new NBTTagCompound() : existing;
            for (var key : keys) {
                if (origCompound.hasKey(key) && !origCompound.getTag(key).isEmpty())
                    tagCompound.setTag(key, origCompound.getTag(key));
            }

            return tagCompound;
        }

        /**
         * The original ItemStack will be modified. No new ItemSTack will be created.
         * <p>
         * Tag provided at end of path will be transferred, if all tags along the path exist, and all, excluding the
         * final tag, are tag compounds.
         */
        public static NBTTagCompound transferTagAtPath(ItemStack orig, String... path) {
            return transferTagAtPath(orig, null, path);
        }

        /**
         * The original ItemStack will be modified. No new ItemSTack will be created.
         * <p>
         * Tag provided at end of path will be transferred, if all tags along the path exist, and all, excluding the
         * final tag, are tag compounds.
         */
        public static NBTTagCompound transferTagAtPath(ItemStack orig, @Nullable NBTTagCompound existing,
                                                       String... path) {
            var origCompound = orig.getTagCompound();
            if (origCompound == null) return existing;

            var linkedPath = new LinkedList<>(Arrays.asList(path));
            NBTBase tag = findTagAtPath(origCompound, new LinkedList<>(linkedPath));

            if (tag == null || tag.isEmpty()) return existing;
            var compound = existing == null ? new NBTTagCompound() : existing;

            addTagAtPath(compound, tag, linkedPath);

            return compound;
        }

        /**
         * Only should be used for drawers (from Storage Drawers, Framed Compacting Drawers, GregTech Drawers, etc.).
         * Makes use of custom Labs mixins and nbt, so that the drawer does not appear taped.
         * <p>
         * Confirmed to Work on:
         * <ul>
         * <li>Storage Drawers: Wooden Drawers, Compacting Drawers, Framed Drawers</li>
         * <li>GregTech Drawers: Rubber Wood Drawers, Treated Wood Drawers</li>
         * <li>Framed Compacting Drawers: Framed Compacting Drawers</li>
         * </ul>
         */
        public static NBTTagCompound transferDrawerUpgradeData(ItemStack orig, @Nullable NBTTagCompound existing) {
            var origCompound = orig.getTagCompound();
            if (origCompound == null) return existing;

            var linkedPath = new LinkedList<>(Arrays.asList("tile", "Upgrades"));
            NBTBase tag = findTagAtPath(origCompound, linkedPath);

            if (!(tag instanceof NBTTagList) || tag.isEmpty()) return existing;
            var compound = existing == null ? new NBTTagCompound() : existing;

            var storage = new NBTTagCompound();
            storage.setTag("Upgrades", tag);

            compound.setTag(CustomUpgradeHandler.CUSTOM_UPGRADES, storage);
            return compound;
        }

        private static void addTagAtPath(@NotNull NBTTagCompound compound, @NotNull NBTBase add,
                                         LinkedList<String> path) {
            if (path.isEmpty()) return;

            String key = path.pollFirst();
            if (path.isEmpty()) {
                compound.setTag(key, add);
                return;
            }

            NBTTagCompound next = compound.getCompoundTag(key);
            addTagAtPath(next, add, path);
            compound.setTag(key, next);
        }

        @Nullable
        private static NBTBase findTagAtPath(@NotNull NBTTagCompound search, LinkedList<String> path) {
            if (search.isEmpty() || path.isEmpty()) return null;

            String key = path.pollFirst();
            if (!search.hasKey(key)) return null;
            if (path.isEmpty()) return search.getTag(key);

            if (!search.hasKey(key, Constants.NBT.TAG_COMPOUND)) return null;
            return findTagAtPath(search.getCompoundTag(key), path);
        }
    }
}
