package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeMap;
import com.nomiceu.nomilabs.gregtech.mixinhelper.ParallelizedChancedOutputList;
import com.nomiceu.nomilabs.gregtech.mixinhelper.ParallelizedChancedOutputLogic;
import com.nomiceu.nomilabs.groovy.RecyclingHelper;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.category.GTRecipeCategory;
import gregtech.api.recipes.chance.output.ChancedOutput;
import gregtech.api.recipes.chance.output.ChancedOutputList;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;
import gregtech.api.recipes.chance.output.impl.ChancedFluidOutput;
import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.recipes.recipeproperties.IRecipePropertyStorage;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.ValidationResult;

/**
 * Adds some util functions to Recipe Builders. Also fixes an issue with parallelizing recipes with non-cached inputs.
 * Also, allows us to use our improved logic for parallelizing chanced outputs.
 */
@SuppressWarnings({ "unchecked", "DataFlowIssue" })
@Mixin(value = RecipeBuilder.class, remap = false)
public abstract class RecipeBuilderMixin<R extends RecipeBuilder<R>> {

    @Shadow
    @Final
    protected List<ItemStack> outputs;

    @Shadow
    @Final
    protected List<GTRecipeInput> inputs;

    @Shadow
    protected EnumValidationResult recipeStatus;

    @Shadow
    public abstract R inputNBT(GTRecipeInput input, NBTMatcher matcher, NBTCondition condition);

    @Shadow
    private static GTRecipeInput ofGroovyIngredient(IIngredient ingredient) {
        return null;
    }

    @Shadow
    protected RecipeMap<R> recipeMap;

    @Shadow
    @Final
    protected List<FluidStack> fluidOutputs;

    @Shadow
    @Final
    protected List<ChancedItemOutput> chancedOutputs;

    @Shadow
    @Final
    protected List<ChancedFluidOutput> chancedFluidOutputs;

    @Shadow
    protected int EUt;

    @Shadow
    protected GTRecipeCategory category;

    @Shadow
    public abstract R chancedOutput(ItemStack stack, int chance, int tierChanceBoost);

    @Shadow
    public abstract R chancedOutputLogic(@NotNull ChancedOutputLogic logic);

    @Shadow
    public abstract R chancedFluidOutput(FluidStack stack, int chance, int tierChanceBoost);

    @Shadow
    public abstract R chancedFluidOutputLogic(@NotNull ChancedOutputLogic logic);

    @Shadow
    protected ChancedOutputLogic chancedOutputLogic;
    @Shadow
    protected ChancedOutputLogic chancedFluidOutputLogic;

    @Shadow
    protected abstract EnumValidationResult finalizeAndValidate();

    @Shadow
    @Final
    protected List<GTRecipeInput> fluidInputs;
    @Shadow
    protected int duration;
    @Shadow
    protected boolean hidden;
    @Shadow
    protected boolean isCTRecipe;
    @Shadow
    protected IRecipePropertyStorage recipePropertyStorage;

    @Shadow
    protected static ItemStack copyItemStackWithCount(ItemStack itemStack, int count) {
        return null;
    }

    @Shadow
    protected static FluidStack copyFluidStackWithAmount(FluidStack fluidStack, int count) {
        return null;
    }

    @Unique
    private int labs$parallel = 0;

