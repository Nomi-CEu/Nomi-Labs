package com.nomiceu.nomilabs.mixin.groovyscript;

import net.minecraft.item.crafting.IRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipeBuilder;
import com.cleanroommc.groovyscript.registry.AbstractCraftingRecipeBuilder;
import com.nomiceu.nomilabs.groovy.mixinhelper.RecipeTooltipAdder;
import com.nomiceu.nomilabs.groovy.mixinhelper.StrictableRecipe;
import com.nomiceu.nomilabs.util.LabsTranslate;

/**
 * Allows for recipes to be 'strict'. This means, in JEI, the list of 'matching stacks' will be displayed
 * exactly as set, instead of expanding wildcards and removing duplicates.
 * <p>
 * Also allows adding recipe input/output tooltips in JEI.
 */
@Mixin(value = CraftingRecipeBuilder.Shapeless.class, remap = false)
@SuppressWarnings("unused")
public abstract class ShapelessRecipeBuilderMixin extends AbstractCraftingRecipeBuilder.AbstractShapeless<IRecipe> {

    @Unique
    private boolean labs$isStrict = false;

    @Unique
    private LabsTranslate.Translatable[][] labs$inputTooltip = null;

    @Unique
    private LabsTranslate.Translatable[] labs$outputTooltip = null;

    /**
     * Default Ignored Constructor
     */
    private ShapelessRecipeBuilderMixin(int width, int height) {
        super(width, height);
    }

    /**
     * Makes recipes 'strict'. This means, in JEI, the list of 'matching stacks' will be displayed
     * exactly as set, instead of expanding wildcards and removing duplicates.
     */
    @Unique
    public CraftingRecipeBuilder.Shapeless strictJEIHandling() {
        labs$isStrict = true;
        return (CraftingRecipeBuilder.Shapeless) (Object) this;
    }

    @Unique
    public CraftingRecipeBuilder.Shapeless setOutputTooltip(LabsTranslate.Translatable... tooltip) {
        labs$outputTooltip = tooltip;
        return (CraftingRecipeBuilder.Shapeless) (Object) this;
    }

    @Unique
    public CraftingRecipeBuilder.Shapeless setInputTooltip(int slotIndex, LabsTranslate.Translatable... tooltip) {
        if (slotIndex < 0 || slotIndex > 8) {
            GroovyLog.get().error("Add Recipe Input Tooltip: Slot Index must be between 0 and 8!");
            return (CraftingRecipeBuilder.Shapeless) (Object) this;
        }

        if (labs$inputTooltip == null) labs$inputTooltip = new LabsTranslate.Translatable[9][0];

        labs$inputTooltip[slotIndex] = tooltip;
        return (CraftingRecipeBuilder.Shapeless) (Object) this;
    }

    @Inject(method = "register()Lnet/minecraft/item/crafting/IRecipe;", at = @At("RETURN"))
    private void setStrict(CallbackInfoReturnable<IRecipe> cir) {
        var val = cir.getReturnValue();
        if (!(val instanceof StrictableRecipe strict)) return;

        if (labs$isStrict) strict.labs$setStrict();
    }

    @Inject(method = "register()Lnet/minecraft/item/crafting/IRecipe;", at = @At("TAIL"))
    private void addRecipeTooltips(CallbackInfoReturnable<IRecipe> cir) {
        RecipeTooltipAdder.addTooltips(name, labs$inputTooltip, labs$outputTooltip);
    }
}
