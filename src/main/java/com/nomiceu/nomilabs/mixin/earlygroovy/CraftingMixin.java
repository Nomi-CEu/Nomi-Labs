package com.nomiceu.nomilabs.mixin.earlygroovy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.Crafting;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.groovy.mixinhelper.CraftingOutputCache;
import com.nomiceu.nomilabs.util.ItemMeta;

@Mixin(value = Crafting.class, remap = false)
public class CraftingMixin {

    @Inject(method = "removeByOutput(Lcom/cleanroommc/groovyscript/api/IIngredient;Z)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void useOutputCache(IIngredient output, boolean log, CallbackInfo ci) {
        if (LabsConfig.groovyScriptSettings.craftingOutputCacheMode ==
                LabsConfig.GroovyScriptSettings.CraftingOutputCacheMode.DISABLED)
            return;

        // noinspection ConstantValue
        if (IngredientHelper.isEmpty(output) || !((Object) output instanceof ItemStack stack)) return;

        ci.cancel();
        CraftingOutputCache.buildCache();
        var itemMeta = new ItemMeta(stack);

        if (!CraftingOutputCache.cache.containsKey(itemMeta)) {
            if (log) {
                GroovyLog.msg("Error removing Minecraft Crafting recipe")
                        .add("No recipes found for {}", output)
                        .error()
                        .post();
            }
            return;
        }

        for (ResourceLocation rl : CraftingOutputCache.cache.get(itemMeta)) {
            ReloadableRegistryManager.removeRegistryEntry(ForgeRegistries.RECIPES, rl);
        }
    }
}
