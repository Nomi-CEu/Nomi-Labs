package com.nomiceu.nomilabs.integration.jei;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.nomiceu.nomilabs.groovy.PartialRecipe;
import com.nomiceu.nomilabs.util.ItemTagMeta;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@mezz.jei.api.JEIPlugin
@SuppressWarnings("unused")
@GroovyBlacklist
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation WILDCARD_LOCATION = new ResourceLocation("*", "*");
    private static final Map<ItemTagMeta, List<String>> DESCRIPTIONS = new HashMap<>();
    private static final Map<ItemTagMeta, List<String>> GROOVY_DESCRIPTIONS = new HashMap<>();
    private static final Table<ItemTagMeta, ResourceLocation, List<String>> RECIPE_OUTPUT_TOOLTIPS = HashBasedTable
            .create();
    private static final Table<ItemTagMeta, ResourceLocation, List<String>> GROOVY_RECIPE_OUTPUT_TOOLTIPS = HashBasedTable
            .create();
    private static final List<Pair<ItemStack, Function<NBTTagCompound, Boolean>>> IGNORE_NBT_HIDE = new ArrayList<>();

    @Override
    public void register(IModRegistry registry) {
        var jeiHelpers = registry.getJeiHelpers();

        // JEI does not recognise Custom Recipe Classes on its own
        registry.handleRecipes(PartialRecipe.class, recipe -> new PartialRecipeWrapper(jeiHelpers, recipe),
                VanillaRecipeCategoryUid.CRAFTING);

        // Add Descriptions
        Map<ItemTagMeta, List<String>> tempMap = new HashMap<>(DESCRIPTIONS);
        GROOVY_DESCRIPTIONS.forEach(((key, value) -> addDescription(tempMap, key, (list) -> list.addAll(value))));
        tempMap.forEach(((itemTagMeta, strings) -> registry.addIngredientInfo(itemTagMeta.toStack(), VanillaTypes.ITEM,
                String.join("\n\n", strings))));
    }

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

    public static void addDescription(@NotNull ItemStack stack, String... description) {
        addDescription(DESCRIPTIONS, new ItemTagMeta(stack), (list) -> Collections.addAll(list, description));
    }

    public static void addGroovyDescription(@NotNull ItemStack stack, String... description) {
        addDescription(GROOVY_DESCRIPTIONS, new ItemTagMeta(stack), (list) -> Collections.addAll(list, description));
    }

    private static void addDescription(Map<ItemTagMeta, List<String>> map,
                                       @NotNull ItemTagMeta stack, Consumer<List<String>> addToList) {
        map.computeIfAbsent(stack, (k) -> new ArrayList<>());
        addToList.accept(map.get(stack));
    }

    public static void addRecipeOutputTooltip(@NotNull ItemStack stack, String... tooltip) {
        addRecipeOutputTooltip(stack, WILDCARD_LOCATION, tooltip);
    }

    public static void addRecipeOutputTooltip(@NotNull ItemStack stack, ResourceLocation recipeName,
                                              String... tooltip) {
        addRecipeOutputTooltip(RECIPE_OUTPUT_TOOLTIPS, new ItemTagMeta(stack), recipeName,
                (list) -> Collections.addAll(list, tooltip));
    }

    public static void addGroovyRecipeOutputTooltip(@NotNull ItemStack stack, String... tooltip) {
        addGroovyRecipeOutputTooltip(stack, WILDCARD_LOCATION, tooltip);
    }

    public static void addGroovyRecipeOutputTooltip(@NotNull ItemStack stack, ResourceLocation recipeName,
                                                    String... tooltip) {
        addRecipeOutputTooltip(GROOVY_RECIPE_OUTPUT_TOOLTIPS, new ItemTagMeta(stack), recipeName,
                (list) -> Collections.addAll(list, tooltip));
    }

    private static void addRecipeOutputTooltip(Table<ItemTagMeta, ResourceLocation, List<String>> table,
                                               @NotNull ItemTagMeta stack, ResourceLocation recipeName,
                                               Consumer<List<String>> addToList) {
        var list = table.get(stack, recipeName);
        if (list == null) list = new ArrayList<>();
        addToList.accept(list);
        table.put(stack, recipeName, list);
    }

    public static List<String> getRecipeOutputTooltip(ItemStack stack, ResourceLocation recipeName) {
        var tempTable = HashBasedTable.create(RECIPE_OUTPUT_TOOLTIPS);
        GROOVY_RECIPE_OUTPUT_TOOLTIPS.cellSet()
                .forEach((cell) -> addRecipeOutputTooltip(tempTable, Objects.requireNonNull(cell.getRowKey()),
                        cell.getColumnKey(),
                        (list) -> list.addAll(Objects.requireNonNull(cell.getValue()))));
        var itemTagMeta = new ItemTagMeta(stack);
        var specific = tempTable.get(itemTagMeta, recipeName);
        if (specific != null) return specific;
        var wildcard = tempTable.get(itemTagMeta, WILDCARD_LOCATION);
        if (wildcard != null) return wildcard;
        return new ArrayList<>();
    }

    public static void onReload() {
        GROOVY_DESCRIPTIONS.clear();
        GROOVY_RECIPE_OUTPUT_TOOLTIPS.clear();
        IGNORE_NBT_HIDE.clear();
    }
}
