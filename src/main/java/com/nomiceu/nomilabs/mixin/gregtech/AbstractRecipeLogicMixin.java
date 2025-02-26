package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleAbstractRecipeLogic;

import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.metatileentity.MTETrait;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.boost.ChanceBoostFunction;
import gregtech.api.recipes.chance.output.ChancedOutput;
import gregtech.api.recipes.chance.output.ChancedOutputList;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;

/**
 * Allows accessing outputs/fluid outputs.
 */
@Mixin(value = AbstractRecipeLogic.class, remap = false)
public abstract class AbstractRecipeLogicMixin extends MTETrait implements AccessibleAbstractRecipeLogic {

    @Unique
    private static final String LABS_NON_CHANCED_ITEM_AMT_KEY = "labs$nonChancedItemAmt";

    @Unique
    private static final String LABS_CHANCED_ITEM_OUTPUTS_KEY = "labs$chancedItemOutputs";

    @Unique
    private static final String LABS_NON_CHANCED_FLUID_AMT_KEY = "labs$nonChancedFluidAmt";

    @Unique
    private static final String LABS_CHANCED_FLUID_OUTPUTS_KEY = "labs$chancedFluidOutputs";

    @Unique
    private static final String LABS_CHANCE_KEY = "chance";

    @Shadow
    protected NonNullList<ItemStack> itemOutputs;

    @Shadow
    protected List<FluidStack> fluidOutputs;

    @Shadow
    protected int recipeEUt;
    @Shadow
    protected int progressTime;

    @Shadow
    public abstract @Nullable RecipeMap<?> getRecipeMap();

    /**
     * List of non-chanced item outputs.The actual non-chanced item outputs are taken from the item outputs saved list,
     * taking the first n elements.
     */
    @Unique
    private int labs$nonChancedItemAmt = 0;

    /**
     * Map of chanced item outputs to their boosted chance, for this recipe.
     */
    @Unique
    private List<Pair<ItemStack, Integer>> labs$chancedItemOutputs = null;

    /**
     * Number of non-chanced fluid outputs. The actual non-chanced fluid outputs are taken from the fluid outputs saved
     * list, taking the first n elements.
     */
    @Unique
    private int labs$nonChancedFluidAmt = 0;

    /**
     * Map of chanced item outputs to their boosted chance, for this recipe.
     */
    @Unique
    private List<Pair<FluidStack, Integer>> labs$chancedFluidOutputs = null;

    /**
     * Default Ignored Constructor
     */
    private AbstractRecipeLogicMixin(@NotNull MetaTileEntity metaTileEntity) {
        super(metaTileEntity);
    }

