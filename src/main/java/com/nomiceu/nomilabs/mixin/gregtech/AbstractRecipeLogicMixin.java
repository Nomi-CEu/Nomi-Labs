package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleRecipeLogic;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.metatileentity.multiblock.ICleanroomProvider;
import gregtech.api.metatileentity.multiblock.ICleanroomReceiver;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;

@Mixin(value = AbstractRecipeLogic.class, remap = false)
public abstract class AbstractRecipeLogicMixin implements AccessibleRecipeLogic {

    @Shadow
    protected boolean isOutputsFull = false;

    @Unique
    protected boolean labs$recipeVoltageTooHigh = false;

    @Unique
    protected boolean labs$noEnergyToStart = false;

    @Unique
    protected boolean labs$requiresCleanroom = false;

    @Unique
    protected boolean labs$cleanroomDirty = false;

    @Unique
    protected boolean labs$wrongCleanroom = false;

    @Shadow
    protected abstract boolean hasNotifiedOutputs();

    @Shadow
    @Nullable
    public abstract RecipeMap<?> getRecipeMap();

    @Shadow
    protected abstract boolean hasEnoughPower(int @NotNull [] resultOverclock);

    @Override
    @Unique
    public boolean labs$outputFull() {
        return isOutputsFull && !hasNotifiedOutputs();
    }

    @Override
    @Unique
    public boolean labs$recipeVoltageTooHigh() {
        return labs$recipeVoltageTooHigh;
    }

    @Unique
    @Override
    public boolean labs$requiresCleanroom() {
        return labs$requiresCleanroom;
    }

    @Unique
    @Override
    public boolean labs$cleanroomDirty() {
        return labs$cleanroomDirty;
    }

    @Unique
    @Override
    public boolean labs$wrongCleanroom() {
        return labs$wrongCleanroom;
    }

    @Override
    @Unique
    public void labs$invalidate() {
        labs$recipeVoltageTooHigh = false;
        labs$noEnergyToStart = false;
    }

    @Redirect(method = "findRecipe",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/recipes/RecipeMap;findRecipe(JLnet/minecraftforge/items/IItemHandlerModifiable;Lgregtech/api/capability/IMultipleTankHandler;)Lgregtech/api/recipes/Recipe;"),
              require = 1)
    private Recipe findRecipeAndReportVoltage(RecipeMap<?> instance, long voltage, IItemHandlerModifiable inputs,
                                              IMultipleTankHandler fluidInputs) {
        labs$recipeVoltageTooHigh = false;

        List<ItemStack> items = GTUtility.itemHandlerToList(inputs).stream().filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        List<FluidStack> fluids = GTUtility.fluidHandlerToList(fluidInputs).stream()
                .filter(f -> f != null && f.amount != 0)
                .collect(Collectors.toList());

        // Shortcut
        // Hopefully won't break some recipes
        if (items.isEmpty() || fluids.isEmpty()) return null;

        Recipe recipe = Objects.requireNonNull(getRecipeMap()).find(items, fluids,
                (recipe1) -> recipe1.matches(false, items, fluids));

        if (recipe == null) return null;

        if (recipe.getEUt() > voltage) {
            // there is not enough voltage to consider the recipe valid
            labs$recipeVoltageTooHigh = true;
            return null;
        }

        return recipe;
    }

    @Redirect(method = "setupAndConsumeRecipeInputs(Lgregtech/api/recipes/Recipe;Lnet/minecraftforge/items/IItemHandlerModifiable;Lgregtech/api/capability/IMultipleTankHandler;)Z",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/capability/impl/AbstractRecipeLogic;hasEnoughPower([I)Z"),
              require = 1)
    private boolean reportHasEnoughPower(AbstractRecipeLogic instance, int[] resultOverclock) {
        labs$noEnergyToStart = false;
        if (!hasEnoughPower(resultOverclock)) {
            labs$noEnergyToStart = true;
            return false;
        }
        return true;
    }

    @Inject(method = "isHasNotEnoughEnergy", at = @At("RETURN"), cancellable = true)
    private void accountStarting(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || labs$noEnergyToStart);
    }

    @Inject(method = "checkCleanroomRequirement", at = @At("HEAD"))
    private void resetCleanroomInfo(Recipe recipe, CallbackInfoReturnable<Boolean> cir) {
        labs$requiresCleanroom = false;
        labs$cleanroomDirty = false;
        labs$wrongCleanroom = false;
    }

    @Redirect(method = "checkCleanroomRequirement",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/metatileentity/multiblock/ICleanroomReceiver;getCleanroom()Lgregtech/api/metatileentity/multiblock/ICleanroomProvider;"),
              require = 1)
    private ICleanroomProvider reportNeedsCleanroom(ICleanroomReceiver instance) {
        var result = instance.getCleanroom();
        if (result == null)
            labs$requiresCleanroom = true;
        return result;
    }

    @Redirect(method = "checkCleanroomRequirement",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/metatileentity/multiblock/ICleanroomProvider;isClean()Z"),
              require = 1)
    private boolean reportIsClean(ICleanroomProvider instance) {
        var result = instance.isClean();
        if (!result)
            labs$cleanroomDirty = true;
        return result;
    }

    @Redirect(method = "checkCleanroomRequirement",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/metatileentity/multiblock/ICleanroomProvider;checkCleanroomType(Lgregtech/api/metatileentity/multiblock/CleanroomType;)Z"),
              require = 1)
    private boolean reportWrongCleanroom(ICleanroomProvider instance, CleanroomType cleanroomType) {
        var result = instance.checkCleanroomType(cleanroomType);
        if (!result)
            labs$wrongCleanroom = true;
        return result;
    }
}
