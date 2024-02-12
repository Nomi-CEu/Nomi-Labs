package com.nomiceu.nomilabs.mixin.groovyscript;

import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import mezz.jei.Internal;
import mezz.jei.ingredients.IngredientFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

// TODO Might Have to Remove this in GS Update, if they fix it there?
@Mixin(value = ReloadableRegistryManager.class, remap = false)
public class ReloadableRegistryManagerMixin {
    @Inject(method = "reloadJei", at = @At("TAIL"))
    private static void test(boolean msgPlayer, CallbackInfo ci) {
        // Fix: HEI Removals Disappearing on Reload
        // Reloads the Removed Ingredients (Actually removes them)
        // Must use Internal, no other way to get IngredientFilter
        // Reflection, method doesn't exist in JEI
        var filter = Internal.getIngredientFilter();
        try {
            IngredientFilter.class.getDeclaredMethod("block").invoke(filter);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {}
    }
}
