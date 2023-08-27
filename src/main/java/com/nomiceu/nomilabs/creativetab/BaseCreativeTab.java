package com.nomiceu.nomilabs.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BaseCreativeTab extends CreativeTabs {
    private final boolean hasSearchBar;
    private final Supplier<Item> icon;

    public BaseCreativeTab(String name, Supplier<Item> icon, boolean hasSearchBar) {
        super(name);
        this.icon = icon;
        this.hasSearchBar = hasSearchBar;

        if (hasSearchBar) {
            setBackgroundImageName("item_search.png");
        }
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(icon.get());
    }

    @Override
    public boolean hasSearchBar() {
        return hasSearchBar;
    }
}
