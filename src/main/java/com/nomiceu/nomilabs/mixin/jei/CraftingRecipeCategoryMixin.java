package com.nomiceu.nomilabs.mixin.jei;

import com.nomiceu.nomilabs.integration.jei.JEIPlugin;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = CraftingRecipeCategory.class, remap = false)
public class CraftingRecipeCategoryMixin {
    @Shadow
    @Final
    private static int craftOutputSlot;

    @Inject(method = "lambda$setRecipe$0", at = @At("HEAD"))
    private static void addRecipeOutputTooltip(ResourceLocation registryName, int slotIndex, boolean input,
                                               ItemStack stack, List<String> tooltip, CallbackInfo ci) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() || slotIndex != craftOutputSlot) return;
        tooltip.addAll(JEIPlugin.getRecipeOutputTooltip(stack, registryName));
    }
}
