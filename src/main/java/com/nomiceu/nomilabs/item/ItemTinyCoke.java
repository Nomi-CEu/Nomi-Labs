package com.nomiceu.nomilabs.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.util.LabsTranslate;

public class ItemTinyCoke extends ItemBase {

    public ItemTinyCoke(ResourceLocation rl, CreativeTabs tab) {
        super(rl, tab, EnumRarity.COMMON, 64, new LabsTranslate.Translatable("tooltip.nomilabs.tiny_fuels"));
    }

    @Override
    public int getItemBurnTime(@NotNull ItemStack stack) {
        return 200;
    }
}
