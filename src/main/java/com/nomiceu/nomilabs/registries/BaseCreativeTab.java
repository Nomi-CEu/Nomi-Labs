package com.nomiceu.nomilabs.registries;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BaseCreativeTab extends CreativeTabs {
    private final boolean hasSearchBar;
    private final Item icon;

    public BaseCreativeTab(String name, Item icon, boolean hasSearchBar) {
        super(name);
        this.icon = icon;
        this.hasSearchBar = hasSearchBar;

        if (hasSearchBar) {
            setBackgroundImageName("item_search.png");
        }
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(icon);
    }

    @Override
    public boolean hasSearchBar() {
        return hasSearchBar;
    }
}
