package com.nomiceu.nomilabs.groovy;

import static com.nomiceu.nomilabs.groovy.OreByProductChangeStorage.chanceChanges;
import static com.nomiceu.nomilabs.groovy.OreByProductChangeStorage.outputsChanges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.nomiceu.nomilabs.mixin.gregtech.OreByProductAccessor;
import com.nomiceu.nomilabs.util.LabsSide;

import gregtech.api.recipes.chance.output.impl.ChancedItemOutput;
import gregtech.api.unification.material.Material;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public interface OreByProductChange {

    int slot();

    default <T> void addArray(Material mat, Map<Material, List<OreByProductChangeStorage.SlotChange<List<T>>>> target,
                              T[] arr) {
        addArrayInternal(slot(), mat, target, arr);
    }

    static <T> void addArrayInternal(int slot, Material mat,
                                     Map<Material, List<OreByProductChangeStorage.SlotChange<List<T>>>> target,
                                     T[] arr) {
        if (!LabsSide.isClient()) return;

        List<T> list = new ArrayList<>();
        Collections.addAll(list, arr);

        target.computeIfAbsent(mat, k -> new ObjectArrayList<>())
                .add(new OreByProductChangeStorage.SlotChange<>(slot, list));
    }

    interface ProcessingChange extends OreByProductChange {

        @SuppressWarnings("unused")
        default void change(Material mat, IIngredient ing) {
            addArray(mat, outputsChanges, ing.getMatchingStacks());
        }

        @SuppressWarnings("unused")
        default void changeChance(Material mat, int baseChance, int chanceBoost) {
            chanceChanges.computeIfAbsent(mat, _k -> new ObjectArrayList<>())
                    .add(new OreByProductChangeStorage.SlotChange<>(slot() + OreByProductAccessor.getNumInputs(),
                            new ChancedItemOutput(ItemStack.EMPTY, baseChance, chanceBoost)));
        }

        @SuppressWarnings("unused")
        default void removeChance(Material mat) {
            chanceChanges.computeIfAbsent(mat, _k -> new ObjectArrayList<>())
                    .add(new OreByProductChangeStorage.SlotChange<>(slot() + OreByProductAccessor.getNumInputs(),
                            null));
        }
    }
}
