package com.nomiceu.nomilabs.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
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
        setRarity(EnumRarity.COMMON);
        setMaxStackSize(64);
    }
    public BaseItem(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity) {
        setRegistryName(rl);
        setCreativeTab(tab);
        setRarity(rarity);
        setMaxStackSize(64);
    }
    public BaseItem(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize) {
        setRegistryName(rl);
        setCreativeTab(tab);
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
