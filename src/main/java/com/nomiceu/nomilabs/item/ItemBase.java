package com.nomiceu.nomilabs.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ItemBase extends Item {
    private final IRarity rarity;
    private final String[] description;

    public ItemBase(ResourceLocation rl, CreativeTabs tab) {
        this(rl, tab, EnumRarity.COMMON, 64);
    }

    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity) {
        this(rl, tab, rarity, 64);
    }

    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize) {
        this(rl, tab, rarity, stackSize, new String[0]);
    }

    /**
     * Makes an item.
     * @param rl Resource Location
     * @param tab Creative Tab
     * @param rarity Rarity
     * @param stackSize Max Stack Size
     * @param description Description. Localized.
     */
    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize, String... description) {
        setRegistryName(rl);
        setCreativeTab(tab);
        setMaxStackSize(stackSize);
        this.rarity = rarity;
        this.description = description;
    }

    @Override
    @NotNull
    public IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        Collections.addAll(tooltip, description);
    }
}
