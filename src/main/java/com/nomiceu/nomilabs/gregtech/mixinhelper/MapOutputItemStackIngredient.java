package com.nomiceu.nomilabs.gregtech.mixinhelper;

import gregtech.api.recipes.map.MapItemStackIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Can't use MapItemStackIngredient as equals() always returns false, if gt recipe input is null.
 */
public class MapOutputItemStackIngredient extends MapItemStackIngredient {
    public MapOutputItemStackIngredient(ItemStack stack, int meta, NBTTagCompound tag) {
        super(stack, meta, tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        MapOutputItemStackIngredient i = (MapOutputItemStackIngredient) o;
        return stack.getItem() == i.stack.getItem() && meta == i.meta && Objects.equals(tag, i.tag);
    }

    @Override
    public String toString() {
        return "MapOutputItemStackIngredient{item=" + this.stack.getItem().getRegistryName() + "} {meta=" + this.meta + "} {tag=" + this.tag + "}";
    }
}
