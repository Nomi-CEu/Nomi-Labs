package com.nomiceu.nomilabs.mixin.groovyscript;

import net.minecraft.item.crafting.IRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipeBuilder;
import com.nomiceu.nomilabs.groovy.mixinhelper.StrictableRecipe;

/**
 * Allows for recipes to be 'strict'. This means, in JEI, the list of 'matching stacks' will be displayed
 * exactly as set, instead of expanding wildcards and removing duplicates.
 */
@Mixin(value = CraftingRecipeBuilder.Shapeless.class, remap = false)
@SuppressWarnings("unused")
public class ShapelessRecipeBuilderMixin {

    @Unique
    private boolean labs$isStrict = false;

    /**
     * Makes recipes 'strict'. This means, in JEI, the list of 'matching stacks' will be displayed
     * exactly as set, instead of expanding wildcards and removing duplicates.
     */
    @Unique
    public CraftingRecipeBuilder.Shapeless strictJEIHandling() {
        labs$isStrict = true;
        return (CraftingRecipeBuilder.Shapeless) (Object) this;
    }

    @Inject(method = "register()Lnet/minecraft/item/crafting/IRecipe;", at = @At("RETURN"))
    private void setStrict(CallbackInfoReturnable<IRecipe> cir) {
        var val = cir.getReturnValue();
        if (!(val instanceof StrictableRecipe strict)) return;

        if (labs$isStrict) strict.labs$setStrict();
    }
}
