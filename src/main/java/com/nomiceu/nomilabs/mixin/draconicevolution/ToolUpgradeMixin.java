package com.nomiceu.nomilabs.mixin.draconicevolution;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.brandon3055.draconicevolution.items.ToolUpgrade;

/**
 * Removes Fusion Recipes for Chaotic Upgrades, which are empty.
 */
@Mixin(value = ToolUpgrade.class, remap = false)
public class ToolUpgradeMixin {

    @Shadow
    @Final
    public static Map<String, Integer> NAME_MAX_LEVEL;

    /**
     * Inserts at the end of the method, where it overrides the previous value in the
     * NAME_MAX_LEVEL map.
     */
    @Inject(method = "registerUpgrade", at = @At("TAIL"))
    private static void registerUpgradeExceptChaotic(int id, String upgrade, int maxLevel, CallbackInfo ci) {
        NAME_MAX_LEVEL.put(upgrade, Math.min(3, maxLevel));
    }
}
