package com.nomiceu.nomilabs.mixin.gregtech;

import static gregtech.api.recipes.ModHandler.finalizeShapedRecipeInput;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.nomiceu.nomilabs.gregtech.mixinhelper.GTDisassemblingOreRecipe;

import gregtech.api.recipes.ModHandler;
import gregtech.loaders.recipe.handlers.MaterialRecipeHandler;

@Mixin(value = MaterialRecipeHandler.class, remap = false)
public class MaterialRecipeHandlerMixin {

    @Redirect(method = "processSmallDust",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/recipes/ModHandler;addShapedRecipe(Ljava/lang/String;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V"))
    private static void addDisassemblingRecipeSmall(String regName, ItemStack result, Object[] recipe) {
        addDisassemblingRecipe(regName, result, recipe);
    }

    @Redirect(method = "processTinyDust",
              at = @At(value = "INVOKE",
                       target = "Lgregtech/api/recipes/ModHandler;addShapedRecipe(Ljava/lang/String;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V"))
    private static void addDisassemblingRecipeTiny(String regName, ItemStack result, Object[] recipe) {
        addDisassemblingRecipe(regName, result, recipe);
    }

    @Unique
    private static void addDisassemblingRecipe(String regName, ItemStack result, Object[] recipe) {
        if (!regName.contains("_dust_disassembling_")) {
            ModHandler.addShapedRecipe(regName, result, recipe);
            return;
        }
        if (!AccessibleModHandler.validateRecipeWithOutput(regName, result, recipe)) return;
        IRecipe shapedOreRecipe = new GTDisassemblingOreRecipe(false, null, result.copy(),
                finalizeShapedRecipeInput(recipe))
                        .setMirrored(false)
                        .setRegistryName(regName);
        ForgeRegistries.RECIPES.register(shapedOreRecipe);
    }
}
