package com.nomiceu.nomilabs.integration.jei;

import static com.nomiceu.nomilabs.util.LabsTranslate.Translatable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.groovy.PartialRecipe;
import com.nomiceu.nomilabs.groovy.mixinhelper.LabsJEIApplied;
import com.nomiceu.nomilabs.integration.jei.mixinhelper.AccessibleModRegistry;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.util.ItemTagMeta;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@mezz.jei.api.JEIPlugin
@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsJEIPlugin implements IModPlugin {

    private static final ResourceLocation WILDCARD_LOCATION = new ResourceLocation("*", "*");

    private static final Map<ItemTagMeta, List<Translatable>> DESCRIPTIONS = new HashMap<>();
    private static final Map<ItemTagMeta, List<Translatable>> GROOVY_DESCRIPTIONS = new HashMap<>();

    private static final Map<ResourceLocation, List<Translatable>> RECIPE_OUTPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static final Map<ResourceLocation, List<Translatable>> GROOVY_RECIPE_OUTPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static Map<ResourceLocation, List<Translatable>> COMPILED_RECIPE_OUTPUT_TOOLTIPS = null;

    private static final Map<ResourceLocation, List<Translatable>[]> RECIPE_INPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static final Map<ResourceLocation, List<Translatable>[]> GROOVY_RECIPE_INPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static Map<ResourceLocation, List<Translatable>[]> COMPILED_RECIPE_INPUT_TOOLTIPS = null;

    private static final List<Pair<ItemStack, Function<NBTTagCompound, Boolean>>> IGNORE_NBT_HIDE = new ArrayList<>();
    private static final Map<String, List<Object>> CATALYST_OVERRIDE = new Object2ObjectOpenHashMap<>();

    private static IIngredientRegistry itemRegistry;

    @Override
    public void register(IModRegistry registry) {
        var jeiHelpers = registry.getJeiHelpers();
        itemRegistry = registry.getIngredientRegistry();

        // JEI does not recognise Custom Recipe Classes on its own
        registry.handleRecipes(PartialRecipe.class, recipe -> new PartialRecipeWrapper(jeiHelpers, recipe),
                VanillaRecipeCategoryUid.CRAFTING);

        // Add Descriptions
        Map<ItemTagMeta, List<Translatable>> tempMap = new HashMap<>(DESCRIPTIONS);
        GROOVY_DESCRIPTIONS.forEach(((key, value) -> addDescription(tempMap, key, (list) -> list.addAll(value))));
        tempMap.forEach(((itemTagMeta, strings) -> registry.addIngredientInfo(itemTagMeta.toStack(), VanillaTypes.ITEM,
                strings.stream().map(Translatable::translate).collect(Collectors.joining("\n\n")))));

        // GrS JEI Fix
        LabsJEIApplied.afterRegisterApplied = false;
    }

    public static void afterModRegisters(IModRegistry registry) {
        for (var override : CATALYST_OVERRIDE.entrySet()) {
            ((AccessibleModRegistry) registry).labs$replaceRecipeCatalyst(override.getKey(), override.getValue());
        }
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        // Remove Info Item from JEI
        itemRegistry.removeIngredientsAtRuntime(VanillaTypes.ITEM,
                Collections.singletonList(new ItemStack(LabsItems.INFO_ITEM)));

        // GrS JEI Fix
        LabsJEIApplied.afterRuntimeApplied = false;
    }

    /* Hiding Helpers */
    public static void hideItemNBTMatch(ItemStack itemStack, Function<NBTTagCompound, Boolean> condition) {
        IGNORE_NBT_HIDE.add(Pair.of(itemStack, condition));
    }

    public static void removeAndHideItemNBTMatch(ItemStack itemStack, Function<NBTTagCompound, Boolean> condition) {
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            var output = recipe.getRecipeOutput();
            if (recipe.getRegistryName() != null && output.getItem() == itemStack.getItem() &&
                    output.getMetadata() == itemStack.getMetadata() && condition.apply(output.getTagCompound()))
                ReloadableRegistryManager.removeRegistryEntry(ForgeRegistries.RECIPES, recipe.getRegistryName());
        }

        IGNORE_NBT_HIDE.add(Pair.of(itemStack, condition));
    }

    public static List<Pair<ItemStack, Function<NBTTagCompound, Boolean>>> getIgnoreNbtHide() {
        return ImmutableList.copyOf(IGNORE_NBT_HIDE);
    }

    /* Descriptions */
    public static void addDescription(@NotNull ItemStack stack, Translatable... description) {
        addDescription(DESCRIPTIONS, new ItemTagMeta(stack), (list) -> Collections.addAll(list, description));
    }

    public static void addGroovyDescription(@NotNull ItemStack stack, Translatable... description) {
        addDescription(GROOVY_DESCRIPTIONS, new ItemTagMeta(stack), (list) -> Collections.addAll(list, description));
    }

    private static void addDescription(Map<ItemTagMeta, List<Translatable>> map,
                                       @NotNull ItemTagMeta stack, Consumer<List<Translatable>> addToList) {
        map.computeIfAbsent(stack, (k) -> new ArrayList<>());
        addToList.accept(map.get(stack));
    }

    /* Recipe Output Tooltip */
    public static void addRecipeOutputTooltip(ResourceLocation recipeName,
                                              Translatable... tooltip) {
        addRecipeOutputTooltip(RECIPE_OUTPUT_TOOLTIPS, recipeName,
                (list) -> Collections.addAll(list, tooltip));
    }

    public static void addGroovyRecipeOutputTooltip(ResourceLocation recipeName,
                                                    Translatable... tooltip) {
        addRecipeOutputTooltip(GROOVY_RECIPE_OUTPUT_TOOLTIPS, recipeName,
                (list) -> Collections.addAll(list, tooltip));
    }

    private static void addRecipeOutputTooltip(Map<ResourceLocation, List<Translatable>> map,
                                               ResourceLocation recipeName, Consumer<List<Translatable>> addToList) {
        addToList.accept(map.computeIfAbsent(recipeName, k -> new ArrayList<>()));
    }

    private static void cacheRecipeOutputTooltip() {
        if (COMPILED_RECIPE_OUTPUT_TOOLTIPS != null) return;

        COMPILED_RECIPE_OUTPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>(RECIPE_OUTPUT_TOOLTIPS);
        GROOVY_RECIPE_OUTPUT_TOOLTIPS.forEach((key, value) -> addRecipeOutputTooltip(COMPILED_RECIPE_OUTPUT_TOOLTIPS,
                Objects.requireNonNull(key),
                (list) -> list.addAll(value)));
    }

    public static List<String> getRecipeOutputTooltip(ResourceLocation recipeName) {
        cacheRecipeOutputTooltip();

        var result = COMPILED_RECIPE_OUTPUT_TOOLTIPS.get(recipeName);
        if (result == null) return new ArrayList<>();
        return result.stream().map(Translatable::translate).collect(Collectors.toList());
    }

    /* Recipe Input Tooltip */
    public static void addRecipeInputTooltip(@NotNull ResourceLocation recipeName, int slotIndex,
                                             Translatable... tooltip) {
        if (slotIndex < 0 || slotIndex > 8)
            throw new IllegalArgumentException("Add Recipe Input Tooltip: Slot Index must be between 0 and 8!");

        addRecipeInputTooltip(RECIPE_INPUT_TOOLTIPS, recipeName, slotIndex,
                (list) -> Collections.addAll(list, tooltip));
    }

    public static void addGroovyRecipeInputTooltip(@NotNull ResourceLocation recipeName, int slotIndex,
                                                   Translatable... tooltip) {
        if (slotIndex < 0 || slotIndex > 8) {
            GroovyLog.get().error("Add Recipe Input Tooltip: Slot Index must be between 0 and 8!");
            return;
        }

        addRecipeInputTooltip(GROOVY_RECIPE_INPUT_TOOLTIPS, recipeName, slotIndex,
                (list) -> Collections.addAll(list, tooltip));
    }

    private static void addRecipeInputTooltip(Map<ResourceLocation, List<Translatable>[]> map,
                                              @NotNull ResourceLocation recipeName, int slotIndex,
                                              Consumer<List<Translatable>> addToList) {
        // noinspection unchecked
        var recipeTooltips = map.computeIfAbsent(recipeName, (k) -> (List<Translatable>[]) new List<?>[9]);
        var existingTranslations = recipeTooltips[slotIndex];

        if (existingTranslations == null) existingTranslations = new ArrayList<>();
        addToList.accept(existingTranslations);
        recipeTooltips[slotIndex] = existingTranslations;
    }

    private static void cacheRecipeInputTooltips() {
        if (COMPILED_RECIPE_INPUT_TOOLTIPS != null) return;

        COMPILED_RECIPE_INPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>(RECIPE_INPUT_TOOLTIPS);
        GROOVY_RECIPE_INPUT_TOOLTIPS.forEach((key, value) -> {
            for (int i = 0; i < value.length; i++) {
                var tooltips = value[i];
                if (tooltips == null) continue;
                addRecipeInputTooltip(COMPILED_RECIPE_INPUT_TOOLTIPS, key, i, (list) -> list.addAll(tooltips));
            }
        });
    }

    public static List<String> getRecipeInputTooltip(ResourceLocation recipeName, int slotIndex) {
        cacheRecipeInputTooltips();

        if (!COMPILED_RECIPE_INPUT_TOOLTIPS.containsKey(recipeName)) return new ArrayList<>();

        var tooltips = COMPILED_RECIPE_INPUT_TOOLTIPS.get(recipeName)[slotIndex];
        if (tooltips == null) return new ArrayList<>();
        return tooltips.stream().map(Translatable::translate).collect(Collectors.toList());
    }

    public static void addRecipeCatalystOverride(String category, Object... catalyst) {
        List<Object> result = new ArrayList<>();
        Collections.addAll(result, catalyst);
        CATALYST_OVERRIDE.put(category, result);
    }

    public static void onReload() {
        GROOVY_DESCRIPTIONS.clear();
        GROOVY_RECIPE_OUTPUT_TOOLTIPS.clear();
        GROOVY_RECIPE_INPUT_TOOLTIPS.clear();
        IGNORE_NBT_HIDE.clear();
        CATALYST_OVERRIDE.clear();
        COMPILED_RECIPE_OUTPUT_TOOLTIPS = null;
        COMPILED_RECIPE_INPUT_TOOLTIPS = null;
    }
}
