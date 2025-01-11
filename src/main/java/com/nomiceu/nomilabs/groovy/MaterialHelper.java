package com.nomiceu.nomilabs.groovy;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.mixin.gregtech.FluidStorageKeyAccessor;
import com.nomiceu.nomilabs.mixin.gregtech.MetaItemsMixin;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.pipenet.block.material.IMaterialPipeType;
import gregtech.api.pipenet.block.material.ItemBlockMaterialPipe;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.ore.StoneType;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.blocks.MaterialItemBlock;
import gregtech.common.blocks.OreItemBlock;
import gregtech.common.pipelike.cable.Insulation;
import gregtech.common.pipelike.fluidpipe.FluidPipeType;
import gregtech.common.pipelike.itempipe.ItemPipeType;

@GroovyBlacklist
public class MaterialHelper {

    public static final int CAPACITY = 1000;

    private static Material material;
    private static Consumer<ItemStack> action;

    public static void forMaterialItem(Material material, Consumer<ItemStack> action, boolean inclBucket) {
        MaterialHelper.material = material;
        MaterialHelper.action = action;

        /* Normal Meta Items */
        forItems(MetaItemsMixin.getOrePrefixes(), MetaItem.class);

        /* Special Meta Items */

        // Wires and Cables (Fine Wire is a Normal Meta Item)
        forItemsInPipeEnum(Insulation.VALUES);

        // Fluid Pipes
        forItemsInPipeEnum(FluidPipeType.VALUES);

        // Item Pipes
        forItemsInPipeEnum(ItemPipeType.VALUES);

        // Compressed Blocks
        forItems(ImmutableList.of(OrePrefix.block, OrePrefix.frameGt), MaterialItemBlock.class);

        // Ores (Processing Intermediates, like Crushed and Crushed Purified, are Meta Items)
        StoneType.STONE_TYPE_REGISTRY.forEach((type) -> {
            if (type.shouldBeDroppedAsItem)
                forItems(type.processingPrefix, OreItemBlock.class);
        });

        /* Buckets */
        if (!material.hasFluid() || !inclBucket) return;
        FluidStorageKeyAccessor.getKeys().values().forEach((key) -> {
            var fluid = material.getFluid(key);
            if (fluid == null || ForgeModContainer.getInstance().universalBucket == null) return;

            var fluidStack = new FluidStack(fluid, CAPACITY);
            var itemStack = new ItemStack(ForgeModContainer.getInstance().universalBucket);
            itemStack.setTagCompound(fluidStack.writeToNBT(new NBTTagCompound()));
            action.accept(itemStack);
        });
    }

    private static void forItemsInPipeEnum(IMaterialPipeType<?>[] types) {
        Arrays.stream(types)
                .map(IMaterialPipeType::getOrePrefix)
                .forEach((prefix) -> forItems(prefix, ItemBlockMaterialPipe.class));
    }

    private static void forItems(List<OrePrefix> prefixes, Class<?> requiredClass) {
        for (var prefix : prefixes) {
            forItems(prefix, requiredClass);
        }
    }

    private static void forItems(OrePrefix prefix, Class<?> requiredClass) {
        var stacks = OreDictUnifier.getAll(new UnificationEntry(prefix, material));
        stacks.stream()
                .filter((stack) -> requiredClass.isInstance(stack.getItem()))
                .forEach(action);
    }

    public static void forMaterialFluid(Material material, Consumer<Fluid> action) {
        if (!material.hasFluid()) return;
        FluidStorageKeyAccessor.getKeys().values().forEach((key) -> {
            var fluid = material.getFluid(key);
            if (fluid != null) action.accept(fluid);
        });
    }
}
