package com.nomiceu.nomilabs.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

public class BaseItem extends Item {
    private IRarity rarity;
    public BaseItem(ResourceLocation rl, CreativeTabs tab) {
        setRegistryName(rl);
        setCreativeTab(tab);
    }
    public BaseItem(ResourceLocation rl, CreativeTabs tab, IRarity rarity) {
        setRegistryName(rl);
        setCreativeTab(tab);
        this.rarity = rarity;
    }
    public BaseItem(ResourceLocation rl, CreativeTabs tab, IRarity rarity, int stackSize) {
        setRegistryName(rl);
        setCreativeTab(tab);
        this.rarity = rarity;
        setMaxStackSize(stackSize);
    }

    public BaseItem setRarity(IRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }
}
