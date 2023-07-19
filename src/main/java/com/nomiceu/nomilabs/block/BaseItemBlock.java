package com.nomiceu.nomilabs.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BaseItemBlock extends ItemBlock {
    private IRarity rarity;

    public BaseItemBlock(Block block, @NotNull IRarity rarity, int stackSize) {
        super(block);
        setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        setCreativeTab(block.getCreativeTab());
        setRarity(rarity);
        setMaxStackSize(stackSize);
    }

    public void setRarity(@NotNull IRarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }
}
