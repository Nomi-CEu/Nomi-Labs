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
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.nomiceu.nomilabs.groovy.PartialRecipe;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.util.ItemTagMeta;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@mezz.jei.api.JEIPlugin
@SuppressWarnings("unused")
@GroovyBlacklist
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation WILDCARD_LOCATION = new ResourceLocation("*", "*");
    private static final Map<ItemTagMeta, List<Translatable>> DESCRIPTIONS = new HashMap<>();
    private static final Map<ItemTagMeta, List<Translatable>> GROOVY_DESCRIPTIONS = new HashMap<>();
    private static final Table<ItemTagMeta, ResourceLocation, List<Translatable>> RECIPE_OUTPUT_TOOLTIPS = HashBasedTable
            .create();
    private static final Table<ItemTagMeta, ResourceLocation, List<Translatable>> GROOVY_RECIPE_OUTPUT_TOOLTIPS = HashBasedTable
            .create();
    private static final List<Pair<ItemStack, Function<NBTTagCompound, Boolean>>> IGNORE_NBT_HIDE = new ArrayList<>();

    private static Table<ItemTagMeta, ResourceLocation, List<Translatable>> COMPILED_RECIPE_OUTPUT_TOOLTIPS = null;

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
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        // Remove Info Item from JEI
        itemRegistry.removeIngredientsAtRuntime(VanillaTypes.ITEM,
                Collections.singletonList(new ItemStack(LabsItems.INFO_ITEM)));
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

    public static void addRecipeOutputTooltip(@NotNull ItemStack stack, Translatable... tooltip) {
        addRecipeOutputTooltip(stack, WILDCARD_LOCATION, tooltip);
    }

    public static void addRecipeOutputTooltip(@NotNull ItemStack stack, ResourceLocation recipeName,
                                              Translatable... tooltip) {
        addRecipeOutputTooltip(RECIPE_OUTPUT_TOOLTIPS, new ItemTagMeta(stack), recipeName,
                (list) -> Collections.addAll(list, tooltip));
    }

    public static void addGroovyRecipeOutputTooltip(@NotNull ItemStack stack, Translatable... tooltip) {
        addGroovyRecipeOutputTooltip(stack, WILDCARD_LOCATION, tooltip);
    }

    public static void addGroovyRecipeOutputTooltip(@NotNull ItemStack stack, ResourceLocation recipeName,
                                                    Translatable... tooltip) {
        addRecipeOutputTooltip(GROOVY_RECIPE_OUTPUT_TOOLTIPS, new ItemTagMeta(stack), recipeName,
                (list) -> Collections.addAll(list, tooltip));
    }

    private static void addRecipeOutputTooltip(Table<ItemTagMeta, ResourceLocation, List<Translatable>> table,
                                               @NotNull ItemTagMeta stack, ResourceLocation recipeName,
                                               Consumer<List<Translatable>> addToList) {
        var list = table.get(stack, recipeName);
        if (list == null) list = new ArrayList<>();
        addToList.accept(list);
        table.put(stack, recipeName, list);
    }

    private static void cacheRecipeOutputTooltip() {
        if (COMPILED_RECIPE_OUTPUT_TOOLTIPS != null) return;

        COMPILED_RECIPE_OUTPUT_TOOLTIPS = HashBasedTable.create(RECIPE_OUTPUT_TOOLTIPS);
        GROOVY_RECIPE_OUTPUT_TOOLTIPS.cellSet()
                .forEach((cell) -> addRecipeOutputTooltip(COMPILED_RECIPE_OUTPUT_TOOLTIPS,
                        Objects.requireNonNull(cell.getRowKey()),
                        cell.getColumnKey(),
                        (list) -> list.addAll(Objects.requireNonNull(cell.getValue()))));
    }

    public static List<String> getRecipeOutputTooltip(ItemStack stack, ResourceLocation recipeName) {
        cacheRecipeOutputTooltip();

        var itemTagMeta = new ItemTagMeta(stack);
        var specific = COMPILED_RECIPE_OUTPUT_TOOLTIPS.get(itemTagMeta, recipeName);
        if (specific != null) return specific.stream().map(Translatable::translate).collect(Collectors.toList());

        var wildcard = COMPILED_RECIPE_OUTPUT_TOOLTIPS.get(itemTagMeta, WILDCARD_LOCATION);
        if (wildcard != null) return wildcard.stream().map(Translatable::translate).collect(Collectors.toList());
        return new ArrayList<>();
    }

    public static void onReload() {
        GROOVY_DESCRIPTIONS.clear();
        GROOVY_RECIPE_OUTPUT_TOOLTIPS.clear();
        IGNORE_NBT_HIDE.clear();
        COMPILED_RECIPE_OUTPUT_TOOLTIPS = null;
    }
}
