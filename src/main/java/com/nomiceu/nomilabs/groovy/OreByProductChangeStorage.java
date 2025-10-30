package com.nomiceu.nomilabs.groovy;

import java.util.*;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;

import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.unification.material.Material;

@GroovyBlacklist
public class OreByProductChangeStorage {

    /* Change Maps */
    public static final Map<Material, List<SlotChange<List<ItemStack>>>> inputsChanges = new HashMap<>();
    public static final Map<Material, List<SlotChange<List<ItemStack>>>> outputsChanges = new HashMap<>();
    public static final Map<Material, List<SlotChange<List<FluidStack>>>> fluidInputsChanges = new HashMap<>();
    public static final Map<Material, List<SlotChange<ChancedItemOutput>>> chanceChanges = new HashMap<>();

    public static void clear() {
        inputsChanges.clear();
        outputsChanges.clear();
        fluidInputsChanges.clear();
        chanceChanges.clear();
    }

    public static class SlotChange<T> {

        private final int slot;
        private final T replacement;

        public SlotChange(int slot, T replacement) {
            this.slot = slot;
            this.replacement = replacement;
        }

        public int slot() {
            return slot;
        }

        public T replacement() {
            return replacement;
        }
    }
}
