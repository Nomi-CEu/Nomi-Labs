package com.nomiceu.nomilabs.integration.jei;

import static appeng.items.misc.ItemCrystalSeed.*;
import static com.nomiceu.nomilabs.integration.jei.recipe.ChargerRecipeHandler.ChargerRecipe;
import static com.nomiceu.nomilabs.integration.jei.recipe.CrystalGrowthRecipeHandler.*;
import static com.nomiceu.nomilabs.util.LabsTranslate.Translatable;
import static com.nomiceu.nomilabs.util.LabsTranslate.translate;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.bdew.ae2stuff.machines.grower.BlockGrower;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.groovy.PartialRecipe;
import com.nomiceu.nomilabs.integration.jei.recipe.ChargerCategory;
import com.nomiceu.nomilabs.integration.jei.recipe.ChargerRecipeHandler;
import com.nomiceu.nomilabs.integration.jei.recipe.CrystalGrowthCategory;
import com.nomiceu.nomilabs.integration.jei.recipe.CrystalGrowthRecipeHandler;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.util.LabsSide;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.definitions.IMaterials;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@mezz.jei.api.JEIPlugin
@SuppressWarnings("unused")
@GroovyBlacklist
public class LabsJEIPlugin implements IModPlugin {

    private static final ResourceLocation WILDCARD_LOCATION = new ResourceLocation("*", "*");

    private static final Map<ResourceLocation, List<Translatable>> RECIPE_OUTPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static final Map<ResourceLocation, List<Translatable>> GROOVY_RECIPE_OUTPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static Map<ResourceLocation, List<Translatable>> COMPILED_RECIPE_OUTPUT_TOOLTIPS = null;

    private static final Map<ResourceLocation, List<Translatable>[]> RECIPE_INPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static final Map<ResourceLocation, List<Translatable>[]> GROOVY_RECIPE_INPUT_TOOLTIPS = new Object2ObjectOpenHashMap<>();
    private static Map<ResourceLocation, List<Translatable>[]> COMPILED_RECIPE_INPUT_TOOLTIPS = null;

    private static final List<Pair<ItemStack, Function<NBTTagCompound, Boolean>>> IGNORE_NBT_HIDE = new ArrayList<>();

    private static IIngredientRegistry itemRegistry;

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        if (Loader.isModLoaded(LabsValues.AE2_MODID)) {
            registry.addRecipeCategories(new ChargerCategory(registry.getJeiHelpers().getGuiHelper()));
        }