    @Redirect(method = "lambda$multiplyInputsAndOutputs$1",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/recipes/ingredients/GTRecipeInput;withAmount(I)Lgregtech/api/recipes/ingredients/GTRecipeInput;"))
    private static GTRecipeInput fixCopyingInputs(GTRecipeInput instance, int amount) {
        return instance.copyWithAmount(amount);
    }

    @Redirect(method = "lambda$multiplyInputsAndOutputs$2",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/recipes/ingredients/GTRecipeInput;withAmount(I)Lgregtech/api/recipes/ingredients/GTRecipeInput;"))
    private static GTRecipeInput fixCopyingFluidInputs(GTRecipeInput instance, int amount) {
        return instance.copyWithAmount(amount);
    }

    @Inject(method = "chancedOutputsMultiply", at = @At("HEAD"), cancellable = true)
    private void injectCustomParallelLogic(Recipe recipe, int parallel, CallbackInfo ci) {
        ci.cancel();

        labs$parallel = parallel;
        labs$handleChancedOutputList(recipe.getChancedOutputs(), parallel,
                (out) -> chancedOutput(out.getIngredient().copy(), out.getChance(), out.getChanceBoost()),
                this::chancedOutputLogic);
        labs$handleChancedOutputList(recipe.getChancedFluidOutputs(), parallel,
                (out) -> chancedFluidOutput(out.getIngredient().copy(), out.getChance(), out.getChanceBoost()),
                this::chancedFluidOutputLogic);
    }

    @WrapMethod(method = "build")
    private ValidationResult<Recipe> useParallelizedIfNeeded(Operation<ValidationResult<Recipe>> original) {
        if (labs$parallel <= 1 || !(chancedOutputLogic instanceof ParallelizedChancedOutputLogic ||
                chancedFluidOutputLogic instanceof ParallelizedChancedOutputLogic)) {
            return original.call();
        }

        // Use our custom parallel lists, depending which logic is parallel
        ChancedOutputList<ItemStack, ChancedItemOutput> itemList = chancedOutputLogic instanceof ParallelizedChancedOutputLogic parallel ?
                new ParallelizedChancedOutputList<>(parallel, chancedOutputs, labs$parallel,
                        (out, amt) -> new ChancedItemOutput(
                                copyItemStackWithCount(out.getIngredient(), out.getIngredient().getCount() * amt),
                                out.getChance(), out.getChanceBoost())) :
                new ChancedOutputList<>(chancedOutputLogic, chancedOutputs);

        ChancedOutputList<FluidStack, ChancedFluidOutput> fluidList = chancedFluidOutputLogic instanceof ParallelizedChancedOutputLogic parallel ?
                new ParallelizedChancedOutputList<>(parallel, chancedFluidOutputs, labs$parallel,
                        (out, amt) -> new ChancedFluidOutput(
                                copyFluidStackWithAmount(out.getIngredient(), out.getIngredient().amount * amt),
                                out.getChance(), out.getChanceBoost())) :
                new ChancedOutputList<>(chancedFluidOutputLogic, chancedFluidOutputs);

        return ValidationResult.newResult(finalizeAndValidate(), new Recipe(inputs, outputs,
                itemList, fluidInputs, fluidOutputs, fluidList,
                duration, EUt, hidden, isCTRecipe, recipePropertyStorage, category));
    }

    @Unique
    private <I,
            T extends ChancedOutput<I>> void labs$handleChancedOutputList(ChancedOutputList<I, T> listIn, int parallel,
                                                                          Consumer<T> addNew,
                                                                          Consumer<ChancedOutputLogic> changeLogic) {
        if (!(listIn.getChancedOutputLogic() == ChancedOutputLogic.NONE || listIn.getChancedEntries().isEmpty())) {
            if (parallel > 1 &&
                    ParallelizedChancedOutputLogic.normalToParallelized.containsKey(listIn.getChancedOutputLogic())) {
                // Immediately replace the logic in the builder with the parallel version
                // Although we can't guarantee the init of the chanced output list, replacing it now
                // Allows the throwing of the logic (if vanilla roll is called) to alert us that something is wrong
                changeLogic.accept(
                        ParallelizedChancedOutputLogic.normalToParallelized.get(listIn.getChancedOutputLogic()));
                for (T entry : listIn.getChancedEntries()) {
                    addNew.accept(entry);
                }
                return;
            }

            // Vanilla logic; plus actually changing the chanced output logic
            changeLogic.accept(listIn.getChancedOutputLogic());
            for (T entry : listIn.getChancedEntries()) {
                for (int i = 0; i < parallel; i++) {
                    addNew.accept(entry);
                }
            }
        }
    }

    @Unique
    @SuppressWarnings("unused")
    public R changeRecycling() {
        if (!RecyclingHelper.changeStackRecycling(outputs, inputs))
            recipeStatus = EnumValidationResult.INVALID;

        return (R) (Object) this;
    }

    @Unique
    public R inputNBT(IIngredient ingredient, NBTMatcher matcher, NBTCondition condition) {
        return inputNBT(ofGroovyIngredient(ingredient), matcher, condition);
    }

    @Unique
    @SuppressWarnings("unused")
    public R inputWildNBT(IIngredient ingredient) {
        return inputNBT(ingredient, NBTMatcher.ANY, NBTCondition.ANY);
    }

    @Unique
    @SuppressWarnings("unused")
    public R replace(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findByOutput(outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs,
                        (r) -> true),
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replaceInCategory(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findByOutput(outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs,
                        (r) -> Objects.equals(category, r.getRecipeCategory())),
                String.format("category: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", category,
                        outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replaceWithVoltage(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findRecipeByOutput(EUt, outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs),
                String.format("voltage: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", EUt, outputs,
                        fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replaceWithExactVoltage(RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findRecipeByOutput(EUt, outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs),
                String.format("exact voltage: %s, items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", EUt,
                        outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    @SuppressWarnings("unused")
    public R replace(Predicate<Recipe> canHandle, RecipeMap<?>... otherMaps) {
        return replaceForMaps(otherMaps, (map) -> removeOrWarn(map,
                ((AccessibleRecipeMap) map).findByOutput(outputs, fluidOutputs, chancedOutputs,
                        chancedFluidOutputs,
                        canHandle),
                String.format("items: %s, fluids: %s, chanced items: %s, chanced fluids: %s", outputs, fluidOutputs,
                        chancedOutputs, chancedFluidOutputs)));
    }

    @Unique
    private R replaceForMaps(RecipeMap<?>[] otherMaps, Consumer<RecipeMap<?>> remover) {
        remover.accept(recipeMap);
        Arrays.stream(otherMaps).forEach(remover);
        return (R) (Object) this;
    }

    @Unique
    private void removeOrWarn(RecipeMap<?> currMap, @Nullable List<Recipe> foundRecipes, String components) {
        if (foundRecipes == null) {
            if (LabsGroovyHelper.isRunningGroovyScripts()) {
                GroovyLog.msg("Error removing GregTech " + currMap.unlocalizedName + " recipe")
                        .add("could not find recipe for: " + components)
                        .error()
                        .post();
            }
            return;
        }

        for (var recipe : foundRecipes) {
            currMap.removeRecipe(recipe);
        }
    }
}
