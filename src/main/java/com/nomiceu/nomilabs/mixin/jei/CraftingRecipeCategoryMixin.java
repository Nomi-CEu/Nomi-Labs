package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.integration.jei.LabsJEIPlugin;
import com.nomiceu.nomilabs.util.LabsSide;

import mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory;

/**
 * Allows adding Custom Recipe Input and Output Tooltips, and adds a new line before any recipe output tooltip
 * (including `Recipe
 * By <modid>`)
 */
@Mixin(value = CraftingRecipeCategory.class, remap = false)
public class CraftingRecipeCategoryMixin {

    @Shadow
    @Final
    private static int craftOutputSlot;

    @Shadow
    @Final
    private static int craftInputSlot1;

    @Inject(method = "lambda$setRecipe$0", at = @At("HEAD"))
    private static void addRecipeIngredientTooltips(ResourceLocation registryName, int slotIndex, boolean input,
                                                    ItemStack stack, List<String> tooltip, CallbackInfo ci) {
        if (LabsSide.isServer()) return;

        if (slotIndex != craftOutputSlot) {
            int index = slotIndex - craftInputSlot1;
            if (index < 0 || index > 8) return;

            var inputTooltip = LabsJEIPlugin.getRecipeInputTooltip(registryName, index);
            if (LabsConfig.modIntegration.addJEIIngEmptyLine && !inputTooltip.isEmpty())
                tooltip.add("");
            tooltip.addAll(inputTooltip);
            return;
        }

        boolean modIdDifferent = false;
        ResourceLocation itemRegistryName = stack.getItem().getRegistryName();
        if (itemRegistryName != null) {
            String itemModId = itemRegistryName.getNamespace();
            modIdDifferent = !registryName.getNamespace().equals(itemModId);
        }

        boolean showAdvanced = Minecraft.getMinecraft().gameSettings.advancedItemTooltips || GuiScreen.isShiftKeyDown();

        var outputTooltip = LabsJEIPlugin.getRecipeOutputTooltip(registryName);
        if (LabsConfig.modIntegration.addJEIIngEmptyLine &&
                (modIdDifferent || showAdvanced || !outputTooltip.isEmpty()))
            tooltip.add("");
        tooltip.addAll(outputTooltip);
    }
}
