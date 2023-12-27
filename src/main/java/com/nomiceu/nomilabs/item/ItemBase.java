package com.nomiceu.nomilabs.item;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
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

import java.util.List;

public class ItemBase extends Item {
    private IRarity rarity;
    private List<LabsTooltipHelper.Tooltip> description;
    public ItemBase(ResourceLocation rl, CreativeTabs tab) {
        initialise(rl, tab, EnumRarity.COMMON, 64, ImmutableList.of());
    }
    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity) {
        initialise(rl, tab, rarity, 64, ImmutableList.of());
    }
    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize) {
        initialise(rl, tab, rarity, stackSize, ImmutableList.of());
    }
    /**
     * Makes an item.
     * @param rl Resource Location
     * @param tab Creative Tab
     * @param rarity Rarity
     * @param stackSize Max Stack Size
     * @param description Description. Map of translation keys to formatting keys. Is of string to string so we can use GTFormatCodes
     */
    public ItemBase(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize, List<LabsTooltipHelper.Tooltip> description) {
        initialise(rl, tab, rarity, stackSize, description);
    }

    private void initialise(ResourceLocation rl, CreativeTabs tab, @NotNull IRarity rarity, int stackSize, List<LabsTooltipHelper.Tooltip> description) {
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
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }

    /**
     * I18n formatting is done here instead of in constructor as I18n is client only
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        for (var text : description)
            tooltip.add(text.getFormattedString());
    }
}