        if (Loader.isModLoaded(LabsValues.AE2_STUFF_MODID)) {
            registry.addRecipeCategories(new CrystalGrowthCategory(registry.getJeiHelpers().getGuiHelper()));
        }
    }

    @Override
    public void register(IModRegistry registry) {
        var jeiHelpers = registry.getJeiHelpers();
        itemRegistry = registry.getIngredientRegistry();

        if (Loader.isModLoaded(LabsValues.AE2_MODID)) {
            registerChargerRecipes(registry);
        }

        if (Loader.isModLoaded(LabsValues.AE2_STUFF_MODID)) {
            registerCrystalGrowthRecipes(registry);
        }

        // JEI does not recognise Custom Recipe Classes on its own
        registry.handleRecipes(PartialRecipe.class, recipe -> new PartialRecipeWrapper(jeiHelpers, recipe),
                VanillaRecipeCategoryUid.CRAFTING);

        // Add Descriptions for Hand Framing
        registry.addIngredientInfo(new ItemStack(LabsItems.HAND_FRAMING_TOOL), VanillaTypes.ITEM,
                translate("item.nomilabs.hand_framing_tool.desc1") + "\n\n" +
                        translate("item.nomilabs.hand_framing_tool.desc2") + "\n\n" +
                        translate("item.nomilabs.hand_framing_tool.desc3") + "\n\n" +
                        translate("item.nomilabs.hand_framing_tool.desc4") + "\n\n" +
                        translate("item.nomilabs.hand_framing_tool.desc5") + "\n\n" +
                        translate("item.nomilabs.hand_framing_tool.desc6"));
    }

    public static void registerChargerRecipes(IModRegistry registry) {
        // Register Recipe Handling & Icon/Catalyst
        registry.handleRecipes(ChargerRecipe.class, new ChargerRecipeHandler(), CrystalGrowthCategory.UID);

        IDefinitions defs = AEApi.instance().definitions();

        Optional<ItemStack> charger = defs.blocks().charger().maybeStack(1);
        charger.ifPresent(itemStack -> registry.addRecipeCatalyst(itemStack, ChargerCategory.UID));

        // Register Recipe
        Optional<ItemStack> certus = defs.materials().certusQuartzCrystal().maybeStack(1);
        Optional<ItemStack> charged = defs.materials().certusQuartzCrystalCharged().maybeStack(1);

        if (certus.isPresent() && charged.isPresent())
            registry.addRecipes(Collections.singletonList(new ChargerRecipe(certus.get(), charged.get())),
                    ChargerCategory.UID);
    }

    public static void registerCrystalGrowthRecipes(IModRegistry registry) {
        // Register Recipe Handling & Icon/Catalyst
        registry.handleRecipes(CrystalGrowthRecipe.class, new CrystalGrowthRecipeHandler(), CrystalGrowthCategory.UID);

        // Reference the original block object by calling default state and then getting block
        // Should be fast, since all used methods are cached
        registry.addRecipeCatalyst(new ItemStack(BlockGrower.getDefaultState().getBlock()), CrystalGrowthCategory.UID);

        // Register Recipes
        List<CrystalGrowthRecipe> recipes = new ArrayList<>(4);

        IDefinitions definitions = AEApi.instance().definitions();
        IMaterials materials = AEApi.instance().definitions().materials();

        Optional<ItemStack> fluix = materials.fluixCrystal().maybeStack(2);
        Optional<ItemStack> certus = materials.certusQuartzCrystal().maybeStack(1);

        if (fluix.isPresent() && certus.isPresent()) {
            recipes.add(createRecipe(ImmutableList.of(
                    certus.get(),
                    new ItemStack(Items.QUARTZ),
                    new ItemStack(Items.REDSTONE)), fluix.get()));
        }

        Optional<Item> seed = definitions.items().crystalSeed().maybeItem();

        if (seed.isPresent()) {
            Item seedItem = seed.get();

            Optional<ItemStack> pureCertus = materials.purifiedCertusQuartzCrystal().maybeStack(1);
            pureCertus.ifPresent(itemStack -> recipes.add(
                    createRecipe(ImmutableList.of(setSeedProgress(seedItem, CERTUS)), itemStack)));

            Optional<ItemStack> pureNether = materials.purifiedNetherQuartzCrystal().maybeStack(1);
            pureNether.ifPresent(itemStack -> recipes.add(
                    createRecipe(ImmutableList.of(setSeedProgress(seedItem, NETHER)), itemStack)));

            Optional<ItemStack> pureFluix = materials.purifiedFluixCrystal().maybeStack(1);
            pureFluix.ifPresent(itemStack -> recipes.add(
                    createRecipe(ImmutableList.of(setSeedProgress(seedItem, FLUIX)), itemStack)));
        }

        registry.addRecipes(recipes, CrystalGrowthCategory.UID);
    }

    public static ItemStack setSeedProgress(Item seed, int progress) {
        var stack = new ItemStack(seed, 1, progress);

        var tag = new NBTTagCompound();
        tag.setInteger("progress", progress);
        stack.setTagCompound(tag);

        return stack;
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        // Remove Info Item from JEI
        itemRegistry.removeIngredientsAtRuntime(VanillaTypes.ITEM,
                Collections.singletonList(new ItemStack(LabsItems.INFO_ITEM)));
    }

    /* Hiding Helpers */
    public static void hideItemNBTMatch(ItemStack itemStack, Function<NBTTagCompound, Boolean> condition) {
        if (!LabsSide.isClient()) return;
        IGNORE_NBT_HIDE.add(Pair.of(itemStack, condition));
    }

    public static void removeAndHideItemNBTMatch(ItemStack itemStack, Function<NBTTagCompound, Boolean> condition) {
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            var output = recipe.getRecipeOutput();
            if (recipe.getRegistryName() != null && output.getItem() == itemStack.getItem() &&
                    output.getMetadata() == itemStack.getMetadata() && condition.apply(output.getTagCompound()))
                ReloadableRegistryManager.removeRegistryEntry(ForgeRegistries.RECIPES, recipe.getRegistryName());
        }

        hideItemNBTMatch(itemStack, condition);
    }

    public static List<Pair<ItemStack, Function<NBTTagCompound, Boolean>>> getIgnoreNbtHide() {
        return ImmutableList.copyOf(IGNORE_NBT_HIDE);
    }

    /* Recipe Output Tooltip */
    public static void addRecipeOutputTooltip(ResourceLocation recipeName,
                                              Translatable... tooltip) {
        if (!LabsSide.isClient()) return;
        addRecipeOutputTooltip(RECIPE_OUTPUT_TOOLTIPS, recipeName,
                (list) -> Collections.addAll(list, tooltip));
    }

    public static void addGroovyRecipeOutputTooltip(ResourceLocation recipeName,
                                                    Translatable... tooltip) {
        if (!LabsSide.isClient()) return;
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
        if (!LabsSide.isClient()) return;
        if (slotIndex < 0 || slotIndex > 8)
            throw new IllegalArgumentException("Add Recipe Input Tooltip: Slot Index must be between 0 and 8!");

        addRecipeInputTooltip(RECIPE_INPUT_TOOLTIPS, recipeName, slotIndex,
                (list) -> Collections.addAll(list, tooltip));
    }

    public static void addGroovyRecipeInputTooltip(@NotNull ResourceLocation recipeName, int slotIndex,
                                                   Translatable... tooltip) {
        if (!LabsSide.isClient()) return;
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

    public static void onReload() {
        GROOVY_RECIPE_OUTPUT_TOOLTIPS.clear();
        GROOVY_RECIPE_INPUT_TOOLTIPS.clear();
        IGNORE_NBT_HIDE.clear();
        COMPILED_RECIPE_OUTPUT_TOOLTIPS = null;
        COMPILED_RECIPE_INPUT_TOOLTIPS = null;
    }
}
