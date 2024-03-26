package com.nomiceu.nomilabs.mixin.jei;

import com.nomiceu.nomilabs.util.LabsTranslate;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.gui.recipes.RecipeCategoryTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Adds the Recipe Category ID to the tooltip of Recipe Categories.
 */
@Mixin(value = RecipeCategoryTab.class, remap = false)
public class RecipeCategoryTabMixin {
    @Shadow
    @Final
    private IRecipeCategory<?> category;

    @Inject(method = "getTooltip", at = @At("RETURN"))
    public void getRecipeCategoryName(CallbackInfoReturnable<List<String>> cir) {
        if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips || GuiScreen.isShiftKeyDown()) {
            cir.getReturnValue().add(LabsTranslate.translate("jei.tooltip.category.id", category.getUid()));
        }
    }
}
