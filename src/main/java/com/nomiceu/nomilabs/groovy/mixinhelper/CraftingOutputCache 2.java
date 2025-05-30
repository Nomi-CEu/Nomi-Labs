package com.nomiceu.nomilabs.groovy.mixinhelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.util.ItemMeta;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class CraftingOutputCache {

    public static Map<ItemMeta, List<ResourceLocation>> cache = null;

    public static void buildCache() {
        if (cache != null) return;

        cache = new Object2ObjectOpenHashMap<>();

        long time = System.currentTimeMillis();
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            ItemStack output = recipe.getRecipeOutput();
            if (output.isEmpty()) continue;

            cache.computeIfAbsent(new ItemMeta(output), k -> new ArrayList<>())
                    .add(recipe.getRegistryName());
        }
        time = System.currentTimeMillis() - time;
        NomiLabs.LOGGER.info("[GrS] Building Crafting Output Cache took {}ms.", time);
    }
}
