package com.nomiceu.nomilabs.mixin.extrautils2;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableMap;
import com.rwtema.extrautils2.api.machine.IMachineRecipe;
import com.rwtema.extrautils2.api.machine.MachineSlotFluid;
import com.rwtema.extrautils2.api.machine.MachineSlotItem;
import com.rwtema.extrautils2.machine.TileMachine;

/**
 * Fixes XU2 Machine Recipes not consuming GT Tools Correctly, especially evident in the Enchanter.
 * <p>
 * This causes a dupe bug of GT Tools.
 */
@Mixin(value = IMachineRecipe.class, remap = false)
public interface IMachineRecipeMixin {

    /**
     * Overrides (most) XU2 Machine Recipes to return a empty map instead of a map of collections of items, populated
     * via {@link ForgeHooks#getContainerItem(ItemStack)}.
     * <p>
     * This is because ItemGTTool overrides that method, in order to damage, instead of consume, GT Tools in Crafting
     * Recipes.
     * <p>
     * See {@link TileMachine#consumeInputs()} for why this works. Essentially, if there is no container item map for
     * the specified slot, an Empty ItemStack is used instead.
     */
    // Interface cannot have private methods (Java 8)
    @Inject(method = "getContainerItems", at = @At("HEAD"), cancellable = true)
    default void getEmptyStacks(Map<MachineSlotItem, ItemStack> inputItems,
                                Map<MachineSlotFluid, FluidStack> inputFluids,
                                CallbackInfoReturnable<Map<MachineSlotItem, ItemStack>> cir) {
        cir.setReturnValue(ImmutableMap.of());
    }
}
