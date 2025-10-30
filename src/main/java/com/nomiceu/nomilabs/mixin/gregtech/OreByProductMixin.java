package com.nomiceu.nomilabs.mixin.gregtech;

import static com.nomiceu.nomilabs.groovy.OreByProductChangeStorage.*;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.unification.material.Material;
import gregtech.integration.jei.basic.OreByProduct;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

@Mixin(value = OreByProduct.class, remap = false)
public class OreByProductMixin {

    @Shadow
    @Final
    private List<List<ItemStack>> inputs;

    @Shadow
    @Final
    private List<List<ItemStack>> outputs;

    @Shadow
    @Final
    private List<List<FluidStack>> fluidInputs;

    @Shadow
    @Final
    private Int2ObjectMap<ChancedItemOutput> chances;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void applyChanges(Material material, CallbackInfo ci) {
        labs$applyChanges(material, inputsChanges, inputs);
        labs$applyChanges(material, outputsChanges, outputs);
        labs$applyChanges(material, fluidInputsChanges, fluidInputs);

        // Chance: Special case, as it is not stored as a list
        if (!chanceChanges.containsKey(material)) return;

        for (SlotChange<ChancedItemOutput> change : chanceChanges.get(material)) {
            if (change.replacement() == null)
                chances.remove(change.slot());
            else
                chances.put(change.slot(), change.replacement());
        }
    }

    @Unique
    private static <T> void labs$applyChanges(Material material, Map<Material, List<SlotChange<T>>> changes,
                                              List<T> target) {
        if (!changes.containsKey(material)) return;

        for (SlotChange<T> change : changes.get(material)) {
            target.set(change.slot(), change.replacement());
        }
    }
}
