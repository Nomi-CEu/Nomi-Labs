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

import java.util.Arrays;
import java.util.List;

public class ItemBase extends Item {
    private IRarity rarity;
    private String[] description;
    public ItemBase(ResourceLocation rl, CreativeTabs tab) {
        initialise(rl, tab, EnumRarity.COMMON, 64);
    }
    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity) {
        initialise(rl, tab, rarity, 64);
    }
    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize) {
        initialise(rl, tab, rarity, stackSize);
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
        initialise(rl, tab, rarity, stackSize, description);
    }

    private void initialise(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize, String... description) {
        setRegistryName(rl);
        setCreativeTab(tab);
        setRarity(rarity);
        setMaxStackSize(stackSize);
        this.description = description;
    }

    public void setRarity(@NotNull IRarity rarity) {
        this.rarity = rarity;
    }

    @Override
    @NotNull
    public IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.addAll(Arrays.asList(description));
    }
}
