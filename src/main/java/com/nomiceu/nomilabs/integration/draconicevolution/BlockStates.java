package com.nomiceu.nomilabs.integration.draconicevolution;

import java.util.Arrays;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Class allowing matching multiple block states.
 */
public class BlockStates {

    private final IBlockState defaultBlockState;
    private final IBlockState[] substitutes;
    private final boolean wildcard;

    private BlockStates(IBlockState defaultBlockState, IBlockState[] substitutes, boolean wildcard) {
        this.defaultBlockState = defaultBlockState;
        this.substitutes = substitutes;
        this.wildcard = wildcard;
    }

    @SuppressWarnings("unused")
    public static BlockStates of(IBlockState defaultBlockState) {
        return new BlockStates(defaultBlockState, new IBlockState[0], false);
    }

    @SuppressWarnings("unused")
    public static BlockStates of(IBlockState defaultBlockState, IBlockState... substitutes) {
        return new BlockStates(defaultBlockState, substitutes, false);
    }

    @SuppressWarnings("unused")
    public static BlockStates of(ItemStack defaultBlockState) {
        return new BlockStates(transformStackToState(defaultBlockState), new IBlockState[0], false);
    }

    @SuppressWarnings("unused")
    public static BlockStates of(ItemStack defaultBlockState, ItemStack... substitutes) {
        return new BlockStates(transformStackToState(defaultBlockState),
                Arrays.stream(substitutes).map(BlockStates::transformStackToState).toArray(IBlockState[]::new),
                false);
    }

    @SuppressWarnings("unused")
    public static BlockStates of(Block defaultBlock) {
        return new BlockStates(defaultBlock.getDefaultState(), new IBlockState[0], false);
    }

    @SuppressWarnings("unused")
    public static BlockStates of(Block defaultBlock, Block... substitutes) {
        return new BlockStates(defaultBlock.getDefaultState(),
                Arrays.stream(substitutes).map(Block::getDefaultState).toArray(IBlockState[]::new),
                false);
    }

    public static BlockStates wildcard() {
        return new BlockStates(null, new IBlockState[0], true);
    }

    public IBlockState getDefault() {
        return defaultBlockState;
    }

    public boolean hasSubstitutes() {
        return substitutes.length > 0;
    }

    public IBlockState[] getSubstitutes() {
        return substitutes;
    }

    public boolean isWildcard() {
        return wildcard;
    }

    /**
     * Returns an itemstack list, with the first item being the default, and then the substitutes.
     * Returns empty list if wildcard.
     */
    public ItemStack[] transformToStack() {
        if (wildcard) return new ItemStack[0];
        var stacks = new ItemStack[substitutes.length + 1]; // Substitues amount + default
        stacks[0] = transformStateToStack(defaultBlockState);
        for (int i = 0; i < substitutes.length; i++)
            stacks[i + 1] = transformStateToStack(substitutes[i]);
        return stacks;
    }

    public static ItemStack transformStateToStack(IBlockState state) {
        return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
    }

    @SuppressWarnings("deprecation")
    public static IBlockState transformStackToState(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlock block))
            throw new IllegalArgumentException("[LabsDraconicEvolution] Stack's Item must extend ItemBlock!");
        return block.getBlock().getStateFromMeta(stack.getMetadata());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockStates states))
            return false;
        return (states.isWildcard() && this.isWildcard()) || statesEqual(states.getDefault(), this.getDefault());
    }

    public static boolean statesEqual(IBlockState a, IBlockState b) {
        return Objects.equals(a.getBlock().getRegistryName(), b.getBlock().getRegistryName()) &&
                a.getBlock().getMetaFromState(a) == b.getBlock().getMetaFromState(b);
    }
}
