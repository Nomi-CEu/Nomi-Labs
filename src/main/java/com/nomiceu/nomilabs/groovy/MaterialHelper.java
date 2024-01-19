package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.nomiceu.nomilabs.mixin.gregtech.FluidStorageKeyMixin;
import com.nomiceu.nomilabs.mixin.gregtech.MetaItemsMixin;
import gregtech.api.pipenet.block.material.IMaterialPipeType;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.pipelike.cable.Insulation;
import gregtech.common.pipelike.fluidpipe.FluidPipeType;
import gregtech.common.pipelike.itempipe.ItemPipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@GroovyBlacklist
public class MaterialHelper {
    public static final int CAPACITY = 1000;

    public static void forMaterialItem(Material material, Consumer<ItemStack> action) {
        List<OrePrefix> prefixes;

        /* Normal Meta Items */
        prefixes = new ArrayList<>(MetaItemsMixin.getOrePrefixes());

        /* Special Meta Items */

        // Wires and Cables (Fine Wire is a Normal Meta Item)
        addFromEnum(Insulation.VALUES, prefixes);

        // Fluid Pipes
        addFromEnum(FluidPipeType.VALUES, prefixes);

        // Item Pipes
        addFromEnum(ItemPipeType.VALUES, prefixes);

        /* Compressed Blocks and Frames */
        prefixes.add(OrePrefix.block);
        prefixes.add(OrePrefix.frameGt);

        prefixes.forEach((prefix) -> {
            var stack = OreDictUnifier.get(prefix, material);
            if (!stack.isEmpty()) action.accept(stack);
        });

        /* Buckets */
        if (!material.hasFluid()) return;
        FluidStorageKeyMixin.getKeys().values().forEach((key) -> {
            var fluid = material.getFluid(key);
            if (fluid == null || ForgeModContainer.getInstance().universalBucket == null) return;

            var fluidStack = new FluidStack(fluid, CAPACITY);
            var itemStack = new ItemStack(ForgeModContainer.getInstance().universalBucket);
            itemStack.setTagCompound(fluidStack.writeToNBT(new NBTTagCompound()));
            action.accept(itemStack);
        });
    }

    public static void forMaterialFluid(Material material, Consumer<Fluid> action) {
        if (!material.hasFluid()) return;
        FluidStorageKeyMixin.getKeys().values().forEach((key) -> {
            var fluid = material.getFluid(key);
            if (fluid != null) action.accept(fluid);
        });
    }

    @SuppressWarnings("SimplifyStreamApiCallChains")
    private static void addFromEnum(IMaterialPipeType<?>[] types, List<OrePrefix> list) {
        list.addAll(Arrays.stream(types)
                .map(IMaterialPipeType::getOrePrefix)
                .collect(Collectors.toList()));
    }
}
