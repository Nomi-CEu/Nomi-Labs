package com.nomiceu.nomilabs.mixin.jei;

import java.util.List;

import net.minecraft.util.text.TextFormatting;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;

/**
 * Trims newlines at the beginning of a page of descriptions.
 */
@Mixin(value = IngredientInfoRecipe.class, remap = false)
public class IngredientInfoRecipeMixin<T> {

    @Shadow
    @Mutable
    @Final
    private List<String> description;

    @Redirect(method = "create",
              at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
              require = 1)
    private static <E> boolean addRecipeIfValid(List<E> instance, E e) {
        if ((e instanceof IngredientInfoRecipe<?>ing) && !ing.getDescription().isEmpty())
            return instance.add(e);
        return true;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void trimDescription(IGuiHelper guiHelper, List<T> ingredients, IIngredientType<T> ingredientType,
                                 List<String> inDescription, CallbackInfo ci) {
        var toRemove = 0;
        for (var line : description) {
            if (removeFormattingAndTrim(line).isEmpty()) toRemove++;
            else break;
        }

        if (toRemove == description.size())
            description.clear();
        else if (toRemove != 0)
            description = description.subList(toRemove, description.size());
    }

    @Unique
    private static String removeFormattingAndTrim(String input) {
        var output = TextFormatting.getTextWithoutFormattingCodes(input.trim());
        if (output == null) return "";
        return output;
    }
}
