package com.nomiceu.nomilabs.mixin.groovyscript;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipeBuilder;
import com.nomiceu.nomilabs.groovy.mixinhelper.ShapedRecipeClassFunction;
import com.nomiceu.nomilabs.groovy.mixinhelper.ShapedRecipeClassFunctionSimplified;

import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;

@Mixin(value = CraftingRecipeBuilder.Shaped.class, remap = false)
@SuppressWarnings("unused")
public abstract class ShapedRecipeBuilderMixin extends CraftingRecipeBuilder {

    @Shadow
    protected boolean mirrored;

    @Unique
    private ShapedRecipeClassFunction recipeClassFunction = null;

    /**
     * Default Ignored Constructor
     */
    public ShapedRecipeBuilderMixin(int width, int height) {
        super(width, height);
    }

    @Unique
    public CraftingRecipeBuilder.Shaped recipeClassFunction(ShapedRecipeClassFunction recipeClassFunction) {
        this.recipeClassFunction = recipeClassFunction;
        return (CraftingRecipeBuilder.Shaped) (Object) this;
    }

    @Unique
    public CraftingRecipeBuilder.Shaped recipeClassFunction(ShapedRecipeClassFunctionSimplified recipeClassFunction) {
        this.recipeClassFunction = (output1, width1, height1, ingredients, _mirrored, _recipeFunction,
                                    _recipeAction) -> recipeClassFunction.createRecipe(output1, width1, height1,
                                            ingredients);
        return (CraftingRecipeBuilder.Shaped) (Object) this;
    }

    @Redirect(method = "register()Lnet/minecraft/item/crafting/IRecipe;",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/groovyscript/compat/vanilla/CraftingRecipeBuilder$Shaped;validateShape(Lcom/cleanroommc/groovyscript/api/GroovyLog$Msg;Ljava/util/List;[Ljava/lang/String;Lit/unimi/dsi/fastutil/chars/Char2ObjectOpenHashMap;Lcom/cleanroommc/groovyscript/compat/vanilla/CraftingRecipeBuilder$IRecipeCreator;)Ljava/lang/Object;"),
              require = 1)
    public Object registerWithClassFunction1(Shaped instance, GroovyLog.Msg msg, List<String> errors,
                                             String[] keyBasedMatrix, Char2ObjectOpenHashMap<IIngredient> keyMap,
                                             IRecipeCreator<?> recipeCreator) {
        if (recipeClassFunction == null) return validateShape(msg, errors, keyBasedMatrix, keyMap, recipeCreator);
        return validateShape(msg, errors, keyBasedMatrix, keyMap, (width1, height1, ingredients) -> recipeClassFunction
                .createRecipe(output, width1, height1, ingredients, mirrored, recipeFunction, recipeAction));
    }

    @Redirect(method = "register()Lnet/minecraft/item/crafting/IRecipe;",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/groovyscript/compat/vanilla/CraftingRecipeBuilder$Shaped;validateShape(Lcom/cleanroommc/groovyscript/api/GroovyLog$Msg;Ljava/util/List;Lcom/cleanroommc/groovyscript/compat/vanilla/CraftingRecipeBuilder$IRecipeCreator;)Ljava/lang/Object;"),
              require = 1)
    public Object registerWithClassFunction2(Shaped instance, GroovyLog.Msg msg,
                                             List<List<IIngredient>> ingredientMatrix,
                                             IRecipeCreator<?> recipeCreator) {
        if (recipeClassFunction == null) return validateShape(msg, ingredientMatrix, recipeCreator);
        return validateShape(msg, ingredientMatrix, (width1, height1, ingredients) -> recipeClassFunction
                .createRecipe(output, width1, height1, ingredients, mirrored, recipeFunction, recipeAction));
    }
}
