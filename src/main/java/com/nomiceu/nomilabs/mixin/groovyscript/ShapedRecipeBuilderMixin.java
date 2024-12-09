package com.nomiceu.nomilabs.mixin.groovyscript;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipeBuilder;
import com.cleanroommc.groovyscript.registry.AbstractCraftingRecipeBuilder;
import com.nomiceu.nomilabs.groovy.mixinhelper.RecipeTooltipAdder;
import com.nomiceu.nomilabs.groovy.mixinhelper.ShapedRecipeClassFunction;
import com.nomiceu.nomilabs.groovy.mixinhelper.ShapedRecipeClassFunctionSimplified;
import com.nomiceu.nomilabs.groovy.mixinhelper.StrictableRecipe;
import com.nomiceu.nomilabs.util.LabsTranslate;

import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;

/**
 * Allows for recipes to be 'strict'. This means, in JEI, the list of 'matching stacks' will be displayed
 * exactly as set, instead of expanding wildcards and removing duplicates.
 * <p>
 * Also allows setting custom shaped class, and adding recipe input/output tooltips in JEI.
 */
@Mixin(value = CraftingRecipeBuilder.Shaped.class, remap = false)
@SuppressWarnings("unused")
public abstract class ShapedRecipeBuilderMixin extends AbstractCraftingRecipeBuilder.AbstractShaped<IRecipe> {

    @Unique
    private ShapedRecipeClassFunction labs$recipeClassFunction = null;

    @Unique
    private boolean labs$isStrict = false;

    @Unique
    private LabsTranslate.Translatable[][] labs$inputTooltip = null;

    @Unique
    private LabsTranslate.Translatable[] labs$outputTooltip = null;

    /**
     * Default Ignored Constructor
     */
    public ShapedRecipeBuilderMixin(int width, int height) {
        super(width, height);
    }

    /**
     * Makes recipes 'strict'. This means, in JEI, the list of 'matching stacks' will be displayed
     * exactly as set, instead of expanding wildcards and removing duplicates.
     */
    @Unique
    public CraftingRecipeBuilder.Shaped strictJEIHandling() {
        labs$isStrict = true;
        return (CraftingRecipeBuilder.Shaped) (Object) this;
    }

    @Inject(method = "register()Lnet/minecraft/item/crafting/IRecipe;", at = @At("RETURN"))
    private void setStrict(CallbackInfoReturnable<IRecipe> cir) {
        var val = cir.getReturnValue();
        if (!(val instanceof StrictableRecipe strict)) return;

        if (labs$isStrict) strict.labs$setStrict();
    }

    @Unique
    public CraftingRecipeBuilder.Shaped recipeClassFunction(ShapedRecipeClassFunction recipeClassFunction) {
        this.labs$recipeClassFunction = recipeClassFunction;
        return (CraftingRecipeBuilder.Shaped) (Object) this;
    }

    @Unique
    public CraftingRecipeBuilder.Shaped recipeClassFunction(ShapedRecipeClassFunctionSimplified recipeClassFunction) {
        this.labs$recipeClassFunction = (output1, width1, height1, ingredients, _mirrored, _recipeFunction,
                                         _recipeAction) -> recipeClassFunction.createRecipe(output1, width1, height1,
                                                 ingredients);
        return (CraftingRecipeBuilder.Shaped) (Object) this;
    }

    @Unique
    public CraftingRecipeBuilder.Shaped setOutputTooltip(LabsTranslate.Translatable... tooltip) {
        labs$outputTooltip = tooltip;
        return (CraftingRecipeBuilder.Shaped) (Object) this;
    }

    @Unique
    public CraftingRecipeBuilder.Shaped setInputTooltip(int slotIndex, LabsTranslate.Translatable... tooltip) {
        if (slotIndex < 0 || slotIndex > 8) {
            GroovyLog.get().error("Add Recipe Input Tooltip: Slot Index must be between 0 and 8!");
            return (CraftingRecipeBuilder.Shaped) (Object) this;
        }

        if (labs$inputTooltip == null) labs$inputTooltip = new LabsTranslate.Translatable[9][0];

        labs$inputTooltip[slotIndex] = tooltip;
        return (CraftingRecipeBuilder.Shaped) (Object) this;
    }

    @Redirect(method = "register()Lnet/minecraft/item/crafting/IRecipe;",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/groovyscript/compat/vanilla/CraftingRecipeBuilder$Shaped;validateShape(Lcom/cleanroommc/groovyscript/api/GroovyLog$Msg;Ljava/util/List;[Ljava/lang/String;Lit/unimi/dsi/fastutil/chars/Char2ObjectOpenHashMap;Lcom/cleanroommc/groovyscript/registry/AbstractCraftingRecipeBuilder$IRecipeCreator;)Ljava/lang/Object;"),
              require = 1)
    public Object registerWithClassFunction1(CraftingRecipeBuilder.Shaped instance, GroovyLog.Msg msg,
                                             List<String> list, String[] strings,
                                             Char2ObjectOpenHashMap<IIngredient> char2ObjectOpenHashMap,
                                             IRecipeCreator<?> recipeCreator) {
        if (labs$recipeClassFunction == null)
            return validateShape(msg, list, strings, char2ObjectOpenHashMap, recipeCreator);
        return validateShape(msg, list, strings, char2ObjectOpenHashMap,
                (width1, height1, ingredients) -> labs$recipeClassFunction
                        .createRecipe(output, width1, height1, ingredients, mirrored, recipeFunction, recipeAction));
    }

    @Redirect(method = "register()Lnet/minecraft/item/crafting/IRecipe;",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/groovyscript/compat/vanilla/CraftingRecipeBuilder$Shaped;validateShape(Lcom/cleanroommc/groovyscript/api/GroovyLog$Msg;Ljava/util/List;Lcom/cleanroommc/groovyscript/registry/AbstractCraftingRecipeBuilder$IRecipeCreator;)Ljava/lang/Object;"),
              require = 1)
    public Object registerWithClassFunction2(CraftingRecipeBuilder.Shaped instance, GroovyLog.Msg msg,
                                             List<List<IIngredient>> list, IRecipeCreator<?> recipeCreator) {
        if (labs$recipeClassFunction == null) return validateShape(msg, list, recipeCreator);
        return validateShape(msg, list, (width1, height1, ingredients) -> labs$recipeClassFunction
                .createRecipe(output, width1, height1, ingredients, mirrored, recipeFunction, recipeAction));
    }

    @Inject(method = "register()Lnet/minecraft/item/crafting/IRecipe;", at = @At("TAIL"))
    private void addRecipeTooltips(CallbackInfoReturnable<IRecipe> cir) {
        RecipeTooltipAdder.addTooltips(name, output, labs$inputTooltip, labs$outputTooltip);
    }
}
