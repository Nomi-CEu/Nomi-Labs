package com.nomiceu.nomilabs.groovy;

import static com.nomiceu.nomilabs.groovy.OreByProductChangeStorage.*;

import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.nomiceu.nomilabs.util.LabsSide;

import gregtech.api.unification.material.Material;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Enums to provide easy access to change OreByProduct slots; by having constant slot indexes stored.
 */
@SuppressWarnings("unused")
public class OreByProductChanger {

    private static final int ORE_IDX = 0;

    public static void changeOre(Material mat, IIngredient ing) {
        OreByProductChange.addArrayInternal(ORE_IDX, mat, inputsChanges, ing.getMatchingStacks());
    }

    public enum Machines implements OreByProductChange {

        FURNACE(1),
        MACERATOR_CRUSH(2),
        MACERATOR_IMPURE(3),
        CENTRIFUGE_IMPURE(4),
        ORE_WASHER(5),
        THERMAL_CENTRIFUGE(6),
        MACERATOR_CENTRIFUGED(7),
        MACERATOR_PURIFIED(8),
        CENTRIFUGE_PURIFIED(9),
        CAULDRON_SIMPLE_WASHER(10),
        CAULDRON_IMPURE(11),
        CAULDRON_PURE(12),
        CHEMICAL_BATH(13),
        ELECTROMAGNETIC_SEPARATOR(14),
        SIFTER(15);

        private final int slot;

        Machines(int slot) {
            this.slot = slot;
        }

        @Override
        public int slot() {
            return slot;
        }

        public void change(Material mat, IIngredient ing) {
            addArray(mat, inputsChanges, ing.getMatchingStacks());
        }
    }

    public enum BasicProcessing implements OreByProductChange.ProcessingChange {

        SMElT_RESULT(0),
        CRUSHED(1),
        CRUSHED_BYPRODUCT(2),
        IMPURE_DUST(3),
        IMPURE_DUST_BYPRODUCT(4),
        IMPURE_CENTRIFUGE(5),
        IMPURE_CENTRIFUGE_BYPRODUCT(6),
        PURIFIED_WASHER(7),
        PURIFIED_WASHER_BYPRODUCT(8),
        CENTRIFUGED(9),
        CENTRIFUGED_BYPRODUCT(10),
        CENTRIFUGED_DUST(11),
        CENTRIFUGED_DUST_BYPRODUCT(12),
        PURIFIED_DUST(13),
        PURIFIED_DUST_BYPRODUCT(14),
        PURIFIED_CENTRIFUGE(15),
        PURIFIED_CENTRIFUGE_BYPRODUCT(16),
        CRUSHED_SIMPLE(17),
        PURIFIED_SIMPLE(18),
        IMPURE_SIMPLE(19),
        IMPURE_DUST_SIMPLE(20),
        PURE_SIMPLE(21),
        PURE_DUST_SIMPLE(22);

        private final int slot;

        BasicProcessing(int slot) {
            this.slot = slot;
        }

        @Override
        public int slot() {
            return slot;
        }
    }

    public enum AdvancedProcessing implements OreByProductChange.ProcessingChange {

        CHEMICAL_BATH(23),
        CHEMICAL_BATH_BYPRODUCT(24),
        ELECTROMAGNETIC_SEPARATOR(25),
        ELECTROMAGNETIC_SEPARATOR_BYPRODUCT_1(26),
        ELECTROMAGNETIC_SEPARATOR_BYPRODUCT_2(27),
        SIFTER_1(28),
        SIFTER_2(29),
        SIFTER_3(30),
        SIFTER_4(31),
        SIFTER_5(32),
        SIFTER_6(33);

        private final int slot;

        AdvancedProcessing(int slot) {
            this.slot = slot;
        }

        @Override
        public int slot() {
            return slot;
        }
    }

    public enum FluidInputs implements OreByProductChange {

        WASHER(0),
        CHEM_BATH(1);

        private final int slot;

        FluidInputs(int slot) {
            this.slot = slot;
        }

        @Override
        public int slot() {
            return slot;
        }

        public void change(Material mat, FluidStack ing) {
            if (!LabsSide.isClient()) return;

            fluidInputsChanges.computeIfAbsent(mat, _k -> new ObjectArrayList<>())
                    .add(new SlotChange<>(slot, Collections.singletonList(ing)));
        }

        public void change(Material mat, FluidStack... ing) {
            addArray(mat, fluidInputsChanges, ing);
        }

        public void change(Material mat, List<FluidStack> ing) {
            if (!LabsSide.isClient()) return;

            fluidInputsChanges.computeIfAbsent(mat, _k -> new ObjectArrayList<>())
                    .add(new SlotChange<>(slot, ing));
        }
    }
}