    @Unique
    @Override
    public List<ItemStack> labs$getOutputs() {
        if (itemOutputs == null) {
            NomiLabs.LOGGER.error("Item Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return itemOutputs;
    }

    @Unique
    @Override
    public List<FluidStack> labs$getFluidOutputs() {
        if (fluidOutputs == null) {
            NomiLabs.LOGGER.error("Fluid Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return fluidOutputs;
    }

    @Unique
    @Override
    public int labs$getEUt() {
        return recipeEUt;
    }

    @Unique
    @Override
    public int labs$getNonChancedItemAmt() {
        return labs$nonChancedItemAmt;
    }

    @Unique
    @Override
    public List<Pair<ItemStack, Integer>> labs$getChancedItemOutputs() {
        if (labs$chancedItemOutputs == null) {
            NomiLabs.LOGGER.error("Chanced Item Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return labs$chancedItemOutputs;
    }

    @Unique
    @Override
    public int labs$getNonChancedFluidAmt() {
        return labs$nonChancedFluidAmt;
    }

    @Unique
    @Override
    public List<Pair<FluidStack, Integer>> labs$getChancedFluidOutputs() {
        if (labs$chancedFluidOutputs == null) {
            NomiLabs.LOGGER.error("Chanced Fluid Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return labs$chancedFluidOutputs;
    }

    @Inject(method = "completeRecipe", at = @At("TAIL"))
    private void clearLabsValues(CallbackInfo ci) {
        labs$nonChancedItemAmt = 0;
        labs$chancedItemOutputs = null;
        labs$nonChancedFluidAmt = 0;
        labs$chancedFluidOutputs = null;
    }

    @Inject(method = "setupRecipe", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void setupLabsValues(Recipe recipe, CallbackInfo ci, @Local(ordinal = 0) int recipeTier,
                                 @Local(ordinal = 1) int machineTier) {
        // At this point, recipe outputs are already trimmed (in prepareRecipe), so we can use the actual values
        labs$nonChancedItemAmt = recipe.getOutputs().size();
        labs$nonChancedFluidAmt = recipe.getFluidOutputs().size();

        labs$chancedItemOutputs = labs$fillChancedOutputsMap(recipe.getChancedOutputs(),
                getRecipeMap().getChanceFunction(),
                recipeTier, machineTier);
        labs$chancedFluidOutputs = labs$fillChancedOutputsMap(recipe.getChancedFluidOutputs(),
                getRecipeMap().getChanceFunction(),
                recipeTier, machineTier);
    }

    @Unique
    private <T> List<Pair<T, Integer>> labs$fillChancedOutputsMap(ChancedOutputList<T, ? extends ChancedOutput<T>> list,
                                                                  ChanceBoostFunction function, int recipeTier,
                                                                  int machineTier) {
        List<Pair<T, Integer>> result = new ArrayList<>();
        if (list.getChancedEntries().isEmpty()) return result;

        for (var entry : list.getChancedEntries()) {
            result.add(Pair.of(entry.getIngredient(), Math.min(ChancedOutputLogic.getMaxChancedValue(),
                    ChancedOutputLogic.getChance(entry, function, recipeTier, machineTier))));
        }
        return result;
    }

    @Inject(method = "deserializeNBT", at = @At("TAIL"))
    private void loadLabsValues(NBTTagCompound compound, CallbackInfo ci) {
        if (progressTime <= 0) return;

        labs$nonChancedItemAmt = compound.getInteger(LABS_NON_CHANCED_ITEM_AMT_KEY);
        labs$nonChancedFluidAmt = compound.getInteger(LABS_NON_CHANCED_FLUID_AMT_KEY);

        NBTTagList items = compound.getTagList(LABS_CHANCED_ITEM_OUTPUTS_KEY, Constants.NBT.TAG_COMPOUND);
        labs$chancedItemOutputs = new ArrayList<>();
        for (var item : items) {
            var tag = (NBTTagCompound) item;
            labs$chancedItemOutputs.add(Pair.of(new ItemStack(tag), tag.getInteger(LABS_CHANCE_KEY)));
        }

        NBTTagList fluids = compound.getTagList(LABS_CHANCED_FLUID_OUTPUTS_KEY, Constants.NBT.TAG_COMPOUND);
        labs$chancedFluidOutputs = new ArrayList<>();
        for (var fluid : fluids) {
            var tag = (NBTTagCompound) fluid;
            labs$chancedFluidOutputs
                    .add(Pair.of(FluidStack.loadFluidStackFromNBT(tag), tag.getInteger(LABS_CHANCE_KEY)));
        }
    }

    @Inject(method = "serializeNBT", at = @At("RETURN"))
    private void saveLabsValues(CallbackInfoReturnable<NBTTagCompound> cir) {
        if (progressTime <= 0) return;
        NBTTagCompound nbt = cir.getReturnValue();
        nbt.setInteger(LABS_NON_CHANCED_ITEM_AMT_KEY, labs$nonChancedItemAmt);
        nbt.setInteger(LABS_NON_CHANCED_FLUID_AMT_KEY, labs$nonChancedFluidAmt);

        labs$addChancedToTag(nbt, LABS_CHANCED_ITEM_OUTPUTS_KEY, labs$chancedItemOutputs,
                (entry) -> entry.getKey().writeToNBT(new NBTTagCompound()), Pair::getValue);
        labs$addChancedToTag(nbt, LABS_CHANCED_FLUID_OUTPUTS_KEY, labs$chancedFluidOutputs,
                (entry) -> entry.getKey().writeToNBT(new NBTTagCompound()), Pair::getValue);
    }

    @Unique
    private <T> void labs$addChancedToTag(NBTTagCompound nbt, String key, List<T> list,
                                          Function<T, NBTTagCompound> createTag, Function<T, Integer> getChance) {
        if (list == null) return;

        var entries = new NBTTagList();
        for (var entry : list) {
            NBTTagCompound tag = createTag.apply(entry);
            tag.setInteger(LABS_CHANCE_KEY, getChance.apply(entry));
            entries.appendTag(tag);
        }
        nbt.setTag(key, entries);
    }
}
